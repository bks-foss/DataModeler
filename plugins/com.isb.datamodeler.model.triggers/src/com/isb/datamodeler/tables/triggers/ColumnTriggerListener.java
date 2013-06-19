package com.isb.datamodeler.tables.triggers;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.datatypes.EPrimitiveDataType;
//import com.isb.datamodeler.db2definition.validation.ColumnDataTypeValidator;
import com.isb.datamodeler.model.triggers.AbstractTriggerListener;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETablesPackage;

public class ColumnTriggerListener extends AbstractTriggerListener {

	public static NotificationFilter setPrimitiveDataTypeFilter;
	public static NotificationFilter setNullableFilter;
	public static NotificationFilter setNameFilter;

	
	static{
		
		setPrimitiveDataTypeFilter = NotificationFilter
        .createNotifierTypeFilter(EColumn.class).and(
            NotificationFilter.createEventTypeFilter(Notification.SET)).and(
            NotificationFilter.createFeatureFilter(
            		EColumn.class,
            		ETablesPackage.COLUMN__PRIMITIVE_TYPE));
		
		setNullableFilter = NotificationFilter
        .createNotifierTypeFilter(EColumn.class).and(
            NotificationFilter.createEventTypeFilter(Notification.SET)).and(
            NotificationFilter.createFeatureFilter(
            		EColumn.class,
            		ETablesPackage.COLUMN__NULLABLE));
		
		setNameFilter = NotificationFilter
        .createNotifierTypeFilter(EColumn.class).and(
            NotificationFilter.createEventTypeFilter(Notification.SET)).and(
            NotificationFilter.createFeatureFilter(
            		EColumn.class,
            		ETablesPackage.COLUMN__NAME));
		
	}
	
	public ColumnTriggerListener() {
		super(NotificationFilter.createNotifierTypeFilter(EColumn.class));
	}

	@Override
	protected Command trigger(
			TransactionalEditingDomain domain, Notification notification) {
		
		EColumn column = (EColumn)notification.getNotifier();
		EDatabase parentDataBase = column.getParentDataBase();
		
		//El método isTouch se usa para saber si el valor cambió realmente
		//Controlamos que la BD no sea null, ya que en el borrado, se dispara este trigger, al desetear la propiedad en las culumnas
		if(setPrimitiveDataTypeFilter.matches(notification) && !notification.isTouch() && parentDataBase!=null)
		{
			EPrimitiveDataType newType = (EPrimitiveDataType)notification.getNewValue();
			EPrimitiveDataType oldType = (EPrimitiveDataType)notification.getOldValue();

			return new SetColumnPrimitiveDataTypeTrigger(domain, column, newType, oldType);
		}
		
		//El método isTouch se usa para saber si el valor cambió realmente
		if(setNullableFilter.matches(notification) && !notification.isTouch() && parentDataBase!=null)
		{
			boolean newValue = (Boolean)notification.getNewValue();
			
			return new SetColumnNullableTrigger(domain, column, newValue);
		}
		
		//El método isTouch se usa para saber si el valor cambió realmente
//		if(setNameFilter.matches(notification) && !notification.isTouch())
//		{
//			String oldName = (String)notification.getOldValue();
//			// Comprobamos que se pueda renombrar
//			// TODO: Tenerlo en cuenta con la implementacion del RENOMBRADO 
//			ColumnDataTypeValidator c = new ColumnDataTypeValidator();
//			DiagnosticChain diagnostic = c.validateEDataType(
//					null, column, ESchemaPackage.eINSTANCE.getDataModelerNamedElement_Name().getName(), column.getName());
//			if(diagnostic !=null)
//				return null;
//			
//			
//			
//			return new SetColumnNameTrigger(domain, column, oldName);
//		}

		return null;
	}

}
