package com.isb.datamodeler.compare.actions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

public class DataModelerCompareActionProvider extends CommonActionProvider {

	private DataModelerCompareActionGroup compareActionGroup;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
	 */
	public void init(ICommonActionExtensionSite anActionSite) {
		compareActionGroup = new DataModelerCompareActionGroup();
	}

	public void dispose() { 
		compareActionGroup.dispose();
	}

	public void fillActionBars(IActionBars actionBars) { 
		compareActionGroup.fillActionBars(actionBars);
	}

	public void fillContextMenu(IMenuManager menu) { 
		compareActionGroup.fillContextMenu(menu);
	}

	public void setContext(ActionContext context) { 
		compareActionGroup.setContext(context);
	}

	public void updateActionBars() { 
		compareActionGroup.updateActionBars();
	}

	

}
