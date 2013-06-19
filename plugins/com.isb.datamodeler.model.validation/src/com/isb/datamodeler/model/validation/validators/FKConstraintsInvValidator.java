package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.tables.EColumn;
/**
 * Validación de correción del modelo de datos. Validación obligatoria.
 * El tipo de relación de una FK debe ser coherente con las columnas de la tabla: 
 * Error: "La restricción 'nombreRestricción' debe marcarse como identificativa, todas las columnas miembro de 'tablaHija' que la forman son PK." 
 * Error: "La restricción 'nombreRestricción' debe marcarse como no identificativa, no todas las columnas miembro de 'tablaHija' que la forman son PK." 
 *
 * @author Delia Salmerón
 *
 */
public class FKConstraintsInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {
		if(eObject instanceof EForeignKey)
		{
			boolean allColumnsArePK = true;
			EForeignKey fk = (EForeignKey)eObject;
			if(fk.getBaseTableSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			for (EColumn column : fk.getMembers())
			{
				if(!column.isPrimaryKey())
					allColumnsArePK = false;
			}
			if(fk.isIdentifying()&& !allColumnsArePK)
			{
				//Error:  "La restricción 'nombreRestricción' debe marcarse como no identificativa, no todas las columnas miembro de 'tablaHija' que la forman son PK."
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR,
						Messages.bind("FKConstraintsInvValidator.validation1",new String[]{fk.getName(),fk.getBaseTable().getName()}),//$NON-NLS-1$
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode());
			}
			else if(!fk.isIdentifying() && allColumnsArePK)
			{
				// Error: "La restricción 'nombreRestricción' debe marcarse como identificativa, todas sus columnas miembro son PK en 'tablaHija'"
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("FKConstraintsInvValidator.validation2",new String[]{fk.getName(),fk.getBaseTable().getName()}),//$NON-NLS-1$
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode());
			}
		}
		return null;
	}

}
