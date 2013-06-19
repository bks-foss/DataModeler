package com.isb.datamodeler.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.datatypes.EPredefinedDataType;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.tables.ETablesPackage;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerCopier extends EcoreUtil.Copier
{

		private static final long serialVersionUID = -3309809625103786850L;
		protected boolean _cancel;
		protected EProject _targetProject;		
		
		public EProject getTargetProject() {
			return _targetProject;
		}

		public void setTargetProject(EProject _targetProject) {
			this._targetProject = _targetProject;
		}

		public ESchema getTargetSchema() {
			return _targetSchema;
		}

		public void setTargetSchema(ESchema _targetSchema) {
			this._targetSchema = _targetSchema;
		}

		protected ESchema _targetSchema;

	    public <T> Collection<T> copyAll(Collection<? extends T> eObjects)
	    {    	
	    	Collection<T> result = new ArrayList<T>(eObjects.size());
			for (Object object : eObjects)
			{
				@SuppressWarnings("unchecked") T t = (T)copy((EObject)object);
				if(_cancel)return Collections.emptyList();
				result.add(t);
			}
			  
			copyReferences();
			
			return result;
	    }

		protected void copyReference(EReference eReference, EObject eObject, EObject copyEObject)
		{
			if (eObject.eIsSet(eReference))
			{
				if (eReference.isMany())
				{
					@SuppressWarnings("unchecked") InternalEList<EObject> source = (InternalEList<EObject>)eObject.eGet(eReference);
					@SuppressWarnings("unchecked") InternalEList<EObject> target = (InternalEList<EObject>)copyEObject.eGet(getTarget(eReference));
					if (source.isEmpty())
					{
						target.clear();
					}
					else
					{
						boolean isBidirectional = eReference.getEOpposite() != null;
						int index = 0;
						for (Iterator<EObject> k = resolveProxies ? source.iterator() : source.basicIterator(); k.hasNext();)
						{
							EObject referencedEObject = k.next();
							EObject copyReferencedEObject = get(referencedEObject);
							if (copyReferencedEObject == null)
							{
								if (useOriginalReferences && !isBidirectional)
								{
									target.addUnique(index, referencedEObject);
									++index;
								}
							}
							else
							{
								if (isBidirectional)
								{
									int position = target.indexOf(copyReferencedEObject);
									if (position == -1)
									{
										target.addUnique(index, copyReferencedEObject);
									}
									else if (index != position)
									{
										target.move(index, copyReferencedEObject);
									}
								}
								else
								{
									int position = target.indexOf(copyReferencedEObject);
									if (position == -1)
									{
										target.addUnique(index, copyReferencedEObject);
									}
								}
								++index;
							}
						}
					}
				}
				else
				{
					Object referencedEObject = eObject.eGet(eReference, resolveProxies);
					if (referencedEObject == null)
					{
						copyEObject.eSet(getTarget(eReference), null);
					}
					else
					{
						Object copyReferencedEObject = get(referencedEObject);
						
						if (copyReferencedEObject == null)
						{
							if (eReference.getFeatureID()==ETablesPackage.COLUMN__PRIMITIVE_TYPE)
					        {
								String sourceColumnPrimitiveTypeID = ((EColumn)eObject).getPrimitiveType().getId();
								
								for(EPrimitiveDataType primitiveType : _targetProject.getDatabase().getPrimitiveDataTypes())
								{
									if(primitiveType.getId().equals(sourceColumnPrimitiveTypeID))
									{
										((EColumn)copyEObject).setPrimitiveType(primitiveType);
										if(((EColumn)copyEObject).getContainedType() instanceof EPredefinedDataType)
										{
											((EPredefinedDataType)((EColumn)copyEObject).getContainedType()).setPrimitiveType(primitiveType);
										}
										return;
									}
								}
							}

							else if (useOriginalReferences && eReference.getEOpposite() == null)
							{
								if(referencedEObject instanceof EPrimitiveDataType)
								{
									for(EPrimitiveDataType primitiveType : _targetProject.getDatabase().getPrimitiveDataTypes())
									{
										if(primitiveType.getId().equals(((EPrimitiveDataType)referencedEObject).getId()))
										{
											copyEObject.eSet(getTarget(eReference), primitiveType);
											break;
										}
									}
								}
								else copyEObject.eSet(getTarget(eReference), referencedEObject);
							}
						}
						else
						{
							copyEObject.eSet(getTarget(eReference), copyReferencedEObject);
						}
					}
				}
			}
		}
		
	    protected void copyContainment(EReference eReference, EObject eObject, EObject copyEObject)
	    {
	    	
	        if (eObject.eIsSet(eReference))
	        {
	        	if (eReference.isMany())
	        	{
	        		@SuppressWarnings("unchecked") List<EObject> source = (List<EObject>)eObject.eGet(eReference);
	        		@SuppressWarnings("unchecked") List<EObject> target = (List<EObject>)copyEObject.eGet(getTarget(eReference));
	        		if (source.isEmpty())
	        		{
	        			target.clear();
	        		}
	        		else
	        		{
	    				if (eReference.getFeatureID()==ETablesPackage.BASE_TABLE__CONSTRAINTS &&
	    						(eObject instanceof ETable))
	    				{
	    					List<EObject> filteredSource = new ArrayList<EObject>();
	    					
	    					for(EObject constraint:source){
	    						// Antes de copiar la FK debemos asegurarnos que la tabla padre
								// tb se copia, sino no se copia la FK.
	    						if(!(constraint instanceof EForeignKey && ((EForeignKey)constraint).getParentTable()==null))
								{
	    							filteredSource.add(constraint);
								}
							}
	    					target.addAll(copyAll(filteredSource));
	    				}
	    				else
	    				{
	    					target.addAll(copyAll(source));
	    				}
	        		}
	        	}
	        	else
	        	{
	        		EObject childEObject = (EObject)eObject.eGet(eReference);
	        		copyEObject.eSet(getTarget(eReference), childEObject == null ? null : copy(childEObject));
	        	}
	        }
	    }
	}

