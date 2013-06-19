package com.isb.datamodeler.ui.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.ui.model.validation.Messages;
import com.isb.datamodeler.ui.project.EProject;
/**
 * Reglas de Normativa. Validacion de modelo. Validacion Opcional
 * El modelo debe tener al menos un esquema.
 * 
 * @author Delia Salmerón
 *
 */
public class SchemaInModelRegulationRuleInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		if(!(eObject instanceof EProject))
			return null;
		
		EProject project = (EProject)eObject;
		
		if(project.getSchemas().size()==0)
		{
			return new DataModelDiagnostic(
					DataModelDiagnostic.ERROR, 
					Messages.bind("SchemaInModelRegulationRuleInvValidator"),//$NON-NLS-1$ 
					getId(), 
					new Object[] { eObject }, 
					getCategory(), getCode());
		}

		return DataModelDiagnostic.OK_INSTANCE;
	}

}
