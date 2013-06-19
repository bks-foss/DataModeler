package com.isb.datamodeler.internal.ui.wizards.port;

import com.isb.datamodeler.internal.registry.AbstractExtensionWizardRegistry;
import com.isb.datamodeler.internal.registry.IDataModelerUIRegistryConstants;
import com.isb.datamodeler.ui.DataModelerUI;

public class DataModelerExportWizardRegistry extends AbstractExtensionWizardRegistry {

	private static DataModelerExportWizardRegistry singleton;
	
	/**
	 * Return the singleton instance of this class.
	 * 
	 * @return the singleton instance of this class
	 */
	public static synchronized DataModelerExportWizardRegistry getInstance() {		
		if (singleton == null) {
			singleton = new DataModelerExportWizardRegistry();
		}
		return singleton;
	}
	
	/**
	 * 
	 */
	public DataModelerExportWizardRegistry() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.wizards.AbstractExtensionWizardRegistry#getExtensionPoint()
	 */
	protected String getExtensionPoint() {
		return IDataModelerUIRegistryConstants.PL_EXPORT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.wizards.AbstractExtensionWizardRegistry#getPlugin()
	 */
	protected String getPlugin() {
		return DataModelerUI.PLUGIN_ID;
	}
}
