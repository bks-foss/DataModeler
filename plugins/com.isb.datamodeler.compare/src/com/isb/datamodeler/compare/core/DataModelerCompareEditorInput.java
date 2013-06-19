package com.isb.datamodeler.compare.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.Splitter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.ui.editor.ModelCompareEditorInput;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorReference;

import com.isb.datamodeler.compare.Activator;
import com.isb.datamodeler.compare.merge.FinalizeMergeCommand;
import com.isb.datamodeler.compare.messages.Messages;
import com.isb.datamodeler.compare.ui.DataModelCompareInput;
import com.isb.datamodeler.compare.ui.DataModelerContentMergeViewer;
import com.isb.datamodeler.compare.ui.DataModelerStructureMergeViewer;
import com.isb.datamodeler.compare.ui.OptionsMessagesDialog;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.ui.project.EProject;

/**
 *  In the case that we need to change properties about compare editors
 *  we can override this class. i.e In this class you can change save properties
 *  in editors. Important. CVS compare don't use this class to open editors.
 *  Match and Diff behavior is inside contentProvider and structureProvider. 
 *  
 * @author Alfonso.
 *
 */
public class DataModelerCompareEditorInput extends ModelCompareEditorInput {		
	
	protected IPropertyChangeListener fDirtyStateListener;	
	private boolean _isRemote;
	private final static int SAVE_MERGES_AND_DELETE_EXTERNAL_CHANGES = 1;
	private final static int UPDATE_EXTERNAL_RESOURCE_AND_DELETE_MERGES = 2;
	private final static int CANCEL_ACTIONS_IN_COLISIONS = 3;
	
	/**
	 * Constructor para visualizar las diferencias en los wizard.
	 * @param snapshot
	 */
	public DataModelerCompareEditorInput(ComparisonSnapshot snapshot, boolean isRemote) {
		super(snapshot);				
		
		fDirtyStateListener= new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String propertyName= e.getProperty();
				if (CompareEditorInput.DIRTY_STATE.equals(propertyName)) {
					boolean changed= false;
					Object newValue= e.getNewValue();
					if (newValue instanceof Boolean)
						changed= ((Boolean)newValue).booleanValue();
						setDirty(changed);
				}
			}
		};
		_isRemote = isRemote;
		
		if (_isRemote)
			getCompareConfiguration().setRightEditable(false);
	}
	
	@Override
	protected void handleDispose() {		
		super.handleDispose();			
	}
	
	public ModelCompareInput getPreparedInput()
	{
		return preparedInput;
	}
	
	/**
	 * Creates the ModelCompareInput for this editor input.
	 * 
	 * @param snap
	 *            Snapshot of the current comparison.
	 * @return The prepared ModelCompareInput for this editor input.
	 */
	protected ModelCompareInput createModelCompareInput(final ComparisonSnapshot snap) {
		if (snap instanceof ComparisonResourceSetSnapshot) {
			return new DataModelCompareInput(((ComparisonResourceSetSnapshot)snap).getMatchResourceSet(),
					((ComparisonResourceSetSnapshot)snap).getDiffResourceSet());
		}
		return new DataModelCompareInput(((ComparisonResourceSnapshot)snap).getMatch(),
				((ComparisonResourceSnapshot)snap).getDiff());
	}
	
	
	/**
	 * Definimos nuestro propio editor de StructureMergeViewer...
	 */
	@Override
	public Control createOutlineContents(Composite parent, int direction) {
		final Splitter splitter = new Splitter( parent, direction);

		final CompareViewerPane pane = new CompareViewerPane(splitter, SWT.NONE);

		structureMergeViewer = new DataModelerStructureMergeViewer(pane, getCompareConfiguration());
		pane.setContent(structureMergeViewer.getTree());
		
		//SI LO PONGO A NULL NO SE MUESTRA LA VISTA DE ESTRUCTURA...
		structureMergeViewer.setInput(preparedInput);

		return splitter;
	}
	
	public DataModelerStructureMergeViewer getStructure() {
		return (DataModelerStructureMergeViewer) structureMergeViewer;
	}
	
	public DataModelerContentMergeViewer getContent() {
		return (DataModelerContentMergeViewer) contentMergeViewer;
	}
	
 		
	/**
	 * Es necesario sobrescribir este m�todo para que vaya directamente a nuestro contentMergeViewer
	 * Para poder controlar el dispose cuando cierran el editor de comparaci�n.
	 */
	protected ModelContentMergeViewer createMergeViewer(CompareViewerPane pane, CompareConfiguration config) {	
		DataModelerContentMergeViewer view = new DataModelerContentMergeViewer(pane, config);
		view.addPropertyChangeListener(fDirtyStateListener);		
		return view;	
	}
		
	
	@Override
	public void saveChanges(IProgressMonitor monitor) {																	
		
		// Control de sincronizaci�nd de recursos...
		int result = detectResourceColisions (preparedInput.getLeftResource(), preparedInput.getRightResource(), _isRemote, null);
		if (result == CANCEL_ACTIONS_IN_COLISIONS)
			return;
			
		
		// No es capaz de resolver los tipos de datos primitivos en los modelos respectivos.
		ExtendModelUtils.preparePrimitiveDataType((EProject) preparedInput.getLeftResource().getContents().get(0));
		
		if (!_isRemote)
			ExtendModelUtils.preparePrimitiveDataType((EProject) preparedInput.getRightResource().getContents().get(0));
				
		super.saveChanges(monitor);				
		
		// Esto no deber�amos forzarlo, pero si no lo hacemos deja el editor sucio.
		setDirty(false);
		
		// Forzamos el refresco de la vista de navegaci�n lanzando un comando EMF.
		Command cmdLeftFile = new FinalizeMergeCommand(preparedInput.getLeftResource());
		Command cmdRightFile = null; 
		
		if (!_isRemote)
			cmdRightFile = new FinalizeMergeCommand (preparedInput.getRightResource());
		
		TransactionalCommandStack transactionalCommandStack = (TransactionalCommandStack) DataModelerUtils.getDataModelerEditingDomain().getCommandStack();
		try {
			transactionalCommandStack.execute(cmdLeftFile, new HashMap());
			
			if (cmdRightFile != null && !_isRemote)
				transactionalCommandStack.execute(cmdRightFile, new HashMap());
			
		} catch (InterruptedException e) {		
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.editorInput.title"), //$NON-NLS-1$
					Messages.bind("datamodeler.compare.editorInput.error"), e)); //$NON-NLS-1$						
		} catch (RollbackException e) {			
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.editorInput.title"), //$NON-NLS-1$
					Messages.bind("datamodeler.compare.editorInput.error"), e)); //$NON-NLS-1$						
		}						
	}	
	
	/**
	 * Detecta modificaciones con respecto al recurso que se est� comparando.
	 * @param leftCompareResource
	 * @param rightCompareResource
	 * @param isRemote
	 * @param wizard
	 * @return
	 */
	public int detectResourceColisions (Resource leftCompareResource, Resource rightCompareResource, boolean isRemote, DataModelerCompareWizardInput wizard) {
		
		if (this instanceof DataModelerCompareWizardInput)
			return SAVE_MERGES_AND_DELETE_EXTERNAL_CHANGES;
		
		// Detecci�n del recurso original modificado, mediante el timestamp.
		//System.out.println(" Recursos Left--> " + leftCompareResource.getTimeStamp());
		Long leftTimeStampComparison = leftCompareResource.getTimeStamp();
		Long leftTimeStampExternalResource = leftCompareResource.getTimeStamp();
		Long rightTimeStampComparison = null;
		Long rightTimeStampExternalResource = null;		
		
		if (!isRemote) {
			//System.out.println(" Recursos Right--> " + rightCompareResource.getTimeStamp());
			rightTimeStampComparison = preparedInput.getRightResource().getTimeStamp();
			rightTimeStampExternalResource = preparedInput.getRightResource().getTimeStamp();						
		}				
		
		// B�squeda del recurso dentro del editing domain
		EList<Resource> resources = DataModelerUtils.getDataModelerEditingDomain().getResourceSet().getResources();
		for (Resource resource : resources) {
			if (resource.getURI().toString().contains(leftCompareResource.getURI().toString())) {
				//System.out.println(resource.getTimeStamp());
				leftTimeStampExternalResource =  resource.getTimeStamp();				
				
			} else if (!isRemote && resource.getURI().toString().contains(rightCompareResource.getURI().toString())) {
				//System.out.println("Original Resource " + resource.getTimeStamp()); //$NON-NLS-1$
				rightTimeStampExternalResource =  resource.getTimeStamp();				
			}
		}
		
		// Comparamos los timestamp de los recursos, nos detecta que los recursos han cambiado y la foto es diferente...
		if (!leftTimeStampComparison.equals(leftTimeStampExternalResource) || 
				(!isRemote &&(!rightTimeStampComparison.equals(rightTimeStampExternalResource)))) {
		
			OptionsMessagesDialog options =  new OptionsMessagesDialog(
					getWorkbenchPart().getSite().getShell(), Messages.bind("datamodeler.compare.editorInput.synchronized.title"), //$NON-NLS-1$
					Messages.bind("datamodeler.compare.editorInput.synchronized.msg"),  //$NON-NLS-1$
					new String[] {Messages.bind("datamodeler.compare.editorInput.synchronized.option1"),  //$NON-NLS-1$
						Messages.bind("datamodeler.compare.editorInput.synchronized.option2")}, //$NON-NLS-1$
						new int[] {SAVE_MERGES_AND_DELETE_EXTERNAL_CHANGES, UPDATE_EXTERNAL_RESOURCE_AND_DELETE_MERGES}, 
						SAVE_MERGES_AND_DELETE_EXTERNAL_CHANGES);
			
			int result = options.open();
			
			if (result == Window.CANCEL)
				return CANCEL_ACTIONS_IN_COLISIONS;
			
			if (result == Window.OK)
			{			
				int selectedOption = options.getSelectedOption();
			
				// Si la opci�n es recargar el comparador tenemos que volver a cargar los recursos que estamos
				// comparando, cerrar el editor de comparaci�n actual y volver a abrir el editor de comparaci�n.
				if (selectedOption == UPDATE_EXTERNAL_RESOURCE_AND_DELETE_MERGES) {
				
					IEditorReference[] editorsReference = getContainer().getWorkbenchPart().getSite().getPage().getEditorReferences();
					for (IEditorReference iEditorReference : editorsReference) {
						if(iEditorReference.getId().equals("org.eclipse.compare.CompareEditor")) //$NON-NLS-1$
						{	
							try {
								//Date fechaAhora = new Date();
								//System.out.println("****** " + fechaAhora + "COMIENZA LA DESCARGA [LEFT]");
								
								leftCompareResource.unload();
								//System.out.println("****** " + fechaAhora + "TERMINA LA DESCARGA Y COMENZA LA CARGA [LEFT]");
								leftCompareResource.load(null);
								//System.out.println("****** " + fechaAhora + "TERMINA LA CARGA [LEFT]");
								
								if (!isRemote) {
									//System.out.println("****** " + fechaAhora + "COMIENZA LA DESCARGA [RIGHT]");
									rightCompareResource.unload();
									//System.out.println("****** " + fechaAhora + "TERMINA LA DESCARGA Y COMENZA LA CARGA [RIGHT]");
									rightCompareResource.load(null);
									//System.out.println("****** " + fechaAhora + "TERMINA LA CARGA [RIGHT]");
								}
							} catch (IOException e) {								
								Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.editorInput.title"), //$NON-NLS-1$
										Messages.bind("datamodeler.compare.editorInput.error"), e)); //$NON-NLS-1$
							}
							
							iEditorReference.getPage().closeEditor(
									iEditorReference.getEditor(false), false);
							}
						}								
					
					DataModelerCompare compare = new DataModelerCompare();
					
					ComparisonResourceSnapshot snapshot = compare.compare(
							(EProject)leftCompareResource.getContents().get(0), 
							(EProject)rightCompareResource.getContents().get(0));
					
					if (wizard == null) {						
						if (snapshot != null)	
							CompareUI.openCompareEditor(new DataModelerCompareEditorInput(snapshot, isRemote));
						
					} else {
						
						wizard.setSnapShot(snapshot);
						
						// No es editable el editor de comparaci�n del lado derecho.
						getCompareConfiguration().setRightEditable(false);
						
							try {
								wizard.run(new NullProgressMonitor());
							} catch (InterruptedException e) {								
								Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.editorInput.title"), //$NON-NLS-1$
										Messages.bind("datamodeler.compare.editorInput.error"), e)); //$NON-NLS-1$
							} catch (InvocationTargetException e) {								
								Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.editorInput.title"), //$NON-NLS-1$
										Messages.bind("datamodeler.compare.editorInput.error"), e)); //$NON-NLS-1$
							}
							wizard.setContentMergeViewerInput();
							wizard.clearSelection();							
					}
					return UPDATE_EXTERNAL_RESOURCE_AND_DELETE_MERGES;
				}			
			}
		}
		
		return SAVE_MERGES_AND_DELETE_EXTERNAL_CHANGES;
	}
	

	
}
