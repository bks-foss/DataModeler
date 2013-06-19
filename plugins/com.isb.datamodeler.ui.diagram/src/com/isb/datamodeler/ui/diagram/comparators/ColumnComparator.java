package com.isb.datamodeler.ui.diagram.comparators;

import java.util.Comparator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Node;

import com.isb.datamodeler.diagram.edit.parts.PersistentTableColumnCompartmentEditPart;
import com.isb.datamodeler.tables.EColumn;

/**
 * Comparador de Columnas para uso del navegador y el diagrama
 * @see DatamodelerCommonSorter.java
 * @see PersistentTableColumnCompartmentEditPart
 * @author xIS05655
 *
 */
public class ColumnComparator implements Comparator<EObject> {
	
	@Override
	/**
	 * Por ahora mostramos las columnas en el orden en que están en la tabla
	 * Si mas adelante queremos otro orden, descomentar el comare de mas abajo
	 */
	public int compare(EObject obj1, EObject obj2) {
		
		EColumn col1 = null;
		EColumn col2 = null;
		
		if(obj1 instanceof EColumn)
			col1 = (EColumn)obj1;
		
		if(obj1 instanceof Node && ((Node)obj1).getElement() instanceof EColumn)
			col1 = (EColumn)((Node)obj1).getElement();

		if(obj2 instanceof EColumn)
			col2 = (EColumn)obj2;
		
		if(obj2 instanceof Node && ((Node)obj2).getElement() instanceof EColumn)
			col2 = (EColumn)((Node)obj2).getElement();
		
		//No se pueden comparar
		if(col1==null || col2 == null)
			return 0;
		
		//Si son columnas normales, ordenamos por su orden en la tabla:
		int index1 = col1.getTable().getColumns().indexOf(col1);
		int index2 = col2.getTable().getColumns().indexOf(col2);
		
		if (index1 > index2)
			return 1;
		else
			return -1;
	}

//	@Override
//	public int compare(EObject obj1, EObject obj2) {
//		
//		EColumn col1 = null;
//		EColumn col2 = null;
//		
//		if(obj1 instanceof EColumn)
//			col1 = (EColumn)obj1;
//		
//		if(obj1 instanceof Node)
//			col1 = (EColumn)((Node)obj1).getElement();
//
//		if(obj2 instanceof EColumn)
//			col2 = (EColumn)obj2;
//		
//		if(obj2 instanceof Node)
//			col2 = (EColumn)((Node)obj2).getElement();
//		
//		//No se pueden comparar
//		if(col1==null || col2 == null)
//			return 0;
//		
//		//Si las dos son PK importa el orden dentro de la constraint:
//		if (col1.isPrimaryKey() && col2.isPrimaryKey()) {
//			int index1 = ((EBaseTable) col1.getTable()).getPrimaryKey()
//					.getMembers().indexOf(col1);
//			int index2 = ((EBaseTable) col2.getTable()).getPrimaryKey()
//					.getMembers().indexOf(col2);
//
//			if (index1 > index2)
//				return 1;
//			else
//				return -1;
//
//		}
//
//		//Si solo una de las columnas es PK, va primero:
//		if (col2.isPrimaryKey()	&& !col1.isPrimaryKey())
//			return 1;
//
//		if (col1.isPrimaryKey()	&& !col2.isPrimaryKey())
//			return -1;
//
//		//Si una de las columnas es FK, y la otra no es PK ni FK , va primero
//		if (col2.isPartOfForeingKey() && (!col1.isPartOfForeingKey() && !col1.isPrimaryKey()))
//			return 1;
//
//		if (col1.isPartOfForeingKey() && (!col2.isPartOfForeingKey() && !col2.isPrimaryKey()))
//			return -1;
//
//		//Si son columnas normales, ordenamos por su orden en la tabla:
//		int index1 = col1.getTable().getColumns().indexOf(col1);
//		int index2 = col2.getTable().getColumns().indexOf(col2);
//		
//		//Si las dos columnas son parte de una FK, intentamos ordenarlas segun el orden relativo dentro de la FK
//		//en caso de que pertenezcan a una misma y unica FK (Si las dos columnas pertenecen a mas de una FK en comun, puede haber contradicciones)
//		if(col1.isPartOfForeingKey() && col2.isPartOfForeingKey())
//		{
//			List<EForeignKey> commonFKList = getColumnsCommonFKs(col1, col2);
//			if(commonFKList.size()==1)
//			{
//				index1 = commonFKList.get(0).getMembers().indexOf(col1);
//				index2 = commonFKList.get(0).getMembers().indexOf(col2);
//			}
//		}
//		
//
//		if (index1 > index2)
//			return 1;
//		else
//			return -1;
//	}
	
//	private List<EForeignKey> getColumnsCommonFKs(EColumn col1, EColumn col2)
//	{
//		 List<EForeignKey> commonFKList = new ArrayList<EForeignKey>();
//		 
//		 List<EReferenceConstraint> rcList1 = col1.getReferencingConstraints();
//		 List<EReferenceConstraint> rcList2 = col2.getReferencingConstraints();
//		 
//		 for(EReferenceConstraint rc: rcList1)
//			 if(rc instanceof EForeignKey && rcList2.contains(rc))
//				 commonFKList.add((EForeignKey)rc);
//		 
//		 return commonFKList;
//	}
	

}
