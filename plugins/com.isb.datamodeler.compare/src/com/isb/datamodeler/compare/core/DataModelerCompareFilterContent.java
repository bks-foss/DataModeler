package com.isb.datamodeler.compare.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.ETable;

public class DataModelerCompareFilterContent {
	
	private Set <String> _filterTableSet = null;
	private boolean _activeFilter = false;
	private static DataModelerCompareFilterContent _instance= null;
	
	private DataModelerCompareFilterContent() {
		_filterTableSet = new HashSet <String>();		
	}
	
	public static DataModelerCompareFilterContent getInstance() {
		if (_instance == null)
			_instance = new DataModelerCompareFilterContent();
		return _instance;
	}
	
	public void dispose() {
		_filterTableSet = null;
		desactiveFilter();
		_instance = null;
	}
	
	public void activeFilter() {
		_activeFilter = true;
	}
	
	public void desactiveFilter() {
		_activeFilter = false;
	}
	
	public void addTables (List<ETable> table) {
		for (ETable eTable : table) {			
			_filterTableSet.add(eTable.getSchema().getName().toUpperCase()+"."+eTable.getName().toUpperCase());
		}
		
	}
	
	public void addSchema (String schemaName, String application) {
		_filterTableSet.add(schemaName.toUpperCase()+"."+application.toUpperCase());
	}
	
	public boolean isActive() {
		return _activeFilter;
	}
	
	public boolean takeFilter (EObject object) {
		
		if (!_activeFilter)
			return false;
			
		if (object instanceof EForeignKey) 
			return true;
		
		else if (object instanceof ETable)
		{
			String qualifiedTableName = ((ETable)object).getSchema().getName().toUpperCase()+ "." + ((ETable)object).getName().toUpperCase();
			if (_filterTableSet.contains(qualifiedTableName))
				return false;
			else 
				return true;
		}
		 else if (object instanceof ESchema) {
			 if (_filterTableSet.contains(((ESchema)object).getName().toUpperCase()+"."+((ESchema)object).getCapability().toUpperCase()))
			 	return false;
		 	else
		 		return true;
		 }
			
		
		
		return false;
		
	}
}
