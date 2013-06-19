package com.isb.datamodeler.ui.diagram.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TriggerListener;
import org.eclipse.emf.transaction.impl.TransactionImpl;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.ConnectorImpl;

import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey2EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey3EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.UniqueConstraintEditPart;
import com.isb.datamodeler.tables.ETable;

public class DataModelerDiagramEditorCleaner extends TriggerListener {
	
	private List<View> removedViews = new ArrayList<View>();
	
	private Transaction currentTransaction = null;
	
	private List<String> notPersistedEditPartIds;
	
	public DataModelerDiagramEditorCleaner()
	{
		super();
		
		notPersistedEditPartIds = new ArrayList<String>(3);
		notPersistedEditPartIds.add(Integer.toString(ColumnEditPart.VISUAL_ID));
		notPersistedEditPartIds.add(Integer.toString(UniqueConstraintEditPart.VISUAL_ID));
		notPersistedEditPartIds.add(Integer.toString(ForeignKey2EditPart.VISUAL_ID));
	}
	
	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event)
			throws RollbackException {
		
		if(currentTransaction!=((TransactionImpl)event.getTransaction()).getRoot())
		{
			currentTransaction = ((TransactionImpl)event.getTransaction()).getRoot();
			removedViews = new ArrayList<View>();
		}
		
		return super.transactionAboutToCommit(event);
	}

	@Override
	protected Command trigger(TransactionalEditingDomain domain,
			Notification notification) {
		
		if(notification.getEventType()==Notification.REMOVE)
		{
			CompoundCommand command = new CompoundCommand();
			boolean isContainment = false;
			
			if(notification.getFeature() instanceof EReference)
				isContainment = ((EReference)notification.getFeature()).isContainment();
			
			addDestroyViewsCommand(command, domain, (EObject)notification.getOldValue(), isContainment);
			
			return command.isEmpty()?null:command;
		}
		
		return null;
	}

	private void addDestroyViewsCommand(CompoundCommand command, TransactionalEditingDomain domain, EObject removedObject, boolean isContainmentFeature)
	{
		List<View> viewsToRemove = new ArrayList<View>();
		
		//Eliminamos los Shortcuts y Views de todos los diagramas
		for (Resource res: domain.getResourceSet().getResources())
		{
			TreeIterator<EObject> iterator = res.getAllContents(); 
			while(iterator.hasNext())
			{
				EObject child = iterator.next();
				
				if(!(child instanceof View))
					continue;
			
				View view = (View)child;
				
				boolean isRemovable = false;
				
				if(view.isSetElement() && view.getElement()==removedObject && isContainmentFeature) {
					// Al mover una tabla con relaciones entrantes o salientes no las refresca bien. 
					// Bugzilla 21894. 22350
					if (view.getElement() instanceof ETable) {						
						for(Setting setting: EcoreUtil.UsageCrossReferencer.find(view, removedObject.eResource()))
						{												
							if (setting.getEObject() instanceof ConnectorImpl) {
							
								ConnectorImpl connector = (ConnectorImpl) setting.getEObject();
									if (connector.getType().equals(Integer.toString(ForeignKeyEditPart.VISUAL_ID)) ||
											connector.getType().equals(Integer.toString(ForeignKey3EditPart.VISUAL_ID)))
									
										viewsToRemove.add(connector);								
							}
						}
					}				
					
					isRemovable = true;
				}
				
				if(notPersistedEditPartIds.contains(view.getType()) && !view.isSetElement())
					isRemovable = true;
				
				if (view.eContainer() instanceof Diagram && !view.isSetElement())
					isRemovable = true;
				
				if(isRemovable)
					viewsToRemove.add(view);

				//Eliminamos las notas que queden "huerfanas". Los NOTEATTACHMENT son las conexiones de las notas attacheadas.
				if (view.getType().equals(ViewType.NOTEATTACHMENT) && ((Edge)view).getTarget()==null)
					viewsToRemove.add(((Edge)view).getSource());

			}

		}
		
		
		
		viewsToRemove.removeAll(removedViews);
		
		if(!viewsToRemove.isEmpty())
			command.appendIfCanExecute(new DeleteCommand((TransactionalEditingDomain) domain, viewsToRemove));
		
		removedViews.addAll(viewsToRemove);
		
			
	}


}