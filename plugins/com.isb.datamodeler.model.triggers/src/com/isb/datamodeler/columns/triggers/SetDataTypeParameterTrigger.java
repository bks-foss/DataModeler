package com.isb.datamodeler.columns.triggers;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.datatypes.EDataType;
import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.tables.EColumn;

public class SetDataTypeParameterTrigger extends AbstractTrigger {

	private EColumn _column;
	private EStructuralFeature _feature;
	private Object _oldValue;
	private Object _newValue;

	private EColumn _auxColumnWithOldDataType;

	public SetDataTypeParameterTrigger(TransactionalEditingDomain domain,
			EColumn parentColumn, EStructuralFeature feature, Object oldValue,
			Object newValue) {
		super(domain);

		_column = parentColumn;
		_feature = feature;
		_oldValue = oldValue;
		_newValue = newValue;

		// Construimos una columna con el parámetro antiguo para comparar:
		_auxColumnWithOldDataType = _column.clone();
		_auxColumnWithOldDataType.getDataType().eSet(_feature, _oldValue);
	}

	@Override
	public void executeTrigger() {
		// Si el parámetro pertenece a una columna que está dentro de una UK,
		// buscamos las FKs que referencian esa UK, y modificamos los parámetros
		// de las columnas asociadas:
		for (EReferenceConstraint refConstraint : _column.getReferencingConstraints())
			if (refConstraint instanceof EUniqueConstraint)
				for (EForeignKey referencingForeignKey : ((EUniqueConstraint) refConstraint).getReferencingForeignKeys())
					modifyMatchingColumn(referencingForeignKey);
	}

	private void modifyMatchingColumn(EForeignKey referencingForeignKey) {
		
		EColumn fkColumn = getFKMatchingMember(referencingForeignKey);
		
		if(fkColumn!=null)
		{
			// Hemos encontrado una columna hija y tenemos que acceder
			// a su DataType para modificar el parámetro correspondiente
			EDataType childDataType = fkColumn.getDataType();
			childDataType.eSet(_feature, _newValue);
		}
	}

	/**
	 * Devuelve la columna miembro de la FK en la misma posicion y con el mismo tipo que la columna modificada
	 * @param fk
	 * @return
	 */
	private EColumn getFKMatchingMember(EForeignKey fk) {

		// Si no hay coincidencia (previo al borrado) entre los miembros y
		// columnas referenciadas, no propagamos el cambio
		// (BUG 20541)
		if (fk.getMembers().size() != fk.getReferencedMembers().size())
			return null;

		int colIndex = fk.getReferencedMembers().indexOf(_column);

		EColumn matchingMember = fk.getMembers().get(colIndex);

		// Si ademas de coincidir en posicion coincide en tipo:
		if (hasSameType(matchingMember, _auxColumnWithOldDataType))
			return matchingMember;

		return null;
	}

	private boolean hasSameType(EColumn col1, EColumn col2) {
		if (col1.getDataType() != null && col2.getDataType() != null)
			return col1.getDataType().compareTo(col2.getDataType()) == 0;
		else if (col1.getDataType() == col2.getDataType())
			return true; // Si no tienen tipo de dato (son null los dos)

		return false;
	}
}
