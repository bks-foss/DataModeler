package com.isb.datamodeler.internal.ui.views.actions.edit;

import java.util.ArrayList;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.project.EProject;

public class CopyDataModelerElementAction extends SelectionListenerAction
{	
	static ArrayList<ETable> _selection;
	
	public CopyDataModelerElementAction() {
		super(Messages.bind("CopyDataModelerElementAction.name"));
	}
	
	public static ArrayList<ETable> filterSelection(ISelection selection)
	{
		ArrayList<ETable> result = new ArrayList<ETable>();
		
		if(selection instanceof IStructuredSelection)
		{
			for(Object element:((IStructuredSelection)selection).toList())
			{
				EObject eobject = null;
				
				if(element instanceof DatamodelerDomainNavigatorItem)
					eobject = ((DatamodelerDomainNavigatorItem)element).getEObject();
				else
				{
					if(element instanceof PersistentTableEditPart)
					{
						Object model = ((EditPart) element).getModel();

						if (model instanceof View) {
							View v = (View) model;
							if(v.getEAnnotation("Shortcut") != null)
								eobject = null;
							else eobject = ((IGraphicalEditPart)element).resolveSemanticElement();
						}
					}
					
				}
				
				if(eobject instanceof ETable)
				{
					ETable table = (ETable)eobject;
					
					// Si es externa no se permite copiar
					if( ((EProject)table.getSchema().eContainer()).getCapability().
						equalsIgnoreCase(table.getSchema().getCapability()))
						result.add(table);
					else
					{
						result.clear();
						return result;
					}
				}
			}
		}
		
		return result;
	}
	
	public static boolean canCopy(ISelection selection)
	{
		boolean enabled = true;
		
		_selection = filterSelection(selection);
		
		if(selection instanceof IStructuredSelection)
		{
			if(((IStructuredSelection)selection).size()!=_selection.size())
				return false;
		}
		

		
		if(_selection.size()<1)
			enabled = false;
		else
		{
			// No permitimos copiar tablas de diferentes esquemas
			ESchema sourceSchema = _selection.get(0).getSchema();
	
			for(ETable table:_selection)
			{
				if(sourceSchema!=table.getSchema())
					enabled = false;
			}
		}
		
		return enabled;
	}
	
	@Override
	public boolean updateSelection(IStructuredSelection selection)
	{
		return canCopy(selection);
	}
	
	@Override
	public void run() {
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		
		CompoundCommand cc = new CompoundCommand();

		cc.append(getCopyCommand( _selection, editingDomain));
		
		editingDomain.getCommandStack().execute(cc);	
	}
	
	public static Command getCopyCommand(ArrayList<ETable> tables, TransactionalEditingDomain editingDomain)
	{
		return CopyToClipboardCommand.create(editingDomain, tables);
	}
}
