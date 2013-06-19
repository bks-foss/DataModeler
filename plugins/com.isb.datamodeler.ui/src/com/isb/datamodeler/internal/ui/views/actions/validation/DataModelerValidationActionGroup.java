package com.isb.datamodeler.internal.ui.views.actions.validation;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.ActionGroup;

public class DataModelerValidationActionGroup extends ActionGroup{

	private ModelValidateAction validateAction;
	
	public DataModelerValidationActionGroup() {
		makeActions();
	}

	public void fillContextMenu(IMenuManager menu) {
		
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		boolean anyResourceSelected = !selection.isEmpty();
		
		if(!anyResourceSelected)
			return;

		validateAction.selectionChanged(selection);

		menu.appendToGroup("group.validation", validateAction);
		
	}

	protected void makeActions() {
		validateAction = new ModelValidateAction();
	}
}
