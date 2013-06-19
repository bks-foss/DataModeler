/**
 * <copyright>
 * </copyright>
 *
 * $Id: EProjectFactory.java,v 1.2 2011/07/14 10:05:00 rdedios Exp $
 */
package com.isb.datamodeler.ui.project;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.isb.datamodeler.ui.project.EProjectPackage
 * @generated
 */
public interface EProjectFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EProjectFactory eINSTANCE = com.isb.datamodeler.ui.project.impl.EProjectFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Project</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Project</em>'.
	 * @generated
	 */
	EProject createProject();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EProjectPackage getProjectPackage();

} //EProjectFactory
