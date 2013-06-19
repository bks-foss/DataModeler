package com.isb.datamodeler.model.validation;

import org.eclipse.emf.ecore.EObject;

import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.ui.project.EProject;

public class UtilsValidations {
	public static String getDataBaseId(EObject eObject)
	{
		if(eObject instanceof ESQLObject)
		{
			EObject parent = (ESQLObject)eObject;
		
			while(parent!=null && !(parent instanceof EProject))
			{
				parent = parent.eContainer();
			}
			if(parent!=null)
			{
				EProject eProject = (EProject)parent;
				return eProject.getDatabase().getId();
			}
		}
		else if(eObject instanceof EProject)
		{
			return ((EProject)eObject).getDatabase().getId();
		}
		return "";
	}
	public static String getDataBaseName(EObject eObject)
	{
		if(eObject instanceof ESQLObject)
		{
			EObject parent = (ESQLObject)eObject;
		
			while(parent!=null && !(parent instanceof EProject))
			{
				parent = parent.eContainer();
			}
			if(parent!=null)
			{
				EProject eProject = (EProject)parent;
				return eProject.getDatabase().getName();
			}
		}
		else if(eObject instanceof EProject)
		{
			return ((EProject)eObject).getDatabase().getName();
		}
		return "";
	}
}
