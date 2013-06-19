package com.isb.datamodeler.constraints.setting;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate.Factory;

import com.isb.datamodeler.constraints.EConstraintsPackage;

public class ConstraintsSettingDelegateFactory implements Factory {

	@Override
	public SettingDelegate createSettingDelegate(
			EStructuralFeature eStructuralFeature) {

		if(eStructuralFeature.getEContainingClass() == EConstraintsPackage.Literals.FOREIGN_KEY)
			return new ForeignKeySettingDelegate(eStructuralFeature);
		
		if(eStructuralFeature.getEContainingClass() == EConstraintsPackage.Literals.TABLE_CONSTRAINT)
			return new TableConstraintSettingDelegate(eStructuralFeature);
		
		return null;
	}

}
