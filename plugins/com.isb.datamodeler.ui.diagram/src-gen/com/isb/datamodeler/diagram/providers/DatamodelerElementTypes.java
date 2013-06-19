package com.isb.datamodeler.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey2EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey3EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.edit.parts.UniqueConstraintEditPart;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.tables.ETablesPackage;

/**
 * @generated
 */
public class DatamodelerElementTypes {

	/**
	 * @generated
	 */
	private DatamodelerElementTypes() {
	}

	/**
	 * @generated
	 */
	private static Map<IElementType, ENamedElement> elements;

	/**
	 * @generated
	 */
	private static ImageRegistry imageRegistry;

	/**
	 * @generated
	 */
	private static Set<IElementType> KNOWN_ELEMENT_TYPES;

	/**
	 * @generated
	 */
	public static final IElementType Schema_1000 = getElementType("com.isb.datamodeler.ui.diagram.Schema_1000"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType PersistentTable_2009 = getElementType("com.isb.datamodeler.ui.diagram.PersistentTable_2009"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType Column_3001 = getElementType("com.isb.datamodeler.ui.diagram.Column_3001"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType UniqueConstraint_3002 = getElementType("com.isb.datamodeler.ui.diagram.UniqueConstraint_3002"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType ForeignKey_3003 = getElementType("com.isb.datamodeler.ui.diagram.ForeignKey_3003"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType ForeignKey_4001 = getElementType("com.isb.datamodeler.ui.diagram.ForeignKey_4001"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType ForeignKey_4002 = getElementType("com.isb.datamodeler.ui.diagram.ForeignKey_4002"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	private static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
		}
		return imageRegistry;
	}

	/**
	 * @generated
	 */
	private static String getImageRegistryKey(ENamedElement element) {
		return element.getName();
	}

	/**
	 * @generated
	 */
	private static ImageDescriptor getProvidedImageDescriptor(
			ENamedElement element) {
		if (element instanceof EStructuralFeature) {
			EStructuralFeature feature = ((EStructuralFeature) element);
			EClass eContainingClass = feature.getEContainingClass();
			EClassifier eType = feature.getEType();
			if (eContainingClass != null && !eContainingClass.isAbstract()) {
				element = eContainingClass;
			} else if (eType instanceof EClass
					&& !((EClass) eType).isAbstract()) {
				element = eType;
			}
		}
		if (element instanceof EClass) {
			EClass eClass = (EClass) element;
			if (!eClass.isAbstract()) {
				return DatamodelerDiagramEditorPlugin.getInstance()
						.getItemImageDescriptor(
								eClass.getEPackage().getEFactoryInstance()
										.create(eClass));
			}
		}
		// TODO : support structural features
		return null;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(ENamedElement element) {
		String key = getImageRegistryKey(element);
		ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
		if (imageDescriptor == null) {
			imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
		}
		return imageDescriptor;
	}

	/**
	 * @generated
	 */
	public static Image getImage(ENamedElement element) {
		String key = getImageRegistryKey(element);
		Image image = getImageRegistry().get(key);
		if (image == null) {
			ImageDescriptor imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
			image = getImageRegistry().get(key);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImageDescriptor(element);
	}

	/**
	 * @generated
	 */
	public static Image getImage(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImage(element);
	}

	/**
	 * Returns 'type' of the ecore object associated with the hint.
	 * 
	 * @generated
	 */
	public static ENamedElement getElement(IAdaptable hint) {
		Object type = hint.getAdapter(IElementType.class);
		if (elements == null) {
			elements = new IdentityHashMap<IElementType, ENamedElement>();

			elements.put(Schema_1000, ESchemaPackage.eINSTANCE.getSchema());

			elements.put(PersistentTable_2009,
					ETablesPackage.eINSTANCE.getPersistentTable());

			elements.put(Column_3001, ETablesPackage.eINSTANCE.getColumn());

			elements.put(UniqueConstraint_3002,
					EConstraintsPackage.eINSTANCE.getUniqueConstraint());

			elements.put(ForeignKey_3003,
					EConstraintsPackage.eINSTANCE.getForeignKey());

			elements.put(ForeignKey_4001,
					EConstraintsPackage.eINSTANCE.getForeignKey());

			elements.put(ForeignKey_4002,
					EConstraintsPackage.eINSTANCE.getForeignKey());
		}
		return (ENamedElement) elements.get(type);
	}

	/**
	 * @generated
	 */
	private static IElementType getElementType(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}

	/**
	 * @generated
	 */
	public static boolean isKnownElementType(IElementType elementType) {
		if (KNOWN_ELEMENT_TYPES == null) {
			KNOWN_ELEMENT_TYPES = new HashSet<IElementType>();
			KNOWN_ELEMENT_TYPES.add(Schema_1000);
			KNOWN_ELEMENT_TYPES.add(PersistentTable_2009);
			KNOWN_ELEMENT_TYPES.add(Column_3001);
			KNOWN_ELEMENT_TYPES.add(UniqueConstraint_3002);
			KNOWN_ELEMENT_TYPES.add(ForeignKey_3003);
			KNOWN_ELEMENT_TYPES.add(ForeignKey_4001);
			KNOWN_ELEMENT_TYPES.add(ForeignKey_4002);
		}
		return KNOWN_ELEMENT_TYPES.contains(elementType);
	}

	/**
	 * @generated
	 */
	public static IElementType getElementType(int visualID) {
		switch (visualID) {
		case SchemaEditPart.VISUAL_ID:
			return Schema_1000;
		case PersistentTableEditPart.VISUAL_ID:
			return PersistentTable_2009;
		case ColumnEditPart.VISUAL_ID:
			return Column_3001;
		case UniqueConstraintEditPart.VISUAL_ID:
			return UniqueConstraint_3002;
		case ForeignKey2EditPart.VISUAL_ID:
			return ForeignKey_3003;
		case ForeignKeyEditPart.VISUAL_ID:
			return ForeignKey_4001;
		case ForeignKey3EditPart.VISUAL_ID:
			return ForeignKey_4002;
		}
		return null;
	}

}
