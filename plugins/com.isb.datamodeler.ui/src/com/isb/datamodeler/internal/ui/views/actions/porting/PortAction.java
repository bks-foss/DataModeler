package com.isb.datamodeler.internal.ui.views.actions.porting;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.internal.ui.wizards.port.DataModelerImportExportWizard;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.project.EProject;

public class PortAction extends SelectionListenerAction{

	private EObject _selectedEObject;
	
	private String _actionId;
	
	public final static String IMPORT = "import";
	public final static String EXPORT = "export";
	
	private static final ImageDescriptor IMPORT_IMAGE_DESC = DataModelerUI.imageDescriptorFromPlugin(
			DataModelerUI.PLUGIN_ID, "icons/full/etools16/import_wiz.gif"); //$NON-NLS-1$
	
	private static final ImageDescriptor EXPORT_IMAGE_DESC = DataModelerUI.imageDescriptorFromPlugin(
			DataModelerUI.PLUGIN_ID, "icons/full/etools16/export_wiz.gif"); //$NON-NLS-1$

	public PortAction(String actionId) {
		super("");
		
		if(IMPORT.equals(actionId))
		{
			setImageDescriptor(IMPORT_IMAGE_DESC);
			setText(Messages.bind("DataModelerImportAction.name"));
		}
			
		
		if(EXPORT.equals(actionId))
		{
			setImageDescriptor(EXPORT_IMAGE_DESC);
			setText(Messages.bind("DataModelerExportAction.name"));
		}
			
		_actionId = actionId;
	}
	
	@Override
	public void run() {
		
		DataModelerImportExportWizard wizard = new DataModelerImportExportWizard(_actionId);
		
        IDialogSettings workbenchSettings = DataModelerUI.getDefault()
                .getDialogSettings();
        IDialogSettings wizardSettings = workbenchSettings
                .getSection("DataModelerImportExportAction"); //$NON-NLS-1$
		
        if(wizardSettings==null)
        	wizardSettings = workbenchSettings.addNewSection("DataModelerImportExportAction");
        
		wizard.setDialogSettings(wizardSettings);
		wizard.setForcePreviousAndNextButtons(true);
		
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(_selectedEObject));
		
		WizardDialog dialog= new WizardDialog(DataModelerUI.getActiveWorkbenchShell(), wizard);
		PixelConverter converter= new PixelConverter(DataModelerUI.getActiveWorkbenchShell());
		dialog.setMinimumPageSize(converter.convertWidthInCharsToPixels(70), 
			converter.convertHeightInCharsToPixels(20));
		dialog.create();
		dialog.open();	
	}
	
	
	@Override
	public boolean updateSelection(IStructuredSelection selection){
		
		// la acción sólo se habilita para proyectos
		if(selection instanceof ITreeSelection)
		{
			ITreeSelection treeSelection = (ITreeSelection)selection;
			if(treeSelection.size()>1)
				return false;
			
			Object element = treeSelection.getFirstElement();
			
			if(element instanceof IAdaptable && ((IAdaptable)element).getAdapter(EProject.class)!=null)
			{
				_selectedEObject = (EProject)((IAdaptable)element).getAdapter(EProject.class);

				return true;
			}
			if(EXPORT.equals(_actionId))
			{
				if(element instanceof IAdaptable)
				{
					if(((IAdaptable)element).getAdapter(EPersistentTable.class)!=null)
					{
						_selectedEObject = (EPersistentTable)((IAdaptable)element).getAdapter(EPersistentTable.class);
						return true;
					}
					else if(((IAdaptable)element).getAdapter(ESchema.class)!=null)
					{
						_selectedEObject = (ESchema)((IAdaptable)element).getAdapter(ESchema.class);
						return true;
					}
				}
			}
		}
		
		return false;
	}

}
