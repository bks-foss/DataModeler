/**
 * 
 */
package com.isb.datamodeler.model.core.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EModelElement;

/**
 * Clase para obtener los validadores del modelo definidos a través
 * del punto de extensión com.isb.datamodeler.validation.validators
 * @author x500714
 *
 */
public class DataModelerValidationManager {
	
	private static DataModelerValidationManager _Instance;
	
	public static String EXTENSION_POINT_VALIDATORS_ID = "com.isb.datamodeler.validation.validators";
	
	private static final String ELEMENT_CATEGORY = "category"; //$NON-NLS-1$
	private static final String ELEMENT_VALIDATOR = "validator"; //$NON-NLS-1$
	
	private Map<DataModelerCategoryDescriptor, List<DataModelerValidatorDescriptor>> _categories2validators = 
		new HashMap<DataModelerCategoryDescriptor, List<DataModelerValidatorDescriptor>>();
	

	public void startup()
	{
		IConfigurationElement[] elements =
			Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_VALIDATORS_ID);
		List<IConfigurationElement> categories = new ArrayList<IConfigurationElement>();
		List<IConfigurationElement> validators = new ArrayList<IConfigurationElement>();
		
		for(IConfigurationElement element:elements)
		{
			if(element.getName().equals(ELEMENT_CATEGORY))
				categories.add(element);
			else if(element.getName().equals(ELEMENT_VALIDATOR))
				validators.add(element);
		}
		for(IConfigurationElement category:categories )
			processCategrory(category);
		for(IConfigurationElement validator:validators )
			processValidator(validator);
	
	}
	public void shutdown()
	{
		// TODO
	}
	public DataModelerCategoryDescriptor[] getCategories()
	{
		return (_categories2validators.keySet()).toArray(new DataModelerCategoryDescriptor[0]);
	}
	private void processCategrory(IConfigurationElement config)
	{
		// Crear el descriptor de categoría y guardarlo en la tabla de categorías
		DataModelerCategoryDescriptor category = new DataModelerCategoryDescriptor(config);
		// Si la categoría ya existía, la sobreescribimos
		_categories2validators.put(category, new ArrayList<DataModelerValidatorDescriptor>());
	}
	private void processValidator(IConfigurationElement config)
	{
		DataModelerValidatorDescriptor validator = new DataModelerValidatorDescriptor(config);
		List<DataModelerValidatorDescriptor> validators = _categories2validators.get(validator.getCategoryDescripor());
		if (validator.getCategoryId() != null && validators != null)
		{
			validators.add(validator);
		}
	}
	
	
	public List<AbstractDataModelerElementValidator> getValidators(EModelElement namedElement)
	{
		List<AbstractDataModelerElementValidator> validators = getValidatorsAsList(namedElement);

		return validators;
	}
	
	
	private List<AbstractDataModelerElementValidator> getValidatorsAsList(EModelElement namedElement)
	{
		List<AbstractDataModelerElementValidator> externalValidators = new ArrayList<AbstractDataModelerElementValidator>();
		DataModelerValidatorDescriptor[] validatorsDEscriptors = getValidatorsDescriptors();
		for(DataModelerValidatorDescriptor v:validatorsDEscriptors)
		{
			if(v.isEnabled())
			{
				AbstractDataModelerElementValidator validator = v.getValidator(namedElement);
				if(validator!=null)
					externalValidators.add(validator);
			}
		}

		return externalValidators;
	}
	/**
	 * Devuelve la instancia singleton del manager
	 * @return
	 */
	public static DataModelerValidationManager getInstance()
	{
		if(_Instance==null)
		{
			_Instance = new DataModelerValidationManager();
			_Instance.startup();
		}
		return _Instance;	
	}
	public DataModelerValidatorDescriptor[] getValidatorsDescriptors()
	{
		HashSet<DataModelerValidatorDescriptor> factories = new HashSet<DataModelerValidatorDescriptor>();
		for(List<DataModelerValidatorDescriptor> factory: _categories2validators.values())
		{
			factories.addAll(factory);
		}
		return factories.toArray(new DataModelerValidatorDescriptor[0]);
	}
	public List<DataModelerValidatorDescriptor> getValidators(DataModelerCategoryDescriptor categoryDescriptor)
	{
		return getValidators( categoryDescriptor , null);
	}
	public List<DataModelerValidatorDescriptor> getValidatorsExt(DataModelerCategoryDescriptor categoryDescriptor)
	{
		return getValidators( categoryDescriptor , DataModelerValidatorDescriptor.CODE_VALUE_NORM);
	}
	// Este método no se usa
	/*public List<VegaModelerValidatorDescriptor> getValidatorsVega(VegaModelerCategoryDescriptor categoryDescriptor)
	{
		return getValidators( categoryDescriptor , VegaModelerValidatorDescriptor.CODE_VALUE_VEGA);
	}*/
	
	public List<DataModelerValidatorDescriptor> getValidators(DataModelerCategoryDescriptor categoryDescriptor , String code)
	{
		List<DataModelerValidatorDescriptor> result = new ArrayList<DataModelerValidatorDescriptor>();
		List<DataModelerValidatorDescriptor> validators = _categories2validators.get(categoryDescriptor);
		if(code == null)
			return _categories2validators.get(categoryDescriptor);
		else for(DataModelerValidatorDescriptor v:validators)
		{
			if(v.getCode().indexOf(code)!=-1)
				result.add(v);
		}
		return result;
	}
	public DataModelerValidatorDescriptor getValidatorDescriptor(String id)
	{
		DataModelerValidatorDescriptor[] validatorsDesc = getValidatorsDescriptors();
		for(DataModelerValidatorDescriptor vd:validatorsDesc)
		{
			if(vd.getId().equals(id))
				return vd;
		}
		return null;
	}

}
