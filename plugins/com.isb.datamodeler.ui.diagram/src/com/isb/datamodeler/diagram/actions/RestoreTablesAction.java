/**
 * 
 */
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
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EPersistentTable;



public class RestoreTablesAction implements IObjectActionDelegate {

	private PersistentTableEditPart _selectedElement;
	private SchemaEditPart _schemaEditPart;

	/**
	 * 
	 */
	public RestoreTablesAction() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) 
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) 
	{
		_schemaEditPart = (SchemaEditPart)_selectedElement.getParent();
//		_schemaEditPart.refresh();
		
		final Diagram diagram = _schemaEditPart.getDiagramView();
		
		RestoreTablesCanonicalEditPolicy cep = new RestoreTablesCanonicalEditPolicy(diagram , _selectedElement);
	
		
		//Al instalar las policies se refrescan automaticamente
		_schemaEditPart.installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, cep);

		_schemaEditPart.removeEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
		
		//Hacemos que los cambios que se han realizado persintan
		
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
			
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		_selectedElement = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1)
			{
				if((structuredSelection.getFirstElement() instanceof PersistentTableEditPart))
				{
					// Comprobamos que la tabla no sea externa
					PersistentTableEditPart tableEditPart = (PersistentTableEditPart) structuredSelection.getFirstElement();
					SchemaEditPart schemaEditPart = (SchemaEditPart)tableEditPart.getParent();
					ESchema schema = (ESchema)schemaEditPart.resolveSemanticElement();
					EPersistentTable table = (EPersistentTable)tableEditPart.resolveSemanticElement();
					if(!table.getSchema().isExternal() && schema==table.getSchema())
						_selectedElement = tableEditPart;
				}
			}
		}
		action.setEnabled(_selectedElement!=null);
	}

}
