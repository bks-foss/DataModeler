package com.isb.datamodeler.ui.triggers.configurators.constraints;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.triggers.AddForeignKeyReferencedMembersTrigger;
import com.isb.datamodeler.diagram.properties.DataModelerForeignKeyEditorDialog;
import com.isb.datamodeler.model.triggers.IParametrizableTrigger;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.ui.triggers.configurators.UITriggerConfiguratorWithToogle;
import com.isb.datamodeler.ui.triggers.configurators.dialogs.OptionsMessageDialogWithToggle;
import com.isb.datamodeler.ui.triggers.configurators.messages.TriggerOptionsMessages;

public class AddForeignKeyReferencedMemberTriggerConfigurator extends UITriggerConfiguratorWithToogle {

//	private static final String ADD_UNIQUE_CONSTRAINT_MEMBERS = TriggerOptionsMessages.ReferenceConstraintTriggersConfigurator_Add_Unique_Constraint_Members;
//	private static final String ADD_UNIQUE_CONSTRAINT_MEMBERS_TOGGLE_LABEL = TriggerOptionsMessages.ReferenceConstraintTriggersConfigurator_Toogle_Label;
	
	private static final String INVALID_OPTION_MESSAGE_TITLE = TriggerOptionsMessages.ReferenceConstraintTriggersConfigurator_Invalid_Option_Message_Title;
	
	private EForeignKey modifiedForeignKey; 
	
	
	@Override
	public void configure(IParametrizableTrigger parametrizableTrigger) {
		
		Assert.isTrue(parametrizableTrigger instanceof AddForeignKeyReferencedMembersTrigger, "Invalid Trigger Type");
		
		AddForeignKeyReferencedMembersTrigger trigger = (AddForeignKeyReferencedMembersTrigger)parametrizableTrigger;
		
		modifiedForeignKey = trigger.getForeignKey();
		
		//Opcion por defecto
		int defaultOption = getLastSelectedOption()!=0?getLastSelectedOption():AddForeignKeyReferencedMembersTrigger.USE_EXISTING_CHILD_COLUMNS_VALUE;
		
		EBaseTable baseTable = trigger.getForeignKey().getBaseTable();	
		EBaseTable parentTable = trigger.getForeignKey().getParentTable();
		
		if(baseTable==parentTable)
			defaultOption = AddForeignKeyReferencedMembersTrigger.CREATE_NEW_COLUMNS_VALUE;
		
		int selectedOption = defaultOption;

		//Para efectuar la propagacion , la lista de miembros debe tener la misma cantidad de elementos que la lista de columnas referenciadas por la FK
		int numberOfColumnsAdded = trigger.getNewFKReferencedMembers().size();
		
		boolean validForeignKey =  trigger.getForeignKey().getReferencedMembers().size() ==  trigger.getForeignKey().getMembers().size() + numberOfColumnsAdded;
		
		if(!validForeignKey)
			selectedOption = AddForeignKeyReferencedMembersTrigger.DO_NOTHING;
		
		// Si no hay coincidencias, seteamos la opcion por defecto (no hay nada que preguntar)
		// Si la FK es autoreferenciada seteamos la opción por defecto
		if(trigger.getForeignKey()!=null && validForeignKey && baseTable!=parentTable)	
		{		
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			
			DataModelerForeignKeyEditorDialog dialog = new DataModelerForeignKeyEditorDialog(shell,modifiedForeignKey);
	  	
			dialog.open();
			
			EList<?> result = dialog.getResult();
			
			selectedOption = AddForeignKeyReferencedMembersTrigger.SHOW_DIALOG;
			
			if(result!= null)
			{		
				// Obtenemos las listas de miembros a añadir y la de miembros a eliminar
				//ArrayList<EColumn> deletedMembers = new ArrayList<EColumn>(modifiedForeignKey.getMembers());	
				ArrayList<EColumn> deletedMembers = new ArrayList<EColumn>();
				ArrayList<EColumn> newMembers = new ArrayList<EColumn>();
				
				for(Object obj:result)
					if(obj instanceof EColumn)
						if(!findByName((EColumn)obj,modifiedForeignKey.getMembers()))
							newMembers.add((EColumn)obj);
						
				for(EColumn col:modifiedForeignKey.getMembers())
					if(!findByName(col,result))
						deletedMembers.add(col);

				trigger.setNewFKMembers(newMembers);
				trigger.setDeletedFKMembers(deletedMembers);
			}
		}
		trigger.setChildTableNewMeberAction(selectedOption);	
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
	
	protected OptionsMessageDialogWithToggle getDialog(
            String title, String message, String[] optionLabels, int[] optionValues, int defaultOption, String toggleMessage,
            boolean toggleState)
	{
		return new OptionsMessageDialogWithToggle( 
				   Display.getCurrent().getActiveShell(),
				   title, 
				   message, 
				   optionLabels,
				   optionValues,
				   defaultOption,
				   toggleMessage,
				   false){
					
					@Override
					protected void buttonPressed(int buttonId) {
						
						if(getToggleState() && getSelectedOption()==AddForeignKeyReferencedMembersTrigger.CREATE_NEW_COLUMNS_VALUE && isCyclicForeignKey(modifiedForeignKey) )						
						{
							String msg = NLS.bind(
									TriggerOptionsMessages.ReferenceConstraintTriggersConfigurator_Invalid_Option_Message, 
									new Object[] {modifiedForeignKey.getBaseTable().getName(), modifiedForeignKey.getParentTable().getName()}); 
							
							
							MessageDialog.openInformation(Display.getCurrent().getActiveShell(), INVALID_OPTION_MESSAGE_TITLE, msg);
							return;
						}
							
						super.buttonPressed(buttonId);
					}
					
					
		};
	}
	
	/**
	 * Devuelve true si hay una relacion ciclica entre la tabla base y la tabla padre con relaciones Identificativas (Bug #21086)
	 * @param foreignKey
	 * @return
	 */
	private boolean isCyclicForeignKey(EForeignKey foreignKey)
	{
		EBaseTable baseTable = foreignKey.getBaseTable();
		
		return isReferencedBy(foreignKey, baseTable);
	}
	
	private boolean isReferencedBy(EForeignKey foreignKey, EBaseTable baseTable)
	{
		if(!foreignKey.isIdentifying())
			return false;
		
		if(foreignKey.getParentTable()==baseTable)
			return true;
		
		for(EForeignKey fk: foreignKey.getParentTable().getForeignKeys())
			if(isReferencedBy(fk,baseTable))
				return true;
		
		return false;
	}
	

}
