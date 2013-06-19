package com.isb.datamodeler.constraints.triggers;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.constraints.ParentCardinality;
import com.isb.datamodeler.model.triggers.AbstractTriggerListener;
import com.isb.datamodeler.tables.EColumn;

public class ReferenceConstraintTriggerListener extends AbstractTriggerListener {
	
	public static NotificationFilter addUniqueConstraintMembersFilter;
	public static NotificationFilter moveUniqueConstraintMembersFilter;
	public static NotificationFilter addForeignKeyMembersFilter;
	public static NotificationFilter addForeignKeyReferencedMembersFilter;
	public static NotificationFilter removeUniqueConstraintMembersFilter;
	public static NotificationFilter removeForeignKeyMembersFilter;
	public static NotificationFilter removeForeignKeyReferencedMembersFilter;
	public static NotificationFilter setIsIdentifyingFilter;
	public static NotificationFilter setUniqueConstraintFilter;
	public static NotificationFilter setParentCardinalityFilter;
	public static NotificationFilter setForeignKeyParentTableFilter;
	
	//Filtros Basicos:
	public static NotificationFilter removeOrRemoveManyEventFilter = NotificationFilter.createEventTypeFilter(Notification.REMOVE)
														.or(NotificationFilter.createEventTypeFilter(Notification.REMOVE_MANY));
	
	public static NotificationFilter addOrAddManyEventFilter = NotificationFilter.createEventTypeFilter(Notification.ADD)
														.or(NotificationFilter.createEventTypeFilter(Notification.ADD_MANY));
	
	public static NotificationFilter addOrAddManyorSetEventFilter = NotificationFilter.createEventTypeFilter(Notification.ADD)
														.or(NotificationFilter.createEventTypeFilter(Notification.ADD_MANY)
														.or(NotificationFilter.createEventTypeFilter(Notification.SET)));
	
	public static NotificationFilter moveEventFilter = NotificationFilter.createEventTypeFilter(Notification.MOVE);


	public ReferenceConstraintTriggerListener() {
		super(NotificationFilter.createNotifierTypeFilter(EReferenceConstraint.class));
	}
	
	static{

		addUniqueConstraintMembersFilter = NotificationFilter
        .createNotifierTypeFilter(EUniqueConstraint.class).and(addOrAddManyorSetEventFilter).and(
            NotificationFilter.createFeatureFilter(
            		EReferenceConstraint.class,
            		EConstraintsPackage.REFERENCE_CONSTRAINT__MEMBERS));
		
		moveUniqueConstraintMembersFilter = NotificationFilter
        .createNotifierTypeFilter(EUniqueConstraint.class).and(moveEventFilter).and(
            NotificationFilter.createFeatureFilter(
            		EReferenceConstraint.class,
            		EConstraintsPackage.REFERENCE_CONSTRAINT__MEMBERS));
		
		addForeignKeyMembersFilter = NotificationFilter
        .createNotifierTypeFilter(EForeignKey.class).and(addOrAddManyEventFilter).and(
            NotificationFilter.createFeatureFilter(
            		EReferenceConstraint.class,
            		EConstraintsPackage.REFERENCE_CONSTRAINT__MEMBERS));
		
		addForeignKeyReferencedMembersFilter = NotificationFilter
        .createNotifierTypeFilter(EForeignKey.class).and(addOrAddManyEventFilter).and(
            NotificationFilter.createFeatureFilter(
            		EReferenceConstraint.class,
            		EConstraintsPackage.FOREIGN_KEY__REFERENCED_MEMBERS));
		
		removeUniqueConstraintMembersFilter = NotificationFilter
        .createNotifierTypeFilter(EUniqueConstraint.class).and(removeOrRemoveManyEventFilter).and(
            NotificationFilter.createFeatureFilter(
            		EReferenceConstraint.class,
            		EConstraintsPackage.REFERENCE_CONSTRAINT__MEMBERS));
		
		removeForeignKeyMembersFilter = NotificationFilter
        .createNotifierTypeFilter(EForeignKey.class).and(removeOrRemoveManyEventFilter).and(
            NotificationFilter.createFeatureFilter(
            		EReferenceConstraint.class,
            		EConstraintsPackage.REFERENCE_CONSTRAINT__MEMBERS));
		
		removeForeignKeyReferencedMembersFilter = NotificationFilter
        .createNotifierTypeFilter(EForeignKey.class).and(removeOrRemoveManyEventFilter).and(
            NotificationFilter.createFeatureFilter(
            		EReferenceConstraint.class,
            		EConstraintsPackage.FOREIGN_KEY__REFERENCED_MEMBERS));
		
		setIsIdentifyingFilter = NotificationFilter
        .createNotifierTypeFilter(EForeignKey.class).and(
            NotificationFilter.createEventTypeFilter(Notification.SET)).and(
            NotificationFilter.createFeatureFilter(
            		EForeignKey.class,
            		EConstraintsPackage.FOREIGN_KEY__IDENTIFYING));
		
		setUniqueConstraintFilter = NotificationFilter
        .createNotifierTypeFilter(EForeignKey.class).and(
            NotificationFilter.createEventTypeFilter(Notification.SET)).and(
            NotificationFilter.createFeatureFilter(
            		EForeignKey.class,
            		EConstraintsPackage.FOREIGN_KEY__UNIQUE_CONSTRAINT));

		setParentCardinalityFilter = NotificationFilter
        .createNotifierTypeFilter(EForeignKey.class).and(
            NotificationFilter.createEventTypeFilter(Notification.SET)).and(
            NotificationFilter.createFeatureFilter(
            		EForeignKey.class,
            		EConstraintsPackage.FOREIGN_KEY__PARENT_CARDINALITY));
		
		setForeignKeyParentTableFilter = NotificationFilter
        .createNotifierTypeFilter(EForeignKey.class).and(
            NotificationFilter.createEventTypeFilter(Notification.SET)).and(
            NotificationFilter.createFeatureFilter(
            		EForeignKey.class,
            		EConstraintsPackage.FOREIGN_KEY__PARENT_TABLE));
	}

	@Override
	protected Command trigger(TransactionalEditingDomain domain,
			Notification notification) {
		
		EReferenceConstraint constraint = (EReferenceConstraint)notification.getNotifier();
		Object newValue = notification.getNewValue();
		Object oldValue = notification.getOldValue();
		
		if(addUniqueConstraintMembersFilter.matches(notification))
		{
			EUniqueConstraint uk = (EUniqueConstraint)constraint;

			if(notification.getEventType()==Notification.ADD)
				return new AddUniqueConstraintMemberTrigger(domain, uk, (EColumn)newValue);
			
			if(notification.getEventType()==Notification.ADD_MANY)
				return new AddUniqueConstraintMemberTrigger(domain, uk, (List<EColumn>)newValue);
			
			if(notification.getEventType()==Notification.SET)
				return new AddUniqueConstraintMemberTrigger(domain, uk, (EColumn)newValue, (EColumn)oldValue);
			
		}
		
		if(moveUniqueConstraintMembersFilter.matches(notification))
		{
			EUniqueConstraint uk = (EUniqueConstraint)constraint;
			
			return new MoveUniqueConstraintMemeberTrigger(
					domain, uk, notification.getPosition(), ((Integer)oldValue).intValue());
		}
		
		if(addForeignKeyMembersFilter.matches(notification))
		{
			EForeignKey foreignKey = (EForeignKey)constraint;
			
			if(notification.getEventType()==Notification.ADD)
				return new AddForeignKeyMemberTrigger(domain, foreignKey, (EColumn)newValue);
			
			if(notification.getEventType()==Notification.ADD_MANY)
				return new AddForeignKeyMemberTrigger(domain, foreignKey, (List<EColumn>)newValue);
		}
		
		if(addForeignKeyReferencedMembersFilter.matches(notification))
		{
			EForeignKey foreignKey = (EForeignKey)constraint;
			
			if(notification.getEventType()==Notification.ADD)
				return new AddForeignKeyReferencedMembersTrigger(domain, foreignKey, (EColumn)newValue);
			
			if(notification.getEventType()==Notification.ADD_MANY)
				return new AddForeignKeyReferencedMembersTrigger(domain, foreignKey, (List<EColumn>)newValue);
			
		}
		
		if(removeUniqueConstraintMembersFilter.matches(notification))
		{
			EUniqueConstraint uk = (EUniqueConstraint)constraint;
			
			if(notification.getEventType()==Notification.REMOVE)
				return new RemoveUniqueConstraintMemberTrigger(domain, uk, (EColumn)oldValue);

			if(notification.getEventType()==Notification.REMOVE_MANY)
				return new RemoveUniqueConstraintMemberTrigger(domain, uk, (List<EColumn>)oldValue);

		}
		
		if(removeForeignKeyMembersFilter.matches(notification))
		{
			EForeignKey foreignKey = (EForeignKey)constraint;
			
			if(notification.getEventType()==Notification.REMOVE)
				return new RemoveForeignKeyMembersTrigger(domain, foreignKey, (EColumn)oldValue);

			if(notification.getEventType()==Notification.REMOVE_MANY)
				return new RemoveForeignKeyMembersTrigger(domain, foreignKey, (List<EColumn>)oldValue);
		}
		
		if(removeForeignKeyReferencedMembersFilter.matches(notification))
		{
			EForeignKey foreignKey = (EForeignKey)constraint;

			if(notification.getEventType()==Notification.REMOVE)
				return new RemoveForeignKeyReferencedMembersTrigger(domain, foreignKey, notification.getPosition());

			if(notification.getEventType()==Notification.REMOVE_MANY)
				return new RemoveForeignKeyReferencedMembersTrigger(domain, foreignKey, (int[])notification.getNewValue());
		}
		
		
		if(setIsIdentifyingFilter.matches(notification))
		{
			EForeignKey foreignKey = (EForeignKey)constraint;
			boolean isIdentifying = (Boolean)notification.getNewValue();
			
			return new SetIsIdentifyingTrigger(domain, foreignKey, isIdentifying);
			
		}
		
		//Si se cambia la UniqueConstraint referenciada por una FK, debemos recalcular los miembros de la FK
		if(setUniqueConstraintFilter.matches(notification))
		{
			EForeignKey foreignKey = (EForeignKey)constraint;
			EUniqueConstraint newUniqueConstraint = (EUniqueConstraint)notification.getNewValue();
			
			return new SetUniqueConstraintTrigger(domain, foreignKey, newUniqueConstraint);
		}

		if(setParentCardinalityFilter.matches(notification) && !notification.isTouch())
		{
			EForeignKey foreignKey = (EForeignKey)constraint;
			ParentCardinality newCardinality = (ParentCardinality)notification.getNewValue();
			ParentCardinality oldCardinality = (ParentCardinality)notification.getOldValue();

			if(!newCardinality.equals(oldCardinality))
				return new SetParentCardinalityTrigger(domain, foreignKey, newCardinality);
		}
		
		//Se elimina la Tabla referenciada por una FK (Ocurre al eliminar la tabla referenciada)
		if(setForeignKeyParentTableFilter.matches(notification) && notification.getNewValue()==null)
		{
			EForeignKey foreignKey = (EForeignKey)constraint;
			
			return new RemoveForeignKeyParentTableTrigger(domain, foreignKey);
		}
		
		return null;
	}

}
