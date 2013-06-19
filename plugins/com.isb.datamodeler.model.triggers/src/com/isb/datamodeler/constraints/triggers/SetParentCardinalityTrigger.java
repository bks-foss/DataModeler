package com.isb.datamodeler.constraints.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.ParentCardinality;
import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.tables.EColumn;

public class SetParentCardinalityTrigger extends AbstractTrigger {

	private EForeignKey foreignKey;
	private ParentCardinality newCardinality;
	
	public SetParentCardinalityTrigger(TransactionalEditingDomain domain, EForeignKey owner, ParentCardinality newValue) {
		super(domain);
		foreignKey = owner;
		newCardinality = newValue;
	}

	@Override
	public void executeTrigger() {
		
		//Establecer cardinalidad origen a 1 supone marcar las columnas correspondientes en el destino como no nulables.
		// Establecer cardinalidad destino (padre) a 1 supone marcar las columnas correspondientes en el origen como no nulables. 
		if(newCardinality.equals(ParentCardinality.ONE))
			for(EColumn fkMember: foreignKey.getMembers())
				fkMember.setNullable(false);
		
		//Establecer cardinalidad origen a 0..1 supone marcar las columnas correspondientes en el destino como nulables.
		
		// Bugzilla 20536:Establecer cardinalidad destino (padre) a 0..1 no tiene mayores implicaciones. 
//		if(newCardinality.equals(ParentCardinality.ZERO_ONE))
//			for(EColumn fkMember: foreignKey.getMembers())
//				fkMember.setNullable(true);

	}

}
