package com.isb.datamodeler.internal.ui.dialogs;

import org.eclipse.jface.dialogs.IInputValidator;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.project.EProject;

public class SchemaNameValidator implements IInputValidator{

	private EProject _project;
	
	public SchemaNameValidator(EProject project){
		_project = project;
	}
	
	@Override
	public String isValid(String newName) {
		if(newName.length() == 0)
			return Messages.bind("SchemaNameValidator.validLength.min"); //$NON-NLS-1$
		else if (!checkLength(newName))
			return Messages.bind("SchemaNameValidator.validLength.max"); //$NON-NLS-1$
		else if (hasSchemaWithSameName(newName))
			return Messages.bind("SchemaNameValidator.hasSchemaWithSameName", //$NON-NLS-1$
					newName);
		else
		{
			Character c = containsInvalidChars(newName);
			if(c!=null)
				return Messages.bind("SchemaNameValidator.validName", //$NON-NLS-1$
					c.toString());
		}
		return null;
	}
	
	private boolean hasSchemaWithSameName(String schemaName)
	{
		for(ESchema schema:_project.getSchemas())
		{
			if(schema.getName().equalsIgnoreCase(schemaName))
				return true;
		}
		
		return false;
	}
	
	// Mover estas validaciones a la base de datos
	private boolean checkLength(String schemaName)
	{
		if(schemaName.length()>30)
			return false;
		return true;
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
	        	if(!Character.isJavaIdentifierStart(c))
	    			return  new Character(c);
	        }
	        
	        if(!(('a'<=c && c<='z') || ('A'<=c && c<='Z') || ('0'<=c && c<='9') || (c=='_') ))
	            return new Character(c);
	    }
	    return null;
	}
}
