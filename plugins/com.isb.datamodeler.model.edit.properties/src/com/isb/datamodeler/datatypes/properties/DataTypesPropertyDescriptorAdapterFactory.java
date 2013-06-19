package com.isb.datamodeler.datatypes.properties;

import org.eclipse.emf.common.notify.Adapter;

import com.isb.datamodeler.datatypes.util.DatatypesAdapterFactory;
import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;

/**
 * Registramos esta clase por punto de extension para que desde los ItemProviders pueda utilizarse
 * descriptores de propiedades alternativos
 * @author xIS05655
 *
 */
public class DataTypesPropertyDescriptorAdapterFactory extends DatatypesAdapterFactory {

	private static Adapter dataTypeProvider = new DataTypeItemPropertyDescriptorProvider();
	
	@Override
	public Adapter createDataTypeAdapter() {
		return dataTypeProvider;
	}

	@Override
	public boolean isFactoryForType(Object object) {
		return object==IItemPropertyDescriptorProvider.class;
	}
	
}
