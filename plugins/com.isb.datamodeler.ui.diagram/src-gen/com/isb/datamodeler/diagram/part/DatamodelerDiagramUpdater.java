package com.isb.datamodeler.diagram.part;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.constraints.ETableConstraint;
import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey2EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey3EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableColumnCompartmentEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableConstraintsCompartmentEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.edit.parts.UniqueConstraintEditPart;
import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.tables.EBaseTable;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.tables.ETable;

/**
 * @generated
 */
public class DatamodelerDiagramUpdater {

	/**
	 * @generated
	 */
	public static boolean isShortcutOrphaned(View view) {
		return !view.isSetElement() || view.getElement() == null
				|| view.getElement().eIsProxy();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerNodeDescriptor> getSemanticChildren(View view) {
		switch (DatamodelerVisualIDRegistry.getVisualID(view)) {
		case SchemaEditPart.VISUAL_ID:
			return getSchema_1000SemanticChildren(view);
		case PersistentTableColumnCompartmentEditPart.VISUAL_ID:
			return getPersistentTableColumnCompartment_7001SemanticChildren(view);
		case PersistentTableConstraintsCompartmentEditPart.VISUAL_ID:
			return getPersistentTableConstraintsCompartment_7002SemanticChildren(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerNodeDescriptor> getSchema_1000SemanticChildren(
			View view) {
		if (!view.isSetElement()) {
			return Collections.emptyList();
		}
		ESchema modelElement = (ESchema) view.getElement();
		LinkedList<DatamodelerNodeDescriptor> result = new LinkedList<DatamodelerNodeDescriptor>();
		for (Iterator<?> it = modelElement.getTables().iterator(); it.hasNext();) {
			ETable childElement = (ETable) it.next();
			int visualID = DatamodelerVisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == PersistentTableEditPart.VISUAL_ID) {
				result.add(new DatamodelerNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerNodeDescriptor> getPersistentTableColumnCompartment_7001SemanticChildren(
			View view) {
		if (false == view.eContainer() instanceof View) {
			return Collections.emptyList();
		}
		View containerView = (View) view.eContainer();
		if (!containerView.isSetElement()) {
			return Collections.emptyList();
		}
		EPersistentTable modelElement = (EPersistentTable) containerView
				.getElement();
		LinkedList<DatamodelerNodeDescriptor> result = new LinkedList<DatamodelerNodeDescriptor>();
		for (Iterator<?> it = modelElement.getColumns().iterator(); it
				.hasNext();) {
			EColumn childElement = (EColumn) it.next();
			int visualID = DatamodelerVisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == ColumnEditPart.VISUAL_ID) {
				result.add(new DatamodelerNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerNodeDescriptor> getPersistentTableConstraintsCompartment_7002SemanticChildren(
			View view) {
		if (false == view.eContainer() instanceof View) {
			return Collections.emptyList();
		}
		View containerView = (View) view.eContainer();
		if (!containerView.isSetElement()) {
			return Collections.emptyList();
		}
		EPersistentTable modelElement = (EPersistentTable) containerView
				.getElement();
		LinkedList<DatamodelerNodeDescriptor> result = new LinkedList<DatamodelerNodeDescriptor>();
		for (Iterator<?> it = modelElement.getConstraints().iterator(); it
				.hasNext();) {
			ETableConstraint childElement = (ETableConstraint) it.next();
			int visualID = DatamodelerVisualIDRegistry.getNodeVisualID(view,
					childElement);
			if (visualID == UniqueConstraintEditPart.VISUAL_ID) {
				result.add(new DatamodelerNodeDescriptor(childElement, visualID));
				continue;
			}
			if (visualID == ForeignKey2EditPart.VISUAL_ID) {
				result.add(new DatamodelerNodeDescriptor(childElement, visualID));
				continue;
			}
		}
		return result;
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getContainedLinks(View view) {
		switch (DatamodelerVisualIDRegistry.getVisualID(view)) {
		case SchemaEditPart.VISUAL_ID:
			return getSchema_1000ContainedLinks(view);
		case PersistentTableEditPart.VISUAL_ID:
			return getPersistentTable_2009ContainedLinks(view);
		case ColumnEditPart.VISUAL_ID:
			return getColumn_3001ContainedLinks(view);
		case UniqueConstraintEditPart.VISUAL_ID:
			return getUniqueConstraint_3002ContainedLinks(view);
		case ForeignKey2EditPart.VISUAL_ID:
			return getForeignKey_3003ContainedLinks(view);
		case ForeignKeyEditPart.VISUAL_ID:
			return getForeignKey_4001ContainedLinks(view);
		case ForeignKey3EditPart.VISUAL_ID:
			return getForeignKey_4002ContainedLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getIncomingLinks(View view) {
		switch (DatamodelerVisualIDRegistry.getVisualID(view)) {
		case PersistentTableEditPart.VISUAL_ID:
			return getPersistentTable_2009IncomingLinks(view);
		case ColumnEditPart.VISUAL_ID:
			return getColumn_3001IncomingLinks(view);
		case UniqueConstraintEditPart.VISUAL_ID:
			return getUniqueConstraint_3002IncomingLinks(view);
		case ForeignKey2EditPart.VISUAL_ID:
			return getForeignKey_3003IncomingLinks(view);
		case ForeignKeyEditPart.VISUAL_ID:
			return getForeignKey_4001IncomingLinks(view);
		case ForeignKey3EditPart.VISUAL_ID:
			return getForeignKey_4002IncomingLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getOutgoingLinks(View view) {
		switch (DatamodelerVisualIDRegistry.getVisualID(view)) {
		case PersistentTableEditPart.VISUAL_ID:
			return getPersistentTable_2009OutgoingLinks(view);
		case ColumnEditPart.VISUAL_ID:
			return getColumn_3001OutgoingLinks(view);
		case UniqueConstraintEditPart.VISUAL_ID:
			return getUniqueConstraint_3002OutgoingLinks(view);
		case ForeignKey2EditPart.VISUAL_ID:
			return getForeignKey_3003OutgoingLinks(view);
		case ForeignKeyEditPart.VISUAL_ID:
			return getForeignKey_4001OutgoingLinks(view);
		case ForeignKey3EditPart.VISUAL_ID:
			return getForeignKey_4002OutgoingLinks(view);
		}
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getSchema_1000ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getPersistentTable_2009ContainedLinks(
			View view) {
		EPersistentTable modelElement = (EPersistentTable) view.getElement();
		LinkedList<DatamodelerLinkDescriptor> result = new LinkedList<DatamodelerLinkDescriptor>();
		result.addAll(getContainedTypeModelFacetLinks_ForeignKey_4001(modelElement));
		result.addAll(getContainedTypeModelFacetLinks_ForeignKey_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getColumn_3001ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getUniqueConstraint_3002ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getForeignKey_3003ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getForeignKey_4001ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getForeignKey_4002ContainedLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getPersistentTable_2009IncomingLinks(
			View view) {
		EPersistentTable modelElement = (EPersistentTable) view.getElement();
		Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.CrossReferencer
				.find(view.eResource().getResourceSet().getResources());
		LinkedList<DatamodelerLinkDescriptor> result = new LinkedList<DatamodelerLinkDescriptor>();
		result.addAll(getIncomingTypeModelFacetLinks_ForeignKey_4001(
				modelElement, crossReferences));
		result.addAll(getIncomingTypeModelFacetLinks_ForeignKey_4002(
				modelElement, crossReferences));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getColumn_3001IncomingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getUniqueConstraint_3002IncomingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getForeignKey_3003IncomingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getForeignKey_4001IncomingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getForeignKey_4002IncomingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getPersistentTable_2009OutgoingLinks(
			View view) {
		EPersistentTable modelElement = (EPersistentTable) view.getElement();
		LinkedList<DatamodelerLinkDescriptor> result = new LinkedList<DatamodelerLinkDescriptor>();
		result.addAll(getContainedTypeModelFacetLinks_ForeignKey_4001(modelElement));
		result.addAll(getContainedTypeModelFacetLinks_ForeignKey_4002(modelElement));
		return result;
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getColumn_3001OutgoingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getUniqueConstraint_3002OutgoingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getForeignKey_3003OutgoingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getForeignKey_4001OutgoingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	public static List<DatamodelerLinkDescriptor> getForeignKey_4002OutgoingLinks(
			View view) {
		return Collections.emptyList();
	}

	/**
	 * @generated
	 */
	private static Collection<DatamodelerLinkDescriptor> getContainedTypeModelFacetLinks_ForeignKey_4001(
			EBaseTable container) {
		LinkedList<DatamodelerLinkDescriptor> result = new LinkedList<DatamodelerLinkDescriptor>();
		for (Iterator<?> links = container.getConstraints().iterator(); links
				.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof EForeignKey) {
				continue;
			}
			EForeignKey link = (EForeignKey) linkObject;
			if (ForeignKeyEditPart.VISUAL_ID != DatamodelerVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			EBaseTable dst = link.getParentTable();
			result.add(new DatamodelerLinkDescriptor(container, dst, link,
					DatamodelerElementTypes.ForeignKey_4001,
					ForeignKeyEditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<DatamodelerLinkDescriptor> getContainedTypeModelFacetLinks_ForeignKey_4002(
			EBaseTable container) {
		LinkedList<DatamodelerLinkDescriptor> result = new LinkedList<DatamodelerLinkDescriptor>();
		for (Iterator<?> links = container.getConstraints().iterator(); links
				.hasNext();) {
			EObject linkObject = (EObject) links.next();
			if (false == linkObject instanceof EForeignKey) {
				continue;
			}
			EForeignKey link = (EForeignKey) linkObject;
			if (ForeignKey3EditPart.VISUAL_ID != DatamodelerVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			EBaseTable dst = link.getParentTable();
			result.add(new DatamodelerLinkDescriptor(container, dst, link,
					DatamodelerElementTypes.ForeignKey_4002,
					ForeignKey3EditPart.VISUAL_ID));
		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<DatamodelerLinkDescriptor> getIncomingTypeModelFacetLinks_ForeignKey_4001(
			EBaseTable target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<DatamodelerLinkDescriptor> result = new LinkedList<DatamodelerLinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences
				.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() != EConstraintsPackage.eINSTANCE
					.getForeignKey_ParentTable()
					|| false == setting.getEObject() instanceof EForeignKey) {
				continue;
			}
			EForeignKey link = (EForeignKey) setting.getEObject();
			if (ForeignKeyEditPart.VISUAL_ID != DatamodelerVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			if (false == link.eContainer() instanceof EBaseTable) {
				continue;
			}
			EBaseTable container = (EBaseTable) link.eContainer();
			result.add(new DatamodelerLinkDescriptor(container, target, link,
					DatamodelerElementTypes.ForeignKey_4001,
					ForeignKeyEditPart.VISUAL_ID));

		}
		return result;
	}

	/**
	 * @generated
	 */
	private static Collection<DatamodelerLinkDescriptor> getIncomingTypeModelFacetLinks_ForeignKey_4002(
			EBaseTable target,
			Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences) {
		LinkedList<DatamodelerLinkDescriptor> result = new LinkedList<DatamodelerLinkDescriptor>();
		Collection<EStructuralFeature.Setting> settings = crossReferences
				.get(target);
		for (EStructuralFeature.Setting setting : settings) {
			if (setting.getEStructuralFeature() != EConstraintsPackage.eINSTANCE
					.getForeignKey_ParentTable()
					|| false == setting.getEObject() instanceof EForeignKey) {
				continue;
			}
			EForeignKey link = (EForeignKey) setting.getEObject();
			if (ForeignKey3EditPart.VISUAL_ID != DatamodelerVisualIDRegistry
					.getLinkWithClassVisualID(link)) {
				continue;
			}
			if (false == link.eContainer() instanceof EBaseTable) {
				continue;
			}
			EBaseTable container = (EBaseTable) link.eContainer();
			result.add(new DatamodelerLinkDescriptor(container, target, link,
					DatamodelerElementTypes.ForeignKey_4002,
					ForeignKey3EditPart.VISUAL_ID));

		}
		return result;
	}

}
