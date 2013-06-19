package com.isb.datamodeler.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.action.global.GlobalActionId;
import org.eclipse.gmf.runtime.common.ui.services.action.global.AbstractGlobalActionHandler;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.isb.datamodeler.internal.ui.views.actions.edit.CopyDataModelerElementAction;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.tables.ETable;

public class DatamodelerClipboardActionHandler extends AbstractGlobalActionHandler{

	ArrayList<ETable> _selection;
	
	public DatamodelerClipboardActionHandler() {
		super();
	}
	
	@Override
	public boolean canHandle(IGlobalActionContext context) {
		boolean result = false;

		/* Check if the active part is a IDiagramWorkbenchPart */
		IWorkbenchPart part = context.getActivePart();
		if (!(part instanceof IDiagramWorkbenchPart)) {
			return false;
		}

		/* Check if the selection is a structured selection */
		if (!(context.getSelection() instanceof IStructuredSelection)) {
			return result;
		}
		
		String actionId = context.getActionId();

		if (actionId.equals(GlobalActionId.COPY)) {
			result = CopyDataModelerElementAction.canCopy(context.getSelection());
		}
			
		return result;
	}
	
	@Override
	public ICommand getCommand(IGlobalActionContext context) {
		ArrayList<ETable> tables = CopyDataModelerElementAction.filterSelection(context.getSelection());
		
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		
		return new EMFtoGMFCommandWrapper(editingDomain, "Copy PersistentTable Command", CopyDataModelerElementAction.getCopyCommand(tables, editingDomain)); //$NON-NLS-1$
	}

	public class EMFtoGMFCommandWrapper extends AbstractTransactionalCommand{

		private Command _command;
		
		public EMFtoGMFCommandWrapper (TransactionalEditingDomain domain, String label, Command command)
		{
			super(domain,label,null);
			_command = command;
		}
		
		@SuppressWarnings("rawtypes")
		public EMFtoGMFCommandWrapper(TransactionalEditingDomain domain,
				String label, List affectedFiles) {
			super(domain, label, affectedFiles);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
				IAdaptable info) throws ExecutionException {

			if(_command.canExecute())
				_command.execute();
			
			return CommandResult.newOKCommandResult();
		}
		
	}
}
