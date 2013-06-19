package com.isb.datamodeler.diagram.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.diagram.ui.actions.AbstractDeleteFromAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EPersistentTable;

/**
 * @generated
 */
public class DeleteElementAction extends AbstractDeleteFromAction {

	/**
	 * @generated
	 */
	public DeleteElementAction(IWorkbenchPart part) {
		super(part);
	}

	/**
	 * @generated
	 */
	public DeleteElementAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * @generated
	 */
	public void init() {
		super.init();
		setId(ActionIds.ACTION_DELETE_FROM_MODEL);
		setText(DiagramUIMessages.DiagramEditor_Delete_from_Model);
		setToolTipText(DiagramUIMessages.DiagramEditor_Delete_from_ModelToolTip);
		ISharedImages workbenchImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setHoverImageDescriptor(workbenchImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setImageDescriptor(workbenchImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(workbenchImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
	}

	/**
	 * @generated
	 */
	protected String getCommandLabel() {
		return DiagramUIMessages.DiagramEditor_Delete_from_Model;
	}

	/**
	 * @generated not
	 */
	protected Command getCommand(Request request) {
		List operationSet = getOperationSet();
		if (operationSet.isEmpty()) {
			return UnexecutableCommand.INSTANCE;
		}

		//reordenamos los elementos seleccionados de forma de poner a lo ultimo las tablas con UKS
		//referenciadas por otras tablas(Bug# 20504)
		Collections.sort(operationSet, new OperationSetComparator());

		Iterator editParts = operationSet.iterator();
		CompositeTransactionalCommand command = new CompositeTransactionalCommand(
				getEditingDomain(), getCommandLabel());
		while (editParts.hasNext()) {
			EditPart editPart = (EditPart) editParts.next();
			Command curCommand = editPart.getCommand(request);
			if (curCommand != null) {
				command.compose(new CommandProxy(curCommand));
			}
		}
		if (command.isEmpty() || command.size() != operationSet.size()) {
			return UnexecutableCommand.INSTANCE;
		}
		return new ICommandProxy(command);
	}

	class OperationSetComparator implements Comparator<EditPart> {
		/**
		 * Las tablas con UKs referenciadas se eliminan ultimas
		 * Si una tabla tiene FKs referenciando a otra va primero.
		 * va en primer lugar table1.
		 */
		@Override
		public int compare(EditPart o1, EditPart o2) {
			if (o1 instanceof PersistentTableEditPart
					&& o2 instanceof PersistentTableEditPart) {
				EPersistentTable table1 = (EPersistentTable) ((PersistentTableEditPart) o1)
						.resolveSemanticElement();
				EPersistentTable table2 = (EPersistentTable) ((PersistentTableEditPart) o2)
						.resolveSemanticElement();

				// tablas visitadas
				Map<EBaseTable, List<EBaseTable>> visitTables = new Hashtable<EBaseTable, List<EBaseTable>>();

				// Si table2 referencia directa o indirectamente a table1 va primero 
				if (isReferencedBy(table1, table2, visitTables))
					return 1;

				visitTables = new Hashtable<EBaseTable, List<EBaseTable>>();

				if (isReferencedBy(table2, table1, visitTables))
					return -1;
			}

			//Las tablas se eliminan primero
			if (o1 instanceof PersistentTableEditPart
					&& !(o2 instanceof PersistentTableEditPart))
				return -1;

			if (o2 instanceof PersistentTableEditPart
					&& !(o1 instanceof PersistentTableEditPart))
				return 1;

			return 1;
		}

	}

	/**
	 * Devuelve 'true' si table2 referencia directa o indirectamente a table1 
	 * @param table1
	 * @param table2
	 * @return
	 */
	private boolean isReferencedBy(EBaseTable table1, EBaseTable table2,
			Map<EBaseTable, List<EBaseTable>> visitTables) {
		List<EBaseTable> listTables = visitTables.get(table1);
		if (listTables != null) {
			if (listTables.contains(table2))
				return false;
			else
				listTables.add(table2);
		} else {
			listTables = new ArrayList<EBaseTable>();
			listTables.add(table2);
			visitTables.put(table1, listTables);

		}

		for (EForeignKey foreignKey : table2.getForeignKeys()) {
			if (foreignKey.getParentTable() == table1)
				return true;

			if (isReferencedBy(table1, foreignKey.getParentTable(), visitTables))
				return true;
		}

		return false;
	}

	/**
	 * @generated
	 */
	@Override
	protected void doRun(IProgressMonitor progressMonitor) {
		// Este código ha sido modificado mediante la plantilla dinámica DeleteElementAction.xpt
		if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), "Eliminar del Modelo",
				"¿Desea eliminar el/los elemento/s seleccionado/s del modelo?"))
			super.doRun(progressMonitor);
	}

	/**
	 * @generated
	 */
	@Override
	protected boolean calculateEnabled() {
		// Este código ha sido modificado mediante la plantilla dinámica DeleteElementAction.xpt
		// para no permitir borrar del modelo relaciones cuyo origen sean shortcut
		List operationSet = getOperationSet();
		Iterator editParts = operationSet.iterator();
		if (!operationSet.isEmpty()) {
			while (editParts.hasNext()) {
				EditPart editPart = (EditPart) editParts.next();
				if (editPart instanceof ConnectionNodeEditPart) {
					ConnectionNodeEditPart cEditPart = (ConnectionNodeEditPart) editPart;
					EditPart source = cEditPart.getSource();
					if (source.getModel() instanceof View) {
						View v = (View) source.getModel();

						if (v.getEAnnotation("Shortcut") != null)
							return false;
					}
				}

			}
		}
		return super.calculateEnabled();
	}

}
