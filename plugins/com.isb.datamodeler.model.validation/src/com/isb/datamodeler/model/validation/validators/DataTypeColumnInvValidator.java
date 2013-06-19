package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.datatypes.EBinaryStringDataType;
import com.isb.datamodeler.datatypes.ECharacterStringDataType;
import com.isb.datamodeler.datatypes.EDataLinkDataType;
import com.isb.datamodeler.datatypes.EDataType;
import com.isb.datamodeler.datatypes.EExactNumericDataType;
import com.isb.datamodeler.datatypes.ENumericalDataType;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.tables.EColumn;
/**
 * Validación de correción del modelo de datos. Validación Obligatoria.
 * Las columnas deben tener definidos los parámetros asociados a su tipo de
 * datos con valores válidos.
 * 
 * @author Delia Salmerón
 *
 */
public class DataTypeColumnInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		if(eObject instanceof EColumn)
		{
			EColumn column = (EColumn)eObject;
			if(column.getTable().getSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			EDataType dataType = column.getDataType();
			// Utilizo instanceof y NO utilizo el datatypesSwitch porque necesito mostrar el nombre de la 
			// columna y no lo puedo obtener a partir del EDataType
			// 1."La columna 'nombreColumna' de 'nombreElemento' no tiene longitud". (Nunca va a darse , por defecto es 0.)
			// 2."La columna 'nombreColumna' de 'nombreElemento' no tiene precision". (Nunca va a darse , por defecto es 0.)
			// 2."La columna 'nombreColumna' de 'nombreElemento' no tiene escala". (Nunca va a darse , por defecto es 0.)
			if(dataType instanceof EBinaryStringDataType)
			{
				EBinaryStringDataType object = (EBinaryStringDataType)dataType;
				// Longitud: El párametro longitud de la columna '{0}' de '{1}' debe ser mayor que 0.
				if(object.getLength()==0)
					return new DataModelDiagnostic(
							DataModelDiagnostic.ERROR, 
							Messages.bind("DataTypeColumnInvValidator.validation.length",//$NON-NLS-1$
									new String[]{column.getName(),column.getTable().getName()}),
							getId(), 
							new Object[] { eObject }, 
							getCategory(), getCode());
								
				return null;
				
			}
			else if(dataType instanceof ECharacterStringDataType)
			{
				ECharacterStringDataType object = (ECharacterStringDataType)dataType;
				// Longitud: El párametro longitud de la columna '{0}' de '{1}' debe ser mayor que 0.
				if(object.getLength()==0)
					return new DataModelDiagnostic(
							DataModelDiagnostic.ERROR, 
							Messages.bind("DataTypeColumnInvValidator.validation.length",//$NON-NLS-1$
									new String[]{column.getName(),column.getTable().getName()}),
							getId(), 
							new Object[] { eObject }, 
							getCategory(), getCode());
				
				return null;
				
			}
			else if(dataType instanceof EDataLinkDataType)
			{
				EDataLinkDataType object = (EDataLinkDataType)dataType;
				// Longitud El párametro longitud de la columna '{0}' de '{1}' debe ser mayor que 0.
				if(object.getLength()==0)
					return new DataModelDiagnostic(
							DataModelDiagnostic.ERROR, 
							Messages.bind("DataTypeColumnInvValidator.validation.length",//$NON-NLS-1$
									new String[]{column.getName(),column.getTable().getName()}),
							getId(), 
							new Object[] { eObject }, 
							getCategory(), getCode());
				
				return null;
				
			}
			else if(dataType instanceof EExactNumericDataType)
			{
				EExactNumericDataType object = (EExactNumericDataType)dataType;
				DataModelDiagnostic diagnostic = new DataModelDiagnostic();
				// Precisión:El párametro precisión de la columna '{0}' de '{1}' debe ser mayor que 0.
				if(object.getPrecision()==0)
					diagnostic.add(new DataModelDiagnostic(
							DataModelDiagnostic.ERROR, 
							Messages.bind("DataTypeColumnInvValidator.validation.precision",//$NON-NLS-1$
									new String[]{column.getName(),column.getTable().getName()}),
							getId(), 
							new Object[] { eObject }, 
							getCategory(), getCode()));
				// Escala: La columna '{0}' de '{1}' no tiene escala.
//				if(object.getScale()==0)
//					diagnostic.add(new DataModelDiagnostic(
//							DataModelDiagnostic.ERROR, 
//							Messages.bind("DataTypeColumnInvValidator.validation.scale",//$NON-NLS-1$
//									new String[]{column.getName(),column.getTable().getName()}),
//							getId(), 
//							new Object[] { eObject }, 
//							getCategory(), getCode()));
				
				
				return diagnostic;
				
			}
			else if(dataType instanceof ENumericalDataType)
			{
				ENumericalDataType object = (ENumericalDataType)dataType;
				// Precisión: :El párametro precisión de la columna '{0}' de '{1}' debe ser mayor que 0.
				if(object.getPrecision()==0)
					return new DataModelDiagnostic(
							DataModelDiagnostic.ERROR, 
							Messages.bind("DataTypeColumnInvValidator.validation.precision",//$NON-NLS-1$
									new String[]{column.getName(),column.getTable().getName()}),
							getId(), 
							new Object[] { eObject }, 
							getCategory(), getCode());
				
			}
		}
		return null;
	
	}
	
}
