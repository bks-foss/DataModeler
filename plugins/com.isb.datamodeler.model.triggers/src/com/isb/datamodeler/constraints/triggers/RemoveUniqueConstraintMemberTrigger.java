package com.isb.datamodeler.constraints.triggers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;

public class RemoveUniqueConstraintMemberTrigger extends AbstractConstraintMemberTrigger {

	private EUniqueConstraint _uniqueKey; 
	private List<EColumn> _removedMembers = new ArrayList<EColumn>();
	
	public RemoveUniqueConstraintMemberTrigger(TransactionalEditingDomain domain, EUniqueConstraint owner, EColumn removedMember) {
		super(domain);

		_uniqueKey = owner;
		_removedMembers.add(removedMember);
	}
	
	public RemoveUniqueConstraintMemberTrigger(TransactionalEditingDomain domain, EUniqueConstraint owner, List<EColumn> removedMembers) {
		super(domain);

		_uniqueKey = owner;
		_removedMembers.addAll(removedMembers);
		
	}

	@Override
	public void executeTrigger() {
		
		EBaseTable baseTable = _uniqueKey.getBaseTable();
		
		//Cuando se eliminan varias columnas de una PK a la vez, se ejecutan varias instancias de este comando
		//Si al eliminar las columnas la PK, queda vacia, la primera vez que se ejecute este comando, se eliminará la PK
		//En el resto de las ejecuciones, la PK está eliminado y por tanto no tiene Tabla asociada
		if(baseTable!=null)
		{
			//Refrescamos las FKs que referencian la PK
			for(EForeignKey fk: _uniqueKey.getReferencingForeignKeys())
				fk.getReferencedMembers().removeAll(_removedMembers);
				
			
			//Refrescamos las FK propias
			for(EForeignKey tableFK:baseTable.getForeignKeys())
			{
				refreshForeignKeyIdentifyingProperty(tableFK);
				refreshForeignKeyCardinality(tableFK);
			}
				
			//Si la constraint queda vacia, la eliminamos
			if(_uniqueKey.getMembers().isEmpty())
				EcoreUtil.delete(_uniqueKey);
		}

	}
	
	@Override
	/**
	 * Cuando se elimina la PK, primero se eliminan sus columnas, por eso puede pasar por este Trigger
	 */
	protected boolean prepare() {
		return _uniqueKey!=null;
	}
	
	/**
	 *
	 * 	Si la FK era identificativa, al dejar de ser una de sus columnas PK, deja de ser identificativa
	 * @param foreignKey
	 */
	protected final void refreshForeignKeyIdentifyingProperty(EForeignKey foreignKey)
	{
		if(foreignKey.isIdentifying() && !foreignKey.allMembersPartOfPrimaryKey())
			foreignKey.setIdentifying(false);
	}
	
}
