package com.isb.datamodeler.tables.properties;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

import com.isb.datamodeler.provider.DataModelerItemPropertyDescriptor;
import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.tables.ETablesPackage;

public class TableItemPropertyDescriptorProvider extends AdapterImpl implements
		IItemPropertyDescriptorProvider
{

@Override
public ItemPropertyDescriptor createItemPropertyDescriptor(
		AdapterFactory adapterFactory, ResourceLocator resourceLocator,
		String displayName, String description, EStructuralFeature feature,
		boolean isSettable, boolean multiLine, boolean sortChoices,
		Object staticImage, String category, String[] filterFlags)
{
	return new TableItemPropertyDescriptor(adapterFactory, 
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

	class TableItemPropertyDescriptor extends DataModelerItemPropertyDescriptor
	{
	
		public TableItemPropertyDescriptor(AdapterFactory adapterFactory,
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
		public Collection<?> getChoiceOfValues(Object object)
		{
			// No queremos que aparezcan columnas para añadir
			if(feature==ETablesPackage.Literals.TABLE__COLUMNS)
			{
				return new UniqueEList<Object>();
			}
			
			return super.getChoiceOfValues(object);
		}
		
	}
}
