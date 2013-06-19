package com.isb.datamodeler.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey2EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey3EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyBaseCardinalityBaseRoleEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyBaseRoleEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyNameEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyParentCardinalityParentEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyParentRoleEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableColumnCompartmentEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableConstraintsCompartmentEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableNameEditPart;
import com.isb.datamodeler.diagram.edit.parts.RelationshipForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.edit.parts.UniqueConstraintEditPart;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.tables.ETablesPackage;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class DatamodelerVisualIDRegistry {

	/**
	 * @generated
	 */
	private static final String DEBUG_KEY = "com.isb.datamodeler.ui.diagram/debug/visualID"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static int getVisualID(View view) {
		if (view instanceof Diagram) {
			if (SchemaEditPart.MODEL_ID.equals(view.getType())) {
				return SchemaEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		return com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry
				.getVisualID(view.getType());
	}

	/**
	 * @generated
	 */
	public static String getModelID(View view) {
		View diagram = view.getDiagram();
		while (view != diagram) {
			EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
			if (annotation != null) {
				return (String) annotation.getDetails().get("modelID"); //$NON-NLS-1$
			}
			view = (View) view.eContainer();
		}
		return diagram != null ? diagram.getType() : null;
	}

	/**
	 * @generated
	 */
	public static int getVisualID(String type) {
		try {
			return Integer.parseInt(type);
		} catch (NumberFormatException e) {
			if (Boolean.TRUE.toString().equalsIgnoreCase(
					Platform.getDebugOption(DEBUG_KEY))) {
				DatamodelerDiagramEditorPlugin.getInstance().logError(
						"Unable to parse view type as a visualID number: "
								+ type);
			}
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static String getType(int visualID) {
		return Integer.toString(visualID);
	}

	/**
	 * @generated
	 */
	public static int getDiagramVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (ESchemaPackage.eINSTANCE.getSchema().isSuperTypeOf(
				domainElement.eClass())
				&& isDiagram((ESchema) domainElement)) {
			return SchemaEditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static int getNodeVisualID(View containerView, EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		String containerModelID = com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry
				.getModelID(containerView);
		if (!SchemaEditPart.MODEL_ID.equals(containerModelID)
				&& !"Datamodeler".equals(containerModelID)) { //$NON-NLS-1$
			return -1;
		}
		int containerVisualID;
		if (SchemaEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry
					.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = SchemaEditPart.VISUAL_ID;
			} else {
				return -1;
			}
		}
		switch (containerVisualID) {
		case SchemaEditPart.VISUAL_ID:
			if (ETablesPackage.eINSTANCE.getPersistentTable().isSuperTypeOf(
					domainElement.eClass())) {
				return PersistentTableEditPart.VISUAL_ID;
			}
			break;
		case PersistentTableColumnCompartmentEditPart.VISUAL_ID:
			if (ETablesPackage.eINSTANCE.getColumn().isSuperTypeOf(
					domainElement.eClass())) {
				return ColumnEditPart.VISUAL_ID;
			}
			break;
		case PersistentTableConstraintsCompartmentEditPart.VISUAL_ID:
			if (EConstraintsPackage.eINSTANCE.getUniqueConstraint()
					.isSuperTypeOf(domainElement.eClass())) {
				return UniqueConstraintEditPart.VISUAL_ID;
			}
			if (EConstraintsPackage.eINSTANCE.getForeignKey().isSuperTypeOf(
					domainElement.eClass())) {
				return ForeignKey2EditPart.VISUAL_ID;
			}
			break;
		}
		return -1;
	}

	/**
	 * @generated
	 */
	public static boolean canCreateNode(View containerView, int nodeVisualID) {
		String containerModelID = com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry
				.getModelID(containerView);
		if (!SchemaEditPart.MODEL_ID.equals(containerModelID)
				&& !"Datamodeler".equals(containerModelID)) { //$NON-NLS-1$
			return false;
		}
		int containerVisualID;
		if (SchemaEditPart.MODEL_ID.equals(containerModelID)) {
			containerVisualID = com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry
					.getVisualID(containerView);
		} else {
			if (containerView instanceof Diagram) {
				containerVisualID = SchemaEditPart.VISUAL_ID;
			} else {
				return false;
			}
		}
		switch (containerVisualID) {
		case SchemaEditPart.VISUAL_ID:
			if (PersistentTableEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case PersistentTableEditPart.VISUAL_ID:
			if (PersistentTableNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (PersistentTableColumnCompartmentEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (PersistentTableConstraintsCompartmentEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case PersistentTableColumnCompartmentEditPart.VISUAL_ID:
			if (ColumnEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case PersistentTableConstraintsCompartmentEditPart.VISUAL_ID:
			if (UniqueConstraintEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ForeignKey2EditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ForeignKeyEditPart.VISUAL_ID:
			if (RelationshipForeignKeyEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ForeignKeyBaseRoleEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ForeignKeyParentRoleEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		case ForeignKey3EditPart.VISUAL_ID:
			if (ForeignKeyNameEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ForeignKeyBaseCardinalityBaseRoleEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			if (ForeignKeyParentCardinalityParentEditPart.VISUAL_ID == nodeVisualID) {
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * @generated
	 */
	public static int getLinkWithClassVisualID(EObject domainElement) {
		if (domainElement == null) {
			return -1;
		}
		if (EConstraintsPackage.eINSTANCE.getForeignKey().isSuperTypeOf(
				domainElement.eClass())
				&& isForeignKey_4001((EForeignKey) domainElement)) {
			return ForeignKeyEditPart.VISUAL_ID;
		}
		if (EConstraintsPackage.eINSTANCE.getForeignKey().isSuperTypeOf(
				domainElement.eClass())
				&& isForeignKey_4002((EForeignKey) domainElement)) {
			return ForeignKey3EditPart.VISUAL_ID;
		}
		return -1;
	}

	/**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
	private static boolean isDiagram(ESchema element) {
		return true;
	}

	/**
	 * @generated
	 */
	private static boolean isForeignKey_4001(EForeignKey domainElement) {
		return domainElement.isDefaultIdentifying();
	}

	/**
	 * @generated
	 */
	private static boolean isForeignKey_4002(EForeignKey domainElement) {
		return !domainElement.isDefaultIdentifying();
	}

}
