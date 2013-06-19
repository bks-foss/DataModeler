package com.isb.datamodeler.tables.properties;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.provider.DataModelerItemPropertyDescriptor;
import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETablesPackage;

public class ColumnItemPropertyDescriptorProvider extends
		AdapterImpl implements IItemPropertyDescriptorProvider {

	@Override
	public ItemPropertyDescriptor createItemPropertyDescriptor(
			AdapterFactory adapterFactory, ResourceLocator resourceLocator,
			String displayName, String description, EStructuralFeature feature,
			boolean isSettable, boolean multiLine, boolean sortChoices,
			Object staticImage, String category, String[] filterFlags) {

		return new ColumnItemPropertyDescriptor(adapterFactory, 
				  resourceLocator, 
				  displayName, 
				  description, 
				  feature, 
				  isSettable, 
				  multiLine, 
				  sortChoices, 
				  staticImage, 
				  category, 
				  filterFlags);
	}

	class ColumnItemPropertyDescriptor extends DataModelerItemPropertyDescriptor {
  
		public ColumnItemPropertyDescriptor
		     (AdapterFactory adapterFactory,
		      ResourceLocator resourceLocator,
		      String displayName,
		      String description,
		      EStructuralFeature feature, 
		      boolean isSettable,
		      boolean multiLine,
		      boolean sortChoices,
		      Object staticImage,
		      String category,
		      String [] filterFlags)
		{
			super(adapterFactory, 
				  resourceLocator, 
				  displayName, 
				  description, 
				  feature, 
				  isSettable, 
				  multiLine, 
				  sortChoices, 
				  staticImage, 
				  category, 
				  filterFlags);
		}
		
		@Override
		public boolean canSetProperty(Object object) {
			
			boolean defaultValue = super.canSetProperty(object);
			
		      switch (feature.getEContainingClass().getEAllStructuralFeatures().indexOf(feature))
		      {
		        case ETablesPackage.COLUMN__NULLABLE:
		        	return canSetNullable((EColumn)object, defaultValue);
		        case ETablesPackage.COLUMN__PRIMITIVE_TYPE:
		        	return canSetPrimitiveType((EColumn)object, defaultValue);	
		        case ETablesPackage.COLUMN__PRIMARY_KEY:
		        	
		        	// No se permite marcar como PK una columna que forma parte de
		        	// una FK que referencia esa misma tabla
		        	EColumn col = (EColumn)object;
		        	
					if(col.getReferencingConstraints().size()>0)
					{
						for(EReferenceConstraint constraint: col.getReferencingConstraints())
						{
							if(constraint instanceof EForeignKey)
							{
								if(((EForeignKey)constraint).getParentTable() == col.getTable())
									defaultValue = false;
							}
						}
					}
		      }
			
			return defaultValue;
		}
		
		private boolean canSetPrimitiveType(EColumn column, boolean defaultValue) {
			
			//Si una columna forma parte de una FK de la tabla, 
			//no se permitirá modificar el tipo de datos (ni sus parámetros).
			if(column.isPartOfForeingKey())
				return false;
			
			return defaultValue;
		}

		private boolean canSetNullable(EColumn column, boolean defaultValue)
		{
			if(column.isPrimaryKey())
				return false;
			
			return defaultValue;
		}
		
		@Override
		public Collection<?> getChoiceOfValues(Object object) {
			
			//Obligamos a que se carguen los tipos primitivos unicamente de la base de datos padre de la columna
			//Como trabajamos con un unico dominio de edicion, todos los proyectos creados
			//van a proveer de tipos primitivos a este combo, si no filtramos se verán todos
			if(feature==ETablesPackage.Literals.COLUMN__PRIMITIVE_TYPE)
			{
				Collection<Object> choiceOfValues = new UniqueEList<Object>();
				
				EDatabase dataBase = ((EColumn)object).getTable().getSchema().getDatabase();
				
				if(dataBase!=null)
					choiceOfValues.addAll(dataBase.getPrimitiveDataTypes());
				
				return choiceOfValues;
			}
			
			return super.getChoiceOfValues(object); 
		}


	}
	
}
