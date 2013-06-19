package com.isb.datamodeler.tables.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.BaseCardinality;
import com.isb.datamodeler.constraints.EConstraint;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.constraints.ParentCardinality;
import com.isb.datamodeler.model.triggers.AbstractInitializerTrigger;
import com.isb.datamodeler.model.triggers.DataModelerTriggersPlugin;
import com.isb.datamodeler.model.triggers.IParametrizableTrigger;
import com.isb.datamodeler.model.triggers.initializers.IDatamodelerInitializer;
import com.isb.datamodeler.tables.EBaseTable;

/**
 * Comando encargado de la inicializacion de una nueva Constraint
 * @author xIS05655
 *
 */
public class TableConstraintInitializerTrigger extends AbstractInitializerTrigger implements IParametrizableTrigger{
	
	private EBaseTable _parentTable;
	private EConstraint _newConstraint;
	private String _constraintName = "";
	private EUniqueConstraint _fkUniqueContraint; //UK seteada por el configurador que se asignará a la nueva FK
	
	public TableConstraintInitializerTrigger(TransactionalEditingDomain domain, EBaseTable owner, EConstraint newConstraint) {
		super(domain);
		
		_parentTable = owner;
		_newConstraint = newConstraint;
	}

	@Override
	public void executeTrigger() {

		IDatamodelerInitializer initializer = DataModelerTriggersPlugin.getInstance().getInitializer(_parentTable); 
		if(initializer!=null)
			initializer.initialize(_parentTable, _newConstraint);

		if(_newConstraint instanceof EUniqueConstraint)
		{
			//Si la tabla es referenciada por alguna FK que no tiene su UniqueConstraint destino asociada, le asociamos esta nueva UK
			for(EForeignKey fk:_parentTable.getReferencingForeignKeys())
				if(fk.getUniqueConstraint()==null)
					fk.setUniqueConstraint((EUniqueConstraint)_newConstraint);
			
		}
		else if(_newConstraint instanceof EForeignKey)
		{
			EForeignKey foreignKey = (EForeignKey)_newConstraint;
			
			foreignKey.setIdentifying(foreignKey.isDefaultIdentifying());
			foreignKey.setBaseCardinality(BaseCardinality.ZERO_MANY);
				
			if(_fkUniqueContraint!=null)
				foreignKey.setUniqueConstraint(_fkUniqueContraint);

			foreignKey.setParentCardinality(ParentCardinality.ONE);
		}
		else
			//NO DEBERIA LLEGAR ACA
			initializeBasics(_newConstraint, _parentTable.getConstraints(), _constraintName);	

	}

	public void setForeignKeyUniqueConstraint(EUniqueConstraint fkUniqueConstraint)
	{
		this._fkUniqueContraint = fkUniqueConstraint;
	}
	
	public EConstraint getNewConstraint()
	{
		return _newConstraint;
	}

}
