package com.isb.datamodeler.model.core.validation.db;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;


public class DataModelerValidatorDBDescriptor
{

	public final static String ATTR_TARGET 			= "targetEObject"; //$NON-NLS-1$
	public final static String ATTR_CLASS 			= "class"; //$NON-NLS-1$
	public final static String ATTR_VALIDATOR_ID 	= "validator_id"; //$NON-NLS-1$
	public final static String ATTR_DATABASE_ID 	= "database_id"; //$NON-NLS-1$
	public final static String ELEMENT_DESCRIPTION 	= "description";   //$NON-NLS-1$

	private String _targetEObject;
	private String _class;
	private String _validatorId;
	private String _databaseId;
	private String _description = "Categoria sin descripcion" ;

	
	private IConfigurationElement _configurationValidator;

	
public DataModelerValidatorDBDescriptor(IConfigurationElement validator)
{
	_targetEObject = validator.getAttribute(ATTR_TARGET);
	_class = validator.getAttribute(ATTR_CLASS);

	_validatorId = validator.getAttribute(ATTR_VALIDATOR_ID);
	_databaseId = validator.getAttribute(ATTR_DATABASE_ID);
	
	IConfigurationElement[] descriptions = validator.getChildren(ELEMENT_DESCRIPTION);
	if (descriptions.length > 0)
		_description = descriptions[0].getValue();
	
	_configurationValidator = validator;
	
}
public String getDescription()
{
	return _description;
}


public String getTargetEObject()
{
	return _targetEObject;
}
public String getValidatorClass()
{
	return _class;
}

public String getValidatorId()
{
	return _validatorId;
}
public String getDataBaseId()
{
	return _databaseId;
}

public AbstractDataModelerDBValidator getValidatorForEObject(EObject eObject , String dataBaseId )
{

	return getValidatorForEClass(eObject.eClass(), dataBaseId);
}
public AbstractDataModelerDBValidator getValidatorForEClass(EClass eClass , String dataBaseId )
{

	String dbId = getDataBaseId();
	String targetName = getTargetEObject();
	try
	{
		if(eClass.getInstanceClassName().equals(targetName) && dataBaseId.equals(dbId))
		{
			AbstractDataModelerDBValidator validator = (AbstractDataModelerDBValidator)_configurationValidator.createExecutableExtension(ATTR_CLASS);
			if(validator != null)
				return validator;
		}
		else for(EGenericType genericType : eClass.getEAllGenericSuperTypes())
		{
			if(genericType.getEClassifier().getInstanceTypeName().equals(targetName) && dataBaseId.equals(dbId))
			{
				AbstractDataModelerDBValidator validator = (AbstractDataModelerDBValidator)_configurationValidator.createExecutableExtension(ATTR_CLASS);
				if(validator != null)
					return validator;
			}
		}
	}
	catch(CoreException e)
	{
		e.printStackTrace();
	}

	return null;
}
public AbstractDataModelerDBValidator getValidatorForValidatorId(String validatorId , EObject eObject , String dataBaseId )
{

	String dbId = getDataBaseId();
	String targetName = getTargetEObject();
	try
	{
		if(eObject.eClass().getInstanceClassName().equals(targetName)&& validatorId.equals(getValidatorId())&& dataBaseId.equals(dbId))
		{
			AbstractDataModelerDBValidator validator = (AbstractDataModelerDBValidator)_configurationValidator.createExecutableExtension(ATTR_CLASS);
			if(validator != null)
				return validator;
		}
		else for(EGenericType genericType : eObject.eClass().getEAllGenericSuperTypes())
		{
			if(genericType.getEClassifier().getInstanceTypeName().equals(targetName) && validatorId.equals(getValidatorId())&& dataBaseId.equals(dbId))
			{
				AbstractDataModelerDBValidator validator = (AbstractDataModelerDBValidator)_configurationValidator.createExecutableExtension(ATTR_CLASS);
				if(validator != null)
					return validator;
			}
		}
	}
	catch(CoreException e)
	{
		e.printStackTrace();
	}

	return null;
}

public AbstractDataModelerDBValidator getValidatorForDB(String databaseId)
{

	String dbId = getDataBaseId();
	try
	{
		if(databaseId.equals(dbId))
		{
			AbstractDataModelerDBValidator validator = (AbstractDataModelerDBValidator)_configurationValidator.createExecutableExtension(ATTR_CLASS);
			if(validator != null)
				return validator;
		}
	}
	catch(CoreException e)
	{
		e.printStackTrace();
	}

	return null;
}
public AbstractDataModelerDBValidator getValidatorForClass(String aClass)
{

	String vClass = getValidatorClass();
	try
	{
		if(vClass.equals(aClass))
		{
			AbstractDataModelerDBValidator validator = (AbstractDataModelerDBValidator)_configurationValidator.createExecutableExtension(ATTR_CLASS);
			if(validator != null)
				return validator;
		}
	}
	catch(CoreException e)
	{
		e.printStackTrace();
	}

	return null;
}


}
