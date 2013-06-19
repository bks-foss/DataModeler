package com.isb.datamodeler.tables.triggers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EConstraint;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.model.triggers.AbstractInitializerTrigger;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.tables.EBaseTable;

/**
 * Comando encargado de la refactorización de los hijos de una tabla
 *
 */
public class TableChildrenRefactorizerTrigger extends AbstractInitializerTrigger {
	
	private EBaseTable _parentTable;
	
	public TableChildrenRefactorizerTrigger(TransactionalEditingDomain domain, EBaseTable owner) {
		super(domain);
		
		_parentTable = owner;
	}

	@Override
	public void executeTrigger() {
		
		EDatabase db = _parentTable.getSchema().getDatabase();
		
		for(EConstraint constraint:_parentTable.getConstraints())
		{
			if(constraint instanceof EUniqueConstraint)
			{
				// Bugzilla 20420
				// Si los nombres de la tabla son muy largos el algoritmo de renombrado puede 
				// dar como resultado el mismo nombre y le añadiría un digito incorrectamente.
				// Para evitarlo "reseteamos" el nombre viejo. Si el nombre viejo contiene _FK...
				String constraintName= constraint.getName();
				if(constraintName.contains("_FK")) //$NON-NLS-1$
					constraint.setName(constraintName.replace("_FK", ""));//$NON-NLS-1$
				
				String uniqueConstraintName = db.initializeUniqueConstraintName(_parentTable, (EUniqueConstraint)constraint);
				constraint.setName(uniqueConstraintName);
			}
			else if(constraint instanceof EForeignKey)
			{
				String constraintName= constraint.getName();
				if(constraintName.contains("_FK")) //$NON-NLS-1$
					constraint.setName(constraintName.replace("_FK", ""));//$NON-NLS-1$
				
				String foreignKeyName = db.initializeForeignKeyName(_parentTable, ((EForeignKey)constraint).getParentTable());
				constraint.setName(foreignKeyName);
			}
		}
		// También tenemos que renombrar las FK "entrantes" siempre que no sean FK recursivas
		for(EConstraint referencingFK:_parentTable.getReferencingForeignKeys())
		{
			EBaseTable baseTable = ((EForeignKey)referencingFK).getBaseTable();
			
			if(_parentTable!=baseTable)
			{
				String constraintName= referencingFK.getName();
				if(constraintName.contains("_FK")) //$NON-NLS-1$
					referencingFK.setName(constraintName.replace("_FK", ""));//$NON-NLS-1$
				
				String foreignKeyName = db.initializeForeignKeyName(baseTable , _parentTable);
				referencingFK.setName(foreignKeyName);
			}
		}
	}
}
