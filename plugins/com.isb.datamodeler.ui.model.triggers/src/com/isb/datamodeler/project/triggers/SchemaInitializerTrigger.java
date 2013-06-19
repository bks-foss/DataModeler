package com.isb.datamodeler.project.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.model.triggers.AbstractInitializerTrigger;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.project.EProject;

public class SchemaInitializerTrigger extends AbstractInitializerTrigger {

	private EProject _project;
	private ESchema _newSchema;
	
	public SchemaInitializerTrigger(TransactionalEditingDomain domain, EProject owner, ESchema newSchema) {
		super(domain);
		
		_project = owner;
		_newSchema = newSchema;
	}

	@Override
	public void executeTrigger() {
		initializeBasics(_newSchema, _project.getSchemas(), "Schema");
	}

}
