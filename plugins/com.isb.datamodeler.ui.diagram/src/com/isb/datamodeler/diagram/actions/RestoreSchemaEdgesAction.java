package com.isb.datamodeler.diagram.actions;

import java.util.Collections;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.tables.EPersistentTable;

public class RestoreSchemaEdgesAction  implements IObjectActionDelegate
{
	
private DiagramEditPart _selectedElement;

public RestoreSchemaEdgesAction()
{
}

@Override
public void run(IAction action)
{
	final Diagram diagram = _selectedElement.getDiagramView();
	for(Object obj:_selectedElement.getChildren())
	{
		if(obj instanceof PersistentTableEditPart)
		{
			PersistentTableEditPart editPart = (PersistentTableEditPart)obj;
			EPersistentTable eTable = (EPersistentTable)((Node)editPart.getModel()).getElement();
			SchemaEdgesCanonicalEditPolicy cep = new SchemaEdgesCanonicalEditPolicy(eTable);
			
			//Al instalar las policies se refrescan automaticamente
			editPart.installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, cep);
		
			editPart.removeEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
		}
	}
	
	//Hacemos que los cambios que se han realizado persistan
	
	TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(diagram);
	
	AbstractTransactionalCommand atc = new AbstractTransactionalCommand(ted, "", Collections.singletonList(
												WorkspaceSynchronizer.getFile(diagram.eResource())))
	{
		@Override
		protected CommandResult doExecuteWithResult(
				IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException
		{
			diagram.persistChildren();
			diagram.persistEdges();

			return null;
		}
	};
	
	try {
		atc.execute(new NullProgressMonitor(), null);
	} catch (ExecutionException e) {
		e.printStackTrace();
	}
}

@Override
public void selectionChanged(IAction action, ISelection selection)
{
	_selectedElement = null;
	if (selection instanceof IStructuredSelection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.size() == 1)
		{
				if((structuredSelection.getFirstElement() instanceof DiagramEditPart))
				{
					_selectedElement = (DiagramEditPart)structuredSelection.getFirstElement();
				}
		}
	}
	action.setEnabled(_selectedElement!=null);
}

@Override
public void setActivePart(IAction action, IWorkbenchPart targetPart)
{
}
}
