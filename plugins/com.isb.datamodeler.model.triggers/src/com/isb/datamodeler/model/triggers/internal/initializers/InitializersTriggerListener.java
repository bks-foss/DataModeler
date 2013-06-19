package com.isb.datamodeler.model.triggers.internal.initializers;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.model.triggers.AbstractTriggerListener;
import com.isb.datamodeler.schema.ESQLObject;

public class InitializersTriggerListener extends AbstractTriggerListener{

	public static NotificationFilter addChildFilter;
	public static NotificationFilter addManyChildrenFilter;

	static{

		addChildFilter = NotificationFilter
		        .createNotifierTypeFilter(ESQLObject.class).and(
		            NotificationFilter.createEventTypeFilter(Notification.ADD));
		
		addManyChildrenFilter = NotificationFilter
		        .createNotifierTypeFilter(ESQLObject.class).and(
		            NotificationFilter.createEventTypeFilter(Notification.ADD_MANY));
	}
	
	public InitializersTriggerListener() {
		super(NotificationFilter.createNotifierTypeFilter(EObject.class));
	}

	@Override
	protected Command trigger(TransactionalEditingDomain domain,
			Notification notification) {
		
		if(addChildFilter.matches(notification))
		{
			ESQLObject child = (ESQLObject)notification.getNewValue();
			EObject owner = (EObject)notification.getNotifier();
			
			return new DatamodelerInitializerTrigger(domain, child, owner);
		}
		else if(addManyChildrenFilter.matches(notification))
		{	
			@SuppressWarnings("unchecked")
			List<ESQLObject> children = (List<ESQLObject>)notification.getNewValue();	
			EObject owner = (EObject)notification.getNotifier();
			
			return new DatamodelerInitializerTrigger(domain, children, owner);
		}
		
		return null;
	}

}
