/**
 * <copyright>
 * </copyright>
 *
 * $Id: SqldatabasePackage.java,v 1.1 2012/03/06 10:18:35 rdedios Exp $
 */
package sqldatabase;

import com.isb.datamodeler.schema.ESchemaPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see sqldatabase.SqldatabaseFactory
 * @model kind="package"
 * @generated
 */
public interface SqldatabasePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "sqldatabase";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///sqldatabase.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "SQLDatabase";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SqldatabasePackage eINSTANCE = sqldatabase.impl.SqldatabasePackageImpl.init();

	/**
	 * The meta object id for the '{@link sqldatabase.impl.SQLDatabaseImpl <em>SQL Database</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see sqldatabase.impl.SQLDatabaseImpl
	 * @see sqldatabase.impl.SqldatabasePackageImpl#getSQLDatabase()
	 * @generated
	 */
	int SQL_DATABASE = 0;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__EANNOTATIONS = ESchemaPackage.DATABASE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__NAME = ESchemaPackage.DATABASE__NAME;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__DEPENDENCIES = ESchemaPackage.DATABASE__DEPENDENCIES;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__LABEL = ESchemaPackage.DATABASE__LABEL;

	/**
	 * The feature id for the '<em><b>Comments</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__COMMENTS = ESchemaPackage.DATABASE__COMMENTS;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__EXTENSIONS = ESchemaPackage.DATABASE__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Privileges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__PRIVILEGES = ESchemaPackage.DATABASE__PRIVILEGES;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__ID = ESchemaPackage.DATABASE__ID;

	/**
	 * The feature id for the '<em><b>Vendor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__VENDOR = ESchemaPackage.DATABASE__VENDOR;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__VERSION = ESchemaPackage.DATABASE__VERSION;

	/**
	 * The feature id for the '<em><b>Schemas</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__SCHEMAS = ESchemaPackage.DATABASE__SCHEMAS;

	/**
	 * The feature id for the '<em><b>Events</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__EVENTS = ESchemaPackage.DATABASE__EVENTS;

	/**
	 * The feature id for the '<em><b>Catalogs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__CATALOGS = ESchemaPackage.DATABASE__CATALOGS;

	/**
	 * The feature id for the '<em><b>Authorization Ids</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__AUTHORIZATION_IDS = ESchemaPackage.DATABASE__AUTHORIZATION_IDS;

	/**
	 * The feature id for the '<em><b>Primitive Data Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__PRIMITIVE_DATA_TYPES = ESchemaPackage.DATABASE__PRIMITIVE_DATA_TYPES;

	/**
	 * The feature id for the '<em><b>Default Primitive Data Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__DEFAULT_PRIMITIVE_DATA_TYPE = ESchemaPackage.DATABASE__DEFAULT_PRIMITIVE_DATA_TYPE;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE__DESCRIPTION = ESchemaPackage.DATABASE__DESCRIPTION;

	/**
	 * The number of structural features of the '<em>SQL Database</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE_FEATURE_COUNT = ESchemaPackage.DATABASE_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Add EAnnotation</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___ADD_EANNOTATION__STRING = ESchemaPackage.DATABASE___ADD_EANNOTATION__STRING;

	/**
	 * The operation id for the '<em>Add EAnnotation Detail</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___ADD_EANNOTATION_DETAIL__EANNOTATION_STRING_STRING = ESchemaPackage.DATABASE___ADD_EANNOTATION_DETAIL__EANNOTATION_STRING_STRING;

	/**
	 * The operation id for the '<em>Get EAnnotation Detail</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___GET_EANNOTATION_DETAIL__EANNOTATION_STRING = ESchemaPackage.DATABASE___GET_EANNOTATION_DETAIL__EANNOTATION_STRING;

	/**
	 * The operation id for the '<em>Set Annotation Detail</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___SET_ANNOTATION_DETAIL__EANNOTATION_STRING_STRING = ESchemaPackage.DATABASE___SET_ANNOTATION_DETAIL__EANNOTATION_STRING_STRING;

	/**
	 * The operation id for the '<em>Remove EAnnotation Detail</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___REMOVE_EANNOTATION_DETAIL__EANNOTATION_STRING = ESchemaPackage.DATABASE___REMOVE_EANNOTATION_DETAIL__EANNOTATION_STRING;

	/**
	 * The operation id for the '<em>Get EAnnotation</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___GET_EANNOTATION__STRING = ESchemaPackage.DATABASE___GET_EANNOTATION__STRING;

	/**
	 * The operation id for the '<em>Is Valid</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___IS_VALID__DIAGNOSTICCHAIN_MAP = ESchemaPackage.DATABASE___IS_VALID__DIAGNOSTICCHAIN_MAP;

	/**
	 * The operation id for the '<em>Get User Defined Types</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___GET_USER_DEFINED_TYPES = ESchemaPackage.DATABASE___GET_USER_DEFINED_TYPES;

	/**
	 * The operation id for the '<em>Load Primitive Datatypes</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___LOAD_PRIMITIVE_DATATYPES = ESchemaPackage.DATABASE___LOAD_PRIMITIVE_DATATYPES;

	/**
	 * The operation id for the '<em>Initialize Foreign Key Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___INITIALIZE_FOREIGN_KEY_NAME__EBASETABLE_EBASETABLE = ESchemaPackage.DATABASE___INITIALIZE_FOREIGN_KEY_NAME__EBASETABLE_EBASETABLE;

	/**
	 * The operation id for the '<em>Initialize Unique Constraint Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___INITIALIZE_UNIQUE_CONSTRAINT_NAME__EBASETABLE_EUNIQUECONSTRAINT = ESchemaPackage.DATABASE___INITIALIZE_UNIQUE_CONSTRAINT_NAME__EBASETABLE_EUNIQUECONSTRAINT;

	/**
	 * The operation id for the '<em>Create New Predefined Data Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___CREATE_NEW_PREDEFINED_DATA_TYPE__EPRIMITIVEDATATYPE = ESchemaPackage.DATABASE___CREATE_NEW_PREDEFINED_DATA_TYPE__EPRIMITIVEDATATYPE;

	/**
	 * The operation id for the '<em>Validate EData Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE___VALIDATE_EDATA_TYPE__EDATATYPE_ESQLOBJECT_STRING_OBJECT = ESchemaPackage.DATABASE___VALIDATE_EDATA_TYPE__EDATATYPE_ESQLOBJECT_STRING_OBJECT;

	/**
	 * The number of operations of the '<em>SQL Database</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SQL_DATABASE_OPERATION_COUNT = ESchemaPackage.DATABASE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link sqldatabase.SQLPrimitiveType <em>SQL Primitive Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see sqldatabase.SQLPrimitiveType
	 * @see sqldatabase.impl.SqldatabasePackageImpl#getSQLPrimitiveType()
	 * @generated
	 */
	int SQL_PRIMITIVE_TYPE = 1;


	/**
	 * Returns the meta object for class '{@link sqldatabase.SQLDatabase <em>SQL Database</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>SQL Database</em>'.
	 * @see sqldatabase.SQLDatabase
	 * @generated
	 */
	EClass getSQLDatabase();

	/**
	 * Returns the meta object for enum '{@link sqldatabase.SQLPrimitiveType <em>SQL Primitive Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>SQL Primitive Type</em>'.
	 * @see sqldatabase.SQLPrimitiveType
	 * @generated
	 */
	EEnum getSQLPrimitiveType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SqldatabaseFactory getSqldatabaseFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link sqldatabase.impl.SQLDatabaseImpl <em>SQL Database</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see sqldatabase.impl.SQLDatabaseImpl
		 * @see sqldatabase.impl.SqldatabasePackageImpl#getSQLDatabase()
		 * @generated
		 */
		EClass SQL_DATABASE = eINSTANCE.getSQLDatabase();

		/**
		 * The meta object literal for the '{@link sqldatabase.SQLPrimitiveType <em>SQL Primitive Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see sqldatabase.SQLPrimitiveType
		 * @see sqldatabase.impl.SqldatabasePackageImpl#getSQLPrimitiveType()
		 * @generated
		 */
		EEnum SQL_PRIMITIVE_TYPE = eINSTANCE.getSQLPrimitiveType();

	}

} //SqldatabasePackage
