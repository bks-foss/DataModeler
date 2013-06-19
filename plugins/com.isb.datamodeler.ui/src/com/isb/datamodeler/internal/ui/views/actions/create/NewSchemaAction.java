package com.isb.datamodeler.internal.ui.views.actions.create;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.internal.ui.wizards.DataModelerSchemaWizard;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.project.EProject;

public class NewSchemaAction extends SelectionListenerAction{

	private IProject _dmProject = null;
	
	private static final ImageDescriptor ACTION_IMAGE_DESC = DataModelerUI.imageDescriptorFromPlugin(
			DataModelerUI.PLUGIN_ID, "icons/full/obj16/datamodelerSchema.png"); //$NON-NLS-1$
	
	public NewSchemaAction() {
		super(Messages.bind("DataModeler.schema.create"));
		
		setImageDescriptor(ACTION_IMAGE_DESC);
	}
	
	@Override
	public void run() {

		DataModelerSchemaWizard wizard = new DataModelerSchemaWizard();
		
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(_dmProject));
		
		WizardDialog dialog= new WizardDialog(DataModelerUI.getActiveWorkbenchShell(), wizard);
		PixelConverter converter= new PixelConverter(DataModelerUI.getActiveWorkbenchShell());
		dialog.setMinimumPageSize(converter.convertWidthInCharsToPixels(70), 
			converter.convertHeightInCharsToPixels(20));
		dialog.create();
		dialog.open();	
	}

	@Override
	public boolean updateSelection(IStructuredSelection selection) {

			if(((IStructuredSelection)selection).size()==1)
			{
				Object element = ((IStructuredSelection)selection).getFirstElement();
				if(element instanceof IAdaptable && ((IAdaptable)element).getAdapter(EProject.class)!=null)
				{
					EProject selectedEProject = (EProject)((IAdaptable)element).getAdapter(EProject.class);
					_dmProject = selectedEProject.getIProject();
					return true;
				}
			}
			
			return false;
	}


}
