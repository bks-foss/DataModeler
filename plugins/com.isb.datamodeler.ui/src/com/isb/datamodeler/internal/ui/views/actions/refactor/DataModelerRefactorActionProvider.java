package com.isb.datamodeler.internal.ui.views.actions.refactor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

public class DataModelerRefactorActionProvider extends CommonActionProvider {

	private DataModelerRefactorActionGroup refactorActionGroup;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
	 */
	public void init(ICommonActionExtensionSite anActionSite) {
		refactorActionGroup = new DataModelerRefactorActionGroup();
	}

	public void dispose() { 
		refactorActionGroup.dispose();
	}

	public void fillActionBars(IActionBars actionBars) { 
		refactorActionGroup.fillActionBars(actionBars);
	}

	public void fillContextMenu(IMenuManager menu) { 
		refactorActionGroup.fillContextMenu(menu);
	}

	public void setContext(ActionContext context) { 
		refactorActionGroup.setContext(context);
	}

	public void updateActionBars() { 
		refactorActionGroup.updateActionBars();
	}

	

}
