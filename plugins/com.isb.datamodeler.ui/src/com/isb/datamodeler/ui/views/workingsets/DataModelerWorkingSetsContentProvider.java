package com.isb.datamodeler.ui.views.workingsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IAggregateWorkingSet;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.IExtensionStateModel;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

import com.isb.datamodeler.internal.ui.views.DatamodelerDomainNavigatorContentProvider;
import com.isb.datamodeler.ui.project.EProject;

/**
 * Content provider used in DataModelerView when the root mode is Working Sets
 * @author xIS05655
 *
 */
public class DataModelerWorkingSetsContentProvider extends DatamodelerDomainNavigatorContentProvider {
	
	public DataModelerWorkingSetsContentProvider() {
		super();
	}

	/**
	 * The extension id for the WorkingSet extension.
	 */
	public static final String EXTENSION_ID = "com.isb.datamodeler.ui.workingSets"; //$NON-NLS-1$

	/**
	 * A key used by the Extension State Model to keep track of whether top level Working Sets or
	 * Projects should be shown in the viewer.
	 */
	public static final String SHOW_TOP_LEVEL_WORKING_SETS = EXTENSION_ID + ".showTopLevelWorkingSets"; //$NON-NLS-1$

	private WorkingSetHelper helper;
	private IAggregateWorkingSet workingSetRoot;
	private IExtensionStateModel extensionStateModel;
	private CommonNavigator projectExplorer;

	
	private IPropertyChangeListener rootModeListener = new IPropertyChangeListener() {
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent event) {
			if(SHOW_TOP_LEVEL_WORKING_SETS.equals(event.getProperty())) {
				updateRootMode();
			}
		}

	};
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.navigator.ICommonContentProvider#init(org.eclipse.ui.navigator.ICommonContentExtensionSite)
	 */
	public void init(ICommonContentExtensionSite aConfig) {

		extensionStateModel = aConfig.getExtensionStateModel();
		extensionStateModel.addPropertyChangeListener(rootModeListener);
		updateRootMode();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.navigator.IMementoAware#restoreState(org.eclipse.ui.IMemento)
	 */
	public void restoreState(IMemento aMemento) {
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.navigator.IMementoAware#saveState(org.eclipse.ui.IMemento)
	 */
	public void saveState(IMemento aMemento) {
		
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IWorkingSet) {
			IWorkingSet workingSet = (IWorkingSet) parentElement;
			if (workingSet.isAggregateWorkingSet() && projectExplorer != null) {
				switch (projectExplorer.getRootMode()) {
					case ProjectExplorer.WORKING_SETS :
						return ((IAggregateWorkingSet) workingSet).getComponents();
					case ProjectExplorer.PROJECTS :
						return getWorkingSetElements(workingSet);
				}
			}
			return getWorkingSetElements(workingSet);
		}
		return super.getChildren(parentElement);
	}

	private IAdaptable[] getWorkingSetElements(IWorkingSet workingSet) {
		
		List<EProject> wsProjects = new ArrayList<EProject>(workingSet.getElements().length);
		
		for(IAdaptable element: workingSet.getElements())
		{
			EProject eProject = (EProject)element.getAdapter(EProject.class); 
			if(eProject!=null)
				wsProjects.add(eProject);
		}
			
		
		return wrapEObjects(wsProjects.toArray(), workingSet);
	}

	public Object getParent(Object element) {
		Object parent = null;
		if (helper != null)
			parent = helper.getParent(element);
		
		if(parent!=null)
			return parent;
		
		return super.getParent(element);
	}

	public boolean hasChildren(Object element) {
		return true;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {
		helper = null;
		extensionStateModel.removePropertyChangeListener(rootModeListener);
		
		super.dispose();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
		super.inputChanged(viewer, oldInput, newInput);
		
		if (newInput instanceof IWorkingSet) {
			IWorkingSet rootSet = (IWorkingSet) newInput;
			helper = new WorkingSetHelper(rootSet);
		}
		
		if(viewer instanceof CommonViewer)
			projectExplorer = ((CommonViewer)viewer).getCommonNavigator();
		
		updateRootMode();
		
	}
 
	private void updateRootMode() {
		if (projectExplorer == null) {
			return;
		}
		if( extensionStateModel.getBooleanProperty(SHOW_TOP_LEVEL_WORKING_SETS) )
			projectExplorer.setRootMode(ProjectExplorer.WORKING_SETS);
		else
			projectExplorer.setRootMode(ProjectExplorer.PROJECTS);
	}

	protected class WorkingSetHelper {

		private final IWorkingSet workingSet;
		private final Map<IAdaptable, IWorkingSet> parents = new WeakHashMap<IAdaptable, IWorkingSet>();

		/**
		 * Create a Helper class for the given working set
		 * 
		 * @param set
		 *            The set to use to build the item to parent map.
		 */
		public WorkingSetHelper(IWorkingSet set) {
			workingSet = set;

			if (workingSet.isAggregateWorkingSet()) {
				IAggregateWorkingSet aggregateSet = (IAggregateWorkingSet) workingSet;
				if (workingSetRoot == null)
					workingSetRoot = aggregateSet;

				IWorkingSet[] components = aggregateSet.getComponents();

				for (int componentIndex = 0; componentIndex < components.length; componentIndex++) {
					IAdaptable[] elements = getWorkingSetElements(components[componentIndex]);
					for (int elementsIndex = 0; elementsIndex < elements.length; elementsIndex++) {
						parents.put(elements[elementsIndex], components[componentIndex]);
					}
					parents.put(components[componentIndex], aggregateSet);

				}
			} else {
				IAdaptable[] elements = getWorkingSetElements(workingSet);
				for (int elementsIndex = 0; elementsIndex < elements.length; elementsIndex++) {
					parents.put(elements[elementsIndex], workingSet);
				}
			}
		}

		/**
		 * 
		 * @param element
		 *            An element from the viewer
		 * @return The parent associated with the element, if any.
		 */
		public Object getParent(Object element) {
			if (element instanceof IWorkingSet && element != workingSetRoot)
				return workingSetRoot;
			return parents.get(element);
		}
	}
	


}
