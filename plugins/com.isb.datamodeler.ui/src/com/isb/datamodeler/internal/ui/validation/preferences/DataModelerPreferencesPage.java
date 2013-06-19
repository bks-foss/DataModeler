/**
 * 
 */
package com.isb.datamodeler.internal.ui.validation.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.isb.datamodeler.Messages;

public class DataModelerPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage 
{
	protected Control createContents(Composite parent) 
	{
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label descr=  new Label(composite, SWT.NONE);
		descr.setText(Messages.bind("DataModelerPreferencesPage.description.text")); //$NON-NLS-1$
		
		return composite;
	}

	public void init(IWorkbench workbench) 
	{
		
	}
}
