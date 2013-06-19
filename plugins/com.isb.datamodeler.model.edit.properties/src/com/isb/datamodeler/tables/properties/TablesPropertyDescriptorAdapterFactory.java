package com.isb.datamodeler.tables.properties;

import org.eclipse.emf.common.notify.Adapter;

import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.tables.util.TablesAdapterFactory;

/**
 * Registramos esta clase por punto de extension para que desde los ItemProviders pueda utilizarse
 * descriptores de propiedades alternativos
 * @author xIS05655
 *
 */
public class TablesPropertyDescriptorAdapterFactory extends TablesAdapterFactory {

	private static Adapter columnProvider = new ColumnItemPropertyDescriptorProvider();
	private static Adapter tableProvider = new TableItemPropertyDescriptorProvider();
	
	@Override
	public Adapter createColumnAdapter() {
		return columnProvider;
	}
	
	@Override
	public Adapter createTableAdapter()
	{
		return tableProvider;
	}

	@Override
	public boolean isFactoryForType(Object object) {
		return object==IItemPropertyDescriptorProvider.class;
	}
	
}
