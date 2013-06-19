package com.isb.datamodeler.ui.triggers.configurators.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class OptionsMessageDialogWithToggle extends MessageDialogWithToggle {
	
	private int selectedOptionValue;
	
	private String[] optionLabels;
	
	private int[] optionValues;
	
	public static final String LAST_SELECTED_OPTION = "lastOption"; //$NON-NLS-1$
	
	public OptionsMessageDialogWithToggle(Shell parentShell, String dialogTitle,
			String message,
			String[] optionLabels,
			int[] optionValues,
			int defaultOption,
			String toggleMessage, 
			boolean toggleState) {
		
		super(parentShell, dialogTitle, null, message, QUESTION,
				new String[] { IDialogConstants.OK_LABEL }, 0, toggleMessage, toggleState);
		
		this.optionLabels = optionLabels;
		this.optionValues = optionValues;
		selectedOptionValue = defaultOption;
	}

	@Override
	protected Control createCustomArea(Composite parent) {
		
		 //Dummy label for spacing purposes
		new Label(parent, SWT.NULL);

		for(int i=0;i<optionLabels.length;i++)
			createRadioButton(parent, optionLabels[i], optionValues[i]);
		
		 //Dummy label for spacing purposes
		new Label(parent, SWT.NULL);
		
		return parent;
	}
	
    /**
     * Creates a toggle button without any text or state.  The text and state
     * will be created by <code>createDialogArea</code>. 
     * 
     * @param parent
     *            The composite in which the toggle button should be placed;
     *            must not be <code>null</code>.
     * @return The added toggle button; never <code>null</code>.
     */
    protected Button createRadioButton(Composite parent, String msg, int value) {
        final Button button = new Button(parent, SWT.RADIO | SWT.LEFT);

        GridData data = new GridData(SWT.NONE);
        data.horizontalSpan = 2;
        button.setLayoutData(data);
        button.setFont(parent.getFont());
        button.setText(msg);
        button.setData(value);
        
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
            	selectedOptionValue = (Integer)button.getData();
            }

        });
        
        if(value==selectedOptionValue)
        	button.setSelection(true);
        

        return button;
    }
    
    public int getSelectedOption()
    {
    	return selectedOptionValue;
    }

}
