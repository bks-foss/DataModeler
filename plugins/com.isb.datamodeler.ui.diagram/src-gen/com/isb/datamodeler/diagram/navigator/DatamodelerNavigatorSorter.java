package com.isb.datamodeler.diagram.navigator;

import org.eclipse.jface.viewers.ViewerSorter;

import com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry;

/**
 * @generated
 */
public class DatamodelerNavigatorSorter extends ViewerSorter {

	/**
	 * @generated
	 */
	private static final int GROUP_CATEGORY = 7004;

	/**
	 * @generated
	 */
	private static final int SHORTCUTS_CATEGORY = 7003;

	/**
	 * @generated
	 */
	public int category(Object element) {
		if (element instanceof DatamodelerNavigatorItem) {
			DatamodelerNavigatorItem item = (DatamodelerNavigatorItem) element;
			if (item.getView().getEAnnotation("Shortcut") != null) { //$NON-NLS-1$
				return SHORTCUTS_CATEGORY;
			}
			return DatamodelerVisualIDRegistry.getVisualID(item.getView());
		}
		return GROUP_CATEGORY;
	}

}
