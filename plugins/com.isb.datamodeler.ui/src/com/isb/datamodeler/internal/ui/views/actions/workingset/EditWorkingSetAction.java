/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.isb.datamodeler.internal.ui.views.actions.workingset;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetEditWizard;

import com.isb.datamodeler.Messages;

/**
 * Displays an IWorkingSetEditWizard for editing a working set.
 * 
 * @since 2.1
 */
public class EditWorkingSetAction extends Action {
    private Shell shell;

    private DataModelerWorkingSetFilterActionGroup actionGroup;

    /**
     * Creates a new instance of the receiver.
     * 
     * @param actionGroup the action group this action is created in
     * @param shell the parent shell
     */
    public EditWorkingSetAction(DataModelerWorkingSetFilterActionGroup actionGroup,
            Shell shell) {
        super(Messages.bind("DataModelerEditWorkingSetAction.text"));
        Assert.isNotNull(actionGroup);
        setToolTipText(Messages.bind("DataModelerEditWorkingSetAction.toolTip")); 

        this.shell = shell;
        this.actionGroup = actionGroup;
//        PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
//				IWorkbenchHelpContextIds.EDIT_WORKING_SET_ACTION);
    }

    /**
     * Overrides method from Action
     * 
     * @see Action#run
     */
    public void run() {
        IWorkingSetManager manager = PlatformUI.getWorkbench()
                .getWorkingSetManager();
        IWorkingSet workingSet = actionGroup.getWorkingSet();

        if (workingSet == null) {
            setEnabled(false);
            return;
        }
        IWorkingSetEditWizard wizard = manager
                .createWorkingSetEditWizard(workingSet);
        if (wizard == null) {
            String title = Messages.bind("DataModelerEditWorkingSetAction.error_nowizard_title"); 
            String message = Messages.bind("DataModelerEditWorkingSetAction.error_nowizard_message");
            MessageDialog.openError(shell, title, message);
            return;
        }
        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.create();
        if (dialog.open() == Window.OK) {
			actionGroup.setWorkingSet(wizard.getSelection());
		}
    }
}
