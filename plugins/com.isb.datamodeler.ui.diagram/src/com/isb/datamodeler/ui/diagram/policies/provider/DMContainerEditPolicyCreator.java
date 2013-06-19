package com.isb.datamodeler.ui.diagram.policies.provider;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ContainerEditPolicy;

import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;



public class DMContainerEditPolicyCreator
{

public static EditPolicy getExtendedContainerEditPolicy(EditPart editPart)
{
	if(editPart instanceof SchemaEditPart)
	{
		return NoDuplicateContainerEditPolicyCreator.getExtendedContainerEditPolicy(editPart);
	}
	
	return (ContainerEditPolicy)editPart.getEditPolicy(EditPolicy.CONTAINER_ROLE);
}

}
