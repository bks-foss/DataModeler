package com.isb.datamodeler.tables.invocation;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicInvocationDelegate;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETablesPackage;

public class ColumnInvocationDelegate extends BasicInvocationDelegate {

	public ColumnInvocationDelegate(EOperation operation) {
		super(operation);
	}

	@Override
	public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
			throws InvocationTargetException {

	    if (eOperation.getEContainingClass() == ETablesPackage.Literals.COLUMN)
	    {
	      switch (eOperation.getEContainingClass().getEAllOperations().indexOf(eOperation))
	      {
	        case ETablesPackage.COLUMN___COMPARE_TO__ECOLUMN:
	            return compareTo((EColumn)target, (EColumn)arguments.get(0));	
	        case ETablesPackage.COLUMN___CLONE:
	            return clone((EColumn)target);		            
	        case ETablesPackage.COLUMN___GET_PARENT_DATA_BASE:
	            return getParentDataBase((EColumn)target);		            
	      }
	    }
		
		return super.dynamicInvoke(target, arguments);
	}

	private EDatabase getParentDataBase(EColumn target) {
		
		if(target.getTable()!=null && target.getTable().getSchema()!=null)
			return target.getTable().getSchema().getDatabase();
		
		return null;
	}

	/**
	 * Compara dos columnas 
	 * @param target
	 * @param column
	 * @return 0 si tienen mismo nombre y tipo
	 */
	private int compareTo(EColumn target, EColumn column) {
		
		int result = target.getName().compareTo(column.getName());
		
		//Si coincide en nombre, comparamos el tipo:
		if(result==0)
		{
			if(target.getDataType()!=null && column.getDataType()!=null)
				result = target.getDataType().compareTo(column.getDataType());
			else if(target.getDataType()!=column.getDataType())
				result = -1;
		}
		
		return result;
	}

	/**
	 * Clona una columna sin copiar el Id
	 * @param target
	 * @return
	 */
	private Object clone(EColumn target) {
		
		EColumn newColumn = EcoreUtil.copy(target);
		
		newColumn.setId(null);
		
		return newColumn;
	}

}
