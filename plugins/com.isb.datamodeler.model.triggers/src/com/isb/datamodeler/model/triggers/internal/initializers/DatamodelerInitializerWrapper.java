package com.isb.datamodeler.model.triggers.internal.initializers;

import com.isb.datamodeler.model.triggers.initializers.IDatamodelerInitializer;

public class DatamodelerInitializerWrapper {

	private IDatamodelerInitializer _initializer;
	
	private int _priority;
	
	public DatamodelerInitializerWrapper(IDatamodelerInitializer initializer, 
			Integer priority)
	{
		_initializer = initializer;
		_priority = priority;
	}
	
	public IDatamodelerInitializer getInitializer()
	{
		return _initializer;
	}
	
	public int getPriority()
	{
		return _priority;
	}
}
