package com.isb.datamodeler.ui.views.workingsets;

import org.eclipse.ui.IWorkingSet;

import com.isb.datamodeler.internal.ui.views.DatamodelerDomainNavigatorLabelProvider;

/**
 * Label Provider used when the Root Mode is Working Sets on the DataModeler View
 * @author xIS05655
 *
 */
public class DataModelerWorkingSetsLabelProvider extends
		DatamodelerDomainNavigatorLabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof IWorkingSet)
			return ((IWorkingSet)element).getName();
		
		return super.getText(element);
	}

}
