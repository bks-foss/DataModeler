package com.isb.datamodeler.model.core.validation;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

public abstract class AbstractDataModelerElementValidator implements
		IExecutableExtension {
	
	private String code;
	private String id;
	private String category;
	private String validatorDB_id;
	private boolean dependOnDb;
	
	public static String CATEGORY_MANDATORY_RULE = "mandatoryRule";
	public static String CATEGORY_REGULATION_RULE = "regulationRule";
	
	public static String BASIC_TYPE = "DMOD";
	public static String EXTERNAL_TYPE = "EXT";

	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1,
			Object arg2) throws CoreException 
	{
		code = arg0.getAttribute("code");
		id = arg0.getAttribute("id");
		category = arg0.getAttribute("category");
		validatorDB_id = arg0.getAttribute("validatorDB_id");
		dependOnDb = new Boolean (arg0.getAttribute("dependOnDb")).booleanValue();

	}
	public abstract DataModelDiagnostic validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression);
	
	public String getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	
	public String getCategory() {
		return category;
	}
	public boolean dependOnDb()
	{
		return dependOnDb;
	}
	public String getValidatorDBId()
	{
		return validatorDB_id;
	}
}
