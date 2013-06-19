package com.isb.datamodeler.internal.ui.views.actions.edit;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.TextActionHandler;
import org.eclipse.ui.navigator.ICommonMenuConstants;

import com.isb.datamodeler.model.core.DataModelerUtils;

/**
 * ActionGroup for Copy, Paste and Delete Actions for DataModelerView
 * @author xIS05655
 *
 */
public class DataModelerEditActionGroup extends ActionGroup{

	private CopyDataModelerElementAction copyAction;
	private PasteDataModelerElementAction pasteAction;
	private DeleteDataModelerElementAction deleteAction;
	
	private TextActionHandler textActionHandler;
	
	private Shell shell;
	
	public DataModelerEditActionGroup(IWorkbenchPartSite site) {
		shell = site.getShell();
		makeActions();
	}

	public void fillContextMenu(IMenuManager menu) {
		
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		boolean anyResourceSelected = !selection.isEmpty();
		
		if(!anyResourceSelected)
			return;

		copyAction.selectionChanged(selection);
		pasteAction.selectionChanged(selection);
		deleteAction.selectionChanged(selection);

		menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, copyAction);
		menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, pasteAction);
		menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, deleteAction);
		
	}

	public void fillActionBars(IActionBars actionBars) {
		
		if (textActionHandler == null) {
			textActionHandler = new TextActionHandler(actionBars); // hook
																	// handlers
		}
		
		textActionHandler.setCopyAction(copyAction);
		textActionHandler.setPasteAction(pasteAction);
		textActionHandler.setDeleteAction(deleteAction);
		// renameAction.setTextActionHandler(textActionHandler);
		updateActionBars();

		textActionHandler.updateActionBars();

	}

	protected void makeActions() {
		
		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
		
		copyAction = new CopyDataModelerElementAction();
		copyAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		copyAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_COPY);
		
		pasteAction = new PasteDataModelerElementAction();
		pasteAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		pasteAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_PASTE);
		
		deleteAction = new DeleteDataModelerElementAction(DataModelerUtils.getDataModelerEditingDomain(), shell);
		deleteAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		deleteAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_DELETE);

	}

	public void updateActionBars() {
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		copyAction.selectionChanged(selection);
		pasteAction.selectionChanged(selection);
		deleteAction.selectionChanged(selection);
	}
}
