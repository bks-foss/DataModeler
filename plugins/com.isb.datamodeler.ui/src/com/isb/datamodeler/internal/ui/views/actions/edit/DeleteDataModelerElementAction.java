package com.isb.datamodeler.internal.ui.views.actions.edit;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.ui.actions.DeleteResourceAction;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;

public class DeleteDataModelerElementAction extends SelectionListenerAction{
	
	private BaseSelectionListenerAction internalAction;
	
	private boolean allProjectSelected = false;
	private boolean allNonProjectSelected = false;
	
	private TransactionalEditingDomain domain;
	private final Shell shell;
	
	public DeleteDataModelerElementAction(TransactionalEditingDomain domain, final Shell shell)
	{
		super(Messages.bind("DeleteAction.title"));
		
		this.domain = domain;
		this.shell = shell;
	}

	@Override
	public boolean updateSelection(IStructuredSelection selection) {
		
		allNonProjectSelected = !getSelectedNonResources().isEmpty() && getSelectedResources().isEmpty(); 
		allProjectSelected = getSelectedNonResources().isEmpty() && !getSelectedResources().isEmpty();
		
		internalAction = makeDeleteAction();
		
		if(internalAction!=null)
			internalAction.selectionChanged(selection);
		
	    return  internalAction!=null && internalAction.isEnabled();
	}

	@Override
	public void run() {
		if(internalAction!=null)
			internalAction.run();
	}


	private BaseSelectionListenerAction makeDeleteAction() {
		
		if(allProjectSelected)
		{
			IShellProvider sp = new IShellProvider() {
				public Shell getShell() {
					return shell;
				}
			};
			
			return new DeleteResourceAction(sp);
		}
		
		if(allNonProjectSelected)
		{
			return new DeleteDataModelerModelElementAction(domain);
		}
		
		return null;
	}

	
}
