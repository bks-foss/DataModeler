package com.isb.datamodeler.ui.validation.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.model.validation.Messages;
import com.isb.datamodeler.ui.project.EProject;
/**
 * Validacion de correción de modelo.Validación obligatoria.<br>
 * No se pueden repetir nombres de esquemas en todo el modelo
 *
 */
public class DuplicatedSchema extends AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {
		if(!(eObject instanceof EProject))
			return null;
		DataModelDiagnostic diagnostic = new DataModelDiagnostic();
		EProject project = (EProject)eObject;
		List<String> schemaNames = new ArrayList<String>();
		for(ESchema schema:project.getSchemas())
		{
			if(schemaNames.contains(schema.getName()))
				diagnostic.add(new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("DuplicatedSchema.validation",schema.getName()),//$NON-NLS-1$ 
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode()));
			else schemaNames.add(schema.getName());
		}

		return diagnostic;
	}

}
