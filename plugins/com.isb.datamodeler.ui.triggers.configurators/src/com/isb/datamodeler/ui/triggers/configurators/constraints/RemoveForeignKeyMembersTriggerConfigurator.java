package com.isb.datamodeler.ui.triggers.configurators.constraints;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.osgi.util.NLS;

import com.isb.datamodeler.constraints.triggers.RemoveForeignKeyMembersTrigger;
import com.isb.datamodeler.model.triggers.IParametrizableTrigger;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.triggers.configurators.UITriggerConfiguratorWithToogle;
import com.isb.datamodeler.ui.triggers.configurators.messages.TriggerOptionsMessages;

public class RemoveForeignKeyMembersTriggerConfigurator extends UITriggerConfiguratorWithToogle {

	private static final String REMOVE_UNIQUE_CONSTRAINT_MEMBERS = TriggerOptionsMessages.ReferenceConstraintTriggersConfigurator_Remove_Unique_Constraint_Members;

	private static final String REMOVE_UNIQUE_CONSTRAINT_MEMBERS_TOGGLE_LABEL = TriggerOptionsMessages.ReferenceConstraintTriggersConfigurator_Toogle_Label;

	/**
	 * Para la lista de Triggers, pregunta al usuario si desea eliminar las columnas que se quitan de las FKS de las tablas hijas 
	 */
	@Override
	public void configure(IParametrizableTrigger parametrizableTrigger) {
		
		Assert.isTrue(parametrizableTrigger instanceof RemoveForeignKeyMembersTrigger, "Invalid Trigger Type");
		
		RemoveForeignKeyMembersTrigger trigger = (RemoveForeignKeyMembersTrigger)parametrizableTrigger;
		
		boolean removeChildColumns = false;
		
		List<EColumn> columns = trigger.getForeignKeyMembersToRemove();
		
//		ETable parentTable = trigger.getTable();
		if(columns.isEmpty())
			return;
		
		ETable parentTable = columns.get(0).getTable();
		
		if(trigger.getForeignKey()==null || parentTable==null)
			return;
		
		
		String listOfRemovedMembers = "";
		
		for(EColumn column: columns)
			listOfRemovedMembers += column.getName() + ","; 
		
		String msg = NLS.bind(
				TriggerOptionsMessages.ReferenceConstraintTriggersConfigurator_Remove_Unique_Constraint_Members_msg, 
				new Object[] {listOfRemovedMembers, parentTable.getName(), trigger.getForeignKey().getName()}); 
	
		removeChildColumns = openYesNoQuestion(
			REMOVE_UNIQUE_CONSTRAINT_MEMBERS, 
			msg,
			REMOVE_UNIQUE_CONSTRAINT_MEMBERS_TOGGLE_LABEL, 
			false);
		
		trigger.setRemoveColumnFromTable(removeChildColumns);

	}
	
}
