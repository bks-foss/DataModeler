package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.datatypes.EBigCharacterStringDataType;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.tables.EColumn;
/**
 * Validación de modelo.Validación obligatoria.<br>
 * Las columnas miembro de una restricción PK/UK no pueden ser tipo LOB.
 *
 */
public class LOBColumnInConstraints extends AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {

		if(!(eObject instanceof EUniqueConstraint))
			return null;
		EUniqueConstraint uk = (EUniqueConstraint)eObject;
		if(uk.getBaseTable().getSchema().isExternal())
			return null;
		
		for(EColumn column:uk.getMembers())
		{
			if(column.getDataType() instanceof EBigCharacterStringDataType)
			{
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR,
						Messages.bind("LOBColumnInConstraints.validation",new String[]{column.getName(),column.getTable().getName(),uk.getName()}),//$NON-NLS-1$
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode());
			}
		}
		return DataModelDiagnostic.OK_INSTANCE;
	}

}
