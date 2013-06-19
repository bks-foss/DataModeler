package com.isb.datamodeler.internal.ui.dialogs;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

public class CheckBoxEditingSupport extends EditingSupport {

	
	private CellEditor _editor;
	private TreeViewer treeViewer;
	PossibleRelationsDialog _dialog;

	public CheckBoxEditingSupport(TreeViewer viewer , PossibleRelationsDialog dialog) {
		
		super(viewer);
		_editor = new CheckboxCellEditor(viewer.getTree(), SWT.CHECK);
		treeViewer = viewer;
		_dialog = dialog;
	}
	
	@Override
	protected CellEditor getCellEditor(Object element) {

		if(element instanceof PossibleRelationElement)
			return _editor;
		return null;
	}

	@Override
	protected boolean canEdit(Object element) {		
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		return ((PossibleRelationElement)element).isCreate();
	}

	@Override
	protected void setValue(Object element, Object value) {
		boolean transform = (Boolean)value;
		((PossibleRelationElement)element).setCreate(transform);
		
		treeViewer.refresh();
		_dialog.updateOKButton();
	}
	
}
