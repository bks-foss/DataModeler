package com.isb.datamodeler.internal.ui.views;

import java.text.Collator;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.UtilsDataModelerUI;
import com.isb.datamodeler.ui.diagram.comparators.ColumnComparator;
import com.isb.datamodeler.ui.project.EProject;

/**
 * Sorter para el navigado DataModeler
 * @author xIS05655
 *
 */
public class DatamodelerCommonSorter extends ViewerSorter {

	public DatamodelerCommonSorter() {
		// TODO Auto-generated constructor stub
	}

	public DatamodelerCommonSorter(Collator collator) {
		super(collator);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		
		EObject obj1 = ((DatamodelerDomainNavigatorItem)e1).getEObject();
		EObject obj2 = ((DatamodelerDomainNavigatorItem)e2).getEObject();
		
		if(obj1 instanceof ESchema && obj2 instanceof ESchema)
		{
			ESchema schema1 = (ESchema)obj1;
			ESchema schema2 = (ESchema)obj2;
			Object parent1 = ((DatamodelerDomainNavigatorItem)e1).getParent();
			Object parent2 = ((DatamodelerDomainNavigatorItem)e2).getParent();
			if(parent1 instanceof IProject && parent2 instanceof IProject)
			{
				EProject eProject1 = UtilsDataModelerUI.findEProject((IProject)parent1);
				EProject eProject2 = UtilsDataModelerUI.findEProject((IProject)parent2);
				if(isExternalSchema(schema1, eProject1))
				{
					if(!isExternalSchema(schema2, eProject2))
						return 1;
				}
				else if(isExternalSchema(schema2, eProject2))
					return -1;
			}
		}
		if(obj1 instanceof EColumn && obj2 instanceof EColumn)
			return (new ColumnComparator()).compare(obj1, obj2);
		
		return super.compare(viewer, e1, e2);
	}
	private boolean isExternalSchema(ESchema schema , EProject project)
	{
		return !schema.getCapability().equals(project.getCapability());
	}

//	/**
//	 * Compara dos columnas de acuerdo al orden PKs, FKS, columnas normales.
//	 * @param col1
//	 * @param col2
//	 * @return
//	 */
//	private int compareColumns(EColumn col1, EColumn col2) {
//		//Si una de las columnas es PK, va primero:
//		if(col2.isPrimaryKey() && (col1.isPartOfForeingKey() || !col1.isPrimaryKey()))
//			return 1;
//		
//		if(col1.isPrimaryKey() &&(col2.isPartOfForeingKey() || !col2.isPrimaryKey()))
//			return -1;
//		
//		//Si una de las columnas es FK, y la otra no es PK , va primero
//		if(col2.isPartOfForeingKey() && !col1.isPrimaryKey())
//			return 1;
//
//		if(col1.isPartOfForeingKey() && !col2.isPrimaryKey())
//			return -1;
//		
//		int index1 = col1.getTable().getColumns().indexOf(col1);
//		int index2 = col2.getTable().getColumns().indexOf(col2);
//		
//		if(index1>index2)
//			return 1;
//		else
//			return -1;
//
//	}
	@Override
	public int category(Object element) {
		
		EObject obj1 = ((DatamodelerDomainNavigatorItem)element).getEObject();
		
		// Column
		if(obj1 instanceof EColumn)
			return 1;
		// PK
		if(obj1 instanceof EPrimaryKey)
			return 2;
		// UK
		if(obj1 instanceof EUniqueConstraint)
			return 3;
		// FK
		if(obj1 instanceof EForeignKey)
			return 4;
		// Tabla (para que pinte los diagramas primero)
		if(obj1 instanceof ETable)
			return 1;
		
		return -1;
	}
}
