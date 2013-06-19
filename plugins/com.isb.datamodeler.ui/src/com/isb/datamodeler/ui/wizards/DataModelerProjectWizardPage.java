package com.isb.datamodeler.ui.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.dialogs.WorkingSetConfigurationBlock;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.model.triggers.DataModelerTriggersPlugin;
import com.isb.datamodeler.model.triggers.initializers.IDatamodelerInitializer;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.model.validation.ApplicationNameValidator;
import com.isb.datamodeler.ui.model.validation.ProjectNameValidator;
import com.isb.datamodeler.ui.project.EProject;
import com.isb.datamodeler.ui.project.EProjectFactory;
import com.isb.datamodeler.ui.project.EProjectPackage;
import com.isb.datamodeler.ui.project.provider.ProjectItemProviderAdapterFactory;
import com.isb.datamodeler.ui.views.workingsets.DataModelerWorkingSetPage;


public class DataModelerProjectWizardPage extends WizardPage implements IWizardPage
{

	private static final int LABEL_WIDTH = 135;
	
	private static ProjectNameValidator _projectNameValidator = new ProjectNameValidator(); 
	private static ApplicationNameValidator _appNameValidator = new ApplicationNameValidator();

	
	private Combo _dataBaseField;
	
	private Map<EStructuralFeature, Control> featureControlsMap = new HashMap<EStructuralFeature, Control>(); 
	private Map<Control, TextFieldValidator> controlValidatorsMap = new HashMap<Control, TextFieldValidator>(); 

	private Text _projectTextField;
	
	private EProject _project;
	
	private EDatabase _database;
	
	private TransactionalEditingDomain _editingDomain = DataModelerUtils.getDataModelerEditingDomain();
	
	private Composite composite;
	
	private WorkingSetGroup workingSetGroup;
	
	private DataBindingContext bindingContext;
	
	private ArrayList<CommandParameter> _newChildDescriptors;

	protected TextFieldMoficationListener _nameModifyListener = new TextFieldMoficationListener();
	
	private TextFieldMoficationListener _acronymModifyListener = new TextFieldMoficationListener()
	{
	    public void handleModification(String newValue)
	    {
			if(newValue == null || 
					newValue.trim().equals("")) //$NON-NLS-1$
				return;
			
			String prjName = "";
			
			EProject tmpProject  = EProjectFactory.eINSTANCE.createProject();
			
			tmpProject.setApplication(newValue);
	
			IDatamodelerInitializer initializer = DataModelerTriggersPlugin.getInstance().getInitializer(_project);
			if(initializer!=null)
				prjName = initializer.generateChildName(tmpProject, EProject.class);

			_projectTextField.setText(prjName);
	    }	
	};
	
	
	public class TextFieldMoficationListener implements Listener {

		@Override
		public final void handleEvent(Event event) {
			
			String newValue = (String)((Text)event.widget).getText();
			
			handleModification(newValue);
			
	    	boolean valid = validatePage();
	    	setPageComplete(valid);
		}

	    public void handleModification(String newValue)
	    {  	
	    }	

	}

	
	public interface TextFieldValidator
	{
		public IStatus validateTextField(Text field);
	}

	private TextFieldValidator _applicationFieldValidator = new TextFieldValidator()
	{

		@Override
		public IStatus validateTextField(Text applicationNameField) {
			
			if(applicationNameField != null && applicationNameField.getText().length()<1)
				return new Status(IStatus.ERROR, "not_used", 0, 
						Messages.bind("DataModelerProjectWizardPage.StatusError.mustGiveApplication"), null); //$NON-NLS-1$
			else 
			{
				IStatus result = _appNameValidator.validate(applicationNameField.getText(), "name");
				if(result!=null && !result.isOK())
					return result;
			}
			
			return null;
		}
		
	};
	
	private TextFieldValidator _projectFieldValidator = new TextFieldValidator()
	{
		@Override
		public IStatus validateTextField(Text projectNameField) {

			if(projectNameField != null)
			{
				if(projectNameField.getText().length()<1)
				{
					return new Status(IStatus.ERROR, "not_used", 0, 
						Messages.bind("DataModelerProjectWizardPage.StatusError.mustGiveProject"), null); //$NON-NLS-1$
				}
				else
				{
					IStatus result = _projectNameValidator.validate(projectNameField.getText(), "name");
					if(result!=null && !result.isOK())
						return result;
					else if(existsProjectInWorkspace(projectNameField.getText()))
						return new Status(IStatus.ERROR, "not_used", 0, 
								Messages.bind("DataModelerProjectWizardPage.StatusError.projectAlreadyExists"), null); //$NON-NLS-1$	
				}
			}
			
			return null;
		}
		
	};
	
	private TextFieldValidator _descriptionFieldValidator = new TextFieldValidator()
	{
		@Override
		public IStatus validateTextField(Text descriptionField) {

			if(descriptionField != null && descriptionField.getText().length()<1)
				return new Status(IStatus.WARNING, "not_used", 0, 
						Messages.bind("DataModelerProjectWizardPage.StatusWarning.mustGiveDescription"), null); //$NON-NLS-1$
			if(_dataBaseField != null && (_dataBaseField.getText().length()==0 || !isValidDataBaseFieldText(_dataBaseField.getText())))
			{
				return new Status(IStatus.ERROR, "not_used", 0, 
						"El tipo de Base de Datos no es válido.", null); //$NON-NLS-1$
			}
			
			return null;
		}
		
	};
	
	private Listener _dataBaseModifyListener = new Listener() 
	{
	    public void handleEvent(Event e)
	    {
		    int i = _dataBaseField.getSelectionIndex();
		    if(i!=-1)
		    	_database = (EDatabase)_newChildDescriptors.get(i).getEValue();
		    else _database = null;
		    boolean valid = validatePage();
	    	setPageComplete(valid);
	    }	
	};
	
	@SuppressWarnings("unchecked")
	public DataModelerProjectWizardPage()
	{
		this(Messages.bind("DataModelerProjectWizard.dataModelerProjectWizardPage.name"));
	}
	
	@SuppressWarnings("unchecked")
	public DataModelerProjectWizardPage(String name)
	{
		super(name);
		
		setTitle(Messages.bind("DataModelerProjectWizardPage.page.title")); //$NON-NLS-1$
		setDescription(Messages.bind("DataModelerProjectWizardPage.page.description")); //$NON-NLS-1$

	}
	
	protected EProject createProject()
	{
		return EProjectFactory.eINSTANCE.createProject();
	}
	
	
	@Override
	public final void createControl(Composite parent) {
		
		init();
		
		composite = new Composite(parent, SWT.NULL);
		
		bindingContext = new DataBindingContext();
		
		initializeDialogUnits(parent);
		
		GridLayout compositeLayout = new GridLayout();
		compositeLayout.numColumns = 2;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// Control para la aplicación
		createProjectWizardTextField(EProjectPackage.Literals.PROJECT__APPLICATION, Messages.bind("DataModelerProjectWizardPage.application.name"), _acronymModifyListener, _applicationFieldValidator);

		// Control para la subaplicación
		createCustomSection(composite);
		//createProjectWizardTextField(ESchemaPackage.Literals.FUNCTIONAL_ELEMENT__CAPABILITY, Messages.bind("DataModelerProjectWizardPage.subapplication.name"), _nameModifyListener, _capabilityFieldValidator);
		
		// Control para el nombre del proyecto
		_projectTextField = createProjectWizardTextField(ESchemaPackage.Literals.DATA_MODELER_NAMED_ELEMENT__NAME, Messages.bind("DataModelerProjectWizardPage.project.name"), _nameModifyListener, _projectFieldValidator);
		
		// Control para la base de datos
		_dataBaseField = createLabelAndComboDatabaseFieldGroup(
				composite, Messages.bind("DataModelerProjectWizardPage.database.name"), SWT.DROP_DOWN, -1); //$NON-NLS-1$
		_dataBaseField.select(0);
		_dataBaseField.addListener(SWT.Modify, _dataBaseModifyListener);

		
		// Separador
		createSeparator(composite);
		
		// Control para la descripción
		createProjectWizardTextField(EProjectPackage.Literals.PROJECT__DESCRIPTION, Messages.bind("DataModelerProjectWizardPage.description"), _nameModifyListener, _descriptionFieldValidator);
		
		createWorkingSetGroup(composite, getSelection(),
				new String[] { DataModelerWorkingSetPage.DATAMODELER_WORKING_SET_ID }); //$NON-NLS-1$

		Control focusedControl = featureControlsMap.get(getFeatureControlFocused());
		
		if(focusedControl!=null)
			focusedControl.setFocus();
		
		setPageComplete(validatePage());
		// Al iniciar la página no mostramos ningún error
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
		
		Dialog.applyDialogFont(composite);
	}
	
    private IStructuredSelection getSelection() {
		return ((DataModelerProjectWizard)getWizard()).getSelection();
	}

	/**
	 * Create a working set group for this page. This method can only be called
	 * once.
	 * 
	 * @param composite
	 *            the composite in which to create the group
	 * @param selection
	 *            the current workbench selection
	 * @param supportedWorkingSetTypes
	 *            an array of working set type IDs that will restrict what types
	 *            of working sets can be chosen in this group
	 * @return the created group. If this method has been called previously the
	 *         original group will be returned.
	 * @since 3.4
	 */
	public WorkingSetGroup createWorkingSetGroup(Composite composite,
			IStructuredSelection selection, String[] supportedWorkingSetTypes) {

		workingSetGroup = new WorkingSetGroup(composite, selection,
				supportedWorkingSetTypes);
		
		return workingSetGroup;
	}
	
	private void init()
	{
		_project = createProject();
		
		ProjectItemProviderAdapterFactory projectAdapterFactory = new ProjectItemProviderAdapterFactory();
		
		// Recuperamos los CommandParameter para la creación de Bases de datos extendidas
		_newChildDescriptors = (ArrayList<CommandParameter>) projectAdapterFactory.getNewChildDescriptors(_project, _editingDomain);

	}
	
	protected void createCustomSection(Composite parent)
	{
		
	}
	
	/**
	 * Returns the Control witch will has the focus in this page.
	 * @return
	 */
	private EStructuralFeature getFeatureControlFocused()
	{
		return EProjectPackage.Literals.PROJECT__APPLICATION;
	}
	
	protected final Text createProjectWizardTextField(EStructuralFeature feature, String label, TextFieldMoficationListener listener, TextFieldValidator validator)
	{
		Text textField = createLabelAndTextFieldGroup(
				composite, label, SWT.BORDER, -1); //$NON-NLS-1$
		
		if(listener!=null)
			textField.addListener(SWT.Modify, listener);
		
		if(validator!=null)
			controlValidatorsMap.put(textField, validator);

		bindingContext.bindValue(
				WidgetProperties.text(SWT.Modify).observe(textField),
				EMFProperties.value(feature)
						.observe(_project));
		
		featureControlsMap.put(feature, textField);
		
		return textField;
	}
	
	/**
	 * Crea los controles específicos para el nombre del proyecto
	 *
	 * @param parent
	 */
	private Text createLabelAndTextFieldGroup(Composite parent, String labelText, int style, int heightHint)
	{
	    // label
	    Label label = new Label(parent, SWT.NONE);
	    label.setText(labelText);
	    label.setFont(parent.getFont());
	    GridData labelGD = new GridData(GridData.BEGINNING);
	    labelGD.widthHint = LABEL_WIDTH;
	    label.setLayoutData(labelGD);
	
	    // field
	    Text nameField = new Text(parent, style);
	    GridData textGD = new GridData(GridData.FILL_HORIZONTAL);
	    textGD.heightHint = heightHint;
	    nameField.setLayoutData(textGD);
	    nameField.setFont(parent.getFont());
	    
	    return nameField;
	}
	
	private Combo createLabelAndComboDatabaseFieldGroup(Composite parent, String labelText, int style, int heightHint)
	{
	    // label
	    Label label = new Label(parent, SWT.NONE);
	    label.setText(labelText);
	    label.setFont(parent.getFont());
	    GridData labelGD = new GridData(GridData.BEGINNING);
	    labelGD.widthHint = LABEL_WIDTH;
	    label.setLayoutData(labelGD);
	
	    // field
	    Combo dbField = new Combo(parent, style);
	    GridData comboGD = new GridData(GridData.FILL_HORIZONTAL);
	    comboGD.heightHint = heightHint;
	    dbField.setLayoutData(comboGD);
	    dbField.setFont(parent.getFont());
	    
	    List<Object> selection = new ArrayList<Object>();
	    selection.add(_project);
	    for(CommandParameter commandParameter:_newChildDescriptors)
	    {
	    	EDatabase database = (EDatabase)commandParameter.getEValue();
	    	
	    	CreateChildCommand command =(CreateChildCommand)CreateChildCommand.create(_editingDomain, _project,commandParameter,selection);

	    	String cmdLabel = database.getName()!=null?database.getName():command.getLabel();
	    	if(cmdLabel.startsWith("New ") || cmdLabel.startsWith("Nuevo ") )
	    		cmdLabel = cmdLabel.substring(cmdLabel.indexOf(" ")+1);
	    	dbField.add(cmdLabel);
	    }
	    
	    _database = (EDatabase)_newChildDescriptors.get(0).getEValue();
	    
	    return dbField;
	}
	
	private void createSeparator(Composite parent)
	{
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalSpan = 3;
		gd.heightHint = 20;
		
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(gd);
	}
	
	public final EProject getProject()
	{
	    return _project;
	}
	
	public final EDatabase getDataBase()
	{
		return _database;
	}
	
	private boolean validatePage()
	{
		// Initialize a variable with the no error status
		List<IStatus> statusList = new ArrayList<IStatus>();
	    Status status = new Status(IStatus.OK, "not_used", 0, "", null);
	    statusList.add(status); 
	    
	    for(Control control: featureControlsMap.values())
	    {
	    	if(!(control instanceof Text))
	    		continue;
	    	
	    	TextFieldValidator validator = controlValidatorsMap.get(control);
	    	if(validator!=null)
	    	{
	    		IStatus validationResult = validator.validateTextField((Text)control);
	    		if(validationResult!=null)
	    			statusList.add(validationResult);
	    	}
	    		
	    }
		
		status = (Status) UtilsDataModelerUI.findMostSevere(statusList);
		
		UtilsDataModelerUI.applyToStatusLine(this, status);
	
		return !status.matches(IStatus.ERROR);	
	}
	
	private boolean isValidDataBaseFieldText(String text)
	{
		String[] items = _dataBaseField.getItems();
		for(String item : items)
		{
			if(item.equals(text))
				return true;
		}
		return false;
	}
	
	protected final boolean existsProjectInWorkspace(String name)
	{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for(IProject project:root.getProjects())
		{
			if(project.getName().equalsIgnoreCase(name))
				return true;
		}

		return false;
	}
	
    /**
	 * Return the selected working sets, if any. If this page is not configured
	 * to interact with working sets this will be an empty array.
	 * 
	 * @return the selected working sets
	 * @since 3.4
	 */
	public IWorkingSet[] getSelectedWorkingSets() {
		return workingSetGroup == null ? new IWorkingSet[0] : workingSetGroup
				.getSelectedWorkingSets();
	}
	
	private class WorkingSetGroup {

		private WorkingSetConfigurationBlock workingSetBlock;

		/**
		 * Create a new instance of this class.
		 * 
		 * @param composite
		 *            parent composite
		 * @param currentSelection
		 *            the initial working set selection to pass to the
		 *            {@link WorkingSetConfigurationBlock}
		 * @param workingSetTypes
		 *            the types of working sets that can be selected by the
		 *            {@link WorkingSetConfigurationBlock}
		 */
		public WorkingSetGroup(Composite composite,
				IStructuredSelection currentSelection, String[] workingSetTypes) {
			Group workingSetGroup = new Group(composite, SWT.NONE);
			workingSetGroup.setFont(composite.getFont());
			workingSetGroup
					.setText(WorkbenchMessages.WorkingSetGroup_WorkingSets_group);
			workingSetGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
					false, 2, 1));
			workingSetGroup.setLayout(new GridLayout(1, false));

			workingSetBlock = new WorkingSetConfigurationBlock(workingSetTypes,
					WorkbenchPlugin.getDefault().getDialogSettings());
			workingSetBlock.setWorkingSets(workingSetBlock
					.findApplicableWorkingSets(currentSelection));
			workingSetBlock.createContent(workingSetGroup);
		}

		/**
		 * Return the working sets selected by the contained
		 * {@link WorkingSetConfigurationBlock}.
		 * 
		 * @return the selected working sets
		 */
		public IWorkingSet[] getSelectedWorkingSets() {
			return workingSetBlock.getSelectedWorkingSets();
		}
	}
}
