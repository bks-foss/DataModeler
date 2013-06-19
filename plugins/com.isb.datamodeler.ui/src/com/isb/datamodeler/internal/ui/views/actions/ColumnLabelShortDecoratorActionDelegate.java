package com.isb.datamodeler.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;

public class ColumnLabelShortDecoratorActionDelegate extends AbstractAnnotateViewActionDelegate{
	
	StructuredSelection _structuredSelection;
	
	@Override
	public void run(IAction action) {
		
		_nodeList = new ArrayList<View>();
		
		for (Iterator<?> it = _structuredSelection.iterator(); it.hasNext();) {
			Object object = it.next();
			if(object instanceof GraphicalEditPart)
				loadNodesWithAnnotations((GraphicalEditPart)object);
		}
		super.run(action);
	}
	
	private void loadNodesWithAnnotations(GraphicalEditPart object){
		if(object.getNotationView().getElement()instanceof ETable)
			loadNodeRecursive(object);
	}
	
	private void loadNodeRecursive(GraphicalEditPart object){
		View view = object.getNotationView();
		
		if(view.getElement()instanceof EColumn &&
				view.getEAnnotation(_annotation)==null){
			_nodeList.add(view);
		}
		
		for(Object child:object.getChildren()){
			if(child instanceof GraphicalEditPart)
				loadNodeRecursive((GraphicalEditPart)child);
		}
	}

	@Override
	public void selectionChanged(IAction actionProxy, ISelection structuredSelection) {
		
		boolean actionEnabled = false;
		
		if(structuredSelection instanceof StructuredSelection){
			
			_structuredSelection = (StructuredSelection)structuredSelection;
			
			for (Iterator<?> it = _structuredSelection.iterator(); it.hasNext();) {
				Object object = it.next();
				if(object instanceof PersistentTableEditPart){
					if(((GraphicalEditPart)object).resolveSemanticElement() instanceof ETable){
						actionEnabled=true;
					}else{
						actionEnabled=false;
						break;
					}
				}
			}
		}

		actionProxy.setEnabled(actionEnabled);
	}

	@Override
	public void init(IAction action) {
		_annotation = "ShortLabelDecorator"; //$NON-NLS-1$
		_addAnnotation = true;
	}
}
