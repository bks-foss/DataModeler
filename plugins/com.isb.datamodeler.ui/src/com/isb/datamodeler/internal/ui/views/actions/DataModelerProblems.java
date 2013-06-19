package com.isb.datamodeler.internal.ui.views.actions;

import org.eclipse.emf.ecore.EObject;

public class DataModelerProblems {

	private String _message;
	private int _severty;
	private String _dbDepends;
	private Object _element;
	
	public DataModelerProblems(int severity , String message, String dbDepends, Object element) 
	{
		super();
		_severty = severity;
		_message = message;
		_dbDepends = dbDepends;
		_element = element;
	}
	
	public int getSeverity()
	{
		return _severty;
	}
	
	public String getMessage()
	{
		return _message;
	}
	
	public String getDBDepends()
	{
		return _dbDepends;
	}
	
	public Object getElement()
	{
		return _element;
	}
}
