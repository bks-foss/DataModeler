package com.isb.datamodeler.internal.ui.views.actions.porting;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;

public class DataModelerPortActionGroup extends ActionGroup{

	private PortAction importAction;
	private PortAction exportAction;
	
	public DataModelerPortActionGroup(ICommonActionExtensionSite anActionSite) {
		makeActions(anActionSite);
	}

	public void fillContextMenu(IMenuManager menu) {
		
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		boolean anyResourceSelected = !selection.isEmpty();
		
		if(!anyResourceSelected)
			return;

		exportAction.selectionChanged(selection);
		importAction.selectionChanged(selection);

		menu.appendToGroup(ICommonMenuConstants.GROUP_PORT, importAction);
		menu.appendToGroup(ICommonMenuConstants.GROUP_PORT, exportAction);
		
	}

	protected void makeActions(ICommonActionExtensionSite anExtensionSite) {
		exportAction = new PortAction(PortAction.EXPORT);
		importAction = new PortAction(PortAction.IMPORT);
	}


	
}
