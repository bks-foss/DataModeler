package com.isb.datamodeler.constraints.triggers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.tables.EColumn;

/**
 * Trigger ejecutado cuando se agrega una nueva columna a una ForeignKey
 * @author xIS05655
 *
 */
public class AddForeignKeyMemberTrigger extends AbstractConstraintMemberTrigger {

	private EForeignKey _foreignKey;
	private List<EColumn> _newMembers = new ArrayList<EColumn>();

	
	public AddForeignKeyMemberTrigger(TransactionalEditingDomain domain, EForeignKey owner, EColumn newMember) {
		super(domain);
		
		_foreignKey = owner;
		_newMembers.add(newMember);
	}

	public AddForeignKeyMemberTrigger(TransactionalEditingDomain domain, EForeignKey owner, List<EColumn> newMembers) {
		super(domain);
		
		_foreignKey = owner;
		_newMembers.addAll(newMembers);
	}

	@Override
	public void executeTrigger() {
		
		//Si agregamos una columna que no es PK, la FK deja de ser identifying
		for(EColumn newMember: _newMembers)
			if(!newMember.isPrimaryKey() && _foreignKey.isIdentifying())
				_foreignKey.setIdentifying(false);

		refreshForeignKeyCardinality(_foreignKey);
	}
	
	@Override
	protected boolean prepare() {
		return _foreignKey!=null;
	}


}
