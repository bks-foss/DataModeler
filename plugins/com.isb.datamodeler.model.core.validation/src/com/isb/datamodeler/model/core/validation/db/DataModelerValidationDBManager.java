/**
 * 
 */
package com.isb.datamodeler.model.core.validation.db;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;

/**
 * Clase para obtener los validadores del modelo dependiente de la base de datos definidos a través
 * del punto de extensión com.isb.datamodeler.validation.db.validators
 * @author x500714
 *
 */
public class DataModelerValidationDBManager {
	
	private static DataModelerValidationDBManager _Instance;
	
	public static String EXTENSION_POINT_VALIDATORS_ID = "com.isb.datamodeler.validation.db.validators";
	
	private static final String ELEMENT_VALIDATOR = "validator"; //$NON-NLS-1$
	
	private List<DataModelerValidatorDBDescriptor> _validators = new ArrayList<DataModelerValidatorDBDescriptor>();
	
	private Map<String, List<DataModelerValidatorDBDescriptor>> _class2Validators = 
		new Hashtable<String, List<DataModelerValidatorDBDescriptor>>();
	
	public void startup()
	{
		IConfigurationElement[] elements =
			Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_VALIDATORS_ID);
		
		
		for(IConfigurationElement element:elements)
		{
			if(element.getName().equals(ELEMENT_VALIDATOR))
				processValidator(element);
		}
		
	
	}
	public void shutdown()
	{
		// TODO
	}

	private void processValidator(IConfigurationElement config)
	{
		DataModelerValidatorDBDescriptor validator = new DataModelerValidatorDBDescriptor(config);
		List<DataModelerValidatorDBDescriptor> listValidatos = _class2Validators.get(validator.getClass());
		if(listValidatos==null)
		{
			listValidatos = new ArrayList<DataModelerValidatorDBDescriptor>();
			_class2Validators.put(validator.getClass().toString(), listValidatos);
		}
		listValidatos.add(validator);
		_validators.add(validator);
	}
	
	public List<AbstractDataModelerDBValidator> getValidators(EModelElement namedElement)
	{
		List<AbstractDataModelerDBValidator> validators = getValidatorsAsList(namedElement);

		return validators;
	}
	
	
	private List<AbstractDataModelerDBValidator> getValidatorsAsList(EModelElement namedElement)
	{
		List<AbstractDataModelerDBValidator> result = new ArrayList<AbstractDataModelerDBValidator>();
		
//		for(DataModelerValidatorDBDescriptor v:getValidatorsDescriptors())
//		{
//			AbstractDataModelerDBValidator validator = v.getValidator(namedElement);
//				if(validator!=null)
//					result.add(validator);
//		}

		return result;
	}
	/**
	 * Validadores para una Base de Datos
	 */
	public List<AbstractDataModelerDBValidator> getValidatorsForDBAsList(String databaseId)
	{
		List<AbstractDataModelerDBValidator> result = new ArrayList<AbstractDataModelerDBValidator>();
		
		for(DataModelerValidatorDBDescriptor v:getValidatorsDescriptors())
		{
			AbstractDataModelerDBValidator validator = v.getValidatorForDB(databaseId);
				if(validator!=null)
					result.add(validator);
		}

		return result;
	}
	/**
	 * Validadores para un obtejo de una Base de Datos
	 */
	public List<AbstractDataModelerDBValidator> getValidatorsForEObjectAsList(String databaseId , EObject eObject)
	{
		List<AbstractDataModelerDBValidator> result = new ArrayList<AbstractDataModelerDBValidator>();
		
		for(DataModelerValidatorDBDescriptor v:getValidatorsDescriptors())
		{
			AbstractDataModelerDBValidator validator = v.getValidatorForEObject(eObject, databaseId);
				if(validator!=null)
					result.add(validator);
		}

		return result;
	}
	public List<AbstractDataModelerDBValidator> getValidatorsForEClassAsList(String databaseId , EClass eClass)
	{
		List<AbstractDataModelerDBValidator> result = new ArrayList<AbstractDataModelerDBValidator>();
		
		for(DataModelerValidatorDBDescriptor v:getValidatorsDescriptors())
		{
			AbstractDataModelerDBValidator validator = v.getValidatorForEClass(eClass, databaseId);
				if(validator!=null)
					result.add(validator);
		}

		return result;
	}
	/**
	 * Validadores para un obtejo de una Base de Datos
	 */
	public AbstractDataModelerDBValidator getValidatorForValidatorDBId(String validatorDBId , EObject eObject , String databaseId)
	{
		for(DataModelerValidatorDBDescriptor v:getValidatorsDescriptors())
		{
			AbstractDataModelerDBValidator validator = v.getValidatorForValidatorId(validatorDBId, eObject, databaseId);
				if(validator!=null)
					return validator;
		}

		return null;
	}

	/**
	 * Devuelve la instancia singleton del manager
	 * @return
	 */
	public static DataModelerValidationDBManager getInstance()
	{
		if(_Instance==null)
		{
			_Instance = new DataModelerValidationDBManager();
			_Instance.startup();
		}
		return _Instance;	
	}
	public List<DataModelerValidatorDBDescriptor> getValidatorsDescriptors()
	{
		
		return _validators;
	}

	public DataModelerValidatorDBDescriptor getValidatorDescriptor(String validatorId)
	{
		for(DataModelerValidatorDBDescriptor vd:getValidatorsDescriptors())
		{
			if(vd.getValidatorId().equals(validatorId))
				return vd;
		}
		return null;
	}

}
