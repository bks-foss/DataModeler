package com.isb.datamodeler.internal.ui.wizards.port;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.ui.DataModelerUI;

public class DataModelerImportExportWizard extends Wizard {
	
	private static final ImageDescriptor IMG_WIZBAN_IMPORT_WIZ = DataModelerUI.imageDescriptorFromPlugin(
			DataModelerUI.PLUGIN_ID, "icons/full/wizban/import_wiz.png"); //$NON-NLS-1$
	
	private static final ImageDescriptor IMG_WIZBAN_EXPORT_WIZ = DataModelerUI.imageDescriptorFromPlugin(
			DataModelerUI.PLUGIN_ID, "icons/full/wizban/export_wiz.png"); //$NON-NLS-1$
	
	/**
	 * Constant used to to specify to the import/export wizard
	 * which page should initially be shown. 
	 */
	public static final String IMPORT = "import";	//$NON-NLS-1$
	/**
	 * Constant used to to specify to the import/export wizard
	 * which page should initially be shown. 
	 */
	public static final String EXPORT = "export";	//$NON-NLS-1$
		
    private IWorkbench workbench;
    private IStructuredSelection selection;
    private DataModelerImportExportPage importExportPage;
    private String page = null;
    
    /**
     * Create an import/export wizard and show the page 
     * with the given id.
     * 
     * @param pageId
     */
    public DataModelerImportExportWizard(String pageId){
    	page = pageId;
    }
    
    /**
     * Subclasses must implement this <code>IWizard</code> method 
     * to perform any special finish processing for their wizard.
     */
    public boolean performFinish() {
    	importExportPage.saveWidgetValues();
        return true;
    }

    /**
     * Creates the wizard's pages lazily.
     */
    public void addPages() {
    	if (page.equals(IMPORT)) {
			importExportPage = new DataModelerImportPage(this.workbench, this.selection);
		} else if (page.equals(EXPORT)) {
			importExportPage = new DataModelerExportPage(this.workbench, this.selection);
		}
        if (importExportPage != null) {
			addPage(importExportPage);
		}
    }

    /**
     * Initializes the wizard.
     * 
     * @param aWorkbench the workbench
     * @param currentSelection the current selectio
     */
    public void init(IWorkbench aWorkbench,
            IStructuredSelection currentSelection) {
        this.workbench = aWorkbench;
        this.selection = currentSelection;

        ImageDescriptor wizardBannerImage = null;
        if (IMPORT.equals(page)){
        	wizardBannerImage = IMG_WIZBAN_IMPORT_WIZ;
        	setWindowTitle(Messages.bind("DataModelerImportAction.ImportWizard_title"));
        }
        else if (EXPORT.equals(page)){
        	wizardBannerImage = IMG_WIZBAN_EXPORT_WIZ;
        	setWindowTitle(Messages.bind("DataModelerImportAction.ExportWizard_title"));
        }
        if (wizardBannerImage != null) {
			setDefaultPageImageDescriptor(wizardBannerImage);
		}
        setNeedsProgressMonitor(true);
    }
}
