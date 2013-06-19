package com.isb.datamodeler.internal.ui.dialogs.search;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditor;
import com.isb.datamodeler.internal.ui.views.DataModelerView;
import com.isb.datamodeler.ui.DataModelerUI;

public final class ShowTableInDiagramHandler extends Action implements IHandler,
		IWorkbenchWindowActionDelegate {

	/**
	 * A collection of objects listening to changes to this manager. This
	 * collection is <code>null</code> if there are no listeners.
	 */
	private transient ListenerList listenerList = null;

	public final void addHandlerListener(final IHandlerListener listener) {
		if (listenerList == null) {
			listenerList = new ListenerList(ListenerList.IDENTITY);
		}

		listenerList.add(listener);
	}

	public final void dispose() {
		listenerList = null;
	}

	public final Object execute(final ExecutionEvent event)
			throws ExecutionException {
		
		final IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window == null) {
			throw new ExecutionException("no active workbench window"); //$NON-NLS-1$
		}

		final IWorkbenchPage page = window.getActivePage();
		
		
		if (page == null) {
			throw new ExecutionException("no active workbench page"); //$NON-NLS-1$
		}

		IEditorPart activeEditor = page.getActiveEditor();
		
		DatamodelerDiagramEditor diagramEditor = null;
		
		if(activeEditor!=null && activeEditor instanceof DatamodelerDiagramEditor)
			diagramEditor = (DatamodelerDiagramEditor)page.getActiveEditor();
		
		EModelElement result = queryFileResource();
		
		if(result==null)
			return null;

		if (diagramEditor!=null) {
			
			String elementID = EMFCoreUtil.getProxyID(result);
			
			List<?> tables = diagramEditor.getDiagramGraphicalViewer().findEditPartsForElement(elementID, PersistentTableEditPart.class);
			
			//Si es una tabla diagramada, la mostramos en el Diagrama abierto:
			if(tables.size()==1)
			{
				diagramEditor.getDiagramGraphicalViewer().select((EditPart)tables.get(0));
				diagramEditor.getDiagramGraphicalViewer().reveal((EditPart)tables.get(0));
				
				return null;
			}
			
		}

		//Si no esta en el diagrama abierto, o no hay ningun diagrama abierto, la mostramos en el navegador:
		for(IViewReference viewRef:page.getViewReferences())
		{
			if(viewRef.getId().equals(DataModelerView.VIEW_ID))
			{
				DatamodelerDomainNavigatorItem elenmentToReveal = new DatamodelerDomainNavigatorItem(result, result.eContainer(), null);
				DataModelerView view = (DataModelerView)viewRef.getView(false);
				if(view!=null)
					view.getCommonViewer().setSelection(new StructuredSelection(elenmentToReveal), true);
			}
		}
		
		return null;
	}

	public final void init(final IWorkbenchWindow window) {
		// Do nothing.
	}

	/**
	 * Query the user for the resources that should be opened
	 * 
	 * @return the resource that should be opened.
	 */
	private final EModelElement queryFileResource() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}
		final Shell parent = window.getShell();
		final IContainer input = ResourcesPlugin.getWorkspace().getRoot();

		final ShowTableInDiagramDialog dialog = new ShowTableInDiagramDialog(parent, input);
		final int resultCode = dialog.open();
		if (resultCode != Window.OK) {
			return null;
		}

		if(dialog.getResult()==null || dialog.getResult().length==0)
			return null;
		
		final EModelElement result = (EModelElement)dialog.getResult()[0];

		return result;
	}

	public final void removeHandlerListener(final IHandlerListener listener) {
		if (listenerList != null) {
			listenerList.remove(listener);

			if (listenerList.isEmpty()) {
				listenerList = null;
			}
		}
	}

	public final void run(final IAction action) {
		try {
			execute(new ExecutionEvent());
		} catch (final ExecutionException e) {
			DataModelerUI.log(e);
		}
	}

	public final void selectionChanged(final IAction action,
			final ISelection selection) {
		// Do nothing.
	}
}
