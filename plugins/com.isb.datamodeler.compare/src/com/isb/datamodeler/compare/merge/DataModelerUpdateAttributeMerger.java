package com.isb.datamodeler.compare.merge;

import java.util.List;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.isb.datamodeler.datatypes.ESQLDataType;

public class DataModelerUpdateAttributeMerger extends DataModelerDefaultMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final UpdateAttribute theDiff = (UpdateAttribute)this.diff;
		final EObject element = theDiff.getRightElement();
		final EObject origin = theDiff.getLeftElement();
		final EAttribute attr = theDiff.getAttribute();
		
		if (element instanceof ESQLDataType) {
			List<EAttribute> attributes = origin.eClass().getEAllAttributes();
			for (EAttribute eAttribute : attributes) {
				try {
					// Bugzilla 21643 : FactoryException
					final EStructuralFeature feature = EFactory.eStructuralFeature(origin, eAttribute.getName());
					if (!feature.isChangeable())
						continue;
					EFactory.eSet(origin, eAttribute.getName(), EFactory.eGet(element, eAttribute.getName()));
				} catch (FactoryException e) {
					EMFComparePlugin.log(e, true);
				}
			}
		}
		else {
			try {
				EFactory.eSet(origin, attr.getName(), EFactory.eGet(element, attr.getName()));
			} catch (FactoryException e) {
				EMFComparePlugin.log(e, true);
			}
		}
		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final UpdateAttribute theDiff = (UpdateAttribute)this.diff;
		final EObject element = theDiff.getRightElement();
		final EObject origin = theDiff.getLeftElement();
		final EAttribute attr = theDiff.getAttribute();
		
		if (element instanceof ESQLDataType) {
			List<EAttribute> attributes = origin.eClass().getEAllAttributes();
			for (EAttribute eAttribute : attributes) {
				try {
					EFactory.eSet(origin, eAttribute.getName(), EFactory.eGet(element, eAttribute.getName()));
				} catch (FactoryException e) {
					EMFComparePlugin.log(e, true);
				}
			}
		}
		else {
		
			try {
				EFactory.eSet(element, attr.getName(), EFactory.eGet(origin, attr.getName()));
			} catch (FactoryException e) {
				EMFComparePlugin.log(e, true);
			}
		}
		super.undoInTarget();
	}
}

