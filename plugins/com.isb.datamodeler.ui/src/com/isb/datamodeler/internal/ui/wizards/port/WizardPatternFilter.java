package com.isb.datamodeler.internal.ui.wizards.port;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.dialogs.PatternFilter;

/**
 * A class that handles filtering wizard node items based on a supplied matching
 * string and keywords
 * 
 * @since 3.2
 * 
 */
public class WizardPatternFilter extends PatternFilter {

	/**
	 * Create a new instance of a WizardPatternFilter 
	 * @param isMatchItem
	 */
	public WizardPatternFilter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.internal.dialogs.PatternFilter#isElementSelectable(java.lang.Object)
	 */
	public boolean isElementSelectable(Object element) {
		return element instanceof DataModelerPortWizardElement;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.internal.dialogs.PatternFilter#isElementMatch(org.eclipse.jface.viewers.Viewer, java.lang.Object)
	 */
	protected boolean isLeafMatch(Viewer viewer, Object element) {
		if (element instanceof DataModelerPortWizardCollectionElement) {
			return false;
		}
		
		if (element instanceof DataModelerPortWizardElement) {
			DataModelerPortWizardElement desc = (DataModelerPortWizardElement) element;
			String text = desc.getLabel();
			if (wordMatches(text)) {
				return true;
			}

			String[] keywordLabels = desc.getKeywordLabels();
			for (int i = 0; i < keywordLabels.length; i++) {
				if (wordMatches(keywordLabels[i]))
					return true;
			}
		}
		return false;
	}

}

