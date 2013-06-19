package com.isb.datamodeler.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditor;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.model.core.DataModelerNature;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.project.EProject;

public class UtilsDataModelerUI
{
	
	/**
	 * Devuelve todos los EProject de proyectos con naturaleza Data Modeler del wks
	 * @return
	 */
	public static Collection<EProject> findDataModelerEProjects()
	{		
		Collection<EProject> eProjects = new ArrayList<EProject>();

    	for(IFile file:findDataModelerFiles())
    	{
    		EProject eProject = findEProject(file);
    		if(eProject!=null)
    			eProjects.add(eProject);
    	}
    	
    	return eProjects;
	}

	public static EProject findEProject(IProject project)
	{
		if(!project.isAccessible())
			return null;
		
		//Colección de ficheros de modelo
		List<IFile> modelFiles = new ArrayList<IFile>();
		
		IResource[] projectMembers;
		try {
			projectMembers = project.members();
			
			for (IResource resource : projectMembers)
			{
				if(resource instanceof IFile && 
						(((IFile)resource).getFileExtension().equals("dm")))
					modelFiles.add((IFile)resource);
			}
			
		} catch (CoreException e) {
			log(e, "UtilsDataModelerUI.findDataModelerEProjects()");
		}
		
		if(!modelFiles.isEmpty())
			return findEProject(modelFiles.get(0));
		
		return null;
	}
	
	public static EProject findEProject(IFile file)
	{
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		
		URI fileURI = URI.createPlatformResourceURI(file.getFullPath()
				.toString(), true);
		final Resource resource = editingDomain.getResourceSet().getResource(
				fileURI, true);
		
		EProject project = null;
		for(EObject eObject:resource.getContents())
		{
			if(eObject instanceof EProject)
				//setResult((EProject)eObject);
				project=(EProject)eObject;
		}
		
		return project;
	}

	/**
	 * Devuelve todos los ficheros de modelo de proyectos con naturaleza Data Modeler del wks
	 * @return
	 */
	public static Collection<IFile> findDataModelerFiles()
	{
		// Colección de proyectos datamodeler en el workspace
		Collection<IProject> projects = findDataModelerProjects();
		
		//Colección de ficheros de modelo
		Collection<IFile> modelFiles = new ArrayList<IFile>();
		
		for(IProject project:projects)
		{
			IResource[] projectMembers;
			try {
				projectMembers = project.members();
				
				for (IResource resource : projectMembers)
				{
					if(resource instanceof IFile && 
							(((IFile)resource).getFileExtension().equals("dm")))
						modelFiles.add((IFile)resource);
				}
				
			} catch (CoreException e) {
				log(e, "UtilsDataModelerUI.findDataModelerEProjects()");
			}
		}
		
		return modelFiles;
	}
	/**
	 * Devuelve el fichero de modelo .dm de un proyecto 
	 * @return
	 */
	public static IFile findDataModelerFile(IProject project)
	{
		IResource[] projectMembers;
		//Colección de ficheros de modelo
		Collection<IFile> modelFiles = new ArrayList<IFile>();
		try {
			projectMembers = project.members();
						
			for (IResource resource : projectMembers)
			{
				if(resource instanceof IFile && 
						(((IFile)resource).getFileExtension().equals("dm")))
					return (IFile)resource;
			}
			
		} catch (CoreException e) {
			log(e, "UtilsDataModelerUI.findDataModelerEProjects()");
		}
		return null;
	}
	
	/**
	 * Devuelve todos los proyectos del wks con naturaleza Data Modeler
	 * @return
	 */
	public static Collection<IProject> findDataModelerProjects()
	{
		Collection<IProject> dataModelerProjects = new ArrayList<IProject>();
		
		try
		{
			IProject[] wksProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (IProject iProject : wksProjects)
			{
				if(iProject.isAccessible())
					if(iProject.getNature(DataModelerNature.DATA_MODELER_NATURE_ID)!=null)
						dataModelerProjects.add(iProject);
			}
		}
		catch (CoreException e)
		{
			log(e, "UtilsDataModelerUI.findDataModelerProjects()");
		}
		
		return dataModelerProjects;
	}
	
	/**
	 * Devuelve una colección con los nombres de los proyectos seleccionados
	 * en la vista del navegador de Data Modeler
	 */
	public static Collection<String> getSelectedProjectsInPreference()
	{
		String prefProjects = DataModelerUI.getDefault().getPreferenceStore().getString(
				DataModelerUI.DATA_MODELER_SELECTED_PROJECTS_PROPERTY);
		
		if(prefProjects.isEmpty())
			return new ArrayList<String>();
		
		// En las preferencias se guardan los proyectos separados por ;
		String[] projects = prefProjects.split(";");
		
		return Arrays.asList(projects);
	}
	
	/**
	 * Logger para una excepción y un mensaje
	 * @param e
	 * @param message
	 */
	public static void log(Throwable e, String message)
	{
		IStatus status= new Status(
			IStatus.ERROR,
			DataModelerUI.getDefault().getBundle().getSymbolicName(),
			IStatus.ERROR,
			message,
			e);
		log(status);
		// También añadimos la entrada a la consola.
		System.err.println(message);
		if(e != null)
			e.printStackTrace(System.err);
	}
	
	/**
	 * Logger para un IStatus
	 * @param status
	 */
	public static void log(IStatus status)
	{
		Plugin p = DataModelerUI.getDefault();
		if (p != null)
			p.getLog().log(status);
		else
		{
			System.err.println(status.getMessage());
			status.getException().printStackTrace();
		}
	}

	/**
	 * Aplica el "status" de la línea de estado de una página de diálogo. 
	 */
	public static void applyToStatusLine(DialogPage page, IStatus status)
	{
		String message = status.getMessage();
		switch (status.getSeverity())
		{
			case IStatus.OK :
				page.setMessage(message, IMessageProvider.NONE);
				break;
			case IStatus.WARNING :
				if (page.getMessage() != null)
					page.setMessage("", IMessageProvider.NONE);
				page.setMessage(message, IMessageProvider.WARNING);
				break;
			case IStatus.INFO :
				page.setMessage(message, IMessageProvider.INFORMATION);
				break;
			default :
				if (message.length() == 0)
				{
					message = null;
				}
				page.setMessage(message, IMessageProvider.ERROR);
				break;
		}
	}
	
	/**
	 * Find the most serious error 
	 */
	public static IStatus findMostSevere(List<IStatus> status)
	{
		IStatus max = null;
		for (IStatus curr : status)//int i = 0; i < status.length; i++)
		{
			//IStatus curr = status[i];
			if (curr.matches(IStatus.ERROR))
			{
				return curr;
			}
			if (max == null || curr.getSeverity() > max.getSeverity())
			{
				max = curr;
			}
		}
		return max;
	}
	
	public static boolean openDiagram(URI uri, String name)
	throws PartInitException {
		String path = uri.toPlatformString(true);
		IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(path));
		if (workspaceResource instanceof IFile) {
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			return null != page.openEditor(new URIEditorInput(
					uri, name), DatamodelerDiagramEditor.ID);
		}
		return false;
	}
	
	/**
	 * Muestra un diálogo para elegir un EProject
	 */
	public static EProject chooseEProject(Shell shell, List<EProject> projects)
	{
		ITreeContentProvider contentProvider = new ITreeContentProvider()
		{

			@Override
			public void dispose() {
			}
			@Override
			public void inputChanged(Viewer viewer, Object obj, Object obj1) {
			}

			@Override
			public Object[] getChildren(Object obj) {
				return ((ArrayList<EProject>)obj).toArray();
			}

			@Override
			public Object[] getElements(Object obj) {
				return getChildren(obj);
			}

			@Override
			public Object getParent(Object obj) {
				return obj;
			}

			@Override
			public boolean hasChildren(Object obj) {
				return false;
			}

		};
		
		ILabelProvider labelProvider= new ILabelProvider(){

			@Override
			public void addListener(
					ILabelProviderListener ilabelproviderlistener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object obj, String s) {
				return false;
			}

			@Override
			public void removeListener(
					ILabelProviderListener ilabelproviderlistener) {
			}

			@Override
			public Image getImage(Object obj) {

				ItemProviderAdapter adapter = (ItemProviderAdapter)DatamodelerDiagramEditorPlugin.getInstance().getItemProvidersAdapterFactory()
				.adapt(obj, IItemLabelProvider.class);

				return ExtendedImageRegistry.getInstance().getImage(adapter.getImage(obj));
			}

			@Override
			public String getText(Object obj) {
				return ((EProject)obj).getIProject().getName();
			}
			
		}; 

		ElementTreeSelectionDialog dialog= new ElementTreeSelectionDialog(
			shell, (ILabelProvider) labelProvider, contentProvider);
		dialog.setTitle(Messages.bind("DataModelerSchemaWizardPage.eproject.dialog.title")); //$NON-NLS-1$
		dialog.setMessage(Messages.bind("DataModelerSchemaWizardPage.eproject.dialog.description")); //$NON-NLS-1$
		dialog.setInput(projects);
		dialog.setInitialSelection(projects.get(0));
			
		if (dialog.open() == Window.OK) 
		{
			Object element = dialog.getFirstResult();
			if (element instanceof EProject) 
				return (EProject)element;
			
			return null;
		}
		
		return null;
	}
	
	/**
	 * Muestra un diálogo para elegir un EProject
	 */
	public static ESchema chooseESchema(Shell shell, List<ESchema> schemas)
	{
		ITreeContentProvider contentProvider = new ITreeContentProvider()
		{

			@Override
			public void dispose() {
			}
			@Override
			public void inputChanged(Viewer viewer, Object obj, Object obj1) {
			}

			@Override
			public Object[] getChildren(Object obj) {
				return ((ArrayList<ESchema>)obj).toArray();
			}

			@Override
			public Object[] getElements(Object obj) {
				return getChildren(obj);
			}

			@Override
			public Object getParent(Object obj) {
				return obj;
			}

			@Override
			public boolean hasChildren(Object obj) {
				return false;
			}

		};
		
		ILabelProvider labelProvider= new ILabelProvider(){

			@Override
			public void addListener(
					ILabelProviderListener ilabelproviderlistener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object obj, String s) {
				return false;
			}

			@Override
			public void removeListener(
					ILabelProviderListener ilabelproviderlistener) {
			}

			@Override
			public Image getImage(Object obj) {

				ItemProviderAdapter adapter = (ItemProviderAdapter)DatamodelerDiagramEditorPlugin.getInstance().getItemProvidersAdapterFactory()
				.adapt(obj, IItemLabelProvider.class);

				return ExtendedImageRegistry.getInstance().getImage(adapter.getImage(obj));
			}

			@Override
			public String getText(Object obj) {
				return ((ESchema)obj).getName();
			}
			
		}; 

		ElementTreeSelectionDialog dialog= new ElementTreeSelectionDialog(
			shell, (ILabelProvider) labelProvider, contentProvider);
		dialog.setTitle(Messages.bind("DataModelerDiagramWizardPage.eschema.dialog.title")); //$NON-NLS-1$
		dialog.setMessage(Messages.bind("DataModelerDiagramWizardPage.eschema.dialog.description")); //$NON-NLS-1$
		dialog.setInput(schemas);
		if(!schemas.isEmpty())
			dialog.setInitialSelection(schemas.get(0));
			
		if (dialog.open() == Window.OK) 
		{
			Object element = dialog.getFirstResult();
			if (element instanceof ESchema) 
				return (ESchema)element;
			
			return null;
		}
		
		return null;
	}

	public static void closeDiagrams(IProject project)
	{
		IFile file = UtilsDataModelerUI.findDataModelerFile(project);
		if(file==null)
			return;
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
					
		try
		{
			Resource resource = editingDomain.getResourceSet().getResource(uri, true);
			
			// Obtenemos los diagramas a borrar
			EList<EObject> resourceContents = resource.getContents();
			List<Diagram> diagrams = new ArrayList<Diagram>();
			
			for(EObject content : resourceContents)
			{
				if(content instanceof Diagram)
				{
					Diagram diagram = (Diagram)content;
					diagrams.add(diagram);

				}
			}
			for(Diagram diagram:diagrams)
			{
				URI diagramURI = EcoreUtil.getURI(diagram);
				for (IEditorReference editorRef : DataModelerUI.getActivePage().getEditorReferences())
				{
					if(editorRef.getEditorInput() instanceof URIEditorInput)
					{
						URI editorURI = ((URIEditorInput)editorRef.getEditorInput()).getURI();
						if(diagramURI.equals(editorURI))
						{
							DataModelerUI.getActivePage().closeEditor(editorRef.getEditor(true), true);
							break;
						}
					}
				}

			}
		}
		catch (Exception e) 
		{
			log(e, "UtilsDataModelerUI.deleteDiagrams()");
		}
	}

	public static void closeDiagram(Diagram diagram)
	{		
		try
		{
			URI diagramURI = EcoreUtil.getURI(diagram);
			for (IEditorReference editorRef : DataModelerUI.getActivePage().getEditorReferences())
			{
				if(editorRef.getEditorInput() instanceof URIEditorInput)
				{
					URI editorURI = ((URIEditorInput)editorRef.getEditorInput()).getURI();
					if(diagramURI.equals(editorURI))
					{
						DataModelerUI.getActivePage().closeEditor(editorRef.getEditor(true), true);
						break;
					}
				}
			}

		}
		catch (Exception e) 
		{
			log(e, "UtilsDataModelerUI.deleteDiagrams()");
		}
	}
}
