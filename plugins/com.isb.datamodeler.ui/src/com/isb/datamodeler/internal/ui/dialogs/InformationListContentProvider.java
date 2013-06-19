package com.isb.datamodeler.internal.ui.dialogs;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class InformationListContentProvider implements IStructuredContentProvider
{
	private Object[] _elements;

	public InformationListContentProvider()
	{
		super();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof Collection)
			_elements = ((Collection)newInput).toArray();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return _elements;
	}

}
