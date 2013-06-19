package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.tables.ETable;

/**
 * Validacion de corrección del modelo de datos.Validacion Obligatoria
 * La tabla debe tener al menos una columna
 * 
 * 
 * @author Delia Salmerón
 *
 */
public class TableHasColumnInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {

		// La tabla debe tener al menos una columna
		if(eObject instanceof ETable)
		{
			ETable eTable = (ETable)eObject;
			if(eTable.getSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			if(eTable.getColumns().size()==0)
			{
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("TableHasColumnInvValidator.validation",eTable.getName()),//$NON-NLS-1$
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode());
			}
		}
		return DataModelDiagnostic.OK_INSTANCE;
	}

}
