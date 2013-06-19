package com.isb.datamodeler.constraints.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.tables.EBaseTable;

/**
 * Trigger para eliminacion de tablas
 * @author xIS05655
 *
 */
public class RemoveForeignKeyParentTableTrigger extends AbstractTrigger {

	private EForeignKey _referenceConstraint;
	
	public RemoveForeignKeyParentTableTrigger(TransactionalEditingDomain domain, EForeignKey owner) {
		super(domain);
		_referenceConstraint = owner;
	}

	@Override
	public void executeTrigger() {
		
		//En general, al eliminar una tabla desde un diagrama GMF en el cual están representadas visualmente
		//las FKs que la referencian, a traves de las politicas semanticas se crean los comandos de borrado de dichas FKs
		//Pero al eliminar una tabla desde el navegador, o desde un diagrama donde no están representadas las relaciones,
		//es necesario eliminarlas manualmente
		EBaseTable baseTable = _referenceConstraint.getBaseTable();
		if(baseTable!=null)
			baseTable.getConstraints().remove(_referenceConstraint);
			

	}

}
