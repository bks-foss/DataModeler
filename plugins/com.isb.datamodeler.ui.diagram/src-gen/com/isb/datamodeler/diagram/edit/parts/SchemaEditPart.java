package com.isb.datamodeler.diagram.edit.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.NonResizableLabelEditPolicy;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.diagram.actions.SchemaEdgesCanonicalEditPolicy;
import com.isb.datamodeler.diagram.edit.policies.SchemaCanonicalEditPolicy;
import com.isb.datamodeler.diagram.edit.policies.SchemaItemSemanticEditPolicy;
import com.isb.datamodeler.diagram.edit.policy.PersistentTableDragDropEditPolicy;
import com.isb.datamodeler.tables.EPersistentTable;

/**
 * @generated
 */
public class SchemaEditPart extends DiagramEditPart {

	/**
	 * @generated
	 */
	public final static String MODEL_ID = "Datamodeler"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 1000;

	/**
	 * @generated
	 */
	public SchemaEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new SchemaItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new SchemaCanonicalEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new PersistentTableDragDropEditPolicy());

		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.POPUPBAR_ROLE);
	}

	/**
	 * @generated
	 */
	/*package-local*/static class NodeLabelDragPolicy extends
			NonResizableEditPolicy {

		/**
		 * @generated
		 */
		@SuppressWarnings("rawtypes")
		protected List createSelectionHandles() {
			MoveHandle h = new MoveHandle((GraphicalEditPart) getHost());
			h.setBorder(null);
			return Collections.singletonList(h);
		}

		/**
		 * @generated
		 */
		public Command getCommand(Request request) {
			return null;
		}

		/**
		 * @generated
		 */
		public boolean understandsRequest(Request request) {
			return false;
		}
	}

	/**
	 * @generated
	 */
	/*package-local*/static class LinkLabelDragPolicy extends
			NonResizableLabelEditPolicy {

		/**
		 * @generated
		 */
		@SuppressWarnings("rawtypes")
		protected List createSelectionHandles() {
			MoveHandle mh = new MoveHandle((GraphicalEditPart) getHost());
			mh.setBorder(null);
			return Collections.singletonList(mh);
		}
	}

	@Override
	//Cuando se agrega un hijo nuevo restauramos las relaciones (Tiene sentido para el drag&drop)
	protected void addChild(EditPart child, int index) {
		super.addChild(child, index);

		if (child instanceof PersistentTableEditPart) {
			EPersistentTable newTable = (EPersistentTable) ((PersistentTableEditPart) child)
					.resolveSemanticElement();

			child.installEditPolicy("RestoreEdges",
					new SchemaEdgesCanonicalEditPolicy(newTable));
			child.removeEditPolicy("RestoreEdges");
		}

	}

}
