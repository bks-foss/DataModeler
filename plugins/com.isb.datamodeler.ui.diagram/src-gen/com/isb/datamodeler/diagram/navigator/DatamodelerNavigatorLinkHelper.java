package com.isb.datamodeler.diagram.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.ILinkHelper;
import org.eclipse.ui.part.FileEditorInput;

import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;

/**
 * @generated
 */
public class DatamodelerNavigatorLinkHelper implements ILinkHelper {

	/**
	 * @generated
	 */
	private static IEditorInput getEditorInput(Diagram diagram) {
		Resource diagramResource = diagram.eResource();
		for (EObject nextEObject : diagramResource.getContents()) {
			if (nextEObject == diagram) {
				return new FileEditorInput(
						WorkspaceSynchronizer.getFile(diagramResource));
			}
			if (nextEObject instanceof Diagram) {
				break;
			}
		}
		URI uri = EcoreUtil.getURI(diagram);
		String editorName = uri.lastSegment() + '#'
				+ diagram.eResource().getContents().indexOf(diagram);
		IEditorInput editorInput = new URIEditorInput(uri, editorName);
		return editorInput;
	}

	/**
	 * (Modified template: NavigatorLinkHelper.xpt)
	 * @generated
	 */
	public IStructuredSelection findSelection(IEditorInput anInput) {
		//If there are EditParts selected in an open IDiagramWorkbenchPart we return this selection:
		IEditorPart activeEditor = getActiveEditor(anInput);

		if (activeEditor != null
				&& activeEditor instanceof IDiagramWorkbenchPart)
			return (IStructuredSelection) ((IDiagramWorkbenchPart) activeEditor)
					.getDiagramGraphicalViewer().getSelection();

		IDiagramDocument document = DatamodelerDiagramEditorPlugin
				.getInstance().getDocumentProvider()
				.getDiagramDocument(anInput);
		if (document == null) {
			return StructuredSelection.EMPTY;
		}
		Diagram diagram = document.getDiagram();
		if (diagram == null || diagram.eResource() == null) {
			return StructuredSelection.EMPTY;
		}
		IFile file = WorkspaceSynchronizer.getFile(diagram.eResource());
		if (file != null) {
			DatamodelerNavigatorItem item = new DatamodelerNavigatorItem(
					diagram, file, false);
			return new StructuredSelection(item);
		}
		return StructuredSelection.EMPTY;
	}

	/**
	 * (Modified template: NavigatorLinkHelper.xpt)
	 * @generated
	 */
	private IEditorPart getActiveEditor(IEditorInput anInput) {
		final IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		if (window == null)
			return null;

		final IWorkbenchPage page = window.getActivePage();

		if (page == null)
			return null;

		IEditorPart activeEditor = page.getActiveEditor();

		if (activeEditor != null && activeEditor.getEditorInput() == anInput)
			return activeEditor;

		return null;
	}

	/**
	 * @generated
	 */
	public void activateEditor(IWorkbenchPage aPage,
			IStructuredSelection aSelection) {
		if (aSelection == null || aSelection.isEmpty()) {
			return;
		}
		if (false == aSelection.getFirstElement() instanceof DatamodelerAbstractNavigatorItem) {
			return;
		}

		DatamodelerAbstractNavigatorItem abstractNavigatorItem = (DatamodelerAbstractNavigatorItem) aSelection
				.getFirstElement();
		View navigatorView = null;
		if (abstractNavigatorItem instanceof DatamodelerNavigatorItem) {
			navigatorView = ((DatamodelerNavigatorItem) abstractNavigatorItem)
					.getView();
		} else if (abstractNavigatorItem instanceof DatamodelerNavigatorGroup) {
			DatamodelerNavigatorGroup navigatorGroup = (DatamodelerNavigatorGroup) abstractNavigatorItem;
			if (navigatorGroup.getParent() instanceof DatamodelerNavigatorItem) {
				navigatorView = ((DatamodelerNavigatorItem) navigatorGroup
						.getParent()).getView();
			} else if (navigatorGroup.getParent() instanceof IAdaptable) {
				navigatorView = (View) ((IAdaptable) navigatorGroup.getParent())
						.getAdapter(View.class);
			}
		}
		if (navigatorView == null) {
			return;
		}
		IEditorInput editorInput = getEditorInput(navigatorView.getDiagram());
		IEditorPart editor = aPage.findEditor(editorInput);
		if (editor == null) {
			return;
		}
		aPage.bringToTop(editor);
		if (editor instanceof DiagramEditor) {
			DiagramEditor diagramEditor = (DiagramEditor) editor;
			ResourceSet diagramEditorResourceSet = diagramEditor
					.getEditingDomain().getResourceSet();
			EObject selectedView = diagramEditorResourceSet.getEObject(
					EcoreUtil.getURI(navigatorView), true);
			if (selectedView == null) {
				return;
			}
			GraphicalViewer graphicalViewer = (GraphicalViewer) diagramEditor
					.getAdapter(GraphicalViewer.class);
			EditPart selectedEditPart = (EditPart) graphicalViewer
					.getEditPartRegistry().get(selectedView);
			if (selectedEditPart != null) {
				graphicalViewer.select(selectedEditPart);
			}
		}
	}

}
