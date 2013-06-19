package com.isb.datamodeler.project.triggers;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.model.triggers.AbstractTriggerListener;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.project.EProject;
import com.isb.datamodeler.ui.project.EProjectPackage;

public class ProjectTriggerListener extends AbstractTriggerListener {
	
	public static NotificationFilter addSchema;
	
	static{
		
		addSchema = NotificationFilter
        .createNotifierTypeFilter(EProject.class).and(
            NotificationFilter.createEventTypeFilter(Notification.ADD)).and(
            NotificationFilter.createFeatureFilter(
            		EProject.class,
            		EProjectPackage.PROJECT__SCHEMAS));
	}

	public ProjectTriggerListener() {
		super(NotificationFilter.createNotifierTypeFilter(EProject.class));
	}

	@Override
	protected Command trigger(
			TransactionalEditingDomain domain, Notification notification) {
		
		if(addSchema.matches(notification))
		{
			EProject project = (EProject)notification.getNotifier();
			ESchema newSchema = (ESchema)notification.getNewValue();
			
			return new SchemaInitializerTrigger(domain, project, newSchema);
		}
		
		return null;
	}

}
