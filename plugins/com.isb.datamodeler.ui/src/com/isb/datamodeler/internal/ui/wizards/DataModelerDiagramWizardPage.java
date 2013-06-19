package com.isb.datamodeler.internal.ui.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
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
import com.isb.datamodeler.diagram.validation.DiagramNameValidator;
import com.isb.datamodeler.model.core.DataModelerUtils;
import com.isb.datamodeler.model.triggers.DataModelerTriggersPlugin;
import com.isb.datamodeler.model.triggers.initializers.IDatamodelerInitializer;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.project.EProject;

public class DataModelerDiagramWizardPage extends WizardPage implements IWizardPage
{
	private Text _diagramNameField;
	private Text _diagramDescriptionField;
	private Text _diagramProjectField;
	private Text _diagramSchemaField;
	
	private static DiagramNameValidator _diagramNameValidator = new DiagramNameValidator(); 
	
	private static final int LABEL_WIDTH = 118;
	
	private EProject _project;
	
	private ESchema _schema;
	
	List<EProject> _orderedEProjects;
	
	List<ESchema> _orderedESchemas;
	
	protected DataModelerDiagramWizardPage(String pageName, ESchema schema) {
		super(pageName);
		
		_schema = schema;
		
		TransactionalEditingDomain myEditingDomain = DataModelerUtils.getDataModelerEditingDomain();
		if(_schema.eIsProxy())
			_schema = (ESchema)EcoreUtil.resolve(_schema, myEditingDomain.getResourceSet());
		
		// Cargamos la lista de proyectos
		EProject eproject = (EProject)schema.eContainer();
		if(eproject.eIsProxy())
			eproject = (EProject) EcoreUtil.resolve(eproject, myEditingDomain.getResourceSet());
		
		// Buscamos los proyectos modelados existentes en el workspace
		Collection<EProject> eProjects = UtilsDataModelerUI.findDataModelerEProjects();
		
		// Ordenamos la lista poniendo primero el proyecto seleccionado
		_orderedEProjects = new ArrayList<EProject>();
		
		Iterator<EProject> iter = eProjects.iterator();
        while (iter.hasNext()) {
        	EProject element = iter.next();
        	if(element.equals(eproject))
        		_orderedEProjects.add(0, element);
        	else
        	{
        		// Solo vamos a mostrar los proyectos visibles
        		Collection<String> projectsSelected = UtilsDataModelerUI.getSelectedProjectsInPreference();
        		if(projectsSelected.contains(element.getIProject().getName()))
        				_orderedEProjects.add(element);
        	}
        }
    	
    	// Ordenamos la lista poniendo primero el esquema seleccionado
    	getOrderedSchemas(_orderedEProjects.get(0)); 
        
        setTitle(pageName);
    	setDescription(Messages.bind("DataModelerDiagramWizardPage.page.description"));//$NON-NLS-1$
	}
	
	private void getOrderedSchemas(EProject project)
	{
		_orderedESchemas = new ArrayList<ESchema>();
		// Cargamos la lista de schemas
    	List<ESchema> eSchemas = new ArrayList<ESchema>();
    	// Eliminamos los esquemas externos
    	for(ESchema schema:project.getSchemas())
    	{
    		if(project.getCapability().equals(schema.getCapability()))
    			eSchemas.add(schema);
    	}
    	// Ordenamos los esquemas
    	List<ESchema> eSchemasOrdered = new ArrayList<ESchema>();
    	eSchemasOrdered.addAll(eSchemas);
    	
    	Collections.sort(eSchemasOrdered , new Comparator<ESchema>() {
    		@Override
    		public int compare(ESchema o1, ESchema o2) {
    			
    			return o1.getName().compareToIgnoreCase(o2.getName());
    			
    		}
		});
    	// Primero añadimos el esquema seleccionado y despues el resto
		Iterator<ESchema> iterS = eSchemasOrdered.iterator();
        while (iterS.hasNext()) {
        	ESchema element = iterS.next();
        	if(element.equals(_schema))
        		_orderedESchemas.add(0, element);
        	else
        		_orderedESchemas.add(element);
        }
	}

	private Listener _nameModifyListener = new Listener()
	{
	    public void handleEvent(Event e)
	    {  	
	    	_diagramNameField.getText();
	    	boolean valid = validatePage();
	    	setPageComplete(valid);
	    }	
	};
	
	private Listener _projectModifyListener = new Listener()
	{
	    public void handleEvent(Event e)
	    {
	    	EProject projectSelected = getEProject(_diagramProjectField.getText());
	    	if(projectSelected!=null && !projectSelected.equals(_project))
	    	{
				_project = projectSelected;
		    	getOrderedSchemas(_project);
		    	refreshDiagramName();
		    	if(_diagramSchemaField!=null)
		    		_diagramSchemaField.setText("");
	    	}
	    	else if(projectSelected==null)
	    		_project = null;
	    	
	    	boolean valid = validatePage();
	    	setPageComplete(valid);
	    }
	};
	
	private SelectionListener _projectButtonListener = new SelectionListener()
	{
		@Override
		public void widgetDefaultSelected(SelectionEvent selectionevent) {
			changeProjectControlPressed();
		}
		@Override
		public void widgetSelected(SelectionEvent selectionevent) {
			changeProjectControlPressed();
		}
	};
	
	private void changeProjectControlPressed()
	{
		EProject eProject = UtilsDataModelerUI.chooseEProject(getShell(),_orderedEProjects);
		if (eProject != null) 
		{
			_diagramProjectField.setText(eProject.getIProject().getName());
		}
	}
	
	private SelectionListener _schemaButtonListener = new SelectionListener()
	{
		@Override
		public void widgetDefaultSelected(SelectionEvent selectionevent) {
			changeSchemaControlPressed();
		}
		@Override
		public void widgetSelected(SelectionEvent selectionevent) {
			changeSchemaControlPressed();
		}
	};
	
	private void changeSchemaControlPressed()
	{
		ESchema eSchema = UtilsDataModelerUI.chooseESchema(getShell(),_orderedESchemas);
		if (eSchema != null) 
		{
			_diagramSchemaField.setText(eSchema.getName());
		}
	}
	
	private Listener _schemaModifyListener = new Listener()
	{
	    public void handleEvent(Event e)
	    {  	
	    	boolean valid = validatePage();
	    	setPageComplete(valid);
	    	_schema = getESchema(_diagramSchemaField.getText());
	    	refreshDiagramName();
	    }	
	};
	
	private void refreshDiagramName()
	{
		if(_diagramNameField!=null)
			_diagramNameField.setText(getNewDefaultDiagramName());
	}
	
	private String getNewDefaultDiagramName()
	{
		String diagramName = "";
		
		IDatamodelerInitializer initializer = DataModelerTriggersPlugin.getInstance().getInitializer(_schema);
		if(initializer!=null)
			diagramName = initializer.generateChildName(_schema, Diagram.class);
		
		return diagramName;
	}

	public String getDiagramName()
	{
		return _diagramNameField.getText();
	}
	
	@Override
	public void createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NULL);
		
		initializeDialogUnits(parent);
		
		GridLayout compositeLayout = new GridLayout();
		compositeLayout.numColumns = 3;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		_diagramProjectField = createTextAndButtonFieldGroup(
				composite, Messages.bind("DataModelerDiagramWizardPage.Diagram.project"), SWT.BORDER, -1, _projectButtonListener); //$NON-NLS-1$
		_diagramProjectField.addListener(SWT.Modify, _projectModifyListener);
		
		_diagramProjectField.setText(_orderedEProjects.get(0).getIProject().getName());
		
		_diagramSchemaField = createTextAndButtonFieldGroup(
				composite, Messages.bind("DataModelerDiagramWizardPage.Diagram.schema"), SWT.BORDER, -1, _schemaButtonListener); //$NON-NLS-1$
		_diagramSchemaField.addListener(SWT.Modify, _schemaModifyListener);
		
		_diagramSchemaField.setText(_orderedESchemas.get(0).getName());
		
		createSeparator(composite);
		
		// Control para el nombre
		_diagramNameField = createLabelAndTextFieldGroup(
				composite, Messages.bind("DataModelerSchemaWizardPage.Schema.name"), SWT.BORDER, -1); //$NON-NLS-1$
		_diagramNameField.addListener(SWT.Modify, _nameModifyListener);
		
		refreshDiagramName();
		
//		bindingContext.bindValue(
//				WidgetProperties.text(SWT.Modify).observe(_diagramNameField),
//				EMFProperties.value(EcorePackage.Literals.ENAMED_ELEMENT__NAME)
//						.observe(_schema));
		
		// Control para la descripción
		_diagramDescriptionField = createLabelAndTextFieldGroup(
				composite, Messages.bind("DataModelerSchemaWizardPage.Schema.description"), SWT.BORDER, -1); //$NON-NLS-1$
		_diagramDescriptionField.addListener(SWT.Modify, _nameModifyListener);

//		bindingContext.bindValue(
//				WidgetProperties.text(SWT.Modify).observe(_diagramDescriptionField),
//				EMFProperties.value(ESchemaPackage.Literals.SQL_OBJECT__DESCRIPTION)
//						.observe(_schema));
		
		setPageComplete(validatePage());
		// Al iniciar la página no mostramos ningún error
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
		Dialog.applyDialogFont(composite);
		
	}

	/**
	 * Crea los controles label + text
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
	 * Crea los controles label + text+ button
	 *
	 * @param parent
	 */
	private Text createTextAndButtonFieldGroup (Composite parent, String labelText, int style, int heightHint, SelectionListener listener)
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

	    buttonField.addSelectionListener(listener);
	    		 
	    return nameField;
	}
	
	public EProject getEProject(String name)
	{
		for(EProject project : _orderedEProjects)
			if(project.getIProject().getName().equalsIgnoreCase(name))
				return project;
		return null;
	}
	
	public ESchema getESchema(String name)
	{
		for(ESchema schema : _orderedESchemas)
			if(schema.getName().equalsIgnoreCase(name))
				return schema;
		return null;
	}
	
	public EProject getProject()
	{
		return _project;
	}
	
	public ESchema getSchema() {
		return _schema;
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
	
	private boolean validatePage()
	{
		
		// Comprobar que exista el proyecto
		if(_project==null)
		{
			setErrorMessage(Messages.bind("DataModelerDiagramWizardPage.notexistproject"));
			return false;
		}
		
		// Comprobar que exista el esquema
		if(_schema==null)
		{
			setErrorMessage(Messages.bind("DataModelerDiagramWizardPage.notexistschema"));
			return false;
		}
		
		// Validar nombre diagrama
		if(_diagramNameField != null && _diagramNameField.getText().length()<1)
		{
			setErrorMessage(Messages.bind("DataModelerDiagramWizardPage.mustsetname"));
			return false;
		}
		else if(_diagramNameField != null)
		{
			IStatus result = _diagramNameValidator.validate(_diagramNameField.getText(), "name");
			if(result!=null && !result.isOK())
			{
				setErrorMessage(result.getMessage());
				return false;
			}
		}
		// Comprobamos que no exista otro diagrama con igual nombre
		for(EObject object:_schema.eResource().getContents())
		{
			if(object instanceof Diagram)
			{
				if(_diagramNameField != null && ((Diagram)object).getName().equalsIgnoreCase(_diagramNameField.getText()))
				{
					setErrorMessage(Messages.bind("DataModelerDiagramWizardPage.already.exist.diagram"));
					return false;
				}
			}
		}
		
		
		
		
		setErrorMessage(null);
		return true;
	}
}
