package com.isb.datamodeler.diagram.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetViewMutabilityCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey3EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramUpdater;
import com.isb.datamodeler.diagram.part.DatamodelerLinkDescriptor;
import com.isb.datamodeler.diagram.part.DatamodelerNodeDescriptor;
import com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EPersistentTable;

/**
 * @see SchemaCanonicalEditPolicy
 */
public class RestoreTablesCanonicalEditPolicy extends CanonicalEditPolicy {

	private PersistentTableEditPart _tableEditPart; 
	private EPersistentTable _pTable;
	private List<EPersistentTable> _tablesInDiagram = new ArrayList<EPersistentTable>();
	
	public RestoreTablesCanonicalEditPolicy(Diagram diagram , PersistentTableEditPart tableEditPart) 
	{
		_tableEditPart = tableEditPart;
		_pTable = (EPersistentTable)((NodeImpl)_tableEditPart.getModel()).getElement();
		
		for(EObject object : diagram.eContents())
		{
			if(object instanceof NodeImpl)
			{
				NodeImpl node = (NodeImpl)object;
				if(node.getElement() instanceof EPersistentTable)
				{
					_tablesInDiagram.add((EPersistentTable)node.getElement());
				}
			}
		}
	}
	/**
	 * 
	 */
	protected void refreshOnActivate() {
		// Need to activate editpart children before invoking the canonical refresh for EditParts to add event listeners
		List<?> c = getHost().getChildren();
		for (int i = 0; i < c.size(); i++) {
			((EditPart) c.get(i)).activate();
		}
		super.refreshOnActivate();
	}

	/**
	 * 
	 */
	protected EStructuralFeature getFeatureToSynchronize() {
		return ESchemaPackage.eINSTANCE.getSchema_Tables();
	}

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	protected List getSemanticChildrenList() {
		View viewObject = (View) getHost().getModel();
		LinkedList<EObject> result = new LinkedList<EObject>();
		List<DatamodelerNodeDescriptor> childDescriptors = DatamodelerDiagramUpdater
				.getSchema_1000SemanticChildren(viewObject);
		for (DatamodelerNodeDescriptor d : childDescriptors) {
			result.add(d.getModelElement());
		}
		return result;
	}

	/**
	 * 
	 */
	protected boolean isOrphaned(Collection<EObject> semanticChildren,
			final View view) {
		if (view.getEAnnotation("Shortcut") != null) { //$NON-NLS-1$
			return DatamodelerDiagramUpdater.isShortcutOrphaned(view);
		}
		return isMyDiagramElement(view)
				&& !semanticChildren.contains(view.getElement());
	}

	/**
	 * 
	 */
	private boolean isMyDiagramElement(View view) {
		return PersistentTableEditPart.VISUAL_ID == DatamodelerVisualIDRegistry
				.getVisualID(view);
	}

	/**
	 * Modificamos el metodo respecto de SchemaCanonicalEditPolicy para que solo añada las tablas
	 * salientes de la tabla seleccionada y que además añada las tablas externas.
	 */
	protected void refreshSemantic() {
		if (resolveSemanticElement() == null) {
			return;
		}
		LinkedList<IAdaptable> createdViews = new LinkedList<IAdaptable>();
		// Obtenemos todos los elementos del esquema
		List<DatamodelerNodeDescriptor> childDescriptors = DatamodelerDiagramUpdater
				.getSchema_1000SemanticChildren((View) getHost().getModel());
		// Filtramos para que solo añada aquellas tablas con las que la tabla seleccionada tenga relacion saliente
		List<EBaseTable> refTables = new ArrayList<EBaseTable>();
		if(((NodeImpl)_tableEditPart.getModel()).getElement() instanceof EPersistentTable)
		{
			EPersistentTable pTable = (EPersistentTable)((NodeImpl)_tableEditPart.getModel()).getElement();
			for(EForeignKey fk : pTable.getForeignKeys())
			{
				EBaseTable parentTable = fk.getParentTable();
				refTables.add(parentTable);
			}
		}
		List<DatamodelerNodeDescriptor> newChildDescriptors = new ArrayList<DatamodelerNodeDescriptor>();
		newChildDescriptors.addAll(childDescriptors);
		for (DatamodelerNodeDescriptor d : newChildDescriptors) {
			if(d.getModelElement() instanceof EPersistentTable )
			{
				EPersistentTable pt = (EPersistentTable)d.getModelElement();
				if( !pt.equals(((NodeImpl)_tableEditPart.getModel()).getElement()) && !refTables.contains(pt))
					childDescriptors.remove(d);
			}
		}
		
		// Añadimos a la lista de descriptors las tablas externas que estuvieran relacionadas
		String capability = _pTable.getSchema().getCapability();
		for(EBaseTable rTable : refTables)
		{
			if(!rTable.getSchema().getCapability().equals(capability))
			{
				childDescriptors.add(new DatamodelerNodeDescriptor(rTable , PersistentTableEditPart.VISUAL_ID));
			}
			else if(!rTable.getSchema().equals(_pTable.getSchema()))
			{
				childDescriptors.add(new DatamodelerNodeDescriptor(rTable , PersistentTableEditPart.VISUAL_ID));
			}
		}
		// Añadimos a la lista de descriptors las tablas que estaban en el diagrama
		for(EPersistentTable t : _tablesInDiagram)
		{
			childDescriptors.add(new DatamodelerNodeDescriptor(t , PersistentTableEditPart.VISUAL_ID));
		}
		
		LinkedList<View> orphaned = new LinkedList<View>();
		// we care to check only views we recognize as ours
		LinkedList<View> knownViewChildren = new LinkedList<View>();
		for (View v : getViewChildren()) {
			if (isMyDiagramElement(v)) {
				knownViewChildren.add(v);
			}
			if (v.getEAnnotation("Shortcut") != null && DatamodelerDiagramUpdater.isShortcutOrphaned(v)) { //$NON-NLS-1$
				orphaned.add(v);
			}
		}
		// alternative to #cleanCanonicalSemanticChildren(getViewChildren(), semanticChildren)
		//
		// iteration happens over list of desired semantic elements, trying to find best matching View, while original CEP
		// iterates views, potentially losing view (size/bounds) information - i.e. if there are few views to reference same EObject, only last one 
		// to answer isOrphaned == true will be used for the domain element representation, see #cleanCanonicalSemanticChildren()
		for (Iterator<DatamodelerNodeDescriptor> descriptorsIterator = childDescriptors
				.iterator(); descriptorsIterator.hasNext();) {
			DatamodelerNodeDescriptor next = descriptorsIterator.next();
			String hint = DatamodelerVisualIDRegistry.getType(next
					.getVisualID());
			LinkedList<View> perfectMatch = new LinkedList<View>(); // both semanticElement and hint match that of NodeDescriptor
			for (View childView : getViewChildren()) {
				EObject semanticElement = childView.getElement();
				if (next.getModelElement().equals(semanticElement)) {
					if (hint.equals(childView.getType())) {
						perfectMatch.add(childView);
						// actually, can stop iteration over view children here, but
						// may want to use not the first view but last one as a 'real' match (the way original CEP does
						// with its trick with viewToSemanticMap inside #cleanCanonicalSemanticChildren
					}
				}
			}
			if (perfectMatch.size() > 0) {
				descriptorsIterator.remove(); // precise match found no need to create anything for the NodeDescriptor
				// use only one view (first or last?), keep rest as orphaned for further consideration
				knownViewChildren.remove(perfectMatch.getFirst());
			}
		}
		// those left in knownViewChildren are subject to removal - they are our diagram elements we didn't find match to,
		// or those we have potential matches to, and thus need to be recreated, preserving size/location information.
		orphaned.addAll(knownViewChildren);
		//
		ArrayList<CreateViewRequest.ViewDescriptor> viewDescriptors = new ArrayList<CreateViewRequest.ViewDescriptor>(
				childDescriptors.size());
		for (DatamodelerNodeDescriptor next : childDescriptors) {
			String hint = DatamodelerVisualIDRegistry.getType(next
					.getVisualID());
			IAdaptable elementAdapter = new CanonicalElementAdapter(
					next.getModelElement(), hint);
			CreateViewRequest.ViewDescriptor descriptor = new CreateViewRequest.ViewDescriptor(
					elementAdapter, Node.class, hint, ViewUtil.APPEND, false,
					host().getDiagramPreferencesHint());
			viewDescriptors.add(descriptor);
		}

		boolean changed = deleteViews(orphaned.iterator());
		//
		CreateViewRequest request = getCreateViewRequest(viewDescriptors);
		Command cmd = getCreateViewCommand(request);
		if (cmd != null && cmd.canExecute()) {
			SetViewMutabilityCommand.makeMutable(
					new EObjectAdapter(host().getNotationView())).execute();
			executeCommand(cmd);
			@SuppressWarnings("unchecked")
			List<IAdaptable> nl = (List<IAdaptable>) request.getNewObject();
			createdViews.addAll(nl);
		}
		if (changed || createdViews.size() > 0) {
			postProcessRefreshSemantic(createdViews);
		}

		Collection<IAdaptable> createdConnectionViews = refreshConnections();

		if (createdViews.size() > 1) {
			// perform a layout of the container
			DeferredLayoutCommand layoutCmd = new DeferredLayoutCommand(host()
					.getEditingDomain(), createdViews, host());
			executeCommand(new ICommandProxy(layoutCmd));
		}

		createdViews.addAll(createdConnectionViews);

		makeViewsImmutable(createdViews);
	}

	/**
	 * 
	 */
	protected Collection<IAdaptable> refreshConnections() {
		Map<EObject, View> domain2NotationMap = new HashMap<EObject, View>();
		Collection<DatamodelerLinkDescriptor> linkDescriptors = collectAllLinks(
				getDiagram(), domain2NotationMap);
		Collection existingLinks = new LinkedList(getDiagram().getEdges());
		for (Iterator linksIterator = existingLinks.iterator(); linksIterator
				.hasNext();) {
			Edge nextDiagramLink = (Edge) linksIterator.next();
			int diagramLinkVisualID = DatamodelerVisualIDRegistry
					.getVisualID(nextDiagramLink);
			if (diagramLinkVisualID == -1) {
				if (nextDiagramLink.getSource() != null
						&& nextDiagramLink.getTarget() != null) {
					linksIterator.remove();
				}
				continue;
			}
			EObject diagramLinkObject = nextDiagramLink.getElement();
			EObject diagramLinkSrc = nextDiagramLink.getSource().getElement();
			EObject diagramLinkDst = nextDiagramLink.getTarget().getElement();
			for (Iterator<DatamodelerLinkDescriptor> linkDescriptorsIterator = linkDescriptors
					.iterator(); linkDescriptorsIterator.hasNext();) {
				DatamodelerLinkDescriptor nextLinkDescriptor = linkDescriptorsIterator
						.next();
				if (diagramLinkObject == nextLinkDescriptor.getModelElement()
						&& diagramLinkSrc == nextLinkDescriptor.getSource()
						&& diagramLinkDst == nextLinkDescriptor
								.getDestination()
						&& diagramLinkVisualID == nextLinkDescriptor
								.getVisualID()) {
					linksIterator.remove();
					linkDescriptorsIterator.remove();
					break;
				}
			}
		}
		deleteViews(existingLinks.iterator());
		Collection<DatamodelerLinkDescriptor> selectedLinkDescriptors = new LinkedList<DatamodelerLinkDescriptor>();
		for (DatamodelerLinkDescriptor link : linkDescriptors)
		{
			// Sólo queremos añadir los links que pertenezcan a la tabla seleccionada
			if(link.getSource().equals(_pTable) || link.getDestination().equals(_pTable))
				selectedLinkDescriptors.add(link);
		}
		return createConnections(selectedLinkDescriptors, domain2NotationMap);
	}

	/**
	 * 
	 */
	protected Collection<DatamodelerLinkDescriptor> collectAllLinks(View view,
			Map<EObject, View> domain2NotationMap) {
		if (!SchemaEditPart.MODEL_ID.equals(DatamodelerVisualIDRegistry
				.getModelID(view))) {
			return Collections.emptyList();
		}
		LinkedList<DatamodelerLinkDescriptor> result = new LinkedList<DatamodelerLinkDescriptor>();
		switch (DatamodelerVisualIDRegistry.getVisualID(view)) {
		case SchemaEditPart.VISUAL_ID: {
			if (!domain2NotationMap.containsKey(view.getElement())) {
				result.addAll(DatamodelerDiagramUpdater
						.getSchema_1000ContainedLinks(view));
			}
			if (!domain2NotationMap.containsKey(view.getElement())
					|| view.getEAnnotation("Shortcut") == null) { //$NON-NLS-1$
				domain2NotationMap.put(view.getElement(), view);
			}
			break;
		}
		case PersistentTableEditPart.VISUAL_ID: {
			if (!domain2NotationMap.containsKey(view.getElement())) {
				result.addAll(DatamodelerDiagramUpdater
						.getPersistentTable_2009ContainedLinks(view));
			}
			if (!domain2NotationMap.containsKey(view.getElement())
					|| view.getEAnnotation("Shortcut") == null) { //$NON-NLS-1$
				domain2NotationMap.put(view.getElement(), view);
			}
			break;
		}
		case ForeignKeyEditPart.VISUAL_ID: {
			if (!domain2NotationMap.containsKey(view.getElement())) {
				result.addAll(DatamodelerDiagramUpdater
						.getForeignKey_4001ContainedLinks(view));
			}
			if (!domain2NotationMap.containsKey(view.getElement())
					|| view.getEAnnotation("Shortcut") == null) { //$NON-NLS-1$
				domain2NotationMap.put(view.getElement(), view);
			}
			break;
		}
		case ForeignKey3EditPart.VISUAL_ID: {
			if (!domain2NotationMap.containsKey(view.getElement())) {
				result.addAll(DatamodelerDiagramUpdater
						.getForeignKey_4002ContainedLinks(view));
			}
			if (!domain2NotationMap.containsKey(view.getElement())
					|| view.getEAnnotation("Shortcut") == null) { //$NON-NLS-1$
				domain2NotationMap.put(view.getElement(), view);
			}
			break;
		}
		}
		for (Iterator children = view.getChildren().iterator(); children
				.hasNext();) {
			result.addAll(collectAllLinks((View) children.next(),
					domain2NotationMap));
		}
		for (Iterator edges = view.getSourceEdges().iterator(); edges.hasNext();) {
			result.addAll(collectAllLinks((View) edges.next(),
					domain2NotationMap));
		}
		return result;
	}

	/**
	 * 
	 */
	protected Collection<IAdaptable> createConnections(
			Collection<DatamodelerLinkDescriptor> linkDescriptors,
			Map<EObject, View> domain2NotationMap) {
		LinkedList<IAdaptable> adapters = new LinkedList<IAdaptable>();
		for (DatamodelerLinkDescriptor nextLinkDescriptor : linkDescriptors) {
			EditPart sourceEditPart = getEditPart(
					nextLinkDescriptor.getSource(), domain2NotationMap);
			EditPart targetEditPart = getEditPart(
					nextLinkDescriptor.getDestination(), domain2NotationMap);
			if (sourceEditPart == null || targetEditPart == null) {
				continue;
			}
			CreateConnectionViewRequest.ConnectionViewDescriptor descriptor = new CreateConnectionViewRequest.ConnectionViewDescriptor(
					nextLinkDescriptor.getSemanticAdapter(),
					DatamodelerVisualIDRegistry.getType(nextLinkDescriptor
							.getVisualID()), ViewUtil.APPEND, false,
					((IGraphicalEditPart) getHost())
							.getDiagramPreferencesHint());
			CreateConnectionViewRequest ccr = new CreateConnectionViewRequest(
					descriptor);
			ccr.setType(RequestConstants.REQ_CONNECTION_START);
			ccr.setSourceEditPart(sourceEditPart);
			sourceEditPart.getCommand(ccr);
			ccr.setTargetEditPart(targetEditPart);
			ccr.setType(RequestConstants.REQ_CONNECTION_END);
			Command cmd = targetEditPart.getCommand(ccr);
			if (cmd != null && cmd.canExecute()) {
				executeCommand(cmd);
				IAdaptable viewAdapter = (IAdaptable) ccr.getNewObject();
				if (viewAdapter != null) {
					adapters.add(viewAdapter);
				}
			}
		}
		return adapters;
	}

	/**
	 * 
	 */
	private EditPart getEditPart(EObject domainModelElement,
			Map<EObject, View> domain2NotationMap) {
		View view = (View) domain2NotationMap.get(domainModelElement);
		if (view != null) {
			return (EditPart) getHost().getViewer().getEditPartRegistry()
					.get(view);
		}
		return null;
	}

	/**
	 * 
	 */
	protected Diagram getDiagram() {
		return ((View) getHost().getModel()).getDiagram();
	}
}
