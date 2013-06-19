package com.isb.datamodeler.internal.ui.views.actions.refactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.internal.ui.dialogs.TableNameValidator;
import com.isb.datamodeler.internal.ui.views.actions.DataModelerCopier;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.project.EProject;

public class MoveDataModelerElementAction extends SelectionListenerAction
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8547556621070369909L;

	private Collection<ETable> _selection;
	
	private ESchema _sourceSchema;
	private EProject _sourceProject;
	private ESchema _targetSchema;
	
	public MoveDataModelerElementAction() {
		super(Messages.bind("MoveDataModelerElementAction.name"));
	}
	
	@Override
	public boolean updateSelection(IStructuredSelection selection)
	{
		if((selection instanceof IStructuredSelection) && 
				((IStructuredSelection)selection).size()>0)
		{
			_selection = new ArrayList<ETable>();
			
			boolean enabled = true;
			
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
					
					// Si es externa no se permite mover
					if( ((EProject)table.getSchema().eContainer()).getCapability().
						equalsIgnoreCase(table.getSchema().getCapability()))
						_selection.add(table);
					else
						enabled = false;
				}
				else
				{
					enabled = false;
				}
			}
			
			
			if(_selection.size()<1)
				return false;
			
			// No permitimos mover tablas de diferentes esquemas
			_sourceSchema = ((List<ETable>)_selection).get(0).getSchema();
			_sourceProject = (EProject)_sourceSchema.eContainer();
			for(ETable table:_selection)
			{
				if(_sourceSchema!=table.getSchema())
					enabled = false;
			}
			
			// Si el proyecto solo tiene un esquema no tiene sentido mover ninguna tabla
			if(_sourceProject.getSchemas().size()<2)
				enabled = false;
		
			return enabled;
		}
		
		return false;
	}

	@Override
	public void run() {
		
		// Mostramos un dialogo para seleccionar el esquema de destino
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		
		ArrayList<ESchema> schemas = new ArrayList<ESchema>();
		for(ESchema schema:_sourceProject.getSchemas())
		{
			if(schema.getCapability().equalsIgnoreCase(_sourceProject.getCapability()) && 
					schema!=_sourceSchema)
				schemas.add(schema);
		}

		_targetSchema = UtilsDataModelerUI.chooseESchema(shell,schemas);
	
		if(_targetSchema!=null)
		{
			TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
			
			CompoundCommand cc = new CompoundCommand();		
			
			// Bugzilla 21894. No funciona el mover con contraints implicadas. No elimina las constraints originales.
			DataModelerCopier copier = new DataModelerCopier();
			copier.setTargetProject(_sourceProject);
			copier.setTargetSchema(_targetSchema);			
			Collection<ETable> selectedTables = copier.copyAll(_selection);
			
			// Primero borramos del esquema original las tablas, de ese modo forzamos el borrado de las constraints
			// Bugzilla 21894 No funciona el mover con contraints implicadas. No elimina las constraints originales.
			cc.append(new DeleteCommand(editingDomain, _selection));	
			
			// Segundo añadimos a al esquema
			cc.append(new AddCommand(editingDomain, _targetSchema, 
					ESchemaPackage.Literals.SCHEMA__TABLES, selectedTables));							

			for(ETable selectedTable:selectedTables)
			{
				String tableName = selectedTable.getName();
				for(ETable table:_targetSchema.getTables()){
					if(table.getName().equalsIgnoreCase(tableName))
					{
						InputDialog dialog = new InputDialog(shell, Messages.bind("Copier.copyNameAttribute.InputDialog.title"), //$NON-NLS-1$
								Messages.bind("Copier.copyNameAttribute.InputDialog.message")+" '"+tableName+"'.",
								tableName,new TableNameValidator(_targetSchema, new ArrayList<String>())); //$NON-NLS-1$
						
						if (dialog.open() != Window.OK) {
							return;
						}		
						tableName = dialog.getValue();
						
						cc.append(new SetCommand(editingDomain, selectedTable, 
								ESchemaPackage.Literals.DATA_MODELER_NAMED_ELEMENT__NAME, tableName));
					}
				}
			}
			
			editingDomain.getCommandStack().execute(cc);
		}
	}

}
