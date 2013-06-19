/**
 * <copyright>
 * </copyright>
 *
 * $Id: EDMDiagramImpl.java,v 1.1 2011/11/10 14:27:36 aalvamat Exp $
 */
package com.isb.datamodeler.ui.diagram.impl;

import com.isb.datamodeler.ui.diagram.EDMDiagram;
import com.isb.datamodeler.ui.diagram.EDiagramPackage;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.gmf.runtime.notation.impl.DiagramImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DM Diagram</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class EDMDiagramImpl extends DiagramImpl implements EDMDiagram {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EDMDiagramImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EDiagramPackage.Literals.DM_DIAGRAM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 15;
	}

} //EDMDiagramImpl
