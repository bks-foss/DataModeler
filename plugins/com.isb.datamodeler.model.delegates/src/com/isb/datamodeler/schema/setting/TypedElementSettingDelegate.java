package com.isb.datamodeler.schema.setting;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicSettingDelegate.Stateless;

import com.isb.datamodeler.datatypes.EDataType;
import com.isb.datamodeler.datatypes.ESQLDataType;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.schema.ETypedElement;

public class TypedElementSettingDelegate extends Stateless {
	
	public TypedElementSettingDelegate(EStructuralFeature arg0) {
		super(arg0);
	}

	@Override
	protected Object get(InternalEObject owner, boolean resolve,
			boolean coreType) {
		
	    if (eStructuralFeature.getEContainingClass() == ESchemaPackage.Literals.TYPED_ELEMENT)
	    {
		      switch (eStructuralFeature.getEContainingClass().getEAllStructuralFeatures().indexOf(eStructuralFeature))
		      {
		        case ESchemaPackage.TYPED_ELEMENT__DATA_TYPE:
		        	return getDataType((ETypedElement)owner);
		      }
	    
	    }
		return null;
	}
	
	@Override
	protected void set(InternalEObject owner, Object newValue) {
	   
	    if (eStructuralFeature.getEContainingClass() == ESchemaPackage.Literals.TYPED_ELEMENT)
	    {
		      switch (eStructuralFeature.getEContainingClass().getEAllStructuralFeatures().indexOf(eStructuralFeature))
		      {
		        case ESchemaPackage.TYPED_ELEMENT__DATA_TYPE:
		        	setDataType((ETypedElement)owner, (EDataType)newValue);		        	
		      }
	    
	    }

	}
	
	private EDataType getDataType(ETypedElement owner) {
		
		EDataType dataType = owner.getContainedType()!=null?owner.getContainedType():owner.getReferencedType();
		
		return dataType;
	}

	private void setDataType(ETypedElement owner, EDataType newDataType) {
		owner.setContainedType((ESQLDataType)newDataType);
		
	}

	@Override
	protected void unset(InternalEObject owner) {
		return;
	}

	@Override
	protected boolean isSet(InternalEObject owner) {
		
		return false;
	}

	
}
