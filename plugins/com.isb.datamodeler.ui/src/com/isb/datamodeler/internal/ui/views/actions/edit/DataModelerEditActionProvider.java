package com.isb.datamodeler.internal.ui.views.actions.edit;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

public class DataModelerEditActionProvider extends CommonActionProvider {

	private DataModelerEditActionGroup editActionGroup;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
	 */
	public void init(ICommonActionExtensionSite anActionSite) {
		editActionGroup = new DataModelerEditActionGroup(((ICommonViewerWorkbenchSite) anActionSite.getViewSite()).getSite());
	}

	public void dispose() { 
		editActionGroup.dispose();
	}

	public void fillActionBars(IActionBars actionBars) { 
		editActionGroup.fillActionBars(actionBars);
	}

	public void fillContextMenu(IMenuManager menu) { 
		editActionGroup.fillContextMenu(menu);
	}

	public void setContext(ActionContext context) { 
		editActionGroup.setContext(context);
	}

	public void updateActionBars() { 
		editActionGroup.updateActionBars();
	}

	

}
