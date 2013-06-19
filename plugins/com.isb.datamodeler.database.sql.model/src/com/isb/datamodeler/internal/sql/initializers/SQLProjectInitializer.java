package com.isb.datamodeler.internal.sql.initializers;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import sqldatabase.SQLDatabase;

import com.isb.datamodeler.model.triggers.initializers.IDatamodelerInitializer;
import com.isb.datamodeler.schema.ECatalog;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.schema.ESchemaFactory;

public class SQLProjectInitializer implements IDatamodelerInitializer{

	public void initialize(EObject owner, List<ESQLObject> children) {
		for(ESQLObject child:children)
		{
			initialize(owner, child);
		}
	}

	@Override
	public void initialize(EObject owner, ESQLObject child) {
		if(child instanceof SQLDatabase)
		{
			EDatabase database = (EDatabase)child;

			ECatalog catalog = ESchemaFactory.eINSTANCE.createCatalog();
			catalog.setName("Default"); //$NON-NLS-1$
			database.getCatalogs().add(catalog);
			
			database.getPrimitiveDataTypes().addAll(database.loadPrimitiveDatatypes());
		}
	}
	

	@Override
	public String generateChildName(EObject owner, Class<?> eClass) {
		return "";
	}
}
