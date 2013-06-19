package com.isb.datamodeler.ui.model.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.isb.datamodeler.ui.model.Messages;

public class ProjectNameValidator extends AbstractFieldValidator {

	public static final int PROJECT_NAME_MAX_LENGTH = 39;
	
	@Override
	public IStatus validate(String value, String fieldName) {
		if(fieldName.equals("name"))
		{
			if(value.length()>PROJECT_NAME_MAX_LENGTH)
				return new Status(IStatus.ERROR, "not_used", 0, 
						Messages.bind("ProjectNameValidator.validation.name" , String.valueOf(PROJECT_NAME_MAX_LENGTH)), null);
			else // Comprobamos caracteres invalidos
			{
				Character c = containsInvalidChars(value);
				if(c!=null)
				{
					return new Status(IStatus.ERROR, "not_used", 0, Messages.bind("AbstractFieldValidator.invalidchar", c.toString()), null);
				}
			}
		}
		return Status.OK_STATUS;
	}
}
