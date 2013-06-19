/**
 * <copyright>
 * </copyright>
 *
 * $Id: EProjectFactoryImpl.java,v 1.5 2011/08/23 12:12:59 dsalmero Exp $
 */
package com.isb.datamodeler.ui.project.impl;

import com.isb.datamodeler.ui.project.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.isb.datamodeler.ui.project.EProject;
import com.isb.datamodeler.ui.project.EProjectFactory;
import com.isb.datamodeler.ui.project.EProjectPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EProjectFactoryImpl extends EFactoryImpl implements EProjectFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EProjectFactory init() {
		try {
			EProjectFactory theProjectFactory = (EProjectFactory)EPackage.Registry.INSTANCE.getEFactory("http:///com/isb/datamodeler/ui/model/project.ecore"); 
			if (theProjectFactory != null) {
				return theProjectFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EProjectFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EProjectFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case EProjectPackage.PROJECT: return createProject();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EProject createProject() {
		EProjectImpl project = new EProjectImpl();
		return project;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EProjectPackage getProjectPackage() {
		return (EProjectPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EProjectPackage getPackage() {
		return EProjectPackage.eINSTANCE;
	}

} //EProjectFactoryImpl
