package com.isb.datamodeler.ui.wizards;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.ISetSelectionTarget;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.model.core.DataModelerNature;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.model.triggers.DataModelerTriggersPlugin;
import com.isb.datamodeler.model.triggers.initializers.IDatamodelerInitializer;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.project.EProject;
/**
 * Wizard para la creación de proyectos de Data Modeler
 *
 */
public class DataModelerProjectWizard extends Wizard implements INewWizard
{
	
	/**
	 * Extensión de los ficheros de modelo
	 */
	public static final String FILE_EXTENSION = "dm";
	
	/**
	 * Página del wizard
	 */
	private DataModelerProjectWizardPage _wizardPage;
	
	/**
	 * Proyecto
	 */
	private EProject _project;
	
	
	private IProject _newProject;
	
	/**
	 * Transanctional Editing Domain
	 */
	private TransactionalEditingDomain _editingDomain = DataModelerUtils.getDataModelerEditingDomain();
	
	protected IWorkbench _workbench;
	
    /**
     * The current selection.
     */
    protected IStructuredSelection selection;

	public DataModelerProjectWizard()
	{
		super();
		setWindowTitle(Messages.bind("DataModelerProjectWizard.title"));
	}
	
    /**
     * Returns the selection which was passed to <code>init</code>.
     *
     * @return the selection
     */
    public IStructuredSelection getSelection() {
        return selection;
    }
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		_workbench = workbench;
		this.selection = selection;
	}
	
	@Override
	public void addPages()
	{
		_wizardPage = DataModelerUI.getDefault().getDataModelerProjectConfigurationPage(); //$NON-NLS-1$
		addPage(_wizardPage); 
	}
	
	@Override
	public boolean performFinish()
	{
		_project = _wizardPage.getProject();
		
		if(_project.getCapability()==null || _project.getCapability().equals(""))
		{
			String newCapability = DataModelerUtils.generateRandomID(8);
			_project.setCapability(newCapability);
		}
		
		// Operación para crear el proyecto de manera sincrona.
		IRunnableWithProgress opCreateProject = new IRunnableWithProgress()
		{
			public void run(IProgressMonitor monitor) throws InvocationTargetException
			{
				//arrancamos el progressMonitor
				monitor.beginTask("",100);//$NON-NLS-1$
				monitor.setTaskName(Messages.bind("DataModelerProjectWizard.create.project"));//$NON-NLS-1$
				monitor.worked(25);
							
				try
				{
					createProject(_project.getName(), monitor);
				}
				catch (CoreException e)
				{
					UtilsDataModelerUI.log(e, "Exception creating Data Modeler project"); //$NON-NLS-1$
					throw new InvocationTargetException(e);
				}
				
				monitor.worked(25);
				monitor.done();
			}
		};
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IFile modelFile = root.getFile(root.getProject(_project.getName()).getFullPath().append(_project.getName()+"."+FILE_EXTENSION)); //$NON-NLS-1$

		// Operación para crear el modelo del proyecto.
		//
		WorkspaceModifyOperation createModelOperation =
			new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor progressMonitor) {
					try {

						// Crea el fichero del modelo
						final Resource resource = _editingDomain.getResourceSet().createResource(
								URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true));
						
						// Comando para añadir el proyecto al modelo
						AbstractTransactionalCommand addProjectCommand = new AbstractTransactionalCommand(_editingDomain, 
								Messages.bind("DataModelerDiagramWizard.creation.command"), Collections.EMPTY_LIST) //$NON-NLS-1$
						{
							@Override
							protected CommandResult doExecuteWithResult(
									IProgressMonitor monitor, IAdaptable info)
									throws ExecutionException 
							{
								EDatabase database = _wizardPage.getDataBase();
								IDatamodelerInitializer initializer = DataModelerTriggersPlugin.getInstance().getInitializer(_project);
								if(initializer!=null)
									initializer.initialize(_project, database);
								_project.setDatabase(database);
								resource.getContents().add(_project);
								
								try {
									Map<Object, Object> options = new HashMap<Object, Object>();
									options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
									resource.save(options);
								} catch (IOException e) {
									UtilsDataModelerUI.log(e, "Unable to store model and diagram resources"); //$NON-NLS-1$
								}
								return CommandResult.newOKCommandResult();
							}
						};

						OperationHistoryFactory.getOperationHistory().execute(addProjectCommand,
								new NullProgressMonitor(), null);
					}
					catch (Exception exception) {
						UtilsDataModelerUI.log(exception, exception.getMessage());
					}
					finally {
						progressMonitor.done();
					}
				}
			};

		// Ejecuta las operaciones
		try 
		{
			getContainer().run(true, false, new WorkspaceModifyDelegatingOperation(opCreateProject));
		
			getContainer().run(true, false, new WorkspaceModifyDelegatingOperation(createModelOperation));

			// Select the new file resource in the current view.
			//
			IWorkbenchWindow workbenchWindow = DataModelerUI.getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			final IWorkbenchPart activePart = page.getActivePart();
			if (activePart instanceof ISetSelectionTarget) {
				final ISelection targetSelection = new StructuredSelection(modelFile);
				DataModelerUI.getActiveWorkbenchShell().getDisplay().asyncExec
					(new Runnable() {
						 public void run() {
							 ((ISetSelectionTarget)activePart).selectReveal(targetSelection);
						 }
					 });
			}
			
			
			if(_newProject!=null)
			{
				IWorkingSet[] workingSets = _wizardPage.getSelectedWorkingSets();
				PlatformUI.getWorkbench().getWorkingSetManager().addToWorkingSets(_newProject,
						workingSets);
			}
			
			return true;
	
		} 
		catch (InterruptedException e) 
		{
			//si el proceso de creacion es interrumpido, miramos si ya se ha creado el proyecto para eliminarlo en ese caso
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(_project.getName());
			
			if(project!=null)
			{
				try 
				{
					project.delete(true, null);
				}
				catch (CoreException e1) 
				{
					MessageDialog.openError(getShell(),
							Messages.bind("DataModelerProjectWizard.MessageDialog.error"), e1.getMessage()); //$NON-NLS-1$
				}
			}
				
			return false;
		} 
		catch (InvocationTargetException e) 
		{
			UtilsDataModelerUI.log(e, "Exception creating Data Modeler project"); //$NON-NLS-1$
			return false;
		}

	}
	
	private void createProject(String projectName, IProgressMonitor monitor) throws CoreException
	{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		_newProject = root.getProject(projectName);
		_newProject.create(new SubProgressMonitor(monitor, 25));
		_newProject.open(new SubProgressMonitor(monitor, 25));
		
		_newProject.open(new SubProgressMonitor(monitor, 40));
	
		if(!_newProject.isAccessible())
		{
			UtilsDataModelerUI.log(null, "DataModelerProjectWizard.createProject()"); //$NON-NLS-1$
			return;
		}
	
		// Añadimos la naturaleza Data Modeler
		IProjectDescription description = _newProject.getDescription();
		String[] natures = description.getNatureIds();
		String[] newNatures = new String[natures.length + 1];
	
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
	
		newNatures[natures.length] = DataModelerNature.DATA_MODELER_NATURE_ID;
		description.setNatureIds(newNatures);
		_newProject.setDescription(description, null);
	}
}
