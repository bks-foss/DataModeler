package com.isb.datamodeler.datatypes.properties;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

import com.isb.datamodeler.datatypes.EDataType;
import com.isb.datamodeler.provider.DataModelerItemPropertyDescriptor;
import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.tables.EColumn;

public class DataTypeItemPropertyDescriptorProvider extends
		AdapterImpl implements IItemPropertyDescriptorProvider {

	@Override
	public ItemPropertyDescriptor createItemPropertyDescriptor(
			AdapterFactory adapterFactory, ResourceLocator resourceLocator,
			String displayName, String description, EStructuralFeature feature,
			boolean isSettable, boolean multiLine, boolean sortChoices,
			Object staticImage, String category, String[] filterFlags) {

		return new DataTypeItemPropertyDescriptor(adapterFactory, 
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

	class DataTypeItemPropertyDescriptor extends DataModelerItemPropertyDescriptor {
  
		public DataTypeItemPropertyDescriptor
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
			
			EObject dataTypeContainer = ((EDataType)object).eContainer();
			
			//Si una columna forma parte de una FK de la tabla, no se permitirá modificar el tipo de datos (ni sus parámetros).
			//TODO Cuando el tipo de datos es un UserDefinedType, no hay una relacion de Containment entre la columna y el tipo de datos
			//Hay que ver como obtenemos la columna en ese caso
			if(dataTypeContainer instanceof EColumn && ((EColumn)dataTypeContainer).isPartOfForeingKey())
				return false;
			
			return defaultValue;
		}
		


	}
	
}
