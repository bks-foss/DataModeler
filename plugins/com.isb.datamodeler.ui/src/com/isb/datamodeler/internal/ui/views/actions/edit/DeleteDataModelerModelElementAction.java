package com.isb.datamodeler.internal.ui.views.actions.edit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.action.DeleteAction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.model.triggers.AbstractTrigger;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.diagram.EDMDiagram;

/**
 * Action for Non Project DataModeler Elements
 * @author xIS05655
 *
 */
public class DeleteDataModelerModelElementAction extends DeleteAction {
	
	public DeleteDataModelerModelElementAction(TransactionalEditingDomain domain)
	{
		super(domain, true);
		setText(Messages.bind("DeleteAction.title"));
	}

	@Override
	public boolean updateSelection(IStructuredSelection selection) {
		
		CompoundCommand cc = new CompoundCommand();
		
		List<?> list = selection.toList();
	    
	    Collection<EObject> objectsToRemove = new ArrayList<EObject>();
	    Collection<EDMDiagram> diagramsToRemove = new ArrayList<EDMDiagram>();
	    
	    for(Object object:list)
	    	if(object instanceof IAdaptable)
	    	{
	    		EObject eObject = (EObject)((IAdaptable)object).getAdapter(EObject.class);
	    		
	    		if (eObject instanceof EDMDiagram)
	    			diagramsToRemove.add((EDMDiagram)eObject);
	    		else if(eObject!=null)
	    			objectsToRemove.add(eObject);
	    	}
	    
	    addDeleteDiagramCommands(objectsToRemove, diagramsToRemove);
	    
	    if(!diagramsToRemove.isEmpty())
	    	cc.append(createDeleteDiagramsCommand(diagramsToRemove));
	    
	    if(!objectsToRemove.isEmpty())
	    	cc.append(createCommand(objectsToRemove));
	    
	    command = cc.unwrap();
	    
	    return command.canExecute();
	}

	@Override
	public void run() {
		if(MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
			Messages.bind("DeleteDataModelerElementAction.dialog.title"), 
			Messages.bind("DeleteDataModelerElementAction.dialog.message")))
			{
				super.run();
			}
	}
	
	/**
	 * Add to diagramsToRemove the EDMDiagrams referencing the Schemas to remove.
	 * @param objectsToRemove
	 * @param diagramsToRemove
	 */
	private void addDeleteDiagramCommands(Collection<EObject> objectsToRemove, Collection<EDMDiagram> diagramsToRemove) {

		for(EObject objectToRemove: objectsToRemove)
			if(objectToRemove instanceof ESchema)
				addDeleteDiagramCommands((ESchema)objectToRemove, diagramsToRemove);
	}

	/**
	 * Add to diagramsToRemove the EDMDiagrams referencing the Schema to remove.
	 * @param schemaToRemove
	 * @param diagramsToRemove
	 */
	private void addDeleteDiagramCommands(ESchema schemaToRemove, Collection<EDMDiagram> diagramsToRemove) {
		
		for (EStructuralFeature.Setting setting : EcoreUtil.UsageCrossReferencer.find(schemaToRemove, domain.getResourceSet()))
			if(setting.getEObject() instanceof Diagram && !diagramsToRemove.contains(setting.getEObject()))
				diagramsToRemove.add((EDMDiagram)setting.getEObject());
	}

	/**
	 * Creates Delete Diagram Commands
	 * @param diagrams
	 * @return
	 */
	private CompoundCommand createDeleteDiagramsCommand(Collection<? extends Diagram> diagrams)
	{
		CompoundCommand cc = new CompoundCommand("Delete Diagrams");
		
		for (final Diagram diagram : diagrams)
			cc.append(new DeleteDiagramCommand((TransactionalEditingDomain)domain, diagram));
		
		return cc;
	}
	

	/**
	 * Delete Diagram Command
	 * @author xIS05655
	 *
	 */
	private class DeleteDiagramCommand extends AbstractTrigger
	{
		private Diagram _diagram;
		
		public DeleteDiagramCommand(TransactionalEditingDomain domain, Diagram diagram) {
			super(domain);
			
			_diagram = diagram;
		}

		@Override
		public void executeTrigger() {
			
			UtilsDataModelerUI.closeDiagram(_diagram);
			
			Resource resource = _diagram.getElement().eResource();
			resource.getContents().remove(_diagram);
		}
		
	}
	
}
