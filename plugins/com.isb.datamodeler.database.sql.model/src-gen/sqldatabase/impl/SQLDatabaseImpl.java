/**
 * <copyright>
 * </copyright>
 *
 * $Id: SQLDatabaseImpl.java,v 1.1 2012/03/06 10:18:35 rdedios Exp $
 */
package sqldatabase.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import sqldatabase.SQLDatabase;
import sqldatabase.SqldatabasePackage;

import com.isb.datamodeler.accesscontrol.EAccesscontrolPackage;
import com.isb.datamodeler.accesscontrol.EAuthorizationIdentifier;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.datatypes.EPredefinedDataType;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.schema.ECatalog;
import com.isb.datamodeler.schema.EEvent;
import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.schema.impl.ESQLObjectImpl;
import com.isb.datamodeler.tables.EBaseTable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>SQL Database</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link sqldatabase.impl.SQLDatabaseImpl#getVendor <em>Vendor</em>}</li>
 *   <li>{@link sqldatabase.impl.SQLDatabaseImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link sqldatabase.impl.SQLDatabaseImpl#getSchemas <em>Schemas</em>}</li>
 *   <li>{@link sqldatabase.impl.SQLDatabaseImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link sqldatabase.impl.SQLDatabaseImpl#getCatalogs <em>Catalogs</em>}</li>
 *   <li>{@link sqldatabase.impl.SQLDatabaseImpl#getAuthorizationIds <em>Authorization Ids</em>}</li>
 *   <li>{@link sqldatabase.impl.SQLDatabaseImpl#getPrimitiveDataTypes <em>Primitive Data Types</em>}</li>
 *   <li>{@link sqldatabase.impl.SQLDatabaseImpl#getDefaultPrimitiveDataType <em>Default Primitive Data Type</em>}</li>
 *   <li>{@link sqldatabase.impl.SQLDatabaseImpl#getDescription <em>Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SQLDatabaseImpl extends ESQLObjectImpl implements SQLDatabase {
	/**
	 * The default value of the '{@link #getVendor() <em>Vendor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVendor()
	 * @generated
	 * @ordered
	 */
	protected static final String VENDOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVendor() <em>Vendor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVendor()
	 * @generated
	 * @ordered
	 */
	protected String vendor = VENDOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSchemas() <em>Schemas</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchemas()
	 * @generated
	 * @ordered
	 */
	protected EList<ESchema> schemas;

	/**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<EEvent> events;

	/**
	 * The cached value of the '{@link #getCatalogs() <em>Catalogs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCatalogs()
	 * @generated
	 * @ordered
	 */
	protected EList<ECatalog> catalogs;

	/**
	 * The cached value of the '{@link #getAuthorizationIds() <em>Authorization Ids</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthorizationIds()
	 * @generated
	 * @ordered
	 */
	protected EList<EAuthorizationIdentifier> authorizationIds;

	/**
	 * The cached value of the '{@link #getPrimitiveDataTypes() <em>Primitive Data Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimitiveDataTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<EPrimitiveDataType> primitiveDataTypes;

	/**
	 * The cached value of the '{@link #getDefaultPrimitiveDataType() <em>Default Primitive Data Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultPrimitiveDataType()
	 * @generated
	 * @ordered
	 */
	protected EPrimitiveDataType defaultPrimitiveDataType;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SQLDatabaseImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SqldatabasePackage.Literals.SQL_DATABASE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVendor(String newVendor) {
		String oldVendor = vendor;
		vendor = newVendor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SqldatabasePackage.SQL_DATABASE__VENDOR, oldVendor, vendor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SqldatabasePackage.SQL_DATABASE__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ESchema> getSchemas() {
		if (schemas == null) {
			schemas = new EObjectWithInverseResolvingEList<ESchema>(ESchema.class, this, SqldatabasePackage.SQL_DATABASE__SCHEMAS, ESchemaPackage.SCHEMA__DATABASE);
		}
		return schemas;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EEvent> getEvents() {
		if (events == null) {
			events = new EObjectWithInverseResolvingEList<EEvent>(EEvent.class, this, SqldatabasePackage.SQL_DATABASE__EVENTS, ESchemaPackage.EVENT__DATABASE);
		}
		return events;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ECatalog> getCatalogs() {
		if (catalogs == null) {
			catalogs = new EObjectContainmentWithInverseEList<ECatalog>(ECatalog.class, this, SqldatabasePackage.SQL_DATABASE__CATALOGS, ESchemaPackage.CATALOG__DATABASE);
		}
		return catalogs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EAuthorizationIdentifier> getAuthorizationIds() {
		if (authorizationIds == null) {
			authorizationIds = new EObjectWithInverseResolvingEList<EAuthorizationIdentifier>(EAuthorizationIdentifier.class, this, SqldatabasePackage.SQL_DATABASE__AUTHORIZATION_IDS, EAccesscontrolPackage.AUTHORIZATION_IDENTIFIER__DATABASE);
		}
		return authorizationIds;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EPrimitiveDataType> getPrimitiveDataTypes() {
		if (primitiveDataTypes == null) {
			primitiveDataTypes = new EObjectContainmentEList.Unsettable<EPrimitiveDataType>(EPrimitiveDataType.class, this, SqldatabasePackage.SQL_DATABASE__PRIMITIVE_DATA_TYPES);
		}
		return primitiveDataTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetPrimitiveDataTypes() {
		if (primitiveDataTypes != null) ((InternalEList.Unsettable<?>)primitiveDataTypes).unset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetPrimitiveDataTypes() {
		return primitiveDataTypes != null && ((InternalEList.Unsettable<?>)primitiveDataTypes).isSet();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPrimitiveDataType getDefaultPrimitiveDataType() {
		return defaultPrimitiveDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultPrimitiveDataType(EPrimitiveDataType newDefaultPrimitiveDataType) {
		EPrimitiveDataType oldDefaultPrimitiveDataType = defaultPrimitiveDataType;
		defaultPrimitiveDataType = newDefaultPrimitiveDataType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SqldatabasePackage.SQL_DATABASE__DEFAULT_PRIMITIVE_DATA_TYPE, oldDefaultPrimitiveDataType, defaultPrimitiveDataType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SqldatabasePackage.SQL_DATABASE__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List getUserDefinedTypes() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * The cached invocation delegate for the '{@link #loadPrimitiveDatatypes() <em>Load Primitive Datatypes</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #loadPrimitiveDatatypes()
	 * @generated
	 * @ordered
	 */
	protected static final EOperation.Internal.InvocationDelegate LOAD_PRIMITIVE_DATATYPES__EINVOCATION_DELEGATE = ((EOperation.Internal)ESchemaPackage.Literals.DATABASE___LOAD_PRIMITIVE_DATATYPES).getInvocationDelegate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<EPrimitiveDataType> loadPrimitiveDatatypes() {
		try {
			return (EList<EPrimitiveDataType>)LOAD_PRIMITIVE_DATATYPES__EINVOCATION_DELEGATE.dynamicInvoke(this, null);
		}
		catch (InvocationTargetException ite) {
			throw new WrappedException(ite);
		}
	}

	/**
	 * The cached invocation delegate for the '{@link #initializeForeignKeyName(com.isb.datamodeler.tables.EBaseTable, com.isb.datamodeler.tables.EBaseTable) <em>Initialize Foreign Key Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #initializeForeignKeyName(com.isb.datamodeler.tables.EBaseTable, com.isb.datamodeler.tables.EBaseTable)
	 * @generated
	 * @ordered
	 */
	protected static final EOperation.Internal.InvocationDelegate INITIALIZE_FOREIGN_KEY_NAME_EBASE_TABLE_EBASE_TABLE__EINVOCATION_DELEGATE = ((EOperation.Internal)ESchemaPackage.Literals.DATABASE___INITIALIZE_FOREIGN_KEY_NAME__EBASETABLE_EBASETABLE).getInvocationDelegate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String initializeForeignKeyName(EBaseTable newBaseTable, EBaseTable referencedBaseTable) {
		try {
			return (String)INITIALIZE_FOREIGN_KEY_NAME_EBASE_TABLE_EBASE_TABLE__EINVOCATION_DELEGATE.dynamicInvoke(this, new BasicEList.UnmodifiableEList<Object>(2, new Object[]{newBaseTable, referencedBaseTable}));
		}
		catch (InvocationTargetException ite) {
			throw new WrappedException(ite);
		}
	}

	/**
	 * The cached invocation delegate for the '{@link #initializeUniqueConstraintName(com.isb.datamodeler.tables.EBaseTable, com.isb.datamodeler.constraints.EUniqueConstraint) <em>Initialize Unique Constraint Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #initializeUniqueConstraintName(com.isb.datamodeler.tables.EBaseTable, com.isb.datamodeler.constraints.EUniqueConstraint)
	 * @generated
	 * @ordered
	 */
	protected static final EOperation.Internal.InvocationDelegate INITIALIZE_UNIQUE_CONSTRAINT_NAME_EBASE_TABLE_EUNIQUE_CONSTRAINT__EINVOCATION_DELEGATE = ((EOperation.Internal)ESchemaPackage.Literals.DATABASE___INITIALIZE_UNIQUE_CONSTRAINT_NAME__EBASETABLE_EUNIQUECONSTRAINT).getInvocationDelegate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String initializeUniqueConstraintName(EBaseTable newBaseTable, EUniqueConstraint uniqueConstraint) {
		try {
			return (String)INITIALIZE_UNIQUE_CONSTRAINT_NAME_EBASE_TABLE_EUNIQUE_CONSTRAINT__EINVOCATION_DELEGATE.dynamicInvoke(this, new BasicEList.UnmodifiableEList<Object>(2, new Object[]{newBaseTable, uniqueConstraint}));
		}
		catch (InvocationTargetException ite) {
			throw new WrappedException(ite);
		}
	}

	/**
	 * The cached invocation delegate for the '{@link #createNewPredefinedDataType(com.isb.datamodeler.datatypes.EPrimitiveDataType) <em>Create New Predefined Data Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #createNewPredefinedDataType(com.isb.datamodeler.datatypes.EPrimitiveDataType)
	 * @generated
	 * @ordered
	 */
	protected static final EOperation.Internal.InvocationDelegate CREATE_NEW_PREDEFINED_DATA_TYPE_EPRIMITIVE_DATA_TYPE__EINVOCATION_DELEGATE = ((EOperation.Internal)ESchemaPackage.Literals.DATABASE___CREATE_NEW_PREDEFINED_DATA_TYPE__EPRIMITIVEDATATYPE).getInvocationDelegate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPredefinedDataType createNewPredefinedDataType(EPrimitiveDataType primitiveType) {
		try {
			return (EPredefinedDataType)CREATE_NEW_PREDEFINED_DATA_TYPE_EPRIMITIVE_DATA_TYPE__EINVOCATION_DELEGATE.dynamicInvoke(this, new BasicEList.UnmodifiableEList<Object>(1, new Object[]{primitiveType}));
		}
		catch (InvocationTargetException ite) {
			throw new WrappedException(ite);
		}
	}

	/**
	 * The cached invocation delegate for the '{@link #validateEDataType(org.eclipse.emf.ecore.EDataType, com.isb.datamodeler.schema.ESQLObject, java.lang.String, java.lang.Object) <em>Validate EData Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #validateEDataType(org.eclipse.emf.ecore.EDataType, com.isb.datamodeler.schema.ESQLObject, java.lang.String, java.lang.Object)
	 * @generated
	 * @ordered
	 */
	protected static final EOperation.Internal.InvocationDelegate VALIDATE_EDATA_TYPE_EDATA_TYPE_ESQL_OBJECT_STRING_OBJECT__EINVOCATION_DELEGATE = ((EOperation.Internal)ESchemaPackage.Literals.DATABASE___VALIDATE_EDATA_TYPE__EDATATYPE_ESQLOBJECT_STRING_OBJECT).getInvocationDelegate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagnosticChain validateEDataType(EDataType dataType, ESQLObject object, String featureName, Object newValue) {
		try {
			return (DiagnosticChain)VALIDATE_EDATA_TYPE_EDATA_TYPE_ESQL_OBJECT_STRING_OBJECT__EINVOCATION_DELEGATE.dynamicInvoke(this, new BasicEList.UnmodifiableEList<Object>(4, new Object[]{dataType, object, featureName, newValue}));
		}
		catch (InvocationTargetException ite) {
			throw new WrappedException(ite);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SqldatabasePackage.SQL_DATABASE__SCHEMAS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSchemas()).basicAdd(otherEnd, msgs);
			case SqldatabasePackage.SQL_DATABASE__EVENTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getEvents()).basicAdd(otherEnd, msgs);
			case SqldatabasePackage.SQL_DATABASE__CATALOGS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getCatalogs()).basicAdd(otherEnd, msgs);
			case SqldatabasePackage.SQL_DATABASE__AUTHORIZATION_IDS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getAuthorizationIds()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SqldatabasePackage.SQL_DATABASE__SCHEMAS:
				return ((InternalEList<?>)getSchemas()).basicRemove(otherEnd, msgs);
			case SqldatabasePackage.SQL_DATABASE__EVENTS:
				return ((InternalEList<?>)getEvents()).basicRemove(otherEnd, msgs);
			case SqldatabasePackage.SQL_DATABASE__CATALOGS:
				return ((InternalEList<?>)getCatalogs()).basicRemove(otherEnd, msgs);
			case SqldatabasePackage.SQL_DATABASE__AUTHORIZATION_IDS:
				return ((InternalEList<?>)getAuthorizationIds()).basicRemove(otherEnd, msgs);
			case SqldatabasePackage.SQL_DATABASE__PRIMITIVE_DATA_TYPES:
				return ((InternalEList<?>)getPrimitiveDataTypes()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SqldatabasePackage.SQL_DATABASE__VENDOR:
				return getVendor();
			case SqldatabasePackage.SQL_DATABASE__VERSION:
				return getVersion();
			case SqldatabasePackage.SQL_DATABASE__SCHEMAS:
				return getSchemas();
			case SqldatabasePackage.SQL_DATABASE__EVENTS:
				return getEvents();
			case SqldatabasePackage.SQL_DATABASE__CATALOGS:
				return getCatalogs();
			case SqldatabasePackage.SQL_DATABASE__AUTHORIZATION_IDS:
				return getAuthorizationIds();
			case SqldatabasePackage.SQL_DATABASE__PRIMITIVE_DATA_TYPES:
				return getPrimitiveDataTypes();
			case SqldatabasePackage.SQL_DATABASE__DEFAULT_PRIMITIVE_DATA_TYPE:
				return getDefaultPrimitiveDataType();
			case SqldatabasePackage.SQL_DATABASE__DESCRIPTION:
				return getDescription();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SqldatabasePackage.SQL_DATABASE__VENDOR:
				setVendor((String)newValue);
				return;
			case SqldatabasePackage.SQL_DATABASE__VERSION:
				setVersion((String)newValue);
				return;
			case SqldatabasePackage.SQL_DATABASE__SCHEMAS:
				getSchemas().clear();
				getSchemas().addAll((Collection<? extends ESchema>)newValue);
				return;
			case SqldatabasePackage.SQL_DATABASE__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends EEvent>)newValue);
				return;
			case SqldatabasePackage.SQL_DATABASE__CATALOGS:
				getCatalogs().clear();
				getCatalogs().addAll((Collection<? extends ECatalog>)newValue);
				return;
			case SqldatabasePackage.SQL_DATABASE__AUTHORIZATION_IDS:
				getAuthorizationIds().clear();
				getAuthorizationIds().addAll((Collection<? extends EAuthorizationIdentifier>)newValue);
				return;
			case SqldatabasePackage.SQL_DATABASE__PRIMITIVE_DATA_TYPES:
				getPrimitiveDataTypes().clear();
				getPrimitiveDataTypes().addAll((Collection<? extends EPrimitiveDataType>)newValue);
				return;
			case SqldatabasePackage.SQL_DATABASE__DEFAULT_PRIMITIVE_DATA_TYPE:
				setDefaultPrimitiveDataType((EPrimitiveDataType)newValue);
				return;
			case SqldatabasePackage.SQL_DATABASE__DESCRIPTION:
				setDescription((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case SqldatabasePackage.SQL_DATABASE__VENDOR:
				setVendor(VENDOR_EDEFAULT);
				return;
			case SqldatabasePackage.SQL_DATABASE__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case SqldatabasePackage.SQL_DATABASE__SCHEMAS:
				getSchemas().clear();
				return;
			case SqldatabasePackage.SQL_DATABASE__EVENTS:
				getEvents().clear();
				return;
			case SqldatabasePackage.SQL_DATABASE__CATALOGS:
				getCatalogs().clear();
				return;
			case SqldatabasePackage.SQL_DATABASE__AUTHORIZATION_IDS:
				getAuthorizationIds().clear();
				return;
			case SqldatabasePackage.SQL_DATABASE__PRIMITIVE_DATA_TYPES:
				unsetPrimitiveDataTypes();
				return;
			case SqldatabasePackage.SQL_DATABASE__DEFAULT_PRIMITIVE_DATA_TYPE:
				setDefaultPrimitiveDataType((EPrimitiveDataType)null);
				return;
			case SqldatabasePackage.SQL_DATABASE__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case SqldatabasePackage.SQL_DATABASE__VENDOR:
				return VENDOR_EDEFAULT == null ? vendor != null : !VENDOR_EDEFAULT.equals(vendor);
			case SqldatabasePackage.SQL_DATABASE__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case SqldatabasePackage.SQL_DATABASE__SCHEMAS:
				return schemas != null && !schemas.isEmpty();
			case SqldatabasePackage.SQL_DATABASE__EVENTS:
				return events != null && !events.isEmpty();
			case SqldatabasePackage.SQL_DATABASE__CATALOGS:
				return catalogs != null && !catalogs.isEmpty();
			case SqldatabasePackage.SQL_DATABASE__AUTHORIZATION_IDS:
				return authorizationIds != null && !authorizationIds.isEmpty();
			case SqldatabasePackage.SQL_DATABASE__PRIMITIVE_DATA_TYPES:
				return isSetPrimitiveDataTypes();
			case SqldatabasePackage.SQL_DATABASE__DEFAULT_PRIMITIVE_DATA_TYPE:
				return defaultPrimitiveDataType != null;
			case SqldatabasePackage.SQL_DATABASE__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SqldatabasePackage.SQL_DATABASE___GET_USER_DEFINED_TYPES:
				return getUserDefinedTypes();
			case SqldatabasePackage.SQL_DATABASE___LOAD_PRIMITIVE_DATATYPES:
				return loadPrimitiveDatatypes();
			case SqldatabasePackage.SQL_DATABASE___INITIALIZE_FOREIGN_KEY_NAME__EBASETABLE_EBASETABLE:
				return initializeForeignKeyName((EBaseTable)arguments.get(0), (EBaseTable)arguments.get(1));
			case SqldatabasePackage.SQL_DATABASE___INITIALIZE_UNIQUE_CONSTRAINT_NAME__EBASETABLE_EUNIQUECONSTRAINT:
				return initializeUniqueConstraintName((EBaseTable)arguments.get(0), (EUniqueConstraint)arguments.get(1));
			case SqldatabasePackage.SQL_DATABASE___CREATE_NEW_PREDEFINED_DATA_TYPE__EPRIMITIVEDATATYPE:
				return createNewPredefinedDataType((EPrimitiveDataType)arguments.get(0));
			case SqldatabasePackage.SQL_DATABASE___VALIDATE_EDATA_TYPE__EDATATYPE_ESQLOBJECT_STRING_OBJECT:
				return validateEDataType((EDataType)arguments.get(0), (ESQLObject)arguments.get(1), (String)arguments.get(2), arguments.get(3));
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (vendor: ");
		result.append(vendor);
		result.append(", version: ");
		result.append(version);
		result.append(", description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} //SQLDatabaseImpl
