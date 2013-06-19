package com.isb.datamodeler.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;

/**
 * @generated
 */
public class ForeignKeyItemSemanticEditPolicy extends
		DatamodelerBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public ForeignKeyItemSemanticEditPolicy() {
		super(DatamodelerElementTypes.ForeignKey_4001);
	}

	/**
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		return getGEFWrapper(new DestroyElementCommand(req));
	}

}
