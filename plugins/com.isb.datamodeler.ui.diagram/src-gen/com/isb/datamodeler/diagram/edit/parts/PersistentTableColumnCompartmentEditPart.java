package com.isb.datamodeler.diagram.edit.parts;

import java.util.Comparator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableCompartmentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.notation.SortingDirection;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.constraints.EConstraint;
import com.isb.datamodeler.diagram.edit.policies.PersistentTableColumnCompartmentCanonicalEditPolicy;
import com.isb.datamodeler.diagram.edit.policies.PersistentTableColumnCompartmentItemSemanticEditPolicy;
import com.isb.datamodeler.diagram.part.Messages;
import com.isb.datamodeler.tables.EPersistentTable;
import com.isb.datamodeler.ui.diagram.comparators.ColumnComparator;

/**
 * @generated
 */
public class PersistentTableColumnCompartmentEditPart extends
		ListCompartmentEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 7001;

	/**
	 * @generated
	 */
	public PersistentTableColumnCompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * @generated not
	 * Modificamoes este método para que se refresquen las columnas al cambiar ciertas propiedades
	 */
	protected boolean hasModelChildrenChanged(Notification evt) {
		return true;
	}

	/**
	 * @generated
	 */
	public String getCompartmentName() {
		return Messages.PersistentTableColumnCompartmentEditPart_title;
	}

	/**
	 * @generated
	 */
	public IFigure createFigure() {
		ResizableCompartmentFigure result = (ResizableCompartmentFigure) super
				.createFigure();
		result.setTitleVisibility(false);
		return result;
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
				new ResizableCompartmentEditPolicy());
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new PersistentTableColumnCompartmentItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new PersistentTableColumnCompartmentCanonicalEditPolicy());
	}

	@Override
	/**
	 * Sobreescribimos este método para escuchar cambios en la PrimaryKey 
	 * (Nos interesa para cambiar el orden visual de las columnas cuando este cambia dentro de la PK)
	 */
	protected void addSemanticListeners() {

		super.addSemanticListeners();

		EPersistentTable table = (EPersistentTable) resolveSemanticElement();

		int i = 0;

		for (EConstraint constraint : table.getConstraints())
			addListenerFilter("IndirectSemanticElement" + i++, this, constraint);//$NON-NLS-1$

	}

	/**
	 * @generated
	 */
	protected void setRatio(Double ratio) {
		// nothing to do -- parent layout does not accept Double constraints as ratio
		// super.setRatio(ratio); 
	}

	@Override
	/**
	 * Creamos un comparador para ordenar las columnas
	 */
	protected Comparator<EObject> getComparator(String name,
			SortingDirection direction) {
		return new ColumnComparator();
	}

}
