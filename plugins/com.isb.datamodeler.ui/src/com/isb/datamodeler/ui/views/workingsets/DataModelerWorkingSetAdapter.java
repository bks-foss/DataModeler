package com.isb.datamodeler.ui.views.workingsets;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetElementAdapter;

/**
 * This class is called when a new Project is added to an existing Working Set
 * @author xIS05655
 *
 */
public class DataModelerWorkingSetAdapter implements IWorkingSetElementAdapter {

	@Override
	/**
	 * We only allow IProjects on Working Sets, so no need of adapt.
	 */
	public IAdaptable[] adaptElements(IWorkingSet ws, IAdaptable[] elements) {
		return elements;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
