package com.isb.datamodeler.ui.diagram.comparators;

import java.util.Comparator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Node;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableColumnCompartmentEditPart;

/**
 * Comparador de Columnas para uso del navegador y el diagrama
 * @see DatamodelerCommonSorter.java
 * @see PersistentTableColumnCompartmentEditPart
 * @author xIS05655
 *
 */
public class ConstraintsComparator implements Comparator<EObject> {
	
	@Override
	/**
	 * Por ahora mostramos las columnas en el orden en que están en la tabla
	 * Si mas adelante queremos otro orden, descomentar el comare de mas abajo
	 */
	public int compare(EObject obj1, EObject obj2) {
		
		EReferenceConstraint constrains1 = null;
		EReferenceConstraint constrains2 = null;
		
		if(obj1 instanceof EReferenceConstraint)
			constrains1 = (EReferenceConstraint)obj1;
		
		if(obj1 instanceof Node && ((Node)obj1).getElement() instanceof EReferenceConstraint)
			constrains1 = (EReferenceConstraint)((Node)obj1).getElement();

		if(obj2 instanceof EReferenceConstraint)
			constrains2 = (EReferenceConstraint)obj2;
		
		if(obj2 instanceof Node && ((Node)obj2).getElement() instanceof EReferenceConstraint)
			constrains2 = (EReferenceConstraint)((Node)obj2).getElement();
		
		//No se pueden comparar
		if(constrains1==null || constrains2 == null)
			return 0;
		
		if(constrains1 instanceof EPrimaryKey)
		{
			if(constrains2 instanceof EPrimaryKey)
			{
				return constrains1.getName().compareTo(constrains2.getName());
			}
			else return -1;
		}
		if(constrains1 instanceof EUniqueConstraint)
		{
			if(constrains2 instanceof EUniqueConstraint)
			{
				if(constrains2 instanceof EPrimaryKey)
					return 1;
				else return constrains1.getName().compareTo(constrains2.getName());
			}
			else 
			{
				if(constrains2 instanceof EPrimaryKey)
				{
					return 1;
				}
				else return -1;
			}
		}
		if(constrains1 instanceof EForeignKey)
		{
			if(constrains2 instanceof EForeignKey)
			{
				return constrains1.getName().compareTo(constrains2.getName());
			}
			else return 1;
		}
		return -1;
		
	}
	

}
