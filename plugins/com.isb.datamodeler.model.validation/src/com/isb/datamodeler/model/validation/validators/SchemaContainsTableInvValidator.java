package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.schema.ESchema;
/**
 * Validacion de correción de modelo.Validación obligatoria.<br>
 * Un esquema debe tener alguna tabla modelada
 */
public class SchemaContainsTableInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {
		if(eObject instanceof ESchema)
		{
			ESchema eSchema = (ESchema)eObject;
			if(eSchema.getTables().isEmpty())
			{
				//Error: "El esquema <nombreEsquema> no contiene ninguna tabla". 
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("SchemaContainsTableInvValidator.validation", eSchema.getName()),	
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode());

			}
		}
		return DataModelDiagnostic.OK_INSTANCE;
	}

}
