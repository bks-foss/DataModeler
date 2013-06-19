package com.isb.datamodeler.constraints.triggers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.ParentCardinality;
import com.isb.datamodeler.model.triggers.IParametrizableTrigger;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;

public class AddForeignKeyReferencedMembersTrigger extends AbstractConstraintMemberTrigger implements IParametrizableTrigger {
	
	private EForeignKey _foreignKey; 
	private List<EColumn> _newUKMembers = new ArrayList<EColumn>();
	private List<EColumn> _newFKMembers = new ArrayList<EColumn>();
	private List<EColumn> _deletedFKMembers = new ArrayList<EColumn>();
	
	private int childTableActionOption = DEFAULT_NEW_MEMBER_ACTION;
	
	public static final int DO_NOTHING = -1;
	public static final int USE_EXISTING_CHILD_COLUMNS_VALUE = 0;
	public static final int REPLACE_EXISTING_COLUMNS_VALUE = 1;
	public static final int CREATE_NEW_COLUMNS_VALUE = 2;
	public static final int SHOW_DIALOG = 3;
	
	public static final int DEFAULT_NEW_MEMBER_ACTION = DO_NOTHING;
	
	public AddForeignKeyReferencedMembersTrigger(TransactionalEditingDomain domain, EForeignKey owner, EColumn newMember) {
		super(domain);
		
		_foreignKey = owner;
		_newUKMembers.add(newMember);
	}

	public EForeignKey getForeignKey() {
		return _foreignKey;
	}

	public AddForeignKeyReferencedMembersTrigger(TransactionalEditingDomain domain, EForeignKey owner, List<EColumn> newMembers) {
		super(domain);
		
		_foreignKey = owner;
		_newUKMembers.addAll(newMembers);
	}

	@Override
	public void executeTrigger() 
	{
		//En el caso de inferencias de relaciones, no se selecciona ninguna acción:
		if(childTableActionOption==DO_NOTHING)
			return;
		addMissingColumnInChildTableForeignKey();
	}

	@Override
	protected boolean prepare() {
		return _foreignKey!=null;
	}

	//Agregamos la columna que falte en una FK
	private void addMissingColumnInChildTableForeignKey()
	{
		if(childTableActionOption == SHOW_DIALOG)
		{
			for(EColumn deletedColumn:_deletedFKMembers)
				_foreignKey.getMembers().remove(deletedColumn);
			
			for(EColumn newColumn:_newFKMembers)
			{
				_foreignKey.getBaseTable().getColumns().add(newColumn) ;
				_foreignKey.getMembers().add(newColumn);
				// Si la relacion es identificativa además incluimos la columna a la PK
				if(_foreignKey.isIdentifying())
					newColumn.setPrimaryKey(true);		
			}
		}
		else
		{
			//Creamos un map con las columnas que vamos a agregar a la FK y en qué posición
			Map<Integer, EColumn> newFKMembers = new HashMap<Integer, EColumn>();
			
			//Buscamos una columna que coincida en la tabla hija
			for(EColumn newUKMember:_newUKMembers)
			{
				int sourcePosition = _foreignKey.getUniqueConstraint().getMembers().indexOf(newUKMember);
				
				EColumn matchingColumn = getMatchingColumn(newUKMember);
				
				//Si no hay coincidencias, creamos una nueva columna 
				if(matchingColumn==null)
				{
					newFKMembers.put(sourcePosition, createNewChildTableColumn(newUKMember));
					continue;
				}
				
				//En caso de encontrar coincidencias ejecutamos la accion configurada por el usuario:
				switch(childTableActionOption)
				{
					//La Accion configurada es "Utilizar las columnas hijas existentes"
					case USE_EXISTING_CHILD_COLUMNS_VALUE:
					{
						//Agregamos la columna existentes que falte
						if(!_foreignKey.getMembers().contains(matchingColumn))
							newFKMembers.put(sourcePosition, matchingColumn);
						
						break;
					}
					//La Accion configurada es "Sustituir por las columnas referenciadas"
					case REPLACE_EXISTING_COLUMNS_VALUE:
					{
						//Clonamos la nueva columna:
						EColumn newMember = newUKMember.clone();
						
						//Remplazamos la columna antigua
						if(matchingColumn!=null)
							replace(matchingColumn, newMember);
							
						//La agregamos a la lista de FKs
						newFKMembers.put(sourcePosition, newMember);
						
						break;
					}
					//La Accion configurada es "Crear nuevas columnas hijas"
					case CREATE_NEW_COLUMNS_VALUE:
					{
						newFKMembers.put(sourcePosition, createNewChildTableColumn(newUKMember));
						break;
					}
				}
			}
		
			//Agregamos los nuevos miembros de la FK en las posiciones definidas:
			for(Integer position: newFKMembers.keySet())
				_foreignKey.getMembers().add(position, newFKMembers.get(position));
			
			//Si ademas la FK es identificativa, los nuevos miembros seran parte de la PK
			if(_foreignKey.isIdentifying())
				for(EColumn newMember:newFKMembers.values())
					newMember.setPrimaryKey(true);
		}

	}
	
	/**
	 * El replace de EcoreUtil no remplaza el objeto en las features que lo referencian (Bugzilla #20427)
	 * Esto hace que las Constraints que referencien la columna remplazada, sigan refereciando la antigua
	 * @param eObject
	 * @param replacementEObject
	 */
	private static void replace(EObject eObject, EObject replacementEObject)
	{
	    EObject rootEObject = EcoreUtil.getRootContainer(eObject);
	    Resource resource = rootEObject.eResource();
	
	    Collection<EStructuralFeature.Setting> usages;
	    if (resource == null)
	    {
	      usages = UsageCrossReferencer.find(eObject, rootEObject);
	    }
	    else
	    {
	      ResourceSet resourceSet = resource.getResourceSet();
	      if (resourceSet == null)
	      {
	        usages = UsageCrossReferencer.find(eObject, resource);
	      }
	      else
	      {
	        usages = UsageCrossReferencer.find(eObject, resourceSet);
	      }
	    }
	
	    //Reemplazamos el objeto en las referencias:
	    for (EStructuralFeature.Setting setting : usages)
	    {
	      if (setting.getEStructuralFeature().isChangeable())
	      {
	        EcoreUtil.replace(setting, eObject, replacementEObject);
	      }
	    }
	
	    //Por ultimo remplaza el objeto en su container:
	    EcoreUtil.replace(eObject, replacementEObject);
	}
	
	private EColumn createNewChildTableColumn(EColumn newUKMember)
	{
		EBaseTable childTable = _foreignKey.getBaseTable();

		//Clonamos la nueva columna:
		EColumn newMember = newUKMember.clone();
		
		//La Agregamos a la tabla hija
		childTable.getColumns().add(newMember);
		
		//Si la FK es identificativa, primero la agregamos a la PK
		if(_foreignKey.isIdentifying() && childTable.getPrimaryKey()!=null)
			childTable.getPrimaryKey().getMembers().add(newMember);
		
		//Si la cardinalidad de la tabla padre es 1, la nueva columna se crea como no nullable:
		if(_foreignKey.getParentCardinality().equals(ParentCardinality.ONE))
			newMember.setNullable(false);
		
		return newMember;
	}
	
	public List<EColumn> getAllMatchingColumns()
	{
		List<EColumn> matchingColumns = new ArrayList<EColumn>();
		
		for(EColumn newUKMember:_newUKMembers)
		{
			EColumn matchingColumn = getMatchingColumn(newUKMember);
			if(matchingColumn!=null)
				matchingColumns.add(matchingColumn);
		}
		return matchingColumns;
	}

	
	private EColumn getMatchingColumn(EColumn newUKMember)
	{
		EBaseTable table = _foreignKey.getBaseTable();
		
		//Buscamos una columna que coincida con la nueva columna de la UK
		for(EColumn baseMember:table.getColumns())
			if(newUKMember.compareTo(baseMember)==0)
				return baseMember;
		
		return null;
	}
	
	/**
	 * 	Si luego de los cambios, las columnas que quedan en la FK de la tabla son todas PK
	 *  la relación se vuelve identificativa
	 * @param foreignKey
	 */
	protected final void refreshForeignKeyIdentifyingProperty(EForeignKey foreignKey)
	{
		if(foreignKey.allMembersPartOfPrimaryKey() && !foreignKey.isIdentifying())
			foreignKey.setIdentifying(true);
	}

	public void setChildTableNewMeberAction(int value)
	{
		childTableActionOption = value;
	}

	public List<EColumn> getNewFKReferencedMembers() {
		return _newUKMembers;
	}
	
	public void setNewFKMembers(List<EColumn> newColumns) {
		_newFKMembers.addAll(newColumns);
	}
	
	public void setDeletedFKMembers(List<EColumn> deletedColumns) {
		_deletedFKMembers.addAll(deletedColumns);
	}
}
