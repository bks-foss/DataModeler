package com.isb.datamodeler.internal.ui.dialogs.search;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ResourceWorkingSetFilter;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.actions.WorkingSetFilterActionGroup;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.eclipse.ui.dialogs.SearchPattern;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.diagram.EDMDiagram;
import com.isb.datamodeler.ui.project.EProject;

public class ShowTableInDiagramDialog extends FilteredItemsSelectionDialog {

	private static final String DIALOG_SETTINGS = "org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog"; //$NON-NLS-1$

	private static final String WORKINGS_SET_SETTINGS = "WorkingSet"; //$NON-NLS-1$

	private ResourceItemLabelProvider resourceItemLabelProvider;

	private ResourceItemDetailsLabelProvider resourceItemDetailsLabelProvider;

	private WorkingSetFilterActionGroup workingSetFilterActionGroup;

	private CustomWorkingSetFilter workingSetFilter = new CustomWorkingSetFilter();
	
	private String title;

	
	/**
	 * The base outer-container which will be used to search for resources. This
	 * is the root of the tree that spans the search space. Often, this is the
	 * workspace root.
	 */
	private IContainer container;

	/**
	 * The container to use as starting point for relative search, or
	 * <code>null</code> if none.
	 * @since 3.6
	 */
	private IContainer searchContainer;
	
	
	private IEditorPart activeEditor;

	/**
	 * Creates a new instance of the class
	 * 
	 * @param shell
	 *            the parent shell
	 * @param multi
	 *            the multi selection flag
	 * @param container
	 *            the container to select resources from, e.g. the workspace root
	 * @param typesMask
	 *            a mask specifying which resource types should be shown in the dialog.
	 *            The mask should contain one or more of the resource type bit masks
	 *            defined in {@link IResource#getType()}
	 */
	public ShowTableInDiagramDialog(Shell shell, IContainer container) {
		super(shell, false);

		setTitle(Messages.bind("ShowTableInDiagramDialog.title"));
		
		setSeparatorLabel(Messages.bind("ShowTableInDiagramDialog.elements"));

		/*
		 * Allow location of paths relative to a searchContainer, which is
		 * initialized from the active editor or the selected element.
		 */
		IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (ww != null) {
			IWorkbenchPage activePage = ww.getActivePage();
			if (activePage != null) {
				IResource resource = null;
				activeEditor = activePage.getActiveEditor();
				if (activeEditor != null && activeEditor == activePage.getActivePart()) {
					IEditorInput editorInput = activeEditor.getEditorInput();
					resource = ResourceUtil.getResource(editorInput);
				} else {
					ISelection selection = ww.getSelectionService().getSelection();
					if (selection instanceof IStructuredSelection) {
						IStructuredSelection structuredSelection = (IStructuredSelection) selection;
						if (structuredSelection.size() == 1) {
							resource = ResourceUtil.getResource(structuredSelection.getFirstElement());
						}
					}
				}
				if (resource != null) {
					if (!(resource instanceof IContainer)) {
						resource = resource.getParent();
					}
					searchContainer = (IContainer) resource;
				}
			}
		}

		this.container = container;

		resourceItemLabelProvider = new ResourceItemLabelProvider();

		resourceItemDetailsLabelProvider = new ResourceItemDetailsLabelProvider();

		setListLabelProvider(resourceItemLabelProvider);
		setDetailsLabelProvider(resourceItemDetailsLabelProvider);
		
		setSelectionHistory(new DiagramedElementsProvider(activeEditor));

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionDialog#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		super.setTitle(title);
		this.title = title;
	}

	/**
	 * Adds or replaces subtitle of the dialog
	 * 
	 * @param text
	 *            the new subtitle
	 */
	private void setSubtitle(String text) {
		if (text == null || text.length() == 0) {
			getShell().setText(title);
		} else {
			getShell().setText(title + " - " + text); //$NON-NLS-1$
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getDialogSettings()
	 */
	protected IDialogSettings getDialogSettings() {
		IDialogSettings settings = DataModelerUI.getDefault()
				.getDialogSettings().getSection(DIALOG_SETTINGS);

		if (settings == null) {
			settings = DataModelerUI.getDefault().getDialogSettings()
					.addNewSection(DIALOG_SETTINGS);
		}

		return settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#storeDialog(org.eclipse.jface.dialogs.IDialogSettings)
	 */
	protected void storeDialog(IDialogSettings settings) {
		super.storeDialog(settings);

		XMLMemento memento = XMLMemento.createWriteRoot("workingSet"); //$NON-NLS-1$
		workingSetFilterActionGroup.saveState(memento);
		workingSetFilterActionGroup.dispose();
		StringWriter writer = new StringWriter();
		try {
			memento.save(writer);
			settings.put(WORKINGS_SET_SETTINGS, writer.getBuffer().toString());
		} catch (IOException e) {
			StatusManager.getManager().handle(
					new Status(IStatus.ERROR, DataModelerUI.PLUGIN_ID,
							IStatus.ERROR, "", e)); //$NON-NLS-1$
			// don't do anything. Simply don't store the settings
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#restoreDialog(org.eclipse.jface.dialogs.IDialogSettings)
	 */
	protected void restoreDialog(IDialogSettings settings) {
		super.restoreDialog(settings);

		String setting = settings.get(WORKINGS_SET_SETTINGS);
		if (setting != null) {
			try {
				IMemento memento = XMLMemento.createReadRoot(new StringReader(
						setting));
				workingSetFilterActionGroup.restoreState(memento);
			} catch (WorkbenchException e) {
				StatusManager.getManager().handle(
						new Status(IStatus.ERROR, DataModelerUI.PLUGIN_ID,
								IStatus.ERROR, "", e)); //$NON-NLS-1$
				// don't do anything. Simply don't restore the settings
			}
		}

		addListFilter(workingSetFilter);

		applyFilter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#fillViewMenu(org.eclipse.jface.action.IMenuManager)
	 */
	protected void fillViewMenu(IMenuManager menuManager) {
		super.fillViewMenu(menuManager);

		
		workingSetFilterActionGroup = new WorkingSetFilterActionGroup(
				getShell(), new IPropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent event) {
						String property = event.getProperty();

						if (WorkingSetFilterActionGroup.CHANGE_WORKING_SET
								.equals(property)) {

							IWorkingSet workingSet = (IWorkingSet) event
									.getNewValue();

							if (workingSet != null
									&& !(workingSet.isAggregateWorkingSet() && workingSet
											.isEmpty())) {
								workingSetFilter.setWorkingSet(workingSet);
								setSubtitle(workingSet.getLabel());
							} else {
								IWorkbenchWindow window = PlatformUI
										.getWorkbench()
										.getActiveWorkbenchWindow();

								if (window != null) {
									IWorkbenchPage page = window
											.getActivePage();
									workingSet = page.getAggregateWorkingSet();

									if (workingSet.isAggregateWorkingSet()
											&& workingSet.isEmpty()) {
										workingSet = null;
									}
								}

								workingSetFilter.setWorkingSet(workingSet);
								setSubtitle(null);
							}

							scheduleRefresh();
						}
					}
				});

		
		menuManager.add(new Separator());
		
		workingSetFilterActionGroup.fillContextMenu(menuManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#createExtendedContentArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createExtendedContentArea(Composite parent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionDialog#getResult()
	 */
	public Object[] getResult() {
		Object[] result = super.getResult();

		if (result == null)
			return null;

		List<EModelElement> resultToReturn = new ArrayList<EModelElement>();

		for (int i = 0; i < result.length; i++) {
			if (result[i] instanceof EModelElement) {
				resultToReturn.add(((EModelElement)result[i]));
			}
		}

		return resultToReturn.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#open()
	 */
	public int open() {
		if (getInitialPattern() == null) {
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (window != null) {
				ISelection selection = window.getSelectionService()
						.getSelection();
				if (selection instanceof ITextSelection) {
					String text = ((ITextSelection) selection).getText();
					if (text != null) {
						text = text.trim();
						if (text.length() > 0) {
							IWorkspace workspace = ResourcesPlugin
									.getWorkspace();
							IStatus result = workspace.validateName(text,
									IResource.FILE);
							if (result.isOK()) {
								setInitialPattern(text);
							}
						}
					}
				}
			}
		}
		return super.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getElementName(java.lang.Object)
	 */
	public String getElementName(Object item) {
		EModelElement object = (EModelElement) item;
		return DatamodelerDiagramEditorPlugin.getInstance().getItemLabel(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#validateItem(java.lang.Object)
	 */
	protected IStatus validateItem(Object item) {
		return new Status(IStatus.OK, DataModelerUI.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#createFilter()
	 */
	protected ItemsFilter createFilter() {
		return new TableFilter(container, searchContainer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#applyFilter()
	 */
	protected void applyFilter() {
		super.applyFilter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#getItemsComparator()
	 */
	protected Comparator<EModelElement> getItemsComparator() {
		return new Comparator<EModelElement>() {

			public int compare(EModelElement o1, EModelElement o2) 
			{
				Collator collator = Collator.getInstance();
				
				String s1 = DatamodelerDiagramEditorPlugin.getInstance().getItemLabel(o1);
				String s2 = DatamodelerDiagramEditorPlugin.getInstance().getItemLabel(o2);
				
				int comparability = collator.compare(s1, s2);
				if (comparability != 0)
					return comparability;
				
				// Search for resource relative paths
				if (searchContainer != null) {
					IPath c1 = new Path(o1.eResource().getURI().toPlatformString(true));
					IPath c2 = new Path(o2.eResource().getURI().toPlatformString(true));
					
					// Return paths 'closer' to the searchContainer first
					comparability = pathDistance(c1) - pathDistance(c2);
					if (comparability != 0)
						return comparability;
				}

				// Finally compare full path segments
				IPath p1 = new Path(o1.eResource().getURI().toPlatformString(true));
				IPath p2 = new Path(o2.eResource().getURI().toPlatformString(true));
				// Don't compare file names again, so subtract 1
				int c1 = p1.segmentCount() - 1;
				int c2 = p2.segmentCount() - 1;
				for (int i= 0; i < c1 && i < c2; i++) {
					comparability = collator.compare(p1.segment(i), p2.segment(i));
					if (comparability != 0)
						return comparability;
				}
				comparability = c1 - c2;

				return comparability;

			};
		};
	}

	/**
	 * Return the "distance" of the item from the root of the relative search
	 * container. Distances can be compared (smaller numbers are better).
	 * <br>
	 * - Closest distance is if the item is the same folder as the search container.<br>
	 * - Next are folders inside the search container.<br>
	 * - After all those, distance increases with decreasing matching prefix folder count.<br>
	 * 
	 * @param item
	 *            parent of the resource being examined
	 * @return the "distance" of the passed in IResource from the search
	 *         container
	 * @since 3.6
	 */
	private int pathDistance(IPath itemPath) {
		// Container search path: e.g. /a/b/c
		IPath containerPath = searchContainer.getFullPath();
		// itemPath:          distance:
		// /a/b/c         ==> 0
		// /a/b/c/d/e     ==> 2
		// /a/b           ==> Integer.MAX_VALUE/4 + 1
		// /a/x/e/f       ==> Integer.MAX_VALUE/4 + 2
		// /g/h           ==> Integer.MAX_VALUE/2
		if (itemPath.equals(containerPath))
			return 0;
		
		int matching = containerPath.matchingFirstSegments(itemPath);
		if (matching == 0)
			return Integer.MAX_VALUE / 2;
		
		int containerSegmentCount = containerPath.segmentCount();
		if (matching == containerSegmentCount) {
			// inside searchContainer: 
			return itemPath.segmentCount() - matching;
		}
		
		//outside searchContainer:
		return Integer.MAX_VALUE / 4 + containerSegmentCount - matching;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#fillContentProvider(org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.AbstractContentProvider,
	 *      org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void fillContentProvider(AbstractContentProvider contentProvider,
			ItemsFilter itemsFilter, IProgressMonitor progressMonitor)
			throws CoreException {
		if (itemsFilter instanceof TableFilter)
			container.accept(new ResourceProxyVisitor(contentProvider,
					(TableFilter) itemsFilter, progressMonitor),
					IResource.NONE);
		if (progressMonitor != null)
			progressMonitor.done();

	}

	/**
	 * A label provider for ResourceDecorator objects. It creates labels with a
	 * resource full path for duplicates. It uses the Platform UI label
	 * decorator for providing extra resource info.
	 */
	private class ResourceItemLabelProvider extends LabelProvider implements
			ILabelProviderListener, IStyledLabelProvider {

		// Need to keep our own list of listeners
		private ListenerList listeners = new ListenerList();

		/**
		 * Creates a new instance of the class
		 */
		public ResourceItemLabelProvider() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(Object element) {
			if (!(element instanceof EModelElement)) {
				return super.getImage(element);
			}

			EModelElement object = (EModelElement) element;

			return DatamodelerDiagramEditorPlugin.getInstance().getImage(object);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			if (!(element instanceof EModelElement)) {
				return super.getText(element);
			}

			EModelElement object = (EModelElement) element;

			String str = DatamodelerDiagramEditorPlugin.getInstance().getItemLabel(object);

			// extra info for duplicates
			if (isDuplicateElement(element))
			{
				String parentDetails = getObjectPath(object);;
				
				str = str + " - " + parentDetails; //$NON-NLS-1$
			}

			return str;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider#getStyledText(java.lang.Object)
		 */
		public StyledString getStyledText(Object element) {
			if (!(element instanceof EModelElement)) {
				return new StyledString(super.getText(element));
			}
			
			EModelElement object = (EModelElement) element;
			
			String name = DatamodelerDiagramEditorPlugin.getInstance().getItemLabel(object);

			StyledString str = new StyledString(name);
			

			// extra info for duplicates
			if (isDuplicateElement(element)) {
				
				String parentDetails = getObjectPath(object);
				
				str.append(" - ", StyledString.QUALIFIER_STYLER); //$NON-NLS-1$
				str.append(parentDetails, StyledString.QUALIFIER_STYLER); //$NON-NLS-1$
			}
			
			return str;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#dispose()
		 */
		public void dispose() {
			super.dispose();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {
			listeners.add(listener);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {
			listeners.remove(listener);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
		 */
		public void labelProviderChanged(LabelProviderChangedEvent event) {
			Object[] l = listeners.getListeners();
			for (int i = 0; i < listeners.size(); i++) {
				((ILabelProviderListener) l[i]).labelProviderChanged(event);
			}
		}

	}

	/**
	 * A label provider for details of ResourceItem objects.
	 */
	private class ResourceItemDetailsLabelProvider extends
			ResourceItemLabelProvider {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(Object element) {
			if (!(element instanceof EModelElement)) {
				return super.getImage(element);
			}

			Object root = getObjectRoot((EObject)element);
			
			return DatamodelerDiagramEditorPlugin.getInstance().getImage(root);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			if (!(element instanceof EModelElement)) {
				return super.getText(element);
			}

			return getObjectPath((EModelElement) element);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProviderListener#labelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
		 */
		public void labelProviderChanged(LabelProviderChangedEvent event) {
			Object[] l = super.listeners.getListeners();
			for (int i = 0; i < super.listeners.size(); i++) {
				((ILabelProviderListener) l[i]).labelProviderChanged(event);
			}
		}
	}

	/**
	 * Viewer filter which filters resources due to current working set
	 */
	private class CustomWorkingSetFilter extends ViewerFilter {
		private ResourceWorkingSetFilter resourceWorkingSetFilter = new ShowTableInDiagramWorkingSetFilter();

		/**
		 * Sets the active working set.
		 * 
		 * @param workingSet
		 *            the working set the filter should work with
		 */
		public void setWorkingSet(IWorkingSet workingSet) {
			resourceWorkingSetFilter.setWorkingSet(workingSet);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			return resourceWorkingSetFilter.select(viewer, parentElement,
						element);
		}
	}

	/**
	 * ResourceProxyVisitor to visit resource tree and get matched resources.
	 * During visit resources it updates progress monitor and adds matched
	 * resources to ContentProvider instance.
	 */
	private class ResourceProxyVisitor implements IResourceProxyVisitor {

		private AbstractContentProvider proxyContentProvider;

		private TableFilter resourceFilter;

		private IProgressMonitor progressMonitor;

		private List<IResource> projects;

		/**
		 * Creates new ResourceProxyVisitor instance.
		 * 
		 * @param contentProvider
		 * @param resourceFilter
		 * @param progressMonitor
		 * @throws CoreException
		 */
		public ResourceProxyVisitor(AbstractContentProvider contentProvider,
				TableFilter resourceFilter, IProgressMonitor progressMonitor)
				throws CoreException {
			super();
			this.proxyContentProvider = contentProvider;
			this.resourceFilter = resourceFilter;
			this.progressMonitor = progressMonitor;
			IResource[] resources = container.members();
			this.projects = new ArrayList<IResource>(Arrays.asList(resources));

			if (progressMonitor != null)
				progressMonitor
						.beginTask(
								Messages.bind("ShowTableInDiagramDialog.searching"),
								projects.size());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceProxyVisitor#visit(org.eclipse.core.resources.IResourceProxy)
		 */
		public boolean visit(IResourceProxy proxy) {

			if (progressMonitor.isCanceled())
				return false;

			IResource resource = proxy.requestResource();

			if (this.projects.remove((resource.getProject()))
					|| this.projects.remove((resource))) {
				progressMonitor.worked(1);
			}
			
			if(resource instanceof IProject)
			{
				EProject eProject = UtilsDataModelerUI.findEProject((IProject)resource);
				
				if(eProject!=null) // SI no es un Datamodeler Project es null
				{
					for(ESchema schema:eProject.getSchemas())
						for(ETable table: schema.getTables())
							proxyContentProvider.add(table, resourceFilter);
				}

			}

			

			if (resource.getType() == IResource.FILE) {
				return false;
			}

			return true;
		}
	}

	/**
	 * Filters resources using pattern and showDerived flag. It overrides
	 * ItemsFilter.
	 */
	protected class TableFilter extends ItemsFilter {

		private IContainer filterContainer;

		/**
		 * Container path pattern. Is <code>null</code> when only a file name pattern is used.
		 * @since 3.6
		 */
		private SearchPattern containerPattern;
		/**
		 * Container path pattern, relative to the current searchContainer. Is <code>null</code> if there's no search container.
		 * @since 3.6
		 */
		private SearchPattern relativeContainerPattern;
		
		/**
		 * Camel case pattern for the name part of the file name (without extension). Is <code>null</code> if there's no extension.
		 * @since 3.6
		 */
		SearchPattern namePattern;
		
		/**
		 * Creates new ResourceFilter instance
		 * 
		 * @param container
		 * @param showDerived
		 *            flag which determine showing derived elements
		 * @param typeMask
		 */
		public TableFilter(IContainer container) {
			super();
			this.filterContainer = container;
		}

		/**
		 * Creates new ResourceFilter instance
		 * 
		 * @param container
		 * @param searchContainer 
		 *            IContainer to use for performing relative search
		 * @param showDerived
		 *            flag which determine showing derived elements
		 * @param typeMask
		 * @since 3.6
		 */
		private TableFilter(IContainer container, IContainer searchContainer) {
			this(container);

			String stringPattern = getPattern();
			String filenamePattern;
			
			int sep = stringPattern.lastIndexOf(IPath.SEPARATOR);
			if (sep != -1) {
				filenamePattern = stringPattern.substring(sep + 1, stringPattern.length());
				if ("*".equals(filenamePattern)) //$NON-NLS-1$
					filenamePattern= "**"; //$NON-NLS-1$
				
				if (sep > 0) {
					if (filenamePattern.length() == 0) // relative patterns don't need a file name
						filenamePattern= "**"; //$NON-NLS-1$
						
					String containerPattern = stringPattern.substring(0, sep);
					
					if (searchContainer != null) {
						relativeContainerPattern = new SearchPattern(SearchPattern.RULE_EXACT_MATCH | SearchPattern.RULE_PATTERN_MATCH);
						relativeContainerPattern.setPattern(searchContainer.getFullPath().append(containerPattern).toString());
					}
					
					if (!containerPattern.startsWith("" + IPath.SEPARATOR)) //$NON-NLS-1$
						containerPattern = IPath.SEPARATOR + containerPattern;
					this.containerPattern= new SearchPattern(SearchPattern.RULE_EXACT_MATCH | SearchPattern.RULE_PREFIX_MATCH | SearchPattern.RULE_PATTERN_MATCH);
					this.containerPattern.setPattern(containerPattern);
				}
				patternMatcher.setPattern(filenamePattern);
				
			} else {
				filenamePattern= stringPattern;
			}
		}

		/**
		 * Creates new ResourceFilter instance
		 */
		public TableFilter() {
			this(container, searchContainer);
		}

		/**
		 * @param item
		 *            Must be instance of IResource, otherwise
		 *            <code>false</code> will be returned.
		 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter#isConsistentItem(java.lang.Object)
		 */
		public boolean isConsistentItem(Object item) {
			if (!(item instanceof EModelElement)) {
				return false;
			}
			EModelElement table = (EModelElement) item;
			if (this.filterContainer.findMember(new Path(table.eResource().getURI().toPlatformString(true))) != null)
				return true;
			return false;
		}

		/**
		 * @param item
		 *            Must be instance of IResource, otherwise
		 *            <code>false</code> will be returned.
		 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter#matchItem(java.lang.Object)
		 */
		public boolean matchItem(Object item) {
			if (!(item instanceof EModelElement)) {
				return false;
			}
			EModelElement table = (EModelElement) item;

			String name = DatamodelerDiagramEditorPlugin.getInstance().getItemLabel(table);
			if (nameMatches(name)) {
				if (containerPattern != null) {
					// match full container path:
					String containerPath = table.eResource().getURI().toString();
					if (containerPattern.matches(containerPath))
						return true;
					// match path relative to current selection:
					if (relativeContainerPattern != null)
						return relativeContainerPattern.matches(containerPath);
					return false;
				}
				return true;
			}
			
			return false;			
		}

		private boolean nameMatches(String name) {
			return matches(name);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter#isSubFilter(org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter)
		 */
		public boolean isSubFilter(ItemsFilter filter) {
			if (!super.isSubFilter(filter))
				return false;
			if (filter instanceof TableFilter) {
				TableFilter resourceFilter = (TableFilter) filter;
				if (containerPattern == null) {
					return resourceFilter.containerPattern == null;
				} else if (resourceFilter.containerPattern == null) {
					return false;
				} else {
					return containerPattern.equals(resourceFilter.containerPattern);
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter#equalsFilter(org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter)
		 */
		public boolean equalsFilter(ItemsFilter iFilter) {
			if (!super.equalsFilter(iFilter))
				return false;
			if (iFilter instanceof TableFilter) {
				TableFilter resourceFilter = (TableFilter) iFilter;
				if (containerPattern == null) {
					return resourceFilter.containerPattern == null;
				} else if (resourceFilter.containerPattern == null) {
					return false;
				} else {
					return containerPattern.equals(resourceFilter.containerPattern);
				}
			}
			return false;
		}

	}

	/**
	 * <code>ResourceSelectionHistory</code> provides behavior specific to
	 * resources - storing and restoring <code>IResource</code>s state
	 * to/from XML (memento).
	 */
	private class DiagramedElementsProvider extends SelectionHistory {

		private List<EObject> contents = new ArrayList<EObject>();
		
		public DiagramedElementsProvider(IEditorPart activeEditor) {
			super();
			
			if(activeEditor!=null && activeEditor instanceof DiagramEditor)
				for(Object child:((DiagramEditor)activeEditor).getDiagramEditPart().getChildren())
					if(child instanceof IGraphicalEditPart)
						contents.add(((IGraphicalEditPart)child).resolveSemanticElement());
		}
		
		

		/**
		 * Returns <code>true</code> if history contains object.
		 * 
		 * @param object
		 *            the item for which check will be executed
		 * @return <code>true</code> if history contains object
		 *         <code>false</code> in other way
		 */
		public synchronized boolean contains(Object object) {
			
			return contents.contains(object);
		}

		/**
		 * Gets array of history items.
		 * 
		 * @return array of history elements
		 */
		public synchronized Object[] getHistoryItems() {
			return contents.toArray();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.SelectionHistory#restoreItemFromMemento(org.eclipse.ui.IMemento)
		 */
		protected Object restoreItemFromMemento(IMemento element) {
			
//			ResourceFactory resourceFactory = new ResourceFactory();
//			IResource resource = (IResource) resourceFactory
//					.createElement(element);
//			return resource;
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.SelectionHistory#storeItemToMemento(java.lang.Object,
		 *      org.eclipse.ui.IMemento)
		 */
		protected void storeItemToMemento(Object item, IMemento element) {
//			IResource resource = (IResource) item;
//			ResourceFactory resourceFactory = new ResourceFactory(resource);
//			resourceFactory.saveState(element);
		}

	}
	
	private String getObjectPath(EObject object)
	{
		String path = "";
		
		EObject container = object.eContainer();
		
		if(object instanceof EDMDiagram)
			container = ((EDMDiagram)object).getElement();
		
		while(container!=null)
		{
			path = DatamodelerDiagramEditorPlugin.getInstance().getItemLabel(container) + path; //$NON-NLS-1$
			
			container = container.eContainer();
			
			if(container!=null)
				path = "/" + path;
		}

		return path;
	}
	
	private Object getObjectRoot(EObject object)
	{
		if(object instanceof EDMDiagram)
			object = ((EDMDiagram)object).getElement();
		
		while(object.eContainer()!=null)
			object = object.eContainer();

		return object;
	}

}
