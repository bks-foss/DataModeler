package com.isb.datamodeler.constraints.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.model.triggers.AbstractTrigger;

public class MoveUniqueConstraintMemeberTrigger extends AbstractTrigger
{

private EUniqueConstraint _uk;
private int _newPosition;
private int _oldPosition;

public MoveUniqueConstraintMemeberTrigger(TransactionalEditingDomain domain,
		EUniqueConstraint uk, int newPosition, int oldPosition)
{
	super(domain);
	_uk = uk;
	_newPosition = newPosition;
	_oldPosition = oldPosition;
}

@Override
public void executeTrigger()
{
	//Refrescamos las FKs que referencian la PK
	for(EForeignKey fk:_uk.getReferencingForeignKeys())
	{
		//Si la FK no tiene misma cantidad de miembros y cols referenciadas no propagamos el cambio (BUG 20541)
		if(fk.getReferencedMembers().size()==fk.getMembers().size())
		{
			fk.getMembers().move(_newPosition, _oldPosition);
			fk.getReferencedMembers().move(_newPosition, _oldPosition);
		}
	}
}
}
