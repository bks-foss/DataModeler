package com.isb.datamodeler.diagram.edit.commands;

import java.util.ArrayList;
import java.util.List;

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
import com.isb.datamodeler.ui.project.EProject;

public class ExternalSchemaCreateCommand extends EditElementCommand {

	private EProject _project;
	
	private ESchema _newSchema;
	
	private ESchema _oldSchema;
	
	public ExternalSchemaCreateCommand(EProject parentProject, ESchema newSchema, ESchema oldSchema, CreateElementRequest req) {
		super(req.getLabel(), null, req);
		_newSchema = newSchema;
		_oldSchema = oldSchema;
		_project = parentProject;
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {
		
		if(findSchemas().size()<1)
		{	
			_newSchema.setDatabase(_project.getDatabase());
			
			_project.getSchemas().add(_newSchema);
	
			// TODO que pasa con los catalogos?
			
			doConfigure(_newSchema, monitor, info);
			
			((CreateElementRequest) getRequest()).setNewElement(_newSchema);
			
			return CommandResult.newOKCommandResult(_newSchema);
		}
		
		return CommandResult.newOKCommandResult();
	}
	
	private List<ESchema> findSchemas()
	{
		List<ESchema> schemas = new ArrayList<ESchema>();
		for(ESchema schema: _project.getSchemas())
		{
			if(schema.getCapability().equalsIgnoreCase(_oldSchema.getCapability()) &&
					schema.getName().equalsIgnoreCase(_oldSchema.getName()))
				schemas.add(schema);
		}
		return schemas;
	}
	
	protected void doConfigure(ESchema newElement,
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