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

import com.isb.datamodeler.Messages;

/**
 * Clears the selected working set in the working set action group.
 * 
 * @since 2.1
 */
public class ClearWorkingSetAction extends Action {
    private DataModelerWorkingSetFilterActionGroup actionGroup;

    /**
     * Creates a new instance of the receiver.
     * 
     * @param actionGroup the action group this action is created in
     */
    public ClearWorkingSetAction(DataModelerWorkingSetFilterActionGroup actionGroup) {
        super(Messages.bind("DataModelerClearWorkingSetAction.text"));
        Assert.isNotNull(actionGroup);
        setToolTipText(Messages.bind("DataModelerClearWorkingSetAction.toolTip"));
        setEnabled(actionGroup.getWorkingSet() != null);
//        PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
//				IWorkbenchHelpContextIds.CLEAR_WORKING_SET_ACTION);
        this.actionGroup = actionGroup;
    }

    /**
     * Overrides method from Action
     * 
     * @see Action#run
     */
    public void run() {
        actionGroup.setWorkingSet(null);
    }
}
