package com.isb.datamodeler.ui.notation.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.provider.DiagramItemProvider;

import com.isb.datamodeler.ui.project.provider.DatamodelerUIEditPlugin;

/**
 * Sobreescribimos el ItemProvider del Diagrama por defecto para tener nuestro propio Label
 * y poder sobreescribir el collectNewChildDescriptor
 * @author xIS05655
 *
 */
public class DataModelerDiagramItemProvider extends DiagramItemProvider {

	public DataModelerDiagramItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	

    @Override
	public Object getImage(Object object) {
		return overlayImage(object, DatamodelerUIEditPlugin.INSTANCE.getImage("full/obj16/Diagram")); //$NON-NLS-1$
	}



	@Override
	protected void collectNewChildDescriptors(Collection newChildDescriptors,
			Object object) {
	}

	@Override
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Diagram_name_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Diagram_name_feature", "_UI_Diagram_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 NotationPackage.Literals.DIAGRAM__NAME,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}
	
	@Override
	protected void addMeasurementUnitPropertyDescriptor(Object object) 
	{
		//no queremos que salga esta propiedad
	}
	
	@Override
	protected void addStylesPropertyDescriptor(Object object) 
	{
		//no queremos que salga esta propiedad
	}
	
	@Override
	public List getPropertyDescriptors(Object object) 
	{
		
		boolean addDescPropDescriptor = itemPropertyDescriptors==null;

		List descriptors = super.getPropertyDescriptors(object);
		
		//Añadimos una propiedad con el esquema del diagrama
		if(addDescPropDescriptor)
		{
			itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				DatamodelerUIEditPlugin.INSTANCE.getString("_UI_Diagram_schema_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", //$NON-NLS-1$
						new Object[]{DatamodelerUIEditPlugin.INSTANCE.getString("_UI_Diagram_schema_feature"), //$NON-NLS-1$
									getString("_UI_Diagram_type")}), //$NON-NLS-1$ 
				 NotationPackage.Literals.VIEW__ELEMENT,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
		}
			
		
		return descriptors;
	}
}
