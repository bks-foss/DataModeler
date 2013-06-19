package com.isb.datamodeler.constraints.setting;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicSettingDelegate.Stateless;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;

public class ForeignKeySettingDelegate extends Stateless {

	public ForeignKeySettingDelegate(EStructuralFeature arg0) {
		super(arg0);
	}

	@Override
	protected Object get(InternalEObject owner, boolean resolve,
			boolean coreType) {
	    if (eStructuralFeature.getEContainingClass() == EConstraintsPackage.Literals.FOREIGN_KEY)
	    {
		      switch (eStructuralFeature.getEContainingClass().getEAllStructuralFeatures().indexOf(eStructuralFeature))
		      {	
		      	case EConstraintsPackage.FOREIGN_KEY__PARENT_TABLE_SCHEMA:
		      		return ((EForeignKey)owner).getParentTable().getSchema();
		      }
	    
	    }
		return null;
	}

	@Override
	protected boolean isSet(InternalEObject owner) {
		// TODO Auto-generated method stub
		return false;
	}




}
