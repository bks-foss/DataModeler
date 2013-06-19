package com.isb.datamodeler.columns.triggers;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.datatypes.EDataType;
import com.isb.datamodeler.model.triggers.AbstractTriggerListener;
import com.isb.datamodeler.tables.EColumn;

public class DataTypeTriggerListener extends AbstractTriggerListener
{

public static NotificationFilter setParameterFilter;


static
{
	setParameterFilter = NotificationFilter
    .createNotifierTypeFilter(EDataType.class).and(
        NotificationFilter.createEventTypeFilter(Notification.SET));
}

public DataTypeTriggerListener()
{
	super(NotificationFilter.createNotifierTypeFilter(EDataType.class));
}

@Override
protected Command trigger(TransactionalEditingDomain domain,
		Notification notification)
{
	EDataType dataType = (EDataType)notification.getNotifier();
	
	if(setParameterFilter.matches(notification) && !notification.isTouch() && dataType.eContainer() instanceof EColumn)
	{
		EColumn parentColumn = (EColumn)dataType.eContainer();
		
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		
		EStructuralFeature feature = (EStructuralFeature)notification.getFeature();
		
		return new SetDataTypeParameterTrigger(domain, parentColumn, feature, oldValue, newValue);
	}
	
	return null;
}
}
