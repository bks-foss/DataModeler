package com.isb.datamodeler.diagram.part;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;

/**
 * @generated
 */
public class DatamodelerPaletteFactory {

	/**
	 * @generated
	 */
	public void fillPalette(PaletteRoot paletteRoot) {
		paletteRoot.add(createUi1Group());
	}

	/**
	 * Creates "ui" palette tool group
	 * @generated
	 */
	private PaletteContainer createUi1Group() {
		PaletteGroup paletteContainer = new PaletteGroup(
				Messages.Ui1Group_title);
		paletteContainer.setId("createUi1Group"); //$NON-NLS-1$
		paletteContainer.add(createTable1CreationTool());
		paletteContainer.add(createColumn2CreationTool());
		paletteContainer.add(createUniqueKey3CreationTool());
		paletteContainer.add(createIdentifying4CreationTool());
		paletteContainer.add(createNonIdentifying5CreationTool());
		return paletteContainer;
	}

	/**
	 * @generated
	 */
	private ToolEntry createTable1CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(
				Messages.Table1CreationTool_title,
				Messages.Table1CreationTool_desc,
				Collections
						.singletonList(DatamodelerElementTypes.PersistentTable_2009));
		entry.setId("createTable1CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(DatamodelerElementTypes
				.getImageDescriptor(DatamodelerElementTypes.PersistentTable_2009));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createColumn2CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(
				Messages.Column2CreationTool_title,
				Messages.Column2CreationTool_desc,
				Collections.singletonList(DatamodelerElementTypes.Column_3001));
		entry.setId("createColumn2CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(DatamodelerElementTypes
				.getImageDescriptor(DatamodelerElementTypes.Column_3001));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createUniqueKey3CreationTool() {
		NodeToolEntry entry = new NodeToolEntry(
				Messages.UniqueKey3CreationTool_title,
				Messages.UniqueKey3CreationTool_desc,
				Collections
						.singletonList(DatamodelerElementTypes.UniqueConstraint_3002));
		entry.setId("createUniqueKey3CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(DatamodelerElementTypes
				.getImageDescriptor(DatamodelerElementTypes.UniqueConstraint_3002));
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createIdentifying4CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(
				Messages.Identifying4CreationTool_title,
				Messages.Identifying4CreationTool_desc,
				Collections
						.singletonList(DatamodelerElementTypes.ForeignKey_4001));
		entry.setId("createIdentifying4CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(DatamodelerDiagramEditorPlugin
				.findImageDescriptor("/com.isb.datamodeler.model.edit/icons/full/obj16/Pkey_Identifying.gif")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private ToolEntry createNonIdentifying5CreationTool() {
		LinkToolEntry entry = new LinkToolEntry(
				Messages.NonIdentifying5CreationTool_title,
				Messages.NonIdentifying5CreationTool_desc,
				Collections
						.singletonList(DatamodelerElementTypes.ForeignKey_4002));
		entry.setId("createNonIdentifying5CreationTool"); //$NON-NLS-1$
		entry.setSmallIcon(DatamodelerDiagramEditorPlugin
				.findImageDescriptor("/com.isb.datamodeler.model.edit/icons/full/obj16/Pkey_Non_Identifying.gif")); //$NON-NLS-1$
		entry.setLargeIcon(entry.getSmallIcon());
		return entry;
	}

	/**
	 * @generated
	 */
	private static class NodeToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List<IElementType> elementTypes;

		/**
		 * @generated
		 */
		private NodeToolEntry(String title, String description,
				List<IElementType> elementTypes) {
			super(title, description, null, null);
			this.elementTypes = elementTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}

	/**
	 * @generated
	 */
	private static class LinkToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List<IElementType> relationshipTypes;

		/**
		 * @generated
		 */
		private LinkToolEntry(String title, String description,
				List<IElementType> relationshipTypes) {
			super(title, description, null, null);
			this.relationshipTypes = relationshipTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
}
