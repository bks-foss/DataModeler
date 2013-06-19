package com.isb.datamodeler.diagram.preferences;

import org.eclipse.gmf.runtime.diagram.ui.preferences.ConnectionsPreferencePage;

import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramConnectionsPreferencePage extends ConnectionsPreferencePage {

	/**
	 * @generated
	 */
	public DiagramConnectionsPreferencePage() {
		setPreferenceStore(DatamodelerDiagramEditorPlugin.getInstance()
				.getPreferenceStore());
	}
}
