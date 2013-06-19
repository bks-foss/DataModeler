package com.isb.datamodeler.internal.ui.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IAggregateWorkingSet;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.navigator.framelist.Frame;
import org.eclipse.ui.internal.navigator.framelist.FrameList;
import org.eclipse.ui.internal.navigator.framelist.TreeFrame;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.navigator.CommonNavigator;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.diagram.navigator.DatamodelerNavigatorItem;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditor;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.ui.UtilsDataModelerUI;

public class DataModelerView extends CommonNavigator
{

	/**
	 * Provides a constant for the standard instance of the Common Navigator.
	 * 
	 * @see PlatformUI#getWorkbench()
	 * @see IWorkbench#getActiveWorkbenchWindow()
	 * @see IWorkbenchWindow#getActivePage()
	 * 
	 * @see IWorkbenchPage#findView(String)
	 * @see IWorkbenchPage#findViewReference(String)
	 */
	public static final String VIEW_ID = "com.isb.datamodeler.internal.ui.views.DataModelerView";

	/**
	 * @since 3.4
	 */
	public static final int WORKING_SETS = 0;

	/**
	 * @since 3.4
	 */
	public static final int PROJECTS = 1;

	public static final String GROUP_NEW = "group.new";
	public static final String GROUP_EDIT = "group.edit"; //$NON-NLS-1$


	private int rootMode;
	
	/**
	 * Used only in the case of top level = PROJECTS and only when some
	 * working sets are selected. 
	 */
	private String workingSetLabel;

	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		
		if (!false)
			getCommonViewer().setMapper(new ResourceToItemsMapper(getCommonViewer()));
	}	

	/**
	 * The superclass does not deal with the content description, handle it
	 * here.
	 * 
	 * @noreference
	 */
	public void updateTitle() {
		super.updateTitle();
		Object input = getCommonViewer().getInput();

		if (input == null || input instanceof IAggregateWorkingSet) {
			setContentDescription(""); //$NON-NLS-1$
			return;
		}

		if (!(input instanceof IResource)) {
			if (input instanceof IAdaptable) {
				IWorkbenchAdapter wbadapter = (IWorkbenchAdapter) ((IAdaptable) input)
						.getAdapter(IWorkbenchAdapter.class);
				if (wbadapter != null) {
					setContentDescription(wbadapter.getLabel(input));
					return;
				}
			}
			setContentDescription(input.toString());
			return;
		}

		IResource res = (IResource) input;
		setContentDescription(res.getName());
	}

	/**
	 * Returns the tool tip text for the given element.
	 * 
	 * @param element
	 *            the element
	 * @return the tooltip
	 * @noreference
	 */
	public String getFrameToolTipText(Object element) {
		String result;
		if (!(element instanceof IResource)) {
			if (element instanceof IAggregateWorkingSet) {
				result = Messages.bind("DataModelerView_workingSetModel");
			} else if (element instanceof IWorkingSet) {
				result = ((IWorkingSet) element).getLabel();
			} else {
				result = super.getFrameToolTipText(element);
			}
		} else {
			IPath path = ((IResource) element).getFullPath();
			if (path.isRoot()) {
				result = Messages.bind("DataModelerView_workspace");
			} else {
				result = path.makeRelative().toString();
			}
		}

		if (rootMode == PROJECTS) {
			if (workingSetLabel == null)
				return result;
			if (result.length() == 0)
				return Messages.bind("DataModelerView_toolTip",
						new String[] { workingSetLabel });
			return Messages.bind("DataModelerView_toolTip2", new String[] {
					result, workingSetLabel });
		}

		// Working set mode. During initialization element and viewer can
		// be null.
		if (element != null && !(element instanceof IWorkingSet)
				&& getCommonViewer() != null) {
			FrameList frameList = getCommonViewer().getFrameList();
			// Happens during initialization
			if (frameList == null)
				return result;
			int index = frameList.getCurrentIndex();
			IWorkingSet ws = null;
			while (index >= 0) {
				Frame frame = frameList.getFrame(index);
				if (frame instanceof TreeFrame) {
					Object input = ((TreeFrame) frame).getInput();
					if (input instanceof IWorkingSet && !(input instanceof IAggregateWorkingSet)) {
						ws = (IWorkingSet) input;
						break;
					}
				}
				index--;
			}
			if (ws != null) {
				return Messages.bind("DataModelerView_toolTip3",
						new String[] { ws.getLabel(), result });
			}
			return result;
		}
		return result;

	}

	/**
	 * @param mode
	 * @noreference This method is not intended to be referenced by clients.
	 * @since 3.4
	 */
	public void setRootMode(int mode) {
		rootMode = mode;
	}

	/**
	 * @return the root mode
	 * @noreference This method is not intended to be referenced by clients.
	 * @since 3.4
	 */
	public int getRootMode() {
		return rootMode;
	}

	/**
	 * @param label
	 * @noreference This method is not intended to be referenced by clients.
	 * @since 3.4
	 */
	public void setWorkingSetLabel(String label) {
		workingSetLabel = label;
	}

	/**
	 * @return the working set label
	 * @noreference This method is not intended to be referenced by clients.
	 * @since 3.4
	 */
	public String getWorkingSetLabel() {
		return workingSetLabel;
	}
	
//	@Override
//	public Object getAdapter(Class adapter)
//	{
//		// Esto es necesario para mostrar las propiedades con pestañas.
//		// Lo que hacemos es reutilizar el contributor generado por gmf
//		if (adapter == IPropertySheetPage.class)
//	        return new TabbedPropertySheetPage(new ITabbedPropertySheetPageContributor() {
//				
//				@Override
//				public String getContributorId()
//				{
//					return "com.isb.datamodeler.ui.diagram"; //$NON-NLS-1$
//				}
//				
//			}, false);
//		
//		if(adapter == IEditingDomainProvider.class)
//			return domainProvider;
//		/*
//		 * Hemos creado el TabbedPropertySheetPage con el parámetro showTitleBar a false.
//		 * Si quisiésemos mostrar esa barra de información habría que:
//		 * - pasarle true al constructor
//		 * - modificar la clase DatamodelerSheetLabelProvider porque ahora no tiene en cuenta
//		 *   los elementos del navegador para mostrar el texto y las imágenes
//		 * - para ello, probablemente habría que modificar la plantilla:
//		 *   com.isb.datamodeler.ui.gmf\templates_original\xpt\propsheet\LabelProvider.xpt
//		 */
//		
//		return super.getAdapter(adapter);
//	}
	
//    /**
//     * My editing domain provider.
//     */
//    private IEditingDomainProvider domainProvider = new IEditingDomainProvider() {
//
//        public EditingDomain getEditingDomain() {
//            return DataModelerUtils.getDataModelerEditingDomain();
//        }
//    };
	
	
	@Override
	public void handleDoubleClick(DoubleClickEvent event)
	{		
		ISelection selection = event.getSelection();
		if(selection instanceof ITreeSelection)
		{
			Object object = ((ITreeSelection)selection).getFirstElement();
			if(object instanceof DatamodelerDomainNavigatorItem)
			{
				EObject eobject = ((DatamodelerDomainNavigatorItem)object).getEObject();
				if(eobject instanceof Diagram)
				{
					Diagram diagram = (Diagram)eobject;							
					
					try {
						UtilsDataModelerUI.openDiagram(EcoreUtil.getURI(diagram), diagram.getName());
					} catch (PartInitException e) {
						DatamodelerDiagramEditorPlugin.getInstance().logError(
								"Unable to open editor", e); //$NON-NLS-1$
					}
				}else
				{
					//Si hacemos doble click sobre una tabla diagramada en el diagrama activo, la mostramos:
					
					IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					final IWorkbenchPage page = ww.getActivePage();
					
					if (page!=null && page.getActiveEditor() instanceof DatamodelerDiagramEditor) {
						
						DatamodelerDiagramEditor diagramEditor = (DatamodelerDiagramEditor)page.getActiveEditor();
						
						String elementID = EMFCoreUtil.getProxyID(eobject);
						
						List<?> tables = diagramEditor.getDiagramGraphicalViewer().findEditPartsForElement(elementID, PersistentTableEditPart.class);
						
						//Si es una tabla diagramada, la mostramos en el Diagrama abierto:
						if(tables.size()==1)
						{
							diagramEditor.getDiagramGraphicalViewer().select((EditPart)tables.get(0));
							diagramEditor.getDiagramGraphicalViewer().reveal((EditPart)tables.get(0));
						}
						
					}

				}
			}
		}
	}
	
	@Override
	public void selectReveal(ISelection selection) {
		
		ISelection transformedSelection = selection;
		
		if(selection instanceof IStructuredSelection)
			transformedSelection = transformSelection((IStructuredSelection)selection);	
		
		super.selectReveal(transformedSelection);
	}

	private ISelection transformSelection(IStructuredSelection selection) {
		Collection<Object> transformedSelection = new ArrayList<Object>(selection.size());
		
		for(Object selectedObject:selection.toList())
			if(selectedObject instanceof DatamodelerNavigatorItem)
			{
				EObject obj = ((DatamodelerNavigatorItem)selectedObject).getView();
				transformedSelection.add(new DatamodelerDomainNavigatorItem(obj, null, null));
			}else if(selectedObject instanceof SchemaEditPart)
			{
				EObject obj = ((SchemaEditPart)selectedObject).getNotationView();
				transformedSelection.add(new DatamodelerDomainNavigatorItem(obj, null, null));
			}			
			else if(selectedObject instanceof IGraphicalEditPart)
			{
				EObject obj = ((IGraphicalEditPart)selectedObject).resolveSemanticElement();
				transformedSelection.add(new DatamodelerDomainNavigatorItem(obj, null, null));
			}
			else
				transformedSelection.add(selectedObject);
				
		
				
		return new StructuredSelection(transformedSelection.toArray());
	}

}
