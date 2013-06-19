package com.isb.datamodeler.compare.merge;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.ecore.EObject;

class  DataModelerMergeService {
	
	private static DataModelerEMFCompareEObjectCopier copier;
	
	public static DataModelerEMFCompareEObjectCopier getCopier(DiffElement diff) {
		final DiffModel diffModel = getContainerDiffModel(diff);
		if (diffModel == null)
			throw new IllegalArgumentException("The diff element should be contained in a DiffModel instance"); //$NON-NLS-1$
		if (diffModel.eContainer() instanceof DiffResourceSet) {
			if (copier == null) {
				copier = new DataModelerEMFCompareEObjectCopier((DiffResourceSet)diffModel.eContainer());
			} else if (copier.getDiffResourceSet() != diffModel.eContainer()) {
				copier.clear();
				copier = new DataModelerEMFCompareEObjectCopier((DiffResourceSet)diffModel.eContainer());
			}
		} else {
			if (copier == null) {
				copier = new DataModelerEMFCompareEObjectCopier(diffModel);
			} else if (copier.getDiffModel() != diffModel) {
				copier.clear();
				copier = new DataModelerEMFCompareEObjectCopier(diffModel);
			}
		}
		return copier;
	}
	
	private static DiffModel getContainerDiffModel(DiffElement diff) {
		EObject container = diff.eContainer();
		while (container != null) {
			if (container instanceof DiffModel)
				return (DiffModel)container;
			container = container.eContainer();
		}
		return null;
	}
}
