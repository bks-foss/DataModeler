package com.isb.datamodeler.constraints.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.ParentCardinality;
import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.tables.EColumn;

public class SetIsIdentifyingTrigger extends AbstractTrigger {
	
	private EForeignKey _foreignKey;
	private boolean _isIdentifying;

	public SetIsIdentifyingTrigger(TransactionalEditingDomain domain, EForeignKey owner, boolean newValue) {
		super(domain);

		_foreignKey = owner;
		_isIdentifying = newValue;
	}

	@Override
	public void executeTrigger() {
		
		if(_isIdentifying && _foreignKey.getUniqueConstraint() instanceof EPrimaryKey)
			for(EColumn member:_foreignKey.getMembers())
				member.setPrimaryKey(true);
		
		if(_isIdentifying)
			//El campo cardinalidad origen (Tabla Padre) será 1 si la relación es identifying
			_foreignKey.setParentCardinality(ParentCardinality.ONE);
		
//		if(!_isIdentifying)
//			for(EColumn member:_foreignKey.getMembers())
//				member.setPrimaryKey(false);
		
	}

	@Override
	protected boolean prepare() {
		return _foreignKey!=null;
	}
	
}
