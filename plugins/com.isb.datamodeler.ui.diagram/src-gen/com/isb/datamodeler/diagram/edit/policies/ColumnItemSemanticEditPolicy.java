package com.isb.datamodeler.diagram.edit.policies;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;

/**
 * @generated
 */
public class ColumnItemSemanticEditPolicy extends
		DatamodelerBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public ColumnItemSemanticEditPolicy() {
		super(DatamodelerElementTypes.Column_3001);
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
		// there are indirectly referenced children, need extra commands: false
		//Se eliminan los nodos o conexiones que referencian al mismo elemento del modelo que se está eliminando
		addDestroyViewsCommand(cmd, view);
		// delete host element
		cmd.add(new DestroyElementCommand(req));

		return getGEFWrapper(cmd.reduce());
	}

}
