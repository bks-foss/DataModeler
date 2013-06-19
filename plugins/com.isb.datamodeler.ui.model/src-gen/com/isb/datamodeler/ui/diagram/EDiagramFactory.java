/**
 * <copyright>
 * </copyright>
 *
 * $Id: EDiagramFactory.java,v 1.1 2011/11/10 14:27:36 aalvamat Exp $
 */
package com.isb.datamodeler.ui.diagram;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.isb.datamodeler.ui.diagram.EDiagramPackage
 * @generated
 */
public interface EDiagramFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EDiagramFactory eINSTANCE = com.isb.datamodeler.ui.diagram.impl.EDiagramFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>DM Diagram</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>DM Diagram</em>'.
	 * @generated
	 */
	EDMDiagram createDMDiagram();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EDiagramPackage getDiagramPackage();

} //EDiagramFactory
