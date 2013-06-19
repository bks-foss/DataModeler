package com.isb.datamodeler.compare.actions;

import java.util.Iterator;

import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.compare.Activator;
import com.isb.datamodeler.compare.core.DataModelerCompare;
import com.isb.datamodeler.compare.core.DataModelerCompareEditorInput;
import com.isb.datamodeler.compare.core.ExtendModelUtils;
import com.isb.datamodeler.compare.messages.Messages;
import com.isb.datamodeler.compare.ui.DataModelerProjectSelectionDialog;
import com.isb.datamodeler.model.core.DataModelerNature;


/**
 * Acción de la comparación en local.
 * 
 *
 */
public class CompareDataModelerAction extends SelectionListenerAction{

	private IProject _leftProject;	
	
	public CompareDataModelerAction() {
		super(Messages.bind("datamodeler.compare.local.label"));
	}
	
	@Override
	public void run() {
		DataModelerCompare dmCompare = new DataModelerCompare();
		DataModelerProjectSelectionDialog dialog = new DataModelerProjectSelectionDialog(null, _leftProject);
		IProject rightProject = dialog.showSelectionDialog();
		
		if (rightProject != null) {				
			
			// Hay que mirar que no exista otro para que se pueda cerrar como en VegaModeler.
			if (ExtendModelUtils.prepareView(_leftProject,rightProject))
				return;
				
			dmCompare.loadResources(_leftProject, rightProject);
			ComparisonResourceSnapshot snapshot = dmCompare.compare(false, true);
			
			// Llamamos para abrir un editor 
			if (snapshot != null) {			
				DataModelerCompareEditorInput editorInput = new DataModelerCompareEditorInput(snapshot, false);				
				CompareUI.openCompareEditor(editorInput);
				editorInput.getStructure().expandAll();
				editorInput.getContent().navigate(true);
				
			}
		}
	}
		
	
	@Override
	public boolean updateSelection(IStructuredSelection selection){
		_leftProject = null;

		if(selection instanceof IStructuredSelection)
			if (((IStructuredSelection) selection).size()>1) {
				return false;
			}
		
			for(Iterator<?> iter = ((IStructuredSelection)selection).iterator(); iter.hasNext();)
			{
				Object element = iter.next();
				
				if(element instanceof IAdaptable && ((IAdaptable)element).getAdapter(IProject.class)!=null)
				{
					IProject iProject = (IProject)((IAdaptable)element).getAdapter(IProject.class);
					
					if(iProject.isAccessible())
					{
						try 
						{
							if(iProject.getNature(DataModelerNature.DATA_MODELER_NATURE_ID)!=null)
								_leftProject = iProject;
						} catch (CoreException e) 
						{
							Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.action.local.title"), //$NON-NLS-1$
									Messages.bind("datamodeler.compare.action.local.text"), e)); //$NON-NLS-1$
						}
					}
				}
			}

		return _leftProject != null;
	}
	
}
