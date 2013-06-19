package com.isb.datamodeler.internal.ui.wizards.port;

import org.eclipse.jface.viewers.IBasicPropertyConstants;
import org.eclipse.jface.viewers.ViewerComparator;


public class DataTransferWizardCollectionComparator extends ViewerComparator {
	/**
	 * Static instance of this class.
	 */	
    public final static DataTransferWizardCollectionComparator INSTANCE = new DataTransferWizardCollectionComparator();

    /**
     * Creates an instance of <code>DataTransferWizardCollectionSorter</code>.  Since this
     * is a stateless sorter, it is only accessible as a singleton; the private
     * visibility of this constructor ensures this.
     */
    private DataTransferWizardCollectionComparator() {
        super();
    }

    public int category(Object element) {
		if (element instanceof DataModelerPortWizardCollectionElement){
//			String id = ((DataModelerPortWizardCollectionElement)element).getId();
//    		if (WizardsRegistryReader.GENERAL_WIZARD_CATEGORY.equals(id)) {
//				return 1;
//			}
//    		if (WizardsRegistryReader.UNCATEGORIZED_WIZARD_CATEGORY.equals(id)) {
//				return 3;
//			}
    		return 2;
		}
		return super.category(element);
	}

	/**
     *	Return true if this sorter is affected by a property 
     *	change of propertyName on the specified element.
     */
    public boolean isSorterProperty(Object object, String propertyId) {
        return propertyId.equals(IBasicPropertyConstants.P_TEXT);
    }
}
