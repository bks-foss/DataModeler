package com.isb.datamodeler.constraints.triggers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.tables.EColumn;

public class SetUniqueConstraintTrigger extends AbstractTrigger {
	
	private EForeignKey _foreignKey;
	private EUniqueConstraint _newUniqueConstraint;

	public SetUniqueConstraintTrigger(TransactionalEditingDomain domain, EForeignKey owner, EUniqueConstraint newUniqueConstraint) {
		super(domain);
		
		_foreignKey = owner;
		_newUniqueConstraint = newUniqueConstraint;
	}

	@Override
	public void executeTrigger() {
		
		//Limpiamos los miembros referenciados
		List<EColumn> membersToRemove = new ArrayList<EColumn>(); 
		membersToRemove.addAll(_foreignKey.getReferencedMembers()); 
		
		//Agregamos los nuevos
		if(_newUniqueConstraint!=null)
		{
			//No eliminamos elementos que vayamos a agregar para evitar notificacionea innecesarias
			membersToRemove.removeAll(_newUniqueConstraint.getMembers());
			
			List<EColumn> membersToAdd = new ArrayList<EColumn>();
			membersToAdd.addAll(_newUniqueConstraint.getMembers());
			
			//No agregamos elementos que ya estén para evitar notificacionea innecesarias
			membersToAdd.removeAll(_foreignKey.getReferencedMembers());
			
			_foreignKey.getReferencedMembers().addAll(membersToAdd);
			_foreignKey.getReferencedMembers().removeAll(membersToRemove);
			
		}else
			//Si estamos eliminando la UK, la FK tambien se elimina
			DataModelerUtils.destroyElement(_foreignKey);
			

	}

	@Override
	protected boolean prepare() {
		return _foreignKey!=null;
	}
	
	

}
