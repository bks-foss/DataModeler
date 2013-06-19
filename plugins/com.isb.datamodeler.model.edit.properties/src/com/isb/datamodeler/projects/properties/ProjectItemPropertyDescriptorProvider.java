package com.isb.datamodeler.projects.properties;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.schema.ESchemaPackage;

public class ProjectItemPropertyDescriptorProvider extends AdapterImpl implements
IItemPropertyDescriptorProvider{

	@Override
	public ItemPropertyDescriptor createItemPropertyDescriptor(
			AdapterFactory adapterFactory, ResourceLocator resourceLocator,
			String displayName, String description, EStructuralFeature feature,
			boolean isSettable, boolean multiLine, boolean sortChoices,
			Object staticImage, String category, String[] filterFlags) {

		return new ProjectItemPropertyDescriptor(adapterFactory, 
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
	
	class ProjectItemPropertyDescriptor extends ItemPropertyDescriptor {
		  
		public ProjectItemPropertyDescriptor
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
		        case ESchemaPackage.SQL_OBJECT__NAME:
		        	return false;	        	
		      }
			
			return defaultValue;
		}
	}

}
