package com.isb.datamodeler.model.validation.validators;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
/**
 * Restricción controlada por Data Modeler. Validación Obligatoria.
 * Una tabla no puede tener columnas con el mismo nombre.
 * 
 * @author Delia Salmerón
 *
 */
public class DuplicateColumnInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		// La columna '{0}' está repetida en la tabla: '{1}'
		DataModelDiagnostic diagnostic = new DataModelDiagnostic();
		
		Set<String> namesColumns = new HashSet<String>();
		if(eObject instanceof ETable)
		{
			ETable eTable = (ETable)eObject;
			if(eTable.getSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			for(EColumn eColumn:eTable.getColumns())
			{
				String columnName = eColumn.getName();
				if(namesColumns.contains(eColumn.getName()))
				{
					diagnostic.add(new DataModelDiagnostic(
							DataModelDiagnostic.ERROR, 
							Messages.bind("DuplicateColumnInvValidator.validation",new String[]{columnName , eTable.getName()}),//$NON-NLS-1$
							getId(), 
							new Object[] { eObject }, 
							getCategory(), getCode()));
				}
				else namesColumns.add(columnName);
					
			}
				
		}
		return diagnostic;
	}

}
