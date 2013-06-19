package com.isb.datamodeler.internal.ui.views.actions.workingset;

import java.text.Collator;
import java.util.Comparator;

import org.eclipse.ui.IWorkingSet;

public class DataModelerWorkingSetComparator implements Comparator<IWorkingSet> {
	
	private static ThreadLocal<DataModelerWorkingSetComparator> INSTANCES = new ThreadLocal<DataModelerWorkingSetComparator>() {
		protected synchronized DataModelerWorkingSetComparator initialValue() {
			return new DataModelerWorkingSetComparator();
		}
	};
	
	public static DataModelerWorkingSetComparator getInstance() {
		return (DataModelerWorkingSetComparator) INSTANCES.get();
	}

    private Collator fCollator = Collator.getInstance();

    /**
     * Implements Comparator.
     * 
     * @see Comparator#compare(Object, Object)
     */
    public int compare(IWorkingSet o1, IWorkingSet o2) {
		String name1 = null;
		String name2 = null;

		if (o1 instanceof IWorkingSet) {
			name1 = ((IWorkingSet) o1).getLabel();
		}

		if (o2 instanceof IWorkingSet) {
			name2 = ((IWorkingSet) o2).getLabel();
		}

		int result = fCollator.compare(name1, name2);
		if (result == 0) { // okay, same name - now try the unique id

			if (o1 instanceof IWorkingSet) {
				name1 = ((IWorkingSet) o1).getName();
			}

			if (o2 instanceof IWorkingSet) {
				name2 = ((IWorkingSet) o2).getName();
			}
			result = name1.compareTo(name2);
		}
		return result;
	}
}
