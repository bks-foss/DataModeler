package com.isb.datamodeler.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.diagram.edit.policies.ForeignKey3ItemSemanticEditPolicy;
import com.isb.datamodeler.diagram.figures.CustomForeignKeyFigure;
import com.isb.datamodeler.diagram.figures.DataModelerCustomFigure;

/**
 * @generated
 */
public class ForeignKey3EditPart extends ConnectionNodeEditPart implements
		ITreeBranchEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 4002;

	/**
	 * @generated
	 */
	public ForeignKey3EditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new ForeignKey3ItemSemanticEditPolicy());
	}

	/**
	 * (Modified template: LinkEditPart.xpt)
	 * @generated
	 */
	@Override
	protected void handleNotificationEvent(Notification notification) {
		Object figure = getPrimaryShape();
		if (figure instanceof DataModelerCustomFigure)
			((DataModelerCustomFigure) figure).updateFigure(this);
		super.handleNotificationEvent(notification);
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 */

	protected Connection createConnectionFigure() {
		return new CustomForeignKeyFigure(this);
	}

	/**
	 * @generated
	 */
	public CustomForeignKeyFigure getPrimaryShape() {
		return (CustomForeignKeyFigure) getFigure();
	}
}
