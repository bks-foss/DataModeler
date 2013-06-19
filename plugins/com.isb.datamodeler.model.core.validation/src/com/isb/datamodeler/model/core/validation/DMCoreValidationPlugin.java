package com.isb.datamodeler.model.core.validation;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class DMCoreValidationPlugin extends Plugin {

	private static DMCoreValidationPlugin plugin;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
	}
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	public static DMCoreValidationPlugin getDefault() {
		return plugin;
	}
	
}
