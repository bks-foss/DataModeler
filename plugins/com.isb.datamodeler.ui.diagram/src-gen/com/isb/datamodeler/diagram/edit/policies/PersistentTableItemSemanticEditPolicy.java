package com.isb.datamodeler.diagram.edit.policies;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.diagram.edit.commands.ForeignKey2ReorientCommand;
import com.isb.datamodeler.diagram.edit.commands.ForeignKey3CreateCommand;
import com.isb.datamodeler.diagram.edit.commands.ForeignKeyCreateCommand;
import com.isb.datamodeler.diagram.edit.commands.ForeignKeyReorientCommand;
import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey2EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey3EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableColumnCompartmentEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableConstraintsCompartmentEditPart;
import com.isb.datamodeler.diagram.edit.parts.UniqueConstraintEditPart;
import com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry;
import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;

/**
 * @generated
 */
public class PersistentTableItemSemanticEditPolicy extends
		DatamodelerBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public PersistentTableItemSemanticEditPolicy() {
		super(DatamodelerElementTypes.PersistentTable_2009);
	}

	/**
	 * (Modified template: NodeItemSemanticEditPolicy.xpt)
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {

		View view = (View) getHost().getModel();
		CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(
				getEditingDomain(), null);
		cmd.setTransactionNestingEnabled(false);
		for (Iterator<?> it = view.getTargetEdges().iterator(); it.hasNext();) {
			Edge incomingLink = (Edge) it.next();
			if (DatamodelerVisualIDRegistry.getVisualID(incomingLink) == ForeignKeyEditPart.VISUAL_ID) {
				DestroyElementRequest r = new DestroyElementRequest(
						incomingLink.getElement(), false);
				cmd.add(new DestroyElementCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
			if (DatamodelerVisualIDRegistry.getVisualID(incomingLink) == ForeignKey3EditPart.VISUAL_ID) {
				DestroyElementRequest r = new DestroyElementRequest(
						incomingLink.getElement(), false);
				cmd.add(new DestroyElementCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
		}
		for (Iterator<?> it = view.getSourceEdges().iterator(); it.hasNext();) {
			Edge outgoingLink = (Edge) it.next();
			if (DatamodelerVisualIDRegistry.getVisualID(outgoingLink) == ForeignKeyEditPart.VISUAL_ID) {
				DestroyElementRequest r = new DestroyElementRequest(
						outgoingLink.getElement(), false);
				cmd.add(new DestroyElementCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
				continue;
			}
			if (DatamodelerVisualIDRegistry.getVisualID(outgoingLink) == ForeignKey3EditPart.VISUAL_ID) {
				DestroyElementRequest r = new DestroyElementRequest(
						outgoingLink.getElement(), false);
				cmd.add(new DestroyElementCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
				continue;
			}
		}
		// there are indirectly referenced children, need extra commands: false
		addDestroyChildNodesCommand(cmd);
		//Se eliminan los nodos o conexiones que referencian al mismo elemento del modelo que se está eliminando
		addDestroyViewsCommand(cmd, view);
		// delete host element
		cmd.add(new DestroyElementCommand(req));

		return getGEFWrapper(cmd.reduce());
	}

	/**
	 * @generated
	 */
	private void addDestroyChildNodesCommand(ICompositeCommand cmd) {
		View view = (View) getHost().getModel();
		for (Iterator<?> nit = view.getChildren().iterator(); nit.hasNext();) {
			Node node = (Node) nit.next();
			switch (DatamodelerVisualIDRegistry.getVisualID(node)) {
			case PersistentTableColumnCompartmentEditPart.VISUAL_ID:
				for (Iterator<?> cit = node.getChildren().iterator(); cit
						.hasNext();) {
					Node cnode = (Node) cit.next();
					switch (DatamodelerVisualIDRegistry.getVisualID(cnode)) {
					case ColumnEditPart.VISUAL_ID:
						cmd.add(new DestroyElementCommand(
								new DestroyElementRequest(getEditingDomain(),
										cnode.getElement(), false))); // directlyOwned: true
						// don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
						// cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
						break;
					}
				}
				break;
			case PersistentTableConstraintsCompartmentEditPart.VISUAL_ID:
				for (Iterator<?> cit = node.getChildren().iterator(); cit
						.hasNext();) {
					Node cnode = (Node) cit.next();
					switch (DatamodelerVisualIDRegistry.getVisualID(cnode)) {
					case UniqueConstraintEditPart.VISUAL_ID:
						cmd.add(new DestroyElementCommand(
								new DestroyElementRequest(getEditingDomain(),
										cnode.getElement(), false))); // directlyOwned: true
						// don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
						// cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
						break;
					case ForeignKey2EditPart.VISUAL_ID:
						cmd.add(new DestroyElementCommand(
								new DestroyElementRequest(getEditingDomain(),
										cnode.getElement(), false))); // directlyOwned: true
						// don't need explicit deletion of cnode as parent's view deletion would clean child views as well 
						// cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), cnode));
						break;
					}
				}
				break;
			}
		}
	}

	/**
	 * (Modified template: linkCommands.xpt)
	 * @generated
	 */
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
		Command command = req.getTarget() == null ? getStartCreateRelationshipCommand(req)
				: getCompleteCreateRelationshipCommand(req);
		return command != null ? command : super
				.getCreateRelationshipCommand(req);
	}

	/**
	 * (Modified template: linkCommands.xpt)
	 * @generated
	 */
	protected Command getStartCreateRelationshipCommand(
			CreateRelationshipRequest req) {

		boolean isShortCut = getHost() instanceof IGraphicalEditPart
				&& ((IGraphicalEditPart) getHost()).getPrimaryView()
						.getEAnnotation("Shortcut") != null;

		if (isShortCut)
			return UnexecutableCommand.INSTANCE;

		if (DatamodelerElementTypes.ForeignKey_4001 == req.getElementType()) {
			return getGEFWrapper(new ForeignKeyCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		if (DatamodelerElementTypes.ForeignKey_4002 == req.getElementType()) {
			return getGEFWrapper(new ForeignKey3CreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		return null;
	}

	/**
	 * (Modified template: linkCommands.xpt)
	 * @generated
	 */
	protected Command getCompleteCreateRelationshipCommand(
			CreateRelationshipRequest req) {
		if (DatamodelerElementTypes.ForeignKey_4001 == req.getElementType()) {
			return getGEFWrapper(new ForeignKeyCreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		if (DatamodelerElementTypes.ForeignKey_4002 == req.getElementType()) {
			return getGEFWrapper(new ForeignKey3CreateCommand(req,
					req.getSource(), req.getTarget()));
		}
		return null;
	}

	/**
	 * Returns command to reorient EClass based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
	protected Command getReorientRelationshipCommand(
			ReorientRelationshipRequest req) {
		switch (getVisualID(req)) {
		case ForeignKeyEditPart.VISUAL_ID:
			return getGEFWrapper(new ForeignKeyReorientCommand(req));
		case ForeignKey3EditPart.VISUAL_ID:
			return getGEFWrapper(new ForeignKey2ReorientCommand(req));
		}
		return super.getReorientRelationshipCommand(req);
	}

}
