package com.isb.datamodeler.compare.ui;

import org.eclipse.emf.compare.ui.viewer.content.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.content.part.IModelContentMergeViewerTab;
import org.eclipse.emf.compare.ui.viewer.content.part.ParameterizedContentMergeTabFolder;
import org.eclipse.swt.widgets.Composite;

public class DataModelerContentMergeTabFolder extends
		ParameterizedContentMergeTabFolder {	

	public DataModelerContentMergeTabFolder(ModelContentMergeViewer viewer,
			Composite composite, int side) {
		super(viewer, composite, side);		
	}
	
	@Override
	protected IModelContentMergeViewerTab createModelContentMergeDiffTab(
			Composite parent) {		
		return  new DataModelerContentMergeDiffTab(parent, partSide, this);
	}
	
	public ModelContentMergeViewer getParentViewer () {
		return parentViewer;
	}
}
