package com.isb.datamodeler.ui.views.workingsets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetPage;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.internal.ui.views.DatamodelerDomainNavigatorLabelProvider;
import com.isb.datamodeler.ui.DataModelerUI;

/**
 * Page for DataModeler Working Set Project Selection
 * @author xIS05655
 *
 */
public class DataModelerWorkingSetPage extends WizardPage implements
		IWorkingSetPage {
	
	public final static String DATAMODELER_WORKING_SET_ID = "com.isb.datamodeler.ui.dataModelerWorkingSet";
	
	private final static int SIZING_SELECTION_WIDGET_WIDTH = 50;

	private final static int SIZING_SELECTION_WIDGET_HEIGHT = 200;

	private Text text;

	private CheckboxTableViewer table;

	private IWorkingSet workingSet;
	
	final private static String PAGE_TITLE= Messages.bind("DataModelerWorkingSetPage_title");
	final private static String PAGE_NAME= "dataModelerWorkingSet"; //$NON-NLS-1$


	private boolean firstCheck = false; // set to true if selection is set in
										// setSelection

	/**
	 * Creates a new instance of the receiver.
	 */
	public DataModelerWorkingSetPage() {
		super(PAGE_NAME, PAGE_TITLE, DataModelerUI.imageDescriptorFromPlugin(DataModelerUI.PLUGIN_ID, "icons/full/wizban/java_workingset_wiz.png"));
		setDescription(Messages.bind("DataModelerWorkingSetPage_description"));
	}

	/**
	 * Overrides method in WizardPage.
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		setControl(composite);

		Label label = new Label(composite, SWT.WRAP);
		label.setText(Messages.bind("DataModelerResourceWorkingSetPage.message"));
		GridData data = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_CENTER);
		label.setLayoutData(data);

		text = new Text(composite, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validateInput();
			}
		});
		text.setFocus();

		label = new Label(composite, SWT.WRAP);
		label.setText(Messages.bind("DataModelerResourceWorkingSetPage.label_tree"));
		data = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_CENTER);
		label.setLayoutData(data);

		table = CheckboxTableViewer.newCheckList(composite, SWT.BORDER);
		table.setUseHashlookup(true);
		final ITreeContentProvider treeContentProvider = new DataModelerWorkingSetPageContentProvider();
		table.setContentProvider(treeContentProvider);
		table.setLabelProvider(new DatamodelerDomainNavigatorLabelProvider());
		table.setInput(ResourcesPlugin.getWorkspace().getRoot());
		table.setComparator(new DataModelerWorkingSetSorter());

		data = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
		data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
		data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
		table.getControl().setLayoutData(data);
		
		table.addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                handleCheckStateChange(event);
            }
        });

		// Add select / deselect all buttons for bug 46669
		Composite buttonComposite = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		buttonComposite.setLayout(layout);
		buttonComposite.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL));

		Button selectAllButton = new Button(buttonComposite, SWT.PUSH);
		selectAllButton
				.setText(Messages.bind("DataModelerResourceWorkingSetPage.selectAll.label"));
		selectAllButton
				.setToolTipText(Messages.bind("DataModelerResourceWorkingSetPage.selectAll.toolTip"));
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent selectionEvent) {
				BusyIndicator.showWhile(getShell().getDisplay(),
						new Runnable() {
							public void run() {
								table.setCheckedElements(treeContentProvider
										.getElements(table.getInput()));
							}
						});
				validateInput();
			}
		});
		setButtonLayoutData(selectAllButton);

		Button deselectAllButton = new Button(buttonComposite, SWT.PUSH);
		deselectAllButton
				.setText(Messages.bind("DataModelerResourceWorkingSetPage.deselectAll.label"));
		deselectAllButton
				.setToolTipText(Messages.bind("DataModelerResourceWorkingSetPage.deselectAll.toolTip"));
		deselectAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent selectionEvent) {
				BusyIndicator.showWhile(getShell().getDisplay(),
						new Runnable() {

							public void run() {
								table.setCheckedElements(new Object[0]);
							}
						});
				validateInput();
			}
		});
		setButtonLayoutData(deselectAllButton);

		initializeCheckedState();
		if (workingSet != null) {
			text.setText(workingSet.getName());
		}
		setPageComplete(false);

		Dialog.applyDialogFont(composite);
	}
	
    /**
     * Collects all checked resources in the specified container.
     * 
     * @param checkedResources the output, list of checked resources
     * @param container the container to collect checked resources in
     */
    private void findCheckedResources(List<IResource> checkedResources,
            IContainer container) {
        IResource[] resources = null;
        try {
            resources = container.members();
        } catch (CoreException ex) {
            handleCoreException(
                    ex,
                    getShell(),
                    Messages.bind("DataModelerResourceWorkingSetPage.error"),
                    Messages.bind("DataModelerResourceWorkingSetPage.error.updateCheckedState"));
        }
        for (int i = 0; i < resources.length; i++) {
        	if (table.getChecked(resources[i])) {
                checkedResources.add(resources[i]);
            }
        }
    }

	/**
	 * Implements IWorkingSetPage.
	 * 
	 * @see org.eclipse.ui.dialogs.IWorkingSetPage#finish()
	 */
	public void finish() {
        List<IResource> resources = new ArrayList<IResource>(10);
        findCheckedResources(resources, (IContainer) table.getInput());
        if (workingSet == null) {
            IWorkingSetManager workingSetManager = PlatformUI.getWorkbench()
                    .getWorkingSetManager();
            workingSet = workingSetManager.createWorkingSet(
                    getWorkingSetName(), (IAdaptable[]) resources
                            .toArray(new IAdaptable[resources.size()]));
        } else {
            workingSet.setName(getWorkingSetName());
            workingSet.setElements((IAdaptable[]) resources
                    .toArray(new IAdaptable[resources.size()]));
        }
	}

	/**
	 * Implements IWorkingSetPage.
	 * 
	 * @see org.eclipse.ui.dialogs.IWorkingSetPage#getSelection()
	 */
	public IWorkingSet getSelection() {
		return workingSet;
	}

	/**
	 * Returns the name entered in the working set name field.
	 * 
	 * @return the name entered in the working set name field.
	 */
	private String getWorkingSetName() {
		return text.getText();
	}
	
    /**
     * Called when the checked state of a tree item changes.
     * 
     * @param event the checked state change event.
     */
    private void handleCheckStateChange(final CheckStateChangedEvent event) {
        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
            public void run() {
                validateInput();
            }
        });
    }

	/**
	 * Displays an error message when a CoreException occured.
	 * 
	 * @param exception
	 *            the CoreException
	 * @param shell
	 *            parent shell for the message box
	 * @param title
	 *            the mesage box title
	 * @param message
	 *            additional error message
	 */
	private void handleCoreException(CoreException exception, Shell shell,
			String title, String message) {
		IStatus status = exception.getStatus();
		if (status != null) {
			ErrorDialog.openError(shell, title, message, status);
		} else {
			MessageDialog.openError(shell, Messages.bind("DataModelerResourceWorkingSetPage.error.internal"),
					exception.getLocalizedMessage());
		}
	}

	/**
	 * Sets the checked state of tree items based on the initial working set, if
	 * any.
	 */
	private void initializeCheckedState() {
		BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
			public void run() {
				Object[] items = null;
				if (workingSet == null) {

					IWorkbenchPage page = DataModelerUI.getDefault()
							.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage();
					if (page == null) {
						return;
					}
					IWorkbenchPart part = page.getActivePart();
					if (part == null) {
						return;
					}
					ISelection selection = page.getSelection();
					if (selection instanceof IStructuredSelection) {
						items = ((IStructuredSelection) selection).toArray();
					}

				} else {
					items = workingSet.getElements();
				}
				if (items == null) {
					return;
				}
				table.setCheckedElements(items);
			}
		});
	}

	/**
	 * Implements IWorkingSetPage.
	 * 
	 * @see org.eclipse.ui.dialogs.IWorkingSetPage#setSelection(IWorkingSet)
	 */
	public void setSelection(IWorkingSet workingSet) {
		if (workingSet == null) {
			throw new IllegalArgumentException("Working set must not be null"); //$NON-NLS-1$
		}
		this.workingSet = workingSet;
		if (getShell() != null && text != null) {
			firstCheck = true;
			initializeCheckedState();
			text.setText(workingSet.getName());
		}
	}

	/**
	 * Validates the working set name and the checked state of the resource
	 * tree.
	 */
	private void validateInput() {
		String errorMessage = null;
		String infoMessage = null;
		String newText = text.getText();

		if (newText.equals(newText.trim()) == false) {
			errorMessage = Messages.bind("DataModelerResourceWorkingSetPage.warning.nameWhitespace");
		} else if (firstCheck) {
			firstCheck = false;
			return;
		}
		if (newText.equals("")) { //$NON-NLS-1$
			errorMessage = Messages.bind("DataModelerResourceWorkingSetPage.warning.nameMustNotBeEmpty");
		}
		if (errorMessage == null
				&& (workingSet == null || newText.equals(workingSet.getName()) == false)) {
			IWorkingSet[] workingSets = PlatformUI.getWorkbench()
					.getWorkingSetManager().getWorkingSets();
			for (int i = 0; i < workingSets.length; i++) {
				if (newText.equals(workingSets[i].getName())) {
					errorMessage = Messages.bind("DataModelerResourceWorkingSetPage.warning.workingSetExists");
				}
			}
		}
		if (infoMessage == null && table.getCheckedElements().length == 0) {
			infoMessage = Messages.bind("DataModelerResourceWorkingSetPage.warning.resourceMustBeChecked");
		}
		setMessage(infoMessage, INFORMATION);
		setErrorMessage(errorMessage);
		setPageComplete(errorMessage == null);
	}
}
