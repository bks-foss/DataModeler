package com.isb.datamodeler.internal.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.ecore.EObject;

import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.ui.diagram.EDMDiagram;
import com.isb.datamodeler.ui.project.EProject;

public class DatamodelerDomainNavigatorItemAdapterFactory implements
		IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adaptableObject instanceof DatamodelerDomainNavigatorItem)
		{
			DatamodelerDomainNavigatorItem item = (DatamodelerDomainNavigatorItem)adaptableObject;
			
			if(adapterType.isInstance(item.getEObject()))
				return item.getEObject();
			
			if((adapterType==IProject.class || adapterType==IResource.class) && item.getAdapter(EProject.class)!=null)
			{
				EProject eProject = (EProject)item.getAdapter(EProject.class);
				return eProject.getIProject();
			}
				
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {EProject.class, IProject.class, ESchema.class, EPersistentTable.class, EDMDiagram.class, EObject.class, IResource.class};
	}

}
