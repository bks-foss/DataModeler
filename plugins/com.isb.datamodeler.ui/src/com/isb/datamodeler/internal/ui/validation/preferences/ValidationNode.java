package com.isb.datamodeler.internal.ui.validation.preferences;

import org.eclipse.jface.viewers.CheckStateChangedEvent;

import com.isb.datamodeler.model.core.validation.DataModelerValidationPreferences;
import com.isb.datamodeler.model.core.validation.DataModelerValidatorDescriptor;

public class ValidationNode {

	private static final java.util.Map<String, ValidationNode> instanceMap =
		new java.util.HashMap<String, ValidationNode>();
	
	private final DataModelerValidatorDescriptor validator;
	private boolean checked = false;
	
	private final java.util.Set<ICategoryNode> categories =
		new java.util.HashSet<ICategoryNode>();
	
	private ValidationNode(DataModelerValidatorDescriptor validator) {
		this.validator = validator;
		
	}
	
	static ValidationNode getInstance(DataModelerValidatorDescriptor validator) {
		String id = validator.getId();
		ValidationNode result = null;
		
		if (id != null) {
			result = instanceMap.get(id);
			
			if (result == null) {
				result = new ValidationNode(validator);
				instanceMap.put(id, result);
			}
		}
		
		return result;
	}
	
	/**
	 * Flushes the current cache of constraint nodes.  This should only be
	 * called when the nodes are no longer in use.
	 */
	public static void flushCache() {
		instanceMap.clear();
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public String getId() {
		return validator.getId();
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public String getName() {
		return validator.getName();
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public String getDescription() {
		return validator.getDescription();
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public boolean isChecked() {
		return checked;
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public void setChecked(boolean checked) {
		if (checked != isChecked()) {
			if (isMandatory()) {
				// reject the attempt to uncheck me
				this.checked = true;
			} else {
				this.checked = checked;
			}
			
			updateCategories();
		}
	}
	
	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public boolean isMandatory() {
		return validator.getCategoryDescripor().isMandatory();

	}


	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public void checkStateChanged(CheckStateChangedEvent event) {
		if (event.getChecked() != isChecked()) {
			if (isMandatory() && !event.getChecked()) {
				// reject the attempt to uncheck me
				event.getCheckable().setChecked(this, true);
			} else {
				checked = event.getChecked();
			}
			
			updateCategories();
		}
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public void applyToPreferences() {
		// set the preference
		DataModelerValidationPreferences.setValidationDisabled(
			validator.getId(),
			!isChecked());
		
		// tell the constraint, too
		validator.setEnabled(isChecked());
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public void revertFromPreferences() {
		setChecked(!DataModelerValidationPreferences.isValidationDisabled(
			validator.getId()));
	}

	/* (non-Javadoc)
	 * Implements the inherited method.
	 */
	public void restoreDefaults() {
		setChecked(!DataModelerValidationPreferences.isValidationDisabledByDefault(
				validator.getId()));
	}

	private void updateCategories() {
		for (ICategoryNode next : categories) {
			next.updateCheckState(this);
		}
	}
}
