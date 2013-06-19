package com.isb.datamodeler.constraints.triggers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;

public class AddUniqueConstraintMemberTrigger extends AbstractConstraintMemberTrigger{
	
	private EUniqueConstraint _uniqueConstraint; 
	private List<EColumn> _newUKMembers = new ArrayList<EColumn>();
	private EColumn _oldUKMember;
	
	public AddUniqueConstraintMemberTrigger(TransactionalEditingDomain domain, EUniqueConstraint owner, EColumn newMember) {
		super(domain);
		
		_uniqueConstraint = owner;
		_newUKMembers.add(newMember);
	}
	
	public AddUniqueConstraintMemberTrigger(TransactionalEditingDomain domain, EUniqueConstraint owner, EColumn newMember, EColumn oldMember) {
		super(domain);
		
		_uniqueConstraint = owner;
		_newUKMembers.add(newMember);
		_oldUKMember = oldMember;
	}
	
	public AddUniqueConstraintMemberTrigger(TransactionalEditingDomain domain, EUniqueConstraint owner, List<EColumn> newMembers) {
		super(domain);
		
		_uniqueConstraint = owner;
		_newUKMembers.addAll(newMembers);
	}

	@Override
	public void executeTrigger() {
		
		EBaseTable table = _uniqueConstraint.getBaseTable();

		//Refrescamos las FKs que referencian la PK
		for(EForeignKey fk:_uniqueConstraint.getReferencingForeignKeys())
		{
			if(_oldUKMember!=null && fk.getReferencedMembers().contains(_oldUKMember))
				fk.getReferencedMembers().remove(_oldUKMember);
				
			
			for(EColumn newUKMember:_newUKMembers)
			{
				int sourcePosition = _uniqueConstraint.getMembers().indexOf(newUKMember);
				// Resolucion bugzilla 19670 : "[QA] No se pueden a�adir varias columnas a la vez a una restricci�n UK o PK"
				if(!fk.getReferencedMembers().contains(newUKMember))
				{
					if(sourcePosition<=fk.getReferencedMembers().size())
						fk.getReferencedMembers().add(sourcePosition, newUKMember);
					else fk.getReferencedMembers().add(newUKMember);

				}
				
			}
		}
			
			

		//Refrescamos las FK propias
		for(EForeignKey tableFK:table.getForeignKeys())
		{
			refreshForeignKeyIdentifyingProperty(tableFK);
			refreshForeignKeyCardinality(tableFK);
		}
		
		//Si la restricci�n que estamos modificando es una PK:
		//Se marcar� la columna como nulable y ese campo quedar� como no editable.
		// Bugzilla 18994: si es PK se pone a nullable=false
		if(_uniqueConstraint instanceof EPrimaryKey)
			for(EColumn newUKMember:_newUKMembers)
				newUKMember.setNullable(false);
			
	}

	@Override
	protected boolean prepare() {
		return _uniqueConstraint!=null;
	}

	/**
	 * 	Si luego de los cambios, las columnas que quedan en la FK de la tabla son todas PK
	 *  la relaci�n se vuelve identificativa
	 * @param foreignKey
	 */
	protected final void refreshForeignKeyIdentifyingProperty(EForeignKey foreignKey)
	{
		if(foreignKey.allMembersPartOfPrimaryKey() && !foreignKey.isIdentifying())
			foreignKey.setIdentifying(true);
	}

	
}
