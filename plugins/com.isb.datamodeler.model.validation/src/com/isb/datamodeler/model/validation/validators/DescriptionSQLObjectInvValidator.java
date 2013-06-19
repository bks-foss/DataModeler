package com.isb.datamodeler.model.validation.validators;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.validation.Messages;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.tables.util.TablesSwitch;
/**
 * Validación de correción de modelo de datos. Validación Obligatoria
 * Todas las tablas , columnas deben estar documentadas.
 * 
 * @author Delia Salmerón
 *
 */
public class DescriptionSQLObjectInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) 
	{
		return tablesSwitch.doSwitch(eObject);
		
	}
	protected TablesSwitch<DataModelDiagnostic> tablesSwitch =
		new TablesSwitch<DataModelDiagnostic>() {
		
		public DataModelDiagnostic caseTable(ETable object) 
		{
			if(object.getSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			// La tabla '{0}' debe tener descripción/documentación.
			if(object.getDescription()==null || object.getDescription().length()==0)
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("DescriptionSQLObjectInvValidator.validation.table.description",object.getName()),//$NON-NLS-1$
						getId(), 
						new Object[] { object }, 
						getCategory(), getCode());
			
			return DataModelDiagnostic.OK_INSTANCE;
		};
		public DataModelDiagnostic caseColumn(EColumn object) 
		{
			if(object.getTable().getSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			// La columna '{0}' debe tener descripción/documentación.
			// La columna 'nombreColumna' de 'nombreElemento' debe tener descripción/documentación". 

			if(object.getDescription()==null || object.getDescription().length()==0)
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("DescriptionSQLObjectInvValidator.validation.column.description",//$NON-NLS-1$
								new String[]{object.getName(), object.getTable().getName()}),
						getId(), 
						new Object[] { object }, 
						getCategory(), getCode());
			
			return DataModelDiagnostic.OK_INSTANCE;
		};
		public DataModelDiagnostic defaultCase(EObject object) 
		{
			return null;
		};
		
	};
}
