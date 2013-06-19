package com.isb.datamodeler.internal.ui.validation.preferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.model.core.validation.AbstractDataModelerElementValidator;
import com.isb.datamodeler.model.core.validation.DataModelerValidationPreferences;

public class ValidationsSelectionBlock {

	private CheckboxTreeViewer categoryTree;
	private CheckboxTableViewer constraintList;
	private StyledText detailsArea;
	
	private Mediator mediator;
	
	private ICategoryNode rootcategory;
	private class CategoryTreeContents implements ITreeContentProvider {
		
		// implements the inherited method
		public Object[] getChildren(Object parentElement)
		{
			Collection<ICategoryNode> categories = new ArrayList<ICategoryNode>();
			
			for (ICategoryNode categoryNode : ((ICategoryNode)parentElement).getChildren())
			{
				// No queremos que se muestre la categoría de validaciones obligatorias
				if(!categoryNode.getCategoryDescriptor().getId().equals(AbstractDataModelerElementValidator.CATEGORY_MANDATORY_RULE))
					categories.add(categoryNode);
			}
			return categories.toArray();
		}

		// implements the inherited method
		public Object getParent(Object element) {
			return ((ICategoryNode)element).getParent();
		}

		// implements the inherited method
		public boolean hasChildren(Object element) {
			return ((ICategoryNode)element).hasChildren();
		}

		// implements the inherited method
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		// implements the inherited method
		public void dispose() {
			// no cached resources to dispose
		}

		// implements the inherited method
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// nothing to do
		}
	}

	private class ValidationListContents
			implements IStructuredContentProvider, ICheckStateListener {
		
		private CheckboxTableViewer viewer;
		private ICategoryNode category;
		
		public Object[] getElements(Object inputElement) {
			if (inputElement == null) {
				return new Object[0];
			} else {
				category = (ICategoryNode)inputElement;
				
				return category.getValidations().toArray();
			}
		}
	
		public void dispose() {
			// nothing to dispose
		}
	
		public void inputChanged(
				Viewer newViewer,
				Object oldInput,
				Object newInput) {
			if (viewer != null) {
				viewer.removeCheckStateListener(this);
			}
			
			viewer = (CheckboxTableViewer) newViewer;
			category = (ICategoryNode) newInput;
			
			if (viewer != null) {
				viewer.addCheckStateListener(this);
			}
		}

		/* (non-Javadoc)
		 * Redefines/Implements/Extends the inherited method.
		 */
		public void checkStateChanged(CheckStateChangedEvent event) {
			category.updateCheckState(
				(ValidationNode) event.getElement());
		}
	}
	
	private class Mediator
			implements ISelectionChangedListener, ICheckStateListener {
		
		private boolean respondingToUserSelection;
		
		// implements the interface method
		public void checkStateChanged(CheckStateChangedEvent event) {
			Object element = event.getElement();
			
			if (element instanceof ICategoryNode) {
				ICategoryNode node = (ICategoryNode)element;
				
				if (!respondingToUserSelection) {
					respondingToUserSelection = true;
					
					try {
						node.checkStateChanged(event);
						
						// update the constraint selections of the currently
						// selected category (because the one that changed
						// might be an ancestory
						IStructuredSelection selection =
							(IStructuredSelection) getCategoryTree().getSelection();
						
						if (!selection.isEmpty()) {
							selectCategory(
								(ICategoryNode) selection.getFirstElement());
						}
					} finally {
						respondingToUserSelection = false;
					}
				}
			} else {
				ValidationNode node = (ValidationNode)element;
				
				if (!respondingToUserSelection) {
					respondingToUserSelection = true;
					
					try {
						node.checkStateChanged(event);
					} finally {
						respondingToUserSelection = false;
					}
				}
			}
		}
		
		// implements the interface method
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection selection =
				(IStructuredSelection)event.getSelection();
				
			if (event.getSource().equals(getCategoryTree())) {
				handleCategorySelection(selection);
			} else if (event.getSource().equals(getConstraintList())) {
				handleConstraintSelection(selection);
			}
		}
		
		/**
		 * Handles a selection change in the category tree.
		 * 
		 * @param selection the new selection
		 */
		private void handleCategorySelection(IStructuredSelection selection) {
			if (!selection.isEmpty()) {
				selectCategory((ICategoryNode) selection.getFirstElement());
			} else {
				getConstraintList().setInput(null);
				clearDetailsArea();
			}
		}
		
		/**
		 * Selects the specified category in the constraints list.
		 * 
		 * @param category the category to select
		 */
		private void selectCategory(ICategoryNode category) {
			getConstraintList().setInput(category);
			selectValidations(category);
			setDetails(category);
		}
		
		/**
		 * Select, in the table viewer, the currently enabled constraints.
		 * 
		 * @param categoryNode the currently selected category node
		 */
		private void selectValidations(ICategoryNode categoryNode) {
			getConstraintList().setCheckedElements(
				categoryNode.getSelectedValidations());
		}
		
		/**
		 * Handles a selection change in the constraints list.
		 * 
		 * @param selection the new selection
		 */
		private void handleConstraintSelection(IStructuredSelection selection) {
			if (!selection.isEmpty()) {
				setDetails((ValidationNode)selection.getFirstElement());
			} else {
				clearDetailsArea();
			}
		}
	
		/**
		 * Clears my details area, showing the "no selection" message.
		 */
		void clearDetailsArea() {
			getDetailsArea().setText(Messages.bind("ValidationsSelectionBlock.nothing.selection")); //$NON-NLS-1$
		}
	
		/**
		 * Sets the details area to show the currently selected
		 * <code>category</code>'s category details.
		 * 
		 * @param category the category in the category tree
		 */
		private void setDetails(ICategoryNode category) {
			String description = (category == null)
				? Messages.bind("ValidationsSelectionBlock.category.not.have.description") //$NON-NLS-1$
				: category.getDescription();
		
			getDetailsArea().setText(description);
		}
	
		/**
		 * Sets the details area to show the currently selected
		 * <code>constraint</code>'s details.
		 * 
		 * @param constraint the constraint meta-data
		 */
		private void setDetails(ValidationNode validation) {
			// lots of style info
			List<StyleRange> styles = new java.util.ArrayList<StyleRange>(32);
			String text = validation.getDescription();
		
			getDetailsArea().setText(text);
			getDetailsArea().setStyleRanges(
					styles.toArray(new StyleRange[styles.size()]));
		}
	}
	
	/**
     * Creates the constraints selection composite on the given parent and filters
     * the composite based on the provided filter.
     *
     * @param parent parent for the newly created composite
     * @return the resulting constraint selection composite
     */
	public Composite createComposite(Composite parent) {
		SashForm result = new SashForm(parent, SWT.VERTICAL);
		
		result.setFont(parent.getFont());
		
		SashForm topPart = new SashForm(result, SWT.HORIZONTAL);
		createCategoryTree(topPart);
		createValidationList(topPart);
		
		createDetailsArea(result);
		
		result.setWeights(new int[] {70, 30});
		
		return result;
	}

	/**
	 * Helper method to create the category-tree part of the GUI.  The result
	 * is a form containing the checkbox tree and a prompt label.
	 * 
	 * @param parent the parent composite in which to create the tree
	 * @return the tree part of the GUI (itself a composite form)
	 */
	private Control createCategoryTree(Composite parent) {
		Composite form = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		form.setLayout(layout);
		
		Label prompt = new Label(form, SWT.NONE);
		prompt.setText(Messages.bind("ValidationsSelectionBlock.categories")); //$NON-NLS-1$
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		prompt.setLayoutData(data);
		
		categoryTree = new CheckboxTreeViewer(form);
		data = new FormData();
		data.top = new FormAttachment(prompt, 4);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		categoryTree.getControl().setLayoutData(data);
		
		rootcategory = CategoryNode.createRoot(categoryTree);
		categoryTree.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				// TODO Auto-generated method stub
				return super.getText(element);
			}
		});
		categoryTree.setContentProvider(new CategoryTreeContents());
		categoryTree.setInput(rootcategory);
		markEnabledCategories(rootcategory);
		
		categoryTree.addCheckStateListener(getMediator());
		categoryTree.addSelectionChangedListener(getMediator());
		
		return categoryTree.getTree();
	}
	
	/**
	 * Helper method to create the constraint-list part of the GUI.  The result
	 * is a form containing the constraints list and a prompt label.
	 * 
	 * @param parent the parent composite in which to create the list
	 * @return the list part of the GUI (itself a composite form)
	 */
	private Control createValidationList(Composite parent) {
		Composite form = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		form.setLayout(layout);
		
		Label prompt = new Label(form, SWT.NONE);
		prompt.setText(Messages.bind("ValidationsSelectionBlock.validations")); //$NON-NLS-1$
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		prompt.setLayoutData(data);
		
		constraintList = CheckboxTableViewer.newCheckList(form, SWT.CHECK | SWT.BORDER);
		data = new FormData();
		data.top = new FormAttachment(prompt, 4);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		constraintList.getControl().setLayoutData(data);
		
		constraintList.setContentProvider(new ValidationListContents());
		
		constraintList.setLabelProvider(new LabelProvider() {
				
			// redefines the inherited method
			@Override
			public String getText(Object element) {
				return ((ValidationNode)element).getName();
			}});
		
		constraintList.setSorter(new ViewerSorter());
		
		constraintList.addCheckStateListener(getMediator());
		constraintList.addSelectionChangedListener(getMediator());
		
		return constraintList.getControl();
	}
	
	/**
	 * Helper method to create the details are of the GUI.
	 * 
	 * @param parent the parent composite in which to create the details area
	 * @return the details text area
	 */
	private Control createDetailsArea(Composite parent) {
		detailsArea = new StyledText(
				parent,
				SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		
		getMediator().clearDetailsArea();
		
		return detailsArea;
	}
	
	/**
	 * Obtains my category tree.
	 * 
	 * @return the tree
	 */
	private CheckboxTreeViewer getCategoryTree() {
		return categoryTree;
	}
	
	/**
	 * Obtains my constraints list.
	 * 
	 * @return the list
	 */
	private CheckboxTableViewer getConstraintList() {
		return constraintList;
	}
	
	/**
	 * Obtains my details area.
	 * 
	 * @return the details text area
	 */
	private StyledText getDetailsArea() {
		return detailsArea;
	}
	
	/**
	 * Obtains my mediator.
	 * 
	 * @return my mediator
	 */
	private Mediator getMediator() {
		if (mediator == null) {
			mediator = new Mediator();
		}
		
		return mediator;
	}
	
	/**
     * Saves the constraint enablement changes made in the composite 
     * to the validation preferences
     */
	public boolean performOk() {
		rootcategory.applyToPreferences();
		DataModelerValidationPreferences.save();
		
		return true;
	}
	
	/**
     * Restores the defaults for the constraints listed in the composite
     */
	public void performDefaults() {
		rootcategory.restoreDefaults();
        
        // update the checked state of the current contents of the Constraints
        // list (if any)
        CheckboxTableViewer viewer = getConstraintList();
        Object input = viewer.getInput();
        
        if (input != null) {
            Object[] elements = ((IStructuredContentProvider) getConstraintList()
                    .getContentProvider()).getElements(input);
            
            if (elements != null) {
                int length = elements.length;
                
                for (int i = 0; i < length; i++) {
                    ValidationNode node = (ValidationNode) elements[i];
                    viewer.setChecked(node, node.isChecked());
                }
            }
        }
	}

	/**
	 * Helper method to set the currently enabled categories in the tree.
	 * Also sets gray states as appropriate.
	 * 
	 * @param root the root of the tree model
	 */
	private void markEnabledCategories(ICategoryNode root) {
		markEnabledCategories(root.getChildren());
	}
	
	private void markEnabledCategories(ICategoryNode[] categories) {
		for (ICategoryNode next : categories) {
			getCategoryTree().setChecked(
				next,
				next.isChecked());
			getCategoryTree().setGrayed(
				next,
				next.isGrayed());
			
			markEnabledCategories(next.getChildren());
		}
	}
	
	/* (non-Javadoc)
	 * Extends the inherited method.
	 */
	public void dispose() {
		// clean up the cached validation nodes
		ValidationNode.flushCache();
	}
}
