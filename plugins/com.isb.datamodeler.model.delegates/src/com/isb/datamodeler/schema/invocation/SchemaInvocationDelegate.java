package com.isb.datamodeler.schema.invocation;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicInvocationDelegate;

import com.isb.datamodeler.schema.EFunctionalElement;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;

public class SchemaInvocationDelegate extends BasicInvocationDelegate {

	public SchemaInvocationDelegate(EOperation operation) {
		super(operation);
	}

	@Override
	public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
			throws InvocationTargetException {

	    if (eOperation.getEContainingClass() == ESchemaPackage.Literals.SCHEMA)
	    {
	      switch (eOperation.getEContainingClass().getEAllOperations().indexOf(eOperation))
	      {
	        case ESchemaPackage.SCHEMA___IS_EXTERNAL:
	            return isExternal((ESchema)target);	
	      }
	    }
		
		return super.dynamicInvoke(target, arguments);
	}

	/**
	 * Determina si un Esquema es externo
	 * @param schema
	 * @return
	 */
	private Object isExternal(ESchema schema) {
		

		EFunctionalElement parent = (EFunctionalElement)schema.eContainer();
		
		if((parent!=null) && 
				(parent.getCapability() !=null) && 
				(!(schema.getCapability().equalsIgnoreCase(parent.getCapability()))))
			return true;

		return false;
	}

	

}
