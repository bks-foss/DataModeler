package com.isb.datamodeler.internal.ui.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;
import org.eclipse.ui.views.properties.IPropertySource;

import com.isb.datamodeler.compare.core.DataModelerCompareEditorInput;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.diagram.properties.DataModelerPropertySource;
import com.isb.datamodeler.diagram.util.DataModelerDiagramUtils;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.diagram.EDMDiagram;
import com.isb.datamodeler.ui.project.EProject;

/**
 * @generated
 */
public class DatamodelerDomainNavigatorContentProvider implements
ICommonContentProvider, IResourceChangeListener {

	/**
	 * @generated
	 */
	private AdapterFactoryContentProvider myAdapterFctoryContentProvier;

	/**
	 * @generated
	 */
	private static final Object[] EMPTY_ARRAY = new Object[0];

	/**
	 * @generated
	 */
	private Viewer myViewer;

	/**
	 * @generated
	 */
	private AdapterFactoryEditingDomain myEditingDomain;
	
	/**
	 * @generated
	 */
	private WorkspaceSynchronizer myWorkspaceSynchronizer;

	/**
	 * @generated
	 */
	private Runnable myViewerRefreshRunnable;
	
	/**
	 * @generated
	 */
	public DatamodelerDomainNavigatorContentProvider() {
		myAdapterFctoryContentProvier = new AdapterFactoryContentProvider(
				DatamodelerDiagramEditorPlugin.getInstance()
						.getItemProvidersAdapterFactory()){
			@Override
			/**
			 * Sobreescribimos este método para utilizar el mismo PropertySource que los diagramas
			 */
			protected IPropertySource createPropertySource(
					Object object,
					IItemPropertySource itemPropertySource) {
				return new DataModelerPropertySource(object, itemPropertySource);
			}
		};
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		myEditingDomain = (AdapterFactoryEditingDomain) editingDomain;
		// Esto está comentado para que permita realizar acciones y
		// modificar propiedades desde el navegador
		/*myEditingDomain.setResourceToReadOnlyMap(new HashMap() {
			public Object get(Object key) {
				if (!containsKey(key)) {
					put(key, Boolean.TRUE);
				}
				return super.get(key);
			}
		});*/
		myViewerRefreshRunnable = new Runnable() {
			public void run() {
				if (myViewer != null) {
					myViewer.refresh();
				}
			}
		};
		
		/* Listener para escuchar cambios en los recursos asociados al editing domain */
		myWorkspaceSynchronizer = new WorkspaceSynchronizer(
		         (TransactionalEditingDomain) editingDomain,
		         createSynchronizationDelegate());
		
		new DataModelerDomainNavigatorListener(editingDomain, this);
	}

	/**
	 * @generated
	 */
	public void dispose() {
		
		if (myViewer != null) {
			IWorkspace workspace = null;
			Object obj = myViewer.getInput();
			if (obj instanceof IWorkspace) {
				workspace = (IWorkspace) obj;
			} else if (obj instanceof IContainer) {
				workspace = ((IContainer) obj).getWorkspace();
			}
			if (workspace != null) {
				workspace.removeResourceChangeListener(this);
			}
		}
		
		myWorkspaceSynchronizer.dispose();
		myViewerRefreshRunnable = null;
		myViewer = null;
		unloadAllResources();
		// Este código ha sido modificado mediante la plantilla dinámica NavigatorContentProvider.xpt
		// Comentamos el dispose del editing domain porque ahora lo añadimos de forma estática.
		//((org.eclipse.emf.transaction.TransactionalEditingDomain) myEditingDomain).dispose();
		myEditingDomain = null;
	}
	
	private WorkspaceSynchronizer.Delegate createSynchronizationDelegate() {
		   return new WorkspaceSynchronizer.Delegate() {
		      public boolean handleResourceDeleted(Resource resource) {
		    	  
		    	  final String resourceName = resource.getURI().lastSegment().substring(0,resource.getURI().lastSegment().indexOf("."));	
		    	  final Resource fresource = resource;
		    	  
		    	  Display.getDefault().syncExec(new Runnable() {

						@Override
						public void run() {
							// Obtenemos los diagramas a borrar
							EList<EObject> resourceContents = fresource.getContents();
							List<Diagram> diagrams = new ArrayList<Diagram>();
							
							for(EObject content : resourceContents)
							{
								if(content instanceof Diagram)
								{
									Diagram diagram = (Diagram)content;
									diagrams.add(diagram);
								}
							}
															
							for (IEditorReference editorRef : DataModelerUI.getActivePage().getEditorReferences())
							{
								try {
									if(editorRef.getEditorInput() instanceof URIEditorInput)
									{
										URI editorURI = ((URIEditorInput)editorRef.getEditorInput()).getURI();
										for(Diagram diagram:diagrams)
										{
											URI diagramURI = EcoreUtil.getURI(diagram);
											if(diagramURI.equals(editorURI))											
												DataModelerUI.getActivePage().closeEditor(editorRef.getEditor(false), false);																							
										}
									} else if (editorRef.getId().equals("org.eclipse.compare.CompareEditor")) //$NON-NLS-1$
									{																						
										
										ModelCompareInput modelCompareInput = null; 
										
										if (editorRef.getEditorInput() != null)	
											modelCompareInput = ((DataModelerCompareEditorInput)editorRef.getEditorInput()).getPreparedInput();
										
										if (modelCompareInput != null) {
											Resource rightResource = modelCompareInput.getRightResource();
											Resource leftResource = modelCompareInput.getLeftResource();
											
											if (rightResource != null && rightResource.getURI().toString().contains(resourceName)) 
												DataModelerUI.getActivePage().closeEditor(editorRef.getEditor(false), false);																										
											
											else if (leftResource != null && leftResource.getURI().toString().contains(resourceName)) 
												DataModelerUI.getActivePage().closeEditor(editorRef.getEditor(false), false);																											
										}										
									}
								} catch (PartInitException e) {
									UtilsDataModelerUI.log(e, "handleResourceDeleted()");
								}
							}							
						}
						
					});
		    	
		    	  myEditingDomain.getResourceSet().getResources().remove(resource);		
		    	  
		          return true;
		      }
		      
		      public boolean handleResourceChanged(Resource resource) {		         

		         return true;
		      }
		      
		      public boolean handleResourceMoved(Resource resource, URI newURI) {		        		        
		         return true;
		      }
		      
		      public void dispose() {		        
		      }};
	}

	/**
	 * @generated
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		myViewer = viewer;
		
		IWorkspace oldWorkspace = null;
		IWorkspace newWorkspace = null;

		if (oldInput instanceof IWorkspace) {
			oldWorkspace = (IWorkspace) oldInput;
		} else if (oldInput instanceof IContainer) {
			oldWorkspace = ((IContainer) oldInput).getWorkspace();
		}

		if (newInput instanceof IWorkspace) {
			newWorkspace = (IWorkspace) newInput;
		} else if (newInput instanceof IContainer) {
			newWorkspace = ((IContainer) newInput).getWorkspace();
		}

		if (oldWorkspace != newWorkspace) {
			if (oldWorkspace != null) {
				oldWorkspace.removeResourceChangeListener(this);
			}
			if (newWorkspace != null) {
				newWorkspace.addResourceChangeListener(this,
						IResourceChangeEvent.POST_CHANGE);
			}
		}
	}

	/**
	 * @generated
	 */
	void unloadAllResources() {
		for (Resource nextResource : myEditingDomain.getResourceSet()
				.getResources()) {
			nextResource.unload();
		}
	}

	/**
	 * @generated
	 */
	void asyncRefresh() {
		if (myViewer != null && !myViewer.getControl().isDisposed()) {
			myViewer.getControl().getDisplay()
					.asyncExec(myViewerRefreshRunnable);
		}
	}

	/**
	 * @generated
	 */
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/**
	 * @generated
	 */
	public void restoreState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public void saveState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public void init(ICommonContentExtensionSite aConfig) {
	}

	/**
	 * @generated
	 */
	public Object[] getChildren(Object parentElement)
	{
		if(parentElement instanceof IWorkspaceRoot)
			return getWksRootChildren((IWorkspaceRoot)parentElement);
		
		// Si parentElement es un IProject de Data Modeler
		// tenemos que mostrar los esquema que contenga
		if(parentElement instanceof IProject)
		{
			EProject eproject = UtilsDataModelerUI.findEProject((IProject)parentElement);
			if(eproject!=null)
				return wrapEObjects(eproject.getSchemas().toArray(),
						parentElement);
			
		}
		
		if (parentElement instanceof IFile) {
			IFile file = (IFile) parentElement;
			URI fileURI = URI.createPlatformResourceURI(file.getFullPath()
					.toString(), true);
			Resource resource = myEditingDomain.getResourceSet().getResource(
					fileURI, true);
			return wrapEObjects(
					myAdapterFctoryContentProvier.getChildren(resource),
					parentElement);
		}

		if (parentElement instanceof DatamodelerDomainNavigatorItem) {
			return wrapEObjects(
					myAdapterFctoryContentProvier.getChildren(((DatamodelerDomainNavigatorItem) parentElement)
							.getEObject()), parentElement);
		}
		return EMPTY_ARRAY;
	}
	
	protected Object[] getWksRootChildren(IWorkspaceRoot wksRoot)
	{
		return wrapEObjects(UtilsDataModelerUI.findDataModelerEProjects().toArray(), wksRoot);
	}

	/**
	 * @generated
	 */
	public DatamodelerDomainNavigatorItem[] wrapEObjects(Object[] objects, Object parentElement) {
		Collection<DatamodelerDomainNavigatorItem> result = new ArrayList<DatamodelerDomainNavigatorItem>();
		for (int i = 0; i < objects.length; i++) {
			if(filter(objects[i], parentElement))
				result.add(new DatamodelerDomainNavigatorItem(
				(EObject) objects[i], parentElement,
				myAdapterFctoryContentProvier));
			
		}
		
		//  Para los esquemas sus hijos serán los diagramas
		if(parentElement instanceof DatamodelerDomainNavigatorItem)
		{
			EObject eobject = ((DatamodelerDomainNavigatorItem)parentElement).getEObject();
			if(eobject instanceof ESchema)
			{
				// Averiguar pq llega aqui proxys
				ESchema schema = (ESchema)eobject;

			    for (Diagram diagram : DataModelerDiagramUtils.getDiagrams(schema))
			    {
			    	ESchema diagramSchema = (ESchema)diagram.getElement();
 
			    	if(diagramSchema.equals(schema))
			    		result.add(new DatamodelerDomainNavigatorItem(
			    				diagram, parentElement,
			    				myAdapterFctoryContentProvier));
			    }
			}
		}
		
		return result.toArray(new DatamodelerDomainNavigatorItem[result.size()]);
	}
	
	private boolean filter (Object object, Object parent)
	{
		// No mostramos los diagramas como hijos del proyecto
		if(object instanceof Diagram && parent instanceof IFile)
			return false;
		// En esta versión de DM no se muestran las bases de datos
		if(object instanceof EDatabase)
			return false;
		
		if(parent instanceof DatamodelerDomainNavigatorItem)
		{
			EObject eobject = ((DatamodelerDomainNavigatorItem)parent).getEObject();
			// Para los diagramas no mostramos sus hijos
			if(eobject instanceof Diagram)
				return false;
		}
		return true;
	}

	/**
	 * Devuelve el Parent de un DatamodelerDomainNavigatorItem
	 * Es necesario para mostrar en el navegador un Diagrama cuando está activado el "Link With Editor"
	 * y no se ha cargado aun todos los elementos del arbol (No se han expandido)
	 */
	public Object getParent(Object element) {
		if (element instanceof DatamodelerDomainNavigatorItem) {
			DatamodelerDomainNavigatorItem item = (DatamodelerDomainNavigatorItem) element;
			EObject eObject = item.getEObject();
			EObject parent = eObject.eContainer();

			if(eObject instanceof EDMDiagram)
				parent = (ESchema)((EDMDiagram)eObject).getElement();

			
			if(parent!=null)
				return new DatamodelerDomainNavigatorItem(parent, null, null);
		}
		return null;
	}

	/**
	 * @generated
	 */
	public boolean hasChildren(Object element) {
		if(element instanceof IProject)
			return true;
		
		return element instanceof IFile || getChildren(element).length > 0;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		processDelta(event.getDelta());
		
	}
	
	/**
	 * Process the resource delta.
	 * 
	 * @param delta
	 */
	protected void processDelta(IResourceDelta delta) {		

		Control ctrl = myViewer.getControl();
		if (ctrl == null || ctrl.isDisposed()) {
			return;
		}
		
		
		final Collection runnables = new ArrayList();
		processDelta(delta, runnables);

		if (runnables.isEmpty()) {
			return;
		}

		//Are we in the UIThread? If so spin it until we are done
		if (ctrl.getDisplay().getThread() == Thread.currentThread()) {
			runUpdates(runnables);
		} else {
			ctrl.getDisplay().asyncExec(new Runnable(){
				/* (non-Javadoc)
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					//Abort if this happens after disposes
					Control ctrl = myViewer.getControl();
					if (ctrl == null || ctrl.isDisposed()) {
						return;
					}
					
					runUpdates(runnables);
				}
			});
		}

	}
	
	/**
	 * Process a resource delta. Add any runnables
	 */
	private void processDelta(IResourceDelta delta, Collection runnables) {
		//he widget may have been destroyed
		// by the time this is run. Check for this and do nothing if so.
		Control ctrl = myViewer.getControl();
		if (ctrl == null || ctrl.isDisposed()) {
			return;
		}

		// Get the affected resource
		final IResource resource = delta.getResource();
	
		// If any children have changed type, just do a full refresh of this
		// parent,
		// since a simple update on such children won't work,
		// and trying to map the change to a remove and add is too dicey.
		// The case is: folder A renamed to existing file B, answering yes to
		// overwrite B.
		IResourceDelta[] affectedChildren = delta
				.getAffectedChildren(IResourceDelta.CHANGED);
		for (int i = 0; i < affectedChildren.length; i++) {
			if ((affectedChildren[i].getFlags() & IResourceDelta.TYPE) != 0) {
				runnables.add(getRefreshRunnable(resource));
				return;
			}
		}

		// Check the flags for changes the Navigator cares about.
		// See ResourceLabelProvider for the aspects it cares about.
		// Notice we don't care about F_CONTENT or F_MARKERS currently.
		int changeFlags = delta.getFlags();
		if ((changeFlags & (IResourceDelta.OPEN | IResourceDelta.SYNC
				| IResourceDelta.TYPE | IResourceDelta.DESCRIPTION)) != 0) {
//			Runnable updateRunnable =  new Runnable(){
//				public void run() {
//					((StructuredViewer) viewer).update(resource, null);
//				}
//			};
//			runnables.add(updateRunnable);
			
			/* support the Closed Projects filter; 
			 * when a project is closed, it may need to be removed from the view.
			 */
			runnables.add(getRefreshRunnable(resource.getParent()));
		}
		// Replacing a resource may affect its label and its children
		if ((changeFlags & IResourceDelta.REPLACED) != 0) {
			runnables.add(getRefreshRunnable(resource));
			return;
		}


		// Handle changed children .
		for (int i = 0; i < affectedChildren.length; i++) {
			processDelta(affectedChildren[i], runnables);
		}

		// @issue several problems here:
		//  - should process removals before additions, to avoid multiple equal
		// elements in viewer
		//   - Kim: processing removals before additions was the indirect cause of
		// 44081 and its varients
		//   - Nick: no delta should have an add and a remove on the same element,
		// so processing adds first is probably OK
		//  - using setRedraw will cause extra flashiness
		//  - setRedraw is used even for simple changes
		//  - to avoid seeing a rename in two stages, should turn redraw on/off
		// around combined removal and addition
		//   - Kim: done, and only in the case of a rename (both remove and add
		// changes in one delta).

		IResourceDelta[] addedChildren = delta
				.getAffectedChildren(IResourceDelta.ADDED);
		IResourceDelta[] removedChildren = delta
				.getAffectedChildren(IResourceDelta.REMOVED);

		if (addedChildren.length == 0 && removedChildren.length == 0) {
			return;
		}

		final Object[] addedObjects;
		final Object[] removedObjects;

		// Process additions before removals as to not cause selection
		// preservation prior to new objects being added
		// Handle added children. Issue one update for all insertions.
		int numMovedFrom = 0;
		int numMovedTo = 0;
		if (addedChildren.length > 0) {
			addedObjects = new Object[addedChildren.length];
			for (int i = 0; i < addedChildren.length; i++) {
				addedObjects[i] = addedChildren[i].getResource();
				if ((addedChildren[i].getFlags() & IResourceDelta.MOVED_FROM) != 0) {
					++numMovedFrom;
				}
			}
		} else {
			addedObjects = new Object[0];
		}

		// Handle removed children. Issue one update for all removals.
		if (removedChildren.length > 0) {
			removedObjects = new Object[removedChildren.length];
			for (int i = 0; i < removedChildren.length; i++) {
				removedObjects[i] = removedChildren[i].getResource();
				if ((removedChildren[i].getFlags() & IResourceDelta.MOVED_TO) != 0) {
					++numMovedTo;
				}
			}
		} else {
			removedObjects = new Object[0];
		}
		// heuristic test for items moving within same folder (i.e. renames)
		final boolean hasRename = numMovedFrom > 0 && numMovedTo > 0;
		
		Runnable addAndRemove = new Runnable(){
			public void run() {
				if (myViewer instanceof AbstractTreeViewer) {
					AbstractTreeViewer treeViewer = (AbstractTreeViewer) myViewer;
					// Disable redraw until the operation is finished so we don't
					// get a flash of both the new and old item (in the case of
					// rename)
					// Only do this if we're both adding and removing files (the
					// rename case)
					if (hasRename) {
						treeViewer.getControl().setRedraw(false);
					}
					try {
						if (addedObjects.length > 0) {
							treeViewer.add(resource, addedObjects);
						}
						if (removedObjects.length > 0) {
							treeViewer.remove(removedObjects);
						}
					}
					finally {
						if (hasRename) {
							treeViewer.getControl().setRedraw(true);
						}
					}
				} else {
					((StructuredViewer) myViewer).refresh(resource);
				}
			}
		};
		runnables.add(addAndRemove);
	}

	/**
	 * Return a runnable for refreshing a resource.
	 * @param resource
	 * @return Runnable
	 */
	private Runnable getRefreshRunnable(final IResource resource) {
		return new Runnable(){
			public void run() {
				((StructuredViewer) myViewer).refresh(resource);
			}
		};
	}
	
	/**
	 * Run all of the runnables that are the widget updates
	 * @param runnables
	 */
	private void runUpdates(Collection runnables) {
		Iterator runnableIterator = runnables.iterator();
		while(runnableIterator.hasNext()){
			((Runnable)runnableIterator.next()).run();
		}
		
	}
}
