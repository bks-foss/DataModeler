package com.isb.datamodeler.project.triggers;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.model.triggers.AbstractTriggerListener;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;

public class SchemaTriggerListener extends AbstractTriggerListener {
	
	public static NotificationFilter setNameFilter;
	
	static{
		
		setNameFilter = NotificationFilter
		        .createNotifierTypeFilter(ESchema.class).and(
		            NotificationFilter.createEventTypeFilter(Notification.SET)).and(
		            NotificationFilter.createFeatureFilter(
		            		ESchema.class,
		            		ESchemaPackage.SCHEMA__NAME));
	}

	public SchemaTriggerListener() {
		super(NotificationFilter.createNotifierTypeFilter(ESchema.class));
	}

	@Override
	protected Command trigger(
			TransactionalEditingDomain domain, Notification notification) {
		
		if(setNameFilter.matches(notification))
		{
			ESchema schema = (ESchema)notification.getNotifier();
			return new SetSchemaNameTrigger(domain, schema);
		}
		
		return null;
	}

}
