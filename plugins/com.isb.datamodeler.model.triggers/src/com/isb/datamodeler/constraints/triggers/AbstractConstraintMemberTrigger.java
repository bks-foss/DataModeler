package com.isb.datamodeler.constraints.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.BaseCardinality;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.model.triggers.AbstractTrigger;

public abstract class AbstractConstraintMemberTrigger extends AbstractTrigger {
	
	public AbstractConstraintMemberTrigger(TransactionalEditingDomain domain) {
		super(domain);
	}

	protected final void refreshForeignKeyCardinality(EForeignKey foreignKey)
	{
		//La cardinalidad destino tendrá valor "0..*" si la PK de la tabla hija 
		//la constituyen más columnas además de las que forman la FK, o valor "0..1" en caso contrario.
		EPrimaryKey baseTablePK = foreignKey.getBaseTable().getPrimaryKey();
		
		if(baseTablePK!=null && foreignKey.getMembers().containsAll(baseTablePK.getMembers()))
			foreignKey.setBaseCardinality(BaseCardinality.ZERO_ONE);
		else
			foreignKey.setBaseCardinality(BaseCardinality.ZERO_MANY);
	}
	
}
