package com.isb.datamodeler.diagram.edit.policies;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;

import com.isb.datamodeler.diagram.edit.commands.PersistentTableCreateCommand;
import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;

/**
 * @generated
 */
public class SchemaItemSemanticEditPolicy extends
		DatamodelerBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public SchemaItemSemanticEditPolicy() {
		super(DatamodelerElementTypes.Schema_1000);
	}

	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req) {
		if (DatamodelerElementTypes.PersistentTable_2009 == req
				.getElementType()) {
			return getGEFWrapper(new PersistentTableCreateCommand(req));
		}
		return super.getCreateCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getDuplicateCommand(DuplicateElementsRequest req) {
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
				.getEditingDomain();
		return getGEFWrapper(new DuplicateAnythingCommand(editingDomain, req));
	}

	/**
	 * @generated
	 */
	private static class DuplicateAnythingCommand extends
			DuplicateEObjectsCommand {

		/**
		 * @generated
		 */
		public DuplicateAnythingCommand(
				TransactionalEditingDomain editingDomain,
				DuplicateElementsRequest req) {
			super(editingDomain, req.getLabel(), req
					.getElementsToBeDuplicated(), req
					.getAllDuplicatedElementsMap());
		}

	}

	@Override
	/**
	 * @generated NOT
	 * Controla si se puede o no eliminar un EditPart del modelo
	 */
	protected boolean shouldProceed(DestroyRequest destroyRequest) {
		return false;
	}

}
