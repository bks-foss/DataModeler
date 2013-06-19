package com.isb.datamodeler.diagram.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerDiagramUtils
{
	
	public static EList<Diagram> getDiagrams(ESchema schema) {
		
		EList<Diagram> list = new BasicEList<Diagram>();
		
		for(Setting setting: EcoreUtil.UsageCrossReferencer.find(schema, schema.eResource()))
		{
			if(setting.getEObject() instanceof Diagram)
				list.add((Diagram)setting.getEObject());
				
		}

		return list;
	}
	public static EProject findEProject(IFile file)
	{
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		
		URI fileURI = URI.createPlatformResourceURI(file.getFullPath()
				.toString(), true);
		final Resource resource = editingDomain.getResourceSet().getResource(
				fileURI, true);
		
		EProject project = null;
		
		// TODO esto es una prueba de lectura de modelos usando TransactionalEditingDomains 
		try {
			project = (EProject) editingDomain.runExclusive(new RunnableWithResult.Impl() {
			    public void run() {
			    	
					for(EObject eObject:resource.getContents())
					{
						if(eObject instanceof EProject)
							setResult((EProject)eObject);
					}	
			    }});
		} catch (InterruptedException e) {
//			log(e, "UtilsDataModelerUI.findProject()");
		}
		
		return project;
	}
	public static void showMessage(final String message)
	{
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();

			IWorkbenchPage page = win.getActivePage();

			IWorkbenchPart part = page.getActivePart();
			IWorkbenchPartSite site = part.getSite();
			IStatusLineManager statusLineManager = null;
			
			if(site instanceof IViewSite)
			{
				IViewSite vSite = ( IViewSite ) site;

				IActionBars actionBars =  vSite.getActionBars();

				if( actionBars == null )
				return ;

				statusLineManager = actionBars.getStatusLineManager();

			}
			else if(site instanceof IEditorSite)
			{
				IEditorSite vSite = ( IEditorSite ) site;

				IActionBars actionBars =  vSite.getActionBars();

				if( actionBars == null )
				return ;

				statusLineManager = actionBars.getStatusLineManager();
				
			}
			if( statusLineManager == null )
				return ;

			statusLineManager.setErrorMessage(message);
		
	}
	
}
