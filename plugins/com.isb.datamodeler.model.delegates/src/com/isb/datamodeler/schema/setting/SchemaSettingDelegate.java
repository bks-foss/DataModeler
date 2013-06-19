package com.isb.datamodeler.schema.setting;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicSettingDelegate.Stateless;

import com.isb.datamodeler.schema.ESchemaPackage;

public class SchemaSettingDelegate extends Stateless{

	public SchemaSettingDelegate(EStructuralFeature arg0) {
		super(arg0);
	}

	@Override
	protected Object get(InternalEObject owner, boolean resolve,
			boolean coreType) {
	    if (eStructuralFeature.getEContainingClass() == ESchemaPackage.Literals.SCHEMA)
	    {
		      switch (eStructuralFeature.getEContainingClass().getEAllStructuralFeatures().indexOf(eStructuralFeature))
		      {
		      	//NO HAY NADA POR AHORA
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
