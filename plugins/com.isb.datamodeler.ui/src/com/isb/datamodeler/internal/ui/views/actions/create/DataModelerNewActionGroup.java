package com.isb.datamodeler.internal.ui.views.actions.create;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.navigator.ICommonMenuConstants;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerNewActionGroup extends ActionGroup{

	private NewProjectAction newProjectActionAction;
	private NewSchemaAction newSchemaAction;
	private NewDiagramAction newDiagramAction;
	
	public DataModelerNewActionGroup() {
		makeActions();
	}

	public void fillContextMenu(IMenuManager menu) {
		
		IMenuManager submenu = new MenuManager(
				Messages.bind("DataModelerViewActions.group.new"),
				"newGroup");
		
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		newDiagramAction.selectionChanged(selection);
		newSchemaAction.selectionChanged(selection);
		newProjectActionAction.selectionChanged(selection);

		submenu.add(newProjectActionAction);
		
		submenu.add(new Separator());
		
		submenu.add(newSchemaAction);
		submenu.add(newDiagramAction);
		
		if(selection instanceof TreeSelection)
			generateCreateChildActions(submenu, (TreeSelection)selection);
		
		menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, submenu);
		
	}

	protected void makeActions() {
		newSchemaAction = new NewSchemaAction();
		newDiagramAction = new NewDiagramAction();
		newProjectActionAction = new NewProjectAction();
	}

	private void generateCreateChildActions(IMenuManager menu, TreeSelection structuredSelection)
	{
		EditingDomain domain = DataModelerUtils.getDataModelerEditingDomain();
		
		Collection<?> descriptors = null;
		
		if (structuredSelection!=null && structuredSelection.size() == 1)
		{
			Object object = structuredSelection.getFirstElement();	
			
			EObject eObject = (EObject)((IAdaptable)object).getAdapter(EObject.class);
			
			if(!(eObject instanceof EProject))
				descriptors = domain.getNewChildDescriptors(eObject, null);
		}
		
		if (descriptors != null) {
			for (Object descriptor : descriptors) {
				menu.add(new CreateDataModelerChildAction(domain, structuredSelection, descriptor));
			}
		}
		
	}
	

}
