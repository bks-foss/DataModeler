package com.isb.datamodeler.internal.ui.dialogs;

import java.util.List;

import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.EPersistentTable;

public class PossibleRelationElement {
	
	private List<EColumn> _suggestedColumns;
	private EUniqueConstraint _uk;
	
	
	private boolean _create;
	private EPersistentTable _parentTable;
	private EPersistentTable _childTable;
	
	public PossibleRelationElement(List<EColumn> suggestedColumns , 
			EUniqueConstraint uk,
			EPersistentTable parentTable,
			EPersistentTable childTable,
			boolean create) 
	{
		super();
		_suggestedColumns = suggestedColumns;
		_uk = uk;
		_parentTable = parentTable;
		_childTable = childTable;
		_create = create;
		
	}
	public List<EColumn> getSuggestedColumns() {
		return _suggestedColumns;
	}

	public boolean isCreate() {
		return _create;
	}

	public List<EColumn> getColumnsUK() {
		return _uk.getMembers();
	}
	public EPersistentTable getParentTable()
	{
		return _parentTable;
	}
	public void setCreate(boolean create) {
		this._create = create;
	}
	public EPersistentTable getChildTable()
	{
		return _childTable;
	}
	public EUniqueConstraint getUk() 
	{
		return _uk;
	}

}
