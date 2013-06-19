package com.isb.datamodeler.compare.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.emf.compare.ui.viewer.content.part.diff.ParameterizedContentMergeDiffTab;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.swt.widgets.Composite;

import com.isb.datamodeler.compare.core.DataModelerCompareFilterContent;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerContentMergeDiffTab extends
		ParameterizedContentMergeDiffTab {
	
	
	public DataModelerContentMergeDiffTab(Composite parentComposite, int side,
			ModelContentMergeTabFolder parentFolder) {
		super(parentComposite, side, parentFolder);	
				
	}
	
	@Override
	public void setContentProvider(IContentProvider provider) {
		super.setContentProvider(getDataModelerContentProvider());
	}
	
	/**
	 * 
	 * @return
	 */
	 private IContentProvider getDataModelerContentProvider() {	 
		return new ModelContentMergeDiffTabContentProvider(AdapterUtils.getAdapterFactory());
	}
	 
	private static class ModelContentMergeDiffTabContentProvider extends AdapterFactoryContentProvider {
			
		 	/**
			 * Default constructor. Delegates to the super implementation.
			 * 
			 * @param factory
			 *            Factory to get labels and icons from.
			 */		 			 
			public ModelContentMergeDiffTabContentProvider(AdapterFactory factory) {
				super(factory);
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public Object[] getElements(Object object) {
				// overwritten to ensure contents of ResourceSets, List<Resource>, and Resource are correclty
				// returned.
				Object[] result = null;
				if (object instanceof ResourceSet) {
					final List<Resource> resources = ((ResourceSet)object).getResources();
					final List<Resource> elements = new ArrayList<Resource>(resources.size());
					for (final Resource resource : resources) {
						if (resource.getContents().isEmpty()
								|| !(resource.getContents().get(0) instanceof ComparisonSnapshot)) {
							elements.add(resource);
						}
					}
					result = elements.toArray();
				} else if (object instanceof EObject) {
					result = new Object[] {((EObject)object).eResource(), };
				} else if (object instanceof List) {
					// we may also display a list of resources
					result = ((List)object).toArray();
				} else if (object instanceof Resource) {
					// return contents of resource
					List<EObject> resultList = new ArrayList<EObject>();
					resultList.add(((Resource)object).getContents().get(0));
					result =  resultList.toArray();
				} else {
					result = super.getElements(object);
				}
				return result;
			}
			
			/*private Object[] checkElementsToView(Object object) {
				EList<EObject> lista = ((EObject)object).eContents();
				List<EObject> elementos =  new ArrayList<EObject>();
				for (EObject eObject : lista) {
					if (!DataModelerCompareFilterContent.getInstance().takeFilter(eObject))
						elementos.add(eObject);
				}
				return elementos.toArray(new Object [elementos.size()]);
			}*/

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
			 */
			@Override
			public Object[] getChildren(Object object) {
				List<EObject> visualElements = new ArrayList<EObject> ();
				if (object instanceof EProject){									
					//return ((EProject)object).getSchemas().toArray(new Object[visualElements.size()]);
					EList<EObject> lista = ((EProject) object).eContents();
					List<EObject> elementos =  new ArrayList<EObject>();
					for (EObject eObject : lista) {
						if (eObject instanceof ESchema && !DataModelerCompareFilterContent.getInstance().takeFilter(eObject))
							elementos.add(eObject);
					}
					//return ((ESchema)object).getTables().toArray(new Object[visualElements.size()]);																
					return elementos.toArray(new Object [elementos.size()]);
				}
				if (object instanceof ESchema) {
					
					EList<EObject> lista = ((ESchema) object).eContents();
					List<EObject> elementos =  new ArrayList<EObject>();
					for (EObject eObject : lista) {
						if (!DataModelerCompareFilterContent.getInstance().takeFilter(eObject))
							elementos.add(eObject);
					}
					//return ((ESchema)object).getTables().toArray(new Object[visualElements.size()]);																
					return elementos.toArray(new Object [elementos.size()]);
				}
				if (object instanceof ETable) {	
					EList<EObject> lista = ((ETable) object).eContents();
					List<EObject> elementos =  new ArrayList<EObject>();
					for (EObject eObject : lista) {
						if (!DataModelerCompareFilterContent.getInstance().takeFilter(eObject))
							elementos.add(eObject);
					}
																				
					return elementos.toArray(new Object [elementos.size()]);
					//return ((ETable) object).eContents().toArray(new Object [((ETable) object).eContents().size()]);															
				}
				
				if (object instanceof EColumn) {						
					return ((EColumn) object).eContents().toArray(new Object [((EColumn) object).eContents().size()]);															
				}
				if (object instanceof Resource) {
					EList<EObject> elementos = ((Resource)object).getContents();
					for (EObject element : elementos) {
						if (element instanceof EProject)
							visualElements.add(element);
						else if (element instanceof ESchema)
							visualElements.add(element);
						else if (element instanceof ETable)
							visualElements.add(element);
						else if (element instanceof EColumn)
							visualElements.add(element);
						else if (element instanceof EPrimaryKey)
							visualElements.add(element);
						else if (element instanceof EForeignKey)
							visualElements.add(element);
						else if (element instanceof EUniqueConstraint)
							visualElements.add(element);
						else if (element instanceof EPrimitiveDataType)
							visualElements.add(element);					
					}
					return visualElements.toArray(new Object[visualElements.size()]);
					
				}
				return visualElements.toArray(new Object[0]);
			}

			/**
			 *{@inheritDoc}
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#hasChildren(java.lang.Object)
			 */
			@Override
			public boolean hasChildren(Object object) {
				if (object instanceof Resource) {
					return ((Resource)object).getContents().size() > 0;
				}
				return super.hasChildren(object);
			}
		}
}
