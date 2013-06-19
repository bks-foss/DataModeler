package com.isb.datamodeler.internal.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdapterFactory;

import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.project.EProject;

public class DatamodelerProjectAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		
		if(adaptableObject instanceof IProject)
		{
			IProject project = (IProject)adaptableObject;
			
			if(adapterType==EProject.class)
				return UtilsDataModelerUI.findEProject(project);
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {EProject.class};

	}

}
