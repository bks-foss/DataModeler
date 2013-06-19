package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;

/**
 * Validacion de corrección del modelo de datos.Validacion Obligatoria
 * Todas las restricciones de clave (PK;FK;UK)del modelo deben tener al menos una columna
 * 
 * 
 * @author Delia Salmerón
 *
 */
public class ConstraintsHasColumnInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {
		
		if(eObject instanceof EReferenceConstraint)
		{
			EReferenceConstraint eReferenceConstraint = (EReferenceConstraint)eObject;
			if(eReferenceConstraint.getBaseTable().getSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			if(eReferenceConstraint.getMembers().size()==0)
			{
				// La restricción '{0}' debe tener al menos una columna.
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("ConstraintsHasColumnInvValidator.validation.message",eReferenceConstraint.getName()),//$NON-NLS-1$
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode());
			}
		}
		return null;
	}

}
