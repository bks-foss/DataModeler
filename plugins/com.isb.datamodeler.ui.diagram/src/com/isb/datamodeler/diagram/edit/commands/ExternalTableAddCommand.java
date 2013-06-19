package com.isb.datamodeler.diagram.edit.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.EditElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.ETable;

public class ExternalTableAddCommand extends EditElementCommand {

	private ESchema _parentSchema;
	
	private ETable _newTable;

	
	public ExternalTableAddCommand(ESchema parentSchema, ETable newTable, CreateElementRequest req) {
		super(req.getLabel(), null, req);
		_parentSchema = parentSchema;
		_newTable = newTable;
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		
		_parentSchema.getTables().add(_newTable);


		doConfigure(_newTable, monitor, info);
		
		((CreateElementRequest) getRequest()).setNewElement(_newTable);
		
		return CommandResult.newOKCommandResult(_newTable);
	}
	
	
	protected void doConfigure(ETable newElement,
			IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		IElementType elementType = ((CreateElementRequest) getRequest())
				.getElementType();
		ConfigureRequest configureRequest = new ConfigureRequest(
				getEditingDomain(), newElement, elementType);
		configureRequest.setClientContext(((CreateElementRequest) getRequest())
				.getClientContext());
		configureRequest.addParameters(getRequest().getParameters());
		ICommand configureCommand = elementType
				.getEditCommand(configureRequest);
		if (configureCommand != null && configureCommand.canExecute()) {
			configureCommand.execute(monitor, info);
		}
	}
	
	public boolean canExecute() {
		return true;
	}
}