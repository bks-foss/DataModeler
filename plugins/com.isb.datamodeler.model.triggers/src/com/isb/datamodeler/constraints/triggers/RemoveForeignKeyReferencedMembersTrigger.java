package com.isb.datamodeler.constraints.triggers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.model.triggers.IParametrizableTrigger;
import com.isb.datamodeler.tables.EColumn;

public class RemoveForeignKeyReferencedMembersTrigger extends AbstractConstraintMemberTrigger implements IParametrizableTrigger {

	private EForeignKey _foreignKey; 
	private int[] _removedMembersIndexes; //Miembros referenciados eliminados, con su posicion
	private List<EColumn> _fkMembersToRemove = new ArrayList<EColumn>(); //Miembros de la FK a eliminar
	
	
	public RemoveForeignKeyReferencedMembersTrigger(TransactionalEditingDomain domain, EForeignKey owner, int position) {
		super(domain);

		_foreignKey = owner;
		_removedMembersIndexes = new int[]{position};
		_fkMembersToRemove = loadForeignKeyMembersToRemove();
		
	}
	
	/**
	 * @param domain
	 * @param owner
	 * @param removedMembers Mapa con las columnas eliminadas asociadas a la posicion de la cual se eliminaron
	 */
	public RemoveForeignKeyReferencedMembersTrigger(TransactionalEditingDomain domain, EForeignKey owner, int[] removedMembersIndexes) {
		super(domain);

		_foreignKey = owner;
		_removedMembersIndexes = removedMembersIndexes;
		_fkMembersToRemove = loadForeignKeyMembersToRemove();
		
	}
	
	@Override
	public void executeTrigger() {
		
		removeSpareColumnsInForeignKey();

	}
	
	/**
	 * Crea una lista con las columnas miembro que coincidan en posicion con las columnas referenciadas eliminadas
	 * @param foreignKey
	 * @param removedUKMember
	 * @return
	 */
	private List<EColumn> loadForeignKeyMembersToRemove()
	{
		List<EColumn> membersToRemove = new ArrayList<EColumn>();
		
		//Si no hay coincidencia (previo al borrado) entre los miembros y columnas referenciadas, no propagamos el cambio 
		//(BUG 20541)
		if(_foreignKey.getMembers().size()!=(_foreignKey.getReferencedMembers().size()+1))
			return membersToRemove;
		
		if(_removedMembersIndexes!=null)
		{
			for(int position: _removedMembersIndexes)
				if(_foreignKey.getMembers().size()>position)
						membersToRemove.add(_foreignKey.getMembers().get(position));
		}else
			membersToRemove.addAll(_foreignKey.getMembers());
		
		return membersToRemove;
		
	}
	
	
	@Override
	/**
	 * Cuando se elimina la PK, primero se eliminan sus columnas, por eso puede pasar por este Trigger
	 */
	protected boolean prepare() {
		return _foreignKey!=null;
	}
	
	//Quitamos las columnas que sobran
	private void removeSpareColumnsInForeignKey()
	{
		List<EColumn> columnsToRemove = _fkMembersToRemove;
		
		_foreignKey.getMembers().removeAll(columnsToRemove);

	}

}
