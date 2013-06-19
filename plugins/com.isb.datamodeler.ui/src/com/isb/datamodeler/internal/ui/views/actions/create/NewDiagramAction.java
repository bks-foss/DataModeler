package com.isb.datamodeler.internal.ui.views.actions.create;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.internal.ui.wizards.DataModelerDiagramWizard;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.project.EProject;

public class NewDiagramAction extends SelectionListenerAction{

	private ESchema _schema;
	
	private static final ImageDescriptor ACTION_IMAGE_DESC = DataModelerUI.imageDescriptorFromPlugin(
			DataModelerUI.PLUGIN_ID, "icons/full/obj16/Diagram.gif"); //$NON-NLS-1$
	
	public NewDiagramAction() {
		super(Messages.bind("DataModeler.diagram.create"));
		
		setImageDescriptor(ACTION_IMAGE_DESC);
	}
	
	@Override
	public void run() {
		
		DataModelerDiagramWizard wizard = new DataModelerDiagramWizard();
		
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(_schema));
		
		WizardDialog dialog= new WizardDialog(DataModelerUI.getActiveWorkbenchShell(), wizard);
		PixelConverter converter= new PixelConverter(DataModelerUI.getActiveWorkbenchShell());
		dialog.setMinimumPageSize(converter.convertWidthInCharsToPixels(70), 
			converter.convertHeightInCharsToPixels(20));
		dialog.create();
		dialog.open();	
	}
	
	
	@Override
	public boolean updateSelection(IStructuredSelection selection){
		
		// la acción sólo se habilita para esquemas
		if(selection instanceof ITreeSelection)
		{
			ITreeSelection treeSelection = (ITreeSelection)selection;
			if(treeSelection.size()>1)
				return false;
			
			Object element = treeSelection.getFirstElement();
			
			if(element instanceof IAdaptable && ((IAdaptable)element).getAdapter(ESchema.class)!=null)
			{
				ESchema schema = (ESchema)((IAdaptable)element).getAdapter(ESchema.class);

				// Comprobamos que no sea externo
				EProject eProject = (EProject)schema.eContainer();
				if(eProject!=null)
					if(!(eProject.getCapability().equals(schema.getCapability())))
						return false;
				
				_schema = schema;
				return true;
			}
		}
		
		return false;
	}

}
