package com.isb.datamodeler.internal.ui.views;

import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class DataModelerDomainNavigatorListener extends ResourceSetListenerImpl {
	
	/**
	 * @generated
	 */
	private DatamodelerDomainNavigatorContentProvider contentProvider;
	
	public DataModelerDomainNavigatorListener(TransactionalEditingDomain editingDomain, DatamodelerDomainNavigatorContentProvider contentProvider)
	{
		editingDomain.addResourceSetListener(this);
		
		this.contentProvider = contentProvider;
	}

	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event) {
		contentProvider.asyncRefresh();
	}
	
}
