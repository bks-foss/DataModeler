package com.isb.datamodeler.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

import com.isb.datamodeler.diagram.edit.commands.ForeignKey2CreateCommand;
import com.isb.datamodeler.diagram.edit.commands.UniqueConstraintCreateCommand;
import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;

/**
 * @generated
 */
public class PersistentTableConstraintsCompartmentItemSemanticEditPolicy extends
		DatamodelerBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public PersistentTableConstraintsCompartmentItemSemanticEditPolicy() {
		super(DatamodelerElementTypes.PersistentTable_2009);
	}

	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req) {
		if (DatamodelerElementTypes.UniqueConstraint_3002 == req
				.getElementType()) {
			return getGEFWrapper(new UniqueConstraintCreateCommand(req));
		}
		if (DatamodelerElementTypes.ForeignKey_3003 == req.getElementType()) {
			return getGEFWrapper(new ForeignKey2CreateCommand(req));
		}
		return super.getCreateCommand(req);
	}

}
