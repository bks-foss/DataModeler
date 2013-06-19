package com.isb.datamodeler.diagram.sheet;

import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.tables.EColumn;

public class ColumnDataTypePropertySection extends DatamodelerPropertySection {

	@Override
	protected Object transformSelection(Object selected) {
		Object transformSelection = super.transformSelection(selected);
		EColumn selectedColumn = null;
		if(transformSelection instanceof EColumn)
			selectedColumn = (EColumn)transformSelection;
		else if(transformSelection instanceof DatamodelerDomainNavigatorItem)
			selectedColumn = (EColumn) ((DatamodelerDomainNavigatorItem) transformSelection).getEObject();
		return selectedColumn.getDataType();
	}

}
