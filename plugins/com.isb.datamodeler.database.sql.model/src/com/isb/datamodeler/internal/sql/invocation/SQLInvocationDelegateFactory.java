package com.isb.datamodeler.internal.sql.invocation;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate.Factory;

import com.isb.datamodeler.schema.ESchemaPackage;

public class SQLInvocationDelegateFactory implements Factory{

	@Override
	public InvocationDelegate createInvocationDelegate(EOperation operation) {
		if (operation.getEContainingClass().isInterface() && 
				operation.getEContainingClass().isSuperTypeOf(ESchemaPackage.Literals.DATABASE))
	    	return new SQLInvocationDelegate(operation);
		return null;
	}

}
