package com.isb.datamodeler.compare.ui;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.ui.viewer.structure.ModelStructureContentProvider;
import org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewer;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.isb.datamodeler.compare.core.DataModelerCompare;
import com.isb.datamodeler.compare.core.DataModelerStructureCompareProvider;


public class DataModelerStructureMergeViewer extends ModelStructureMergeViewer {

	public DataModelerStructureMergeViewer(Composite parent,
			CompareConfiguration compareConfiguration) {
		super(parent, compareConfiguration);		
	}
	
	public void refreshContent (Object newInput) {
		inputChanged (newInput, getInput());
	}
	
	@Override
	protected void inputChanged(Object input, Object oldInput) {		
		final TreePath[] expandedPaths = getExpandedTreePaths();

		preservingSelection(new Runnable() {
			public void run() {
	            Control tree = getControl();
	            tree.setRedraw(false);
	            try {
	                removeAll(tree);
	                tree.setData(getRoot());
	                internalInitializeTree(tree);
	            } finally {
	                tree.setRedraw(true);
	            }
			}
		});
		if (input != null) {
			if (!(input instanceof ComparisonSnapshot) && input != oldInput) {
				DataModelerCompare comparator = new DataModelerCompare(configuration);
				comparator.loadResources((ICompareInput) input);
				ComparisonResourceSnapshot snapshot = comparator.compare(false,false);
				if (snapshot == null)
					return;
				Object match = null;
				// check whether a resource or resource set comparison was performed
				//if (snapshot instanceof ComparisonResourceSnapshot) {
					match = ((ComparisonResourceSnapshot)snapshot).getMatch();
				//} else if (snapshot != null) {					
				//	match = ((ComparisonResourceSetSnapshot)snapshot).getMatchResourceSet();
				//}
				if (match != null) {
					setInput(snapshot);
				} else {
					setInput(null);
				}
			}
			updateToolItems();

			setExpandedTreePaths(expandedPaths);
		} else {
			hideStructurePane();
		}
		
		//super.inputChanged(null, oldInput); 
		/// PONER INPUT A NULL DESHABILITA LA VENTANA DE STRUCTURE DE LAS DIFERENCIAS				
		
	}
	
	 
	private void hideStructurePane() {
		// Si queremos que se oculte el panel estructural cuando llegue null habría que ponerlo... false
		//getControl().getParent().getParent().setVisible(false);
		getControl().getParent().getParent().setVisible(true);
	}
	
	/**
	 * Importante que pase por nuestro provider ya que llama a nuestro
	 * motor de macheo y de diferencias que contiene toda lógica de datamodeler 
	 * de otro modo llama al comparador por defecto de EMF Compare.
	 * 
	 * Este método es el que da sentido a la extensión del modelStructureMergeViewer...
	 */
	@Override
	protected ModelStructureContentProvider createContentProvider(
			CompareConfiguration compareConfiguration) {
		
		return new DataModelerStructureCompareProvider(compareConfiguration);
	}
	
	

}
