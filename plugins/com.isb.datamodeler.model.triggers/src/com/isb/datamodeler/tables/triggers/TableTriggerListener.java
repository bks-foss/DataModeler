package com.isb.datamodeler.tables.triggers;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EConstraint;
import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.model.triggers.AbstractTriggerListener;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.tables.ETablesPackage;

public class TableTriggerListener extends AbstractTriggerListener {
	
	public static NotificationFilter addColumnFilter;
	
	public static NotificationFilter addManyColumnsFilter;
	
	public static NotificationFilter addConstraintFilter;
	
	public static NotificationFilter removeConstraintFilter;
	
	public static NotificationFilter removeManyConstraintsFilter;
	
	public static NotificationFilter setNameFilter;

	
	static{
		
		addColumnFilter = NotificationFilter
        .createNotifierTypeFilter(EBaseTable.class).and(
            NotificationFilter.createEventTypeFilter(Notification.ADD)).and(
            NotificationFilter.createFeatureFilter(
            		EBaseTable.class,
            		ETablesPackage.BASE_TABLE__COLUMNS));
		
		addManyColumnsFilter = NotificationFilter
		        .createNotifierTypeFilter(EBaseTable.class).and(
		            NotificationFilter.createEventTypeFilter(Notification.ADD_MANY)).and(
		            NotificationFilter.createFeatureFilter(
		            		EBaseTable.class,
		            		ETablesPackage.BASE_TABLE__COLUMNS));
		
		addConstraintFilter = NotificationFilter
        .createNotifierTypeFilter(EBaseTable.class).and(
            NotificationFilter.createEventTypeFilter(Notification.ADD)).and(
            NotificationFilter.createFeatureFilter(
            		EBaseTable.class,
            		ETablesPackage.BASE_TABLE__CONSTRAINTS));
		
		removeConstraintFilter = NotificationFilter
        .createNotifierTypeFilter(EBaseTable.class).and(
            NotificationFilter.createEventTypeFilter(Notification.REMOVE)).and(
            NotificationFilter.createFeatureFilter(
            		EBaseTable.class,
            		ETablesPackage.BASE_TABLE__CONSTRAINTS));
		
		removeManyConstraintsFilter = NotificationFilter
		        .createNotifierTypeFilter(EBaseTable.class).and(
		            NotificationFilter.createEventTypeFilter(Notification.REMOVE_MANY)).and(
		            NotificationFilter.createFeatureFilter(
		            		EBaseTable.class,
		            		ETablesPackage.BASE_TABLE__CONSTRAINTS));
		
		setNameFilter = NotificationFilter
        .createNotifierTypeFilter(EBaseTable.class).and(
            NotificationFilter.createEventTypeFilter(Notification.SET)).and(
            NotificationFilter.createFeatureFilter(
            		EBaseTable.class,
            		ETablesPackage.BASE_TABLE__NAME));
		
	}
	
	public TableTriggerListener() {
		super(NotificationFilter.createNotifierTypeFilter(ETable.class));
	}

	@Override
	protected Command trigger(
			TransactionalEditingDomain domain, Notification notification) {
		
		EBaseTable table = (EBaseTable)notification.getNotifier();

//		if(addColumnFilter.matches(notification))
//		{
//			EColumn newColumn = (EColumn)notification.getNewValue();
//			
//			return new TableColumnInitializerTrigger(domain, table, newColumn);
//		}
//		
//		if(addManyColumnsFilter.matches(notification))
//		{
//			List<EColumn> newColumns = (List<EColumn>)notification.getNewValue();
//			
//			return new TableColumnInitializerTrigger(domain, table, newColumns);
//		}
		
			
		if(addConstraintFilter.matches(notification))
		{
			EConstraint newConstraint = (EConstraint)notification.getNewValue();
			
			return new TableConstraintInitializerTrigger(domain, table, newConstraint);
		}
		
		//Cuando se elimina una Constraint, los miembros no se eliminan explicitamente, por eso los eliminamos para forzar el trigger de borrado de miembros de la constraint
		if(removeConstraintFilter.matches(notification))
		{
			EReferenceConstraint removedConstraint = (EReferenceConstraint)notification.getOldValue();
			return RemoveCommand.create(domain, removedConstraint, EConstraintsPackage.Literals.REFERENCE_CONSTRAINT__MEMBERS, removedConstraint.getMembers());
		}
		
		//Cuando se elimina una Constraint, los miembros no se eliminan explicitamente, por eso los eliminamos para forzar el trigger de borrado de miembros de la constraint
		if(removeManyConstraintsFilter.matches(notification))
		{
			List<EReferenceConstraint> removedConstraints = (List<EReferenceConstraint>)notification.getOldValue();
			CompoundCommand cc = new CompoundCommand();
			
			for(EReferenceConstraint removedConstraint:removedConstraints)
				cc.append(RemoveCommand.create(domain, removedConstraint, EConstraintsPackage.Literals.REFERENCE_CONSTRAINT__MEMBERS, removedConstraint.getMembers()));
			
			return cc;
		}

		if(setNameFilter.matches(notification))
		{
			return new TableChildrenRefactorizerTrigger(domain, table);
		}
		
		return null;
	}

}