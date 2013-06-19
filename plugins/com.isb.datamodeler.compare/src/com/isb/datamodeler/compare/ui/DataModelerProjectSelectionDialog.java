package com.isb.datamodeler.compare.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.isb.datamodeler.compare.messages.Messages;
import com.isb.datamodeler.model.core.DataModelerNature;


/**
 * Crea el diálogo para visualizar los proyectos datamodeler que existen en el workspace 
 * para seleccionar uno de ellos y poder compararlo. A partir del projecto donde hemos iniciado la
 * acción. El proyecto que se seleccione de este diálogo es el projecto del lado derecho de la comparación.
 * 
 * @author Alfonso
 *
 */
public class DataModelerProjectSelectionDialog {

	/** */	
	private Shell _shell;
	
	/** proyecto a partir del cual se desea comparar con otro */
	private IProject _selectedProject;
	
	
	public DataModelerProjectSelectionDialog(Shell shell, IProject selectedProject) {
		_shell = shell;
		_selectedProject = selectedProject;
	}
	
	/**
	 * Muestra el dialogo para seleccionar otro proyecto datamodeler para comparar
	 */
	public IProject showSelectionDialog()
	{
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(_shell, new InternalLabelProvider());
		dialog.setElements(getModelerProjects());
		dialog.setMessage(Messages.bind("datamodeler.compare.project.selection.to.compare.title"));  //$NON-NLS-1$
		if (dialog.open() != Window.OK) {
			return null;
		}
		
		Object[] result = dialog.getResult();
		if (result.length > 0)
			return (IProject) result[0];
		
		return null;
	}
	
	/** 
	 * S�lo para imagen asociada al proyecto de vegamodeler.
	 */ 
	private static class InternalLabelProvider  implements ILabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {						
		}

		@Override
		public void dispose() {						
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {					
		}

		@Override
		public Image getImage(Object element) {
			return (new WorkbenchLabelProvider()).getImage(element);
		}

		@Override
		public String getText(Object element) {
			return ((IProject) element).getName();
		}
		
	}
		
	/**
	 * Obtiene los proyectos de datamodeler que existen en el workspace.	 
	 * TODO: Pasar a un utils en un futuro
	 * 
	 * @return
	 */
	private IProject[] getModelerProjects () {
		List<IProject> dataModelerProjects = new ArrayList<IProject> ();
		try {
			
			IProject[] wksProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for(int i=0; i<wksProjects.length; i++) {
			
				if (wksProjects[i].equals(_selectedProject))
					continue;
				
				if(wksProjects[i].getNature(DataModelerNature.DATA_MODELER_NATURE_ID)!=null)
					dataModelerProjects.add(wksProjects[i]);
			}
		} catch (CoreException e) {			
		}
		return dataModelerProjects.toArray(new IProject[dataModelerProjects.size()]);
		
	}
}
