package com.isb.datamodeler.model.core.validation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;


public class DataModelerValidatorDescriptor
{
	//public final static String CODE_VALUE_VEGA = "VMOD";   //$NON-NLS-1$
	
	// Cógido base para las reglas de normativa
	public final static String CODE_VALUE_NORM  = "DMN";   //$NON-NLS-1$
	// Cógido base para las reglas de modelado
	public final static String CODE_VALUE_MODEL  = "DMM";   //$NON-NLS-1$
	
	public final static String ELEMENT_STATE_CLASS = "stateClass";   //$NON-NLS-1$
	
	public final static String ATTR_CATEGORY 		= "category";   //$NON-NLS-1$
	public final static String ATTR_TARGET 			= "target"; //$NON-NLS-1$
	public final static String ATTR_CLASS 			= "class"; //$NON-NLS-1$
	public final static String ATTR_CODE 			= "code"; //$NON-NLS-1$
	public final static String ATTR_ID 				= "id"; //$NON-NLS-1$
	public final static String ATTR_NAME 			= "name"; //$NON-NLS-1$
	public final static String ELEMENT_DESCRIPTION 	= "description";   //$NON-NLS-1$
	public final static String ATTR_DEPEND_ON_DB	= "dependOnDb";   //$NON-NLS-1$
	public final static String ATTR_VALIDATORDB_ID	= "validatorDB_id";   //$NON-NLS-1$
	
	

	private String _categoryId;
	private String _target;
	private String _class;
	private String _code;
	private String _id;
	private String _name;
	private String _description = "Categoria sin descripcion" ;
	private boolean enabled;
	private boolean _dependOnDb;
	private String _validatorDB_id;
	
	private IConfigurationElement _configurationValidator;
	// Categoría a la que pertenece la validación
	private DataModelerCategoryDescriptor _categoryDescriptor; 
	
public DataModelerValidatorDescriptor(IConfigurationElement validator)
{
	_categoryId = validator.getAttribute(ATTR_CATEGORY);
	_target = validator.getAttribute(ATTR_TARGET);
	_class = validator.getAttribute(ATTR_CLASS);
	_code = validator.getAttribute(ATTR_CODE);
	_id = validator.getAttribute(ATTR_ID);
	_name = validator.getAttribute(ATTR_NAME);
	_validatorDB_id = validator.getAttribute(ATTR_VALIDATORDB_ID);
	_dependOnDb = new Boolean(validator.getAttribute(ATTR_DEPEND_ON_DB)).booleanValue();
	
	IConfigurationElement[] descriptions = validator.getChildren(ELEMENT_DESCRIPTION);
	if (descriptions.length > 0)
		_description = descriptions[0].getValue();
	
	_configurationValidator = validator;
	
	// accedemos al manager para validar la categoría
	DataModelerValidationManager manager = DataModelerValidationManager.getInstance();
	DataModelerCategoryDescriptor[] definedCategories = manager.getCategories();
	
	for (int i=0; _categoryId != null && i<definedCategories.length; i++)
	{
		if (definedCategories[i].getId().equals(_categoryId))
		{
			_categoryDescriptor = definedCategories[i];
			break;
		}
	}
	
	setEnabled(!DataModelerValidationPreferences.isValidationDisabled(_id));
}
public String getValidatorDBId()
{
	return _validatorDB_id;
}
public boolean dependOnDb()
{
	return _dependOnDb;
}
public String getDescription()
{
	return _description;
}
public String getName()
{
	return _name;
}
public String getCategoryId()
{
	return _categoryId;
}

public String getTarget()
{
	return _target;
}
public String getValidatorClass()
{
	return _class;
}
public String getCode()
{
	return _code;
}
public String getId()
{
	return _id;
}
public DataModelerCategoryDescriptor getCategoryDescripor()
{
	return _categoryDescriptor;
}
//public Category getCategory()
//{
//	
//}
public AbstractDataModelerElementValidator getValidator(EModelElement namedElement)
{

	String targetName = getTarget();
	try
	{
//		Class<?> targetClass = Class.forName(targetName);
//		
//		if(targetClass.isInstance(namedElement))
//		{
//			AbstractDataModelerElementValidator validator = (AbstractDataModelerElementValidator)_configurationValidator.createExecutableExtension(ATTR_CLASS);
//			if(validator != null)
//				return validator;
//		}
		if(namedElement.eClass().getInstanceClassName().equals(targetName))
		{
			AbstractDataModelerElementValidator validator = (AbstractDataModelerElementValidator)_configurationValidator.createExecutableExtension(ATTR_CLASS);
			if(validator != null)
				return validator;
		}
		else for(EGenericType genericType : namedElement.eClass().getEAllGenericSuperTypes())
		{
			if(genericType.getEClassifier().getInstanceTypeName().equals(targetName))
			{
				AbstractDataModelerElementValidator validator = (AbstractDataModelerElementValidator)_configurationValidator.createExecutableExtension(ATTR_CLASS);
				if(validator != null)
					return validator;
			}
		}
	}
	catch(CoreException e)
	{
		e.printStackTrace();
	}
//	catch(ClassNotFoundException e1)
//	{
//		e1.printStackTrace();
//	}
	return null;
}
public final void setEnabled(boolean enabled)
{
    if(!enabled)
        enabled = isMandatory();
    if(this.enabled != enabled)
    {
        this.enabled = enabled;

    }
}
public final boolean isEnabled()
{
    return enabled;
}

private boolean isMandatory()
{
    return  getCategoryDescripor().isMandatory();
}
}
