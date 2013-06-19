package com.isb.datamodeler.model.core.validation;

import org.eclipse.core.runtime.IConfigurationElement;


public class DataModelerCategoryDescriptor
{
	
	public static String ATTR_ID 		= "id"; //$NON-NLS-1$
	public static String ATTR_NAME		= "name"; //$NON-NLS-1$
	public static String ATTR_MANDATORY = "mandatory"; //$NON-NLS-1$
	public final static String ELEMENT_DESCRIPTION = "description";   //$NON-NLS-1$


/**
 * Id de la categor�a: identificador �nico compuesto por el id del plugin que la declara m�s
 * un nombre
 */	
private String _id;
/**
 * Nombre que se ofrece al usuario (internacionalizable)
 */
private String _name;

/**
 * Orden de la categor�a
 */
private boolean _mandatory = true;

private String _description = "Categoria sin descripcion" ;

public DataModelerCategoryDescriptor(IConfigurationElement category)
{
	_id = category.getAttribute(ATTR_ID);
	_name = category.getAttribute(ATTR_NAME);
	_mandatory = Boolean.parseBoolean(category.getAttribute(ATTR_MANDATORY));

	IConfigurationElement[] descriptions = category.getChildren(ELEMENT_DESCRIPTION);
	if (descriptions.length > 0)
		_description = descriptions[0].getValue();
}
public String getDescription()
{
	return _description;
}
/**
 * Devuelve el id de la categor�a
 * @return
 */
public String getId()
{
	return _id;
}
/**
 * Devuelve el nombre de la categor�a
 * @return
 */
public String getName()
{
	return _name;
}

public boolean isMandatory()
{
	return _mandatory;
}

}
