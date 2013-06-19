package com.isb.datamodeler.compare.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.isb.datamodeler.compare.Activator;
import com.isb.datamodeler.compare.messages.Messages;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.project.EProject;

/**
 * Crea un ModelerCompareEditorInput para añadir el editor de comparación, en los wizardPage
 * el getCompareEditorControl() nos devuelve un @link Control que habrá que pasar al wizardPage
 * dentro del getControl() y añadirlo mediante setControl. Si queremos visualizar las diferencias
 * a partir de dos proyectos de DataModeler tenemos el método refresh().
 *
 * @author Alfonso
 *
 */
public class DataModelerCompareWizardEditor extends Observable {
	
	private DataModelerCompareWizardInput _compareEditorInput = null;	
	private EProject _leftProject;
	private IPropertyChangeListener _wizardDirtyStateListener = null;
	
	public DataModelerCompareWizardEditor() {	
		 _compareEditorInput = new DataModelerCompareWizardInput(null);			 		

		 _wizardDirtyStateListener= new IPropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					String propertyName= e.getProperty();
					if (CompareEditorInput.DIRTY_STATE.equals(propertyName)) {
						boolean changed= false;
						Object newValue= e.getNewValue();
						if (newValue instanceof Boolean)
							changed= ((Boolean)newValue).booleanValue();
						System.out.println(changed);
						if (changed) {
							setChanged();
							notifyObservers();
						}
					}
				}
			};
	}
	
	/**
	 * Nos devuelve un Control donde esta toda la información visual
	 * del editor de comparación.
	 * 
	 * @param parent
	 * @return
	 */
	public Control getCompareEditorControl(Composite parent) {
		return _compareEditorInput.createContents(parent);
	}
	
	
	
	/**
	 * Es el encargado de lanzar la comparación EMF y actualizar la vista
	 * para mostrar las diferencias.
	 * @param leftProject
	 * @param rightProject
	 */
	public void refresh(EProject leftProject, EProject rightProject) 
	{		
		
		
		//Llamamos al comparador
		DataModelerCompare compare = new DataModelerCompare();						
		
		//Preparamos los EProject para el EMFCompare
		boolean refresh = true;
		if ((_leftProject == null) ||(_leftProject != null && !_leftProject.equals(leftProject)))
			refresh = false;
		
		if (!refresh)	
			compare.prepareLeftWizardCompare(refresh, leftProject);
		else
			compare.prepareLeftWizardCompare(refresh, (EProject) _compareEditorInput.getPreparedInput().getLeftResource().getContents().get(0));
		compare.prepareRightVirtualCompare(rightProject);
		
		prepareFilter(rightProject);
		ComparisonResourceSnapshot snapshot = compare.compare(refresh,false);
		
		if (snapshot.getDiff().getSubchanges()<=0) {
			_compareEditorInput.getCompareConfiguration().setLeftEditable(false);
			_compareEditorInput.updateToolItems(false);
		}
		else {
			_compareEditorInput.getCompareConfiguration().setLeftEditable(true);
			_compareEditorInput.updateToolItems(true);			
		}
		
		_compareEditorInput.setSnapShot(snapshot);		
		
		// No es editable el editor de comparaciï¿½n del lado derecho.
		_compareEditorInput.getCompareConfiguration().setRightEditable(false);

		try {
			_compareEditorInput.run(new NullProgressMonitor());
			
			// Se quita este if porque no actualizarï¿½a si hay cambiso en los proyectos importados
			// del lado derecho. El CompareEditor.CONFIRM_SAVE_PROPERTY, false) se encuentra dentro
			// del compareWizardEditorInput
			//if ((_leftProject == null) ||(_leftProject != null && !_leftProject.equals(leftProject)))
			_compareEditorInput.setContentMergeViewerInput();
			_compareEditorInput.clearSelection();
			
			_compareEditorInput.getStructure().expandToLevel(2);//expandAll();			
			_compareEditorInput.getContent().navigate(true);			
			if (!refresh)
				_compareEditorInput.getContent().addPropertyChangeListener(_wizardDirtyStateListener);
						
			_leftProject = leftProject;

		} catch (InterruptedException e) {				
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.editorInput.wizard.title"), //$NON-NLS-1$
					Messages.bind("datamodeler.compare.editorInput.wizard.error"), e)); //$NON-NLS-1$			
			
		} catch (InvocationTargetException e) {				
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Messages.bind("datamodeler.compare.editorInput.wizard.title"), //$NON-NLS-1$
					Messages.bind("datamodeler.compare.editorInput.wizard.error"), e)); //$NON-NLS-1$			
			
		}					
	}
	
	private void prepareFilter(EProject rightProject) {
		DataModelerCompareFilterContent.getInstance().activeFilter();
		EList<ESchema> schema= rightProject.getSchemas();
		for (ESchema eSchema : schema) {
			DataModelerCompareFilterContent.getInstance().addSchema(eSchema.getName(), eSchema.getCapability());
			DataModelerCompareFilterContent.getInstance().addTables(eSchema.getTables());			
		}							
	}

	/**
	 * Guarda los cambios del merge y realiza el refresco
	 */
	public void saveCompareResources() {
		_compareEditorInput.saveChanges(new NullProgressMonitor());					
	}
	
}
