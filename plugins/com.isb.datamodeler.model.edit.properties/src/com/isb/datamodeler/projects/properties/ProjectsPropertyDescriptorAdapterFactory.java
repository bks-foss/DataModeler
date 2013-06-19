package com.isb.datamodeler.projects.properties;

import org.eclipse.emf.common.notify.Adapter;

import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.ui.project.util.ProjectAdapterFactory;

/**
 * Registramos esta clase por punto de extension para que desde los ItemProviders pueda utilizarse
 * descriptores de propiedades alternativos
 * @author xIS06000
 *
 */
public class ProjectsPropertyDescriptorAdapterFactory extends ProjectAdapterFactory{

	private static Adapter projectProvider = new ProjectItemPropertyDescriptorProvider();
	
	@Override
	public Adapter createProjectAdapter() 
	{
		return projectProvider;
	}
	
	@Override
	public boolean isFactoryForType(Object object) {
		return object==IItemPropertyDescriptorProvider.class;
	}
}
