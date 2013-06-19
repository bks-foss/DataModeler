package com.isb.datamodeler.model.validation.validators;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * Debe haber correspondencia entre las columnas que forman parte de una FK y las columnas de
 * la clave única referenciada(tanto en el número de columnas como en el tipo de datos y 
 * sus parámetros).
 * 
 * @author Delia Salmerón
 *
 */
public class FKColumnsRefsInvValidator extends
		AbstractDataModelerElementValidator {

	@Override
	public DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {
		if(eObject instanceof EForeignKey)
		{
			EForeignKey fk = (EForeignKey)eObject;
			if(fk.getBaseTableSchema().isExternal())
				return DataModelDiagnostic.OK_INSTANCE;
			
			List<EColumn> columns = fk.getMembers();
			List<EColumn> referencedColumns = fk.getReferencedMembers();
			if(columns.size()!=referencedColumns.size())
			{
				//  La restricción '{0}' tiene distinto número de columnas que la restricción padre.
				return new DataModelDiagnostic(
						DataModelDiagnostic.ERROR, 
						Messages.bind("FKColumnsRefsInvValidator.validation1",fk.getName()),//$NON-NLS-1$
						getId(), 
						new Object[] { eObject }, 
						getCategory(), getCode());
			}
			else 
			{
				// Comprobamos que el tipo y los parametros son los mismos
				DataModelDiagnostic dataModelDiagnostic = new DataModelDiagnostic();
				Set<EColumn> columnsNotFound = new HashSet<EColumn>();
				columnsNotFound.addAll(columns);
				for(int i=0; i<columns.size();i++)
				{
					if(columns.get(i).getDataType().compareTo(referencedColumns.get(i).getDataType())==0)
					{
						columnsNotFound.remove(columns.get(i));
					}
					
				}
				
				if(columnsNotFound.size()>0)	
				{
					StringBuffer msg = new StringBuffer();
					for(EColumn c:columnsNotFound)
					{
						msg.append(c.getName());
						msg.append(",");
					}
					msg.deleteCharAt(msg.length()-1);
					//La restricción  '{0}' tiene columnas que no corresponden con las de la restriccion padre: '{1}'
					dataModelDiagnostic.add(new DataModelDiagnostic(
							DataModelDiagnostic.ERROR,
							Messages.bind("FKColumnsRefsInvValidator.validation2",new String[]{fk.getName(),msg.toString()}),//$NON-NLS-1$
							getId(), 
							new Object[] { eObject }, 
							getCategory(), getCode()));
				}
				
				return dataModelDiagnostic;
			}
		}
		return DataModelDiagnostic.OK_INSTANCE;
	}

}
