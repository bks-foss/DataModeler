package com.isb.datamodeler.internal.ui.wizards;

import java.io.IOException;
import java.util.LinkedList;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorUtil;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.UtilsDataModelerUI;

public class DataModelerDiagramWizard extends Wizard implements INewWizard
{
	
	/**
	 * Página del wizard
	 */
	private DataModelerDiagramWizardPage _wizardPage;
	
	private ESchema _schema;
	private Resource _resource;
	private Diagram _diagram;

	public DataModelerDiagramWizard()
	{
		super();
		setWindowTitle(Messages.bind("DataModelerDiagramWizard.window.title"));  //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_schema = (ESchema)selection.getFirstElement();
		
	}

	public void addPages() 
	{
		_wizardPage = new DataModelerDiagramWizardPage(
				Messages.bind("DataModelerDiagramWizard.dataModelerDiagramWizardPage.title"),_schema); //$NON-NLS-1$
		addPage(_wizardPage);
	}
	
	@Override
	public boolean performFinish()
	{
		_wizardPage.setPageComplete(false);
		TransactionalEditingDomain myEditingDomain = DataModelerUtils.getDataModelerEditingDomain();
		
		_schema = _wizardPage.getSchema();
		
		if(_schema.eIsProxy())
			_schema = (ESchema)EcoreUtil.resolve(_schema, myEditingDomain.getResourceSet());
		
		if(!_schema.eIsProxy())
			_resource = _schema.eResource();
		
		if (_resource == null) return true;
		
		String path = _resource.getURI().toPlatformString(true);
		
		LinkedList<IFile> affectedFiles = new LinkedList<IFile>();
		
		IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(path));
		
		if (workspaceResource instanceof IFile)
			affectedFiles.add((IFile) workspaceResource);
		
		AbstractTransactionalCommand command = new AbstractTransactionalCommand(
				myEditingDomain,
				"Comando de creación de diagramas", //$NON-NLS-1$
				affectedFiles) {

			protected CommandResult doExecuteWithResult(
					IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				
				_diagram = ViewService
						.createDiagram(_schema,
								SchemaEditPart.MODEL_ID,
								DatamodelerDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
				_diagram.setName(_wizardPage.getDiagramName());
				_resource.getContents().add(_diagram);

				return CommandResult.newOKCommandResult();
			}
		};
		try {
			OperationHistoryFactory.getOperationHistory().execute(command,
					new NullProgressMonitor(), null);
			
			_resource.save(DatamodelerDiagramEditorUtil.getSaveOptions());
			
			final URI diagramURI = EcoreUtil.getURI(_diagram);
			
			Display.getCurrent().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					if(diagramURI.toPlatformString(true)!=null)
						try
						{
							UtilsDataModelerUI.openDiagram(diagramURI, _diagram.getName());
						}
						catch (PartInitException ex)
						{
							DatamodelerDiagramEditorPlugin.getInstance().logError(
									"Unable to open editor", ex); //$NON-NLS-1$
						}
				}
			});
			
			

		} catch (ExecutionException e) {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Unable to create model and diagram", e); //$NON-NLS-1$
		} catch (IOException ex) {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Save operation failed for: " + _resource.getURI(), ex); //$NON-NLS-1$
		}
		
		return true;
	}
}
