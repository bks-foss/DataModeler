package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.tables.EColumn;

/**
 * Validación de corrección del modelo de datos. Validación obligatoria.
 * Las columnas que forman parte de una PK deben ser no anulables.
 */
public class NonNullablePKColumnValidator extends AbstractDataModelerElementValidator 
{
	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		if (eObject instanceof EPrimaryKey) 
		{
			EPrimaryKey pk = (EPrimaryKey)eObject;
			
			if(pk.getBaseTableSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			for (EColumn column : ((EPrimaryKey)eObject).getMembers()) 
			{
				if (column.isNullable()) {
					return new DataModelDiagnostic(
							DataModelDiagnostic.ERROR, 
							Messages.bind("NonNullablePKColumnValidator.validation", 			//$NON-NLS-1$
									new String[] {column.getName(), 
										((EPrimaryKey)eObject).getBaseTable().getName()}),	
							getId(), 
							new Object[] { eObject }, 
							getCategory(), getCode());
				}
			}
		}
		return null;
	}

}
