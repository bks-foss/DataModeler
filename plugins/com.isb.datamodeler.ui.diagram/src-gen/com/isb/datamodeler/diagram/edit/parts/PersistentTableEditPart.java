package com.isb.datamodeler.diagram.edit.parts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConstrainedToolbarLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import com.isb.datamodeler.diagram.edit.policies.DatamodelerTextSelectionEditPolicy;
import com.isb.datamodeler.diagram.edit.policies.PersistentTableItemSemanticEditPolicy;
import com.isb.datamodeler.diagram.part.DatamodelerVisualIDRegistry;
import com.isb.datamodeler.diagram.providers.DatamodelerElementTypes;
import com.isb.datamodeler.tables.ETable;
import com.isb.datamodeler.ui.project.EProject;

/**
 * @generated
 */
public class PersistentTableEditPart extends ShapeNodeEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 2009;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	protected IFigure primaryShape;

	/**
	 * @generated
	 */
	public PersistentTableEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy());
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new PersistentTableItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {

		ConstrainedToolbarLayoutEditPolicy lep = new ConstrainedToolbarLayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
				if (child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE) == null) {
					if (child instanceof ITextAwareEditPart) {
						return new DatamodelerTextSelectionEditPolicy();
					}
				}
				return super.createChildEditPolicy(child);
			}
		};
		return lep;
	}

	/**
	 * @generated
	 */
	protected IFigure createNodeShape() {
		return primaryShape = new TableFigure();
	}

	/**
	 * @generated
	 */
	public TableFigure getPrimaryShape() {
		return (TableFigure) primaryShape;
	}

	/**
	 * @generated
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof PersistentTableNameEditPart) {
			((PersistentTableNameEditPart) childEditPart)
					.setLabel(getPrimaryShape().getFigureTableNameFigure());
			return true;
		}
		if (childEditPart instanceof PersistentTableColumnCompartmentEditPart) {
			IFigure pane = getPrimaryShape().getFigureColumnCompartmentFigure();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane.add(((PersistentTableColumnCompartmentEditPart) childEditPart)
					.getFigure());
			return true;
		}
		if (childEditPart instanceof PersistentTableConstraintsCompartmentEditPart) {
			IFigure pane = getPrimaryShape()
					.getFigureConstraintCompartmentFigure();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane.add(((PersistentTableConstraintsCompartmentEditPart) childEditPart)
					.getFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof PersistentTableNameEditPart) {
			return true;
		}
		if (childEditPart instanceof PersistentTableColumnCompartmentEditPart) {
			IFigure pane = getPrimaryShape().getFigureColumnCompartmentFigure();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane.remove(((PersistentTableColumnCompartmentEditPart) childEditPart)
					.getFigure());
			return true;
		}
		if (childEditPart instanceof PersistentTableConstraintsCompartmentEditPart) {
			IFigure pane = getPrimaryShape()
					.getFigureConstraintCompartmentFigure();
			setupContentPane(pane); // FIXME each comparment should handle his content pane in his own way 
			pane.remove(((PersistentTableConstraintsCompartmentEditPart) childEditPart)
					.getFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, -1);
	}

	/**
	 * @generated
	 */
	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
	}

	/**
	 * @generated
	 */
	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if (editPart instanceof PersistentTableColumnCompartmentEditPart) {
			return getPrimaryShape().getFigureColumnCompartmentFigure();
		}
		if (editPart instanceof PersistentTableConstraintsCompartmentEditPart) {
			return getPrimaryShape().getFigureConstraintCompartmentFigure();
		}
		return getContentPane();
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(40, 40);
		return result;
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
	protected NodeFigure createNodeFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane.
	 * Respects layout one may have set for generated figure.
	 * @param nodeShape instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
		if (nodeShape.getLayoutManager() == null) {
			ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setSpacing(5);
			nodeShape.setLayoutManager(layout);
		}
		return nodeShape; // use nodeShape itself as contentPane
	}

	/**
	 * @generated
	 */
	public IFigure getContentPane() {
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	/**
	 * @generated
	 */
	protected void setForegroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setForegroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setBackgroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setBackgroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineWidth(int width) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineWidth(width);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineType(int style) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineStyle(style);
		}
	}

	/**
	 * @generated
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(DatamodelerVisualIDRegistry
				.getType(PersistentTableNameEditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnSource() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(DatamodelerElementTypes.ForeignKey_4001);
		types.add(DatamodelerElementTypes.ForeignKey_4002);
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnSourceAndTarget(
			IGraphicalEditPart targetEditPart) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (targetEditPart instanceof com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart) {
			types.add(DatamodelerElementTypes.ForeignKey_4001);
		}
		if (targetEditPart instanceof com.isb.datamodeler.diagram.edit.parts.PersistentTableEditPart) {
			types.add(DatamodelerElementTypes.ForeignKey_4002);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMATypesForTarget(IElementType relationshipType) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (relationshipType == DatamodelerElementTypes.ForeignKey_4001) {
			types.add(DatamodelerElementTypes.PersistentTable_2009);
		} else if (relationshipType == DatamodelerElementTypes.ForeignKey_4002) {
			types.add(DatamodelerElementTypes.PersistentTable_2009);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnTarget() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(2);
		types.add(DatamodelerElementTypes.ForeignKey_4001);
		types.add(DatamodelerElementTypes.ForeignKey_4002);
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMATypesForSource(IElementType relationshipType) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (relationshipType == DatamodelerElementTypes.ForeignKey_4001) {
			types.add(DatamodelerElementTypes.PersistentTable_2009);
		} else if (relationshipType == DatamodelerElementTypes.ForeignKey_4002) {
			types.add(DatamodelerElementTypes.PersistentTable_2009);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public EditPart getTargetEditPart(Request request) {
		if (request instanceof CreateViewAndElementRequest) {
			CreateElementRequestAdapter adapter = ((CreateViewAndElementRequest) request)
					.getViewAndElementDescriptor()
					.getCreateElementRequestAdapter();
			IElementType type = (IElementType) adapter
					.getAdapter(IElementType.class);
			if (type == DatamodelerElementTypes.Column_3001) {
				return getChildBySemanticHint(DatamodelerVisualIDRegistry
						.getType(PersistentTableColumnCompartmentEditPart.VISUAL_ID));
			}
			if (type == DatamodelerElementTypes.UniqueConstraint_3002) {
				return getChildBySemanticHint(DatamodelerVisualIDRegistry
						.getType(PersistentTableConstraintsCompartmentEditPart.VISUAL_ID));
			}
			if (type == DatamodelerElementTypes.ForeignKey_3003) {
				return getChildBySemanticHint(DatamodelerVisualIDRegistry
						.getType(PersistentTableConstraintsCompartmentEditPart.VISUAL_ID));
			}
		}
		return super.getTargetEditPart(request);
	}

	/**
	 * @generated
	 */
	protected void handleNotificationEvent(Notification event) {
		if (event.getNotifier() == getModel()
				&& EcorePackage.eINSTANCE.getEModelElement_EAnnotations()
						.equals(event.getFeature())) {
			handleMajorSemanticChange();
		} else {
			super.handleNotificationEvent(event);
		}
	}

	/**
	 * @generated NOT
	 */
	public class TableFigure extends RoundedRectangle {

		private WrappingLabel fFigureTableNameFigure;

		private RectangleFigure fFigureColumnCompartmentFigure;

		private RectangleFigure fFigureConstraintCompartmentFigure;

		private Color FOREGROUND;

		private Color BACKGROUND;

		public TableFigure() {

			if (isExternal()) {
				FOREGROUND = THIS_FORE_EXTERNAL;
				BACKGROUND = THIS_BACK_EXTERNAL;
			} else {
				FOREGROUND = THIS_FORE;
				BACKGROUND = THIS_BACK;
			}

			ToolbarLayout layoutThis = new ToolbarLayout();
			layoutThis.setStretchMinorAxis(true);
			layoutThis.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);

			layoutThis.setSpacing(5);
			layoutThis.setVertical(true);

			this.setLayoutManager(layoutThis);

			this.setCornerDimensions(new Dimension(getMapMode().DPtoLP(6),
					getMapMode().DPtoLP(6)));
			this.setLineWidth(3);

			this.setForegroundColor(FOREGROUND);
			this.setBackgroundColor(BACKGROUND);

			this.setMinimumSize(new Dimension(getMapMode().DPtoLP(160),
					getMapMode().DPtoLP(150)));

			this.setBorder(new MarginBorder(getMapMode().DPtoLP(5),
					getMapMode().DPtoLP(3), getMapMode().DPtoLP(3),
					getMapMode().DPtoLP(3)));
			createContents();
		}

		private boolean isExternal() {
			String capability1 = "";
			String capability2 = "";

			EObject node = ((Node) getModel()).getElement();

			if (node instanceof ETable) {
				capability1 = ((ETable) node).getSchema().getCapability();
				if (((ETable) node).getSchema().eContainer() != null)
					capability2 = ((EProject) ((ETable) node).getSchema()
							.eContainer()).getCapability();
			}

			if (capability1.equalsIgnoreCase(capability2))
				return false;
			return true;
		}

		private void createContents() {

			fFigureTableNameFigure = new WrappingLabel();
			fFigureTableNameFigure.setText("<..>");

			fFigureTableNameFigure.setFont(FFIGURETABLENAMEFIGURE_FONT);

			this.add(fFigureTableNameFigure);

			fFigureColumnCompartmentFigure = new RectangleFigure();
			fFigureColumnCompartmentFigure.setForegroundColor(FOREGROUND);
			fFigureColumnCompartmentFigure.setBackgroundColor(BACKGROUND);

			this.add(fFigureColumnCompartmentFigure);

			fFigureConstraintCompartmentFigure = new RectangleFigure();
			fFigureConstraintCompartmentFigure.setForegroundColor(FOREGROUND);
			fFigureConstraintCompartmentFigure.setBackgroundColor(BACKGROUND);

			this.add(fFigureConstraintCompartmentFigure);

		}

		public WrappingLabel getFigureTableNameFigure() {
			return fFigureTableNameFigure;
		}

		public RectangleFigure getFigureColumnCompartmentFigure() {
			return fFigureColumnCompartmentFigure;
		}

		public RectangleFigure getFigureConstraintCompartmentFigure() {
			return fFigureConstraintCompartmentFigure;
		}

	}

	/**
	 * @generated
	 */
	static final Color THIS_FORE = new Color(null, 216, 227, 250);

	/**
	 * @generated
	 */
	static final Color THIS_BACK = new Color(null, 251, 252, 255);

	/**
	 * @generated NOT
	 */
	static final Color THIS_FORE_EXTERNAL = new Color(null, 253, 225, 129);

	/**
	 * @generated NOT
	 */
	static final Color THIS_BACK_EXTERNAL = new Color(null, 255, 250, 235);

	/**
	 * @generated
	 */
	static final Font FFIGURETABLENAMEFIGURE_FONT = new Font(
			Display.getCurrent(), "Tahoma", 9, SWT.BOLD);

	/**
	 * @generated
	 */
	static final Color FFIGURECOLUMNCOMPARTMENTFIGURE_FORE = new Color(null,
			216, 227, 250);

	/**
	 * @generated
	 */
	static final Color FFIGURECOLUMNCOMPARTMENTFIGURE_BACK = new Color(null,
			251, 252, 255);

	/**
	 * @generated
	 */
	static final Color FFIGURECONSTRAINTCOMPARTMENTFIGURE_FORE = new Color(
			null, 216, 227, 250);

	/**
	 * @generated
	 */
	static final Color FFIGURECONSTRAINTCOMPARTMENTFIGURE_BACK = new Color(
			null, 251, 252, 255);

}
