package com.isb.datamodeler.compare.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.compare.messages.Messages;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.datatypes.EPredefinedDataType;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ETypedElement;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.ui.diagram.EDMDiagram;
import com.isb.datamodeler.ui.project.EProject;

public class ExtendModelUtils {

	/** */
	public static  EList<EObject> load(URI modelURI, ResourceSet resourceSet) throws IOException {
		 EList<EObject> result = null;

		final Resource modelResource = createResource(modelURI, resourceSet);
		modelResource.load(Collections.emptyMap());
		if (modelResource.getContents().size() > 0)
			result = modelResource.getContents();
		return result;
	}
	
	public static EList<EObject> load(File file, ResourceSet resourceSet) throws IOException {
		return load(URI.createFileURI(file.getPath()), resourceSet);
	}
	
	/** */
	public static Resource createResource(URI modelURI, ResourceSet resourceSet) {
		String fileExtension = modelURI.fileExtension();
		if (fileExtension == null || fileExtension.length() == 0) {
			fileExtension = Resource.Factory.Registry.DEFAULT_EXTENSION;
		}

		// First search the resource set for our resource factory
		Resource.Factory.Registry registry = resourceSet.getResourceFactoryRegistry();
		Object resourceFactory = registry.getExtensionToFactoryMap().get(fileExtension);
		if (resourceFactory == null) {
			// then the global registry
			registry = Resource.Factory.Registry.INSTANCE;
			resourceFactory = registry.getExtensionToFactoryMap().get(fileExtension);
			if (resourceFactory != null) {
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(fileExtension,
						resourceFactory);
			} else {
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(fileExtension,
						new XMIResourceFactoryImpl());
			}
		}

		return resourceSet.createResource(modelURI);
	}
	
	/**
	 * Lanza el borrado del diagrama cuando se borra alg�n elemento v�a merge.
	 * @param element
	 */
	public static void fireViewDeleteCommands(EObject element) {
		
		CompoundCommand command = new CompoundCommand();
		TransactionalEditingDomain domain = DataModelerUtils.getDataModelerEditingDomain();
		Resource leftResource = element.eResource();
		
		List<View> viewsToRemove = new ArrayList<View> ();
		
		if (leftResource != null)
		{						
			TreeIterator<EObject> iterator = leftResource.getAllContents(); 
			while(iterator.hasNext())
			{
				EObject child = iterator.next();
				
				if(!(child instanceof View))
					continue;
			
				View view = (View)child;
				
				if(view.isSetElement() && view.getElement()==element)
					viewsToRemove.add(view);
			}
		}
		
		if(!viewsToRemove.isEmpty())
			command.appendIfCanExecute(new DeleteCommand((TransactionalEditingDomain) domain, viewsToRemove));
			
		// Ejecutamos la lista de elementos a borrar
		domain.getCommandStack().execute(command);
	}

	/**
	 * Comprueba que no existan otros editores abiertos en el momento de realizar la comparación.
	 * Deja decidir al usuario para que cierre automáticamente los editore para que se lleve a cabo
	 * la comparación.
	 * 
	 * @param leftProject
	 * @param rightProject
	 * @return
	 */
	public static boolean prepareView(IProject leftProject, IProject rightProject) {

		// Obtener la información de los modelos que vamos a comparar.
		List<IResource> resources = new ArrayList<IResource>();
		IPath path = leftProject.getFullPath().append("/" + leftProject.getName() + DataModelerCompare.DATA_MODELER_EXTENSION_FILE); //$NON-NLS-1$		
		IResource resL = leftProject.getFile(path);
		resources.add(resL);
		
		if (rightProject != null) {
			IPath pathR = rightProject.getFullPath().append("/" + rightProject.getName() + DataModelerCompare.DATA_MODELER_EXTENSION_FILE); //$NON-NLS-1$
			IResource resR = rightProject.getFile(pathR);
			resources.add(resR);
		}

		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = activeWorkbenchWindow.getActivePage();		
		IEditorReference[] editors = page.getEditorReferences();		
				
		// Guardamos los editores de comparación que pudieran estar abiertos.
		List<IEditorReference> editorsToClose = new ArrayList<IEditorReference>();
				
		// Bugzilla... 20285
		for (IResource resource : resources) 
		{										
			for(int i = 0; i < editors.length; i++)
			{				
				try 
				{						
					if (editors[i].getEditorInput() instanceof URIEditorInput && 
							((URIEditorInput)editors[i].getEditorInput()).getURI().toString().contains(resource.getName()) &&
								(resource.getFullPath().lastSegment().equals(leftProject.getName()+ "." + resource.getFileExtension()) ||  //$NON-NLS-1$
										(rightProject != null && 
												resource.getFullPath().lastSegment().equals(
														rightProject.getName()+ "." + resource.getFileExtension())))) //$NON-NLS-1$
					{ 
					
						editorsToClose.add(editors[i]);
					}
				}				
				catch (PartInitException e) 
				{			
					e.printStackTrace();
				}			
			}
		}
		for(IEditorReference editorRef:editors)
		{
			if(editorRef.getId().equals("org.eclipse.compare.CompareEditor")) //$NON-NLS-1$
			{
				// Bugzilla 21351
				if (editorRef.isDirty()) {
					MessageDialog.open(MessageDialog.INFORMATION, null, Messages.bind("datamodeler.compare.prepareView.otherCompareEditor.dirty.title"), 
							Messages.bind("datamodeler.compare.prepareView.otherCompareEditor.dirty.text"), SWT.NONE);
					return true;
				}
				
				editorsToClose.add(editorRef);
			}
		}
		boolean confirm = false;
		
		if(editorsToClose.size()>0)
		{
			confirm = MessageDialog.openConfirm(null, Messages.bind("datamodeler.compare.closed.compare.editor.title"), //$NON-NLS-1$
			Messages.bind("datamodeler.compare.closed.other.editor.text")); //$NON-NLS-1$
		}

		if(editorsToClose.size()>0)
		{
			if(confirm)
			{			
				// Cerramos el/los editor/es de comparación o de diagrama.
				for(IEditorReference editor:editorsToClose)									
					editor.getPage().closeEditor(editor.getEditor(false), false);				
			}

			else return true;

		}

		return false;
	}
		
	
	/**
	 * Método por defecto para obtener el display
	 * @return
	 */
	public static Display getDisplay() {
	      Display display = Display.getCurrent();
	      //may be null if outside the UI thread
	      if (display == null)
	         display = Display.getDefault();
	      return display;		
	   }
	
	public static void preparePrimitiveDataType(EProject project) {
		
		EDatabase dataBase = project.getDatabase();
		if (dataBase == null)
			return;
		
		// Integridad de los diagramas que han quedado huerfanos. Esto ocurre porque el merge no tiene en cuenta
		// los diagramas, en aquellos casos que yo hago un merge total (todo el modelo) y tengo  un diagrama externo que tiene el mismo nombre
		// que otro esquema del proyecto, deja los diagramas huerfanos.
		List<EDMDiagram> diagramToDelete = new ArrayList<EDMDiagram> ();
		for (EObject object:project.eResource().getContents()){
			if (object instanceof EDMDiagram) {
				EObject schema = ((EDMDiagram)object).getElement();
				if (schema != null && schema instanceof ESchema) {
					if (!project.getSchemas().contains(schema))				
						diagramToDelete.add ((EDMDiagram) object);
				}
			}				
		}
		for (EDMDiagram edmDiagram : diagramToDelete) {
			project.eResource().getContents().remove(edmDiagram);
		}
		
		
		// Integridad de los esquemas en el database	
		List<ESchema> schemasToDelete = new ArrayList<ESchema> ();
		for (ESchema eSchema:dataBase.getSchemas()) {
			if (!project.getSchemas().contains(eSchema))				
				schemasToDelete.add (eSchema);
		}
		
		for (ESchema eSchema : schemasToDelete) {
			dataBase.getSchemas().remove(eSchema);
		}
			
		
		List <EForeignKey> foreignKeyToRemove = new ArrayList<EForeignKey> ();
		
		for (Iterator<EObject> ref = project.eAllContents(); ref.hasNext(); ) {
			EObject originalElement = ref.next();
			
			if (originalElement instanceof EColumn ) {
				if (((EColumn)originalElement).getPrimitiveType()== null ) {
					EList <EPrimitiveDataType>  primitive = dataBase.getPrimitiveDataTypes();
					for (EPrimitiveDataType ePrimitiveDataType : primitive) {
						if (ePrimitiveDataType != null && 
								((ETypedElement)originalElement).getContainedType() != null && 
								ePrimitiveDataType.getName().equals(((ETypedElement)originalElement).getContainedType().getName())) {

							((EColumn)originalElement).setPrimitiveType(ePrimitiveDataType);
							((EPredefinedDataType)((ETypedElement)originalElement).
									getContainedType()).setPrimitiveType(ePrimitiveDataType);
						}
					}
				}			
			}
			
			

			if (originalElement instanceof ESchema) {
				if (((ESchema)originalElement).getDatabase()==null)
					((ESchema)originalElement).setDatabase(dataBase);
			}
			
			// Bugzilla 2206
			if (originalElement instanceof EForeignKey) {
				if (((EForeignKey)originalElement).getParentTable()== null) {
					foreignKeyToRemove.add((EForeignKey) originalElement);					
				}
					
			}
		}
		
		// Borramso aquellos elementos que quedan inconsistentes
		for (EForeignKey eForeignKey : foreignKeyToRemove) {
			EObject parent = eForeignKey.eContainer();
			List<?>list = (List<?>) parent.eGet(eForeignKey.eContainingFeature());
			list.remove(eForeignKey);
		}
		
	}
	
	public static void prepareFK(EProject project) {
				
		List <EForeignKey> foreignKeyToRemove = new ArrayList<EForeignKey> ();
		List<EColumn> deleteIncoherencePKRefMembers = new ArrayList<EColumn>();
		List<EColumn> deleteIncoherenceUKRefMembers = new ArrayList<EColumn>();
		
		for (Iterator<EObject> ref = project.eAllContents(); ref.hasNext(); ) {
			EObject originalElement = ref.next();
	
			// Bugzilla 22026.
			if (originalElement instanceof EForeignKey) {
				
				if (((EForeignKey)originalElement).getParentTable()== null) {
					foreignKeyToRemove.add((EForeignKey) originalElement);					
				} else {
					
					// Bugzilla 22192. Comprobamos la integridad de los miembros referenciados de las uk's y las pk's
					EPersistentTable parentTable = (EPersistentTable) ((EForeignKey)originalElement).getParentTable();					
					for (EUniqueConstraint uniqueConstraint : parentTable.getUniqueConstraints()) {
						for (EColumn member: ((EForeignKey)originalElement).getReferencedMembers()) {
							if (!uniqueConstraint.getMembers().contains(member))
								deleteIncoherenceUKRefMembers.add(member);							
						}		
					}
					
					if (parentTable.getPrimaryKey() != null) {
						EList<EColumn> membersParentTable = parentTable.getPrimaryKey().getMembers();
						for (EColumn member: ((EForeignKey)originalElement).getReferencedMembers()) {
							if (!membersParentTable.contains(member))
								deleteIncoherencePKRefMembers.add(member);							
						}										
					}
				}
								
				// Borrado de miembros de las FKS incoherentes porque ya no existen en la tabla
				List <EColumn> FKMembers2Delete = new ArrayList<EColumn> ();
				for (EColumn eColumn : ((EForeignKey)originalElement).getMembers()) {
					if (!((EForeignKey)originalElement).getBaseTable().getColumns().contains(eColumn))
						FKMembers2Delete.add(eColumn);
				}
				
				for (EColumn eColumn : FKMembers2Delete) {
					((EForeignKey)originalElement).getMembers().remove(eColumn);
				}
				
				//Borramos los elementos incoherentes de las PK's
				for (EColumn eColumn : deleteIncoherencePKRefMembers) {
					((EForeignKey)originalElement).getReferencedMembers().remove(eColumn);				
				}
				
				//Borramos los elementos incoherentes de las UK's
				for (EColumn eColumn : deleteIncoherenceUKRefMembers) {
					((EForeignKey)originalElement).getReferencedMembers().remove(eColumn);					
				}
			}
			
			
			//Lista de miembros de la UK y la PK
			List <EColumn> uniqueMembers2Delete = new ArrayList<EColumn> ();
			if (originalElement instanceof EUniqueConstraint) {
				for (EColumn eColumn : ((EUniqueConstraint)originalElement).getMembers()) {
					if (!((EUniqueConstraint)originalElement).getBaseTable().getColumns().contains(eColumn))
						uniqueMembers2Delete.add(eColumn);
				}
				
				for (EColumn eColumn : uniqueMembers2Delete) {
					((EUniqueConstraint)originalElement).getMembers().remove(eColumn);
				}
			}
			
			
			List <EColumn> PKMembers2Delete = new ArrayList<EColumn> ();
			if (originalElement instanceof EPrimaryKey) {
				for (EColumn eColumn : ((EPrimaryKey)originalElement).getMembers()) {
					if (!((EPrimaryKey)originalElement).getBaseTable().getColumns().contains(eColumn))
						PKMembers2Delete.add(eColumn);
				}
				
				for (EColumn eColumn : PKMembers2Delete) {
					((EPrimaryKey)originalElement).getMembers().remove(eColumn);
				}
			}						
		}
						
		// Borramos las FK's que quedan inconsistentes que no ha podido solucionar el Merge
		for (EForeignKey eForeignKey : foreignKeyToRemove) {									
			EObject parent = eForeignKey.eContainer();
			List<?>list = (List<?>) parent.eGet(eForeignKey.eContainingFeature());
			list.remove(eForeignKey);			
		}
		
		
	}
	
}
