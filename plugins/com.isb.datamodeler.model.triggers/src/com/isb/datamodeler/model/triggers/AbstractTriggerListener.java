package com.isb.datamodeler.model.triggers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.NotificationChainImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TriggerListener;
import org.eclipse.emf.transaction.impl.TransactionImpl;
import org.eclipse.emf.transaction.util.ConditionalRedoCommand;

public abstract class AbstractTriggerListener extends TriggerListener {
	
	public AbstractTriggerListener(NotificationFilter filter) {
		super(filter);
	}

	/**
	 * Implements the trigger callback by processing the <code>event</code>'s
	 * notifications one by one, delegating to the {@link #trigger} method for each to
	 * generate a command.  The commands created by the subclass are chained in
	 * the order that they are received from the subclass.
	 * 
	 * @return a composite of the commands returned by the subclass
	 *     implementation of the {@link #trigger} method
	 */
	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
		
		//Agrupamos las notificaciones en REMOVE_ALL, ADD_ALL etc..
		//Esto es para que al preguntar al usuario sobre las acciones a tomar, poder agrupar por tablas
		//y no preguntar por cada columna añadida o eliminada
		List<Notification> mergedNotifications = mergeNotifications(event.getNotifications());
		
		Command result = transactionAboutToCommit(mergedNotifications, event.getEditingDomain());
		
		//Recolectamos los triggers parametrizables
		List<IParametrizableTrigger> parametrizableTriggers = new ArrayList<IParametrizableTrigger>();

		if (result instanceof ConditionalRedoCommand.Compound)
		{
			for(Command trigger: ((ConditionalRedoCommand.Compound)result).getCommandList())
				if(trigger instanceof IParametrizableTrigger)
					parametrizableTriggers.add((IParametrizableTrigger)trigger);
			
		}else if(result instanceof IParametrizableTrigger)
			parametrizableTriggers.add((IParametrizableTrigger)result);

		
		Transaction rootTransaction = ((TransactionImpl)event.getTransaction()).getRoot();
		
		//Configuramos los triggers parametrizables
		DataModelerTriggersPlugin.getInstance().configure(parametrizableTriggers, rootTransaction);
		
		return result;
	}

	public Command transactionAboutToCommit(List<Notification> notifications, TransactionalEditingDomain domain) throws RollbackException {
		Command result = null;
		
		for (Notification next : notifications) {
			Command trigger = trigger(domain, next);
			if (trigger != null) {
				if (result == null) {
					result = trigger;
				} else {
					if (result instanceof ConditionalRedoCommand.Compound) {
						result = result.chain(trigger);
					} else {
						Command previous = result;
						result = new ConditionalRedoCommand.Compound();
						result.chain(previous);
						result.chain(trigger);
					}
				}
			}
		}
		
		return result;
	}

	private List<Notification> mergeNotifications(List<Notification> notifications)
	{
		NotificationChainImpl notificationChain = new NotificationChainImpl();
		
		for(Notification notification: notifications)
		{
			//Convertimos la notificacion en una FeatureENotificationImpl para que el merge incluya
			//el evento ADD (De lo contrario solo hará merge de los Remove)
			Notification newNotification = new FeatureMapUtil.FeatureENotificationImpl((InternalEObject)notification.getNotifier(), 
																					   notification.getEventType(), 
																					   (EStructuralFeature)notification.getFeature(), 
																					   notification.getOldValue(), 
																					   notification.getNewValue(),
																					   notification.getPosition(),
																					   notification.wasSet());
			notificationChain.add(newNotification);
		}
			
		
		return notificationChain;
	}
	
}
