package com.isb.datamodeler.internal.ui.views.actions.refactor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.navigator.ICommonMenuConstants;

import com.isb.datamodeler.internal.ui.views.actions.DetectPossibleInRelationsAction;
import com.isb.datamodeler.internal.ui.views.actions.DetectPossibleOutRelationsAction;


public class DataModelerRefactorActionGroup extends ActionGroup{

	private RenameDataModelerElementAction renameAction;
	private MoveDataModelerElementAction moveAction;
	private DetectPossibleRelationsAction detectInRelationsAction;
	private DetectPossibleRelationsAction detectOutRelationsAction;
	
	public DataModelerRefactorActionGroup() {
		makeActions();
	}

	public void fillContextMenu(IMenuManager menu) {
		
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		boolean anyResourceSelected = !selection.isEmpty();
		
		if(!anyResourceSelected)
			return;

		renameAction.selectionChanged(selection);
		moveAction.selectionChanged(selection);
		detectInRelationsAction.selectionChanged(selection);
		detectOutRelationsAction.selectionChanged(selection);

		menu.appendToGroup(ICommonMenuConstants.GROUP_REORGANIZE, renameAction);
		menu.appendToGroup(ICommonMenuConstants.GROUP_REORGANIZE, moveAction);
		menu.appendToGroup(ICommonMenuConstants.GROUP_REORGANIZE, detectInRelationsAction);
		menu.appendToGroup(ICommonMenuConstants.GROUP_REORGANIZE, detectOutRelationsAction);

		
	}

	protected void makeActions() {
		
		renameAction = new RenameDataModelerElementAction();
		moveAction = new MoveDataModelerElementAction();
		detectInRelationsAction = new DetectPossibleInRelationsAction();
		detectOutRelationsAction = new DetectPossibleOutRelationsAction();
	}

}
