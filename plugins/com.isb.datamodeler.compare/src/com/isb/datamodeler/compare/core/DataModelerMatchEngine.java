package com.isb.datamodeler.compare.core;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.engine.GenericMatchEngine;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.datatypes.ESQLDataType;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.ui.project.EProject;

/**
 * Motor de macheo específico para los modelos DataModeler. 
 * Los elementos de DataModeler se machean por nombre.
 * 
 * @author Alfonso
 *
 */
public class DataModelerMatchEngine extends GenericMatchEngine {

	@Override
	protected boolean isSimilar(EObject obj1, EObject obj2)
			throws FactoryException {			
		
		// Sólo objetos del mismo tipo.
		if (obj1.eClass() != obj2.eClass() && !(obj1 instanceof ESQLDataType) && !(obj2 instanceof ESQLDataType))
			return false;
		
		if (obj1 instanceof EProject)
			return true; 
		
		if (obj1 instanceof ESchema) 
			return ((((ESchema)obj1).getName().equalsIgnoreCase(((ESchema)obj2).getName())) & 
					(((ESchema)obj1).getCapability().equalsIgnoreCase(((ESchema)obj2).getCapability())));	
		
		if (obj1 instanceof EPersistentTable) 		
			return ((EPersistentTable)obj1).getName().trim().equalsIgnoreCase(((EPersistentTable)obj2).getName().trim());		
		
		
		if (obj1 instanceof EColumn)
			return ((EColumn)obj1).getName().trim().equalsIgnoreCase(((EColumn)obj2).getName().trim()) && 
					((EColumn)obj1).getTable().getName().trim().equalsIgnoreCase(((EColumn)obj2).getTable().getName().trim());
		
		if (obj1 instanceof Diagram)
			return ((Diagram)obj1).getName().trim().equalsIgnoreCase(((Diagram)obj2).getName().trim());
		
		if (obj1 instanceof ESQLDataType)			
			return isSameDataModelerSQLDataType (obj1, obj2);
		
		if (obj1 instanceof EPrimaryKey)
			return true;
		
		if (obj1 instanceof EForeignKey)
			return ((EForeignKey) obj1).getName().equals(((EForeignKey) obj2).getName());
		
		if (obj1 instanceof EReferenceConstraint)
			return ((EReferenceConstraint) obj1).getName().equalsIgnoreCase(((EReferenceConstraint) obj2).getName());
		
		
									
		// sino se machean por el generic match Engine.
		return super.isSimilar(obj1, obj2);
	}
	
	@Override
	protected EObject findMostSimilar(EObject eObj, List<EObject> list)
			throws FactoryException {
		if (eObj instanceof ESQLDataType) {						
			final Iterator<EObject> it = list.iterator();
			while (it.hasNext()) {
				final EObject next = it.next();
				if (next instanceof ESQLDataType &&
						isSameDataModelerSQLDataType( eObj, next))
						return next;
			}
		}
		
		return super.findMostSimilar(eObj, list);				
	}
	
	/**
	 * Si las columnas que los contienen tienen el mismo nombre tendremos que machear estos tipos
	 * de datos.
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	private boolean isSameDataModelerSQLDataType (EObject obj1, EObject obj2) {
		if (((ESQLDataType)obj1).eContainer() instanceof EColumn &&  ((ESQLDataType)obj2).eContainer() instanceof EColumn) {
			String col1Name = ((EColumn)((ESQLDataType)obj1).eContainer()).getName().trim();
			String col2Name = ((EColumn)((ESQLDataType)obj2).eContainer()).getName().trim();
			
			if (col1Name.equalsIgnoreCase(col2Name))
				return true;
			else
				return false;
		}
		return false;
	}
}
