package com.isb.datamodeler.ui.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.provider.DatamodelerEditPlugin;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerNavigatorLabelProvider {
	
	private AdapterFactoryLabelProvider myAdapterFactoryLabelProvider = new AdapterFactoryLabelProvider(
			DatamodelerDiagramEditorPlugin.getInstance()
					.getItemProvidersAdapterFactory());


	public AdapterFactoryLabelProvider getAdapterFactoryLabelProvider() {
		return myAdapterFactoryLabelProvider;
	}

	public Image getImage(Object element) {

		if (element instanceof DatamodelerDomainNavigatorItem) {
			
			EObject eobject = ((DatamodelerDomainNavigatorItem)element).getEObject();
			
			if(eobject instanceof ESchema && ((ESchema)eobject).isExternal())
			{
				return ExtendedImageRegistry.getInstance().
					getImage(DatamodelerEditPlugin.INSTANCE.getImage("full/obj16/ExternalSchema"));//$NON-NLS-1$
			}
			return myAdapterFactoryLabelProvider
					.getImage(((DatamodelerDomainNavigatorItem) element)
							.getEObject());
		}
		
		if(element instanceof IProject){
			
			if(((IProject)element).isAccessible())
			{
				EProject eProject = UtilsDataModelerUI.findEProject((IProject)element);
				
				return myAdapterFactoryLabelProvider
						.getImage(eProject);
			}
		}
		
		return (new WorkbenchLabelProvider()).getImage(element);
	}

	public String getText(Object element) {
		
		if (element instanceof DatamodelerDomainNavigatorItem)
		{
			EObject eobject = ((DatamodelerDomainNavigatorItem)element).getEObject();
			if(eobject instanceof Diagram){
				return ((Diagram)eobject).getName();
			}
			else if(eobject instanceof ESchema && ((ESchema)eobject).isExternal())
			{
				EProject eProject = (EProject)eobject.eContainer();
				return "<<"+eProject.getApplication()+">> "+myAdapterFactoryLabelProvider.getText(eobject);//$NON-NLS-1$ //$NON-NLS-2$
			}
			else{
				return myAdapterFactoryLabelProvider
						.getText(eobject);
			}
		}
		
		if(element instanceof IProject){
			EProject eProject = UtilsDataModelerUI.findEProject((IProject)element);
			
			return myAdapterFactoryLabelProvider
					.getText(eProject);
		}
		
		//Case of DataModelerWorkingSetPage
		if(element instanceof IAdaptable && ((IAdaptable)element).getAdapter(EProject.class)!=null)
			return myAdapterFactoryLabelProvider
					.getText((EProject)((IAdaptable)element).getAdapter(EProject.class));
		
		return "UNKNOWN";
	}
	
	public final void addListener(ILabelProviderListener listener) {
		myAdapterFactoryLabelProvider.addListener(listener);
	}

	public final void dispose() {
		myAdapterFactoryLabelProvider.dispose();
	}

	public final boolean isLabelProperty(Object element, String property) {
		return myAdapterFactoryLabelProvider.isLabelProperty(element, property);
	}

	public final void removeListener(ILabelProviderListener listener) {
		myAdapterFactoryLabelProvider.removeListener(listener);
	}


}
