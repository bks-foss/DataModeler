package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.ParentCardinality;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.tables.EColumn;

/**
 * Validación de corrección del modelo de datos. Validación obligatoria.<br>
 * La cardinalidad de una relación debe ser compatible con la nulabilidad de sus columnas: 
 * <li>debe ser 1 si todas las columnas referenciadas son no anulables". 
 * <li>debe ser '0..1' si alguna columna referenciada es anulable". 
 */
public class RelationCardinalityValidator extends AbstractDataModelerElementValidator 
{
	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		if (eObject instanceof EForeignKey) {
			EForeignKey fk = (EForeignKey)eObject;
			if(fk.getBaseTableSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			ParentCardinality pc = fk.getParentCardinality();
			boolean hasNullableColumns = false;
			for (EColumn col : fk.getMembers()) {
				if (col.isNullable()) {
					hasNullableColumns = true;
					if (pc.getValue() != ParentCardinality.ZERO_ONE_VALUE) {
						return new DataModelDiagnostic(
								DataModelDiagnostic.ERROR, 
								Messages.bind("RelationCardinalityValidator.zero_one.validation", 	//$NON-NLS-1$
										fk.getName()),	
								getId(), 
								new Object[] { eObject }, 
								getCategory(), getCode());
					}
				}
			}
			if (!hasNullableColumns && pc.getValue() != ParentCardinality.ONE_VALUE) {
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("RelationCardinalityValidator.one.validation", 			//$NON-NLS-1$
								fk.getName()),	
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode());
			}	
		}
		return null;
	}
}
