package com.isb.datamodeler.tables.invocation;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicInvocationDelegate;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.ETableConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.ETablesPackage;

public class BaseTableInvocationDelegate extends BasicInvocationDelegate {

	public BaseTableInvocationDelegate(EOperation operation) {
		super(operation);
	}

	@Override
	public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
			throws InvocationTargetException {

	    if (eOperation.getEContainingClass() == ETablesPackage.Literals.BASE_TABLE)
	    {

	      switch (eOperation.getEContainingClass().getEAllOperations().indexOf(eOperation))
	      {
	        case ETablesPackage.BASE_TABLE___GET_PRIMARY_KEY:
	            return getPrimaryKey((EBaseTable)target);	           
	        case ETablesPackage.BASE_TABLE___GET_FOREIGN_KEYS:
	            return getForeingKeys((EBaseTable)target);	 
	        case ETablesPackage.BASE_TABLE___GET_UNIQUE_CONSTRAINTS:
	            return getUniqueConstraints((EBaseTable)target);	 
	      }
	    }
		
		return super.dynamicInvoke(target, arguments);
	}

	private EPrimaryKey getPrimaryKey(EBaseTable target) {
		
		for(ETableConstraint constraint : target.getConstraints())
			if(constraint instanceof EPrimaryKey)
				return (EPrimaryKey)constraint;
		
		return null;
	}
	
	private List<EUniqueConstraint> getUniqueConstraints(EBaseTable target) {
		
		List<EUniqueConstraint> uniqueConstraints = new BasicEList<EUniqueConstraint>();
		
		for(ETableConstraint constraint : target.getConstraints())
			if(constraint instanceof EUniqueConstraint)
				uniqueConstraints.add((EUniqueConstraint)constraint);
		
		return uniqueConstraints;
	}

	private List<EForeignKey> getForeingKeys(EBaseTable target) {
		
		List<EForeignKey> foreingKeys = new BasicEList<EForeignKey>();
		
		for(ETableConstraint constraint : target.getConstraints())
			if(constraint instanceof EForeignKey)
				foreingKeys.add((EForeignKey)constraint);
		
		return foreingKeys;
	}
}
