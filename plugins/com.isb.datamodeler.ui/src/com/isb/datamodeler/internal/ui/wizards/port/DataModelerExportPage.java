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


public class DataModelerExportPage extends DataModelerImportExportPage {

	private static final String STORE_SELECTED_EXPORT_WIZARD_ID = DIALOG_SETTING_SECTION_NAME
			+ "STORE_SELECTED_EXPORT_WIZARD_ID"; //$NON-NLS-1$
		
		private static final String STORE_EXPANDED_EXPORT_CATEGORIES = DIALOG_SETTING_SECTION_NAME
			+ "STORE_EXPANDED_EXPORT_CATEGORIES";	//$NON-NLS-1$

		CategorizedWizardSelectionTree exportTree;
		
		/**
		 * Constructor for export wizard selection page.
		 * 
		 * @param aWorkbench
		 * @param currentSelection
		 */
		public DataModelerExportPage(IWorkbench aWorkbench,
				IStructuredSelection currentSelection) {
			super(aWorkbench, currentSelection);
		}
		
		protected void initialize() {
//			workbench.getHelpSystem().setHelp(getControl(),
//	                IWorkbenchHelpContextIds.EXPORT_WIZARD_SELECTION_WIZARD_PAGE);
		}

		protected Composite createTreeViewer(Composite parent) {
			IWizardCategory root = DataModelerUI.getDefault()
				.getExportWizardRegistry().getRootCategory();
			exportTree = new CategorizedWizardSelectionTree(
					root, Messages.bind("DataModelerImportAction.ExportWizard_selectDestination"));
			Composite exportComp = exportTree.createControl(parent);
			exportTree.getViewer().addSelectionChangedListener(new ISelectionChangedListener(){
				public void selectionChanged(SelectionChangedEvent event) {
					listSelectionChanged(event.getSelection());    	       			
				}
			});
			exportTree.getViewer().addDoubleClickListener(new IDoubleClickListener(){
		    	public void doubleClick(DoubleClickEvent event) {
		    		treeDoubleClicked(event);
		    	}
		    });
			setTreeViewer(exportTree.getViewer());
		    return exportComp;
		}
		
		public void saveWidgetValues(){
	    	storeExpandedCategories(STORE_EXPANDED_EXPORT_CATEGORIES, exportTree.getViewer());
	        storeSelectedCategoryAndWizard(STORE_SELECTED_EXPORT_WIZARD_ID, exportTree.getViewer()); 	
	        super.saveWidgetValues();
		}
		
		protected void restoreWidgetValues(){
	        IWizardCategory exportRoot = DataModelerUI.getDefault().getExportWizardRegistry().getRootCategory();
	        expandPreviouslyExpandedCategories(STORE_EXPANDED_EXPORT_CATEGORIES, exportRoot, exportTree.getViewer());
	        selectPreviouslySelected(STORE_SELECTED_EXPORT_WIZARD_ID, exportRoot, exportTree.getViewer());       
	        super.restoreWidgetValues();
		}
		
		protected ITriggerPoint getTriggerPoint(){
			return getWorkbench().getActivitySupport()
	    		.getTriggerPointManager().getTriggerPoint(DataModelerWizardTriggerPoints.EXPORT_WIZARDS);
		}
		
		protected void updateMessage(){
			setMessage(Messages.bind("DataModelerImportAction.ImportExportPage_chooseExportDestination")); 
			super.updateMessage();
		}

}
