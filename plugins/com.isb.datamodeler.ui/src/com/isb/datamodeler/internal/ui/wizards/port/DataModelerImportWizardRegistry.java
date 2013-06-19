package com.isb.datamodeler.internal.ui.wizards.port;

import com.isb.datamodeler.internal.registry.AbstractExtensionWizardRegistry;
import com.isb.datamodeler.internal.registry.IDataModelerUIRegistryConstants;
import com.isb.datamodeler.ui.DataModelerUI;


public class DataModelerImportWizardRegistry extends AbstractExtensionWizardRegistry {

	private static DataModelerImportWizardRegistry singleton;
	
	/**
	 * Return the singleton instance of this class.
	 * 
	 * @return the singleton instance of this class
	 */
	public static synchronized DataModelerImportWizardRegistry getInstance() {		
		if (singleton == null) {
			singleton = new DataModelerImportWizardRegistry();
		}
		return singleton;
	}
		
	
	/**
	 * 
	 */
	public DataModelerImportWizardRegistry() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.wizards.AbstractExtensionWizardRegistry#getExtensionPoint()
	 */
	protected String getExtensionPoint() {
		return IDataModelerUIRegistryConstants.PL_IMPORT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.wizards.AbstractExtensionWizardRegistry#getPlugin()
	 */
	protected String getPlugin() {
		return DataModelerUI.PLUGIN_ID;
	}
}
