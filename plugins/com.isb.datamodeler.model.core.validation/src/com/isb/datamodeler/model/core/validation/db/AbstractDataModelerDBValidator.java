package com.isb.datamodeler.model.core.validation.db;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

public abstract class AbstractDataModelerDBValidator implements
		IExecutableExtension {
	
	private String validator_id;
	private String database_id;
	
	public abstract BasicDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression);
	
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		validator_id = config.getAttribute("validator_id");
		database_id = config.getAttribute("database_id");

	}
	
	public abstract BasicDiagnostic validate(EObject object, String featureName, Object newValue);
	
//	public abstract DataModelDiagnostic validate(EDataType eDataType, Object value,
//			Map<Object, Object> context, String constraint, String expression);
	
	public String getValidatorId() 
	{
		return validator_id;
	}
	public String getDataBaseId() 
	{
		return database_id;
	}
	protected Character containsInvalidChars(String name) 
	{
		if(name.length()==0)
			return new Character(' ');
	    for (int i = 0; i < name.length(); i++) 
	    {
	        char c = name.charAt(i);
	        if(i==0)
	        {
	        	// El primer carácter de la serie debe ser un carácter alfabético (a-z, A-Z del alfabeto inglés), @, # o $. 
	        	if(!(('a'<=c && c<='z') || ('A'<=c && c<='Z') || (c=='@')|| (c=='#')||c=='$' ))
		            return new Character(c);
	        	if(c=='ñ' || c=='Ñ')
	 	        	return new Character(c);
	        }
	        else //El resto debe ser un carácter alfabético, numérico (0-9), @, #, $ o _. Se recomienda no utilizar @, # ni $. 
	        {
	        	if(c=='ñ' || c=='Ñ')
		        	return new Character(c);
	        	if(!(('a'<=c && c<='z') || ('A'<=c && c<='Z') || ('0'<=c && c<='9') || (c=='_') ))
		            return new Character(c);
	        }
	        
	        
	    }
	    return null;
	}
}
