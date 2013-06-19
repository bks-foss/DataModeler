package com.isb.datamodeler.internal.ui.views.actions.validation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.internal.ui.views.DataModelerValidationView;
import com.isb.datamodeler.internal.ui.views.actions.DataModelerProblems;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;
import com.isb.datamodeler.model.core.validation.DataModelerValidatorDescriptor;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.project.EProject;

public class ModelValidateAction extends SelectionListenerAction
{
	private EProject _eProject;
	private IProject _iProject;

	private Collection<DataModelerProblems> _basicErrors = new ArrayList<DataModelerProblems>();
	private Collection<DataModelerProblems> _basicWarnings = new ArrayList<DataModelerProblems>();

	private Collection<DataModelerProblems> _regulationErrors = new ArrayList<DataModelerProblems>();
	private Collection<DataModelerProblems> _regulationWarnings = new ArrayList<DataModelerProblems>();
	
	public ModelValidateAction() {
		super(Messages.bind("DMValidationAction.name"));
	}

	@Override
	public void run() {
		final Shell sh = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ProgressMonitorDialog pmDialog = new ProgressMonitorDialog(sh);
		
		DataModelerValidationView view = (DataModelerValidationView) DataModelerUI.getActivePage().findView(
				DataModelerValidationView.ID);
		
		initializeErrors();
		
		try {
			pmDialog.run(true, false, new IRunnableWithProgress()
			{
				public void run(final IProgressMonitor monitor)
				{
					try
					{
						if(_eProject == null)
							return;
						int totalWork = _eProject.eContents().size()*100;
						monitor.beginTask(Messages.bind("ModelValidateActionDelegate.progressmonitor"), totalWork); 
						
						DataModelerValidationView.setInput(new LinkedHashMap<String, Collection<DataModelerProblems>>());
						
						validateAction(monitor);
					}
					finally
					{
						monitor.done();
					}
				}
			});
			
			// Después de ejecutar la validación, actualizamos la vista
			Shell shell = DataModelerUI.getActiveWorkbenchShell();
			
			// Si no hay errores ni warnings mostramos un diálogo diciendo que todo ha ido bien
			if(_basicErrors.isEmpty() && _basicWarnings.isEmpty() &&
				_regulationErrors.isEmpty() && _regulationWarnings.isEmpty())
			{
								
				// Cerramos la vista
				if(view != null)
					DataModelerUI.getActivePage().hideView(view);
			
				MessageDialog.openInformation(shell,
						Messages.bind("ModelValidateActionDelegate.messagedialog.title"),//$NON-NLS-1$
						Messages.bind("ModelValidateActionDelegate.messagedialog.message"));//$NON-NLS-1$
			}
			else
			{
				Map<String, Collection<DataModelerProblems>> errAndWarnMap = collectErrAndWarn();
				
				// Por defecto mostramos el icono de warning
				try 
				{
					DataModelerValidationView.setProject(_iProject);
					DataModelerValidationView.setInput(errAndWarnMap);
					
					// Cerramos la vista si está abierta para que refresque los contenidos
					if(view != null)
						DataModelerUI.getActivePage().hideView(view);
					
					DataModelerUI.getActivePage().showView(DataModelerValidationView.ID);
				} 
				catch (Exception e) 
				{		
					UtilsDataModelerUI.log(e , "ModelValidateActionDelegate.run(): "+e);
				}
			}
		} catch (InvocationTargetException e) {
			UtilsDataModelerUI.log(e , "ModelValidateActionDelegate.run(): "+e);
		} catch (InterruptedException e) {
			UtilsDataModelerUI.log(e , "ModelValidateActionDelegate.run(): "+e);
		}
	}
	
	private void validateAction(IProgressMonitor monitor)
	{
		if(_eProject == null)
			return;
	
		// Borramos los marcadores de error
		IFile file = UtilsDataModelerUI.findDataModelerFile(_iProject);

		Diagnostic diagnostics = Diagnostician.INSTANCE.validate(_eProject);
		
		findProblems(diagnostics);
		
		for(Diagnostic childDiagnostic : diagnostics.getChildren())
		{
			if(childDiagnostic instanceof DataModelDiagnostic)
			{
				if(childDiagnostic.getSource()!=null)
				{
					DataModelerValidation.createDMMarker((DataModelDiagnostic)childDiagnostic, file);
				}
			}
			updateMonitor(monitor);
		}
		updateDiagramMarkers(file , monitor);
	}
	
	public void findProblems(Diagnostic diagnostics)
	{
		for(Diagnostic childDiagnostic : diagnostics.getChildren())
		{
			if(childDiagnostic instanceof DataModelDiagnostic)
			{
				if(childDiagnostic.getSource()==null)
				{
					for(Diagnostic child : childDiagnostic.getChildren())
					{
						if(child instanceof DataModelDiagnostic)
							classifyDiagnostic((DataModelDiagnostic)child);
					}
				}
				else 
				{
						classifyDiagnostic((DataModelDiagnostic)childDiagnostic);
				}
			}
		}
	}

	private void classifyDiagnostic(DataModelDiagnostic dataModelDiagnostic)
	{
		if(dataModelDiagnostic.getData().isEmpty())
			return;
		
		Object element = dataModelDiagnostic.getData().get(0);
		
		switch(dataModelDiagnostic.getSeverity())
		{
			case Diagnostic.ERROR:
				if(dataModelDiagnostic.getValidationCode().indexOf(DataModelerValidatorDescriptor.CODE_VALUE_MODEL)!=-1)
					_basicErrors.add(new DataModelerProblems(DataModelDiagnostic.ERROR , dataModelDiagnostic.getMessage(),dataModelDiagnostic.getDBDepends(), element));
				else if(dataModelDiagnostic.getValidationCode().indexOf(DataModelerValidatorDescriptor.CODE_VALUE_NORM)!=-1)
					_regulationErrors.add(new DataModelerProblems(DataModelDiagnostic.ERROR , dataModelDiagnostic.getMessage(),dataModelDiagnostic.getDBDepends(), element));
				break;
			case Diagnostic.WARNING:
				if(dataModelDiagnostic.getValidationCode().indexOf(DataModelerValidatorDescriptor.CODE_VALUE_MODEL)!=-1)
					_basicWarnings.add(new DataModelerProblems(DataModelDiagnostic.WARNING ,dataModelDiagnostic.getMessage(),dataModelDiagnostic.getDBDepends(), element));
				else if(dataModelDiagnostic.getValidationCode().indexOf(DataModelerValidatorDescriptor.CODE_VALUE_NORM)!=-1)
					_regulationWarnings.add(new DataModelerProblems(DataModelDiagnostic.WARNING ,dataModelDiagnostic.getMessage(),dataModelDiagnostic.getDBDepends(), element));
				break;
		}
	}

	@Override
	public boolean updateSelection(IStructuredSelection selection) {
		
		// la acción sólo se habilita para proyectos
		if(selection instanceof ITreeSelection)
		{
			ITreeSelection treeSelection = (ITreeSelection)selection;
			if(treeSelection.size()>1)
				return false;
			
			Object element = treeSelection.getFirstElement();
			
			if(element instanceof IAdaptable && ((IAdaptable)element).getAdapter(IProject.class)!=null)
			{
				_eProject = (EProject)((IAdaptable)element).getAdapter(EProject.class);
				_iProject = (IProject)((IAdaptable)element).getAdapter(IProject.class);

				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Valida cada diagrama GMF de la funcionalidad para mostrar los márcadores
	 * de error en la vista de problemas y decorar los elementos de los diagramas
	 */
	private void updateDiagramMarkers(IFile file , IProgressMonitor monitor)
	{
		// Obtenemos todos los diagramas
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		
		try
		{
			Resource resource = editingDomain.getResourceSet().getResource(uri, true);
			
			// Iteramos sobre los elementos del proyecto
			EList<EObject> resourceContents = resource.getContents();
			boolean deleteMarkers = true;
			for(EObject content : resourceContents)
			{
				if(content instanceof Diagram)
				{
					final Diagram diagram = (Diagram)content;
					final boolean fdeleteMarkers = deleteMarkers;
					Display.getDefault().syncExec(new Runnable() {

						@Override
						public void run() {
							DMValidateAction.runNonUIValidation((View)diagram , fdeleteMarkers);
						}
						});
					deleteMarkers = false;

				}
				updateMonitor(monitor);
			}
		}
		catch (Exception e) 
		{
			UtilsDataModelerUI.log(e , "ModelValidateActionDelegate.updateDiagramMarkers(): "+e);
		}
	}
		
	private Map<String, Collection<DataModelerProblems>> collectErrAndWarn()
	{
		// Usamos un LinkedHashMap para que después recuperemos los
		// elementos en el mismo orden en el que los introducimos
		Map<String, Collection<DataModelerProblems>> errAndWarnMap = new LinkedHashMap<String, Collection<DataModelerProblems>>();
		
		_basicErrors.addAll(_basicWarnings);
		if(!_basicErrors.isEmpty())
			errAndWarnMap.put(Messages.bind("ModelValidateActionDelegate.basics.validations"), _basicErrors);
		
		_regulationErrors.addAll(_regulationWarnings);
		if(!_regulationErrors.isEmpty())
			errAndWarnMap.put(Messages.bind("ModelValidateActionDelegate.regulation.validations"), _regulationErrors);
			
		return errAndWarnMap;
	}		

	private boolean updateMonitor(IProgressMonitor monitor)
	{
		monitor.worked(1);
		if(monitor.isCanceled())
			return false;
		
		return true;
	}	
	
	public void setProject(EProject project)
	{
		_eProject = project;
	}
	
	public Collection<Object> getProblems()
	{
		Collection<Object> problems = new ArrayList<Object>();
		
		for(DataModelerProblems dmProblem: _basicErrors)
		{
			problems.add(dmProblem);
		}
		for(DataModelerProblems dmProblem: _basicWarnings)
		{
			problems.add(dmProblem);
		}
		for(DataModelerProblems dmProblem: _regulationErrors)
		{
			problems.add(dmProblem);
		}
		for(DataModelerProblems dmProblem: _regulationWarnings)
		{
			problems.add(dmProblem);
		}
		
		return problems;
	}
	private void initializeErrors()
	{
		_basicErrors = new ArrayList<DataModelerProblems>();
		_basicWarnings = new ArrayList<DataModelerProblems>();

		_regulationErrors = new ArrayList<DataModelerProblems>();
		_regulationWarnings = new ArrayList<DataModelerProblems>();
	}
}
