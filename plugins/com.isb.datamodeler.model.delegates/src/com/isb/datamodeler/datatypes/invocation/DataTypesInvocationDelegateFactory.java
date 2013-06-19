package com.isb.datamodeler.datatypes.invocation;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate.Factory;

import com.isb.datamodeler.datatypes.EDatatypesPackage;

public class DataTypesInvocationDelegateFactory implements Factory {

	@Override
	public InvocationDelegate createInvocationDelegate(EOperation operation) {
		
	    if (operation.getEContainingClass() == EDatatypesPackage.Literals.DATA_TYPE)
	    	return new DataTypeInvocationDelegate(operation);
	    
		return null;
	}

}
