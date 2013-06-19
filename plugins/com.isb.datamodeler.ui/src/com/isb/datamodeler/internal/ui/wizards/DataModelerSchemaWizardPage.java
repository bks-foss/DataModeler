package com.isb.datamodeler.internal.ui.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaFactory;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.model.validation.SchemaNameValidator;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerSchemaWizardPage extends WizardPage implements IWizardPage{

	private Text _schemaNameField;
	private Text _schemaDescriptionField;
	private Text _schemaProjectField;
	
	private ESchema _schema = ESchemaFactory.eINSTANCE.createSchema();
	
	private EProject _project;
	
	List<EProject> _orderedEProjects;
	
	private static final int LABEL_WIDTH = 118;
	
	private SchemaNameValidator _schemaNameValidator;
	
	protected DataModelerSchemaWizardPage(String pageName, IProject project) {
		
		super(pageName);
		
		_project = UtilsDataModelerUI.findEProject(project);
		_schemaNameValidator = new SchemaNameValidator(_schema , getProject().getDatabase().getId());
		
		_schema.setId(DataModelerUtils.generateRandomID(16) );
		
		// Buscamos los proyectos modelados existentes en el workspace
		Collection<EProject> eProjects = UtilsDataModelerUI.findDataModelerEProjects();
		
		// Ordenamos la lista poniendo primero el proyecto seleccionado
		_orderedEProjects = new ArrayList<EProject>();
		
    	List<EProject> eProjectOrdered = new ArrayList<EProject>();
    	eProjectOrdered.addAll(eProjects);
    	
    	Collections.sort(eProjectOrdered , new Comparator<EProject>() {
    		@Override
    		public int compare(EProject o1, EProject o2) {
    			
    			return o1.getIProject().getName().compareToIgnoreCase(o2.getIProject().getName());
    			
    		}
		});
    	// Primero añadimos el proyecto seleccionado y despues el resto
		
		Iterator<EProject> iter = eProjectOrdered.iterator();
        while (iter.hasNext()) {
        	EProject eproject = iter.next();
        	if(eproject.getIProject().getName().equalsIgnoreCase(project.getName()))
        		_orderedEProjects.add(0, eproject);
        	else
        	{
        		// Solo vamos a mostrar los proyectos visibles
        		Collection<String> projectsSelected = UtilsDataModelerUI.getSelectedProjectsInPreference();
        		if(projectsSelected.contains(eproject.getIProject().getName()))
        			_orderedEProjects.add(eproject);
        	}
        }
        setTitle(pageName);
    	setDescription(Messages.bind("DataModelerSchemaWizardPage.page.description"));//$NON-NLS-1$
	}

	private Listener _nameModifyListener = new Listener()
	{
	    public void handleEvent(Event e)
	    {  	
	    	boolean valid = validatePage();
	    	setPageComplete(valid);
	    }	
	};
	
	private Listener _projectModifyListener = new Listener()
	{
	    public void handleEvent(Event e)
	    {  	
	    	boolean valid = validatePage();
	    	setPageComplete(valid);
	    	_project = getEProject(_schemaProjectField.getText());
	    }	
	};
	
	@Override
	public void createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NULL);
		
		DataBindingContext bindingContext = new DataBindingContext();
		
		initializeDialogUnits(parent);
		
		GridLayout compositeLayout = new GridLayout();
		compositeLayout.numColumns = 3;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		_schemaProjectField = createTextAndButtonFieldGroup(
				composite, Messages.bind("DataModelerSchemaWizardPage.Schema.project"), SWT.BORDER, -1); //$NON-NLS-1$
		_schemaProjectField.addListener(SWT.Modify, _projectModifyListener);
		
		_schemaProjectField.setText(_orderedEProjects.get(0).getIProject().getName());
		
		createSeparator(composite);
		
		// Control para el nombre
		_schemaNameField = createLabelAndTextFieldGroup(
				composite, Messages.bind("DataModelerSchemaWizardPage.Schema.name"), SWT.BORDER, -1); //$NON-NLS-1$
		_schemaNameField.addListener(SWT.Modify, _nameModifyListener);
		
		bindingContext.bindValue(
				WidgetProperties.text(SWT.Modify).observe(_schemaNameField),
				EMFProperties.value(EcorePackage.Literals.ENAMED_ELEMENT__NAME)
						.observe(_schema));
		
		// Control para la descripción
		_schemaDescriptionField = createLabelAndTextFieldGroup(
				composite, Messages.bind("DataModelerSchemaWizardPage.Schema.description"), SWT.BORDER, -1); //$NON-NLS-1$
		_schemaDescriptionField.addListener(SWT.Modify, _nameModifyListener);

		bindingContext.bindValue(
				WidgetProperties.text(SWT.Modify).observe(_schemaDescriptionField),
				EMFProperties.value(ESchemaPackage.Literals.SCHEMA__DESCRIPTION)
						.observe(_schema));
		
		// Si ya está relleno el proyecto fijamos el foco en el nombre del esquema
		if(!_schemaProjectField.getText().isEmpty())
			_schemaNameField.setFocus();
		
		setPageComplete(validatePage());
		// Al iniciar la página no mostramos ningún error
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
		Dialog.applyDialogFont(composite);
	}
	
	/**
	 * Crea los controles específicos para el nombre del esquema
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
	    textGD.horizontalSpan = 2;
	    textGD.heightHint = heightHint;
	    nameField.setLayoutData(textGD);
	    nameField.setFont(parent.getFont());
	    
	    return nameField;
	}
	
	/**
	 * Crea los controles específicos para el proyecto del esquema
	 *
	 * @param parent
	 */
	private Text createTextAndButtonFieldGroup (Composite parent, String labelText, int style, int heightHint)
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
	    
	    // button
	    Button buttonField = new Button(parent, SWT.PUSH);
	    GridData buttonGD = new GridData(GridData.FILL);
	    buttonGD.heightHint = heightHint;
	    buttonGD.grabExcessHorizontalSpace = false;
	    
	    buttonField.setText(Messages.bind("DataModelerSchemaWizardPage.Button.label"));
	    buttonField.setLayoutData(buttonGD);

	    buttonField.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
				projectChangeControlPressed();
			}
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				projectChangeControlPressed();
			}
		});
	    
	    
	    return nameField;
	}
	
	private void projectChangeControlPressed()
	{
		EProject eProject = UtilsDataModelerUI.chooseEProject(getShell(),_orderedEProjects);
		if (eProject != null) 
			_schemaProjectField.setText(eProject.getIProject().getName());
	}
	
	private boolean validatePage()
	{
		// Initialize a variable with the no error status
		List<IStatus> statusList = new ArrayList<IStatus>();
	    Status status = new Status(IStatus.OK, "not_used", 0, "", null);
	    statusList.add(status); 
	    
		if(_schemaProjectField != null)
		{
			if(_schemaProjectField.getText().length()<1)
			{
				statusList.add(new Status(IStatus.ERROR, "not_used", 0, 
					Messages.bind("DataModelerSchemaWizardPage.StatusError.mustGiveProject"), null)); //$NON-NLS-1$
			}
			else
			{
				if(getEProject(_schemaProjectField.getText())==null)
					statusList.add(new Status(IStatus.ERROR, "not_used", 0, 
							Messages.bind("DataModelerSchemaWizardPage.StatusError.projectNotValid"), null)); //$NON-NLS-1$	
			}
		}
		if(_schemaNameField != null && _schemaNameField.getText().length()<1)
		{
			statusList.add(new Status(IStatus.ERROR, "not_used", 0, 
					Messages.bind("DataModelerSchemaWizardPage.StatusError.mustGiveName"), null)); //$NON-NLS-1$
		}
		else if(_schemaNameField != null && _schemaNameField.getText().length()>0){
			// validar que no existe un esquema con el mismo nombre
			EProject project = getEProject(_schemaProjectField.getText());	
			if(project != null)
			{
				for(ESchema schema:project.getSchemas())
				{
					String schemaName = schema.getName();
					if( schemaName!=null && schemaName.equalsIgnoreCase(_schemaNameField.getText()))
						statusList.add(new Status(IStatus.ERROR, "not_used", 0, 
								Messages.bind("DataModelerSchemaWizardPage.StatusError.duplicateSchema"), null)); //$NON-NLS-1$
				}
			}
			// validar que no supera la longitud maxima
			IStatus result = _schemaNameValidator.validate(_schemaNameField.getText(), "name");
				if(result!=null && !result.isOK())
					statusList.add(result);
			
		}
		
		if(_schemaDescriptionField != null && _schemaDescriptionField.getText().length()<1)
			statusList.add(new Status(IStatus.WARNING, "not_used", 0, 
					Messages.bind("DataModelerSchemaWizardPage.StatusWarning.mustGiveDescription"), null)); //$NON-NLS-1$
		
		status = (Status) UtilsDataModelerUI.findMostSevere(statusList);
		
		UtilsDataModelerUI.applyToStatusLine(this, status);
	
		return !status.matches(IStatus.ERROR);	
	}
	
	private EProject getEProject(String name)
	{
		for(EProject project : _orderedEProjects)
			if(project.getIProject().getName().equalsIgnoreCase(name))
				return project;
		return null;
	}
	
	public ESchema getSchema()
	{
	    return _schema;
	}
	
	public EProject getProject()
	{
	    return _project;
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
}
