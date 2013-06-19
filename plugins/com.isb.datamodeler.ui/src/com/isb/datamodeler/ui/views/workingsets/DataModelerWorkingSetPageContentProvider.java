package com.isb.datamodeler.ui.views.workingsets;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.isb.datamodeler.ui.UtilsDataModelerUI;

/**
 * Content provider for the DataModeler Working Set Page
 * @author xIS05655
 *
 */
public class DataModelerWorkingSetPageContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return UtilsDataModelerUI.findDataModelerProjects().toArray();
	}
	
	@Override
	public Object[] getChildren(Object parentElement) {
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return false;
	}


}
