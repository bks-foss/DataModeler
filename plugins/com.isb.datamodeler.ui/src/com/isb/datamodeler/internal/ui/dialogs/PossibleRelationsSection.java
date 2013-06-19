package com.isb.datamodeler.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.internal.ui.views.actions.refactor.DetectPossibleRelationsAction.PossibleRelation;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.ui.DataModelerUI;
import com.isb.datamodeler.ui.project.EProject;



public class PossibleRelationsSection extends SectionPart {
	
	private static final int COLUMN_COLUMNS_SUGGESTED_INDEX = 0;
//	private static final int COLUMN_PARENT_TABLE_INDEX = 1;
	private static final int COLUMN_COLUMNS_UK_INDEX = 1;
	private static final int COLUMN_SELECT_INDEX = 2;
	
	private static final String COLUMNS_SUGGESTED = Messages.bind("PossibleRelationsSection.columns_suggested"); //$NON-NLS-1$
	private static final String COLUMNS_SUGGESTED_PROPERTY = "ColumnsSuggestedProperty"; //$NON-NLS-1$
	private static final String SELECT = Messages.bind("PossibleRelationsSection.select"); //$NON-NLS-1$
	private static final String SELECT_PROPERTY = "SelectProperty"; //$NON-NLS-1$
	private static final String COLUMNS_UK = Messages.bind("PossibleRelationsSection.columns.uk"); //$NON-NLS-1$
	private static final String COLUMNS_UK_PROPERTY = "ColumnsUKProperty"; //$NON-NLS-1$
	private static final String PARENT_TABLE = Messages.bind("PossibleRelationsSection.parent.table"); //$NON-NLS-1$
	private static final String PARENT_TABLE_PROPERTY = "ParentTableProperty"; //$NON-NLS-1$
	
	// Imágenes para representar las checkboxes
	private static final Image CHECKED = DataModelerUI.imageDescriptorFromPlugin(
			DataModelerUI.PLUGIN_ID, "icons/full/obj16/checked.gif").createImage(); //$NON-NLS-1$
	private static final Image UNCHECKED = DataModelerUI.imageDescriptorFromPlugin(
			DataModelerUI.PLUGIN_ID, "icons/full/obj16/unchecked.gif").createImage(); //$NON-NLS-1$

	private AdapterFactoryLabelProvider myAdapterFactoryLabelProvider = new AdapterFactoryLabelProvider(
			DatamodelerDiagramEditorPlugin.getInstance()
					.getItemProvidersAdapterFactory());

	
	TreeViewer _viewer = null;
	
//	TreeViewerColumn _targetColumn;
	
	private List<PossibleRelationElement> _tElements;
	private Object _input;
	PossibleRelationsDialog _dialog;
	EProject _eProject;
	
	class TableContentProvider implements	ITreeContentProvider {

		private ESchema _input;
		
		public Object[] getElements(Object inputElement)
		{
			if(_input == null)
				return new Object[0];
			if(inputElement instanceof ESchema)
			{
				ESchema schema = ((ESchema)inputElement);
				Map<EPersistentTable, List<PossibleRelation>> table2relations = _dialog.getCache().get(schema);
				if(table2relations != null)
				{
					List<EPersistentTable> pTables = new ArrayList<EPersistentTable>();
					pTables.addAll(table2relations.keySet());
					Collections.sort(pTables, new Comparator<EPersistentTable>() 
					{
						public int compare(EPersistentTable o1, EPersistentTable o2) 
						{
							return o1.getName().compareToIgnoreCase(o2.getName());
						};
					});
					return pTables.toArray();
				}
			}
			return new Object[0];
		
		}
		
		public void dispose()
		{
			// Nada que liberar.
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{
			if(newInput instanceof ESchema)
			{
				_input = (ESchema)newInput;
			}
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if(parentElement instanceof ESchema)
			{
				Map<EPersistentTable, List<PossibleRelation>> table2relations = _dialog.getCache().get((ESchema)parentElement);
				if(table2relations != null)
					return table2relations.keySet().toArray();
			}
			else if(parentElement instanceof EPersistentTable)
			{
				List<PossibleRelationElement> children = new ArrayList<PossibleRelationElement>();
				List<PossibleRelationElement> tElements  = PossibleRelationsSection.this._tElements;
				for(PossibleRelationElement te: tElements)
				{
					if(te.getChildTable().equals(parentElement))
						children.add(te);
				}
				Collections.sort(children , new Comparator<PossibleRelationElement>() 
					{
						public int compare(PossibleRelationElement o1, PossibleRelationElement o2) 
						{
							// Criterio de comparacion:
							// Tamaño (de mayor a menor número de columnas que componen la clave). 
							// Nombre de tabla padre
							if(o1.getColumnsUK().size()>o2.getColumnsUK().size())
								return -1;
							if(o1.getColumnsUK().size()<o2.getColumnsUK().size())
								return 1;
							if(o2.getColumnsUK().size()>o1.getColumnsUK().size())
								return -1;
							if(o2.getColumnsUK().size()<o1.getColumnsUK().size())
								return 1;
							if(o1.getColumnsUK().size()==o2.getColumnsUK().size())
							{
								return o1.getParentTable().getName().compareToIgnoreCase(o2.getParentTable().getName());
							}
							
							return -1;
						};
					});
				return children.toArray();	
			}
					
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			if(element instanceof PossibleRelationElement)
			{
				return ((PossibleRelationElement)element).getChildTable();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if(element instanceof PossibleRelationElement)
				return false;
			else return getChildren(element).length>0;
		}
		
	}
	
	class TableLabelProvider implements ITableLabelProvider
	{

		@Override
		public Image getColumnImage(Object element, int columnIndex) 
		{
			if(element instanceof EPersistentTable)
			{
				if(columnIndex==COLUMN_COLUMNS_SUGGESTED_INDEX)
					return myAdapterFactoryLabelProvider
					.getImage((EPersistentTable)element);
			}
			else 
			if(element instanceof PossibleRelationElement)
			{
				if(columnIndex==COLUMN_SELECT_INDEX)
				{
					if (((PossibleRelationElement) element).isCreate()) {
						return CHECKED;
					} else {
						return UNCHECKED;
					}
					
				}
//				else if(columnIndex == COLUMN_PARENT_TABLE_INDEX)
//				{
//					return myAdapterFactoryLabelProvider
//					.getImage(((PossibleRelationElement)element).getParentTable());
//				}
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			switch(columnIndex)
			{
				case COLUMN_COLUMNS_SUGGESTED_INDEX: 
				{
					if(element instanceof EPersistentTable)
					{
						EPersistentTable table = (EPersistentTable)element;
						return table.getName();
						
					}
					else 
					{
						StringBuffer buffer = new StringBuffer();
						for(EColumn column : ((PossibleRelationElement)element).getSuggestedColumns())
						{
							buffer.append(column.getName().toLowerCase());
//							buffer.append("[");
//							buffer.append(column.getDataType().getName());
							buffer.append(",");
						}
						buffer.deleteCharAt(buffer.lastIndexOf(","));
						return buffer.toString();
					}
					
				}
				case COLUMN_SELECT_INDEX: 
				{
					return ""; //$NON-NLS-1$
				}
//				case COLUMN_PARENT_TABLE_INDEX:
//				{
//					if(element instanceof PossibleRelationElement)
//					{
//						List<String> tableNames = new ArrayList<String>();
//						
// 						for(ESchema schema: _eProject.getSchemas())
//						{
//							 for(ETable table:schema.getTables())
//								 tableNames.add(table.getName());
//						}
//						int cont = 0;
//						for(String tableName:tableNames)
//						{
//							if(((PossibleRelationElement)element).getParentTable().getName().equalsIgnoreCase(tableName))
//								cont++;
//						}
//						if(cont>1)
//							return ((PossibleRelationElement)element).getParentTable().getName() + " <"+
//							((PossibleRelationElement)element).getParentTable().getSchema().getName()+">";
//						return ((PossibleRelationElement)element).getParentTable().getName();
//					}
//					break;
//				}
				case COLUMN_COLUMNS_UK_INDEX:
				{
					if(element instanceof PossibleRelationElement)
					{
						StringBuffer buffer = new StringBuffer();
						for(int i=0; i<((PossibleRelationElement)element).getColumnsUK().size();i++)
						{
							EColumn column = ((PossibleRelationElement)element).getColumnsUK().get(i);
							if(i==0)
								buffer.append(column.getTable().getName()+".[");
							buffer.append(column.getName().toLowerCase()+",");
//							buffer.append("[");
//							buffer.append(column.getDataType().getName());
//							buffer.append("] ,");
						}
						
						buffer.deleteCharAt(buffer.lastIndexOf(","));
						buffer.append("]");
						return buffer.toString();
					}
					break;
				}
			}
			return ""; //$NON-NLS-1$
		}

		@Override
		public void addListener(ILabelProviderListener listener) {}

		@Override
		public void dispose() {}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {}
		
		
	}
	
	public PossibleRelationsSection(IManagedForm form, Composite parent , PossibleRelationsDialog dialog, EProject eProject) {
		super(parent, form.getToolkit(), Section.TITLE_BAR | Section.DESCRIPTION | Section.EXPANDED | Section.TWISTIE );
		
		_dialog = dialog;
		_eProject = eProject;
		
		initialize(form);
		
		getSection().clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		getSection().setData("part", this); //$NON-NLS-1$
		getSection().setLayout(FormLayoutFactory.createClearGridLayout(true, 2));
		
		createClient(getSection(), form.getToolkit());

	}
	public void createClient(Section section, FormToolkit toolkit) {

		section.setLayout(FormLayoutFactory.createClearGridLayout(true, 2));
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(section, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		GridLayout gl = new GridLayout(1,false);		
		composite.setLayout(gl);

		GridData layoutData = new GridData(GridData.FILL, GridData.FILL, true, true);
		layoutData.minimumHeight = 400;

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnPixelData(375, true));
		layout.addColumnData(new ColumnPixelData(375, true));
		layout.addColumnData(new ColumnPixelData(50, true));
//		layout.addColumnData(new ColumnPixelData(150, true));
		
		Tree tree = new Tree(composite, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		
		_viewer = new TreeViewer(tree);
				
		_viewer.getTree().setLinesVisible(true);
		_viewer.getTree().setLayout(layout);
		_viewer.getTree().setLayoutData(layoutData);
		_viewer.getTree().setHeaderVisible(true);
		
								
		TreeViewerColumn stateColumn = new TreeViewerColumn(_viewer, SWT.NONE);
		stateColumn.getColumn().setResizable(true);
		stateColumn.getColumn().setMoveable(true);
		stateColumn.getColumn().setText(COLUMNS_SUGGESTED);
		
		
	
//		TreeViewerColumn contractColumn = new TreeViewerColumn(_viewer, SWT.NONE);
//		contractColumn.getColumn().setResizable(true);
//		contractColumn.getColumn().setMoveable(true);
//		contractColumn.getColumn().setText(PARENT_TABLE);
//		contractColumn.setEditingSupport(new ContractEditingSupport(_viewer , _dialog));
		
		TreeViewerColumn columnsUKColumn = new TreeViewerColumn(_viewer, SWT.NONE);
		columnsUKColumn.getColumn().setResizable(true);
		columnsUKColumn.getColumn().setMoveable(true);
		columnsUKColumn.getColumn().setText(COLUMNS_UK);
		
		TreeViewerColumn transformColumn = new TreeViewerColumn(_viewer, SWT.NONE);		
		transformColumn.getColumn().setResizable(true);
		transformColumn.getColumn().setMoveable(true);
		transformColumn.getColumn().setText(SELECT);
				
		CheckBoxEditingSupport editingSupport = new CheckBoxEditingSupport(_viewer , _dialog);
		transformColumn.setEditingSupport(editingSupport);
		

		
		_viewer.setContentProvider(new TableContentProvider());
		_viewer.setLabelProvider(new TableLabelProvider());		
		_viewer.setInput(getInput());
		_viewer.setAutoExpandLevel(2);
		
					
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));	
		section.setClient(scrolledComposite);	
		
		

	}	
	protected List<PossibleRelationElement> initializeTransforsElements(ESchema tableInput) 
	{
		List<PossibleRelationElement> tElements = new ArrayList<PossibleRelationElement>();
		ESchema schema = ((ESchema)tableInput);
		Map<ESchema, Map<EPersistentTable, List<PossibleRelation>>> cache = _dialog.getCache();
		Map<EPersistentTable, List<PossibleRelation>> table2relations = cache.get(schema);
		if(!table2relations.isEmpty())
		{
			for(EPersistentTable table : table2relations.keySet())
			{
				for(PossibleRelation pr : table2relations.get(table))
				{
					tElements.add(new PossibleRelationElement(pr.getColumns(), pr.getUK(), pr.getParentTable(), table, false));
				}
			}
			
		}
		return tElements;
		
		
	}
	@Override
	public boolean setFormInput(Object input) {
		
		if(input instanceof ESchema)
		{
			_input = input;
			_tElements = initializeTransforsElements((ESchema)input);
			if(_viewer!=null)
			{
				_viewer.getTree().setRedraw(false);
				_viewer.setInput(input);
//				_viewer.expandAll();
			    _viewer.getTree().setRedraw(true);
			}
			
			
		}
		return super.setFormInput(input);
	}
	private Object getInput()
	{
		return _input;
	}
	public List<PossibleRelationElement> getTransformElements()
	{
		return _tElements;
	}

}
