package com.isb.datamodeler.compare.core;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;

import com.isb.datamodeler.compare.ui.DataModelerContentMergeViewer;

/**
 * Para el caso de los wizard  debemos modificar el snapshot interno del ModelCompareEditorInput
 * como en esa clase es final. Por tanto es necesario refrescar el editor, 
 * ya creado y volver a realizar la comparaci�n. 
 * 
 * @author Alfonso
 *
 */ 

public class DataModelerCompareWizardInput extends
		DataModelerCompareEditorInput {

	private  ICompareInputChangeListener _inputListener;	
	private ComparisonSnapshot _dataModelerSnapShot;
	private DataModelerContentMergeViewer _contentMergeViewer = null;	
	
	public DataModelerCompareWizardInput(ComparisonSnapshot snapshot) {	
		super(snapshot, true);
		
		getCompareConfiguration().setProperty(CompareEditor.CONFIRM_SAVE_PROPERTY, false);
		
		_inputListener = new ICompareInputChangeListener() {
			public void compareInputChanged(ICompareInput source) {				
				contentMergeViewer.setInput(source);				
				setDirty(true);
			}
		};				 
	}
	
	/**
	 * Modificaci�n de la foto con el modelo de macheo y las diferencias encontradas.
	 * @param modified
	 */
	public void setSnapShot (ComparisonSnapshot modified) {
		_dataModelerSnapShot = modified;
	}
	
	/**
	 * Necesario para actualizar el contenido del view del comparador.
	 */
	public void setContentMergeViewerInput() {
		contentMergeViewer.setInput(preparedInput);
		getStructure().setInput(preparedInput);
		
	}
			
	@Override
	protected Object prepareInput(IProgressMonitor monitor) {		
		preparedInput = createModelCompareInput(_dataModelerSnapShot);
		preparedInput.addCompareInputChangeListener(_inputListener);
		return preparedInput;
	}
	
	@Override
	protected ModelContentMergeViewer createMergeViewer(CompareViewerPane pane,
			CompareConfiguration config) {		
		 _contentMergeViewer = new DataModelerContentMergeViewer(pane, config);
		 return _contentMergeViewer;
	}

	
	public void clearSelection () {
		if (_contentMergeViewer != null)
			_contentMergeViewer.clearSelection();
	}
	
	public void updateToolItems (boolean enabled) {
		_contentMergeViewer.updateToolItems(enabled);		
	}

}
