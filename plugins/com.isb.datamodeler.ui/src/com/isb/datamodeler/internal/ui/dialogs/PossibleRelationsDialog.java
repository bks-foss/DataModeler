package com.isb.datamodeler.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.HyperlinkSettings;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.internal.MessageLine;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.internal.ui.views.actions.refactor.DetectPossibleRelationsAction.PossibleRelation;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.ui.project.EProject;

public class PossibleRelationsDialog extends FormDialog {
	
	private MessageLine fStatusLine;
	private IStatus fLastStatus;
	
	private Map<ESchema, Map<EPersistentTable, List<PossibleRelation>>> _schema2table2relations;
	
	private PossibleRelationsSection _section;
	private List<PossibleRelationElement> _tElements = new ArrayList<PossibleRelationElement>();
	private EProject _eProject;

	public PossibleRelationsDialog(EProject eProject , Shell parentShell , Map<ESchema, Map<EPersistentTable, List<PossibleRelation>>> schema2table2relations ) {
		super(parentShell);
		_schema2table2relations = schema2table2relations;
		fLastStatus = new StatusInfo();
		_eProject = eProject;
	
	}
	@Override
	protected void createFormContent(IManagedForm mform) {
		
		for(ESchema schema : _schema2table2relations.keySet())
		{
			createSharedFormContent(mform , schema);
		}
		

		mform.getForm().setBackgroundImage(null);
		mform.getToolkit().decorateFormHeading(mform.getForm().getForm());
		
	}
	public void createSharedFormContent(IManagedForm managedForm , ESchema schema) {
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.getHyperlinkGroup().setHyperlinkUnderlineMode(HyperlinkSettings.UNDERLINE_HOVER);
		form.setText(Messages.bind("PossibleRelationsDialog.form.text"));  //$NON-NLS-1$
		TableWrapLayout layout = new TableWrapLayout();
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		form.getBody().setLayout(layout);

		createFormSection(managedForm, toolkit , schema);
	}
	private void createFormSection(final IManagedForm form, FormToolkit toolkit , ESchema schema) {
		_section = createTransformViewStateSection(schema, form, toolkit, form.getForm().getBody());

		_section.getSection().setActiveToggleColor(
			toolkit.getHyperlinkGroup().getActiveForeground());
		_section.getSection().setToggleColor(
			toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(_section.getSection());

		_section.getSection().addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.getForm().reflow(false);
			}
		});

		_section.getSection().setText(schema.getName()); 
		_section.getSection().setDescription(Messages.bind("PossibleRelationsDialog.section.description", schema.getName()));  //$NON-NLS-1$
		TableWrapData td = new TableWrapData();
		td.align = TableWrapData.FILL;
		td.grabHorizontal = true;
		_section.getSection().setLayoutData(td);
		_tElements.addAll(_section.getTransformElements());
		
	}

	protected PossibleRelationsSection createTransformViewStateSection(ESchema schema , IManagedForm form , FormToolkit toolkit, Composite parent)
	{
		PossibleRelationsSection section = new PossibleRelationsSection(form, parent , this , _eProject);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 120;
		gd.horizontalSpan=2;
		section.getSection().setLayoutData(gd);
		section.setFormInput(schema);
		
		return section;
	}

	public List<PossibleRelationElement> getTransformElements()
	{
		return _tElements;
	}

	public void updateOKButton()
	{
		StatusInfo status = new StatusInfo();
		
		boolean enableButton = false;
		for(PossibleRelationElement te:getTransformElements())
		{
			if(te.isCreate())
			{
				enableButton = true;
			}		
		}
		if(!enableButton)
			status.setInfo(Messages.bind("PossibleRelationsDialog.status.info"));  //$NON-NLS-1$
		else status.setInfo(""); //$NON-NLS-1$
		updateStatus(status);

		Button okButton = getButton(IDialogConstants.OK_ID);
		if(okButton!=null)
			getButton(IDialogConstants.OK_ID).setEnabled(enableButton);
	}
	@Override
	protected Control createContents(Composite parent) {
		
		Control control = super.createContents(parent);

		updateOKButton();
		
		return control;
	}
	@Override
	protected Control createButtonBar(Composite parent) 
	{
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth =
			convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		fStatusLine = new MessageLine(composite);
		fStatusLine.setAlignment(SWT.LEFT);
		fStatusLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fStatusLine.setErrorStatus(null); 

		super.createButtonBar(composite);
		return composite;
	}
	private void updateButtonsEnableState(IStatus status)
	{
		if (getButton(IDialogConstants.OK_ID) != null && !getButton(IDialogConstants.OK_ID).isDisposed())
			getButton(IDialogConstants.OK_ID).setEnabled(!status.matches(IStatus.ERROR));
	}
	private void updateStatus(IStatus status)
	{
		fLastStatus = status;
		if (fStatusLine != null && !fStatusLine.isDisposed())
		{
			updateButtonsEnableState(status);
			fStatusLine.setErrorStatus(status);
		}
	}

	public IStatus getStatus()
	{
		return fLastStatus;
	}
	public Map<ESchema, Map<EPersistentTable, List<PossibleRelation>>> getCache()
	{
		return _schema2table2relations;
	}
}
