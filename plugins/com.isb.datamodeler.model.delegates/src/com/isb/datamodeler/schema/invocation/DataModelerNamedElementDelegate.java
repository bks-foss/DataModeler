package com.isb.datamodeler.schema.invocation;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicInvocationDelegate;

import com.isb.datamodeler.model.core.DataModelerNature;
import com.isb.datamodeler.schema.EDataModelerNamedElement;
import com.isb.datamodeler.schema.ESchemaPackage;

public class DataModelerNamedElementDelegate extends BasicInvocationDelegate {

	public DataModelerNamedElementDelegate(EOperation operation) {
		super(operation);
	}

	@Override
	public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
			throws InvocationTargetException {

	    if (eOperation.getEContainingClass() == ESchemaPackage.Literals.DATA_MODELER_NAMED_ELEMENT)
	    {
	      switch (eOperation.getEContainingClass().getEAllOperations().indexOf(eOperation))
	      {
	        case ESchemaPackage.DATA_MODELER_NAMED_ELEMENT___GET_IPROJECT:
	            return getIProject((EDataModelerNamedElement)target);	
	      }
	    }
		
		return super.dynamicInvoke(target, arguments);
	}


	private IProject getIProject(EDataModelerNamedElement target) {
		
		String iProjectName = getIProjectName(target);
		
		IProject[] wksProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject iProject : wksProjects)
		{
			// Bugzilla 20887
			if(iProject.isOpen())
			{
				try {
					if(iProject.getNature(DataModelerNature.DATA_MODELER_NATURE_ID)!=null
							&& iProject.getName().equals(iProjectName))
						
						return iProject;
					
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return null;
	}

	private String getIProjectName(EDataModelerNamedElement target) {
		
		if(target.eResource()!=null)
			if(target.eResource().getURI().segmentCount()>1)
				return target.eResource().getURI().segment(1);
		
		return "";
	}

}
