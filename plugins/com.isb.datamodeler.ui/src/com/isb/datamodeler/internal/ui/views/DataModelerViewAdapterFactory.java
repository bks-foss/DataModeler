package com.isb.datamodeler.internal.ui.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.isb.datamodeler.model.core.DataModelerUtils;

public class DataModelerViewAdapterFactory implements IAdapterFactory {

    /**
     * My editing domain provider.
     */
    private IEditingDomainProvider domainProvider = new IEditingDomainProvider() {

        public EditingDomain getEditingDomain() {
            return DataModelerUtils.getDataModelerEditingDomain();
        }
    };
	
	 /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
     *      java.lang.Class)
     */
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adaptableObject instanceof DataModelerView) {
        	if (IPropertySheetPage.class == adapterType)
        		// Esto es necesario para mostrar las propiedades con pestañas.
        		// Lo que hacemos es reutilizar el contributor generado por gmf      
	    		/*
	    		 * Hemos creado el TabbedPropertySheetPage con el parámetro showTitleBar a false.
	    		 * Si quisiésemos mostrar esa barra de información habría que:
	    		 * - pasarle true al constructor
	    		 * - modificar la clase DatamodelerSheetLabelProvider porque ahora no tiene en cuenta
	    		 *   los elementos del navegador para mostrar el texto y las imágenes
	    		 * - para ello, probablemente habría que modificar la plantilla:
	    		 *   com.isb.datamodeler.ui.gmf\templates_original\xpt\propsheet\LabelProvider.xpt
	    		 */
    	        return new TabbedPropertySheetPage(new ITabbedPropertySheetPageContributor() {
    				
    				@Override
    				public String getContributorId()
    				{
    					return "com.isb.datamodeler.ui.diagram"; //$NON-NLS-1$
    				}
    				
    			}, false);
        	
        	
        	if (IEditingDomainProvider.class == adapterType)
        		return domainProvider;
        }
        
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
     */
    public Class[] getAdapterList() {
        return new Class[] {IPropertySheetPage.class, IEditingDomainProvider.class, IResource.class};
    }

}
