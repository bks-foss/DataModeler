package com.isb.datamodeler.internal.ui.views.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;

/**
 * @generated
 */
public class DatamodelerAnnotateViewCommand extends
		AbstractTransactionalCommand {

	private ArrayList<View> _viewList;
	private String _annotation;
	private boolean _annotate;

	public DatamodelerAnnotateViewCommand(
			TransactionalEditingDomain editingDomain,
			ArrayList<View> viewList, String annotation, boolean enable) {
		
		super(editingDomain, "Annotate View Command", getFiles(viewList)); //$NON-NLS-1$
		_viewList = viewList;
		_annotation = annotation;
		_annotate = enable;
	}

	private static List<Object> getFiles(ArrayList<View> viewList)
	{
		List<Object> result = new ArrayList<Object>();
		
		for(View view:viewList)
		{
			for(Object obj:getWorkspaceFiles(view))
			{
				if(!result.contains(obj))
					result.add(obj);
			}
			
		}
		return result;
	}

	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {

		for(View view:_viewList)
		{
			EAnnotation annotation = view.getEAnnotation(_annotation);
			if (annotation != null && !_annotate)
					view.getEAnnotations().remove(annotation);
			if (annotation == null && _annotate){
				EAnnotation customAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				customAnnotation.setSource(_annotation);
				customAnnotation.getDetails().put("modelID", SchemaEditPart.MODEL_ID); //$NON-NLS-1$
				view.getEAnnotations().add(customAnnotation);
			}
		}

		return CommandResult.newOKCommandResult();
	}
}
