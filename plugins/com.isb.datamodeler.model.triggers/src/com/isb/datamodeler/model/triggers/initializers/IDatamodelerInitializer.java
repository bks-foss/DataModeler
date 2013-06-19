package com.isb.datamodeler.model.triggers.initializers;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.isb.datamodeler.schema.ESQLObject;

public interface IDatamodelerInitializer {
	
	public void initialize(EObject owner, List<ESQLObject> children);
	
	public void initialize(EObject owner, ESQLObject child);
	
	public String generateChildName(EObject owner, Class<?> eClass);

}
