package com.isb.datamodeler.schema.triggers;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.model.triggers.AbstractTriggerListener;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.tables.ETable;

public class SchemaTriggerListener extends AbstractTriggerListener {
	
	public static NotificationFilter addTableFilter;

	static{
		
		addTableFilter = NotificationFilter
        .createNotifierTypeFilter(ESchema.class).and(
            NotificationFilter.createEventTypeFilter(Notification.ADD)).and(
            NotificationFilter.createFeatureFilter(
            		ESchema.class,
            		ESchemaPackage.SCHEMA__TABLES));
	}
	
	public SchemaTriggerListener() {
		super(NotificationFilter.createNotifierTypeFilter(ESchema.class));
	}

	@Override
	protected Command trigger(
			TransactionalEditingDomain domain, Notification notification) {
		
		if(addTableFilter.matches(notification))
		{
//			ETable newTable = (ETable)notification.getNewValue();
//			ESchema schema = (ESchema)notification.getNotifier();
//			
//			return new TableInitializer(domain, schema, newTable);
		}
		
		return null;
	}

}
