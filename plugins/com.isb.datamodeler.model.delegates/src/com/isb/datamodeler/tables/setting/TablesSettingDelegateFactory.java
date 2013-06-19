package com.isb.datamodeler.tables.setting;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate;
import org.eclipse.emf.ecore.EStructuralFeature.Internal.SettingDelegate.Factory;

import com.isb.datamodeler.tables.ETablesPackage;

public class TablesSettingDelegateFactory implements Factory {

	@Override
	public SettingDelegate createSettingDelegate(
			EStructuralFeature eStructuralFeature) {
		
		if(eStructuralFeature.getEContainingClass() == ETablesPackage.Literals.COLUMN)
			return new ColumnSettingDelegate(eStructuralFeature);
		
		
		return null;
	}

}
