package com.isb.datamodeler.diagram.parsers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ISemanticParser;

import com.isb.datamodeler.tables.EColumn;

/**
 * MessageFormatParser para ColumnEditPart (Se asocia en DatamodelerParserProvider)
 * Lo usamos para provocar un refresco del label, ante cambios en la columna y en las propiedades del tipo de dato
 * @author xIS05655
 *
 */
public class ColumnFormatParser extends MessageFormatParser implements ISemanticParser{

	public ColumnFormatParser(EAttribute[] features) {
		super(features);
	}

	public ColumnFormatParser(EAttribute[] features,
			EAttribute[] editableFeatures) {
		super(features, editableFeatures);
	}

	public static ColumnFormatParser getInstance(EAttribute[] features,
			EAttribute[] editableFeatures)
	{
		if(editableFeatures==null)
			return new ColumnFormatParser(features);
		else
			return new ColumnFormatParser(features, editableFeatures);
	}

	/**
	 * Al hacer que esta clase implemente ISemanticParser, podemos hacer que ColumnEditPart escuche notificaciones de
	 * otros Notifiers ademas de EColumn
	 */
	@Override
	public List getSemanticElementsBeingParsed(EObject element) {
		List<EObject> parserElements = new ArrayList<EObject>(2);
		
		//Deberia ser siempre EColumn pero alguna vez da un ClassCast y no sabemos por que
		if(element instanceof EColumn)
		{
			parserElements.add((EColumn)element);
			parserElements.add(((EColumn)element).getDataType());
		}
		
		
		return parserElements;
	}

	@Override
	public boolean areSemanticElementsAffected(EObject listener,
			Object notification) {
		return true;
	}

}
