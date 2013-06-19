package com.isb.datamodeler.compare.merge;

import java.util.Iterator;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.isb.datamodeler.compare.core.ExtendModelUtils;

/**
 * Merger for an {@link ModelElementChangeLeftTarget} operation.<br/>
 * Esta versión está pensada para DATAMODELER.
 * <p>
 * Are considered for this merger :
 * <ul>
 * <li>AddModelElement</li>
 * <li>RemoteRemoveModelElement</li> Aquí se tiene en cuenta si existe en los diagramas...
 * </ul>
 * </p>
 * 
 */
public class DataModelerChangeLeftTargetMerger extends DataModelerDefaultMerger {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final ModelElementChangeLeftTarget theDiff = (ModelElementChangeLeftTarget)this.diff;
		final EObject element = theDiff.getLeftElement();
		final EObject parent = theDiff.getLeftElement().eContainer();
		
		// limpiamos los diagramas.
		ExtendModelUtils.fireViewDeleteCommands (element);
		
		// borrado del elemento.
		EcoreUtil.remove(element);		
							
		// now removes all the dangling references
		removeDanglingReferences(parent);
		
		//DeleteElementBridge.getInstance().addLeftElementToRemove(element);
		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final ModelElementChangeLeftTarget theDiff = (ModelElementChangeLeftTarget)this.diff;
		// we should copy the element to the Origin one.
		final EObject origin = theDiff.getRightParent();
		final EObject element = theDiff.getLeftElement();
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
		} else if (origin == null && getDiffModel().getRightRoots().size() > 0) {
			getDiffModel().getRightRoots().get(0).eResource().getContents().add(newOne);
		} else if (origin != null) {
			origin.eResource().getContents().add(newOne);
		} else {
			// FIXME throw exception : couldn't merge this
		}
		// we should now have a look for RemovedReferencesLinks needing elements to apply
		final Iterator<EObject> siblings = getDiffModel().eAllContents();
		while (siblings.hasNext()) {
			final Object op = siblings.next();
			if (op instanceof ReferenceChangeLeftTarget) {
				final ReferenceChangeLeftTarget link = (ReferenceChangeLeftTarget)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getRightTarget() != null && link.getRightTarget().equals(element)) {
					link.setLeftTarget(newOne);
				}
			} else if (op instanceof ReferenceOrderChange) {
				final ReferenceOrderChange link = (ReferenceOrderChange)op;
				if (link.getReference().equals(ref)) {
					// FIXME respect ordering!
					link.getRightTarget().add(newOne);
				}
			}
		}
		super.undoInTarget();
	}
}
