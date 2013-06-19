package com.isb.datamodeler.diagram.providers;

import java.util.List;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;

import com.isb.datamodeler.diagram.edit.parts.ForeignKey2EditPart;

/**
 * Filtra las tools que aparecen el popup bar
 * @author xIS05655
 *
 */
public class DatamodelerModelingAssistantProviderFilter {
	
	public static void filterTypesForPopupBar(List<IElementType> elementTypes)
	{
		elementTypes.remove(DatamodelerElementTypes.getElementType(ForeignKey2EditPart.VISUAL_ID));
	}
}
