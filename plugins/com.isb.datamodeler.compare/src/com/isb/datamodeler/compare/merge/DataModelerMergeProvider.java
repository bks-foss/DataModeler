package com.isb.datamodeler.compare.merge;

import java.util.Map;

import org.eclipse.emf.compare.diff.merge.IMerger;
import org.eclipse.emf.compare.diff.merge.IMergerProvider;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.util.EMFCompareMap;

/**
 * Provider para los mergeadores específicos de DataModeler que hemos tenido
 * que copiar de EMF Compare y adaptarlos en algunos casos para nosotros.
 * 
 * Existe un bug de EMFCompare para que sean extensibles los mergeadores.
 * 
 * @author xIS06722
 *
 */
public class DataModelerMergeProvider implements IMergerProvider {

	private Map<Class<? extends DiffElement>, Class<? extends IMerger>> _dataModelerMergerTypes = null;
	
	@Override
	public Map<Class<? extends DiffElement>, Class<? extends IMerger>> getMergers() {
		if (_dataModelerMergerTypes == null) {
			_dataModelerMergerTypes = new EMFCompareMap<Class<? extends DiffElement>, Class<? extends IMerger>>();
			_dataModelerMergerTypes.put(UpdateReference.class, DataModelerUpdateReferenceMerger.class);
			_dataModelerMergerTypes.put(ModelElementChangeLeftTarget.class, DataModelerChangeLeftTargetMerger.class);
			_dataModelerMergerTypes.put(ModelElementChangeRightTarget.class, DataModelerChangeRightTargetMerger.class);
			_dataModelerMergerTypes.put(ReferenceChangeLeftTarget.class, DataModelerReferenceChangeLeftTargetMerge.class);
			_dataModelerMergerTypes.put(ReferenceChangeRightTarget.class, DataModelerReferenceChangeRightTargetMerge.class);
			_dataModelerMergerTypes.put(UpdateAttribute.class, DataModelerUpdateAttributeMerger.class);
		}
		
		return _dataModelerMergerTypes;
	}

}
