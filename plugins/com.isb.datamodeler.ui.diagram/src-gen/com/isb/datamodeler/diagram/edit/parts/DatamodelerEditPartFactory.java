package com.isb.datamodeler.diagram.edit.parts;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry;

/**
 * @generated
 */
public class DatamodelerEditPartFactory implements EditPartFactory {

	/**
	 * @generated
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof View) {
			View view = (View) model;
			switch (DatamodelerVisualIDRegistry.getVisualID(view)) {

			case SchemaEditPart.VISUAL_ID:
				return new SchemaEditPart(view);

			case PersistentTableEditPart.VISUAL_ID:
				return new PersistentTableEditPart(view);

			case PersistentTableNameEditPart.VISUAL_ID:
				return new PersistentTableNameEditPart(view);

			case ColumnEditPart.VISUAL_ID:
				return new ColumnEditPart(view);

			case UniqueConstraintEditPart.VISUAL_ID:
				return new UniqueConstraintEditPart(view);

			case ForeignKey2EditPart.VISUAL_ID:
				return new ForeignKey2EditPart(view);

			case PersistentTableColumnCompartmentEditPart.VISUAL_ID:
				return new PersistentTableColumnCompartmentEditPart(view);

			case PersistentTableConstraintsCompartmentEditPart.VISUAL_ID:
				return new PersistentTableConstraintsCompartmentEditPart(view);

			case ForeignKeyEditPart.VISUAL_ID:
				return new ForeignKeyEditPart(view);

			case RelationshipForeignKeyEditPart.VISUAL_ID:
				return new RelationshipForeignKeyEditPart(view);

			case ForeignKeyBaseRoleEditPart.VISUAL_ID:
				return new ForeignKeyBaseRoleEditPart(view);

			case ForeignKeyParentRoleEditPart.VISUAL_ID:
				return new ForeignKeyParentRoleEditPart(view);

			case ForeignKey3EditPart.VISUAL_ID:
				return new ForeignKey3EditPart(view);

			case ForeignKeyNameEditPart.VISUAL_ID:
				return new ForeignKeyNameEditPart(view);

			case ForeignKeyBaseCardinalityBaseRoleEditPart.VISUAL_ID:
				return new ForeignKeyBaseCardinalityBaseRoleEditPart(view);

			case ForeignKeyParentCardinalityParentEditPart.VISUAL_ID:
				return new ForeignKeyParentCardinalityParentEditPart(view);

			}
		}
		return createUnrecognizedEditPart(context, model);
	}

	/**
	 * @generated
	 */
	private EditPart createUnrecognizedEditPart(EditPart context, Object model) {
		// Handle creation of unrecognized child node EditParts here
		return null;
	}

	/**
	 * @generated
	 */
	public static CellEditorLocator getTextCellEditorLocator(
			ITextAwareEditPart source) {
		if (source.getFigure() instanceof WrappingLabel)
			return new TextCellEditorLocator((WrappingLabel) source.getFigure());
		else {
			return new LabelCellEditorLocator((Label) source.getFigure());
		}
	}

	/**
	 * @generated
	 */
	static private class TextCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private WrappingLabel wrapLabel;

		/**
		 * @generated
		 */
		public TextCellEditorLocator(WrappingLabel wrapLabel) {
			this.wrapLabel = wrapLabel;
		}

		/**
		 * @generated
		 */
		public WrappingLabel getWrapLabel() {
			return wrapLabel;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getWrapLabel().getTextBounds().getCopy();
			getWrapLabel().translateToAbsolute(rect);
			if (!text.getFont().isDisposed()) {
				if (getWrapLabel().isTextWrapOn()
						&& getWrapLabel().getText().length() > 0) {
					rect.setSize(new Dimension(text.computeSize(rect.width,
							SWT.DEFAULT)));
				} else {
					int avr = FigureUtilities.getFontMetrics(text.getFont())
							.getAverageCharWidth();
					rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT,
							SWT.DEFAULT)).expand(avr * 2, 0));
				}
			}
			if (!rect.equals(new Rectangle(text.getBounds()))) {
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	/**
	 * @generated
	 */
	private static class LabelCellEditorLocator implements CellEditorLocator {

		/**
		 * @generated
		 */
		private Label label;

		/**
		 * @generated
		 */
		public LabelCellEditorLocator(Label label) {
			this.label = label;
		}

		/**
		 * @generated
		 */
		public Label getLabel() {
			return label;
		}

		/**
		 * @generated
		 */
		public void relocate(CellEditor celleditor) {
			Text text = (Text) celleditor.getControl();
			Rectangle rect = getLabel().getTextBounds().getCopy();
			getLabel().translateToAbsolute(rect);
			if (!text.getFont().isDisposed()) {
				int avr = FigureUtilities.getFontMetrics(text.getFont())
						.getAverageCharWidth();
				rect.setSize(new Dimension(text.computeSize(SWT.DEFAULT,
						SWT.DEFAULT)).expand(avr * 2, 0));
			}
			if (!rect.equals(new Rectangle(text.getBounds()))) {
				text.setBounds(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}
}
