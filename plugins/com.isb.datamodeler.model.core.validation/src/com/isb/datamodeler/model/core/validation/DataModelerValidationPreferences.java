package com.isb.datamodeler.model.core.validation;

import org.eclipse.core.runtime.Preferences;

public class DataModelerValidationPreferences {
	static final String VALIDATION_DISABLED_PREFIX = "val.disabled/"; //$NON-NLS-1$
	
	
	private static final Preferences prefs =
		DMCoreValidationPlugin.getDefault().getPluginPreferences();
	
	
	/**
	 * Not instantiable, as all features are static.
	 */
	private DataModelerValidationPreferences() {
		super();
		
	}

	/**
	 * Saves the preferences.
	 */
	public static void save() {
		// TODO
		DMCoreValidationPlugin.getDefault().savePluginPreferences();
		
	}
	
	/**
	 * Queries whether the specified validation <code>ID</code> is disabled.
	 * 
	 * @param id the validation ID
	 * @return whether it is disabled
	 */
	public static boolean isValidationDisabled(String id) {
		
		return prefs.getBoolean(VALIDATION_DISABLED_PREFIX + id);
		
	}

	/**
	 * Queries whether the specified validation <code>ID</code> is disabled
	 * by default.
	 * 
	 * @param id the validation ID
	 * @return whether it is disabled
	 */
	public static boolean isValidationDisabledByDefault(String id) {
		
		return prefs.getDefaultBoolean(VALIDATION_DISABLED_PREFIX + id);
		
	}

	/**
	 * Sets whether the specified constraint <code>id</code> is disabled.
	 * 
	 * @param id the constraint ID
	 * @param disabled whether it is disabled
	 */
	public static void setValidationDisabled(String id, boolean disabled) {
		final String prefName = VALIDATION_DISABLED_PREFIX + id;
		final DataModelerValidatorDescriptor constraint =
			DataModelerValidationManager.getInstance().getValidatorDescriptor(id);
	
		
		prefs.setValue(prefName, disabled);
		
		if (constraint != null) {
			// set its enablement from the new preference value
			constraint.setEnabled(!disabled);
		} else {
			// remove this preference to declutter the prefs.ini file
			prefs.setToDefault(prefName);
		}
	}
}
