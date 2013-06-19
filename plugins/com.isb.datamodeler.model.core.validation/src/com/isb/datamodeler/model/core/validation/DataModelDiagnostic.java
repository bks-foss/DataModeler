package com.isb.datamodeler.model.core.validation;

import org.eclipse.emf.common.util.BasicDiagnostic;

public class DataModelDiagnostic extends BasicDiagnostic{
	
	public static final String ATTR_MARKER_VALIDATOR_ID = "validatorId"; //$NON-NLS-1$
	public static final int IS_VALID = 1;
	public static final String DIAGNOSTIC_SOURCE = "com.isb.datamodeler.model.core.validation";
	
	String validatorId;
	String category;
	String validationCode;
	String dbDepends;

	public String getDBDepends()
	{
		return dbDepends;
	}
	public String getValidatorId() {
		return validatorId;
	}

	public void setValidatorId(String validatorId) {
		this.validatorId = validatorId;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getValidationCode() {
		return validationCode;
	}
	
	public void setMessage(String newMessage)
	{
		this.message = newMessage;
	}

	public DataModelDiagnostic()
	{
		super();
	}
	public DataModelDiagnostic(int severity, String message, 
			String validatorId, Object [] data, String category, String validationCode) {

		this(severity, message, validatorId ,data, category ,validationCode, "");
		
	}
	public DataModelDiagnostic(int severity, String message, 
			String validatorId, Object [] data, String category, String validationCode,String dbDepends) {

		super(severity, DIAGNOSTIC_SOURCE, IS_VALID, message, data);
		
		this.validatorId = validatorId;
		this.category = category;
		this.validationCode = validationCode;
		this.dbDepends = dbDepends;
	}

	public DataModelDiagnostic(int severity, String source, int code,
			String message, String validatorId, Object[] data, String category, String validationCode) {
		super(severity, source, code, message, data);
		
		this.validatorId = validatorId;
		this.category = category;
		this.validationCode = validationCode;
	}

	/**
	   * A diagnostic indicating that everything is okay.
	   */
	public static DataModelDiagnostic OK_INSTANCE = 
	    new DataModelDiagnostic
	      (OK, DIAGNOSTIC_SOURCE, 0, org.eclipse.emf.common.CommonPlugin.INSTANCE.getString("_UI_OK_diagnostic_0"), null, null, "", "");

	/*
	 * Sobrescribimos el método que devuelve el mensaje para que aparezca la información del código.
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.emf.common.util.BasicDiagnostic#getMessage()
	 */
	@Override
	public String getMessage()
	{
		return getValidationCode() +" "+ super.getMessage();
	}
//	@Override
//	public boolean equals(Object obj) {
//		
//		if(obj instanceof DataModelDiagnostic)
//		{
//			
//			DataModelDiagnostic diagnostic = (DataModelDiagnostic)obj;
//			if(getData().equals(diagnostic.getData()) && 
//					getMessage().equals(diagnostic.getMessage())&& 
//					getValidationCode().equals(diagnostic.getValidationCode()) && 
//					getValidatorId().equals(diagnostic.getValidatorId()))
//				return true;
//		}
//		return super.equals(obj);
//	}
}
