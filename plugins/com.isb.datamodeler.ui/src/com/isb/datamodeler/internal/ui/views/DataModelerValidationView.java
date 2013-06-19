package com.isb.datamodeler.internal.ui.views;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.team.internal.ccvs.core.CVSException;
import org.eclipse.team.internal.ccvs.core.ICVSRemoteResource;
import org.eclipse.team.internal.ccvs.core.ICVSRepositoryLocation;
import org.eclipse.team.internal.ccvs.core.resources.CVSWorkspaceRoot;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.swt.IFocusService;

import com.isb.datamodeler.internal.ui.dialogs.CopyErrorsAction;
import com.isb.datamodeler.internal.ui.dialogs.ExportDialog;
import com.isb.datamodeler.internal.ui.dialogs.InformationListContentProvider;
import com.isb.datamodeler.internal.ui.dialogs.InformationListLabelProvider;
import com.isb.datamodeler.internal.ui.views.actions.DataModelerProblems;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.UtilsDataModelerUI;

/**
 * Vista de validación de DataModeler.
 */
public class DataModelerValidationView extends ViewPart implements IMenuListener
{
	public static final String ID = "com.isb.datamodeler.internal.ui.views.DataModelerValidationView";
	private static Map<String, Collection<DataModelerProblems>> _inputs;
	private static IProject _project;
	private Action _exportAction;
	private IFocusService _focusService;
	private IHandlerService _handlerService;

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

public void createPartControl(Composite parent)
{
	if(_inputs == null)
		return;
	
	GridLayout gl = new GridLayout();
	parent.setLayout(gl);
	
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
		tableViewer.setContentProvider(new InformationListContentProvider());
		tableViewer.setLabelProvider(new InformationListLabelProvider());
		tableViewer.setInput(inputList);
		
		control = tableViewer.getControl();
		GridData data= new GridData(SWT.FILL, SWT.FILL, true, true);
		// Ajustamos el tamaño vertical de las tablas porque si tenemos
		// demasiados errores el diálogo no se muestra bien
		data.heightHint = 50;
		control.setLayoutData(data);

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
		final CopyErrorsAction copyErrorsActionFinal = copyErrorsAction;
		final MenuManager mgr = new MenuManager();
		final TableViewer tableViewerFinal = tableViewer;
		mgr.setRemoveAllWhenShown(true);

		mgr.addMenuListener(new IMenuListener() 
		{
			public void menuAboutToShow(IMenuManager manager) 
			{
				IStructuredSelection selection = (IStructuredSelection)tableViewerFinal.getSelection();
				if (!selection.isEmpty()) 
				{
					copyErrorsActionFinal.setText("Copiar  Ctrl+C");
					mgr.add(copyErrorsActionFinal);
				}
			}
		});
		tableViewer.addSelectionChangedListener(copyErrorsActionFinal);
		tableViewer.getControl().setMenu(mgr.createContextMenu(tableViewer.getControl()));
	}
	
	makeActions();
	contributeToActionBars();
}

private void makeActions() 
{
	_exportAction = new Action()
	{
		public void run()
		{
			exportData();
		}
	};
	
	_exportAction.setText("Exportar"); //$NON-NLS-1$
	_exportAction.setToolTipText("Exportar"); //$NON-NLS-1$
	_exportAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_EXPORT_WIZ));
}

private void contributeToActionBars()
{
	IActionBars bars = getViewSite().getActionBars();
	fillLocalPullDown(bars.getMenuManager());
	fillLocalToolBar(bars.getToolBarManager());
}

private void fillLocalPullDown(IMenuManager manager)
{
	manager.add(_exportAction);
}

private void fillLocalToolBar(IToolBarManager manager)
{
	manager.add(_exportAction);
}

public void menuAboutToShow(IMenuManager manager)
{
}

@Override
public void setFocus() 
{
}

public static void setInput(Map<String, Collection<DataModelerProblems>> inputs) 
{
	_inputs = inputs;
}

public static void setProject(IProject project) 
{
	_project = project;
}

protected void exportData() 
{
	String repositoryInfo = "";
	try
	{
		ICVSRemoteResource remoteResource = CVSWorkspaceRoot.getRemoteResourceFor(_project.getProject());
		
		// Si no existe el recurso remoto es que no está conectado al CVS
		if(remoteResource!=null)
		{
			ICVSRepositoryLocation repositoryLocation = remoteResource.getRepository();
			
			String repositoryHost = repositoryLocation.getHost();
			String repositoryFolder = repositoryLocation.getRootDirectory();
			
			repositoryInfo = repositoryHost + repositoryFolder;
		}
		
		new ExportDialog(DataModelerUI.getActiveWorkbenchShell(), _inputs, _project.getName(), repositoryInfo).show();
	}
	catch (CVSException e)
	{
		UtilsDataModelerUI.log(e , "");
	}

}

}