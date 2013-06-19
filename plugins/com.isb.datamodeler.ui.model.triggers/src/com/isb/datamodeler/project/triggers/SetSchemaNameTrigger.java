package com.isb.datamodeler.project.triggers;

import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditor;
import com.isb.datamodeler.diagram.util.DataModelerDiagramUtils;
import com.isb.datamodeler.model.triggers.AbstractInitializerTrigger;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.project.EProject;

/**
 * Comando encargado de la refactorización de los esquemas de un proyecto
 *
 */
public class SetSchemaNameTrigger extends AbstractInitializerTrigger {
	
	private ESchema _schema;
	private EProject _project;
	
	public SetSchemaNameTrigger(TransactionalEditingDomain domain, ESchema owner) {
		super(domain);
		
		_schema = owner;
		_project = (EProject)_schema.eContainer();
	}

	@Override
	public void executeTrigger() {
		
		for(Diagram diagram:DataModelerDiagramUtils.getDiagrams(_schema))
		{
			diagram.setName(getNewDefaultDiagramName());
			
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			
			URI diagramURI = EcoreUtil.getURI(diagram);
			for (IEditorReference editorRef : page.getEditorReferences())
			{
				try {
					if(editorRef.getEditorInput() instanceof URIEditorInput)
					{
						URI editorURI = ((URIEditorInput)editorRef.getEditorInput()).getURI();
						if(diagramURI.equals(editorURI))
						{
							DatamodelerDiagramEditor diagramEditor = (DatamodelerDiagramEditor)editorRef.getEditor(true);
							diagramEditor.setDiagramName(diagram.getName());
							//break;
						}
					}
				} catch (PartInitException e) {
					UtilsDataModelerUI.log(e , "SetSchemaNameTrigger");
					e.printStackTrace();
				}
			}
		}
	}
	
	private String getNewDefaultDiagramName()
	{
		int i=0;
		String baseName = "";
		if(_schema!=null)
			baseName = "DD_"+_project.getApplication()+"_"+_schema.getName(); //$NON-NLS-1$ //$NON-NLS-2$
		
		String newName = baseName;
		
		if(_schema!=null)
			while(hasChildWithSameName(DataModelerDiagramUtils.getDiagrams(_schema), newName))
				newName = baseName + ++i;
		
		return newName;
	}
	
	private boolean hasChildWithSameName(EList<Diagram> existingChildren, String name)
	{
		for(Diagram diagram:existingChildren)
			if(diagram.getName()!=null && diagram.getName().equals(name))
				return true;
		
		return false;
	}
}
