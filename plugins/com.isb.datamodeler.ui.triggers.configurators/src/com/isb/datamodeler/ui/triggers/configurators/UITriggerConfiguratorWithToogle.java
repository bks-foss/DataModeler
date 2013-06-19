package com.isb.datamodeler.ui.triggers.configurators;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.isb.datamodeler.ui.triggers.configurators.dialogs.OptionsMessageDialogWithToggle;

public abstract class UITriggerConfiguratorWithToogle extends
		UITriggerConfigurator {


	@Override
	protected IPreferenceStore initializePreferences() {
		// TODO Auto-generated method stub
		IPreferenceStore preferences = super.initializePreferences();
		
		preferences.setValue(MessageDialogWithToggle.PROMPT, true);
		preferences.setValue(MessageDialogWithToggle.ALWAYS, false);
		preferences.setValue(MessageDialogWithToggle.NEVER, false);
		
		return preferences;
	}

	private void setAlways(boolean always)
	{
		configuratorPreferences.setValue(MessageDialogWithToggle.ALWAYS, always);
	}
	
	private void setNever(boolean never)
	{
		configuratorPreferences.setValue(MessageDialogWithToggle.NEVER, never);
	}

	private void setPrompt(boolean prompt)
	{
		configuratorPreferences.setValue(MessageDialogWithToggle.PROMPT, prompt);
	}
	
	private boolean isAlways()
	{
		return configuratorPreferences.getBoolean(MessageDialogWithToggle.ALWAYS);
	}
	

	private void setLastSelectedOption(int option)
	{
		configuratorPreferences.setValue(OptionsMessageDialogWithToggle.LAST_SELECTED_OPTION, option);
	}
	
	protected int getLastSelectedOption()
	{
		return configuratorPreferences.getInt(OptionsMessageDialogWithToggle.LAST_SELECTED_OPTION);
	}

	private boolean isPrompt()
	{
		return configuratorPreferences.getBoolean(MessageDialogWithToggle.PROMPT);
	}

	protected boolean openYesNoQuestion(
            String title, String message, String toggleMessage,
            boolean toggleState)
	{
		//Si previamente se seleccionó "Si a todo" o "No a Todo", devolvemos el resultado guardado 
		if(!isPrompt())
			return isAlways();
		
		MessageDialogWithToggle dialog = MessageDialogWithToggle
		.openYesNoQuestion(Display.getCurrent().getActiveShell(),
				title, message,
				toggleMessage, false, null,
				null);

		boolean result =  dialog.getReturnCode() == IDialogConstants.YES_ID;

		//Se seleccionó la opcion "Hacer lo mismo para el resto de tablas:
		if(dialog.getToggleState())
		{
			setPrompt(false);
			setAlways(result);
			setNever(!result);
		}

		return result;
	}
	
	protected int openOptionsDialog(
            String title, String message, String[] optionLabels, int[] optionValues, int defaultOption, int cancelOption, String toggleMessage,
            boolean toggleState)
	{
		//Si previamente se seleccionó "Si a todo" o "No a Todo", devolvemos el resultado guardado 
		if(!isPrompt())
			return getLastSelectedOption();
		
		OptionsMessageDialogWithToggle dialog = getDialog(title, 
														  message, 
														  optionLabels,
														  optionValues,
														  defaultOption,
														  toggleMessage,
														  false);
		
		dialog.open();
		
		int selectedOption = dialog.getSelectedOption(); 
		
		//Se cerró el dialogo con la X
		if(dialog.getReturnCode()==SWT.DEFAULT)
			return cancelOption;
		
		//Se seleccionó la opcion "Hacer lo mismo para el resto de tablas:
		if(dialog.getToggleState())
			setPrompt(false);

		setLastSelectedOption(selectedOption);
		
		return selectedOption; 

	}
	
	protected OptionsMessageDialogWithToggle getDialog(
            String title, String message, String[] optionLabels, int[] optionValues, int defaultOption, String toggleMessage,
            boolean toggleState)
	{
		return new OptionsMessageDialogWithToggle( 
				   Display.getCurrent().getActiveShell(),
				   title, 
				   message, 
				   optionLabels,
				   optionValues,
				   defaultOption,
				   toggleMessage,
				   false);
	}

}
