package com.isb.datamodeler.diagram.properties;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;


public class DataModelerPropertySource extends PropertySource {

	private boolean _isShortCut;
	
	public DataModelerPropertySource(Object object,
			IItemPropertySource itemPropertySource , boolean isShortCut) {
		super(object, itemPropertySource);
		_isShortCut = isShortCut;
	}
	public DataModelerPropertySource(Object object,
			IItemPropertySource itemPropertySource) {
		this(object, itemPropertySource , false);
	}

	@Override
	protected IPropertyDescriptor createPropertyDescriptor(
			IItemPropertyDescriptor itemPropertyDescriptor) {
	    return new DataModelerPropertyDescriptor(object, itemPropertyDescriptor, _isShortCut);
	}
	
	

}
