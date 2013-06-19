/**
 * 
 */
package com.isb.datamodeler.internal.ui.dialogs;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.swt.IFocusService;

import com.isb.datamodeler.internal.ui.views.actions.DataModelerProblems;

/**
 * @author Delia Salmerón
 *
 */
public class InformationListDialog extends MessageDialog
{


	static class CopyHandler extends AbstractHandler
	{
		private Action _action;
		
		public CopyHandler(Action a) 
		{
			_action = a;
		}
	
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			_action.run();
			return null;
		}
	
	}

	static class ControlExpression extends Expression 
	{
		private Control _control;
		
		public ControlExpression(Control c) 
		{
			_control = c;
		}
		
		@Override
		public void collectExpressionInfo(ExpressionInfo info) 
		{
			// when these variables change, they'll be re-evaluated
			info.addVariableNameAccess(ISources.ACTIVE_FOCUS_CONTROL_NAME);
		}
		
		@Override
		public EvaluationResult evaluate(IEvaluationContext context)throws CoreException 
		{
			return EvaluationResult
			.valueOf(context.getVariable(ISources.ACTIVE_FOCUS_CONTROL_NAME) == _control);
		}
	}

private IStructuredContentProvider _contentProvider;
private IBaseLabelProvider _labelProvider;


private IFocusService _focusService;
private IHandlerService _handlerService;


private Map<String, Collection<DataModelerProblems>> _inputs;

/**
 * 
 * @param parentShell
 * @param dialogTitle
 * @param dialogTitleImage
 * @param dialogMessage
 * @param dialogImageType
 * @param dialogButtonLabels
 * @param defaultIndex
 * @param contentProvider
 * @param labelProvider
 * @param inputs
 */
public InformationListDialog(Shell parentShell, String dialogTitle,
		Image dialogTitleImage, String dialogMessage, int dialogImageType,
		String[] dialogButtonLabels, int defaultIndex,
		IStructuredContentProvider contentProvider, IBaseLabelProvider labelProvider,
		Map<String, Collection<DataModelerProblems>> inputs ) {
	super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
			dialogImageType, dialogButtonLabels, defaultIndex);
	_contentProvider = contentProvider;
	_labelProvider = labelProvider;
	_inputs = inputs;

	setShellStyle(getShellStyle() | SWT.DIALOG_TRIM | SWT.RESIZE);
}

/* (non-Javadoc)
 * @see org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
 */
@Override
protected Control createCustomArea(Composite parent)
{
	// Inicializamos a parent por si _inputs está vacio
	Control control = parent;
	for (String inputElement : _inputs.keySet())
	{
		Label inputLabel = new Label(parent, SWT.LEFT);
		inputLabel.setText(inputElement);
		
		Table inputTable = new Table(parent, SWT.V_SCROLL | SWT.BORDER | SWT.MULTI);
		inputTable.setLinesVisible(true);
		
		Collection<DataModelerProblems> inputList = _inputs.get(inputElement);
		TableViewer tableViewer = new TableViewer(inputTable);
		tableViewer.setContentProvider(_contentProvider);
		tableViewer.setLabelProvider(_labelProvider);
		tableViewer.setInput(inputList);
		
		control = tableViewer.getControl();
		GridData data= new GridData(SWT.FILL, SWT.FILL, true, true);
		// Ajustamos el tamaño vertical de las tablas porque si tenemos
		// demasiados errores el diálogo no se muestra bien
		data.heightHint = 50;
		control.setLayoutData(data);
		applyDialogFont(control);
		
		CopyErrorsAction copyErrorsAction = new CopyErrorsAction(new Clipboard(Display.getDefault())) {
		};
		//implementacion del Ctrl+C  	
		// add the control to the focus service
		_focusService = (IFocusService) PlatformUI.getWorkbench().getService(IFocusService.class);
		_focusService.addFocusTracker(tableViewer.getControl(),"my.dialog.tree.unique.id");
		_handlerService = (IHandlerService)PlatformUI.getWorkbench().getService(IHandlerService.class);

		// activate the handler for that control
		final IHandlerActivation activation = _handlerService.activateHandler("org.eclipse.ui.edit.copy",
				new CopyHandler(copyErrorsAction),
				new ControlExpression(tableViewer.getControl()));

		// clean up by deactivating the handler. The focus tracker will
		// be removed on dispose automatically
		tableViewer.getControl().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) 
			{
				_handlerService.deactivateHandler(activation);
			}
		});
		
		// Creamos un menu contextual y añadimos la accion de copiar
		final CopyErrorsAction a = copyErrorsAction;
		final MenuManager mgr = new MenuManager();
		final TableViewer tv = tableViewer;
		mgr.setRemoveAllWhenShown(true);

		mgr.addMenuListener(new IMenuListener() {

			/* (non-Javadoc)
			 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
			 */
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
				if (!selection.isEmpty()) {
					a.setText("Copiar  Ctrl+C");
								
					mgr.add(a);
				}
			}
		});
		tableViewer.addSelectionChangedListener(a);
		tableViewer.getControl().setMenu(mgr.createContextMenu(tableViewer.getControl()));

	}

	return control;
}

}
