package com.isb.datamodeler.model.triggers.internal.initializers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.model.triggers.DataModelerTriggersPlugin;
import com.isb.datamodeler.model.triggers.initializers.IDatamodelerInitializer;
import com.isb.datamodeler.schema.ESQLObject;

public class DatamodelerInitializerTrigger extends AbstractTrigger {

	private List<ESQLObject> _children = new ArrayList<ESQLObject>();
	private EObject _owner;

	public DatamodelerInitializerTrigger(TransactionalEditingDomain domain, ESQLObject child, EObject owner) {
		super(domain);
		
		_children.add(child);
		_owner = owner;
	}

	public DatamodelerInitializerTrigger(TransactionalEditingDomain domain, List<ESQLObject> children, EObject owner) {
		super(domain);
		
		_owner = owner;
		_children.addAll(children);
	}
	
	@Override
	public void executeTrigger() {
		IDatamodelerInitializer initializer = DataModelerTriggersPlugin.getInstance().getInitializer(_owner); 
		if(initializer!=null)
			initializer.initialize(_owner, _children);
	}

}
