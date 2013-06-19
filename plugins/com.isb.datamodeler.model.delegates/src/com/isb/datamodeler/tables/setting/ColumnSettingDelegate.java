package com.isb.datamodeler.tables.setting;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicSettingDelegate.Stateless;

import com.isb.datamodeler.constraints.EConstraintsFactory;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.tables.ETablesPackage;

public class ColumnSettingDelegate extends Stateless {
	
	public ColumnSettingDelegate(EStructuralFeature arg0) {
		super(arg0);
	}

	@Override
	protected Object get(InternalEObject owner, boolean resolve,
			boolean coreType) {
		
	    if (eStructuralFeature.getEContainingClass() == ETablesPackage.Literals.COLUMN)
	    {
		      switch (eStructuralFeature.getEContainingClass().getEAllStructuralFeatures().indexOf(eStructuralFeature))
		      {
		        case ETablesPackage.COLUMN__TABLE:
		        	return getTable((EColumn)owner);
		        case ETablesPackage.COLUMN__PRIMARY_KEY:
		        	return isPrimaryKey((EColumn)owner);		       
		        case ETablesPackage.COLUMN__PART_OF_FOREING_KEY:
		        	return isPartOfForeingKey((EColumn)owner);	
		        case ETablesPackage.COLUMN__PART_OF_UNIQUE_CONSTRAINT:
		        	return isPartOfUniqueConstraintKey((EColumn)owner);	
		      }
	    
	    }
		return null;
	}
	
	
	@Override
	protected void set(InternalEObject owner, Object newValue) {
	   
		if (eStructuralFeature.getEContainingClass() == ETablesPackage.Literals.COLUMN)
	    {
		      switch (eStructuralFeature.getEContainingClass().getEAllStructuralFeatures().indexOf(eStructuralFeature))
		      {
		        case ETablesPackage.COLUMN__PRIMARY_KEY:
		        	if(newValue != null)
		        		setPrimaryKey((EColumn)owner, (Boolean)newValue);		    
		      }
	    
	    }

	}


	/**
	 * Es llamado por el UNDO
	 */
	@Override
	protected void unset(InternalEObject owner) {
		return;
	}
	
	private void setPrimaryKey(EColumn owner, boolean newValue) {
		boolean oldValue = isPrimaryKey(owner);
		
		if(oldValue==newValue)
			return;
		
		EBaseTable baseTable = (EBaseTable)owner.getTable();
		
		EPrimaryKey pk = baseTable.getPrimaryKey();
		
		if(newValue)
		{
			if(pk==null)
			{
				pk = EConstraintsFactory.eINSTANCE.createPrimaryKey();
				baseTable.getConstraints().add(pk);
			}
			
			if(!pk.getMembers().contains(owner))
				pk.getMembers().add(owner);
		}else if(pk!=null)
		{
			if(pk.getMembers().contains(owner))
				pk.getMembers().remove(owner);
		}
	}

	private boolean isPrimaryKey(EColumn owner) {
		
		for(EReferenceConstraint refConstraint:owner.getReferencingConstraints())
			if(refConstraint instanceof EPrimaryKey)
				return true;
		
		return false;
	}
	
	private Object isPartOfUniqueConstraintKey(EColumn owner) {

		for(EReferenceConstraint refConstraint:owner.getReferencingConstraints())
			if(refConstraint instanceof EUniqueConstraint)
				return true;
				
		return false;
	}

	private boolean isPartOfForeingKey(EColumn owner) {
		
		for(EReferenceConstraint refConstraint:owner.getReferencingConstraints())
			if(refConstraint instanceof EForeignKey)
				return true;

		return false;
	}

	private ETable getTable(EColumn column) {
		return (ETable)column.eContainer();
	}

	@Override
	protected boolean isSet(InternalEObject owner) {
		
		return false;
	}

}
