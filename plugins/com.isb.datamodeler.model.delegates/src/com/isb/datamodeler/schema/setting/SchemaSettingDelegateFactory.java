package com.isb.datamodeler.schema.setting;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate.Factory;

import com.isb.datamodeler.schema.ESchemaPackage;

public class SchemaSettingDelegateFactory implements Factory {

	@Override
	public SettingDelegate createSettingDelegate(
			EStructuralFeature eStructuralFeature) {
		
		if(eStructuralFeature.getEContainingClass() == ESchemaPackage.Literals.TYPED_ELEMENT)
			return new TypedElementSettingDelegate(eStructuralFeature);
		
		if(eStructuralFeature.getEContainingClass() == ESchemaPackage.Literals.DATABASE)
			return new DataBaseSettingDelegate(eStructuralFeature);
		
		if(eStructuralFeature.getEContainingClass() == ESchemaPackage.Literals.SCHEMA)
			return new SchemaSettingDelegate(eStructuralFeature);
		
		return null;
	}

}
