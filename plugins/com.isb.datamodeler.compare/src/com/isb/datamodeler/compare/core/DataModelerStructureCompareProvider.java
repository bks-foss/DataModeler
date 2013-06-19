package com.isb.datamodeler.compare.core;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.ui.viewer.structure.ModelStructureContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.isb.datamodeler.compare.ui.DataModelCompareInput;

/**
 * 
 *
 */
public class DataModelerStructureCompareProvider extends
		ModelStructureContentProvider {

	public DataModelerStructureCompareProvider(
			CompareConfiguration compareConfiguration) {
		super(compareConfiguration);		
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
		if (oldInput != newInput && newInput instanceof DataModelCompareInput) {
			DataModelerCompare comparator = new DataModelerCompare(configuration);
			comparator.loadResources((ICompareInput) newInput);
			ComparisonResourceSnapshot snapshot = comparator.compare(false,false);
			((DataModelCompareInput) newInput).match = snapshot.getMatch();
			((DataModelCompareInput) newInput).diff = snapshot.getDiff();
			if (snapshot != null)
				super.inputChanged( viewer, oldInput, newInput/*new  ModelCompareInput(snapshot.getMatch(), snapshot.getDiff())*/);
			else 
				return;
		}
		else
			super.inputChanged(viewer, oldInput, newInput);
	}	

}
