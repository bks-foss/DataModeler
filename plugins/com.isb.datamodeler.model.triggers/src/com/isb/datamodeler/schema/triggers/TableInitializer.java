package com.isb.datamodeler.schema.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

//import com.isb.datamodeler.model.core.DatamodelerCoreActivator;
//import com.isb.datamodeler.model.core.initializers.IDatamodelerInitializer;
import com.isb.datamodeler.model.triggers.AbstractInitializerTrigger;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.ETable;

public class TableInitializer extends AbstractInitializerTrigger {

	private ESchema _schema;
	private ETable _newTable;

	public TableInitializer(TransactionalEditingDomain domain, ESchema owner, ETable newTable) {
		super(domain);
		
		_schema = owner;
		_newTable = newTable;
	}

	@Override
	public void executeTrigger() {
//		IDatamodelerInitializer initializer = DatamodelerCoreActivator.getDefault().getInitializer(_schema); 
//		if(initializer!=null)
//			initializer.initialize(_schema, _newTable);
//		if(_newTable.getName()==null || _newTable.getName().isEmpty())
//			initializeBasics(_newTable, _schema.getTables(), DEFAULT_TABLE_NAME);
	}

}
