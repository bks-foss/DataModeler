package com.isb.datamodeler.schema.invocation;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate;
import org.eclipse.emf.ecore.EOperation.Internal.InvocationDelegate.Factory;

import com.isb.datamodeler.schema.ESchemaPackage;

public class SchemaInvocationDelegateFactory implements Factory {

	@Override
	public InvocationDelegate createInvocationDelegate(EOperation operation) {
		
	    if (operation.getEContainingClass() == ESchemaPackage.Literals.SCHEMA)
	    	return new SchemaInvocationDelegate(operation);
	    
	    if (operation.getEContainingClass() == ESchemaPackage.Literals.DATA_MODELER_NAMED_ELEMENT)
	    	return new DataModelerNamedElementDelegate(operation);
	    
	    if (operation.getEContainingClass() == ESchemaPackage.Literals.SQL_OBJECT)
	    	return new SqlObjectInvocationDelegate(operation);
	    
		return null;
	}

}
