package com.isb.datamodeler.internal.ui.validation.preferences;

import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;

import com.isb.datamodeler.model.core.validation.DataModelerCategoryDescriptor;

public interface ICategoryNode {


	String getDescription();
	

	boolean isChecked();
	

	boolean isGrayed();
	
	ICategoryNode getParent();
	
	/**
	 * Queries whether I have children.
	 * 
	 * @return <code>true</code> if I have children; <code>false</code> if I
	 *     am a leaf
	 */
	boolean hasChildren();
	
	/**
	 * Obtains my children.
	 * 
	 * @return my children, or an empty array if I am a leaf
	 */
	ICategoryNode[] getChildren();
	
	/**
	 * Obtains my constraints.
	 * 
	 * @return my constraints, or an empty list if I am not a leaf node
	 */
	List<ValidationNode> getValidations();
	
	/**
	 * Obtains the category that I represent if I am a leaf node.
	 * 
	 * @return my category, or <code>null</code> if I am an internal node
	 */
	public DataModelerCategoryDescriptor getCategoryDescriptor();
	
	/**
	 * Obtains the constraints that are selected in me, if I am a leaf node.
	 * 
	 * @return my selected constraints, or <code>[]</code> if I am an
	 *    internal node
	 */
	ValidationNode[] getSelectedValidations();
	
	/**
	 * Causes me to transition to a new check and/or gray state, according to
	 * the given <code>event</code>.
	 * 
	 * @param event a check-state event in the GUI
	 */
	void checkStateChanged(CheckStateChangedEvent event);
	
	/**
	 * Updates my state to reflect a change in the specified <code>child</code>.
	 * 
	 * @param child my changed child
	 */
	void updateCheckState(ICategoryNode child);
	
	/**
	 * Updates my state to reflect a change in the specified
	 * <code>constraint</code> selection.
	 * 
	 * @param constraint my changed constraint
	 */
	void updateCheckState(ValidationNode validator);
	
	/**
	 * Applies, recursively, my state to the constraint enablement preferences.
	 */
	void applyToPreferences();
	
	/**
	 * Reverts, recursively, my state from the current constraint enablement
	 * preferences.
	 */
	void revertFromPreferences();

	/**
	 * Restores, recursively, my state to the default.
	 */
	void restoreDefaults();
	
}
