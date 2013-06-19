package com.isb.datamodeler.model.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * Naturaleza para los proyectos de Data Modeler
 *
 */
public class DataModelerNature implements IProjectNature
{

/**
 * ID para la naturaleza Data Modeler
 */
public static final String DATA_MODELER_NATURE_ID = Activator.PLUGIN_ID+".DataModelerNature"; //$NON-NLS-1$
	
/** 
 * Proyecto al que esta instancia de naturaleza está asociado.
 */
protected IProject _project;

@Override
public void configure() throws CoreException
{
}

@Override
public void deconfigure() throws CoreException
{
}

@Override
public IProject getProject()
{
	return _project;
}

@Override
public void setProject(IProject project)
{
	_project = project;
}

}
