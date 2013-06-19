package com.isb.datamodeler.compare.ui;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.compare.contentmergeviewer.TextMergeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Visor de diferencias de los contenidos.
 * 
 * @author Alfonso
 *
 */
public class DataModelerViewerContentCreator implements IViewerCreator {

	@Override
	public Viewer createViewer(Composite parent, CompareConfiguration config) {
		config.setLeftEditable(true);
		return new TextMergeViewer(parent,config);		
		//return new ModelContentMergeViewer(parent, config);
	}

}
