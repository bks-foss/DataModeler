package com.isb.datamodeler.compare.merge;

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;

import com.isb.datamodeler.model.core.DataModelerUtils;

/**
 * Comando para forzar un refresco en la vista de navegador de
 * DataModeler, cuando termina el merge y se salva el recurso.
 * 
 * @author Alfonso
 *
 */
public class FinalizeMergeCommand extends AbstractCommand{

	TransactionalEditingDomain _editingDomain;
	Resource _res;
	
	public FinalizeMergeCommand(Resource res) {
		_editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		_res = res;
	}
	
	@Override
	public void execute() {
		  InternalTransactionalEditingDomain internalDomain =
	            (InternalTransactionalEditingDomain) _editingDomain;
		  
	        Transaction nested = null;
	        
	        try {
	        	
                nested = internalDomain.startTransaction(false, null);
            
	        } catch (InterruptedException e) {
	        	
            	 internalDomain.getActiveTransaction().abort(new Status(
                         IStatus.ERROR, "EMFCompare", "FinalizeMergeCommand", e)); //$NON-NLS-1$ //$NON-NLS-2$
            }
	        
	        EList<Resource> resources  = _editingDomain.getResourceSet().getResources();
	        for (Resource resource : resources) {
				String file = resource.getURI().lastSegment();
				if (file.equals(_res.getURI().lastSegment()))
					try {
						//_res.save(null);						
						resource.unload();
						resource.load(null);
						
						
					} catch (IOException e) {
						
						internalDomain.getActiveTransaction().abort(new Status(
		                         IStatus.ERROR,"EMFCompare","error FinalizeMergeCommand", e)); //$NON-NLS-1$ //$NON-NLS-1$
					}
			}
	        try {
	        	
				nested.commit();
				
			} catch (RollbackException e) {				
				e.printStackTrace();
			}
	}
	
	@Override
	protected boolean prepare() {		
		return true;
	}

	@Override
	public void redo() {		
	}

}
