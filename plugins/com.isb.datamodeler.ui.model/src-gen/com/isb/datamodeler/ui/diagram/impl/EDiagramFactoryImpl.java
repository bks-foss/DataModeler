/**
 * <copyright>
 * </copyright>
 *
 * $Id: EDiagramFactoryImpl.java,v 1.1 2011/11/10 14:27:36 aalvamat Exp $
 */
package com.isb.datamodeler.ui.diagram.impl;

import com.isb.datamodeler.ui.diagram.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EDiagramFactoryImpl extends EFactoryImpl implements EDiagramFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EDiagramFactory init() {
		try {
			EDiagramFactory theDiagramFactory = (EDiagramFactory)EPackage.Registry.INSTANCE.getEFactory("http:///com/isb/datamodeler/ui/model/diagram.ecore"); 
			if (theDiagramFactory != null) {
				return theDiagramFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EDiagramFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDiagramFactoryImpl() {
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
			case EDiagramPackage.DM_DIAGRAM: return createDMDiagram();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDMDiagram createDMDiagram() {
		EDMDiagramImpl dmDiagram = new EDMDiagramImpl();
		return dmDiagram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDiagramPackage getDiagramPackage() {
		return (EDiagramPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EDiagramPackage getPackage() {
		return EDiagramPackage.eINSTANCE;
	}

} //EDiagramFactoryImpl
