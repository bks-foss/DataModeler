package com.isb.datamodeler.ui.model.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.isb.datamodeler.ui.model.Messages;

public class ApplicationNameValidator extends AbstractFieldValidator {

	public static final int APPLICATION_NAME_MAX_LENGTH = 6;
	
	@Override
	public IStatus validate(String value, String fieldName) {
		if(fieldName.equals("name"))
		{
			if(value.length()>APPLICATION_NAME_MAX_LENGTH)
				return new Status(IStatus.ERROR, "not_used", 0, 
						Messages.bind("ApplicationNameValidator.validation.name", String.valueOf(APPLICATION_NAME_MAX_LENGTH)), null);
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
