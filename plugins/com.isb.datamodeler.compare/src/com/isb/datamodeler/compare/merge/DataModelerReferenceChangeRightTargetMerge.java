package com.isb.datamodeler.compare.merge;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class DataModelerReferenceChangeRightTargetMerge extends DataModelerDefaultMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
	@Override
	public void applyInOrigin() {
		final ReferenceChangeRightTarget theDiff = (ReferenceChangeRightTarget)this.diff;	
		final EReference reference = theDiff.getReference();
		
		// Bugzilla 21390 Tiene que mergear toda la lista de los miembros cada vez que se realiza el merge.
		if (reference.getName().equals("members")) 		
			doMembersMerge((DiffGroup) theDiff.eContainer(), false);					
		else 
			mockApplyMerge(theDiff);						
	}

	private void doMembersMerge(DiffGroup diff, boolean undo) {
		EList<DiffElement> subdiff = diff.getSubDiffElements();
		int i = 0;
		int diffSize = diff.getSubDiffElements().size(); 
		
		while (i < diffSize){
           DiffElement particularDiff = subdiff.get(0);
           if (particularDiff instanceof ReferenceChangeRightTarget) {               
                  if (undo)
                         mockUndoMerge((ReferenceChangeRightTarget) particularDiff);
                  else
                         mockApplyMerge((ReferenceChangeRightTarget) particularDiff);
           }
           i ++;
		}
	}

	/**
	 * Bugzilla 21390 Tiene que mergear toda la lista de los miembros cada vez que se realiza el merge.
	 * @param diff
	 */
	private void mockApplyMerge(ReferenceChangeRightTarget diff){
		final ReferenceChangeRightTarget theDiff = diff;
		final EObject element = theDiff.getLeftElement();
		final EObject rightTarget = theDiff.getRightTarget();
		EObject leftTarget = theDiff.getLeftTarget();

		final EReference reference = theDiff.getReference();			

		// ordering handling:
		int index = -1;
		if (reference.isMany()) {
			final EObject rightElement = theDiff.getRightElement();
			final Object rightRefValue = rightElement.eGet(reference);
			if (rightRefValue instanceof List) {
				final List refRightValueList = (List)rightRefValue;
				index = refRightValueList.indexOf(rightTarget);
			}
		}
		
		// Bugzilla xxxxx Cuando hacía el copyReferenceValue con el rightTarget == null realizaba una copia del col2
		// y cuando lo añadía a la lista de elementos referenciados le añadía el id. Por lo que primero voy a realizar
		// una búsqueda del objeto para referenciado.
		if (leftTarget == null)		
			 leftTarget = (EObject) DataModelerMergeService.getCopier(diff).findReferencedObjectCopy(rightTarget);
		
		final EObject copiedValue = DataModelerMergeService.getCopier(diff).copyReferenceValue(reference, element,
				rightTarget, leftTarget, index);

		// We'll now look through this reference's eOpposite as they are already taken care of
		final Iterator<EObject> related = getDiffModel(diff).eAllContents();
		while (related.hasNext()) {
			final DiffElement op = (DiffElement)related.next();
			if (op instanceof ReferenceChangeRightTarget) {
				final ReferenceChangeRightTarget link = (ReferenceChangeRightTarget)op;
				// If this is my eOpposite, delete it from the DiffModel (merged along with this one)
				if (link.getReference().equals(theDiff.getReference().getEOpposite())
						&& link.getRightTarget().equals(element)) {
					removeFromContainer(link);
				}
			} else if (op instanceof ReferenceOrderChange) {
				final ReferenceOrderChange link = (ReferenceOrderChange)op;
				if (link.getReference().equals(theDiff.getReference())) {
					// FIXME respect ordering!
					link.getLeftTarget().add(copiedValue);
				}
			}
		}
		super.applyInOrigin(diff);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		final ReferenceChangeRightTarget theDiff = (ReferenceChangeRightTarget)this.diff;		
		
		// Bugzilla 21390 Tiene que mergear toda la lista de los miembros cada vez que se realiza el merge.
		if (theDiff.getReference().getName().equals("members")) 
			doMembersMerge((DiffGroup) theDiff.eContainer(), true);					
		else 
			mockUndoMerge(theDiff);				
	}
	
	/**
	 * Bugzilla 21390 Tiene que mergear toda la lista de los miembros cada vez que se realiza el merge.
	 * 
	 */
	private void mockUndoMerge(ReferenceChangeRightTarget particularDiff) {
		final ReferenceChangeRightTarget theDiff = particularDiff;
		final EObject element = theDiff.getRightElement();
		final EObject rightTarget = theDiff.getRightTarget();			
		
		try {
			EFactory.eRemove(element, theDiff.getReference().getName(), rightTarget);
		} catch (final FactoryException e) {
			EMFComparePlugin.log(e, true);
		}
		// we should now have a look for AddReferencesLinks needing this object
		final Iterator<EObject> related = getDiffModel(theDiff).eAllContents();
		while (related.hasNext()) {
			final DiffElement op = (DiffElement)related.next();
			if (op instanceof ReferenceChangeRightTarget) {
				final ReferenceChangeRightTarget link = (ReferenceChangeRightTarget)op;
				// now if I'm in the target References I should put my copy in the origin
				if (link.getReference().equals(theDiff.getReference().getEOpposite())
						&& link.getRightTarget().equals(element)) {
					removeFromContainer(link);
				}
			} else if (op instanceof ResourceDependencyChange) {
				final ResourceDependencyChange link = (ResourceDependencyChange)op;
				final Resource res = link.getRoots().get(0).eResource();
				if (res == rightTarget.eResource()) {
					EcoreUtil.remove(link);
					//Date fechaAhora = new Date();
					//System.out.println(fechaAhora + " UNLOAD EN MERGE --> DataModelerReferenceChangeRightTarget");
					res.unload();
				}
			}
		}
		super.undoInTarget(theDiff);
	}
}
