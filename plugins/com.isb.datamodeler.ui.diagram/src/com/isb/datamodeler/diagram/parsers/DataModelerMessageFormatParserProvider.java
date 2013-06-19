package com.isb.datamodeler.diagram.parsers;

import org.eclipse.emf.ecore.EAttribute;

import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;

/**
 * Se encarga de crear un MessageFormatParser para un EditPart en particular
 * El MessageFormatParser define cuales son las features que afectan el refresco del editPart
 * Por defecto solo afectan las definidas como Label en el gmfmap.
 * @author xIS05655
 *
 */
public class DataModelerMessageFormatParserProvider{

	public static MessageFormatParser getParser(int visualId, EAttribute[] features)
	{
		return createParser(visualId, features, null);
	}
	
	public static MessageFormatParser getParser(int visualId, EAttribute[] features, EAttribute[] editableFeatures)
	{
		return createParser(visualId, features, editableFeatures);
	}
	
	private static MessageFormatParser createParser(int visualId, EAttribute[] features, EAttribute[] editableFeatures)
	{
		switch(visualId)
		{
			case ColumnEditPart.VISUAL_ID: return ColumnFormatParser.getInstance(features, editableFeatures); 
		}
		
		if(editableFeatures==null)
			return new MessageFormatParser(features);
		else
			return new MessageFormatParser(features, editableFeatures);
		
	}
	
}
