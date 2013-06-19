package com.isb.datamodeler.diagram.navigator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserOptions;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

import com.isb.datamodeler.diagram.edit.parts.ColumnEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey2EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKey3EditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.ForeignKeyNameEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart;
import com.isb.datamodeler.diagram.edit.parts.PersistentTableNameEditPart;
import com.isb.datamodeler.diagram.edit.parts.RelationshipForeignKeyEditPart;
import com.isb.datamodeler.diagram.edit.parts.SchemaEditPart;
import com.isb.datamodeler.diagram.edit.parts.UniqueConstraintEditPart;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;
import com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry;
import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;
import com.isb.datamodeler.diagram.providers.DatamodelerParserProvider;
import com.isb.datamodeler.schema.ESchema;

/**
 * @generated
 */
public class DatamodelerNavigatorLabelProvider extends LabelProvider implements
		ICommonLabelProvider, ITreePathLabelProvider {

	/**
	 * @generated
	 */
	static {
		DatamodelerDiagramEditorPlugin
				.getInstance()
				.getImageRegistry()
				.put("Navigator?UnknownElement", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
		DatamodelerDiagramEditorPlugin
				.getInstance()
				.getImageRegistry()
				.put("Navigator?ImageNotFound", ImageDescriptor.getMissingImageDescriptor()); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public void updateLabel(ViewerLabel label, TreePath elementPath) {
		Object element = elementPath.getLastSegment();
		if (element instanceof DatamodelerNavigatorItem
				&& !isOwnView(((DatamodelerNavigatorItem) element).getView())) {
			return;
		}
		label.setText(getText(element));
		label.setImage(getImage(element));
	}

	/**
	 * @generated
	 */
	public Image getImage(Object element) {
		if (element instanceof DatamodelerNavigatorGroup) {
			DatamodelerNavigatorGroup group = (DatamodelerNavigatorGroup) element;
			return DatamodelerDiagramEditorPlugin.getInstance()
					.getBundledImage(group.getIcon());
		}

		if (element instanceof DatamodelerNavigatorItem) {
			DatamodelerNavigatorItem navigatorItem = (DatamodelerNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return super.getImage(element);
			}
			return getImage(navigatorItem.getView());
		}

		// Due to plugin.xml content will be called only for "own" views
		if (element instanceof IAdaptable) {
			View view = (View) ((IAdaptable) element).getAdapter(View.class);
			if (view != null && isOwnView(view)) {
				return getImage(view);
			}
		}

		return super.getImage(element);
	}

	/**
	 * (Modified template: NavigatorLabelProvider.xpt)
	 * @generated
	 */
	public Image getImage(View view) {

		EObject element = view.getElement();

		ImageDescriptor imageDescriptor = DatamodelerDiagramEditorPlugin
				.getInstance().getItemImageDescriptor(element);

		if (imageDescriptor != null)
			return imageDescriptor.createImage();

		return getImage("Navigator?UnknownElement", null); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	private Image getImage(String key, IElementType elementType) {
		ImageRegistry imageRegistry = DatamodelerDiagramEditorPlugin
				.getInstance().getImageRegistry();
		Image image = imageRegistry.get(key);
		if (image == null && elementType != null
				&& DatamodelerElementTypes.isKnownElementType(elementType)) {
			image = DatamodelerElementTypes.getImage(elementType);
			imageRegistry.put(key, image);
		}

		if (image == null) {
			image = imageRegistry.get("Navigator?ImageNotFound"); //$NON-NLS-1$
			imageRegistry.put(key, image);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public String getText(Object element) {
		if (element instanceof DatamodelerNavigatorGroup) {
			DatamodelerNavigatorGroup group = (DatamodelerNavigatorGroup) element;
			return group.getGroupName();
		}

		if (element instanceof DatamodelerNavigatorItem) {
			DatamodelerNavigatorItem navigatorItem = (DatamodelerNavigatorItem) element;
			if (!isOwnView(navigatorItem.getView())) {
				return null;
			}
			return getText(navigatorItem.getView());
		}

		// Due to plugin.xml content will be called only for "own" views
		if (element instanceof IAdaptable) {
			View view = (View) ((IAdaptable) element).getAdapter(View.class);
			if (view != null && isOwnView(view)) {
				return getText(view);
			}
		}

		return super.getText(element);
	}

	/**
	 * @generated
	 */
	public String getText(View view) {
		if (view.getElement() != null && view.getElement().eIsProxy()) {
			return getUnresolvedDomainElementProxyText(view);
		}
		switch (DatamodelerVisualIDRegistry.getVisualID(view)) {
		case UniqueConstraintEditPart.VISUAL_ID:
			return getUniqueConstraint_3002Text(view);
		case ForeignKey2EditPart.VISUAL_ID:
			return getForeignKey_3003Text(view);
		case ColumnEditPart.VISUAL_ID:
			return getColumn_3001Text(view);
		case ForeignKey3EditPart.VISUAL_ID:
			return getForeignKey_4002Text(view);
		case SchemaEditPart.VISUAL_ID:
			return getSchema_1000Text(view);
		case PersistentTableEditPart.VISUAL_ID:
			return getPersistentTable_2009Text(view);
		case ForeignKeyEditPart.VISUAL_ID:
			return getForeignKey_4001Text(view);
		}
		return getUnknownElementText(view);
	}

	/**
	 * @generated
	 */
	private String getPersistentTable_2009Text(View view) {
		IParser parser = DatamodelerParserProvider.getParser(
				DatamodelerElementTypes.PersistentTable_2009,
				view.getElement() != null ? view.getElement() : view,
				DatamodelerVisualIDRegistry
						.getType(PersistentTableNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 5004); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getUniqueConstraint_3002Text(View view) {
		IParser parser = DatamodelerParserProvider.getParser(
				DatamodelerElementTypes.UniqueConstraint_3002, view
						.getElement() != null ? view.getElement() : view,
				DatamodelerVisualIDRegistry
						.getType(UniqueConstraintEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 3002); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getForeignKey_3003Text(View view) {
		IParser parser = DatamodelerParserProvider.getParser(
				DatamodelerElementTypes.ForeignKey_3003,
				view.getElement() != null ? view.getElement() : view,
				DatamodelerVisualIDRegistry
						.getType(ForeignKey2EditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 3003); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getColumn_3001Text(View view) {
		IParser parser = DatamodelerParserProvider.getParser(
				DatamodelerElementTypes.Column_3001,
				view.getElement() != null ? view.getElement() : view,
				DatamodelerVisualIDRegistry.getType(ColumnEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 3001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getSchema_1000Text(View view) {
		ESchema domainModelElement = (ESchema) view.getElement();
		if (domainModelElement != null) {
			return domainModelElement.getName();
		} else {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"No domain element for view with visualID = " + 1000); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getForeignKey_4002Text(View view) {
		IParser parser = DatamodelerParserProvider.getParser(
				DatamodelerElementTypes.ForeignKey_4002,
				view.getElement() != null ? view.getElement() : view,
				DatamodelerVisualIDRegistry
						.getType(ForeignKeyNameEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 6002); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getForeignKey_4001Text(View view) {
		IParser parser = DatamodelerParserProvider.getParser(
				DatamodelerElementTypes.ForeignKey_4001,
				view.getElement() != null ? view.getElement() : view,
				DatamodelerVisualIDRegistry
						.getType(RelationshipForeignKeyEditPart.VISUAL_ID));
		if (parser != null) {
			return parser.getPrintString(new EObjectAdapter(
					view.getElement() != null ? view.getElement() : view),
					ParserOptions.NONE.intValue());
		} else {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Parser was not found for label " + 6001); //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * @generated
	 */
	private String getUnknownElementText(View view) {
		return "<UnknownElement Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	private String getUnresolvedDomainElementProxyText(View view) {
		return "<Unresolved domain element Visual_ID = " + view.getType() + ">"; //$NON-NLS-1$  //$NON-NLS-2$
	}

	/**
	 * @generated
	 */
	public void init(ICommonContentExtensionSite aConfig) {
	}

	/**
	 * @generated
	 */
	public void restoreState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public void saveState(IMemento aMemento) {
	}

	/**
	 * @generated
	 */
	public String getDescription(Object anElement) {
		return null;
	}

	/**
	 * @generated
	 */
	private boolean isOwnView(View view) {
		return SchemaEditPart.MODEL_ID.equals(DatamodelerVisualIDRegistry
				.getModelID(view));
	}

}
