package com.isb.datamodeler.constraints.triggers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.model.triggers.IParametrizableTrigger;
import com.isb.datamodeler.tables.EColumn;

public class RemoveForeignKeyMembersTrigger extends AbstractConstraintMemberTrigger implements IParametrizableTrigger {

	private EForeignKey _foreignKey; 
	private List<EColumn> _removedMembers = new ArrayList<EColumn>();
	
	private boolean removeColumnFromTable = false;


	public RemoveForeignKeyMembersTrigger(TransactionalEditingDomain domain, EForeignKey owner, List<EColumn> removedMembers) {
		super(domain);

		_foreignKey = owner;
		_removedMembers.addAll(removedMembers);
	}
	
	public RemoveForeignKeyMembersTrigger(TransactionalEditingDomain domain, EForeignKey owner, EColumn removedMember) {
		super(domain);

		_foreignKey = owner;
		_removedMembers.add(removedMember);
	}
	
	@Override
	public void executeTrigger() {
		
		//Cuando se está eliminando una FK la BaseTable es null. No tiene sentido entonces refrescar la FK
		if(_foreignKey.getBaseTable()!=null)
		{
			//Si luego de quitar una columna de la FK, las columnas que quedan son todas PK
			//la relación se vuelve identificativa
			if(_foreignKey.allMembersPartOfPrimaryKey() && !_foreignKey.isIdentifying())
				_foreignKey.setIdentifying(true);
			
			refreshForeignKeyCardinality(_foreignKey);
		}
		
		//Si está configurada la opcion de eliminar las columnas hijas, las eliminamos de la tabla hija
		for(EColumn columnToRemoveFromFK: _removedMembers)
			if(removeColumnFromTable)
				EcoreUtil.delete(columnToRemoveFromFK);
	

	}
	
	public List<EColumn> getForeignKeyMembersToRemove()
	{
		return _removedMembers;
	}
	
	public EForeignKey getForeignKey()
	{
		return _foreignKey;
	}
	
	@Override
	protected boolean prepare() {
		return _foreignKey!=null;
	}
	
	/**
	 * Setea las columnas que se van a eliminar de la tabla ademas de eliminarse de la FK
	 * Esto se configura en RemovePrimaryKeyMemberTriggerConfigurator consultando al usuario
	 * @param columnsToRemove
	 */
	public void setRemoveColumnFromTable(boolean remove) {
		removeColumnFromTable = remove;
	}
	
}
