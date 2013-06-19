package com.isb.datamodeler.tables.invocation;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate.Factory;

import com.isb.datamodeler.tables.ETablesPackage;

public class TablesInvocationDelegateFactory implements Factory {

	@Override
	public InvocationDelegate createInvocationDelegate(EOperation operation) {
		
	    if (operation.getEContainingClass() == ETablesPackage.Literals.BASE_TABLE)
	    	return new BaseTableInvocationDelegate(operation);
	    
	    if (operation.getEContainingClass() == ETablesPackage.Literals.COLUMN)
	    	return new ColumnInvocationDelegate(operation);
	    
		return null;
	}

}
