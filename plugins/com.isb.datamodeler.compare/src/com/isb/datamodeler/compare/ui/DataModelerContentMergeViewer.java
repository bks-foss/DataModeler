package com.isb.datamodeler.compare.ui;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.ui.viewer.content.ParameterizedContentMergeViewer;
import org.eclipse.emf.compare.ui.viewer.content.part.ModelContentMergeTabFolder;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;

import com.isb.datamodeler.compare.core.DataModelerCompare;
import com.isb.datamodeler.compare.merge.DataModelerMergeMessages;

/**
 * Viewer espec�fico que lanza nuestro comparador EMF 
 * (con su DataModelerMatchEngine y DataModelerDiffEngine).
 * 
 * @author Alfonso
 *
 */
public class DataModelerContentMergeViewer extends
		ParameterizedContentMergeViewer {				

	@SuppressWarnings("restriction")
	public DataModelerContentMergeViewer(Composite parent,
			CompareConfiguration config) {		
		super(parent, config);
		Utilities.getBoolean(config, CompareEditor.CONFIRM_SAVE_PROPERTY, false);			
	}
			
	
	@Override
	protected void handleDispose(DisposeEvent event) {	
		super.handleDispose(event);
	}
	
	@Override
	public void setInput(Object input) {	
		
		if (input instanceof ICompareInput) {
			DataModelerCompare comparator = new DataModelerCompare(configuration);
			comparator.loadResources((ICompareInput) input);
			ComparisonResourceSnapshot snapshot = comparator.compare(false,false);
			((DataModelCompareInput) input).match = snapshot.getMatch();
			((DataModelCompareInput) input).diff = snapshot.getDiff();
			if (snapshot != null)
				super.setInput(input);
			else
				return;
		}
		else
			super.setInput(input);				
	}	
	
	@Override
	protected ModelContentMergeTabFolder createModelContentMergeTabFolder(
			Composite composite, int side) {		
		return new DataModelerContentMergeTabFolder(this, composite, side);
	}
	
	public boolean isDirty () {
		if (isLeftDirty() || isRightDirty())
			return true;
		else
			return false;
	}
	
	public void clearSelection ()
	{
		if (currentSelection != null)
			currentSelection.clear();
	}
	
	@Override
	protected void copyDiffLeftToRight() {	
		DataModelerMergeMessages messages = DataModelerMergeMessages.getInstance();
		super.copyDiffLeftToRight();
		messages.showMessages();
		messages.dispose();
		
	}
	
	@Override
	protected void copyDiffRightToLeft() {
		DataModelerMergeMessages messages = DataModelerMergeMessages.getInstance();		
		super.copyDiffRightToLeft();
		messages.showMessages();
		messages.dispose();
	}
		
	public void updateToolItems(boolean enabled) {		
		//super.updateToolItems();
		super.switchCopyState(enabled);
	}
	
		
	public void navigate(boolean down) {		
		super.navigate(down);
	}
	
		
}
