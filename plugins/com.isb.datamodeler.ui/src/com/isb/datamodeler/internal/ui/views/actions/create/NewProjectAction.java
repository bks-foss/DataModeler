package com.isb.datamodeler.internal.ui.views.actions.create;

import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.wizards.DataModelerProjectWizard;

public class NewProjectAction extends SelectionListenerAction
{

	private static final ImageDescriptor ACTION_IMAGE_DESC = DataModelerUI.imageDescriptorFromPlugin(
			DataModelerUI.PLUGIN_ID, "icons/full/obj16/datamodelerProject.png"); //$NON-NLS-1$
	
	public NewProjectAction() {
		super(Messages.bind("DataModeler.project.create"));
		
		setImageDescriptor(ACTION_IMAGE_DESC);
	}
	
	@Override
	public void run()
	{
		DataModelerProjectWizard wizard = new DataModelerProjectWizard();
		wizard.init(DataModelerUI.getDefault().getWorkbench(), getStructuredSelection());

		WizardDialog dialog= new WizardDialog(DataModelerUI.getActiveWorkbenchShell(), wizard);
		PixelConverter converter= new PixelConverter(DataModelerUI.getActiveWorkbenchShell());
		dialog.setMinimumPageSize(converter.convertWidthInCharsToPixels(70), 
			converter.convertHeightInCharsToPixels(20));
		dialog.create();
		dialog.open();
	}

	@Override
	public boolean updateSelection(IStructuredSelection selection)
	{
//		if(selection instanceof ITreeSelection)
//		{
//			// deshabilitamos la accion para elementos del modelo
//			if( ((ITreeSelection)selection).getFirstElement() instanceof DatamodelerDomainNavigatorItem)
//				return false;
//		}
		
		return true;
	}



}
