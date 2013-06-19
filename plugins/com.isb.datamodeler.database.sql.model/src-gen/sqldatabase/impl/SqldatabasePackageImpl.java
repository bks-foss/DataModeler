/**
 * <copyright>
 * </copyright>
 *
 * $Id: SqldatabasePackageImpl.java,v 1.1 2012/03/06 10:18:35 rdedios Exp $
 */
package sqldatabase.impl;

import com.isb.datamodeler.schema.ESchemaPackage;

import com.isb.datamodeler.ui.diagram.EDiagramPackage;

import com.isb.datamodeler.ui.project.EProjectPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import sqldatabase.SQLDatabase;
import sqldatabase.SQLPrimitiveType;
import sqldatabase.SqldatabaseFactory;
import sqldatabase.SqldatabasePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SqldatabasePackageImpl extends EPackageImpl implements SqldatabasePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sqlDatabaseEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum sqlPrimitiveTypeEEnum = null;

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
	 * @see sqldatabase.SqldatabasePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SqldatabasePackageImpl() {
		super(eNS_URI, SqldatabaseFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link SqldatabasePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SqldatabasePackage init() {
		if (isInited) return (SqldatabasePackage)EPackage.Registry.INSTANCE.getEPackage(SqldatabasePackage.eNS_URI);

		// Obtain or create and register package
		SqldatabasePackageImpl theSqldatabasePackage = (SqldatabasePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SqldatabasePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SqldatabasePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EProjectPackage.eINSTANCE.eClass();
		EDiagramPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theSqldatabasePackage.createPackageContents();

		// Initialize created meta-data
		theSqldatabasePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSqldatabasePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SqldatabasePackage.eNS_URI, theSqldatabasePackage);
		return theSqldatabasePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSQLDatabase() {
		return sqlDatabaseEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSQLPrimitiveType() {
		return sqlPrimitiveTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SqldatabaseFactory getSqldatabaseFactory() {
		return (SqldatabaseFactory)getEFactoryInstance();
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
		sqlDatabaseEClass = createEClass(SQL_DATABASE);

		// Create enums
		sqlPrimitiveTypeEEnum = createEEnum(SQL_PRIMITIVE_TYPE);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		sqlDatabaseEClass.getESuperTypes().add(theSchemaPackage.getDatabase());

		// Initialize classes, features, and operations; add parameters
		initEClass(sqlDatabaseEClass, SQLDatabase.class, "SQLDatabase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(sqlPrimitiveTypeEEnum, SQLPrimitiveType.class, "SQLPrimitiveType");
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.CHARACTER);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.CHARACTER_VARYING);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.CHARACTER_LARGE_OBJECT);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.BINARY);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.BINARY_VARYING);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.BINARY_LARGE_OBJECT);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.NUMERIC);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.DECIMAL);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.SMALLINT);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.INTEGER);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.BIGINT);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.FLOAT);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.REAL);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.DOUBLE_PRECISION);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.BOOLEAN);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.DATE);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.TIME);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.TIME_WITH_TIMEZONE);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.TIMESTAMP);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.TIMESTAMP_WITH_TIMEZONE);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.INTERVAL);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.XML);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.DATALINK);
		addEEnumLiteral(sqlPrimitiveTypeEEnum, SQLPrimitiveType.ROWID);

		// Create resource
		createResource(eNS_URI);
	}

} //SqldatabasePackageImpl
