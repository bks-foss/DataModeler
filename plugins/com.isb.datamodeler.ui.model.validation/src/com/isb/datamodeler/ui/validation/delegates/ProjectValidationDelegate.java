package com.isb.datamodeler.ui.validation.delegates;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EValidator.ValidationDelegate;

import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.core.validation.DataModelerValidationManager;
import com.isb.datamodeler.model.core.validation.db.AbstractDataModelerDBValidator;
import com.isb.datamodeler.model.core.validation.db.DataModelerValidationDBManager;
import com.isb.datamodeler.model.validation.UtilsValidations;
import com.isb.datamodeler.schema.ESQLObject;

public class ProjectValidationDelegate implements ValidationDelegate {

	@Override
	public boolean validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {
		
		List<AbstractDataModelerElementValidator> invariantValidators = DataModelerValidationManager.getInstance().getValidators((EModelElement)eObject);

		boolean isValid = true;
		if(invariantValidators!=null)
		{
			BasicDiagnostic diagnostic = (BasicDiagnostic)context.get("Diagnostics");//$NON-NLS-1$
			
			for(AbstractDataModelerElementValidator validator:invariantValidators) 
			{
				if(validator.dependOnDb())
				{
					AbstractDataModelerDBValidator validatorDB =  DataModelerValidationDBManager.getInstance().getValidatorForValidatorDBId(
							validator.getValidatorDBId(), eObject, UtilsValidations.getDataBaseId(eObject));
					if(validatorDB!=null)
					{
						BasicDiagnostic basicDiagnostic = validatorDB.validate(eClass, eObject, context, invariant, expression);
						if(basicDiagnostic != null)
						{
							diagnostic.add(new DataModelDiagnostic(
									basicDiagnostic.getSeverity(), 
									basicDiagnostic.getMessage(),
									validator.getId(), 
									basicDiagnostic.getData().toArray(), 
									validator.getCategory(),
									validator.getCode(),
									UtilsValidations.getDataBaseName(eObject)));
						}
						isValid = isValid && false;
					}	
				}
				else
				{
					DataModelDiagnostic vegaDiagnostic = validator.validate(eClass, eObject, context, invariant, expression);
					if(vegaDiagnostic!=null && vegaDiagnostic.getSeverity()!=Diagnostic.OK)
					{
						if (diagnostic != null) 
						{
							diagnostic.add(vegaDiagnostic);
						}
						isValid = isValid && false;
					}
				}
			}
			
		}
		return isValid;
	}

	@Override
	public boolean validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, String constraint, String expression) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean validate(EDataType eDataType, Object value,
			Map<Object, Object> context, String constraint, String expression) {
		// TODO Auto-generated method stub
		return true;
	}
}
