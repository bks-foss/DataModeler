package com.isb.datamodeler.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.isb.datamodeler.internal.ui.views.actions.DataModelerProblems;

public class CopyErrorsAction extends Action implements ISelectionChangedListener
{
	
	Clipboard _clipboard;
	private DataModelerProblems[] _messages;
	
	
	
public CopyErrorsAction(Clipboard clipboard) {
	_clipboard = clipboard;
	setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
}
@Override
public void run() {
	doCopy(_messages,_clipboard);
}
@Override
public void selectionChanged(SelectionChangedEvent event) {
	if(event.getSelection()instanceof IStructuredSelection)
	{
		IStructuredSelection sSelection = (IStructuredSelection) event.getSelection();
		Iterator it = sSelection.iterator();
		
		ArrayList<DataModelerProblems> messages = new ArrayList<DataModelerProblems>();
		while(it.hasNext())
		{
			DataModelerProblems element = null;
			Object next = it.next();
			
			if(next instanceof DataModelerProblems)
			{
				element = (DataModelerProblems) next;
			}
			
			if(element !=null)
			{
				messages.add(element);
			}
			else
			{
				messages.clear();
				break;
			}
		}
		_messages = (DataModelerProblems[]) messages.toArray(new DataModelerProblems[0]);
		
	}
	
}
static void doCopy(DataModelerProblems[] elements, Clipboard clipboard)
{
	if(elements==null || elements.length==0)
		return;
	// Get the file names and a string representation
	int len = elements.length;
	DataModelerProblems[] elementNames = new DataModelerProblems[len];
	StringBuffer buf = new StringBuffer();
	for(int i = 0, length = len; i < length; i++)
	{
		DataModelerProblems element = elements[i];
		if(i > 0)
			buf.append("\n"); //$NON-NLS-1$
		buf.append(element.getMessage());
	}

	// set the clipboard contents
	clipboard.setContents(new Object[]{elements, buf.toString()}, new Transfer[]{ElementTransfer.getInstance(),
		TextTransfer.getInstance()});
}
}
