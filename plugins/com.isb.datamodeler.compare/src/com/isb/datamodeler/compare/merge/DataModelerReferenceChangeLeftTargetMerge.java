package com.isb.datamodeler.compare.merge;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DataModelerReferenceChangeLeftTargetMerge extends DataModelerDefaultMerger {

	@Override
	public void applyInOrigin() {
		final ReferenceChangeLeftTarget theDiff = (ReferenceChangeLeftTarget)this.diff;
		
		// Bugzilla 21390 Tiene que mergear toda la lista de los miembros cada vez que se realiza el merge.
		if (theDiff.getReference().getName().equals("members")) 
			doMembersMerge((DiffGroup) theDiff.eContainer(), true);			
		else 
			mockApplyMerge(theDiff);		
	}
	
	/**
	 * Bugzilla 21390 Tiene que mergear toda la lista de los miembros cada vez que se realiza el merge.
	 * @param diff
	 * @param undo
	 */
	private void doMembersMerge(DiffGroup diff, boolean undo) {
		EList<DiffElement> subdiff = diff.getSubDiffElements();
		int i = 0;
		int diffSize = diff.getSubDiffElements().size(); 
		
		while (i < diffSize){
           DiffElement particularDiff = subdiff.get(0);
           if (particularDiff instanceof ReferenceChangeLeftTarget) {               
                  if (undo)
                         mockUndoMerge((ReferenceChangeLeftTarget) particularDiff);
                  else
                         mockApplyMerge((ReferenceChangeLeftTarget) particularDiff);
           }
           i ++;
		}       	
	}

	/**
	 * Bugzilla 21390 Tiene que mergear toda la lista de los miembros cada vez que se realiza el merge.
	 * @param particularDiff
	 */
	private void mockApplyMerge(ReferenceChangeLeftTarget particularDiff) {
		final ReferenceChangeLeftTarget theDiff = particularDiff;

		final EReference reference = theDiff.getReference();

		final EObject rightElement = theDiff.getRightElement();
		final EObject leftElement = theDiff.getLeftElement();

		final EObject leftTarget = theDiff.getLeftTarget();
		EObject rightTarget = theDiff.getRightTarget();			

		int index = -1;
		if (reference.isMany()) {
			final Object leftRefValue = leftElement.eGet(reference);
			if (leftRefValue instanceof List) {
				final List refLeftValueList = (List)leftRefValue;
				index = refLeftValueList.indexOf(leftTarget);
			}
		}
		
		// Bugzilla xxxxx Cuando hacía el copyReferenceValue con el rightTarget == null realizaba una copia del col2
		// y cuando lo añadía a la lista de elementos referenciados le añadía el id.
		
		if (rightTarget == null)		
			 rightTarget = (EObject) DataModelerMergeService.getCopier(theDiff).findReferencedObjectCopy(leftTarget);
		
		final EObject copiedValue = DataModelerMergeService.getCopier(theDiff).copyReferenceValue(reference, rightElement,
				leftTarget, rightTarget, index);

		// we should now have a look for AddReferencesLinks needing this object
		final Iterator<EObject> siblings = getDiffModel(theDiff).eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof ReferenceChangeLeftTarget) {
				final ReferenceChangeLeftTarget link = (ReferenceChangeLeftTarget)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getReference().equals(reference.getEOpposite())
						&& link.getLeftTarget().equals(rightElement)) {
					removeFromContainer(link);
				}
			} else if (op instanceof ReferenceOrderChange) {
				final ReferenceOrderChange link = (ReferenceOrderChange)op;
				if (link.getReference().equals(reference)) {
					// FIXME respect ordering!
					link.getRightTarget().add(copiedValue);
				}
			}
		}
		super.undoInTarget(theDiff);
		
	}

	/**
	 * Bugzilla 21390 Tiene que mergear toda la lista de los miembros cada vez que se realiza el merge.
	 * @param particularDiff
	 */
	private void mockUndoMerge(ReferenceChangeLeftTarget particularDiff) {
		final ReferenceChangeLeftTarget theDiff = particularDiff;
		final EObject element = theDiff.getLeftElement();
		final EObject leftTarget = theDiff.getLeftTarget();
		try {
			EFactory.eRemove(element, theDiff.getReference().getName(), leftTarget);
		} catch (final FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		// we should now have a look for AddReferencesLinks needing this object
		final Iterator<EObject> siblings = getDiffModel(theDiff).eAllContents();
		while (siblings.hasNext()) {
			final DiffElement op = (DiffElement)siblings.next();
			if (op instanceof ReferenceChangeLeftTarget) {
				final ReferenceChangeLeftTarget link = (ReferenceChangeLeftTarget)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getReference().equals(theDiff.getReference().getEOpposite())
						&& link.getLeftTarget().equals(element)) {
					removeFromContainer(link);
				}
			} else if (op instanceof ResourceDependencyChange) {
				final ResourceDependencyChange link = (ResourceDependencyChange)op;
				final Resource res = link.getRoots().get(0).eResource();
				if (res == leftTarget.eResource()) {
					EcoreUtil.remove(link);
					//Date fechaAhora = new Date();
					//System.out.println(fechaAhora + " UNLOAD EN MERGE --> DataModelerReferenceChangeLeftTarget");
					res.unload();
				}
			}
		}
		super.applyInOrigin(particularDiff);
		
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final ReferenceChangeLeftTarget theDiff = (ReferenceChangeLeftTarget)this.diff;
		final EReference reference = theDiff.getReference();
		
		// Bugzilla 21390 Tiene que mergear toda la lista de los miembros cada vez que se realiza el merge.
		if (reference.getName().equals("members"))
			doMembersMerge((DiffGroup) theDiff.eContainer(), false);				
		else 
			mockUndoMerge(theDiff);		
	}
}
