package com.isb.datamodeler.internal.ui.dialogs.search;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.ResourceWorkingSetFilter;

import com.isb.datamodeler.ui.project.EProject;

public class ShowTableInDiagramWorkingSetFilter extends
		ResourceWorkingSetFilter {
	
    private IAdaptable[] cachedWorkingSet = null;

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		EProject eProject = null;

        if (getWorkingSet() == null || (getWorkingSet().isAggregateWorkingSet() &&
        		getWorkingSet().isEmpty())) {
            return true;
        }
        
        if (element instanceof EObject) {
        	eProject = getEProject((EObject) element);
        } 
        
        if (eProject != null && eProject instanceof EProject) {
            return isEnclosed(eProject);
        }
        return true;
	}
	
    private EProject getEProject(EObject element) {
		
    	if(element instanceof EProject)
			return (EProject)element;
		
    	if(element!=null && element.eContainer()!=null)
    		return getEProject(element.eContainer());
		
		return null;
	}

	private boolean isEnclosed(EProject element) {

    	IAdaptable[] workingSetElements = cachedWorkingSet;

        // working set elements won't be cached if select is called
        // directly, outside filter. fixes bug 14500.
        if (workingSetElements == null) {
			workingSetElements = getWorkingSet().getElements();
		}

        for (int i = 0; i < workingSetElements.length; i++) {
        	workingSetElements[i].getAdapter(EProject.class);
        	if(element.equals(workingSetElements[i].getAdapter(EProject.class)))
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
