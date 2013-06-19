/**
 * <copyright>
 * </copyright>
 *
 * $Id: EDiagramPackageImpl.java,v 1.1 2011/11/10 14:27:36 aalvamat Exp $
 */
package com.isb.datamodeler.ui.diagram.impl;

import com.isb.datamodeler.accesscontrol.EAccesscontrolPackage;

import com.isb.datamodeler.constraints.EConstraintsPackage;

import com.isb.datamodeler.datatypes.EDatatypesPackage;

import com.isb.datamodeler.expressions.EExpressionsPackage;

import com.isb.datamodeler.routines.ERoutinesPackage;

import com.isb.datamodeler.schema.ESchemaPackage;

import com.isb.datamodeler.statements.EStatementsPackage;

import com.isb.datamodeler.tables.ETablesPackage;

import com.isb.datamodeler.ui.diagram.EDMDiagram;
import com.isb.datamodeler.ui.diagram.EDiagramFactory;
import com.isb.datamodeler.ui.diagram.EDiagramPackage;

import com.isb.datamodeler.ui.project.EProjectPackage;

import com.isb.datamodeler.ui.project.impl.EProjectPackageImpl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EDiagramPackageImpl extends EPackageImpl implements EDiagramPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dmDiagramEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.isb.datamodeler.ui.diagram.EDiagramPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EDiagramPackageImpl() {
		super(eNS_URI, EDiagramFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link EDiagramPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EDiagramPackage init() {
		if (isInited) return (EDiagramPackage)EPackage.Registry.INSTANCE.getEPackage(EDiagramPackage.eNS_URI);

		// Obtain or create and register package
		EDiagramPackageImpl theDiagramPackage = (EDiagramPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EDiagramPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EDiagramPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ESchemaPackage.eINSTANCE.eClass();
		EConstraintsPackage.eINSTANCE.eClass();
		EDatatypesPackage.eINSTANCE.eClass();
		EExpressionsPackage.eINSTANCE.eClass();
		ERoutinesPackage.eINSTANCE.eClass();
		EStatementsPackage.eINSTANCE.eClass();
		ETablesPackage.eINSTANCE.eClass();
		EAccesscontrolPackage.eINSTANCE.eClass();
		NotationPackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		EProjectPackageImpl theProjectPackage = (EProjectPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(EProjectPackage.eNS_URI) instanceof EProjectPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(EProjectPackage.eNS_URI) : EProjectPackage.eINSTANCE);

		// Create package meta-data objects
		theDiagramPackage.createPackageContents();
		theProjectPackage.createPackageContents();

		// Initialize created meta-data
		theDiagramPackage.initializePackageContents();
		theProjectPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDiagramPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EDiagramPackage.eNS_URI, theDiagramPackage);
		return theDiagramPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDMDiagram() {
		return dmDiagramEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDiagramFactory getDiagramFactory() {
		return (EDiagramFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		dmDiagramEClass = createEClass(DM_DIAGRAM);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		NotationPackage theNotationPackage = (NotationPackage)EPackage.Registry.INSTANCE.getEPackage(NotationPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		dmDiagramEClass.getESuperTypes().add(theNotationPackage.getDiagram());

		// Initialize classes and features; add operations and parameters
		initEClass(dmDiagramEClass, EDMDiagram.class, "DMDiagram", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //EDiagramPackageImpl
