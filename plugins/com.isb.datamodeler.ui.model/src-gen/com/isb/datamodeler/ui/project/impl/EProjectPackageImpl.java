/**
 * <copyright>
 * </copyright>
 *
 * $Id: EProjectPackageImpl.java,v 1.9 2011/12/20 12:28:52 rdedios Exp $
 */
package com.isb.datamodeler.ui.project.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.gmf.runtime.notation.NotationPackage;
import com.isb.datamodeler.accesscontrol.EAccesscontrolPackage;
import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.datatypes.EDatatypesPackage;
import com.isb.datamodeler.expressions.EExpressionsPackage;
import com.isb.datamodeler.routines.ERoutinesPackage;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.statements.EStatementsPackage;
import com.isb.datamodeler.tables.ETablesPackage;
import com.isb.datamodeler.ui.diagram.EDiagramPackage;
import com.isb.datamodeler.ui.diagram.impl.EDiagramPackageImpl;
import com.isb.datamodeler.ui.project.EProject;
import com.isb.datamodeler.ui.project.EProjectFactory;
import com.isb.datamodeler.ui.project.EProjectPackage;
import com.isb.datamodeler.ui.project.util.ProjectValidator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EProjectPackageImpl extends EPackageImpl implements EProjectPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass projectEClass = null;

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
	 * @see com.isb.datamodeler.ui.project.EProjectPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EProjectPackageImpl() {
		super(eNS_URI, EProjectFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link EProjectPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EProjectPackage init() {
		if (isInited) return (EProjectPackage)EPackage.Registry.INSTANCE.getEPackage(EProjectPackage.eNS_URI);

		// Obtain or create and register package
		EProjectPackageImpl theProjectPackage = (EProjectPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EProjectPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EProjectPackageImpl());

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
		EDiagramPackageImpl theDiagramPackage = (EDiagramPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(EDiagramPackage.eNS_URI) instanceof EDiagramPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(EDiagramPackage.eNS_URI) : EDiagramPackage.eINSTANCE);

		// Create package meta-data objects
		theProjectPackage.createPackageContents();
		theDiagramPackage.createPackageContents();

		// Initialize created meta-data
		theProjectPackage.initializePackageContents();
		theDiagramPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theProjectPackage, 
			 new EValidator.Descriptor() {
				 public EValidator getEValidator() {
					 return ProjectValidator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theProjectPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EProjectPackage.eNS_URI, theProjectPackage);
		return theProjectPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProject() {
		return projectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProject_Database() {
		return (EReference)projectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProject_Schemas() {
		return (EReference)projectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProject_Description() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProject_Application() {
		return (EAttribute)projectEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EProjectFactory getProjectFactory() {
		return (EProjectFactory)getEFactoryInstance();
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
		projectEClass = createEClass(PROJECT);
		createEReference(projectEClass, PROJECT__DATABASE);
		createEReference(projectEClass, PROJECT__SCHEMAS);
		createEAttribute(projectEClass, PROJECT__DESCRIPTION);
		createEAttribute(projectEClass, PROJECT__APPLICATION);
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
		ESchemaPackage theSchemaPackage = (ESchemaPackage)EPackage.Registry.INSTANCE.getEPackage(ESchemaPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		projectEClass.getESuperTypes().add(theSchemaPackage.getFunctionalElement());
		projectEClass.getESuperTypes().add(theSchemaPackage.getDataModelerNamedElement());

		// Initialize classes and features; add operations and parameters
		initEClass(projectEClass, EProject.class, "Project", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProject_Database(), theSchemaPackage.getDatabase(), null, "database", null, 0, 1, EProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProject_Schemas(), theSchemaPackage.getSchema(), null, "schemas", null, 0, -1, EProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Description(), theEcorePackage.getEString(), "description", null, 0, 1, EProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProject_Application(), ecorePackage.getEString(), "application", null, 0, 1, EProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(projectEClass, ecorePackage.getEBoolean(), "isValid", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEDiagnosticChain(), "diagnostics", 0, 1, IS_UNIQUE, IS_ORDERED);
		EGenericType g1 = createEGenericType(ecorePackage.getEMap());
		EGenericType g2 = createEGenericType(ecorePackage.getEJavaObject());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(ecorePackage.getEJavaObject());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "context", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/emf/2002/Ecore
		createEcoreAnnotations();
		// com.isb.datamodeler.ui.validation.delegates.ProjectValidationDelegate
		createComAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/Ecore</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createEcoreAnnotations() {
		String source = "http://www.eclipse.org/emf/2002/Ecore";		
		addAnnotation
		  (this, 
		   source, 
		   new String[] {
			 "validationDelegates", "com.isb.datamodeler.ui.validation.delegates.ProjectValidationDelegate"
		   });	
	}

	/**
	 * Initializes the annotations for <b>com.isb.datamodeler.ui.validation.delegates.ProjectValidationDelegate</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createComAnnotations() {
		String source = "com.isb.datamodeler.ui.validation.delegates.ProjectValidationDelegate";			
		addAnnotation
		  (projectEClass.getEOperations().get(0), 
		   source, 
		   new String[] {
			 "body", "indiferent"
		   });
	}

} //EProjectPackageImpl
