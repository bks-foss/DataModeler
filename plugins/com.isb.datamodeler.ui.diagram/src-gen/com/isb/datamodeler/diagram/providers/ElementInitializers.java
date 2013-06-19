package com.isb.datamodeler.diagram.providers;

import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.diagram.part.DatamodelerDiagramEditorPlugin;

/**
 * @generated
 */
public class ElementInitializers {

	protected ElementInitializers() {
		// use #getInstance to access cached instance
	}

	/**
	 * @generated
	 */
	public void init_ForeignKey_4001(EForeignKey instance) {
		try {
			Object value_0 = defaultIdentifying_ForeignKey_4001(instance);
			instance.setDefaultIdentifying(((Boolean) value_0).booleanValue());
		} catch (RuntimeException e) {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	public void init_ForeignKey_4002(EForeignKey instance) {
		try {
			Object value_0 = defaultIdentifying_ForeignKey_4002(instance);
			instance.setDefaultIdentifying(((Boolean) value_0).booleanValue());
		} catch (RuntimeException e) {
			DatamodelerDiagramEditorPlugin.getInstance().logError(
					"Element initialization failed", e); //$NON-NLS-1$						
		}
	}

	/**
	 * @generated
	 */
	private Boolean defaultIdentifying_ForeignKey_4001(EForeignKey self) {
		return true;
	}

	/**
	 * @generated
	 */
	private Boolean defaultIdentifying_ForeignKey_4002(EForeignKey self) {
		return false;
	}

	/**
	 * @generated
	 */
	public static ElementInitializers getInstance() {
		ElementInitializers cached = DatamodelerDiagramEditorPlugin
				.getInstance().getElementInitializers();
		if (cached == null) {
			DatamodelerDiagramEditorPlugin.getInstance()
					.setElementInitializers(cached = new ElementInitializers());
		}
		return cached;
	}
}
