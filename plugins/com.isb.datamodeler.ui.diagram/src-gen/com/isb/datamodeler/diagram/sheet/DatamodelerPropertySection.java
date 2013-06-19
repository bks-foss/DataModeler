package com.isb.datamodeler.diagram.sheet;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.LabelEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.sections.AdvancedPropertySection;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import com.isb.datamodeler.diagram.properties.DataModelerPropertySource;
import com.isb.datamodeler.ui.diagram.EDMDiagram;

/**
 * @generated
 */
public class DatamodelerPropertySection extends AdvancedPropertySection
		implements IPropertySourceProvider {
	/**
	 * !!OJO!! Si borramos la clase y volvemos a generar tendriamos 
	 * que volver a declarar la variable.
	 */
	boolean _isShortCut = false;

	/**
	 * (Modified template: PropertySection.xpt)
	 * @generated
	 */
	public IPropertySource getPropertySource(Object object) {
		if (object instanceof IPropertySource) {
			return (IPropertySource) object;
		}
		AdapterFactory af = getAdapterFactory(object);
		if (af != null) {
			IItemPropertySource ips = (IItemPropertySource) af.adapt(object,
					IItemPropertySource.class);
			if (ips != null) {
				//Utilizamos nuestro propio PropertySource para poder customizar los CellEditors
				return new DataModelerPropertySource(object, ips, _isShortCut);
			}
		}
		if (object instanceof IAdaptable) {
			return (IPropertySource) ((IAdaptable) object)
					.getAdapter(IPropertySource.class);
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected IPropertySourceProvider getPropertySourceProvider() {
		return this;
	}

	/**
	 * Modify/unwrap selection.
	 * @generated NOT
	 */
	protected Object transformSelection(Object selected) {

		_isShortCut = false;

		if (selected instanceof LabelEditPart)
			return false;

		if (selected instanceof EditPart) {
			Object model = ((EditPart) selected).getModel();

			if (model instanceof EDMDiagram)
				return model;
			if (model instanceof View) {
				View v = (View) model;
				_isShortCut = v.getEAnnotation("Shortcut") != null;

				if (!_isShortCut) {
					View v1;
					if (((EditPart) selected) instanceof ConnectionNodeEditPart) {
						ConnectionNodeEditPart cEditPart = (ConnectionNodeEditPart) ((EditPart) selected);
						EditPart source = cEditPart.getSource();
						if (source.getModel() instanceof View) {
							v1 = (View) source.getModel();
							_isShortCut = v1.getEAnnotation("Shortcut") != null;
						}
					} else {
						EditPart parent = ((EditPart) selected).getParent();

						while (parent != null && !(_isShortCut)) {
							Object model1 = parent.getModel();
							if (model1 instanceof View) {
								v1 = (View) model1;
								_isShortCut = v1.getEAnnotation("Shortcut") != null;
							}
							parent = parent.getParent();
						}
					}

				}
			}

			return model instanceof View ? ((View) model).getElement() : null;
		}
		if (selected instanceof View) {
			return ((View) selected).getElement();
		}
		if (selected instanceof IAdaptable) {
			View view = (View) ((IAdaptable) selected).getAdapter(View.class);
			if (view != null) {
				return view.getElement();
			}
		}
		return selected;
	}

	/**
	 * (Modified template: PropertySection.xpt)
	 * @generated
	 */
	public void setInput(IWorkbenchPart part, ISelection selection) {
		if (selection.isEmpty()
				|| false == selection instanceof StructuredSelection) {
			super.setInput(part, selection);
			return;
		}
		final StructuredSelection structuredSelection = ((StructuredSelection) selection);
		ArrayList transformedSelection = new ArrayList(
				structuredSelection.size());
		for (Iterator it = structuredSelection.iterator(); it.hasNext();) {
			Object r = transformSelection(it.next());
			if (r != null) {
				transformedSelection.add(r);
			}
		}
		super.setInput(part, new StructuredSelection(transformedSelection));
	}

	/**
	 * @generated
	 */
	protected AdapterFactory getAdapterFactory(Object object) {
		if (getEditingDomain() instanceof AdapterFactoryEditingDomain) {
			return ((AdapterFactoryEditingDomain) getEditingDomain())
					.getAdapterFactory();
		}
		TransactionalEditingDomain editingDomain = TransactionUtil
				.getEditingDomain(object);
		if (editingDomain != null) {
			return ((AdapterFactoryEditingDomain) editingDomain)
					.getAdapterFactory();
		}
		return null;
	}

}
