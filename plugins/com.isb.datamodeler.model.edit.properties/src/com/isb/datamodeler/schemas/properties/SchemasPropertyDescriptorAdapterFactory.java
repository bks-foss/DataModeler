package com.isb.datamodeler.schemas.properties;

import org.eclipse.emf.common.notify.Adapter;

import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.schema.util.SchemaAdapterFactory;

/**
 * Registramos esta clase por punto de extension para que desde los ItemProviders pueda utilizarse
 * descriptores de propiedades alternativos
 * @author xIS06000
 *
 */
public class SchemasPropertyDescriptorAdapterFactory extends SchemaAdapterFactory{

	private static Adapter schemaProvider = new SchemaItemPropertyDescriptorProvider();
	
	@Override
	public Adapter createSchemaAdapter()
	{
		return schemaProvider;
	}
	
	@Override
	public boolean isFactoryForType(Object object) {
		return object==IItemPropertyDescriptorProvider.class;
	}
}
