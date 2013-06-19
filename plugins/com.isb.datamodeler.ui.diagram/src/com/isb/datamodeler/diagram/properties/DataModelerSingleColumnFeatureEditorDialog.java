package com.isb.datamodeler.diagram.properties;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class DataModelerSingleColumnFeatureEditorDialog extends Dialog
{
	protected ILabelProvider labelProvider;
	protected IContentProvider contentProvider;
	protected Object object;
	protected String displayName;
	protected ItemProvider values;
	protected EList<?> result;


	public DataModelerSingleColumnFeatureEditorDialog(Shell parent,
			ILabelProvider labelProvider,
			Object object, 
			List<?> currentValues,
		    String displayName) {
		
		super(parent);
		
	    setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	    this.labelProvider = labelProvider;
	    this.object = object;
	    this.displayName = displayName;
	    
	    AdapterFactory adapterFactory = new ComposedAdapterFactory(Collections.<AdapterFactory>emptyList());
	    values = new ItemProvider(adapterFactory, currentValues);
	    contentProvider = new AdapterFactoryContentProvider(adapterFactory);
	}
	
	  @Override
	  protected void configureShell(Shell shell) 
	  {
	    super.configureShell(shell);
	    shell.setText
	      (EMFEditUIPlugin.INSTANCE.getString
	         ("_UI_FeatureEditorDialog_title", new Object [] { displayName, labelProvider.getText(object) }));
	    shell.setImage(labelProvider.getImage(object));
	  }
	
	  @Override
	  protected Control createDialogArea(Composite parent) 
	  {
			Composite contents = (Composite)super.createDialogArea(parent);
		  
		    GridLayout contentsGridLayout = (GridLayout)contents.getLayout();
		    contentsGridLayout.numColumns = 2;

		    GridData contentsGridData = (GridData)contents.getLayoutData();
		    contentsGridData.horizontalAlignment = SWT.FILL;
		    contentsGridData.verticalAlignment = SWT.FILL;

		    Composite controlButtons = new Composite(contents, SWT.NONE);
		    GridData controlButtonsGridData = new GridData();
		    controlButtonsGridData.verticalAlignment = SWT.FILL;
		    controlButtonsGridData.horizontalAlignment = SWT.FILL;
		    controlButtons.setLayoutData(controlButtonsGridData);

		    GridLayout controlsButtonGridLayout = new GridLayout();
		    controlButtons.setLayout(controlsButtonGridLayout);

		    final Button upButton = new Button(controlButtons, SWT.PUSH);
		    upButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Up_label"));
		    GridData upButtonGridData = new GridData();
		    upButtonGridData.verticalAlignment = SWT.FILL;
		    upButtonGridData.horizontalAlignment = SWT.FILL;
		    upButton.setLayoutData(upButtonGridData);

		    final Button downButton = new Button(controlButtons, SWT.PUSH);
		    downButton.setText(EMFEditUIPlugin.INSTANCE.getString("_UI_Down_label"));
		    GridData downButtonGridData = new GridData();
		    downButtonGridData.verticalAlignment = SWT.FILL;
		    downButtonGridData.horizontalAlignment = SWT.FILL;
		    downButton.setLayoutData(downButtonGridData);

		    Composite featureComposite = new Composite(contents, SWT.NONE);
		    {
		      GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		      data.horizontalAlignment = SWT.END;
		      featureComposite.setLayoutData(data);

		      GridLayout layout = new GridLayout();
		      data.horizontalAlignment = SWT.FILL;
		      layout.marginHeight = 0;
		      layout.marginWidth = 0;
		      layout.numColumns = 1;
		      featureComposite.setLayout(layout);
		    }

		    final Table featureTable = new Table(featureComposite, SWT.MULTI | SWT.BORDER);
		    GridData featureTableGridData = new GridData();
		    featureTableGridData.widthHint = Display.getCurrent().getBounds().width / 5;
		    featureTableGridData.heightHint = Display.getCurrent().getBounds().height / 3;
		    featureTableGridData.verticalAlignment = SWT.FILL;
		    featureTableGridData.horizontalAlignment = SWT.FILL;
		    featureTableGridData.grabExcessHorizontalSpace= true;
		    featureTableGridData.grabExcessVerticalSpace= true;
		    featureTable.setLayoutData(featureTableGridData);

		    final TableViewer featureTableViewer = new TableViewer(featureTable);
		    featureTableViewer.setContentProvider(contentProvider);
		    featureTableViewer.setLabelProvider(labelProvider);
		    featureTableViewer.setInput(values);
		    if (!values.getChildren().isEmpty())
		    {
		      featureTableViewer.setSelection(new StructuredSelection(values.getChildren().get(0)));
		    }
		    
		    upButton.addSelectionListener(
		      new SelectionAdapter()
		      {
		        @Override
		        public void widgetSelected(SelectionEvent event)
		        {
		          IStructuredSelection selection = (IStructuredSelection)featureTableViewer.getSelection();
		          int minIndex = 0;
		          for (Iterator<?> i = selection.iterator(); i.hasNext();)
		          {
		            Object value = i.next();
		            int index = values.getChildren().indexOf(value);
		            values.getChildren().move(Math.max(index - 1, minIndex++), value);
		          }
		        }
		      });

		    downButton.addSelectionListener(
		      new SelectionAdapter()
		      {
		        @Override
		        public void widgetSelected(SelectionEvent event)
		        {
		          IStructuredSelection selection = (IStructuredSelection)featureTableViewer.getSelection();
		          int maxIndex = values.getChildren().size() - selection.size();
		          for (Iterator<?> i = selection.iterator(); i.hasNext();)
		          {
		            Object value = i.next();
		            int index = values.getChildren().indexOf(value);
		            values.getChildren().move(Math.min(index + 1, maxIndex++), value);
		          }
		        }
		      });
		        
		    return contents;
	  }
	  
	  @Override
	  protected void okPressed()
	  {
	    result = new BasicEList<Object>(values.getChildren());
	    super.okPressed();
	  }

	  @Override
	  public boolean close()
	  {
	    contentProvider.dispose();
	    return super.close();
	  }

	  public EList<?> getResult()
	  {
	    return result;
	  }
	
}

