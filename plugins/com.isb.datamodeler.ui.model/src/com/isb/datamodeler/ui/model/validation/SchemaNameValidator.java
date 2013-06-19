package com.isb.datamodeler.ui.model.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.ecore.EClass;

import com.isb.datamodeler.model.core.validation.db.AbstractDataModelerDBValidator;
import com.isb.datamodeler.model.core.validation.db.DataModelerValidationDBManager;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;

public class SchemaNameValidator extends AbstractFieldValidator {

	private ESchema _schema;
	private String _dataBaseId;
	private EClass _eClass;
	public SchemaNameValidator(ESchema schema , String dataBaseId)
	{
		super();
		_schema = schema;
		_dataBaseId = dataBaseId;
	}
	public SchemaNameValidator(EClass schemaClass , String dataBaseId)
	{
		super();
		_eClass = schemaClass;
		_dataBaseId = dataBaseId;
	}
	@Override
	public IStatus validate(String value, String fieldName) {
		List<AbstractDataModelerDBValidator> validators;
		if(_schema !=null)
			validators = DataModelerValidationDBManager.getInstance().getValidatorsForEObjectAsList(_dataBaseId ,_schema);
		else validators = DataModelerValidationDBManager.getInstance().getValidatorsForEClassAsList(_dataBaseId, _eClass);
		
		for(AbstractDataModelerDBValidator v:validators)
		{
			BasicDiagnostic diagnostic = v.validate(_schema ,fieldName, value);
			if(diagnostic!=null)
				return new Status(IStatus.ERROR, "not_used", 0, diagnostic.getMessage(), null);
		}
		
		return Status.OK_STATUS;
	}
}
