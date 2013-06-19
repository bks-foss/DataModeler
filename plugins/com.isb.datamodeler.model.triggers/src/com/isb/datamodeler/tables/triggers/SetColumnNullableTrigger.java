package com.isb.datamodeler.tables.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EConstraint;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.ParentCardinality;
import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.tables.EColumn;

public class SetColumnNullableTrigger extends AbstractTrigger {

	private EColumn column;
	private boolean nullable;
	
	public SetColumnNullableTrigger(TransactionalEditingDomain domain, EColumn owner, boolean newValue) {
		super(domain);
		
		column = owner;
		nullable = newValue;
	}

	@Override
	public void executeTrigger() {
		
		//Marcar como nulable una columna que forma parte de una FK supone establecer la cardinalidad origen de la relación a "0..1".
		if(nullable)
			for(EConstraint parentConstraint: column.getReferencingConstraints())
				if(parentConstraint instanceof EForeignKey)
					((EForeignKey)parentConstraint).setParentCardinality(ParentCardinality.ZERO_ONE);

		//Marcar como no nulable una columna que forma parte de una FK supone que si el resto de columnas de la FK también son no nulables se establecerá la cardinalidad origen de la relación a "1".
		if(!nullable)
			for(EConstraint parentConstraint: column.getReferencingConstraints())
				if(parentConstraint instanceof EForeignKey && allMembersNonNullable((EForeignKey)parentConstraint))
					((EForeignKey)parentConstraint).setParentCardinality(ParentCardinality.ONE);
	}
	
	private boolean allMembersNonNullable(EForeignKey fk)
	{
		for(EColumn fkMember: fk.getMembers())
			if(fkMember.isNullable())
				return false;
		
		return true;
	}

}
