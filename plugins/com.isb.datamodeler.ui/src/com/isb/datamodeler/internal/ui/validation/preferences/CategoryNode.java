package com.isb.datamodeler.internal.ui.validation.preferences;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

import com.isb.datamodeler.model.core.validation.DataModelerCategoryDescriptor;
import com.isb.datamodeler.model.core.validation.DataModelerValidationManager;
import com.isb.datamodeler.model.core.validation.DataModelerValidatorDescriptor;

public class CategoryNode extends AbstractCategoryNode {
	private boolean grayed;
	private boolean checked;
	
	
	private List<ValidationNode> validations;
	
	private static class RootNode extends AbstractCategoryNode {
		
		RootNode(CheckboxTreeViewer tree) {
			super(tree, null, null);
		}


		@Override
		protected List<ICategoryNode> createChildren() {
		    DataModelerCategoryDescriptor[] topLevel = DataModelerValidationManager.getInstance().getCategories();
			List<ICategoryNode> result = new java.util.ArrayList<ICategoryNode>(
					topLevel.length);
			
			for (DataModelerCategoryDescriptor next : topLevel) {
				result.add(new CategoryNode(getTree(), next, this));
				
			}
			
			return result;
		}

		// implements the inherited method
		public void checkStateChanged(CheckStateChangedEvent event) {
			// I cannot be selected by the user, so there is never a transition
			
		}

		@Override
		public String getDescription() {
			return ""; //$NON-NLS-1$
		}

		@Override
		public List<ValidationNode> getValidations() {

			return Collections.emptyList();
		
		}

		// implements the inherited method
		public boolean isChecked() {
			return false;
		}

		// implements the inherited method
		public boolean isGrayed() {
			return false;
		}

		@Override
		public void updateCheckState(ICategoryNode child) {
			// I am never visible and don't represent a category, anyway
			
		}

		@Override
		public void updateCheckState(ValidationNode validator) {
			// I am never visible and don't represent a category, anyway
			
		}
		
	
	}

	private CategoryNode(
			CheckboxTreeViewer tree,
			DataModelerCategoryDescriptor categoryDescriptor,
			ICategoryNode parent) {
		
		super(tree, categoryDescriptor, parent);
	}
	public static ICategoryNode createRoot(CheckboxTreeViewer tree) {
	              
		ICategoryNode result = new RootNode(tree);
		
		result.revertFromPreferences();
		
		return result;
	}
	
	@Override
	protected List<ICategoryNode> createChildren() {

		return Collections.emptyList();
	
	}

	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		boolean newState = event.getChecked();
		
		if (isGrayed()) {
			// always transition to fully checked from the grayed state
			internalSetChecked(true);
		} else {
			// transition from unchecked to fully checked or vice-versa
			internalSetChecked(newState);
		}
	}

	@Override
	public String getDescription() {
		return getCategoryDescriptor().getDescription();
	}

	@Override
	public List<ValidationNode> getValidations() {
		if (validations == null) {
		    List<DataModelerValidatorDescriptor> descriptors = 
		    	DataModelerValidationManager.getInstance().getValidatorsExt(getCategoryDescriptor());
		    validations = new java.util.ArrayList<ValidationNode>(
		            descriptors.size());
		    
			for (DataModelerValidatorDescriptor next : descriptors) {
				ValidationNode node = ValidationNode.getInstance(next);
				
				validations.add(node);
			}
		}
		
		return validations;
	}

	@Override
	public boolean isChecked() {
		return checked;
	}

	@Override
	public boolean isGrayed() {
		
		return grayed;
	}

	@Override
	public void updateCheckState(ICategoryNode child) {
		if (child.isGrayed()) {
			// easy case
			grayed = true;
			checked = true;
			
			updateTree();
		} else {
			boolean newValue = child.isChecked();
			
			ICategoryNode[] children = getChildren();
			
			for (int i = 0; i < children.length; i++) {
				if (children[i].isGrayed()
						|| (children[i].isChecked() != newValue)) {
					grayed = true;
					checked = true;
					
					updateTree();
					
					return;
				}
			}
			
			// children have the same check state, and none is gray, so I'm
			// not gray and have the same check-state as they
			
			grayed = false;
			checked = newValue;
			
			updateTree();
		}
	}

	@Override
	public void updateCheckState(ValidationNode validator) {
		boolean newValue = validator.isChecked();
		boolean newChecked = newValue;
		boolean newGrayed = false;
		
		for (ValidationNode next : getValidations()) {
			if (next != validator) {
				if (next.isChecked() != newValue) {
					newGrayed = true;
					newChecked = true;
					
					// can quit as soon as I have found that I am gray-checked
					break;
				}
			}
		}
		
		checked = newChecked;
		grayed = newGrayed;
		
		updateTree();
	}
	protected void internalSetChecked(boolean newState) {
		if (getCategoryDescriptor().isMandatory()) {
			checked = true;
		} else {
			checked = newState;
		}
		
		grayed = false;
		
		propagateToConstraints(newState);
		
		
		updateTree();  // propagates my state up the tree, too
	}
	private void propagateToConstraints(boolean newChecked) {
		
		for (ValidationNode next : getValidations()) {
			// my constraints update my check state as necessary
			next.setChecked(newChecked);
		}
		
	}
	private void updateTree() {
		CheckboxTreeViewer tree = getTree();
		
		if (tree.getChecked(this) != isChecked()) {
			tree.setChecked(this, isChecked());
		}
		
		if (tree.getGrayed(this) != isGrayed()) {
			tree.setGrayed(this, isGrayed());
		}
		
		updateParent();
	}
	
	/**
	 * Updates my parent, if I have one, to reflect my new state.
	 */
	private void updateParent() {
		if (getParent() != null) {
			getParent().updateCheckState(this);
		}
	}
	@Override
	public ValidationNode[] getSelectedValidations() {

		List<ValidationNode> result = new java.util.ArrayList<ValidationNode>(
		        getValidations().size());
		
		for (ValidationNode next : getValidations()) {
			if (next.isChecked()) {
				result.add(next);
			}
		}
		
		return result.toArray(
			new ValidationNode[result.size()]);
	
	}
	@Override
	public String toString() {
		
		return getCategoryDescriptor().getName() ;
	}
	// extends the inherited method
	@Override
    public void applyToPreferences() {
		for (ValidationNode next : getValidations()) {
				next.applyToPreferences();
			}
		
		
		super.applyToPreferences();
	}
	
	// extends the inherited method
	@Override
    public void revertFromPreferences() {
		
		ValidationNode node = null;
		
		for (Iterator<ValidationNode> iter = getValidations().iterator();
		        iter.hasNext();) {
		    
			node = iter.next();
			
			node.revertFromPreferences();
		}
		
		if (node != null) {
			// force a recomputation of my check state
			updateCheckState(node);
		}
		
		
		super.revertFromPreferences();
	}
	
	// extends the inherited method
	@Override
    public void restoreDefaults() {
		
		ValidationNode node = null;
		
		for (Iterator<ValidationNode> iter = getValidations().iterator();
		        iter.hasNext();) {
		    
			node = iter.next();
			
			node.restoreDefaults();
		}
		
		if (node != null) {
			// force a recomputation of my check state
			updateCheckState(node);
		}
		
		
		super.restoreDefaults();
	}
}
