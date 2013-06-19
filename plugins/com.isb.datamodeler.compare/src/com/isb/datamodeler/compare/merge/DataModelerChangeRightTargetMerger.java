package com.isb.datamodeler.compare.merge;

import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.isb.datamodeler.compare.core.ExtendModelUtils;

/**
 * Versión del merge por defecto para Datamodeler. Para tener en cuenta
 * los elementos que se borran del modelo también se borran de los diagramas.
 * 
 * @author Alfonso
 *
 */
public class DataModelerChangeRightTargetMerger extends DataModelerDefaultMerger{

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final ModelElementChangeRightTarget theDiff = (ModelElementChangeRightTarget)this.diff;
		final EObject origin = theDiff.getLeftParent();
		final EObject element = theDiff.getRightElement();
		final EObject newOne = copy(element);
		final EReference ref = element.eContainmentFeature();
		if (ref != null) {
			try {
				int elementIndex = getIndex(ref, element, origin);
				EFactory.eAdd(origin, ref.getName(), newOne, elementIndex);
				setXMIID(newOne, EcoreUtil.generateUUID()/*getXMIID(element)*/);
			} catch (final FactoryException e) {
				EMFComparePlugin.log(e, true);
			}
		} else if (origin == null && getDiffModel().getLeftRoots().size() > 0) {
			getDiffModel().getLeftRoots().get(0).eResource().getContents().add(newOne);
		} else if (origin != null) {
			origin.eResource().getContents().add(newOne);
		} else {
			// FIXME Throw exception : couldn't merge this
		}
		// we should now have a look for AddReferencesLinks needing this object
		final Iterator<EObject> siblings = getDiffModel().eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof ReferenceChangeRightTarget) {
				final ReferenceChangeRightTarget link = (ReferenceChangeRightTarget)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getLeftTarget() != null && link.getLeftTarget().equals(element)) {
					link.setRightTarget(newOne);
				}
			} else if (op instanceof ReferenceOrderChange) {
				final ReferenceOrderChange link = (ReferenceOrderChange)op;
				if (link.getReference().equals(ref)) {
					// FIXME respect ordering!
					link.getLeftTarget().add(newOne);
				}
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
		
		final ModelElementChangeRightTarget theDiff = (ModelElementChangeRightTarget)this.diff;
		final EObject element = theDiff.getRightElement();
		final EObject parent = theDiff.getRightElement().eContainer();
		
		// limpiamos los diagramas.
		ExtendModelUtils.fireViewDeleteCommands (element);		
		
		EcoreUtil.remove(element);
		// now removes all the dangling references
		removeDanglingReferences(parent);
		super.undoInTarget();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canUndoInTarget() {
		final ModelElementChangeRightTarget theDiff = (ModelElementChangeRightTarget)this.diff;
		final boolean isRightElementNotNull = theDiff.getRightElement() != null;
		return isRightElementNotNull;
	}
}
