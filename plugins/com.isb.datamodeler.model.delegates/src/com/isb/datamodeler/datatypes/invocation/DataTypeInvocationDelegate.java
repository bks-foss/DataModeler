package com.isb.datamodeler.datatypes.invocation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicInvocationDelegate;

import com.isb.datamodeler.datatypes.EDataType;
import com.isb.datamodeler.datatypes.EDatatypesPackage;
import com.isb.datamodeler.datatypes.EPredefinedDataType;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;

public class DataTypeInvocationDelegate extends BasicInvocationDelegate {

	public DataTypeInvocationDelegate(EOperation operation) {
		super(operation);
	}

	@Override
	public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
			throws InvocationTargetException {

	    if (eOperation.getEContainingClass() == EDatatypesPackage.Literals.DATA_TYPE)
	    {
	      switch (eOperation.getEContainingClass().getEAllOperations().indexOf(eOperation))
	      {
	        case EDatatypesPackage.DATA_TYPE___COMPARE_TO__EDATATYPE:
	            return compareTo((EDataType)target, (EDataType)arguments.get(0));	
	        case EDatatypesPackage.DATA_TYPE___GET_DATA_TYPE_ATTRIBUTES:
	            return getDataTypeAttributes(target.eClass());		            
	      }
	    }
		
		return super.dynamicInvoke(target, arguments);
	}

	private int compareTo(EDataType type1, EDataType type2) {
		
		int result = -1;

		if(type1 instanceof EPredefinedDataType && type2 instanceof EPredefinedDataType)
		{
			EPrimitiveDataType primitiveType1 = ((EPredefinedDataType)type1).getPrimitiveType();
			EPrimitiveDataType primitiveType2 = ((EPredefinedDataType)type2).getPrimitiveType();
			
			result = primitiveType1.getId().compareTo(primitiveType2.getId());
			
			//Si coincide el tipo primitivo comparamos los atributos:
			if(result==0)
				result = compareDataTypesDetails(type1, type2);
		}
		
		
		return result;
	}
	
	/**
	 * Compara los atributos de dos EDataType
	 * @param type1
	 * @param type2
	 * @return
	 */
	private int compareDataTypesDetails(EDataType type1, EDataType type2)
	{
		int result = 0;
		
		//Se supone que si llegamos aca es por que son del mismo tipo
		Assert.isTrue(type1.getClass()==type2.getClass());
		
		List<EAttribute> typeAttributes = type1.getDataTypeAttributes(); 
		
		for(EAttribute typeAttribute: typeAttributes)
		{
			Object value1 = type1.eGet(typeAttribute);
			Object value2 = type2.eGet(typeAttribute);
			
			if(value1==null && value2!=null)
				return -1;
			
			if(value1!=null && !value1.equals(value2))
				return -1;

		}
		
		return result;
	}
	
	/**
	 * Obtiene todos los atributos propios unicamente del DataType
	 * @param sourceDataType
	 * @return
	 */
	private EList<EAttribute> getDataTypeAttributes(EClass dataTypeEClass)
	{
		EList<EAttribute> dtFeatures =  new BasicEList<EAttribute>();
		
		for(EAttribute attribute:dataTypeEClass.getEAllAttributes())
			if(EDatatypesPackage.eINSTANCE.getDataType().isSuperTypeOf(attribute.getEContainingClass()))
				dtFeatures.add(attribute);
		
		return dtFeatures;
	}


}
