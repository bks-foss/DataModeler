package com.isb.datamodeler.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.diagram.edit.policies.DatamodelerBaseItemSemanticEditPolicy;
import com.isb.datamodeler.tables.EBaseTable;

/**
 * @generated
 */
public class ForeignKeyReorientCommand extends EditElementCommand {

	/**
	 * @generated
	 */
	private final int reorientDirection;

	/**
	 * @generated
	 */
	private final EObject oldEnd;

	/**
	 * @generated
	 */
	private final EObject newEnd;

	/**
	 * @generated
	 */
	public ForeignKeyReorientCommand(ReorientRelationshipRequest request) {
		super(request.getLabel(), request.getRelationship(), request);
		reorientDirection = request.getDirection();
		oldEnd = request.getOldRelationshipEnd();
		newEnd = request.getNewRelationshipEnd();
	}

	/**
	 * @generated
	 */
	public boolean canExecute() {
		if (false == getElementToEdit() instanceof EForeignKey) {
			return false;
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
			return canReorientSource();
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
			return canReorientTarget();
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean canReorientSource() {
		if (!(oldEnd instanceof EBaseTable && newEnd instanceof EBaseTable)) {
			return false;
		}
		EBaseTable target = getLink().getParentTable();
		return DatamodelerBaseItemSemanticEditPolicy.getLinkConstraints()
				.canExistForeignKey_4001(getLink(), getNewSource(), target);
	}

	/**
	 * @generated
	 */
	protected boolean canReorientTarget() {
		if (!(oldEnd instanceof EBaseTable && newEnd instanceof EBaseTable)) {
			return false;
		}
		if (!(getLink().eContainer() instanceof EBaseTable)) {
			return false;
		}
		EBaseTable source = (EBaseTable) getLink().eContainer();
		return DatamodelerBaseItemSemanticEditPolicy.getLinkConstraints()
				.canExistForeignKey_4001(getLink(), source, getNewTarget());
	}

	/**
	 * @generated
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		if (!canExecute()) {
			throw new ExecutionException(
					"Invalid arguments in reorient link command"); //$NON-NLS-1$
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_SOURCE) {
			return reorientSource();
		}
		if (reorientDirection == ReorientRelationshipRequest.REORIENT_TARGET) {
			return reorientTarget();
		}
		throw new IllegalStateException();
	}

	/**
	 * @generated
	 */
	protected CommandResult reorientSource() throws ExecutionException {
		getOldSource().getConstraints().remove(getLink());
		getNewSource().getConstraints().add(getLink());
		return CommandResult.newOKCommandResult(getLink());
	}

	/**
	 * @generated
	 */
	protected CommandResult reorientTarget() throws ExecutionException {
		getLink().setParentTable(getNewTarget());
		return CommandResult.newOKCommandResult(getLink());
	}

	/**
	 * @generated
	 */
	protected EForeignKey getLink() {
		return (EForeignKey) getElementToEdit();
	}

	/**
	 * @generated
	 */
	protected EBaseTable getOldSource() {
		return (EBaseTable) oldEnd;
	}

	/**
	 * @generated
	 */
	protected EBaseTable getNewSource() {
		return (EBaseTable) newEnd;
	}

	/**
	 * @generated
	 */
	protected EBaseTable getOldTarget() {
		return (EBaseTable) oldEnd;
	}

	/**
	 * @generated
	 */
	protected EBaseTable getNewTarget() {
		return (EBaseTable) newEnd;
	}
}
