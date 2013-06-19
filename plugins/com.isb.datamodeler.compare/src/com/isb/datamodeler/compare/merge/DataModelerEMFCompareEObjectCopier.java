package com.isb.datamodeler.compare.merge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.merge.EMFCompareEObjectCopier;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.project.impl.EProjectImpl;

/**
 * Esta clase se ha creado para solucionar el bug 20460, que al realizar un merge de FK, UK o PK
 * se produce una excepci�n en una clase interna de EMFCompare, El problema se encuentra, al buscar un 
 * elemento referenciado en el modelo destino que no se encuentra dentro del modelo de diferencias.
 * 
 * Tenemos que buscarlo nosotros en nuestro propio modelo destino. Es FEO FEO pero debido por ejemplo
 * al atributo primary key de la columna, que es un atributo calculado pero la propiedad seteable y al estar fuera de editingdomain
 * (... Eso implica que los trigger de Datamodeler no lo calculan ...) exista esta soluci�n.
 * EMFCompare no tiene en cuenta este tipos de casos y nos obliga a hacerlo en nuestros merges.
 * 
 * 
 * 
 * @author Alfonso
 *
 */
public class DataModelerEMFCompareEObjectCopier extends EMFCompareEObjectCopier{

	private DiffModel diffModel;
	private DiffResourceSet diffResourceSet;
	
	public DataModelerEMFCompareEObjectCopier(DiffModel diff) {
		super(diff);
		diffModel = getDiffModel();		
	}
	
	public DataModelerEMFCompareEObjectCopier(DiffResourceSet diff) {
		super(diff);
		diffResourceSet = getDiffResourceSet();		
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			
	@Override
	protected Object findReferencedObjectCopy(EObject referencedEObject) {
		Object copyReferencedObject = referencedEObject;
		if (referencedEObject.eResource() == null) {
			return findReferencedObjectCopyNullResource(referencedEObject);
		}

		// Is the referencedObject in either left or right?
		final Resource referencedResource = referencedEObject.eResource();
		final String uriFragment = referencedEObject.eResource().getURIFragment(referencedEObject);
		Resource leftResource = null;
		Resource rightResource = null;

		if (diffResourceSet != null) {
			final Iterator<DiffModel> diffModels = diffResourceSet.getDiffModels().iterator();
			while (diffModels.hasNext() && leftResource == null && rightResource == null) {
				final DiffModel aDiffModel = diffModels.next();
				DiffModel referencedDiffModel = null;
				if (!aDiffModel.getLeftRoots().isEmpty()
						&& aDiffModel.getLeftRoots().get(0).eResource() != null) {
					final Resource resource = aDiffModel.getLeftRoots().get(0).eResource();
					if (referencedEObject.eResource() == resource) {
						referencedDiffModel = aDiffModel;
					}
				}
				if (referencedDiffModel == null) {
					if (!aDiffModel.getRightRoots().isEmpty()
							&& aDiffModel.getRightRoots().get(0).eResource() != null) {
						final Resource resource = aDiffModel.getRightRoots().get(0).eResource();
						if (referencedEObject.eResource() == resource) {
							referencedDiffModel = aDiffModel;
						}
					}
				}
				if (referencedDiffModel != null) {
					leftResource = referencedDiffModel.getLeftRoots().get(0).eResource();
					rightResource = referencedDiffModel.getRightRoots().get(0).eResource();
				}
			}
		} else if (diffModel != null) {
			if (!diffModel.getLeftRoots().isEmpty() && diffModel.getLeftRoots().get(0).eResource() != null) {
				leftResource = diffModel.getLeftRoots().get(0).eResource();
			}
			if (!diffModel.getRightRoots().isEmpty() && diffModel.getRightRoots().get(0).eResource() != null) {
				rightResource = diffModel.getRightRoots().get(0).eResource();
			}
		}

		if (referencedResource == leftResource && rightResource != null) {
			/*
			 * FIXME we should be using the MatchModel, but can't access it. let's hope the referenced object
			 * has already been copied
			 */
			copyReferencedObject = rightResource.getEObject(uriFragment);
			if (copyReferencedObject == null) {
				// FIXME can we find the referenced object without the match model?
				if (referencedEObject instanceof EColumn)
					copyReferencedObject = getEColumnReference ((EColumn) referencedEObject, rightResource );
				if (referencedEObject instanceof EForeignKey)
					copyReferencedObject = getEForeignKeyReference((EForeignKey) referencedEObject, rightResource );
				if (referencedEObject instanceof EUniqueConstraint)
					copyReferencedObject = getEUniqueKeyReference((EUniqueConstraint) referencedEObject, rightResource );
				if (referencedEObject instanceof ETable)
					copyReferencedObject = getTableReference((ETable) referencedEObject, rightResource );
				if (referencedEObject instanceof EPrimitiveDataType)
					copyReferencedObject = getPrimitiveDataType((EPrimitiveDataType) referencedEObject, rightResource);
				if (referencedEObject instanceof EDatabase)
					copyReferencedObject = getEDataBase((EDatabase) referencedEObject, rightResource);
			}
		} else if (referencedResource == rightResource && leftResource != null) {
			/*
			 * FIXME we should be using the MatchModel, but can't access it. let's hope the referenced object
			 * has already been copied
			 */
			copyReferencedObject = leftResource.getEObject(uriFragment);
			if (copyReferencedObject == null) {
				if (referencedEObject instanceof EColumn)
					copyReferencedObject = getEColumnReference ((EColumn) referencedEObject, leftResource );
				if (referencedEObject instanceof EForeignKey)
					copyReferencedObject = getEForeignKeyReference((EForeignKey) referencedEObject, leftResource );
				if (referencedEObject instanceof EUniqueConstraint)
					copyReferencedObject = getEUniqueKeyReference((EUniqueConstraint) referencedEObject, leftResource );
				if (referencedEObject instanceof ETable)
					copyReferencedObject = getTableReference((ETable) referencedEObject, leftResource );
				if (referencedEObject instanceof EPrimitiveDataType)
					copyReferencedObject = getPrimitiveDataType((EPrimitiveDataType) referencedEObject, leftResource);
				if (referencedEObject instanceof EDatabase)
					copyReferencedObject = getEDataBase((EDatabase) referencedEObject, leftResource);
			}
		} else {
			// Reference lies in another resource. Simply return it as is.
		}
			
		return copyReferencedObject;
	}
	
	private Object getEDataBase(EDatabase referencedEObject,
			Resource resourceTarget) {
		EProjectImpl project = (EProjectImpl) resourceTarget.getContents().get(0);
		return project.getDatabase();
	}

	private Object getPrimitiveDataType(EPrimitiveDataType referencedEObject,
			Resource resourceTarget) {
		
		EProjectImpl project = (EProjectImpl) resourceTarget.getContents().get(0);
		EDatabase database = project.getDatabase();
		EList<EPrimitiveDataType> tipos = database.getPrimitiveDataTypes();
		for (EPrimitiveDataType ePrimitiveDataType : tipos) {
			if (ePrimitiveDataType.getName().equals(referencedEObject.getName()))
				return ePrimitiveDataType;
		}			
		
		return null;
	}

	private EObject getTableReference (ETable referencedEObject, Resource resourceTarget) {
		
		ESchema schemaOrigin = ((ETable) referencedEObject).getSchema();
		EProjectImpl dataBase = (EProjectImpl) resourceTarget.getContents().get(0);
		EList<ESchema> schemasTarget = dataBase.getSchemas();
		for (ESchema eSchema : schemasTarget) {
			if (eSchema.getName().equals(schemaOrigin.getName()))
			{
				EList<ETable> tablesTarget = eSchema.getTables();
				for (ETable eTable : tablesTarget) {
					if (eTable.getName().equals(referencedEObject.getName()))
					{
						return eTable;
					}	
				}
			}
		}
		return null;
	}
	
	
	private EObject getEForeignKeyReference (EForeignKey referencedEObject, Resource resourceTarget) {
				
		ETable tableOrigin = referencedEObject.getParentTable();
		ESchema schemaOrigin = ((ETable) tableOrigin).getSchema();
		EProjectImpl dataBase = (EProjectImpl) resourceTarget.getContents().get(0);
		EList<ESchema> schemasTarget = dataBase.getSchemas();
		for (ESchema eSchema : schemasTarget) {
			if (eSchema.getName().equals(schemaOrigin.getName()))
			{
				EList<ETable> tablesTarget = eSchema.getTables();
				for (ETable eTable : tablesTarget) {
					if (eTable.getName().equals(tableOrigin.getName()))
					{
						EList<EForeignKey> foreignKeys = ((EBaseTable)eTable).getForeignKeys();
						
						for (EForeignKey eForeignKey : foreignKeys) {
							if (eForeignKey.getName().equals(((EForeignKey)referencedEObject).getName()))
									return eForeignKey;
						}
					}								
					
				}
			}
		}
		return null;
	}
	
	private EObject getEUniqueKeyReference (EUniqueConstraint referencedEObject, Resource resourceTarget) {
		
		ETable tableOrigin = referencedEObject.getBaseTable();
		ESchema schemaOrigin = ((ETable) tableOrigin).getSchema();
		EProjectImpl dataBase = (EProjectImpl) resourceTarget.getContents().get(0);
		EList<ESchema> schemasTarget = dataBase.getSchemas();
		for (ESchema eSchema : schemasTarget) {
			if (eSchema.getName().equals(schemaOrigin.getName()))
			{
				EList<ETable> tablesTarget = eSchema.getTables();
				for (ETable eTable : tablesTarget) {
					if (eTable.getName().equals(tableOrigin.getName()))
					{
						EList<EUniqueConstraint> uniqueConstraint = ((EBaseTable)eTable).getUniqueConstraints();
						
						for (EUniqueConstraint eUniqueConstraint : uniqueConstraint) {
							if (eUniqueConstraint.getName().equals(((EUniqueConstraint)referencedEObject).getName()))
									return eUniqueConstraint;
						}
					}								
					
				}
			}
		}
		return null;
	}
	
	private EObject getEColumnReference (EColumn referencedEObject, Resource resourceTarget) {
		
		ETable tableOrigin = ((EColumn)referencedEObject).getTable();
		ESchema schemaOrigin = ((ETable) tableOrigin).getSchema();
		EProjectImpl dataBase = (EProjectImpl) resourceTarget.getContents().get(0);
		EList<ESchema> schemasTarget = dataBase.getSchemas();
		for (ESchema eSchema : schemasTarget) {
			if (eSchema.getName().equals(schemaOrigin.getName()))
			{
				EList<ETable> tablesTarget = eSchema.getTables();
				for (ETable eTable : tablesTarget) {
					if (eTable.getName().equals(tableOrigin.getName()))
					{
						EList<EColumn> columnTarget = eTable.getColumns();
						for (EColumn eColumn : columnTarget) {
							if (eColumn.getName().equals(((EColumn)referencedEObject).getName()))
									return eColumn;
						}
					}								
					
				}
			}
		}
		return null;
	}
	
 @SuppressWarnings("unchecked")
 protected void copyReference(EReference eReference, EObject eObject,
		EObject copyEObject) {
	// No use trying to copy the reference if it isn't set in the origin
		if (!eObject.eIsSet(eReference)) {
			return;
		}
		final Object referencedEObject = eObject.eGet(eReference, resolveProxies);
		if (eReference == EcorePackage.eINSTANCE.getEPackage_EFactoryInstance()) {
			// Let the super do its work.
			super.copyReference(eReference, eObject, copyEObject);
		} else if (eReference.isMany()) {
			final List<?> referencedObjectsList = (List<?>)referencedEObject;
			if (referencedObjectsList == null) {
				copyEObject.eSet(getTarget(eReference), null);
			} else if (referencedObjectsList.size() == 0) {
				copyEObject.eSet(getTarget(eReference), referencedObjectsList);
			} else {
				
				Iterator<?> it = referencedObjectsList.iterator();						
				while (it.hasNext()) {
					Object referencedEObj = it.next();
					final Object copyReferencedEObject = get(referencedEObj);
					if (copyReferencedEObject != null) {
						// The referenced object has been copied via this Copier
						((List<Object>)copyEObject.eGet(getTarget(eReference))).add(copyReferencedEObject);
					} else if (mergeLinkedDiff((EObject)referencedEObj)) {
						// referenced object was an unmatched one and we managed to merge its corresponding
						// diff
						((List<Object>)copyEObject.eGet(getTarget(eReference))).add(get(referencedEObj));
						// else => don't take any action, this has already been handled
					} else if (referencedEObj instanceof EObject) {
						// referenced object lies in another resource, simply reference it
						final Object copyReferencedObject = findReferencedObjectCopy((EObject)referencedEObj);
						if (copyReferencedObject != null)
							((List<Object>)copyEObject.eGet(getTarget(eReference))).add(copyReferencedObject);
					} else {
						((List<Object>)copyEObject.eGet(getTarget(eReference))).add(referencedEObj);
					}
				}
			}
		} else {
			if (referencedEObject == null) {
				copyEObject.eSet(getTarget(eReference), null);
			} else {
				final Object copyReferencedEObject = get(referencedEObject);
				if (copyReferencedEObject != null) {
					// The referenced object has been copied via this Copier
					copyEObject.eSet(getTarget(eReference), copyReferencedEObject);
				} else if (mergeLinkedDiff((EObject)referencedEObject)) {
					// referenced object was an unmatched one and we managed to merge its corresponding diff
					copyEObject.eSet(getTarget(eReference), get(referencedEObject));
				} else if (referencedEObject instanceof EObject) {
					final Object copyReferencedObject = findReferencedObjectCopy((EObject)referencedEObject);
					if (copyReferencedObject != null)
						copyEObject.eSet(getTarget(eReference), copyReferencedObject);
					// Bugzilla 22026. No mergea bien las FK sin información de las Parent Table
					else if (copyEObject instanceof EForeignKey && referencedEObject instanceof EPersistentTable &&
							eReference.equals(EConstraintsPackage.eINSTANCE.getForeignKey_ParentTable()) ) {
												
						DataModelerMergeMessages.getInstance().addElement((EForeignKey)copyEObject, (EPersistentTable)referencedEObject);						
					}
				} else {
					((List<Object>)copyEObject.eGet(getTarget(eReference))).add(referencedEObject);
				}
			}
		}
}
 

	public void copyReferences() {
		final Set<Map.Entry<EObject, EObject>> entrySetCopy = new HashSet<Map.Entry<EObject, EObject>>(
				entrySet());
		
		for (final Map.Entry<EObject, EObject> entry : entrySetCopy) {
			final EObject eObject = entry.getKey();
			final EObject copyEObject = entry.getValue();
			final EClass eClass = eObject.eClass();
			for (int j = 0; j < eClass.getFeatureCount(); ++j) {
				final EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(j);
				if (eStructuralFeature.isChangeable() && !eStructuralFeature.isDerived()) {
					if (eStructuralFeature instanceof EReference) {
						final EReference eReference = (EReference)eStructuralFeature;
						if (!eReference.isContainment() && !eReference.isContainer()) {
							copyReference(eReference, eObject, copyEObject);
						}
					} else if (FeatureMapUtil.isFeatureMap(eStructuralFeature)) {
						copyFeatureMap(eObject, eStructuralFeature);
					}
				}
			}
		}
		
		//
//		if (notMergedElements.size() > 0 ) {
//			Iterator it = notMergedElements.entrySet().iterator();
//			String message = "No se puede completar el merge en los siguientes casos:";
//			while (it.hasNext()) {
//				message += "\n";
//				Map.Entry e = (Map.Entry)it.next();
//				System.out.println(e.getKey() + " " + e.getValue());
//				message += "La siguiente FK : '" + ((EForeignKey)e.getKey()).getName() + "' Necesita mergear antes la tabla: '"  + 
//					((EPersistentTable) e.getValue()).getName() + "' del esquema '" + ((EPersistentTable) e.getValue()).getSchema().getName() + "'"; 
//			}
//			
//			MessageDialog.openWarning(null, Messages.bind("datamodeler.merge.FK.problemsToMerge.title"), 
//					message); 
//		}
	}
	
	/**
	 * Copies the feature as a FeatureMap.
	 * 
	 * @param eObject
	 *            The EObject from which the feature is copied
	 * @param eStructuralFeature
	 *            The feature which needs to be copied.
	 */
	private void copyFeatureMap(EObject eObject, EStructuralFeature eStructuralFeature) {
		final FeatureMap featureMap = (FeatureMap)eObject.eGet(eStructuralFeature);
		final FeatureMap copyFeatureMap = (FeatureMap)get(eObject).eGet(getTarget(eStructuralFeature));
		int copyFeatureMapSize = copyFeatureMap.size();
		for (int k = 0; k < featureMap.size(); ++k) {
			final EStructuralFeature feature = featureMap.getEStructuralFeature(k);
			if (feature instanceof EReference) {
				final Object referencedEObject = featureMap.getValue(k);
				Object copyReferencedEObject = get(referencedEObject);
				if (copyReferencedEObject == null && referencedEObject != null) {
					final EReference reference = (EReference)feature;
					if (!useOriginalReferences || reference.isContainment()
							|| reference.getEOpposite() != null) {
						continue;
					}
					copyReferencedEObject = referencedEObject;
				}
				// If we can't add it, it must already be in the list so find it and move it
				// to the end.
				//
				if (!copyFeatureMap.add(feature, copyReferencedEObject)) {
					for (int l = 0; l < copyFeatureMapSize; ++l) {
						if (copyFeatureMap.getEStructuralFeature(l) == feature
								&& copyFeatureMap.getValue(l) == copyReferencedEObject) {
							copyFeatureMap.move(copyFeatureMap.size() - 1, l);
							--copyFeatureMapSize;
							break;
						}
					}
				}
			} else {
				copyFeatureMap.add(featureMap.get(k));
			}
		}
	}
 
 private boolean mergeLinkedDiff(EObject element) {
		boolean hasMerged = false;
		final TreeIterator<EObject> diffIterator;
		if (diffResourceSet != null) {
			diffIterator = diffResourceSet.eAllContents();
		} else {
			diffIterator = diffModel.eAllContents();
		}

		while (diffIterator.hasNext()) {
			final EObject next = diffIterator.next();
			if (next instanceof ModelElementChangeLeftTarget) {
				if (((ModelElementChangeLeftTarget)next).getLeftElement() == element) {
					MergeService.merge((ModelElementChangeLeftTarget)next, true);
					hasMerged = true;
					break;
				}
			} else if (next instanceof ModelElementChangeRightTarget) {
				if (((ModelElementChangeRightTarget)next).getRightElement() == element) {
					MergeService.merge((ModelElementChangeRightTarget)next, false);
					hasMerged = true;
					break;
				}
			}
		}
		return hasMerged;
	}
 
}
