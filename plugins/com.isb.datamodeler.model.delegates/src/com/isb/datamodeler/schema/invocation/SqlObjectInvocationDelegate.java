package com.isb.datamodeler.schema.invocation;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicInvocationDelegate;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.schema.ESchemaPackage;

public class SqlObjectInvocationDelegate extends BasicInvocationDelegate {

	public SqlObjectInvocationDelegate(EOperation operation) {
		super(operation);
	}

	@Override
	public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
			throws InvocationTargetException {

	    if (eOperation.getEContainingClass() == ESchemaPackage.Literals.SQL_OBJECT)
	    {
	      switch (eOperation.getEContainingClass().getEAllOperations().indexOf(eOperation))
	      {
	        case ESchemaPackage.SQL_OBJECT___GET_ROOT_CONTAINER:
	            return getRootContainer((ESQLObject)target);	
	      }
	    }
		
		return super.dynamicInvoke(target, arguments);
	}

	/**
	 * Returns the Root Container
	 * @param schema
	 * @return
	 */
	private Object getRootContainer(ESQLObject eObject) {
		
		return EcoreUtil.getRootContainer(eObject, true);
	}

	

}
