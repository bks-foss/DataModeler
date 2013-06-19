package com.isb.datamodeler.model.core.editingDomain;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.DiagramEditingDomainFactory;


public class DataModelerEditingDomainFactory extends DiagramEditingDomainFactory{

	public static String DATA_MODELER_EDITING_DOMAIN_ID= "com.isb.datamodeler.ui.diagram.EditingDomain";
	
	public TransactionalEditingDomain createEditingDomain() {
	    TransactionalEditingDomain result = super.createEditingDomain();;
	    return result;
	}

}
