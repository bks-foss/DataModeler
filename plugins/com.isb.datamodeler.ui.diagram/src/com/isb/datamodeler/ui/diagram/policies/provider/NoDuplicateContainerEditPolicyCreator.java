package com.isb.datamodeler.ui.diagram.policies.provider;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;



/**
 * Clase para crear ContainerEditPolicy que tengan desactivado
 * el comando de duplicar elementos
 */
public class NoDuplicateContainerEditPolicyCreator
{

public static EditPolicy getExtendedContainerEditPolicy(EditPart editPart)
{
	if(editPart instanceof SchemaEditPart)
	{
		return new ContainerEditPolicy ()
		{
			/*
			 * Como el método getDuplicateCommand() es privado sobreescribimos
			 * el getCommand() y en el caso que queremos devolvemos un UnexecutableCommand
			 */
			
			@Override
			public Command getCommand(Request request)
			{
				if (RequestConstants.REQ_DUPLICATE.equals(request.getType()))
				{
		            return UnexecutableCommand.INSTANCE;
		        }
				
				return super.getCommand(request);
			}
		};
	}
	
	return (ContainerEditPolicy)editPart.getEditPolicy(EditPolicy.CONTAINER_ROLE);
}

}
