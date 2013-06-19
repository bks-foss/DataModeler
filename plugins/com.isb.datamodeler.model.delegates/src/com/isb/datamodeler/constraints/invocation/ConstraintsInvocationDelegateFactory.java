package com.isb.datamodeler.constraints.invocation;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate.Factory;

import com.isb.datamodeler.constraints.EConstraintsPackage;

public class ConstraintsInvocationDelegateFactory implements Factory {

	@Override
	public InvocationDelegate createInvocationDelegate(EOperation operation) {
		
	    if (operation.getEContainingClass() == EConstraintsPackage.Literals.FOREIGN_KEY)
	    	return new ForeignKeyInvocationDelegate(operation);
	    
		return null;
	}

}
