package com.isb.datamodeler.tables.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.datatypes.EDataType;
import com.isb.datamodeler.datatypes.EPredefinedDataType;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.tables.EColumn;

public class SetColumnPrimitiveDataTypeTrigger extends AbstractTrigger {
	
	private EColumn _column;
	private EPrimitiveDataType _newType;
	
	private EColumn _auxColumnWithOldType;
	
	public SetColumnPrimitiveDataTypeTrigger(TransactionalEditingDomain domain, EColumn owner, EPrimitiveDataType newValue, EPrimitiveDataType oldValue) {
		super(domain);
		
		_column = owner;
		_newType = newValue;
		
		//Construimos una columna con el tipo antiguo para comparar:
		_auxColumnWithOldType = _column.clone();
		_auxColumnWithOldType.setPrimitiveType(oldValue);
	}

	@Override
	public void executeTrigger() {
		
		EDataType existingDataType = _column.getDataType();
		EDatabase parentDataBase = _column.getParentDataBase();

		//Si el tipo seteado no es el adecuado, creamos un nuevo EPredefinedDataType
		if( !(existingDataType instanceof EPredefinedDataType) || 
				!((EPredefinedDataType)existingDataType).getPrimitiveType().getId().equals(_newType.getId()))
		{
			EPredefinedDataType newPredefinedDataType = parentDataBase.createNewPredefinedDataType(_newType);
			_column.setDataType(newPredefinedDataType);
		}
		
		//Si la columna cuyo tipo hemos cambiado esta dentro de una UK, buscamos las FKs
		//que referencian esa UK, y cambiamos el tipo de las columnas asociadas:
		for(EReferenceConstraint refConstraint:_column.getReferencingConstraints())
			if(refConstraint instanceof EUniqueConstraint)
				for(EForeignKey referencingForeignKey: ((EUniqueConstraint)refConstraint).getReferencingForeignKeys())
					retypeMatchingColumn(referencingForeignKey);
		
		_column.setDefaultValue("");
	}
	
	/**
	 * Para una FK busca la columna asociada a la columna modificada, y si la encuentra la modifica
	 * @param referencingForeignKey
	 */
	private void retypeMatchingColumn(EForeignKey referencingForeignKey) {
		
		EColumn fkColumn = getFKMatchingMember(referencingForeignKey);
		
		if(fkColumn!=null)
			fkColumn.setPrimitiveType(_newType);
	}
	
	/**
	 * Devuelve la columna miembro de la FK en la misma posicion y con el mismo tipo que la columna modificada
	 * @param fk
	 * @return
	 */
	private EColumn getFKMatchingMember(EForeignKey fk) {
		
		//Si no hay coincidencia (previo al borrado) entre los miembros y columnas referenciadas, no propagamos el cambio 
		//(BUG 20541)
		if(fk.getMembers().size()!=fk.getReferencedMembers().size())
			return null;
		
		int colIndex = fk.getReferencedMembers().indexOf(_column);
		
		EColumn matchingMember = fk.getMembers().get(colIndex);
		
		//Si ademas de coincidir en posicion coincide en tipo:
		if(hasSameType(matchingMember,_auxColumnWithOldType))
			return matchingMember;
		
		return null;
	}
	
	/**
	 * Devuelve true si dos columnas tienen el  mismo tipo de dato (Y valores asociados al tipo de dato)
	 * @see com.isb.datamodeler.tables.invocation.ColumnInvocationDelegate.java
	 * @param col1
	 * @param col2
	 * @return
	 */
	private boolean hasSameType(EColumn col1, EColumn col2)
	{
		if(col1.getDataType()!=null && col2.getDataType()!=null)
			return col1.getDataType().compareTo(col2.getDataType())==0;
		else if(col1.getDataType()==col2.getDataType())
			return true; //Si no tienen tipo de dato (son null los dos)
					
		return false;
	}
}
