package com.isb.datamodeler.internal.ui.views.actions.validation;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

public class DataModelerValidationActionProvider extends CommonActionProvider {

	private DataModelerValidationActionGroup validationActionGroup;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
	 */
	public void init(ICommonActionExtensionSite anActionSite) {
		validationActionGroup = new DataModelerValidationActionGroup();
	}

	public void dispose() { 
		validationActionGroup.dispose();
	}

	public void fillActionBars(IActionBars actionBars) { 
		validationActionGroup.fillActionBars(actionBars);
	}

	public void fillContextMenu(IMenuManager menu) { 
		validationActionGroup.fillContextMenu(menu);
	}

	public void setContext(ActionContext context) { 
		validationActionGroup.setContext(context);
	}

	public void updateActionBars() { 
		validationActionGroup.updateActionBars();
	}

	

}
