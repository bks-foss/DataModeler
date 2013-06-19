package com.isb.datamodeler.constraints.setting;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicSettingDelegate.Stateless;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.ETableConstraint;

public class TableConstraintSettingDelegate extends Stateless {

	public TableConstraintSettingDelegate(EStructuralFeature arg0) {
		super(arg0);
	}

	@Override
	protected Object get(InternalEObject owner, boolean resolve,
			boolean coreType) {
	    if (eStructuralFeature.getEContainingClass() == EConstraintsPackage.Literals.TABLE_CONSTRAINT)
	    {
		      switch (eStructuralFeature.getEContainingClass().getEAllStructuralFeatures().indexOf(eStructuralFeature))
		      {	
		      	case EConstraintsPackage.TABLE_CONSTRAINT__BASE_TABLE_SCHEMA:
		      		return ((ETableConstraint)owner).getBaseTable().getSchema();
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
