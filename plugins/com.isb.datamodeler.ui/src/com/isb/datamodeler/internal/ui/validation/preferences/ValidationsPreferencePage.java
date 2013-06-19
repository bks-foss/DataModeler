package com.isb.datamodeler.internal.ui.validation.preferences;


import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class ValidationsPreferencePage
		extends PreferencePage
		implements IWorkbenchPreferencePage {
	
	private ValidationsSelectionBlock validationsComposite;
    
	// implements the inherited method
	@Override
    protected Control createContents(Composite parent) {
		Composite result = new Composite(parent, SWT.NONE);
		FillLayout layout = new FillLayout();
		result.setLayout(layout);
        
		validationsComposite = new ValidationsSelectionBlock();
		validationsComposite.createComposite(result);
        
		applyDialogFont(result);
		
		return result;
	}

	public void init(IWorkbench workbench) {
		// nothing to do
	}
	
	// redefines the inherited method
	@Override
    public boolean performOk() {
		return validationsComposite.performOk();
	}
	
	// extends the inherited method
	@Override
    protected void performDefaults() {
		validationsComposite.performDefaults();
	}
}
