package com.isb.datamodeler.model.triggers;

import org.eclipse.emf.transaction.Transaction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;


public abstract class AbstractTriggerConfigurator {
	
	protected static String CURRENT_ROOT_TRANSACTION = "currentRootTransaction";
	
	protected IPreferenceStore configuratorPreferences;
	
	public AbstractTriggerConfigurator()
	{
		configuratorPreferences = initializePreferences(); 
	}
	
	public final void configure(IParametrizableTrigger trigger, Transaction rootTransaction)
	{
		if(!excecuteConfigurator(rootTransaction))
			return;
		
		int currentRootTransaction = configuratorPreferences.getInt(CURRENT_ROOT_TRANSACTION);
		
		//Las preferencias las reiniciamos cuando estamos en una nueva rootTransaction
		if(currentRootTransaction!=rootTransaction.hashCode())
		{
			configuratorPreferences = initializePreferences(); 
			configuratorPreferences.setValue(CURRENT_ROOT_TRANSACTION, rootTransaction.hashCode());
		}
		
		configure(trigger);
	}
	
	/**
	 * Sobreescribir si se quiere especificar una condicion particular para la ejecucion del configurador
	 * @return
	 */
	protected boolean excecuteConfigurator(Transaction rootTransaction)
	{
		return true;
	}
	
	protected IPreferenceStore initializePreferences()
	{
		return new PreferenceStore();
	}
	
	public abstract void configure(IParametrizableTrigger trigger);
	
}
