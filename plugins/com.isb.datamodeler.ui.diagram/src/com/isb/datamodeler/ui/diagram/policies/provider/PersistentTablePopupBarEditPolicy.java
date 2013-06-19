package com.isb.datamodeler.ui.diagram.policies.provider;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.PopupBarEditPolicy;

import com.isb.datamodeler.diagram.util.DataModelerDiagramUtils;

public class PersistentTablePopupBarEditPolicy extends PopupBarEditPolicy {

	/**
	 * Usamos este métodos para "limpiar" la statusbar de los mensajes de restricciones de relaciones
	 */
	public void mouseMoved(MouseEvent me) {
		
		DataModelerDiagramUtils.showMessage(null);
		
		super.mouseMoved(me);
	}

}
