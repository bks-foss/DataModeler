package com.isb.datamodeler.compare.merge;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.merge.DefaultMerger;
import org.eclipse.emf.compare.diff.merge.EMFCompareEObjectCopier;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.isb.datamodeler.datatypes.ESQLDataType;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;

public abstract class DataModelerDefaultMerger extends DefaultMerger {

	/**
	 * Para que utilice el copier de específico datamodeler. 
	 * Que corrige cosas de EMF COMPARE
	 */
	@Override
	protected EObject copy(EObject eObject) {		
		final EMFCompareEObjectCopier copier = DataModelerMergeService.getCopier(diff);
		final EObject result = copier.copy(eObject);
		copier.copyReferences();
		copier.copyXMIIDs();
		return result;
	}
	
	public void applyInOrigin(DiffElement diff) {
		handleMutuallyDerivedReferences(diff);
		ensureXMIIDCopied(diff);
		removeFromContainer(diff);
	}
	
	public void undoInTarget(DiffElement diff) {
		handleMutuallyDerivedReferences(diff);
		ensureXMIIDCopied(diff);
		removeFromContainer(diff);
	}
	
	protected DiffModel getDiffModel(DiffElement diff) {
		EObject container = diff.eContainer();
		while (container != null) {
			if (container instanceof DiffModel)
				return (DiffModel)container;
			container = container.eContainer();
		}
		return null;
	}
	
	private void handleMutuallyDerivedReferences(DiffElement diff) {
		DiffElement toRemove = null;
		if (diff instanceof ReferenceChange) {
			final EReference reference = ((ReferenceChange)diff).getReference();
			if (reference == EcorePackage.eINSTANCE.getEClass_ESuperTypes()) {
				EObject referenceType = null;
				if (diff instanceof ReferenceChangeLeftTarget) {
					referenceType = ((ReferenceChangeLeftTarget)diff).getRightTarget();
				} else if (diff instanceof ReferenceChangeRightTarget) {
					referenceType = ((ReferenceChangeRightTarget)diff).getLeftTarget();
				} else if (diff instanceof UpdateReference) {
					referenceType = ((UpdateReference)diff).getLeftTarget();
				} else {
					// we did cover all the subclasses, we should have a RferenceOrderChange
				}
				for (final DiffElement siblingDiff : ((DiffGroup)diff.eContainer()).getSubDiffElements()) {
					if (siblingDiff instanceof ModelElementChangeLeftTarget) {
						if (((ModelElementChangeLeftTarget)siblingDiff).getLeftElement() instanceof EGenericType
								&& ((EGenericType)((ModelElementChangeLeftTarget)siblingDiff)
										.getLeftElement()).getEClassifier() == referenceType) {
							toRemove = siblingDiff;
							break;
						}
					} else if (siblingDiff instanceof ModelElementChangeRightTarget) {
						if (((ModelElementChangeRightTarget)siblingDiff).getRightElement() instanceof EGenericType
								&& ((EGenericType)((ModelElementChangeRightTarget)siblingDiff)
										.getRightElement()).getEClassifier() == referenceType) {
							toRemove = siblingDiff;
							break;
						}
					}
				}
			}
		} else if (diff instanceof ModelElementChangeLeftTarget
				&& ((ModelElementChangeLeftTarget)diff).getLeftElement() instanceof EGenericType) {
			final ModelElementChangeLeftTarget theDiff = (ModelElementChangeLeftTarget)diff;
			final EClassifier referenceType = ((EGenericType)theDiff.getLeftElement()).getEClassifier();
			for (final DiffElement siblingDiff : ((DiffGroup)diff.eContainer()).getSubDiffElements()) {
				if (siblingDiff instanceof ReferenceChangeLeftTarget
						&& ((ReferenceChangeLeftTarget)siblingDiff).getReference().getFeatureID() == EcorePackage.ECLASS__ESUPER_TYPES) {
					if (((ReferenceChangeLeftTarget)siblingDiff).getRightTarget() == referenceType) {
						toRemove = siblingDiff;
						break;
					}
				}
			}
		} else if (diff instanceof ModelElementChangeRightTarget
				&& ((ModelElementChangeRightTarget)diff).getRightElement() instanceof EGenericType) {
			final ModelElementChangeRightTarget theDiff = (ModelElementChangeRightTarget)diff;
			final EClassifier referenceType = ((EGenericType)theDiff.getRightElement()).getEClassifier();
			for (final DiffElement siblingDiff : ((DiffGroup)diff.eContainer()).getSubDiffElements()) {
				if (siblingDiff instanceof ReferenceChangeRightTarget
						&& ((ReferenceChangeRightTarget)siblingDiff).getReference().getFeatureID() == EcorePackage.ECLASS__ESUPER_TYPES) {
					if (((ReferenceChangeRightTarget)siblingDiff).getLeftTarget() == referenceType) {
						toRemove = siblingDiff;
						break;
					}
				}
			}
		}
		if (toRemove != null) {
			removeFromContainer(toRemove);
		}
	}
	
	/**
	 * Para que utilice el copier específico de DataModeler.
	 */
	@Override
	protected void ensureXMIIDCopied() {
		final EMFCompareEObjectCopier copier = DataModelerMergeService.getCopier(diff);
		copier.copyXMIIDs();
	}
	
	
	private void ensureXMIIDCopied(DiffElement diff) {
		final EMFCompareEObjectCopier copier = DataModelerMergeService.getCopier(diff);
		copier.copyXMIIDs();
	}

	
	/**
	 * Busca el indice donde insertar un elemento de una lista tras el merge
	 */
	protected int getIndex(EReference ref, EObject element, EObject origin)
	{
		int elementIndex = -1;
		if (ref.isMany()) 
		{
			//Buscamos el indice del elemento en la lista orgien para saber su
			//posicion en la lista destino
			Object containmentRefVal = element.eContainer().eGet(ref);
			if (containmentRefVal instanceof List) 
			{
				List listVal = (List)containmentRefVal;
				elementIndex = listVal.indexOf(element);
			}
			
			//Si es una columna lo que andamos mergeando, hay que tratarla de forma 
			//especial, ya que puede que no haya las mismas columnas en origen y 
			//en destino, por lo que hay que buscar su posicion exacta
			if(element instanceof EColumn && elementIndex != 0)
			{
				ETable originTable = (ETable)origin;
				
				int indexAux = elementIndex;
				
				while(indexAux!=0)
				{
					indexAux--;
					
					//obtenemos el nombre de la columna que hay encima de la actual para buscarla
					//en el destino del merge y asi saber donde colocarla.
					EColumn columAux = (EColumn)((List)containmentRefVal).get(indexAux);
					String columAuxName = columAux.getName();
					
					for(EColumn colum: originTable.getColumns())
						if(colum.getName().equals(columAuxName))
							return originTable.getColumns().indexOf(colum) + 1;
				}
				
				elementIndex = 0;
			}
		}

		return elementIndex;
	}
	
	/**
	 * Removes a {@link DiffElement} from its {@link DiffGroup}.
	 * 
	 * @param diffElement
	 *            {@link DiffElement} to remove from its container.
	 */
	protected void removeFromContainer(DiffElement diffElement) {
		
		if (diffElement instanceof UpdateAttribute && 
			((UpdateAttribute) diffElement).getLeftElement() instanceof ESQLDataType && 
			((UpdateAttribute) diffElement).getLeftElement() instanceof ESQLDataType)
				{
					EObject parent = diffElement.eContainer();
					EList<DiffElement> differences = ((DiffElement)(diffElement.eContainer())).getSubDiffElements();
					differences.clear();
				
					if (parent instanceof DiffGroup) {
						cleanDiffGroup((DiffGroup)parent);
					}					
		} else {
			final EObject parent = diffElement.eContainer();
			EcoreUtil.remove(diffElement);
			removeDanglingReferences(parent);
	
			// If diff was contained by a ConflictingDiffElement, we call back this on it
			if (parent instanceof ConflictingDiffElement) {
				removeFromContainer((DiffElement)parent);
			}
	
			// if diff was in a diffGroup and it was the last one, we also remove the diffgroup
			if (parent instanceof DiffGroup) {
				cleanDiffGroup((DiffGroup)parent);
			}
		}
	}

}
