package com.isb.datamodeler.internal.ui.views.actions.create;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.CreateChildAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.project.EProject;

public class CreateDataModelerChildAction extends CreateChildAction
{

public CreateDataModelerChildAction(EditingDomain editingDomain,
		ISelection selection, Object descriptor)
{
	super(editingDomain, selection, descriptor);
}

@Override
public void run()
{
	super.run();
}
@Override
public void configureAction(ISelection selection) {
	if (!(selection instanceof IStructuredSelection))
    {
      disable();
      return;
    }
    else
    {
      IStructuredSelection sSelection = (IStructuredSelection) selection;
      if(sSelection.size()>0)
    	  disable();
      
      EObject eObject = (EObject)((IAdaptable)sSelection.getFirstElement()).getAdapter(EObject.class);
      EProject eModelerProject = null;
      
      if(eObject instanceof ESQLObject)
    	  eModelerProject = (EProject)((ESQLObject)eObject).getRootContainer();
      
      // Comprobamos que el elemento no sea externo
      if(eObject instanceof ESchema)
      {
    	  ESchema schema = (ESchema)eObject;
		  if(eModelerProject!=null && !(eModelerProject.getCapability().equals(schema.getCapability())))
		  {
			  disable();
			  return;
		  }
      }
      else if(eObject instanceof ETable)
      {
    	  ETable table = (ETable)eObject;
		  if(eModelerProject !=null && !(eModelerProject.getCapability().equals(table.getSchema().getCapability())))
		  {
			disable();
			return;
		  }
      }
      
      super.configureAction(new StructuredSelection(eObject));
      
    }
	
}

}
