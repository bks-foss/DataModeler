package com.isb.datamodeler.internal.ui.views.actions.workingset;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IAggregateWorkingSet;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ResourceWorkingSetFilter;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.IExtensionActivationListener;
import org.eclipse.ui.navigator.IExtensionStateModel;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.INavigatorFilterService;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.views.workingsets.DataModelerWorkingSetsContentProvider;

public class DataModelerWorkingSetActionProvider extends CommonActionProvider {

	private static final String TAG_CURRENT_WORKING_SET_NAME = "currentWorkingSetName"; //$NON-NLS-1$

	private static final String WORKING_SET_FILTER_ID = "com.isb.datamodeler.ui.navigator.filters.workingSet"; //$NON-NLS-1$
	
	private boolean contributedToViewMenu = false;

	private CommonViewer viewer;

	private INavigatorContentService contentService;

	private INavigatorFilterService filterService;
	
	private DataModelerWorkingSetFilterActionGroup workingSetActionGroup;
	private DataModelerWorkingSetRootModeActionGroup workingSetRootModeActionGroup;

	private Object originalViewerInput = ResourcesPlugin.getWorkspace().getRoot();

	private IExtensionStateModel extensionStateModel;

	private boolean emptyWorkingSet;
	private IWorkingSet workingSet;

	private IPropertyChangeListener topLevelModeListener;
	
	private boolean ignoreFilterChangeEvents;

	/**
	 * Provides a smart listener to monitor changes to the Working Set Manager.
	 * 
	 */
	public class WorkingSetManagerListener implements IPropertyChangeListener {

		private boolean listening = false;

		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			Object newValue = event.getNewValue();
			Object oldValue = event.getOldValue();

			String newLabel = null;
			if (IWorkingSetManager.CHANGE_WORKING_SET_REMOVE.equals(property) && oldValue == workingSet) {
				newLabel = ""; //$NON-NLS-1$
				setWorkingSet(null);
			} else if (IWorkingSetManager.CHANGE_WORKING_SET_NAME_CHANGE.equals(property) && newValue == workingSet) {
				newLabel = workingSet.getLabel();
			} else if (IWorkingSetManager.CHANGE_WORKING_SET_CONTENT_CHANGE.equals(property) && newValue == workingSet) {
				if (workingSet.isAggregateWorkingSet() && workingSet.isEmpty()) {
					// act as if the working set has been made null
					if (!emptyWorkingSet) {
						emptyWorkingSet = true;
						setWorkingSetFilter(null);
						newLabel = null;
					}
				} else {
					// we've gone from empty to non-empty on our set.
					// Restore it.
					if (emptyWorkingSet) {
						emptyWorkingSet = false;
						setWorkingSetFilter(workingSet);
						newLabel = workingSet.getLabel();
					}
				}
			}
			if (viewer != null) {
				if (newLabel != null)
					viewer.getCommonNavigator().setWorkingSetLabel(newLabel);
				viewer.getFrameList().reset();
				viewer.refresh();
			}
		}

		/**
		 * Begin listening to the correct source if not already listening.
		 */
		public synchronized void listen() {
			if (!listening) {
				PlatformUI.getWorkbench().getWorkingSetManager().addPropertyChangeListener(managerChangeListener);
				listening = true;
			}
		}

		/**
		 * Begin listening to the correct source if not already listening.
		 */
		public synchronized void ignore() {
			if (listening) {
				PlatformUI.getWorkbench().getWorkingSetManager().removePropertyChangeListener(managerChangeListener);
				listening = false;
			}
		}
	}

	private IPropertyChangeListener filterChangeListener = new IPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent event) {
			
			if (ignoreFilterChangeEvents)
				return;
			
			IWorkingSet newWorkingSet = (IWorkingSet) event.getNewValue();

			setWorkingSet(newWorkingSet);
			if (newWorkingSet != null) {
				if (!contentService.isActive(DataModelerWorkingSetsContentProvider.EXTENSION_ID)) {
					contentService.getActivationService().activateExtensions(
							new String[] { DataModelerWorkingSetsContentProvider.EXTENSION_ID }, false);
					contentService.getActivationService().persistExtensionActivations();
				}
				if (newWorkingSet.isAggregateWorkingSet()) {
					IAggregateWorkingSet agWs = (IAggregateWorkingSet) newWorkingSet;
					IWorkingSet[] comps = agWs.getComponents();
					if (comps.length > 1) {
						viewer.getCommonNavigator().setWorkingSetLabel(
								Messages.bind("DataModelerWorkingSetActionProvider.multipleWorkingSets"));
					} else if (comps.length > 0) {
						viewer.getCommonNavigator().setWorkingSetLabel(comps[0].getLabel());
					} else {
						viewer.getCommonNavigator().setWorkingSetLabel(null);
					}
				} else
					viewer.getCommonNavigator().setWorkingSetLabel(workingSet.getLabel());
			} else {
				viewer.getCommonNavigator().setWorkingSetLabel(null);
			}

			viewer.getFrameList().reset();
		}
	};

	private WorkingSetManagerListener managerChangeListener = new WorkingSetManagerListener();

	private IExtensionActivationListener activationListener = new IExtensionActivationListener() {

		private IWorkingSet savedWorkingSet;

		public void onExtensionActivation(String aViewerId, String[] theNavigatorExtensionIds, boolean isActive) {

			for (int i = 0; i < theNavigatorExtensionIds.length; i++) {
				if (DataModelerWorkingSetsContentProvider.EXTENSION_ID.equals(theNavigatorExtensionIds[i])) {
					if (isActive) {
						extensionStateModel = contentService.findStateModel(DataModelerWorkingSetsContentProvider.EXTENSION_ID);
						workingSetRootModeActionGroup.setStateModel(extensionStateModel);
						extensionStateModel.addPropertyChangeListener(topLevelModeListener);

						if (savedWorkingSet != null) {
							setWorkingSet(savedWorkingSet);
						}
						managerChangeListener.listen();

					} else {
						savedWorkingSet = workingSet;
						setWorkingSet(null);
						viewer.getCommonNavigator().setWorkingSetLabel(null);
						managerChangeListener.ignore();
						workingSetRootModeActionGroup.setShowTopLevelWorkingSets(false);
						extensionStateModel.removePropertyChangeListener(topLevelModeListener);

					}
				}
			}
		}

	};

	public void init(ICommonActionExtensionSite aSite) {
		viewer = (CommonViewer) aSite.getStructuredViewer();
		contentService = aSite.getContentService();
		filterService = contentService.getFilterService();

		extensionStateModel = contentService.findStateModel(DataModelerWorkingSetsContentProvider.EXTENSION_ID);

		workingSetActionGroup = new DataModelerWorkingSetFilterActionGroup(aSite.getViewSite().getShell(), filterChangeListener);
		workingSetRootModeActionGroup = new DataModelerWorkingSetRootModeActionGroup(viewer, extensionStateModel);

		topLevelModeListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				setWorkingSet(workingSet);
				viewer.getFrameList().reset();
			}
		};

		if (contentService.isActive(DataModelerWorkingSetsContentProvider.EXTENSION_ID)) {
			managerChangeListener.listen();
			extensionStateModel.addPropertyChangeListener(topLevelModeListener);
		}

		contentService.getActivationService().addExtensionActivationListener(activationListener);
	}

	/**
	 * Restores the working set filter from the persistence store.
	 */
	protected void initWorkingSetFilter(String workingSetName) {
		IWorkingSet workingSet = null;

		if (workingSetName != null && workingSetName.length() > 0) {
			IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
			workingSet = workingSetManager.getWorkingSet(workingSetName);
		} else if (PlatformUI.getPreferenceStore().getBoolean(
				IWorkbenchPreferenceConstants.USE_WINDOW_WORKING_SET_BY_DEFAULT)) {
			// use the window set by default if the global preference is set
			workingSet = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getAggregateWorkingSet();
		}

		if (workingSet != null) {
			setWorkingSet(workingSet);
		}
	}

	private void setWorkingSetFilter(IWorkingSet workingSet) {
		setWorkingSetFilter(workingSet, FIRST_TIME);
	}
	
	private static final boolean FIRST_TIME = true;
	
	private void setWorkingSetFilter(IWorkingSet workingSet, boolean firstTime) {
		ResourceWorkingSetFilter workingSetFilter = null;
		ViewerFilter[] filters = viewer.getFilters();
		for (int i = 0; i < filters.length; i++) {
			if (filters[i] instanceof ResourceWorkingSetFilter) {
				workingSetFilter = (ResourceWorkingSetFilter) filters[i];
				break;
			}
		}
		if (workingSetFilter == null) {
			if (firstTime) {
				filterService.setActiveFilterIds(new String[] { WORKING_SET_FILTER_ID });
				ViewerFilter[] visibleFilters =	filterService.getVisibleFilters(true);
				viewer.setFilters(visibleFilters);
				setWorkingSetFilter(workingSet, !FIRST_TIME);
				return;
			}
			DataModelerUI.log("Required filter " + WORKING_SET_FILTER_ID +  //$NON-NLS-1$
					" is not present. Working set support will not function correctly.",   //$NON-NLS-1$
				new Status(IStatus.ERROR, DataModelerUI.PLUGIN_ID, ""));  //$NON-NLS-1$
			return;
		}
		workingSetFilter.setWorkingSet(emptyWorkingSet ? null : workingSet);
	}

	/**
	 * Set current active working set.
	 * 
	 * @param workingSet
	 *            working set to be activated, may be <code>null</code>
	 */
	protected void setWorkingSet(IWorkingSet workingSet) {
		this.workingSet = workingSet;
		emptyWorkingSet = workingSet != null && workingSet.isAggregateWorkingSet() && workingSet.isEmpty();

        ignoreFilterChangeEvents = true;
        try {
        	workingSetActionGroup.setWorkingSet(workingSet);
        } finally {
        	ignoreFilterChangeEvents = false;
       	}		
		
		if (viewer != null) {
			setWorkingSetFilter(workingSet);
			if (workingSet == null || emptyWorkingSet
					|| !extensionStateModel.getBooleanProperty(DataModelerWorkingSetsContentProvider.SHOW_TOP_LEVEL_WORKING_SETS)) {
				if (viewer.getInput() != originalViewerInput) {
					viewer.setInput(originalViewerInput);
				} else {
					viewer.refresh();
				}
			} else {
				if (!workingSet.isAggregateWorkingSet()) {
					IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
					viewer.setInput(workingSetManager.createAggregateWorkingSet(
							"", "", new IWorkingSet[] { workingSet })); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					viewer.setInput(workingSet);
				}
			}
		}
	}

	public void restoreState(final IMemento aMemento) {
		super.restoreState(aMemento);

		// Need to run this async to avoid being reentered when processing a selection change
		viewer.getControl().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				boolean showWorkingSets = true;
				if (aMemento != null) {
					Integer showWorkingSetsInt = aMemento
							.getInteger(DataModelerWorkingSetsContentProvider.SHOW_TOP_LEVEL_WORKING_SETS);
					showWorkingSets = showWorkingSetsInt == null || showWorkingSetsInt.intValue() == 1;
					extensionStateModel.setBooleanProperty(DataModelerWorkingSetsContentProvider.SHOW_TOP_LEVEL_WORKING_SETS,
							showWorkingSets);
					workingSetRootModeActionGroup.setShowTopLevelWorkingSets(showWorkingSets);

					String lastWorkingSetName = aMemento.getString(TAG_CURRENT_WORKING_SET_NAME);
					initWorkingSetFilter(lastWorkingSetName);
				} else {
					showWorkingSets = false;

					extensionStateModel.setBooleanProperty(DataModelerWorkingSetsContentProvider.SHOW_TOP_LEVEL_WORKING_SETS,
							showWorkingSets);
					workingSetRootModeActionGroup.setShowTopLevelWorkingSets(showWorkingSets);
				}
			}
		});
	}

	public void saveState(IMemento aMemento) {
		super.saveState(aMemento);

		if (aMemento != null) {
			int showWorkingSets = extensionStateModel
					.getBooleanProperty(DataModelerWorkingSetsContentProvider.SHOW_TOP_LEVEL_WORKING_SETS) ? 1 : 0;
			aMemento.putInteger(DataModelerWorkingSetsContentProvider.SHOW_TOP_LEVEL_WORKING_SETS, showWorkingSets);

			if (workingSet != null) {
				aMemento.putString(TAG_CURRENT_WORKING_SET_NAME, workingSet.getName());
			}
		}

	}

	public void fillActionBars(IActionBars actionBars) {
		if (!contributedToViewMenu) {
			try {
				super.fillActionBars(actionBars);
				workingSetActionGroup.fillActionBars(actionBars);
				if (workingSetRootModeActionGroup != null) {
					workingSetRootModeActionGroup.fillActionBars(actionBars);
				}
			} finally {
				contributedToViewMenu = true;
			}
		}
	}

	public void dispose() {
		super.dispose();
		workingSetActionGroup.dispose();
		if (workingSetRootModeActionGroup != null) {
			workingSetRootModeActionGroup.dispose();
		}

		managerChangeListener.ignore();
		extensionStateModel.removePropertyChangeListener(topLevelModeListener);

		contentService.getActivationService().removeExtensionActivationListener(activationListener);
	}

	/**
	 * This is used only for the tests.
	 * 
	 * @return a PropertyChangeListener
	 */
	public IPropertyChangeListener getFilterChangeListener() {
		return filterChangeListener;
	}

}
