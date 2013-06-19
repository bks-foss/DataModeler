package com.isb.datamodeler.diagram.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetViewMutabilityCommand;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.policies.SchemaCanonicalEditPolicy;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramUpdater;
import com.isb.datamodeler.diagram.part.DatamodelerLinkDescriptor;
import com.isb.datamodeler.diagram.part.DatamodelerNodeDescriptor;
import com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.tables.ETablesPackage;

public class SchemaEdgesCanonicalEditPolicy extends SchemaCanonicalEditPolicy
{

private EPersistentTable _selectedTable;

public SchemaEdgesCanonicalEditPolicy(EPersistentTable ePersistentTable)
{
	_selectedTable = ePersistentTable;
}

@Override
protected EStructuralFeature getFeatureToSynchronize()
{
	return ETablesPackage.eINSTANCE.getBaseTable_Constraints();
}

@Override
protected void refreshSemantic()
{
	if (resolveSemanticElement() == null) {
		return;
	}
	LinkedList<IAdaptable> createdViews = new LinkedList<IAdaptable>();
	List<DatamodelerLinkDescriptor> childDescriptors = DatamodelerDiagramUpdater
			.getPersistentTable_2009ContainedLinks((View) getHost().getModel());
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
	for (Iterator<DatamodelerLinkDescriptor> descriptorsIterator = childDescriptors
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

private boolean isMyDiagramElement(View view)
{
	int visualID = DatamodelerVisualIDRegistry.getVisualID(view);
	return PersistentTableEditPart.VISUAL_ID == visualID;
}

@Override
protected Collection<IAdaptable> refreshConnections()
{
	Map<EObject, View> domain2NotationMap = new HashMap<EObject, View>();
	Collection<DatamodelerLinkDescriptor> linkDescriptors = collectAllLinks(
			getDiagram(), domain2NotationMap);
	Collection<View> existingLinks = new LinkedList<View>(getDiagram().getEdges());
	for (Iterator<View> linksIterator = existingLinks.iterator(); linksIterator
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
		if(link.getSource()!=null && link.getDestination()!=null)
		{
			if(link.getSource().equals(_selectedTable) || link.getDestination().equals(_selectedTable))
				selectedLinkDescriptors.add(link);
		}
	}
	
	return createConnections(selectedLinkDescriptors, domain2NotationMap);
}
}
