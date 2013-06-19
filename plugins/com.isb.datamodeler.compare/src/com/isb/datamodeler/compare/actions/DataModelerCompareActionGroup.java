package com.isb.datamodeler.compare.actions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;

import com.isb.datamodeler.compare.messages.Messages;

public class DataModelerCompareActionGroup extends ActionGroup{

	private CompareDataModelerAction compareWithLocalAction;
	private CompareRemoteDataModelerAction compareWithRemoteAction;
	
	public DataModelerCompareActionGroup() {
		makeActions();
	}

	public void fillContextMenu(IMenuManager menu) {
		
		IMenuManager submenu = new MenuManager(
				Messages.bind("datamodeler.compare.label"),
				"compareGroup");
		
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		boolean anyResourceSelected = !selection.isEmpty();
		
		if(!anyResourceSelected)
			return;

		compareWithRemoteAction.selectionChanged(selection);
		compareWithLocalAction.selectionChanged(selection);

		submenu.add(compareWithLocalAction);
		submenu.add(compareWithRemoteAction);
		
		menu.appendToGroup("group.compare", submenu);
		
	}

	public void fillActionBars(IActionBars actionBars) {

		updateActionBars();
	}

	protected void makeActions() {
		compareWithRemoteAction = new CompareRemoteDataModelerAction();
		compareWithLocalAction = new CompareDataModelerAction();
	}

	public void updateActionBars() {
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		compareWithRemoteAction.selectionChanged(selection);
		compareWithLocalAction.selectionChanged(selection);
	}
	
}
