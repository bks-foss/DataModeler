package com.isb.datamodeler.diagram.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;

import com.isb.datamodeler.ui.diagram.Messages;
import com.isb.datamodeler.ui.model.validation.AbstractFieldValidator;

public class DiagramNameValidator extends AbstractFieldValidator {

	public static final int DIAGRAM_NAME_MAX_LENGTH = 42;
	
	public IStatus validate(String value, String fieldName) {
		if(fieldName.equals("name"))
		{
			if(value.length()>DIAGRAM_NAME_MAX_LENGTH)
				return new Status(IStatus.ERROR, "not_used", 0, 
						Messages.bind("DiagramNameValidator.validation.name.length", String.valueOf(DIAGRAM_NAME_MAX_LENGTH)), null);
			else // Comprobamos caracteres invalidos
			{
				Character c = containsInvalidChars(value);
				if(c!=null)
				{
					return new Status(IStatus.ERROR, "not_used", 0, Messages.bind("DiagramNameValidator.invalidchar", c.toString()), null);
				}
			}
		}
		return Status.OK_STATUS;
	}

}
