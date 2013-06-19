package com.isb.datamodeler.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.List;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.internal.ui.views.actions.refactor.DetectPossibleRelationsAction;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.ETable;

/**
 * Acción encargada de detectar posibles referencias desde las tablas del modelo hacia las tablas seleccionadas.
 * Ofrece la posibilidad de añadir estas referencias al modelo mediando un diálogo.
 * 
 * La acción estará disponible desde el menú contextual del nodo del proyecto del navegador Data Modeler.
 * 
 * @author xIS00714
 *
 */
public class DetectPossibleInRelationsAction extends DetectPossibleRelationsAction{//implements IActionDelegate {
	
	public DetectPossibleInRelationsAction() {
		super(Messages.bind("DetectPossibleRelationsInAction.name"));
	}

@Override
	public List<ETable> getApplicationTables() 
	{
		List<ETable> applicationTables = new ArrayList<ETable>();
		
		for(ESchema eSchema : getEProject().getSchemas())
		{
			// No detectamos posibles relaciondes desde tablas externas
			if(eSchema.getCapability().equals(getEProject().getCapability()))
				applicationTables.addAll(eSchema.getTables());
		}
		
		return applicationTables;
	}

@Override
	public List<ETable> getPossibleTables()
	{
		List<ETable> possibleTables = new ArrayList<ETable>();
		
		possibleTables.addAll(getSelectedTables());
		
		return possibleTables;
	}
}
