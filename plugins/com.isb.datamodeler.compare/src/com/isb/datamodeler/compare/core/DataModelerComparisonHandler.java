package com.isb.datamodeler.compare.core;

import java.io.IOException;

import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.compare.ui.team.AbstractTeamHandler;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.team.core.history.IFileRevision;
import org.eclipse.team.internal.ui.history.FileRevisionTypedElement;

//import com.isb.datamodeler.model.core.DataModelerDisableEdition;

/**
 * Handler del team para cargar los recursos remotos y cargar el modelo dataModeler.
 * Para poder lanzar la comparación tenemos que crear un modelo temporal.
 * 
 * @author Alfonso
 *
 */
public class DataModelerComparisonHandler extends AbstractTeamHandler {

	@SuppressWarnings("restriction")
	@Override
	public boolean loadResources(ICompareInput input) throws IOException,
			CoreException {
		
		final ITypedElement left = input.getLeft();
		final ITypedElement right = input.getRight();
		
		if (right instanceof FileRevisionTypedElement) {
			
			IFileRevision revisionFileModel = ((FileRevisionTypedElement) right).getFileRevision();			
			leftResource = prepareLeftLocalCompare(left);
			rightResource = prepareRightRemoteCompare(revisionFileModel, right);
			
			return true;
		}	
			
		return false;
	}
	
	/**
	 * 
	 * @param left
	 * @return
	 */
	public Resource prepareLeftLocalCompare (ITypedElement left) {
		
		Resource resource = null;			
		ResourceSet res1 = new ResourceSetImpl();
		
		try {
			
			resource = EclipseModelUtils.load(((ResourceNode)left).getResource().getFullPath(),
					res1).eResource();	
			
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		
		return resource;
	}	
	
	/**
	 * 
	 * @param rightRevision
	 * @param right
	 * @return
	 */
	public Resource prepareRightRemoteCompare (IFileRevision rightRevision, ITypedElement right) {
		
		Resource resource = null;			
		ResourceSet temporalResourceSet = new ResourceSetImpl();				
		temporalResourceSet.setURIConverter(new DataModelerRevisionedURIConverter(rightRevision));
		try {
			
			resource =  ModelUtils.load(((IStreamContentAccessor)right).getContents(),
					right.getName(), temporalResourceSet).eResource();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resource;
	}

}
