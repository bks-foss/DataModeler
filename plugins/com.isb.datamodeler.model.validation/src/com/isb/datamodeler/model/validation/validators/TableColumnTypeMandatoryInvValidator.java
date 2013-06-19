package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.tables.EColumn;

/**
 * Validación de correción de modelo de datos. Validacion Obligatoria
 * El tipo de las columnas de una tabla es obligatorio
 * 
 * @author Delia Salmerón
 *
 */
public class TableColumnTypeMandatoryInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {
		
		if(eObject instanceof EColumn)
		{
			EColumn column = (EColumn)eObject;
			if(column.getTable().getSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			if(column.getDataType().getName().trim().length()==0)
			{
				// La columna '{0}' de '{1}' no tiene tipo.
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("TableColumnTypeMandatoryInvValidator.validation",new String[]{column.getName(),column.getTable().getName()}),//$NON-NLS-1$
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode());
			}
			
		}
		return null;
	}

}
