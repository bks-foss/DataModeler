package com.isb.datamodeler.ui.diagram.listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.progress.UIJob;

import com.isb.datamodeler.datatypes.EDatatypesPackage;
import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditor;
import com.isb.datamodeler.diagram.util.DataModelerDiagramUtils;
import com.isb.datamodeler.diagram.validation.DiagramNameValidator;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.model.core.validation.db.AbstractDataModelerDBValidator;
import com.isb.datamodeler.model.core.validation.db.DataModelerValidationDBManager;
import com.isb.datamodeler.model.validation.UtilsValidations;
import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerDiagramEditorSaver extends ResourceSetListenerImpl {

	private static Map<DatamodelerDiagramEditor, Object> _editParts = new WeakHashMap<DatamodelerDiagramEditor, Object>();
	private boolean _changing = false;
	private static Object _theObject;
	
	public static String SAVE_EDITOR_JOB_FAMILY = "SAVE_EDITOR"; //$NON-NLS-1$
	public static String SAVE_RESOURCE_JOB_FAMILY = "SAVE_RESOURCE"; //$NON-NLS-1$
	
	public DataModelerDiagramEditorSaver()
	{
		super();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#isAggregatePrecommitListener()
	 */
	@Override
	public boolean isAggregatePrecommitListener() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#resourceSetChanged(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event)
	{
		if(_changing)
		{
			_changing = false;
			saveEditors();
			
			//Cuando no hay editores sucios (Porque estamos editando desde las propiedades del navegador), guardamos el recurso:
			if(!hasDirtyEditors())
				saveEditingDomainResource();
			
			//TODO: COMENTADO PARA QUE FUNCIONE EL UNDO ; VER QUE PASA CON LAS MODIFICACIONES DESDE EL NAVEGADOR
			//saveEditingDomainResource();
		}else if(event.getTransaction()!=null &&
				Boolean.TRUE.equals(event.getTransaction().getOptions().get(Transaction.OPTION_IS_UNDO_REDO_TRANSACTION)))
		{
			//si lo que estamos haciendo es un undo/redo sobre el diagrama, tambien los guardamos.
			saveEditors();

			saveEditingDomainResource();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.ResourceSetListenerImpl#transactionAboutToCommit(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event)
			throws RollbackException 
	{
		for(Notification notification:event.getNotifications())
		{
			if(notification.getFeature() instanceof EStructuralFeature )
			{
				Object object = notification.getNotifier();
				// Si estamos ante un SET del nombre, entonces validamos.
				// Si el nombre es invalido mostramos mensaje y lanzamos RollBackException.
				if(notification.getEventType()== Notification.SET)
				{		
					EStructuralFeature feature = (EStructuralFeature)notification.getFeature();
					if(feature.getEType() instanceof EDataType && getFeatureListToValidate().contains(feature.getName()))
					{
						if(object instanceof ESQLObject)
						{
							// Obtenemos el proyecto para obtener el dataBase
							Resource resource = ((EObject)object).eResource();
							if(resource != null)
							{
								String path = resource.getURI().toPlatformString(true);
								
								IResource workspaceResource = ResourcesPlugin.getWorkspace().getRoot()
										.findMember(new Path(path));
								
								if (workspaceResource instanceof IFile)
								{	
//									EProject eProject = DataModelerDiagramUtils.findEProject((IFile)workspaceResource);
//									EDatabase dataBase = eProject.getDatabase();
								
									
									Object newValue = notification.getNewValue();
									
//									DiagnosticChain diagnosticChain = dataBase.validateEDataType((EDataType)feature.getEType(), (ESQLObject)object , feature.getName(),newValue);
									List<AbstractDataModelerDBValidator> validators = 
										DataModelerValidationDBManager.getInstance().getValidatorsForEObjectAsList(
												UtilsValidations.getDataBaseId((ESQLObject)object), (ESQLObject)object);
									for(AbstractDataModelerDBValidator validator:validators)
									{
										BasicDiagnostic basicDiagnostic = validator.validate((ESQLObject)object, feature.getName(), newValue);
										if(basicDiagnostic!= null && basicDiagnostic.getSeverity() == Diagnostic.ERROR)
										{
											DataModelerDiagramUtils.showMessage(basicDiagnostic.getMessage());
											throw new RollbackException(Status.CANCEL_STATUS);
											
										}
									}
								}
							}
						}
						else if(object instanceof Diagram)
						{
							Diagram diagram = (Diagram)object;
							DiagramNameValidator diagramValidator = new DiagramNameValidator();
							IStatus status = diagramValidator.validate(diagram.getName(), "name");
							if(status!=null && !status.isOK())
							{
								DataModelerDiagramUtils.showMessage(status.getMessage());
								
								throw new RollbackException(Status.CANCEL_STATUS);
							
							}
						}
						// TODO: Validar el nombre del proyecto cuando tengamos editor 
					}
				}

			}
		}

		_changing  = true;

		return null;
	}

	public static void addEditor(DatamodelerDiagramEditor editPart, boolean saveNow) {
		_editParts.put(editPart, _theObject);

		if (saveNow)
			saveEditors();
	}

	private static void saveEditors()
	{
		for (DatamodelerDiagramEditor diagramDocumentEditor : _editParts.keySet())
		{
			if (diagramDocumentEditor.isDirty())
			{
				new SaveEditorWorkspaceJob(diagramDocumentEditor).schedule();
				// Bugzilla 21430: No se actualiza el diagrama bien al deshacer una relación
				// No es una buena solución.
				refreshColumnsEditPart(diagramDocumentEditor.getDiagramEditPart().getChildren());
				
			}
		}
	}
	
	private static boolean hasDirtyEditors()
	{
		for (DatamodelerDiagramEditor diagramDocumentEditor : _editParts.keySet())
			if (diagramDocumentEditor.isDirty())
				return true;

		return false;
	}
	
	private void saveEditingDomainResource()
	{
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		for (Resource resource : editingDomain.getResourceSet().getResources())
		{
			// TODO -- coger la extensión de otra parte
			if(resource.getURI().lastSegment().endsWith("dm"))
			{
				if(resource.isModified())
				{
					new SaveResourceWorkspaceJob(resource).schedule();
				}
			}
		}
	}
		
	private static class SaveEditorWorkspaceJob extends UIJob
	{
		private DatamodelerDiagramEditor _editor;

		public SaveEditorWorkspaceJob(DatamodelerDiagramEditor part)
		{
			super("Saving " + part.getTitle());
			_editor = part;
			IWorkspace wks = ResourcesPlugin.getWorkspace();
			IResourceRuleFactory resourceRuleFactory = wks.getRuleFactory();
			setRule(resourceRuleFactory.modifyRule(wks.getRoot()));
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) 
		{
			_editor.doSave(monitor);
			return Status.OK_STATUS;
		}

		
		@Override
		public boolean belongsTo(Object family) 
		{
			if(family instanceof String)
			{
				return ((String)family).equals(SAVE_EDITOR_JOB_FAMILY);
			}
			
			return super.belongsTo(family);
		}
	}
	
	private static class SaveResourceWorkspaceJob extends UIJob
	{
		private Resource _resource;

		public SaveResourceWorkspaceJob(Resource resource)
		{
			super("Saving " + resource.getURI().lastSegment());
			_resource = resource;
			IWorkspace wks = ResourcesPlugin.getWorkspace();
			IResourceRuleFactory resourceRuleFactory = wks.getRuleFactory();
			setRule(resourceRuleFactory.modifyRule(wks.getRoot()));
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) 
		{
			try {
				_resource.save(null);
				
//				_resource.unload();
//				_resource.load(null);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Status.OK_STATUS;
		}
 		
		@Override
		public boolean belongsTo(Object family) 
		{
			if(family instanceof String)
			{
				return ((String)family).equals(SAVE_RESOURCE_JOB_FAMILY);
			}
			
			return super.belongsTo(family);
		}
 	}
	private static List<String> getFeatureListToValidate()
	{
		List<String> featuresToValidate = null;
		if(featuresToValidate == null)
		{
			featuresToValidate = new ArrayList<String>();
			featuresToValidate.add("name");
			featuresToValidate.add("baseRole");
			featuresToValidate.add("parentRole");
			featuresToValidate.add(EDatatypesPackage.eINSTANCE.getExactNumericDataType_Scale().getName());
			featuresToValidate.add(EDatatypesPackage.eINSTANCE.getBinaryStringDataType_Length().getName());
			featuresToValidate.add(EDatatypesPackage.eINSTANCE.getNumericalDataType_Precision().getName());
			
			
		}
		return featuresToValidate;
	}
	private static void refreshColumnsEditPart(List<Object> objects)
	{
		for(Object obj:objects)
		{
			if(obj instanceof ColumnEditPart)
			{
				((ColumnEditPart)obj).refresh();
			}
			else if(obj instanceof AbstractEditPart)
			{
				refreshColumnsEditPart(((AbstractEditPart)obj).getChildren());
			}
		}
	}
}