package com.isb.datamodeler.diagram.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PatternFilter;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.schema.EDataModelerNamedElement;
import com.isb.datamodeler.schema.provider.DatamodelerEditPlugin;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.tables.ETablesPackage;
import com.isb.datamodeler.ui.diagram.Messages;

//public class DataModelerForeignKeyEditorDialog extends FeatureEditorDialog{
public class DataModelerForeignKeyEditorDialog extends Dialog{

	static final Color disabledColor = new Color(null,245,245,245);
	
	protected ILabelProvider labelProvider;
	protected ILabelProvider referencedMembersLabelProvider;

	protected IContentProvider columnsContentProvider;
	protected IContentProvider membersContentProvider;
	protected IContentProvider referencedMembersContentProvider;
	
	protected EForeignKey foreignKey;
	protected ETable baseTable;
	protected EUniqueConstraint uniqueConstraint;
	
	protected EClassifier eClassifier;
	
	protected ItemProvider columns;
	protected ItemProvider members;
	protected ItemProvider referencedMembers;

	protected EList<?> result;
	
	protected boolean multiLine = false;
	
	protected CLabel errorLabel;
	
	public DataModelerForeignKeyEditorDialog(Shell parent, EForeignKey foreignKey) {
			
		super(parent);
		
		this.foreignKey = foreignKey;
		this.baseTable = foreignKey.getBaseTable();
		this.uniqueConstraint = foreignKey.getUniqueConstraint();
		 
		this.eClassifier = ETablesPackage.eINSTANCE.getColumn();

		AdapterFactory columnsAdapterFactory = new ComposedAdapterFactory(Collections.<AdapterFactory>emptyList());
		AdapterFactory membersAdapterFactory = new ComposedAdapterFactory(Collections.<AdapterFactory>emptyList());
		AdapterFactory referencedMembersAdapterFactory = new ComposedAdapterFactory(Collections.<AdapterFactory>emptyList());
		
		columns = new ItemProvider(columnsAdapterFactory, foreignKey.getBaseTable().getColumns());
		members = new ItemProvider(membersAdapterFactory, foreignKey.getMembers());
		referencedMembers = new ItemProvider(referencedMembersAdapterFactory, foreignKey.getUniqueConstraint().getMembers());
		
		columnsContentProvider = new AdapterFactoryContentProvider(columnsAdapterFactory);
		membersContentProvider = new AdapterFactoryContentProvider(membersAdapterFactory);
		referencedMembersContentProvider = new AdapterFactoryContentProvider(referencedMembersAdapterFactory);
		
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);

		this.labelProvider = new LabelProvider()
		{
			DatamodelerDiagramEditorPlugin datamodelerPlugin = DatamodelerDiagramEditorPlugin.getInstance();
			@Override
			public String getText(Object object)
			{
				String text = datamodelerPlugin.getItemLabel(object);
				if(text.indexOf("[")>0)
					text = text.substring(0, text.indexOf("["));
				return text;
			}
			@Override
			public Image getImage(Object object)
			{
				Image img = datamodelerPlugin.getImage(object);
				if(object instanceof EColumn)
				{
					if(!baseTable.getColumns().contains((EColumn)object))
					{	
					  List<Object> images = new ArrayList<Object>(2);
				      images.add(img);
				      images.add(datamodelerPlugin.getBundledImage("icons/copy.gif"));
				      img = ExtendedImageRegistry.getInstance().getImage(new ComposedImage(images));
					}	
				}
				return img;
			}
		};
		
		this.referencedMembersLabelProvider = new LabelProvider()
		{
			DatamodelerDiagramEditorPlugin datamodelerPlugin = DatamodelerDiagramEditorPlugin.getInstance();
			@Override
			public String getText(Object object)
			{
				String text = datamodelerPlugin.getItemLabel(object);
				if(text.indexOf("[")>0)
					text = text.substring(0, text.indexOf("["));
				return text;
			}
			@Override
			public Image getImage(Object object)
			{
				return datamodelerPlugin.getImage(object);
			}
		};
		
		if (!columns.getChildren().isEmpty())
		{
			ExtendedComboBoxCellEditor.createItems(columns.getChildren(), labelProvider, true);
		}

	}

	@Override
	protected void configureShell(Shell shell) 
	{
		super.configureShell(shell);
		shell.setText(DatamodelerEditPlugin.INSTANCE.getString("_UI_ForeignKey_type")+" : "+labelProvider.getText(foreignKey));
		shell.setImage(labelProvider.getImage(foreignKey));
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite contents = (Composite)super.createDialogArea(parent);

	    GridLayout contentsGridLayout = (GridLayout)contents.getLayout();
	    contentsGridLayout.horizontalSpacing = 0;
	    contentsGridLayout.numColumns = 5;

	    GridData contentsGridData = (GridData)contents.getLayoutData();
	    contentsGridData.horizontalAlignment = SWT.FILL;
	    contentsGridData.verticalAlignment = SWT.FILL;

	    Text patternText = null;

//	    if (!columns.getChildren().isEmpty()) 
//	    {
	      Group filterGroupComposite = new Group(contents, SWT.NONE);

	      filterGroupComposite.setText(Messages.bind("DataModelerForeignKeyEditorDialog_UI_Columns_pattern_group"));
	      filterGroupComposite.setLayout(new GridLayout(5, false));
	      filterGroupComposite.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 5, 1));
	      
	      Label label = new Label(filterGroupComposite, SWT.NONE);
	      label.setText(Messages.bind("DataModelerForeignKeyEditorDialog_UI_Columns_pattern_label"));

	      patternText = new Text(filterGroupComposite, SWT.BORDER);
	      patternText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//	    }

	    // COLUMNS
	    Composite columnsComposite = new Composite(contents, SWT.NONE);
	    {
	      GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
	      data.horizontalAlignment = SWT.END;
	      columnsComposite.setLayoutData(data);

	      GridLayout layout = new GridLayout();
	      data.horizontalAlignment = SWT.FILL;
	      layout.marginHeight = 0;
	      layout.marginWidth = 0;
	      layout.numColumns = 1;
	      columnsComposite.setLayout(layout);
	    }

	    CLabel columnsLabel = new CLabel(columnsComposite, SWT.NONE);

	    columnsLabel.setImage(labelProvider.getImage(baseTable));
	    columnsLabel.setText(labelProvider.getText(baseTable));
	    
	    GridData columnsLabelGridData = new GridData();
	    columnsLabelGridData.verticalAlignment = SWT.FILL;
	    columnsLabelGridData.horizontalAlignment = SWT.FILL;
	    columnsLabel.setLayoutData(columnsLabelGridData);

	    final Table columnsTable = columns.getChildren() == null ? null : new Table(columnsComposite, SWT.MULTI | SWT.BORDER);
	    if (columnsTable != null)
	    {
	      GridData columnsTableGridData = new GridData();
	      columnsTableGridData.widthHint = Display.getCurrent().getBounds().width / 9;
	      columnsTableGridData.heightHint = Display.getCurrent().getBounds().height / 4;
	      columnsTableGridData.verticalAlignment = SWT.FILL;
	      columnsTableGridData.horizontalAlignment = SWT.FILL;
	      columnsTableGridData.grabExcessHorizontalSpace= true;
	      columnsTableGridData.grabExcessVerticalSpace= true;
	      columnsTable.setLayoutData(columnsTableGridData);
	    }

	    final TableViewer columnsTableViewer = columns.getChildren() == null ? null : new TableViewer(columnsTable);
	    if (columnsTableViewer != null)
	    {
	    	columnsTableViewer.setContentProvider(new AdapterFactoryContentProvider(new AdapterFactoryImpl()));
	    	columnsTableViewer.setLabelProvider(labelProvider);
	      final PatternFilter filter =
	        new PatternFilter()
	        {
	          @Override
	          protected boolean isParentMatch(Viewer viewer, Object element)
	          {
	            return viewer instanceof AbstractTreeViewer && super.isParentMatch(viewer, element);
	          }
	        };
	        columnsTableViewer.addFilter(filter);
	      assert patternText != null;
	      patternText.addModifyListener
	        (new ModifyListener()
	         {
	           public void modifyText(ModifyEvent e)
	           {
	             filter.setPattern(((Text)e.widget).getText());	            
	             columnsTableViewer.refresh();
	           }
	         });
	       
	      columnsTableViewer.setContentProvider(columnsContentProvider);
	      columnsTableViewer.setLabelProvider(labelProvider);
	      columnsTableViewer.setInput(columns);
	    }

	    // We use multi even for a single line because we want to respond to the enter key.
	    //
	    int style = multiLine ?
	      SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER :
	      SWT.MULTI | SWT.BORDER;
	    final Text columnsText = columns.getChildren() == null ? new Text(columnsComposite, style) : null;
	    if (columnsText != null)
	    {
	      GridData columnsTextGridData = new GridData();
	      columnsTextGridData.widthHint = Display.getCurrent().getBounds().width / 9;
	      columnsTextGridData.verticalAlignment = SWT.BEGINNING;
	      columnsTextGridData.horizontalAlignment = SWT.FILL;
	      columnsTextGridData.grabExcessHorizontalSpace = true;
	      if (multiLine)
	      {
	        columnsTextGridData.verticalAlignment = SWT.FILL;
	        columnsTextGridData.grabExcessVerticalSpace = true;
	      }
	      columnsText.setLayoutData(columnsTextGridData);
	    }

	    // BUTTONS
	    Composite controlButtons = new Composite(contents, SWT.NONE);
	    GridData controlButtonsGridData = new GridData();
	    controlButtonsGridData.verticalAlignment = SWT.FILL;
	    controlButtonsGridData.horizontalAlignment = SWT.FILL;
	    controlButtons.setLayoutData(controlButtonsGridData);

	    GridLayout controlsButtonGridLayout = new GridLayout();
	    controlButtons.setLayout(controlsButtonGridLayout);

	    new Label(controlButtons, SWT.NONE);
	    
	    // ADD BUTTON
	    final Button addButton = new Button(controlButtons, SWT.PUSH);
	    addButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Add_label"));
	    GridData addButtonGridData = new GridData();
	    addButtonGridData.verticalAlignment = SWT.FILL;
	    addButtonGridData.horizontalAlignment = SWT.FILL;
	    addButton.setLayoutData(addButtonGridData);

	    // REMOVE BUTTON
	    final Button removeButton = new Button(controlButtons, SWT.PUSH);
	    removeButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Remove_label"));
	    GridData removeButtonGridData = new GridData();
	    removeButtonGridData.verticalAlignment = SWT.FILL;
	    removeButtonGridData.horizontalAlignment = SWT.FILL;
	    removeButton.setLayoutData(removeButtonGridData);
	    
	    Label spaceLabel = new Label(controlButtons, SWT.NONE);
	    GridData spaceLabelGridData = new GridData();
	    spaceLabelGridData.verticalSpan = 2;
	    spaceLabel.setLayoutData(spaceLabelGridData);
	    
	    // UP BUTTON
	    final Button upButton = new Button(controlButtons, SWT.PUSH);
	    upButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Up_label"));
	    GridData upButtonGridData = new GridData();
	    upButtonGridData.verticalAlignment = SWT.FILL;
	    upButtonGridData.horizontalAlignment = SWT.FILL;
	    upButton.setLayoutData(upButtonGridData);

	    // DOWN BUTTON
	    final Button downButton = new Button(controlButtons, SWT.PUSH);
	    downButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Down_label"));
	    GridData downButtonGridData = new GridData();
	    downButtonGridData.verticalAlignment = SWT.FILL;
	    downButtonGridData.horizontalAlignment = SWT.FILL;
	    downButton.setLayoutData(downButtonGridData);

	    // MEMBERS
	    Composite membersComposite = new Composite(contents, SWT.NONE);
	    {
	      GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
	      data.horizontalAlignment = SWT.END;
	      membersComposite.setLayoutData(data);

	      GridLayout layout = new GridLayout();
	      data.horizontalAlignment = SWT.FILL;
	      layout.marginHeight = 0;
	      layout.marginWidth = 0;
	      layout.numColumns = 1;
	      membersComposite.setLayout(layout);
	    }

	    // Members label
	    CLabel membersLabel = new CLabel(membersComposite, SWT.NONE);
	    Object image = null;
	    
		if(foreignKey.isIdentifying())
			image =  DatamodelerEditPlugin.INSTANCE.getImage("full/obj16/Column_pkfk");
		else
			image = DatamodelerEditPlugin.INSTANCE.getImage("full/obj16/Column_fk");

	    membersLabel.setImage(ExtendedImageRegistry.getInstance().getImage(image));
	    membersLabel.setText(DatamodelerEditPlugin.INSTANCE.getString("_UI_ReferenceConstraint_members_feature"));
	    
	    GridData membersLabelGridData = new GridData();
	    membersLabelGridData.horizontalSpan = 1;
	    membersLabelGridData.horizontalAlignment = SWT.FILL;
	    membersLabelGridData.verticalAlignment = SWT.FILL;
	    membersLabel.setLayoutData(membersLabelGridData);
	    
	    final Table membersTable = new Table(membersComposite, SWT.MULTI | SWT.BORDER);
	    GridData membersTableGridData = new GridData();
	    membersTableGridData.widthHint = Display.getCurrent().getBounds().width / 9;
	    membersTableGridData.heightHint = Display.getCurrent().getBounds().height / 4;
	    membersTableGridData.verticalAlignment = SWT.FILL;
	    membersTableGridData.horizontalAlignment = SWT.FILL;
	    membersTableGridData.grabExcessHorizontalSpace= true;
	    membersTableGridData.grabExcessVerticalSpace= true;
	    membersTable.setLayoutData(membersTableGridData);

	    final TableViewer membersTableViewer = new TableViewer(membersTable);
	    membersTableViewer.setContentProvider(membersContentProvider);
	    membersTableViewer.setLabelProvider(labelProvider);
	    membersTableViewer.setInput(members);
	    if (!members.getChildren().isEmpty())
	    {
	    	membersTableViewer.setSelection(new StructuredSelection(members.getChildren().get(0)));
	    }
	    
	    // si se ha creado la tabla de columnas escuchamos el doble click para las columnas y los miembros
	    if (columnsTableViewer != null)
	    {
	    	columnsTableViewer.addDoubleClickListener(new IDoubleClickListener()
	    	{
	    		public void doubleClick(DoubleClickEvent event)
	    		{
	    			if (addButton.isEnabled())
	    			{
	    				addButton.notifyListeners(SWT.Selection, null);
	    			}
	    		}
	    	});

	    	membersTableViewer.addDoubleClickListener(new IDoubleClickListener()
	    	{
	    		public void doubleClick(DoubleClickEvent event)
	    		{
	    			if (removeButton.isEnabled())
	    			{
	    				removeButton.notifyListeners(SWT.Selection, null);
	    			}
	    		}
	    	});
	    }
	    
	    // Si existen columnas en la tabla
	    if (columnsText != null)
	    {
	    	columnsText.addKeyListener(
	        new KeyAdapter()
	        {
	          @Override
	          public void keyPressed(KeyEvent event)
	          {
	            if (!multiLine && (event.character == '\r' || event.character == '\n'))
	            {
	              try
	              {
	                Object value = EcoreUtil.createFromString((EDataType)eClassifier, columnsText.getText());
	                members.getChildren().add(value);
	                columnsText.setText("");
	                membersTableViewer.setSelection(new StructuredSelection(value));
	                event.doit = false;
	              }
	              catch (RuntimeException exception)
	              {
	                // Ignore
	              }
	            }
	            else if (event.character == '\33')
	            {
	              columnsText.setText("");
	              event.doit = false;
	            }
	          }
	        });
	    }
	    	    
	    // Listener del botón Up
	    upButton.addSelectionListener(
	    		new SelectionAdapter()
	    		{
	    			@Override
	    			public void widgetSelected(SelectionEvent event)
	    			{
	    				IStructuredSelection selection = (IStructuredSelection)membersTableViewer.getSelection();
	    				int minIndex = 0;
	    				for (Iterator<?> i = selection.iterator(); i.hasNext();)
	    				{
	    					Object value = i.next();
	    					int index = members.getChildren().indexOf(value);
	    					members.getChildren().move(Math.max(index - 1, minIndex++), value);
	    				}
	    				
	    				checkErrors();
	    			}
	    		});

	    // Listener del botón down
	    downButton.addSelectionListener(
	    		new SelectionAdapter()
	    		{
	    			@Override
	    			public void widgetSelected(SelectionEvent event)
	    			{
	    				IStructuredSelection selection = (IStructuredSelection)membersTableViewer.getSelection();
	    				int maxIndex = members.getChildren().size() - selection.size();
	    				for (Iterator<?> i = selection.iterator(); i.hasNext();)
	    				{
	    					Object value = i.next();
	    					int index = members.getChildren().indexOf(value);
	    					members.getChildren().move(Math.min(index + 1, maxIndex++), value);
	    				}
	    				
	    				checkErrors();
	    			}
	    		});

	    // Listener del botón Add
	    addButton.addSelectionListener(
	    		new SelectionAdapter()
	    		{
	    			// event is null when choiceTableViewer is double clicked
	    			@Override
	    			public void widgetSelected(SelectionEvent event)
	    			{
	    				if (columnsTableViewer != null)
	    				{
	    					IStructuredSelection selection = (IStructuredSelection)columnsTableViewer.getSelection();
	    					for (Iterator<?> i = selection.iterator(); i.hasNext();)
	    					{
	    						Object value = i.next();
	    						if (!members.getChildren().contains(value))
	    						{
	    							members.getChildren().add(value);	                
	    						}
	    					}
	    					membersTableViewer.setSelection(selection);
	    				}
	    				else if (columnsText != null)
	    				{
	    					try
	    					{
	    						Object value = EcoreUtil.createFromString((EDataType)eClassifier, columnsText.getText());
	    						if (!members.getChildren().contains(value))
	    						{
	    							members.getChildren().add(value);	               
	    							columnsText.setText("");
	    						}
	    						membersTableViewer.setSelection(new StructuredSelection(value));
	    					}
	    					catch (RuntimeException exception)
	    					{
	    						// Ignore
	    					}
	    				}
	    				
	    				checkErrors();
	    			}
	    		});

	    // Listener del botón Remove
	    removeButton.addSelectionListener(
	    		new SelectionAdapter()
	    		{
	    			// event is null when featureTableViewer is double clicked 
	    			@Override
	    			public void widgetSelected(SelectionEvent event)
	    			{
	    				IStructuredSelection selection = (IStructuredSelection)membersTableViewer.getSelection();
	    				Object firstValue = null;
	    				for (Iterator<?> i = selection.iterator(); i.hasNext();)
	    				{
	    					Object value = i.next();
	    					if (firstValue == null)
	    					{
	    						firstValue = value;
	    					}
	    					members.getChildren().remove(value);
	    					if(!baseTable.getColumns().contains(value))
	    						columns.getChildren().remove(value);
	    				}

	    				if (!members.getChildren().isEmpty())
	    				{
	    					membersTableViewer.setSelection(new StructuredSelection(members.getChildren().get(0)));
	    				}

	    				if (columnsTableViewer != null)
	    				{
	    					columnsTableViewer.setSelection(selection);
	    				}
	    				else if (columnsText != null)
	    				{
	    					if (firstValue != null)
	    					{
	    						String value = EcoreUtil.convertToString((EDataType)eClassifier, firstValue);
	    						columnsText.setText(value);
	    					}
	    				}
	    				
	    				checkErrors();
	    			}
	    		});   
	  	    
	  	    
	    // COPY_BUTTONS
	    Composite copyButtons = new Composite(contents, SWT.NONE);
	    GridData copyButtonsGridData = new GridData();
	    copyButtonsGridData.verticalAlignment = SWT.FILL;
	    copyButtonsGridData.horizontalAlignment = SWT.FILL;

	    copyButtons.setLayoutData(copyButtonsGridData);

	    GridLayout copyButtonGridLayout = new GridLayout();
	    copyButtonGridLayout.marginWidth = 0;
	    copyButtons.setLayout(copyButtonGridLayout);

	    new Label(copyButtons, SWT.NONE);
	    
	    // COPY BUTTON
	    final Button copyButton = new Button(copyButtons, SWT.PUSH);
	    copyButton.setText("< "+Messages.bind("DataModelerForeignKeyEditorDialog_Copy"));
	    GridData copyButtonGridData = new GridData();
	    copyButtonGridData.verticalAlignment = SWT.FILL;
	    copyButtonGridData.horizontalAlignment = SWT.CENTER;
	    copyButtonGridData.widthHint = 95;
	    copyButton.setLayoutData(copyButtonGridData);
	    
	    // COPY ALL BUTTON
	    final Button copyAllButton = new Button(copyButtons, SWT.PUSH);
	    copyAllButton.setText("<< "+Messages.bind("DataModelerForeignKeyEditorDialog_Copy_All"));
	    copyAllButton.setSize(300, 300);
	    GridData copyAllButtonGridData = new GridData();
	    copyAllButtonGridData.verticalAlignment = SWT.FILL;
	    copyAllButtonGridData.horizontalAlignment = SWT.CENTER;
	    copyAllButtonGridData.widthHint = 95;
	    copyAllButton.setLayoutData(copyAllButtonGridData);
	    
	    new Label(copyButtons, SWT.NONE);
	    
	    // ForeignKey Label
	    CLabel labelFK = new CLabel(copyButtons, SWT.NONE);
	    labelFK.setAlignment(SWT.RIGHT);
	   
		labelFK.setImage(labelProvider.getImage(foreignKey));
		labelFK.setFont(new Font(contents.getDisplay(), "Arial", 8, SWT.BOLD));
	    labelFK.setText(labelProvider.getText(foreignKey));
	    GridData labelFKGridData = new GridData();
	    labelFKGridData.verticalAlignment = SWT.FILL;
	    labelFKGridData.horizontalAlignment = SWT.CENTER;
	    labelFK.setLayoutData(labelFKGridData);
		
	    // Link Label
	    CLabel link = new CLabel(copyButtons, SWT.NONE);
	    link.setText("                                           ");
	    if(foreignKey.isIdentifying())
	    	link.setBackground(DatamodelerDiagramEditorPlugin.getInstance().getBundledImage("icons/identifying.gif"));
	    else
	    	link.setBackground(DatamodelerDiagramEditorPlugin.getInstance().getBundledImage("icons/nonIdentifying.gif"));
	    
	    new Label(copyButtons, SWT.NONE);
	    
	    // REFERENCED_MEMBERS
	    Composite referencedMembersComposite = new Composite(contents, SWT.NONE);
	    {
	      GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
	      data.horizontalAlignment = SWT.END;
	      referencedMembersComposite.setLayoutData(data);

	      GridLayout layout = new GridLayout();
	      data.horizontalAlignment = SWT.FILL;
	      layout.marginHeight = 0;
	      layout.marginWidth = 0;
	      layout.numColumns = 1;
	      referencedMembersComposite.setLayout(layout);
	    }
	    
	    CLabel referencedMembersLabel = new CLabel(referencedMembersComposite, SWT.NONE);

	    referencedMembersLabel.setImage(labelProvider.getImage(uniqueConstraint));
	    referencedMembersLabel.setText(labelProvider.getText(uniqueConstraint));
	    
	    GridData referencedMembersLabelGridData = new GridData();
	    referencedMembersLabelGridData.horizontalSpan = 1;
	    referencedMembersLabelGridData.horizontalAlignment = SWT.FILL;
	    referencedMembersLabelGridData.verticalAlignment = SWT.FILL;
	    referencedMembersLabel.setLayoutData(referencedMembersLabelGridData);
	    
	    final Table referencedMembersTable = new Table(referencedMembersComposite, SWT.MULTI | SWT.BORDER);
	    
	    referencedMembersTable.setBackground(disabledColor);
	    
	    GridData referencedMembersTableGridData = new GridData();

	    referencedMembersTableGridData.widthHint = Display.getCurrent().getBounds().width / 9;
	    referencedMembersTableGridData.heightHint = Display.getCurrent().getBounds().height / 4;
	    referencedMembersTableGridData.verticalAlignment = SWT.FILL;
	    referencedMembersTableGridData.horizontalAlignment = SWT.FILL;
	    referencedMembersTableGridData.grabExcessHorizontalSpace= true;
	    referencedMembersTableGridData.grabExcessVerticalSpace= true;
	    referencedMembersTable.setLayoutData(referencedMembersTableGridData);
	    
	    final TableViewer referencedMembersTableViewer = new TableViewer(referencedMembersTable);
	    referencedMembersTableViewer.setContentProvider(referencedMembersContentProvider);
	    referencedMembersTableViewer.setLabelProvider(referencedMembersLabelProvider);
	    referencedMembersTableViewer.setInput(referencedMembers);
	    
	    if (!referencedMembers.getChildren().isEmpty())
	    {
	      referencedMembersTableViewer.setSelection(new StructuredSelection(referencedMembers.getChildren().get(0)));
	    }
	    
	    if (referencedMembersTableViewer != null)
	    {
	    	referencedMembersTableViewer.addDoubleClickListener(new IDoubleClickListener()
	    	{
	    		public void doubleClick(DoubleClickEvent event)
	    		{
	    			if (copyButton.isEnabled())
	    			{
	    				copyButton.notifyListeners(SWT.Selection, null);
	    			}
	    		}
	    	});
	    }
	    
	    // Listener del botón Copy
	    copyButton.addSelectionListener(
	    		new SelectionAdapter()
	    		{
	    			// event is null when choiceTableViewer is double clicked
	    			@Override
	    			public void widgetSelected(SelectionEvent event)
	    			{
	    				if (referencedMembersTableViewer != null)
	    				{
	    					IStructuredSelection selection = (IStructuredSelection)referencedMembersTableViewer.getSelection();

	    					copyFromReferencedMembers(selection);
	    					
	    					membersTableViewer.setSelection(selection);
	    					
	    					checkErrors();
	    				}
	    			}
	    		});

	    // Listener del botón Copy All
	    copyAllButton.addSelectionListener(
	    		new SelectionAdapter()
	    		{
	    			// event is null when choiceTableViewer is double clicked
	    			@Override
	    			public void widgetSelected(SelectionEvent event)
	    			{
	    				if (referencedMembersTableViewer != null)
	    				{
	    					referencedMembersTableViewer.setSelection(new StructuredSelection(referencedMembers.getChildren()));
	    					IStructuredSelection selection = (IStructuredSelection)referencedMembersTableViewer.getSelection();
	    					
	    					copyFromReferencedMembers(selection);
	    					
	    					membersTableViewer.setSelection(selection);
	    					
	    					checkErrors();
	    				}
	    			}
	    		}); 
	        

	    Group errorGroupComposite = new Group(contents, SWT.NONE);

	    errorGroupComposite.setLayout(new GridLayout(5, false));
	    errorGroupComposite.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 5, 1));

	    errorLabel = new CLabel(errorGroupComposite, SWT.NONE);
	    errorLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	    return contents;

	}

	private void checkErrors()
	{
		boolean hasErrors = false;
		// El número de columnas miembro y referenciadas debe coincidir
		if(members.getChildren().size() != referencedMembers.getChildren().size())
		{
			errorLabel.setText(Messages.bind("DataModelerForeignKeyEditorDialog_UI_num_members_FK_validation"));
			hasErrors = true;
		}
		else
		{
			for(int i=0;i<members.getChildren().size();i++)
			{
				EColumn memberColumn = (EColumn)members.getChildren().get(i);
				EColumn referencedMemberColumn = (EColumn)referencedMembers.getChildren().get(i);
				if(memberColumn.getDataType().compareTo(referencedMemberColumn.getDataType()) != 0)
				{
					errorLabel.setText(Messages.bind("DataModelerForeignKeyEditorDialog_UI_members_types_FK_validation",labelProvider.getText(memberColumn)));
					hasErrors = true;
					break;
				}
			}
		}
		
		if(hasErrors)
			errorLabel.setImage(getImage(Dialog.DLG_IMG_MESSAGE_ERROR));
		else
		{
			errorLabel.setText("");
			errorLabel.setImage(null);
		}
		
		getButton(IDialogConstants.OK_ID).setEnabled(!hasErrors);
	
	}
	
	private void copyFromReferencedMembers(IStructuredSelection selection)
	{
		for (Iterator<?> i = selection.iterator(); i.hasNext();)
		{
			EColumn referencedMemberColumn = (EColumn)i.next();
			String referencedMemberColumnName = referencedMemberColumn.getName();
			
			if (!members.getChildren().contains(referencedMemberColumn))
			{
				String newName = getNewDefaultName(columns.getChildren(), referencedMemberColumnName);
				EColumn clonedColumn = referencedMemberColumn.clone(); 
				clonedColumn.setName(newName);
				
				members.getChildren().add(clonedColumn);	
				if (!columns.getChildren().contains(clonedColumn))
				{
					columns.getChildren().add(clonedColumn);
				}
			}
		}
	}
	
	protected Control createButtonBar(Composite parent) {
		Control control = super.createButtonBar(parent);
		
		checkErrors();
		
		return control;
	}
	
	public EList<?> getResult()
	{
		return result;
	}
	
	private String getNewDefaultName(EList<Object> existingChildren, String baseName)
	{
		int i=0;
		
		String newName = baseName;
		
		while(hasChildWithSameName(existingChildren, newName))
			newName = baseName + ++i;
		
		return newName;
	}
	
	private boolean hasChildWithSameName(EList<Object> existingChildren, String name)
	{
		for(Object namedElement:existingChildren)
			if(((EDataModelerNamedElement)namedElement).getName()!=null && 
			((EDataModelerNamedElement)namedElement).getName().equalsIgnoreCase(name))
				return true;
		
		return false;
	}
	
	@Override
	protected void okPressed()
	{
		result = new BasicEList<Object>(members.getChildren());
		super.okPressed();
	}
	  
	public boolean close()
	{
		columnsContentProvider.dispose();
		membersContentProvider.dispose();
		referencedMembersContentProvider.dispose();

		return super.close();
	}
}
