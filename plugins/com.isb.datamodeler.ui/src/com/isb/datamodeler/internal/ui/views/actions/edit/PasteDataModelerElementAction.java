package com.isb.datamodeler.internal.ui.views.actions.edit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.internal.ui.dialogs.TableNameValidator;
import com.isb.datamodeler.internal.ui.views.actions.DataModelerCopier;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.project.EProject;

public class PasteDataModelerElementAction extends SelectionListenerAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4802012613762107026L;

	private EProject _targetProject;
	
	private ESchema _targetSchema;
	
	

	private TransactionalEditingDomain _editingDomain = DataModelerUtils.getDataModelerEditingDomain();
	
	public PasteDataModelerElementAction() {
		super(Messages.bind("PasteDataModelerElementAction.name"));
	}
	
	@Override
	public boolean updateSelection(IStructuredSelection selection) {
		
		if(_editingDomain.getClipboard()==null)
			return false;
	
		boolean enabled = false;
		
		if((selection instanceof IStructuredSelection) && 
				((IStructuredSelection)selection).size()==1)
		{
			DatamodelerDomainNavigatorItem firstElement = (DatamodelerDomainNavigatorItem)(((IStructuredSelection)selection).getFirstElement());

			if(firstElement.getEObject() instanceof ESchema)
			{
				enabled = true;
				_targetSchema = (ESchema)firstElement.getEObject();
				_targetProject = (EProject)_targetSchema.eContainer();
				// No se permite la copia en nodos externos
				if(!_targetSchema.getCapability().equalsIgnoreCase(_targetProject.getCapability()))
					return false;
			}
			else
				return false;
			
			if(!enabled)
				return false;

			// Comprobamos que lo que hay en el clipboard son tablas
			if(_editingDomain.getClipboard()!=null)
			{
				for(Object object:_editingDomain.getClipboard())
				{
					if(!(object instanceof ETable))
					{
						return false;
					}	
				}
			}
		}

		return enabled;
	}


	
	@Override
	public void run() {
		
		CompoundCommand cc = new CompoundCommand();

		DataModelerCopier copier = new DataModelerCopier();
		
		copier.setTargetProject(_targetProject);
		copier.setTargetSchema(_targetSchema);
		Collection<Object> selectedTables = copier.copyAll(_editingDomain.getClipboard());
		
		cc.append(new AddCommand(_editingDomain, _targetSchema, 
				ESchemaPackage.Literals.SCHEMA__TABLES, selectedTables));
		
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		List<String> newTableNames = new ArrayList<String>();
		
		for(Object object:selectedTables)
		{
			ETable table = (ETable)object;
			String tableName = table.getName();
			
			for(ETable schemaTable:_targetSchema.getTables()){
				if(schemaTable.getName().equalsIgnoreCase(tableName))
				{
					InputDialog dialog = new InputDialog(shell, Messages.bind("Copier.copyNameAttribute.InputDialog.title"), //$NON-NLS-1$
							Messages.bind("Copier.copyNameAttribute.InputDialog.message")+" '"+tableName+"'.",
							tableName,new TableNameValidator(_targetSchema, newTableNames)); //$NON-NLS-1$
					
					if (dialog.open() != Window.OK) {
						return;
					}		
					tableName = dialog.getValue();
					newTableNames.add(tableName);
					cc.append(new SetCommand(_editingDomain, table, 
							ESchemaPackage.Literals.DATA_MODELER_NAMED_ELEMENT__NAME, tableName));
				}
			}
		}

		_editingDomain.getCommandStack().execute(cc);
	}	
}
