package com.isb.datamodeler.compare;

import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.util.EMFComparePreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.isb.datamodeler.compare"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public void earlyStartup() {
		
		// Las preferencias por defecto se fuerzan a true para que no tenga en cuenta ID ni XMI-ID en el macheo
		IPreferenceStore prefs = EMFCompareUIPlugin.getDefault().getPreferenceStore();
		
		if (prefs.getBoolean(EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_ID)==false)
			prefs.setValue(EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_ID, true);
		
		if (prefs.getBoolean(EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_XMIID)==false)
			prefs.setValue(EMFComparePreferenceConstants.PREFERENCES_KEY_IGNORE_XMIID,true);				
	}

}
