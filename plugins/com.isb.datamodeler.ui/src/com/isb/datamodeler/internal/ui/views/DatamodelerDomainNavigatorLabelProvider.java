package com.isb.datamodeler.internal.ui.views;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.views.DataModelerNavigatorLabelProvider;

public class DatamodelerDomainNavigatorLabelProvider implements
		ICommonLabelProvider{

	private DataModelerNavigatorLabelProvider internalLabelProvider = DataModelerUI.getDefault().getInternalDataModelerNavigatorLabelProvider();
	
	public void init(ICommonContentExtensionSite aConfig) {
	}

	public Image getImage(Object element) {
		
		return internalLabelProvider.getImage(element);
	}

	public String getText(Object element) {
		return internalLabelProvider.getText(element);
	}

	public void addListener(ILabelProviderListener listener) {
		internalLabelProvider.addListener(listener);
	}

	public void dispose() {
		internalLabelProvider.dispose();
	}

	public boolean isLabelProperty(Object element, String property) {
		return internalLabelProvider.isLabelProperty(element, property);
	}

	public void removeListener(ILabelProviderListener listener) {
		internalLabelProvider.removeListener(listener);
	}

	public void restoreState(IMemento aMemento) {
	}

	public void saveState(IMemento aMemento) {
	}

	public String getDescription(Object anElement) {
		return null;
	}
}
