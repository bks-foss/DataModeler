package com.isb.datamodeler.internal.ui.views.actions;

import java.util.ArrayList;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.isb.datamodeler.internal.ui.views.commands.DatamodelerAnnotateViewCommand;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.ui.UtilsDataModelerUI;

public abstract class AbstractAnnotateViewActionDelegate implements IEditorActionDelegate, IActionDelegate2{

	protected ArrayList<View> _nodeList;
	
	protected String _annotation;
	
	protected boolean _addAnnotation;
	
	StructuredSelection _structuredSelection;
	
	protected IEditorPart _editorPart;

	protected IAction _action;
	  
	@Override
	public void run(IAction action) {
		TransactionalEditingDomain editingDomain = DataModelerUtils.getDataModelerEditingDomain();
		
		ICommand annotateCommand = new DatamodelerAnnotateViewCommand(editingDomain,_nodeList,_annotation,_addAnnotation);

		try {
			OperationHistoryFactory.getOperationHistory().execute(annotateCommand, new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			UtilsDataModelerUI.log(e , "AnnotateViewActionDelegate"); //$NON-NLS-1$
			e.printStackTrace();
		}
	}
	
	public void setActiveEditor(IAction action, IEditorPart editorPart)
	{
		_editorPart = editorPart;
		_action = action;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void runWithEvent(IAction action, Event event) {
	    run(action);
	}
}
