package com.isb.datamodeler.diagram.sheet;

import org.eclipse.jface.viewers.IFilter;

import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;
import com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem;
import com.isb.datamodeler.tables.EColumn;

/**
 * Filtro para que la secci�n de tipo de dato de las propiedades s�lo aparezca en las columnas
 */
public class ColumnDataTypePropertyFilter implements IFilter
{

	@Override
public boolean select(Object toTest)
{
	if(toTest instanceof DatamodelerDomainNavigatorItem)
	{
		if(((DatamodelerDomainNavigatorItem) toTest).getEObject() instanceof EColumn)
			return true;
	}
	else if(toTest instanceof ColumnEditPart)
		return true;
	
	return false;
}
}
