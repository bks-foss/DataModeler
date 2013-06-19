package com.isb.datamodeler.internal.ui.views.actions.create;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

public class DataModelerNewActionProvider extends CommonActionProvider {

	private DataModelerNewActionGroup newActionGroup;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
	 */
	public void init(ICommonActionExtensionSite anActionSite) {
		newActionGroup = new DataModelerNewActionGroup();
	}

	public void dispose() { 
		newActionGroup.dispose();
	}

	public void fillActionBars(IActionBars actionBars) { 
		newActionGroup.fillActionBars(actionBars);
	}

	public void fillContextMenu(IMenuManager menu) { 
		newActionGroup.fillContextMenu(menu);
	}

	public void setContext(ActionContext context) { 
		newActionGroup.setContext(context);
	}

	public void updateActionBars() { 
		newActionGroup.updateActionBars();
	}

	

}
