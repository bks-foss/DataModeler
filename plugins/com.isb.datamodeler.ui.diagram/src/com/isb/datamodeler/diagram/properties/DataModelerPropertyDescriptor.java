package com.isb.datamodeler.diagram.properties;

import java.util.List;

import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.model.core.validation.db.AbstractDataModelerDBValidator;
import com.isb.datamodeler.model.core.validation.db.DataModelerValidationDBManager;
import com.isb.datamodeler.model.validation.UtilsValidations;
import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.tables.ETablesPackage;
import com.isb.datamodeler.ui.diagram.Messages;

public class DataModelerPropertyDescriptor extends PropertyDescriptor {

	boolean _isShortCut;
	public DataModelerPropertyDescriptor(Object object,
			IItemPropertyDescriptor itemPropertyDescriptor , boolean isShortCut) {
		super(object, itemPropertyDescriptor);
		_isShortCut = isShortCut;
	}
	private class DescriptionValidator implements IInputValidator
	{
		private String _featureName;
		public DescriptionValidator(String featureName)
		{
			super();
			_featureName = featureName;
		}
		@Override
		public String isValid(String newText) {
			String dataBaseId = UtilsValidations.getDataBaseId((EObject)object);
			List<AbstractDataModelerDBValidator> validators = 
				DataModelerValidationDBManager.getInstance().getValidatorsForEObjectAsList(
						dataBaseId, (ESQLObject)object);
			for(AbstractDataModelerDBValidator validator:validators)
			{
				BasicDiagnostic basicDiagnostic = 
					validator.validate((ESQLObject)object, _featureName, newText);
				if(basicDiagnostic!= null)
				{
					return basicDiagnostic.getMessage();
				}
			}
			return null;
			
		}
		
	}
	private class MultiLineInputDialog extends InputDialog
	{

		public MultiLineInputDialog(Shell parentShell, String dialogTitle,
				String dialogMessage, String initialValue,
				IInputValidator validator) {
			super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
			
		}
		@Override
		protected int getInputTextStyle() {
			return SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER;
		}
		@Override
		protected Control createDialogArea(Composite parent) {
			// TODO Auto-generated method stub
			Control control =  super.createDialogArea(parent);
			Text text = getText();
			GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
			data.heightHint = 5 * text.getLineHeight();
			data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.ENTRY_FIELD_WIDTH);
			text.setLayoutData(data);
			return control;
	      
		}
	}

	@Override
	public CellEditor createPropertyEditor(Composite composite) {
		
		if(_isShortCut)
			return null;
	    if (!itemPropertyDescriptor.canSetProperty(object))
	    {
	      return null;
	    }
		
	    CellEditor result = null;
	    
        Object genericFeature = itemPropertyDescriptor.getFeature(object);
        
	    // Para el caso de la descripcion de una columna
	    // creamos un editor particular:
        if (genericFeature==ETablesPackage.Literals.COLUMN__DESCRIPTION || genericFeature==ETablesPackage.Literals.TABLE__DESCRIPTION)
        {
        	if(itemPropertyDescriptor.isMultiLine(object))
        	{
        		result = createDescriptorPropertyEditor(composite , ((EAttribute)genericFeature).getName());
        		return result;
            }
        }
	    //Para el caso de las columnas de una tabla
	    //en que no hay opciones a agregar y solo nos interesa editar el orden de las columnas
	    //creamos un editor particular:
        //else if (genericFeature==ETablesPackage.Literals.TABLE__COLUMNS)
        if (genericFeature==ETablesPackage.Literals.TABLE__COLUMNS)
        {
            if (itemPropertyDescriptor.isMany(object))
            {
                final ILabelProvider editLabelProvider = getEditLabelProvider();
                result =
                  new ExtendedDialogCellEditor(composite, editLabelProvider)
                  {
                    @Override
                    protected Object openDialogBox(Control cellEditorWindow)
                    {
                    	DataModelerSingleColumnFeatureEditorDialog dialog = new DataModelerSingleColumnFeatureEditorDialog(
                        cellEditorWindow.getShell(),
                        editLabelProvider,
                        object,
                        (List<?>)doGetValue(),
                        getDisplayName());
                      dialog.open();
                      return dialog.getResult();
                    }
                  };
            }
            
        }
        else if ((genericFeature==EConstraintsPackage.Literals.FOREIGN_KEY__UNIQUE_CONSTRAINT))
        {
        	result = super.createPropertyEditor(composite);
        	result.setValidator(new ICellEditorValidator() {
				
				@Override
				public String isValid(Object value) {
					if(value == null)
						return Messages.bind("DataModelerPropertyDescriptor.invalid.uk");
					return null;
				}
			});
        }
        else result = super.createPropertyEditor(composite);


	    return result;
	}
	
	public CellEditor createDescriptorPropertyEditor(Composite composite , String featureName) 
	{
		CellEditor result = null;

		Object genericFeature = itemPropertyDescriptor.getFeature(object);

		if (genericFeature instanceof EStructuralFeature)
		{
			final EStructuralFeature feature = (EStructuralFeature)genericFeature;
			final EClassifier eType = feature.getEType();

			if (eType instanceof EDataType)
			{
				EDataType eDataType = (EDataType)eType;
				result = createDescriptorCellEditor(eDataType, composite , featureName);
			}
		}

		return result;
	}
	
	protected CellEditor createDescriptorCellEditor(final EDataType eDataType, Composite composite , final String featureName)
	{
		if (itemPropertyDescriptor.isMultiLine(object))
		{
			return new ExtendedDialogCellEditor(composite, getEditLabelProvider())
			{
				protected DescriptionValidator validator = new DescriptionValidator(featureName);

				@Override
				protected Object openDialogBox(Control cellEditorWindow)
				{

					InputDialog dialog = new MultiLineInputDialog(
							cellEditorWindow.getShell(),
							EMFEditUIPlugin.INSTANCE.getString
							("_UI_FeatureEditorDialog_title", new Object [] { getDisplayName(), getEditLabelProvider().getText(object) }), 
							EMFEditUIPlugin.INSTANCE.getString("_UI_MultiLineInputDialog_message"), 
							(String)getValue(), validator);

					return dialog.open() == Window.OK ? dialog.getValue() : null;
					
				}
			};
		}
		return new EDataTypeCellEditor(eDataType, composite);
	}
	  
	  
} 