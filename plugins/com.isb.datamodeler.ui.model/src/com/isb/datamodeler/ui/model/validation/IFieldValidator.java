package com.isb.datamodeler.ui.model.validation;

import org.eclipse.core.runtime.IStatus;

public interface IFieldValidator {
	public IStatus validate(String value , String fieldName);
}
