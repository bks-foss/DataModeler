package com.isb.datamodeler.constraints.properties;

import org.eclipse.emf.common.notify.Adapter;

import com.isb.datamodeler.constraints.util.ConstraintsAdapterFactory;
import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;

public class ConstraintPropertyDescriptorAdapterFactory extends ConstraintsAdapterFactory {

	private static Adapter referenceConstraintProvider = new ReferenceConstraintItemPropertyDescriptorProvider();
	
	@Override
	public Adapter createReferenceConstraintAdapter() {
		return referenceConstraintProvider;
	}

	@Override
	public boolean isFactoryForType(Object object) {
		return object==IItemPropertyDescriptorProvider.class;
	}
	
}
