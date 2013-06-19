package com.isb.datamodeler.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.schema.ESchema;

public abstract class DataModelerItemPropertyDescriptor extends ItemPropertyDescriptor {
	
	public DataModelerItemPropertyDescriptor(AdapterFactory adapterFactory,
			ResourceLocator resourceLocator, String displayName,
			String description, EStructuralFeature feature, boolean isSettable,
			boolean multiLine, boolean sortChoices, Object staticImage,
			String category, String[] filterFlags)
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
		
		ESchema parentSchema = getDataModelerObjectSchema(object);								
		boolean canSetProperty = parentSchema==null || !parentSchema.isExternal();
		return canSetProperty && super.canSetProperty(object);
	}
	
	private ESchema getDataModelerObjectSchema(Object object)
	{
		if(object instanceof ESQLObject)
		{
			EObject parent = (ESQLObject)object;
			
			while(parent.eContainer()!=null && !(parent instanceof ESchema))
				parent = parent.eContainer();
			
			if(parent instanceof ESchema)
				return (ESchema)parent;
				
		}
		
		return null;
	}

}
