package com.isb.datamodeler.constraints.invocation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.BasicInvocationDelegate;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;

public class ForeignKeyInvocationDelegate extends BasicInvocationDelegate {

	public ForeignKeyInvocationDelegate(EOperation operation) {
		super(operation);
	}

	@Override
	public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
			throws InvocationTargetException {

	    if (eOperation.getEContainingClass() == EConstraintsPackage.Literals.FOREIGN_KEY)
	    {
	      switch (eOperation.getEContainingClass().getEAllOperations().indexOf(eOperation))
	      {
	        case EConstraintsPackage.FOREIGN_KEY___ALL_MEMBERS_PART_OF_PRIMARY_KEY:
	            return allMembersPartOfPrimaryKey((EForeignKey)target);	
	        case EConstraintsPackage.FOREIGN_KEY___GET_PAIRS:
	            return getForeignKeyPairs((EForeignKey)target);		 
	        case EConstraintsPackage.FOREIGN_KEY___CONTAINS_PAIR__ECOLUMN_ECOLUMN:
	            return containsPairs((EForeignKey)target, (EColumn)arguments.get(0), (EColumn)arguments.get(1));		            
	      }
	    }
		
		return super.dynamicInvoke(target, arguments);
	}

	private boolean allMembersPartOfPrimaryKey(EForeignKey foreignKey) {
		
		EBaseTable table = foreignKey.getBaseTable();
		EPrimaryKey tablePK = table!=null ? table.getPrimaryKey() : null;
		
		return tablePK!=null && tablePK.getMembers().containsAll(foreignKey.getMembers());
	}
	
	/**
	 * Devuelve una lista de pares <memberColumn, referencedMemberColumn>
	 * @param target
	 * @return
	 */
	private List<EColumn[]> getForeignKeyPairs(EForeignKey target) {
		
		int membersSize = target.getMembers().size();
		int referencedMembersSize = target.getReferencedMembers().size();
		
		int mapSize = membersSize>=referencedMembersSize?membersSize:referencedMembersSize;
		
		List<EColumn[]> pairs = new ArrayList<EColumn[]>(mapSize);
		
		//Si la cantidad de columnas y miembros referenciados no es la misma, los valores que falten seran null
		for(int i=0;i<mapSize;i++)
		{
			EColumn member = target.getMembers().size()>i?target.getMembers().get(i):null;
			EColumn referencedMember = target.getReferencedMembers().size()>i?target.getReferencedMembers().get(i):null;
			
			pairs.add(new EColumn[]{member, referencedMember});
		}
		
		return pairs;
	}
	
	/**
	 * Returns true if target contains a pair <memberColumn, referencedColumn>
	 * @param target
	 * @param memberColumn
	 * @param referencedColumn
	 * @return
	 */
	private boolean containsPairs(EForeignKey target, EColumn memberColumn, EColumn referencedColumn) {
		
		for(EColumn[] pair:target.getPairs())
		{
			EColumn targetMember = pair[0];
			EColumn targetReferencedMember = pair[1];
			
			if(targetMember==null || targetReferencedMember==null)
				return false;
			
			if(targetMember.getName().equals(memberColumn.getName()) && targetReferencedMember.getName().equals(referencedColumn.getName()))
				return true;
		}
		return false;
	}

}
