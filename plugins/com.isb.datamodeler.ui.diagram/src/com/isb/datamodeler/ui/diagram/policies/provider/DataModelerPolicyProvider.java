package com.isb.datamodeler.ui.diagram.policies.provider;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.CreateEditPoliciesOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.IEditPolicyProvider;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey3EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.properties.DataModelerForeignKeyEditorDialog;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.tables.ETablesPackage;

public class DataModelerPolicyProvider extends AbstractProvider implements
		IEditPolicyProvider {

	@Override
	public boolean provides(IOperation operation) {

		if(operation instanceof CreateEditPoliciesOperation)
		{
			CreateEditPoliciesOperation op = (CreateEditPoliciesOperation)operation;
			
			EditPart editPart = op.getEditPart();
			
			if(editPart instanceof IGraphicalEditPart)
				return true;
		}
		return false;
	}

	@Override
	public void createEditPolicies(EditPart editPart) {
		
		//Eliminamos la Canonical Policy del diagrama para que no esté sincronizado y poder eliminar de diagrama.
		if(editPart instanceof SchemaEditPart)
		{
			editPart.removeEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
			editPart.removeEditPolicy(EditPolicy.CONTAINER_ROLE);
		}
		// Instalamos nuestro propio CONTAINER_ROLE
		editPart.installEditPolicy(EditPolicy.CONTAINER_ROLE, DMContainerEditPolicyCreator.getExtendedContainerEditPolicy(editPart));
		
		//Elimimanos el Drag & Drop de las columnas y Constraints (De todo lo que esta dentro de un comprartment)
		if(editPart instanceof CompartmentEditPart)
			editPart.removeEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE);
		
		//Hacemos ReadOnly los Shortcuts
		if(editPart instanceof IGraphicalEditPart)
		{
			View primaryView = ((IGraphicalEditPart)editPart).getPrimaryView();
			boolean isShortCut =  primaryView!=null && primaryView.getEAnnotation("Shortcut")!=null;
			
			if(isShortCut)
			{
				editPart.removeEditPolicy(EditPolicyRoles.POPUPBAR_ROLE);
				editPart.removeEditPolicy(EditPolicyRoles.CREATION_ROLE);
				editPart.removeEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE);
			}
				
		}
		if(editPart instanceof PersistentTableEditPart)
		{
			editPart.installEditPolicy(EditPolicyRoles.POPUPBAR_ROLE, new PersistentTablePopupBarEditPolicy());
		}
		// Eliminamos de las editpart los conectores para tirar relaciones
		if(editPart instanceof ShapeNodeEditPart)
		{
			editPart.removeEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE);
		}
		
		//En las notas solo dejamos la opcion "Eliminar del Diagrama"
		if(editPart instanceof NoteEditPart)
		{
			editPart.removeEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
			editPart.installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new SemanticEditPolicy(){
					protected Command getSemanticCommand(IEditCommandRequest editRequest) {
	
						if ( editRequest instanceof DestroyElementRequest ) {
							return null;
						}
						return super.getSemanticCommand( editRequest );
					}
			});
		}
		
		// Install OpenEditPolicy for ForeignKey EditParts  
		if(editPart instanceof ForeignKeyEditPart || editPart instanceof ForeignKey3EditPart)
		{
			ConnectionNodeEditPart foreignKeyEditPart = (ConnectionNodeEditPart)editPart;
			
			final EForeignKey fk = (EForeignKey)foreignKeyEditPart.resolveSemanticElement();
			
			foreignKeyEditPart.installEditPolicy(EditPolicyRoles.OPEN_ROLE,
					new OpenEditPolicy(){

					protected Command getOpenCommand(Request request) {
	
						Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
						
						DataModelerForeignKeyEditorDialog dialog = new DataModelerForeignKeyEditorDialog(shell,fk);
	              	
						dialog.open();
						
						EList<?> result = dialog.getResult();
						
						if(result!= null)
						{
	    					CompositeCommand cc = new CompositeCommand("FK Edit Command"); 
	    					
	    					// Obtenemos las listas de miembros a añadir y la de miembros a eliminar
	    					ArrayList<EColumn> deletedMembers = new ArrayList<EColumn>();
	    					ArrayList<EColumn> newMembers = new ArrayList<EColumn>();
	    					
	    					for(Object obj:result)
	    						if(obj instanceof EColumn)
	    							if(!findByName((EColumn)obj,fk.getMembers()))
	    								newMembers.add((EColumn)obj);
	    							
	    					for(EColumn col:fk.getMembers())
	    						if(!findByName(col,result))
	    							deletedMembers.add(col);
	    					
	    					// Creamos el comando
	    					for(EColumn deletedColumn:deletedMembers)
	    					{
		       					DestroyReferenceRequest destroyReferenceRequest = new DestroyReferenceRequest(fk,
		       							EConstraintsPackage.eINSTANCE.getReferenceConstraint_Members(), deletedColumn, false);
								cc.compose(new DestroyReferenceCommand(destroyReferenceRequest));
	    					}
	    					for(EColumn column:newMembers)
	    					{
								ETable baseTable = fk.getBaseTable();
								
		       					SetRequest setRequestAddColumn = new SetRequest(baseTable,ETablesPackage.eINSTANCE.getTable_Columns(),column);
								cc.compose(new SetValueCommand(setRequestAddColumn));

								SetRequest setRequestAddMember = new SetRequest(fk,EConstraintsPackage.eINSTANCE.getReferenceConstraint_Members(),column);
								cc.compose(new SetValueCommand(setRequestAddMember));
								
	    					}
	    					return new ICommandProxy(cc);
						}
						else
							return null;
					}
				});		
		}
	}
	
	private boolean findByName(EColumn column, EList<?> list)
	{
		for(Object element:list)
		{
			if(element instanceof EColumn)
			{
				EColumn col = (EColumn)element;
				if(col.getName().equalsIgnoreCase(column.getName()))
					return true;
			}
		}
		return false;
	}
}
