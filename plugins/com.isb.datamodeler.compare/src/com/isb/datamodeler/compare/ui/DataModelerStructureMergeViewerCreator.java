package com.isb.datamodeler.compare.ui;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.emf.compare.ui.viewer.structure.ModelStructureMergeViewerCreator;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Visor de diferencias estructurales.
 * 
 * @author Alfonso
 *
 */
public class DataModelerStructureMergeViewerCreator extends
		ModelStructureMergeViewerCreator {

	@Override
	public Viewer createViewer(Composite parent, CompareConfiguration config) {
		config.setLeftEditable(true);				
		// Bugzilla 22331. Editor estructural en la vista de sincro.
		//return new DataModelerStructureMergeViewer(parent, config);
		return null;
	}
}
