package com.isb.datamodeler.model.validation.validators;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.constraints.ETableConstraint;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.ETable;

/**
 * Resstricción Controlada por DataModeler. Validación Obligatoria
 * No se pueden repetir nombres en objetos de un mismo esquema.
 * 
 * @author Delia Salmerón
 *
 */
public class DuplicateTableInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		// El esquema '{0}' tiene varios objetos con el mismo nombre: '{1}'
		DataModelDiagnostic diagnostic = new DataModelDiagnostic();
		
		Set<String> names = new HashSet<String>();
		
		Set<String> constrainsNames = new HashSet<String>();
		if(eObject instanceof ESchema)
		{
			ESchema eSchema = (ESchema)eObject;
			for(ETable eTable:eSchema.getTables())
			{
				String eTableName = eTable.getName();
				if(names.contains(eTableName.toUpperCase()))
				{
					diagnostic.add(new DataModelDiagnostic(
							DataModelDiagnostic.ERROR, 
							Messages.bind("DuplicateTableInvValidator.validation.table",new String[]{eSchema.getName() ,  eTableName}),//$NON-NLS-1$
							getId(), 
							new Object[] { eObject }, 
							getCategory(), getCode()));
				}
				else names.add(eTableName.toUpperCase());
				if(eTable instanceof EBaseTable)
				{
					EBaseTable baseTable = (EBaseTable)eTable;
					for(ETableConstraint c:baseTable.getConstraints())
					{
						if(constrainsNames.contains(c.getName()))
							diagnostic.add(new DataModelDiagnostic(
									DataModelDiagnostic.ERROR, 
									Messages.bind("DuplicateTableInvValidator.validation.constraint",new String[]{eSchema.getName() ,  c.getName()}),//$NON-NLS-1$
									getId(), 
									new Object[] { eObject }, 
									getCategory(), getCode()));
						else constrainsNames.add(c.getName());
					}
				}
					
			}
				
		}
		return diagnostic;
	}

}
