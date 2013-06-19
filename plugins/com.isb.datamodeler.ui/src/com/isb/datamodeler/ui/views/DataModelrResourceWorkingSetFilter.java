package com.isb.datamodeler.ui.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.ResourceWorkingSetFilter;

public class DataModelrResourceWorkingSetFilter extends
		ResourceWorkingSetFilter {
	
    private IAdaptable[] cachedWorkingSet = null;

	public DataModelrResourceWorkingSetFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		IAdaptable item = null;

        if (getWorkingSet() == null || (getWorkingSet().isAggregateWorkingSet() &&
        		getWorkingSet().isEmpty())) {
            return true;
        }
        
        if (element instanceof IAdaptable) {
        	item = (IAdaptable) element;
        } 
        
        if (item != null && item.getAdapter(IProject.class)!=null) {
            return isEnclosed((IProject)item.getAdapter(IProject.class));
        }
        return true;
	}
	
    private boolean isEnclosed(IResource element) {

    	IAdaptable[] workingSetElements = cachedWorkingSet;

        // working set elements won't be cached if select is called
        // directly, outside filter. fixes bug 14500.
        if (workingSetElements == null) {
			workingSetElements = getWorkingSet().getElements();
		}

        for (int i = 0; i < workingSetElements.length; i++) {
        	if(element.equals(workingSetElements[i]))
        		return true;
        }
        return false;
    }
    
    /**
     * Filters out elements that are neither a parent nor a child of 
     * a working set element.
     * 
     * @see ViewerFilter#filter(Viewer, Object, Object[])
     */
    public Object[] filter(Viewer viewer, Object parent, Object[] elements) {
        Object[] result = null;
        if (getWorkingSet() != null) {
			cachedWorkingSet = getWorkingSet().getElements();
		}
        try {
            result = super.filter(viewer, parent, elements);
        } finally {
            cachedWorkingSet = null;
        }
        return result;
    }

	
}
