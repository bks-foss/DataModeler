package com.isb.datamodeler.internal.ui.views.actions.porting;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

public class DataModelerPortActionProvider extends CommonActionProvider {

	private DataModelerPortActionGroup importExportActionGroup;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
	 */
	public void init(ICommonActionExtensionSite anActionSite) {
		importExportActionGroup = new DataModelerPortActionGroup(anActionSite);
	}

	public void dispose() { 
		importExportActionGroup.dispose();
	}

	public void fillActionBars(IActionBars actionBars) { 
		importExportActionGroup.fillActionBars(actionBars);
	}

	public void fillContextMenu(IMenuManager menu) { 
		importExportActionGroup.fillContextMenu(menu);
	}

	public void setContext(ActionContext context) { 
		importExportActionGroup.setContext(context);
	}

	public void updateActionBars() { 
		importExportActionGroup.updateActionBars();
	}

	

}
