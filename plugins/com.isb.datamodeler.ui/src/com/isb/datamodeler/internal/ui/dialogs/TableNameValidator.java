package com.isb.datamodeler.internal.ui.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.ETable;

public class TableNameValidator implements IInputValidator{

	private ESchema _schema;
	List<String> _newTableNames;
	
	public TableNameValidator(ESchema schema , List<String> newTableNames){
		_schema = schema;
		_newTableNames = newTableNames;
	}
	
	@Override
	public String isValid(String newName) {
		if (newName.length() == 0) 
			return Messages.bind("TableNameValidator.validLength.min"); //$NON-NLS-1$
		else if (!checkLength(newName))
			return Messages.bind("TableNameValidator.validLength.max"); //$NON-NLS-1$
		else if (hasTableWithSameName(newName)) {
			return Messages.bind("TableNameValidator.hasTableWithSameName", //$NON-NLS-1$
					newName);
		}
		else if(hasNewTableWithSameName(newName)){
			return Messages.bind("TableNameValidator.hasNewTableWithSameName", //$NON-NLS-1$
					newName);
		}
		else
		{
			Character c = containsInvalidChars(newName);
			if(c!=null)
				return Messages.bind("TableNameValidator.validName", //$NON-NLS-1$
					c.toString());
		}
		return null;
	}
	private boolean hasTableWithSameName(String tableName)
	{
		for(ETable table:_schema.getTables())
		{
			if(table.getName().equalsIgnoreCase(tableName))
				return true;
		}
		
		return false;
	}	
	private boolean hasNewTableWithSameName(String tableName)
	{
		for(String s:_newTableNames)
		{
			if(s.equalsIgnoreCase(tableName))
				return true;
		}
		
		return false;
	}
	
	
	// Mover estas validaciones a la base de datos
	private boolean checkLength(String tableName)
	{
		if(tableName.length()>15)
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
