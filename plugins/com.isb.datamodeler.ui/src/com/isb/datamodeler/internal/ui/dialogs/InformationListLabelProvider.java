package com.isb.datamodeler.internal.ui.dialogs;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.internal.ui.views.actions.DataModelerProblems;
import com.isb.datamodeler.model.core.validation.DataModelDiagnostic;

public class InformationListLabelProvider implements ITableLabelProvider
{

	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if(element instanceof DataModelerProblems)
		{
			
			if (columnIndex==0)
			{
				DataModelerProblems vmp = (DataModelerProblems)element;
				if(vmp.getSeverity()==DataModelDiagnostic.ERROR)
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
				else if(vmp.getSeverity()==DataModelDiagnostic.WARNING)
					return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
			}
		}
		return null;
		
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof DataModelerProblems)
		{
			if (columnIndex==0)
			{
				if(((DataModelerProblems)element).getDBDepends()!=null && !((DataModelerProblems)element).getDBDepends().isEmpty())
				{
					return "["+((DataModelerProblems)element).getDBDepends()+"] "+((DataModelerProblems)element).getMessage();
					
				}
				else return ((DataModelerProblems)element).getMessage();
			}
		}

		return null;
	}
	
	@Override
	public void addListener(ILabelProviderListener listener) {
	}
	
	@Override
	public void dispose() {
	}
	
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return true;
	}
	
	@Override
	public void removeListener(ILabelProviderListener listener) {
	}
}
