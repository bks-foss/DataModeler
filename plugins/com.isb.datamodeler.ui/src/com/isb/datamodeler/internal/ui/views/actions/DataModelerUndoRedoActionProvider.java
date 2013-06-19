package com.isb.datamodeler.internal.ui.views.actions;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.eclipse.ui.operations.UndoRedoActionGroup;

import com.isb.datamodeler.model.core.DataModelerUtils;

public class DataModelerUndoRedoActionProvider extends CommonActionProvider {

	private UndoRedoActionGroup undoRedoGroup;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
	 */
	public void init(ICommonActionExtensionSite anActionSite) {
		IUndoContext undoContext = ((IWorkspaceCommandStack)DataModelerUtils.getDataModelerEditingDomain().getCommandStack()).getDefaultUndoContext();
		undoRedoGroup = new UndoRedoActionGroup(((ICommonViewerWorkbenchSite) anActionSite.getViewSite()).getSite(),
				undoContext, true);
	}

	public void dispose() {
		undoRedoGroup.dispose();
	}

	public void fillActionBars(IActionBars actionBars) {
		undoRedoGroup.fillActionBars(actionBars);
	}

	public void fillContextMenu(IMenuManager menu) {
		undoRedoGroup.fillContextMenu(menu);
	}

	public void setContext(ActionContext context) {
		undoRedoGroup.setContext(context);
	}

	public void updateActionBars() {
		undoRedoGroup.updateActionBars();
	}

}
