package com.isb.datamodeler.compare.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.CVSCompareSubscriber;
import org.eclipse.team.internal.ccvs.core.CVSProviderPlugin;
import org.eclipse.team.internal.ccvs.core.CVSTag;
import org.eclipse.team.internal.ccvs.ui.tags.TagSelectionDialog;
import org.eclipse.team.internal.ccvs.ui.tags.TagSource;
import org.eclipse.team.internal.ui.TeamUIPlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.compare.Activator;
import com.isb.datamodeler.compare.core.DataModelerCompare;
import com.isb.datamodeler.compare.core.DataModelerCompareEditorInput;
import com.isb.datamodeler.compare.core.ExtendModelUtils;
import com.isb.datamodeler.compare.messages.Messages;
import com.isb.datamodeler.model.core.DataModelerNature;

/**
 *  
 *  Acción para la comparación en remoto vía CVS.
 *   
 * @author Alfonso.
 *
 */
@SuppressWarnings("restriction")
public class CompareRemoteDataModelerAction extends SelectionListenerAction{

	IResource _resource;
	IProject _leftProject;
	
	public CompareRemoteDataModelerAction() {
		super(Messages.bind("datamodeler.compare.remote.label"));
	}
		  
	protected CVSTag promptForTag(IResource[] resources) {
		   
		   CVSTag tag = TagSelectionDialog.getTagToCompareWith(getShell(), 
				   TagSource.create(resources), TagSelectionDialog.INCLUDE_ALL_TAGS);
			return tag;
		}
	  
	   protected Shell getShell() {
			
			IWorkbench workbench = TeamUIPlugin.getPlugin().getWorkbench();
			if (workbench == null) return null;
			
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();			
			if (window == null) return null;
			
			return window.getShell();
			
		}
	   
	/**
	 * Devuelve una lista de recursos cuyo miembro es el fichero del modelo  . 
	 * @return
	 */
	private IResource[] getResources() {
		IResource[] internalResources;
		List<IResource> result = new ArrayList<IResource> ();
		try {
			internalResources = _leftProject.members();
		
			for (IResource file : internalResources) {
				if (file.getName().contains(DataModelerCompare.DATA_MODELER_EXTENSION_FILE)) { 
					result.add(file);
					_resource = file;
					return result.toArray(new IResource[result.size()]);
				}
			}
		} catch (CoreException e) {		
			e.printStackTrace();
		}
		
		return result.toArray(new IResource[result.size()]);
	}
	
	@Override
	public boolean updateSelection(IStructuredSelection selection) {
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
		
		boolean enable = false;
		if (_leftProject != null) {
			
			RepositoryProvider provider = RepositoryProvider.getProvider(_leftProject);
			if(provider != null && CVSProviderPlugin.getTypeId().equals(provider.getID()))
				enable = true;
		} 
			
		return enable;
	}
	
	@Override
	public void run() {
		
		CVSTag tag = promptForTag(getResources());				
		if (tag == null) return;
		
		CVSCompareSubscriber compareSubscriber = new CVSCompareSubscriber(getResources(), tag);
		IStorage storage = null;
		
		if (_resource == null) return;
		
		// Hay que mirar que no exista otro para que se pueda cerrar como en VegaModeler.
		if (ExtendModelUtils.prepareView(_leftProject,null))
			return;
		
		// Realizamos el refresco...		
		try {
			compareSubscriber.refresh(new IResource[]{_resource}, IResource.DEPTH_ZERO, new NullProgressMonitor());
		
			if (compareSubscriber.getSyncInfo(_resource).getRemote()!= null)
				storage = compareSubscriber.getSyncInfo(_resource).getRemote().
					getStorage(new NullProgressMonitor());
			
			if (storage != null) {
				DataModelerCompare compare = new DataModelerCompare();
				compare.loadCVSResources(_resource, storage.getContents(), _resource.getName());
				ComparisonResourceSnapshot snapshot = compare.compare(false, true);
				
				// Llamamos para abrir un editor 
				if (snapshot != null) {						
					DataModelerCompareEditorInput editorInput = new DataModelerCompareEditorInput(snapshot, true);
					CompareUI.openCompareEditor(editorInput);
					editorInput.getStructure().expandAll();
					editorInput.getContent().navigate(true);
				}
			}
		} catch (TeamException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.action.local.title"), //$NON-NLS-1$
					Messages.bind("datamodeler.compare.action.local.text"), e)); //$NON-NLS-1$			
			
		} catch (CoreException e) {			
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.action.local.title"), //$NON-NLS-1$
					Messages.bind("datamodeler.compare.action.local.text"), e)); //$NON-NLS-1$			
		}
		
	}

}