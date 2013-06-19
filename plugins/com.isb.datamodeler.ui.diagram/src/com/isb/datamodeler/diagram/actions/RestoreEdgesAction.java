/**
 * 
 */
package com.isb.datamodeler.diagram.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.tables.EPersistentTable;

/**
 * Lanzamos una acción desde los elementos en el diagrama que va 
 * a volver a mostrar las flechas eliminadas de este, es decir
 * se vuelven a pintar todas las flechas de un elemento que se
 * hubieran borrado
 */
public class RestoreEdgesAction implements IObjectActionDelegate {

	private List<PersistentTableEditPart> _selectedElements;
	private SchemaEditPart _schemaEditPart;

	/**
	 * 
	 */
	public RestoreEdgesAction() {
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
		if(_selectedElements.isEmpty())
			return;
		_schemaEditPart = (SchemaEditPart)_selectedElements.get(0).getParent();
		
		final Diagram diagram = _schemaEditPart.getDiagramView();
		
		for(PersistentTableEditPart editPart:_selectedElements)
		{
			EPersistentTable eTable = (EPersistentTable)((Node)editPart.getModel()).getElement();
			SchemaEdgesCanonicalEditPolicy cep = new SchemaEdgesCanonicalEditPolicy(eTable);
			
			//Al instalar las policies se refrescan automaticamente
			editPart.installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, cep);
		
			editPart.removeEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
		}
				
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
		_selectedElements = new ArrayList<PersistentTableEditPart>();
		if (selection instanceof IStructuredSelection) 
		{
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			for(Iterator<?> iter = structuredSelection.iterator(); iter.hasNext();)
			{
				Object element = iter.next();
				
				if(element instanceof PersistentTableEditPart)
				{
					PersistentTableEditPart tableEditPart = (PersistentTableEditPart) element;
					EPersistentTable table = (EPersistentTable)tableEditPart.resolveSemanticElement();
//						 Comprobamos que la tabla no sea externa
//						if(!table.getSchema().isExternal())
						_selectedElements.add(tableEditPart);
				}
				else
				{
					action.setEnabled(false);
					return;
				}
				
			}
			if(_selectedElements.isEmpty())
			{
				action.setEnabled(false);
				return;
			}
			action.setEnabled(true);
		}
		else action.setEnabled(false);
	}

}
