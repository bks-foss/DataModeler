package com.isb.datamodeler.internal.ui.validation.preferences;

import java.util.List;

import org.eclipse.jface.viewers.CheckboxTreeViewer;

import com.isb.datamodeler.model.core.validation.DataModelerCategoryDescriptor;

public abstract class AbstractCategoryNode implements ICategoryNode {

	private final CheckboxTreeViewer tree;
	
	private ICategoryNode[] children;
	private final DataModelerCategoryDescriptor categoryDescriptor;
	private final ICategoryNode parent;
	

	protected AbstractCategoryNode(
			CheckboxTreeViewer tree,
			DataModelerCategoryDescriptor categoryDescriptor,
			ICategoryNode parent) {
		
		this.tree = tree;
		this.categoryDescriptor = categoryDescriptor;
		this.parent = parent;

	}

	protected abstract List<ICategoryNode> createChildren();
	
	private void initChildren() {
		if (children == null) {
			List<ICategoryNode> childList = createChildren();
			children = childList.toArray(new ICategoryNode[childList.size()]);
		}
	}
	protected final CheckboxTreeViewer getTree() {
		return tree;
	}
	
	// implements the interface method
	public DataModelerCategoryDescriptor getCategoryDescriptor() {
		return categoryDescriptor;
	}
	// implements the interface method
	public ICategoryNode[] getChildren() {
		initChildren();
		
		return children;
	}

	// implements the interface method
	public ICategoryNode getParent() {
		return parent;
	}

	// implements the interface method
	public ValidationNode[] getSelectedValidations() {
		return new ValidationNode[0];
	}

	// implements the inherited method
	public boolean hasChildren() {
		return getChildren().length > 0;
	}
	// implements the inherited method
	public void applyToPreferences() {
		ICategoryNode[] currentChildren = getChildren();
		
		for (ICategoryNode element : currentChildren) {
			element.applyToPreferences();
		}
	}
	
	// implements the inherited method
	public void revertFromPreferences() {
		ICategoryNode[] currentChildren = getChildren();
		
		for (ICategoryNode element : currentChildren) {
			element.revertFromPreferences();
		}
	}

	// implements the inherited method
	public void restoreDefaults() {
		ICategoryNode[] currentChildren = getChildren();
		
		for (ICategoryNode element : currentChildren) {
			element.restoreDefaults();
		}
	}
}
