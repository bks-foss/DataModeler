package com.isb.datamodeler.schema.setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicSettingDelegate.Stateless;

import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.schema.ESchemaPackage;

public class DataBaseSettingDelegate extends Stateless {
	
	//Guardamos un registro estático de los tipos primitivos para cada Base de Datos registrada
	private static Map<String, List<EPrimitiveDataType>> _dataBaseToPrimitiveTypes = new HashMap<String, List<EPrimitiveDataType>>();
	
	public DataBaseSettingDelegate(EStructuralFeature arg0) {
		super(arg0);
	}

	@Override
	protected Object get(InternalEObject owner, boolean resolve,
			boolean coreType) {
		
	    if (eStructuralFeature.getEContainingClass() == ESchemaPackage.Literals.DATABASE)
	    {
		      switch (eStructuralFeature.getEContainingClass().getEAllStructuralFeatures().indexOf(eStructuralFeature))
		      {
//		        case ESchemaPackage.DATABASE__PRIMITIVE_DATA_TYPES:
//		        	return getPrimitiveDataTypes((EDatabase)owner);
		      }
	    
	    }
		return null;
	}
	
//	private List<EPrimitiveDataType> getPrimitiveDataTypes(EDatabase owner) {
//		
//		List<EPrimitiveDataType> primitiveTypes = _dataBaseToPrimitiveTypes.get(owner.getName());
//		
//		if(primitiveTypes==null)
//		{
//			primitiveTypes = owner.loadPrimitiveDataTypes();
//			_dataBaseToPrimitiveTypes.put(owner.getName(), primitiveTypes);
//		}
//		
//		return primitiveTypes;
//	}

	@Override
	protected void unset(InternalEObject owner) {
		return;
	}

	@Override
	protected boolean isSet(InternalEObject owner) {
		
		return false;
	}

	
}
