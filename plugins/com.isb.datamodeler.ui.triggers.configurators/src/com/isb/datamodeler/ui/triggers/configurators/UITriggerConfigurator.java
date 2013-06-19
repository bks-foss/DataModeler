package com.isb.datamodeler.ui.triggers.configurators;

import org.eclipse.emf.transaction.Transaction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.isb.datamodeler.model.triggers.AbstractTriggerConfigurator;
import com.isb.datamodeler.ui.triggers.configurators.dialogs.OptionsMessageDialog;

public abstract class UITriggerConfigurator extends
		AbstractTriggerConfigurator {
	
	public static String NOT_EXECUTE_UI_CONFIGURATOR = "notExecuteUIConfigurator";
	
	/**
	 * Si en la transaccion se setea la propiedad NOT_EXECUTE_UI_CONFIGURATOR a True, no se ejecutan los UITriggerConfigurator
	 */
	protected boolean excecuteConfigurator(Transaction rootTransaction)
	{
		return !(rootTransaction.getOptions().get(NOT_EXECUTE_UI_CONFIGURATOR)==Boolean.TRUE);
	}

	/**
	 * Abre un dialogo con opciones
	 * @param title
	 * @param message
	 * @param optionLabels
	 * @param optionValues
	 * @param defaultOption
	 * @return
	 */
	protected int openOptionsDialog(
            String title, String message, String[] optionLabels, int[] optionValues, int defaultOption, int cancelOption)
	{
		OptionsMessageDialog dialog = new OptionsMessageDialog( 
				   Display.getCurrent().getActiveShell(),
				   title, 
				   message, 
				   optionLabels,
				   optionValues,
				   defaultOption);
		
		dialog.open();
		
		int selectedOption = dialog.getSelectedOption(); 
		
		//Se cerró el dialogo con la X
		if(dialog.getReturnCode()==SWT.DEFAULT)
			return cancelOption;
		
		return selectedOption; 

	}

}
