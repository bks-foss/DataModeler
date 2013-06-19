package com.isb.datamodeler.ui.triggers.configurators.tables;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.osgi.util.NLS;

import com.isb.datamodeler.model.triggers.IParametrizableTrigger;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.tables.triggers.SetColumnNameTrigger;
import com.isb.datamodeler.ui.triggers.configurators.UITriggerConfiguratorWithToogle;
import com.isb.datamodeler.ui.triggers.configurators.messages.TriggerOptionsMessages;

public class SetColumnNameTriggerConfigurator extends UITriggerConfiguratorWithToogle {

	private static final String RENAME_PARENT_COLUMN = TriggerOptionsMessages.ColumnTriggersConfigurator_RenameParentColumn;

	private static final String REMOVE_UNIQUE_CONSTRAINT_MEMBERS_TOGGLE_LABEL = TriggerOptionsMessages.ReferenceConstraintTriggersConfigurator_Toogle_Label;

	/**
	 * Pregunta al usuario si desea renombrar las columnas hijas 
	 */
	@Override
	public void configure(IParametrizableTrigger parametrizableTrigger) {
		
		Assert.isTrue(parametrizableTrigger instanceof SetColumnNameTrigger, "Invalid Trigger Type");
		
		SetColumnNameTrigger trigger = (SetColumnNameTrigger)parametrizableTrigger;
		
		boolean renameColumn = false;
		
		List<EColumn> columnsToRename = trigger.getAllMatchingColumns();
		
		for(EColumn columnToRename:columnsToRename)
		{
			ETable parentTable = columnToRename.getTable();
			
			String msg = NLS.bind(
					TriggerOptionsMessages.ColumnTriggersConfigurator_RenameChildColumn, 
					new Object[] {columnToRename.getName(), parentTable.getName()}); 
		
			renameColumn = openYesNoQuestion(
				RENAME_PARENT_COLUMN, 
				msg,
				REMOVE_UNIQUE_CONSTRAINT_MEMBERS_TOGGLE_LABEL, 
				false);
			
			if(renameColumn)
				trigger.getChildColumnsToRename().add(columnToRename);
			
		}
		

	}
	
}
