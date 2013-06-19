package com.isb.datamodeler.tables.triggers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.model.triggers.IParametrizableTrigger;
import com.isb.datamodeler.tables.EColumn;

public class SetColumnNameTrigger extends AbstractTrigger implements IParametrizableTrigger{

	private EColumn renamedColumn;
	private EColumn auxColumnWithOldName;
	private String _oldName;
	
	//El configurador del trigger deberá setear esta propiedas 
	private List<EColumn> childColumnsToRename = new ArrayList<EColumn>();
	
	public SetColumnNameTrigger(TransactionalEditingDomain domain, EColumn owner, String oldName) {
		super(domain);
		
		renamedColumn = owner;
		_oldName = oldName;
		
		//Construimos una columna con el nombre antiguo para comparar:
		auxColumnWithOldName = renamedColumn.clone();
		auxColumnWithOldName.setName(_oldName);
	}

	@Override
	public void executeTrigger() {
		
		renameMatchingColumns();		
	}

	private void renameMatchingColumns() {

		//SetColumnNameTriggerConfigurator previamente calculó las columnas a renombrar en base a las coincidencias y la respuesta del usuario:
		for(EColumn columnToRename:childColumnsToRename)
			columnToRename.setName(renamedColumn.getName());

	}
	
	/**
	 * Devuelve la columna miembro de la FK en la misma posicion y con el mismo tipo y nombre que la columna modificada
	 * @param fk
	 * @return
	 */
	private EColumn getFKMatchingMember(EForeignKey fk) {
		
		//Si no hay coincidencia (previo al borrado) entre los miembros y columnas referenciadas, no propagamos el cambio 
		//(BUG 20541)
		if(fk.getMembers().size()!=fk.getReferencedMembers().size())
			return null;
		
		int colIndex = fk.getReferencedMembers().indexOf(renamedColumn);
		
		if(colIndex<0)
			return null;
		
		EColumn matchingMember = fk.getMembers().get(colIndex);
		
		//Si ademas de coincidir en posicion coincide en nombre y tipo:
		if(matchingMember.compareTo(auxColumnWithOldName)==0)
			return matchingMember;
		
		return null;
	}

	/**
	 * Devuelve todas las columnas de las tablas hijas que tengan coincidencia con la columna renombrada 
	 * (Y esten dentro de una FK que referencia la tabla padre)
	 * @return
	 */
	public List<EColumn> getAllMatchingColumns() {
		
		List<EColumn> matchingColumns = new ArrayList<EColumn>();
		
		for(EReferenceConstraint refConstraint:renamedColumn.getReferencingConstraints())
			if(refConstraint instanceof EUniqueConstraint)
				for(EForeignKey referencingForeignKey: ((EUniqueConstraint)refConstraint).getReferencingForeignKeys())
				{
					EColumn matchingMember = getFKMatchingMember(referencingForeignKey);
					if(matchingMember!=null)
						matchingColumns.add(matchingMember);
				}
					
		
		return matchingColumns;
		
	}
	
	public List<EColumn> getChildColumnsToRename() {
		return childColumnsToRename;
	}
	


}
