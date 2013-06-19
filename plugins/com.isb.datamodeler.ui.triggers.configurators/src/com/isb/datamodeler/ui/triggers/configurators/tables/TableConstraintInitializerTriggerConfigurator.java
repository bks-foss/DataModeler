package com.isb.datamodeler.ui.triggers.configurators.tables;

import org.eclipse.core.runtime.Assert;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.model.triggers.IParametrizableTrigger;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.triggers.TableConstraintInitializerTrigger;
import com.isb.datamodeler.ui.triggers.configurators.UITriggerConfigurator;
import com.isb.datamodeler.ui.triggers.configurators.messages.TriggerOptionsMessages;

/**
 * Configurador asociado al trigger TableConstraintInitializerTrigger
 * Pregunta al usuario cual es la UK referenciada por la nueva FK
 */
public class TableConstraintInitializerTriggerConfigurator extends UITriggerConfigurator {
	
	private static final String NEW_FOREIGN_KEY = TriggerOptionsMessages.TableConstraintInitializerTriggerConfigurator_New_FK;

	
	@Override
	public void configure(IParametrizableTrigger parametrizableTrigger) {
		
		Assert.isTrue(parametrizableTrigger instanceof TableConstraintInitializerTrigger, "Invalid Trigger Type");

		TableConstraintInitializerTrigger trigger = (TableConstraintInitializerTrigger)parametrizableTrigger;
		
		//Se está creando una nueva FK 
		if(trigger.getNewConstraint() instanceof EForeignKey)
		{
			EForeignKey foreignKey = (EForeignKey)trigger.getNewConstraint();
			EBaseTable referencedTable = foreignKey.getParentTable();
			
			//Si la tabla origen tiene mas de una UK, el usuario deberá elegir una (Bugzilla #20280)
			if(referencedTable.getUniqueConstraints().size()>1)
			{
				String msg = TriggerOptionsMessages.TableConstraintInitializerTriggerConfigurator_Select_UK; 
				
				String[] ukNames = new String[referencedTable.getUniqueConstraints().size()];
				int[] ukIds = new int[referencedTable.getUniqueConstraints().size()];

				
				for(int i=0;i<referencedTable.getUniqueConstraints().size();i++)
				{
					EUniqueConstraint uk = referencedTable.getUniqueConstraints().get(i);
					ukNames[i] = uk.getName();
					String members = uk.getMembers().get(0) == null ? "" : uk.getMembers().get(0).getName(); 
					for(int j=1;j<uk.getMembers().size();j++)
					{
						members=members+", "+uk.getMembers().get(j).getName();
					}
					ukNames[i] = uk.getName() +" (" + members +")";
					ukIds[i] = i;
				}
					
				EUniqueConstraint defaultUK = null;
				
				if(referencedTable.getPrimaryKey()!=null)
					defaultUK = referencedTable.getPrimaryKey();
				else 
					defaultUK = referencedTable.getUniqueConstraints().get(0); //Si llegamos aca, al menos una UK debe existir

				int defaultOption = referencedTable.getUniqueConstraints().indexOf(defaultUK);
				
				int selectedOption = openOptionsDialog(NEW_FOREIGN_KEY, 
					    msg, 
					    ukNames,
					    ukIds,
					    defaultOption,
					    -1);
				
				//EL usuario puede haber cerrado el dialogo con la X. En ese caso no se selecciona ninguna UK y la FK quedará sin UK.
				if(selectedOption!=-1)
					trigger.setForeignKeyUniqueConstraint(referencedTable.getUniqueConstraints().get(selectedOption));

			}else if(referencedTable.getUniqueConstraints().size()==1)
			{
				trigger.setForeignKeyUniqueConstraint(referencedTable.getUniqueConstraints().get(0));
			}
		}
	}

}
