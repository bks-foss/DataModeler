package com.isb.datamodeler.diagram.providers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.GetParserOperation;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.parser.ParserHintAdapter;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey2EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyBaseCardinalityBaseRoleEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyBaseRoleEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyNameEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyParentCardinalityParentEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyParentRoleEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableNameEditPart;
import com.isb.datamodeler.diagram.edit.parts.RelationshipForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.UniqueConstraintEditPart;
import com.isb.datamodeler.diagram.parsers.DataModelerMessageFormatParserProvider;
import com.isb.datamodeler.diagram.parsers.MessageFormatParser;
import com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry;
import com.isb.datamodeler.schema.ESchemaPackage;

/**
 * @generated
 */
public class DatamodelerParserProvider extends AbstractProvider implements
		IParserProvider {

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser persistentTableName_5004Parser;

	/**
	 * @generated
	 */
	private IParser getPersistentTableName_5004Parser() {
		if (persistentTableName_5004Parser == null) {
			EAttribute[] features = new EAttribute[] { ESchemaPackage.eINSTANCE
					.getDataModelerNamedElement_Name() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(5004, features);
			persistentTableName_5004Parser = parser;
		}
		return persistentTableName_5004Parser;
	}

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser column_3001Parser;

	/**
	 * @generated
	 */
	private IParser getColumn_3001Parser() {
		if (column_3001Parser == null) {
			EAttribute[] features = new EAttribute[] { ESchemaPackage.eINSTANCE
					.getDataModelerNamedElement_Name() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(3001, features);
			column_3001Parser = parser;
		}
		return column_3001Parser;
	}

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser uniqueConstraint_3002Parser;

	/**
	 * @generated
	 */
	private IParser getUniqueConstraint_3002Parser() {
		if (uniqueConstraint_3002Parser == null) {
			EAttribute[] features = new EAttribute[] { ESchemaPackage.eINSTANCE
					.getDataModelerNamedElement_Name() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(3002, features);
			uniqueConstraint_3002Parser = parser;
		}
		return uniqueConstraint_3002Parser;
	}

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser foreignKey_3003Parser;

	/**
	 * @generated
	 */
	private IParser getForeignKey_3003Parser() {
		if (foreignKey_3003Parser == null) {
			EAttribute[] features = new EAttribute[] { ESchemaPackage.eINSTANCE
					.getDataModelerNamedElement_Name() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(3003, features);
			foreignKey_3003Parser = parser;
		}
		return foreignKey_3003Parser;
	}

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser foreignKeyName_6001Parser;

	/**
	 * @generated
	 */
	private IParser getForeignKeyName_6001Parser() {
		if (foreignKeyName_6001Parser == null) {
			EAttribute[] features = new EAttribute[] { ESchemaPackage.eINSTANCE
					.getDataModelerNamedElement_Name() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(6001, features);
			foreignKeyName_6001Parser = parser;
		}
		return foreignKeyName_6001Parser;
	}

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser foreignKeyBaseCardinalityBaseRole_6003Parser;

	/**
	 * @generated
	 */
	private IParser getForeignKeyBaseCardinalityBaseRole_6003Parser() {
		if (foreignKeyBaseCardinalityBaseRole_6003Parser == null) {
			EAttribute[] features = new EAttribute[] {
					EConstraintsPackage.eINSTANCE
							.getForeignKey_BaseCardinality(),
					EConstraintsPackage.eINSTANCE.getForeignKey_BaseRole() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(6003, features);
			parser.setViewPattern("{0}\n{1} "); //$NON-NLS-1$
			parser.setEditorPattern("{0}\n{1} "); //$NON-NLS-1$
			parser.setEditPattern("{0}\n{1} "); //$NON-NLS-1$
			foreignKeyBaseCardinalityBaseRole_6003Parser = parser;
		}
		return foreignKeyBaseCardinalityBaseRole_6003Parser;
	}

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser foreignKeyParentCardinalityParentRole_6004Parser;

	/**
	 * @generated
	 */
	private IParser getForeignKeyParentCardinalityParentRole_6004Parser() {
		if (foreignKeyParentCardinalityParentRole_6004Parser == null) {
			EAttribute[] features = new EAttribute[] {
					EConstraintsPackage.eINSTANCE
							.getForeignKey_ParentCardinality(),
					EConstraintsPackage.eINSTANCE.getForeignKey_ParentRole() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(6004, features);
			parser.setViewPattern("{0}\n{1} "); //$NON-NLS-1$
			parser.setEditorPattern("{0}\n{1} "); //$NON-NLS-1$
			parser.setEditPattern("{0}\n{1} "); //$NON-NLS-1$
			foreignKeyParentCardinalityParentRole_6004Parser = parser;
		}
		return foreignKeyParentCardinalityParentRole_6004Parser;
	}

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser foreignKeyName_6002Parser;

	/**
	 * @generated
	 */
	private IParser getForeignKeyName_6002Parser() {
		if (foreignKeyName_6002Parser == null) {
			EAttribute[] features = new EAttribute[] { ESchemaPackage.eINSTANCE
					.getDataModelerNamedElement_Name() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(6002, features);
			foreignKeyName_6002Parser = parser;
		}
		return foreignKeyName_6002Parser;
	}

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser foreignKeyBaseCardinalityBaseRole_6005Parser;

	/**
	 * @generated
	 */
	private IParser getForeignKeyBaseCardinalityBaseRole_6005Parser() {
		if (foreignKeyBaseCardinalityBaseRole_6005Parser == null) {
			EAttribute[] features = new EAttribute[] {
					EConstraintsPackage.eINSTANCE
							.getForeignKey_BaseCardinality(),
					EConstraintsPackage.eINSTANCE.getForeignKey_BaseRole() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(6005, features);
			parser.setViewPattern("{0}\n{1} "); //$NON-NLS-1$
			parser.setEditorPattern("{0}\n{1} "); //$NON-NLS-1$
			parser.setEditPattern("{0}\n{1} "); //$NON-NLS-1$
			foreignKeyBaseCardinalityBaseRole_6005Parser = parser;
		}
		return foreignKeyBaseCardinalityBaseRole_6005Parser;
	}

	/**
	 * (Modified template: ParserProvider.xpt)
	 * @generated
	 */
	private IParser foreignKeyParentCardinalityParentRole_6006Parser;

	/**
	 * @generated
	 */
	private IParser getForeignKeyParentCardinalityParentRole_6006Parser() {
		if (foreignKeyParentCardinalityParentRole_6006Parser == null) {
			EAttribute[] features = new EAttribute[] {
					EConstraintsPackage.eINSTANCE
							.getForeignKey_ParentCardinality(),
					EConstraintsPackage.eINSTANCE.getForeignKey_ParentRole() };
			MessageFormatParser parser = DataModelerMessageFormatParserProvider
					.getParser(6006, features);
			parser.setViewPattern("{0}\n{1} "); //$NON-NLS-1$
			parser.setEditorPattern("{0}\n{1} "); //$NON-NLS-1$
			parser.setEditPattern("{0}\n{1} "); //$NON-NLS-1$
			foreignKeyParentCardinalityParentRole_6006Parser = parser;
		}
		return foreignKeyParentCardinalityParentRole_6006Parser;
	}

	/**
	 * @generated
	 */
	protected IParser getParser(int visualID) {
		switch (visualID) {
		case PersistentTableNameEditPart.VISUAL_ID:
			return getPersistentTableName_5004Parser();
		case ColumnEditPart.VISUAL_ID:
			return getColumn_3001Parser();
		case UniqueConstraintEditPart.VISUAL_ID:
			return getUniqueConstraint_3002Parser();
		case ForeignKey2EditPart.VISUAL_ID:
			return getForeignKey_3003Parser();
		case RelationshipForeignKeyEditPart.VISUAL_ID:
			return getForeignKeyName_6001Parser();
		case ForeignKeyBaseRoleEditPart.VISUAL_ID:
			return getForeignKeyBaseCardinalityBaseRole_6003Parser();
		case ForeignKeyParentRoleEditPart.VISUAL_ID:
			return getForeignKeyParentCardinalityParentRole_6004Parser();
		case ForeignKeyNameEditPart.VISUAL_ID:
			return getForeignKeyName_6002Parser();
		case ForeignKeyBaseCardinalityBaseRoleEditPart.VISUAL_ID:
			return getForeignKeyBaseCardinalityBaseRole_6005Parser();
		case ForeignKeyParentCardinalityParentEditPart.VISUAL_ID:
			return getForeignKeyParentCardinalityParentRole_6006Parser();
		}
		return null;
	}

	/**
	 * Utility method that consults ParserService
	 * @generated
	 */
	public static IParser getParser(IElementType type, EObject object,
			String parserHint) {
		return ParserService.getInstance().getParser(
				new HintAdapter(type, object, parserHint));
	}

	/**
	 * @generated
	 */
	public IParser getParser(IAdaptable hint) {
		String vid = (String) hint.getAdapter(String.class);
		if (vid != null) {
			return getParser(DatamodelerVisualIDRegistry.getVisualID(vid));
		}
		View view = (View) hint.getAdapter(View.class);
		if (view != null) {
			return getParser(DatamodelerVisualIDRegistry.getVisualID(view));
		}
		return null;
	}

	/**
	 * @generated
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof GetParserOperation) {
			IAdaptable hint = ((GetParserOperation) operation).getHint();
			if (DatamodelerElementTypes.getElement(hint) == null) {
				return false;
			}
			return getParser(hint) != null;
		}
		return false;
	}

	/**
	 * @generated
	 */
	private static class HintAdapter extends ParserHintAdapter {

		/**
		 * @generated
		 */
		private final IElementType elementType;

		/**
		 * @generated
		 */
		public HintAdapter(IElementType type, EObject object, String parserHint) {
			super(object, parserHint);
			assert type != null;
			elementType = type;
		}

		/**
		 * @generated
		 */
		public Object getAdapter(Class adapter) {
			if (IElementType.class.equals(adapter)) {
				return elementType;
			}
			return super.getAdapter(adapter);
		}
	}

}
