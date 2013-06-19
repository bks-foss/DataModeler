package com.isb.datamodeler.internal.ui.wizards.port;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.activities.ITriggerPoint;
import org.eclipse.ui.wizards.IWizardCategory;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.ui.DataModelerUI;

public class DataModelerImportPage extends DataModelerImportExportPage {

    private static final String STORE_SELECTED_IMPORT_WIZARD_ID = DIALOG_SETTING_SECTION_NAME
    		+ "STORE_SELECTED_IMPORT_WIZARD_ID"; //$NON-NLS-1$

    private static final String STORE_EXPANDED_IMPORT_CATEGORIES = DIALOG_SETTING_SECTION_NAME
			+ "STORE_EXPANDED_IMPORT_CATEGORIES";	//$NON-NLS-1$    

    protected CategorizedWizardSelectionTree importTree;
	
    /**
     * Constructor for import wizard selection page.
     * 
     * @param aWorkbench
     * @param currentSelection
     */
	public DataModelerImportPage(IWorkbench aWorkbench,
			IStructuredSelection currentSelection) {
		super(aWorkbench, currentSelection);
	}

	protected void initialize() {
//        workbench.getHelpSystem().setHelp(
//				getControl(),
//				IWorkbenchHelpContextIds.IMPORT_WIZARD_SELECTION_WIZARD_PAGE); 		
	}

	protected Composite createTreeViewer(Composite parent) {
		IWizardCategory root = DataModelerUI.getDefault()
			.getImportWizardRegistry().getRootCategory();
		importTree = new CategorizedWizardSelectionTree(
				root, Messages.bind("DataModelerImportAction.ImportWizard_selectSource"));
		Composite importComp = importTree.createControl(parent);
		importTree.getViewer().addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				listSelectionChanged(event.getSelection());    	       			
			}
		});
		importTree.getViewer().addDoubleClickListener(new IDoubleClickListener(){
	    	public void doubleClick(DoubleClickEvent event) {
	    		treeDoubleClicked(event);
	    	}
	    });
		setTreeViewer(importTree.getViewer());
		return importComp;
	}
	
	public void saveWidgetValues(){
    	storeExpandedCategories(STORE_EXPANDED_IMPORT_CATEGORIES, importTree.getViewer());
        storeSelectedCategoryAndWizard(STORE_SELECTED_IMPORT_WIZARD_ID, importTree.getViewer());   
        super.saveWidgetValues();
	}
	
	protected void restoreWidgetValues(){
    	IWizardCategory importRoot = DataModelerUI.getDefault().getImportWizardRegistry().getRootCategory();
        expandPreviouslyExpandedCategories(STORE_EXPANDED_IMPORT_CATEGORIES, importRoot,importTree.getViewer());
        selectPreviouslySelected(STORE_SELECTED_IMPORT_WIZARD_ID, importRoot, importTree.getViewer());
        super.restoreWidgetValues();
	}

	protected ITriggerPoint getTriggerPoint(){
		return getWorkbench().getActivitySupport()
    		.getTriggerPointManager().getTriggerPoint(DataModelerWizardTriggerPoints.IMPORT_WIZARDS);		
	}
	
	protected void updateMessage(){
		setMessage(Messages.bind("DataModelerImportAction.ImportExportPage_chooseImportSource"));
		super.updateMessage();
	}

}
