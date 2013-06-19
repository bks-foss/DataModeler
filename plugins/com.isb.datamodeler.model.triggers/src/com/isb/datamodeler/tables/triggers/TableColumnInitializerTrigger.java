package com.isb.datamodeler.tables.triggers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.model.triggers.AbstractInitializerTrigger;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;

/**
 * Comando encargado de la inicializacion de una nueva columna
 * @author xIS05655
 *
 */
public class TableColumnInitializerTrigger extends AbstractInitializerTrigger {
	
	private EBaseTable _parentTable;
	private List<EColumn> _newColumns = new ArrayList<EColumn>();
	
	private static final String DEFAULT_COLUMN_NAME = "col";

	public TableColumnInitializerTrigger(TransactionalEditingDomain domain, EBaseTable owner, EColumn newColumn) {
		super(domain);
		
		_parentTable = owner;
		_newColumns.add(newColumn);
	}

	public TableColumnInitializerTrigger(TransactionalEditingDomain domain, EBaseTable owner, List<EColumn> newColumns) {
		super(domain);
		
		_parentTable = owner;
		_newColumns.addAll(newColumns);
	}

	@Override
	public void executeTrigger() {
		
//		EList<EColumn> existingColumns = new BasicEList<EColumn>(_parentTable.getColumns());
//		existingColumns.removeAll(_newColumns);
//		
//		for(EColumn newColumn:_newColumns)
//		{
//			//El tipo de dato por defecto es el CHAR(4), es el primero definido en los tipos primitivos
//			EPrimitiveDataType defaultDataType = newColumn.getParentDataBase().getDefaultPrimitiveDataType();
//			
//			EDatabase obj= newColumn.getParentDataBase();
//			
//			//Si la base de datos no tiene definido ningun tipo por defecto, usamos el primero
//			if(defaultDataType==null)
//				defaultDataType = newColumn.getParentDataBase().getPrimitiveDataTypes().get(0);
//			
//			if(defaultDataType!=null && newColumn.getPrimitiveType()==null)
//				newColumn.setPrimitiveType(defaultDataType);
//			
//			//Las columnas se crean como No Nulables
//			newColumn.setNullable(false);
//
//			
//			//Inicializamos la nueva columna
//			initializeBasics(newColumn, existingColumns, DEFAULT_COLUMN_NAME);
//			
//			existingColumns.add(newColumn);
//		}
		

	}

}
