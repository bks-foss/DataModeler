package com.isb.datamodeler.internal.ui.wizards;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerSchemaWizard extends Wizard implements INewWizard{

	/**
	 * Página del wizard
	 */
	private DataModelerSchemaWizardPage _wizardPage;
	
	private IProject _project;
	
	public DataModelerSchemaWizard()
	{
		super();
		setWindowTitle(Messages.bind("DataModelerSchemaWizard.window.title"));  //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
		_project = (IProject)selection.getFirstElement();

	}
	
	public void addPages() 
	{
		_wizardPage = new DataModelerSchemaWizardPage(
				Messages.bind("DataModelerSchemaWizard.dataModelerSchemaWizardPage.title"),_project); //$NON-NLS-1$
		addPage(_wizardPage);
	}
	
	@Override
	public boolean performFinish() {

		_wizardPage.setPageComplete(false);
    	final ESchema schema = _wizardPage.getSchema();
		final EProject project = _wizardPage.getProject();
		
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		TransactionalCommandStack stack = (TransactionalCommandStack) editingDomain.getCommandStack();
	
		stack.execute(new RecordingCommand(editingDomain) {
		    protected void doExecute() {
		    	
				schema.setCapability(project.getCapability());
				schema.setDatabase(project.getDatabase());
				
		    	project.getSchemas().add(schema);	
		    	
		        }
		    });
	
		try {
			project.eResource().save(null);
		} catch (IOException e) {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Save operation failed for: " + project.eResource().getURI(), e); //$NON-NLS-1$
		}
		
		return true;
		    
	}
}
