package com.isb.datamodeler.model.validation.delegates;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
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
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.model.validation.UtilsValidations;
import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.ui.project.EProject;

public class SQLObjectValidationDelegate implements ValidationDelegate {

	@Override
	public boolean validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		if(!(eObject instanceof EModelElement))
			return true;

		List<AbstractDataModelerElementValidator> invariantValidators = DataModelerValidationManager.getInstance().getValidators((EModelElement)eObject);

		boolean isValid = true;
		BasicDiagnostic diagnostic = (BasicDiagnostic)context.get("Diagnostics");//$NON-NLS-1$
		// El framework de validacion actual ejecuta dos veces la validacion de invariants cuando
		// el super del objeto a validar tiene asociada una validacion de tipo invariant y esta en el mismo paquete que el validador.
		// Ej: Tablas y restricciones, tiene el ESQLObject como supertipo el cual tiene asociada una invariant(isValid()). En este caso 
		// segun el algoritmo definido en EObjectValidator$DynamicEClassValidator.validate(EClass , EObject , DiagnosticChain , Map<Object, Object> )
		// las validaciones invariants se ejecutan dos veces.
		// Sin embargo las columnas tiene como super tipo ETypedElement, entonces la validacion que se ejecuta es la validacion 
		// para el ETypedElement que no tiene asociada ninguna invariants. La validacion se ejecutaria una unica vez
		// Por este motivo a?adimos un indicador para saber si la validacion ya se ha ejecutado
		// @ see SchemaValidator
		Boolean validate =(Boolean)context.get("validate");
		if(validate==Boolean.TRUE)
			return true;
		
		context.put("validate", Boolean.TRUE);	
		
		for(AbstractDataModelerElementValidator validator:invariantValidators) 
		{
			if(validator.dependOnDb())
			{
				AbstractDataModelerDBValidator validatorDB =  DataModelerValidationDBManager.getInstance().getValidatorForValidatorDBId(
						validator.getValidatorDBId(), ((ESQLObject)eObject), UtilsValidations.getDataBaseId(eObject));
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
		
		return isValid;
			

	}

	@Override
	public boolean validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, String constraint, String expression) 
	{
		// Validacion de constrainst a nivel de objeto, hay que lanzar la validacion
		return true;
	}

	@Override
	public boolean validate(EDataType eDataType, Object value,
			Map<Object, Object> context, String constraint, String expression) 
	{
//		// Validacion de constrainst a nivel de DataType, se lanza al modificar 
//		// un EDataType(descripcion)
//		if((expression.equals("descriptionShort") && ((String)value).length()>254) )//$NON-NLS-1$
//		{
//			DiagnosticChain diagnostics = (DiagnosticChain)context.get("Diagnostics");//$NON-NLS-1$
//			
//			diagnostics.add(new BasicDiagnostic("EDataType" ,BasicDiagnostic.ERROR , 
//					Messages.bind("SQLObjectValidationDelegate.validation.description" , "254") , //$NON-NLS-1$
//					new Object[]{eDataType}));
//			return false;
//		}
//		else if((expression.equals("descriptionLarge") && ((String)value).length()>1250) )//$NON-NLS-1$
//		{
//			DiagnosticChain diagnostics = (DiagnosticChain)context.get("Diagnostics");//$NON-NLS-1$
//			
//			diagnostics.add(new BasicDiagnostic("EDataType" ,BasicDiagnostic.ERROR , 
//					Messages.bind("SQLObjectValidationDelegate.validation.description" , "1250") , //$NON-NLS-1$
//					new Object[]{eDataType}));
//			return false;
//		}
		return true;
	}
}
