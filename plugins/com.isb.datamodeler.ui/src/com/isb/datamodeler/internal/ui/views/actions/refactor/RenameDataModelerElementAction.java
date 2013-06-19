package com.isb.datamodeler.internal.ui.views.actions.refactor;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.internal.ui.dialogs.SchemaNameValidator;
import com.isb.datamodeler.internal.ui.dialogs.TableNameValidator;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.ui.project.EProject;

public class RenameDataModelerElementAction extends SelectionListenerAction
{	
	private ESchema _schema;
	private EProject _project;
	private EBaseTable _table;
	private EObject _selected;
	
	public RenameDataModelerElementAction() {
		super(Messages.bind("RenameDataModelerElementAction.name"));
	}
	
	@Override
	public void run() {
		
		// Mostramos un dialogo para cambiar el nombre	
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		
		String elementName;
		
		IInputValidator validator;
		
		if(_table!=null)
		{
			elementName = _table.getName();
			validator = new TableNameValidator(_schema , new ArrayList<String>());
		}
		else
		{
			elementName = _schema.getName();
			validator = new SchemaNameValidator(_project);
		}
		
		InputDialog dialog = new InputDialog(shell, Messages.bind("Copier.copyNameAttribute.InputDialog.title"), //$NON-NLS-1$
				Messages.bind("Copier.copyNameAttribute.InputDialog.message")+" '"+elementName+"'.",elementName, validator); //$NON-NLS-1$
		
		if (dialog.open() != Window.OK) {
			return;
		}
		
		elementName = dialog.getValue();
	
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		
		editingDomain.getCommandStack().execute(new SetCommand(editingDomain, _selected, 
				ESchemaPackage.Literals.DATA_MODELER_NAMED_ELEMENT__NAME, elementName));
	}
	
	@Override
	public boolean updateSelection(IStructuredSelection selection)
	{
		boolean enabled = false;
	
		if((selection instanceof IStructuredSelection) && 
				((IStructuredSelection)selection).size()==1)
		{
			DatamodelerDomainNavigatorItem firstElement = (DatamodelerDomainNavigatorItem)(((IStructuredSelection)selection).getFirstElement());

			_selected = firstElement.getEObject();
			
			if(_selected instanceof EBaseTable)
			{
				enabled = true;
				_table = (EBaseTable)_selected;
				_schema = _table.getSchema();
				_project = (EProject)_schema.eContainer();
			}
			else if(_selected instanceof ESchema)
			{
				enabled = true;
				_table = null;
				_schema = (ESchema)_selected;
				_project = (EProject)_schema.eContainer();				
			}
			else
				enabled= false;
			// No se permite renombrar nodos externos
			if(_schema!=null && !_schema.getCapability().equalsIgnoreCase(_project.getCapability()))
				enabled=false;
		}
		return enabled;
	}


}
