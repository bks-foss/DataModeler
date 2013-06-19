package com.isb.datamodeler.ui.model.validation;


public abstract class AbstractFieldValidator implements IFieldValidator {

	protected Character containsInvalidChars(String name) 
	{
		if(name.length()==0)
			return new Character(' ');
	    for (int i = 0; i < name.length(); i++) 
	    {
	        char c = name.charAt(i);
	        if(i==0)
	        {
	        	// El primer carácter de la serie debe ser un carácter alfabético (a-z, A-Z del alfabeto inglés). 
	        	if(!(('a'<=c && c<='z') || ('A'<=c && c<='Z')))
		            return new Character(c);
	        	if(c=='ñ' || c=='Ñ')
	 	        	return new Character(c);
	        }
	        else //El resto debe ser un carácter alfabético, numérico (0-9) o _. 
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
