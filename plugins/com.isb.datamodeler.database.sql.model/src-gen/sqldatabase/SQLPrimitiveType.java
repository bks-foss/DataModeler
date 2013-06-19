/**
 * <copyright>
 * </copyright>
 *
 * $Id: SQLPrimitiveType.java,v 1.1 2012/03/06 10:18:35 rdedios Exp $
 */
package sqldatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>SQL Primitive Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see sqldatabase.SqldatabasePackage#getSQLPrimitiveType()
 * @model
 * @generated
 */
public enum SQLPrimitiveType implements Enumerator {
	/**
	 * The '<em><b>CHARACTER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CHARACTER_VALUE
	 * @generated
	 * @ordered
	 */
	CHARACTER(0, "CHARACTER", "CHAR"),

	/**
	 * The '<em><b>CHARACTER VARYING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CHARACTER_VARYING_VALUE
	 * @generated
	 * @ordered
	 */
	CHARACTER_VARYING(1, "CHARACTER_VARYING", "VARCHAR"),

	/**
	 * The '<em><b>CHARACTER LARGE OBJECT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CHARACTER_LARGE_OBJECT_VALUE
	 * @generated
	 * @ordered
	 */
	CHARACTER_LARGE_OBJECT(2, "CHARACTER_LARGE_OBJECT", "CLOB"),

	/**
	 * The '<em><b>BINARY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BINARY_VALUE
	 * @generated
	 * @ordered
	 */
	BINARY(3, "BINARY", "BINARY"),

	/**
	 * The '<em><b>BINARY VARYING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BINARY_VARYING_VALUE
	 * @generated
	 * @ordered
	 */
	BINARY_VARYING(4, "BINARY_VARYING", "VARBINARY"),

	/**
	 * The '<em><b>BINARY LARGE OBJECT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BINARY_LARGE_OBJECT_VALUE
	 * @generated
	 * @ordered
	 */
	BINARY_LARGE_OBJECT(5, "BINARY_LARGE_OBJECT", "BLOB"),

	/**
	 * The '<em><b>NUMERIC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NUMERIC_VALUE
	 * @generated
	 * @ordered
	 */
	NUMERIC(6, "NUMERIC", "NUMERIC"),

	/**
	 * The '<em><b>DECIMAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DECIMAL_VALUE
	 * @generated
	 * @ordered
	 */
	DECIMAL(7, "DECIMAL", "DECIMAL"),

	/**
	 * The '<em><b>SMALLINT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SMALLINT_VALUE
	 * @generated
	 * @ordered
	 */
	SMALLINT(8, "SMALLINT", "SMALLINT"),

	/**
	 * The '<em><b>INTEGER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #INTEGER_VALUE
	 * @generated
	 * @ordered
	 */
	INTEGER(9, "INTEGER", "INTEGER"),

	/**
	 * The '<em><b>BIGINT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BIGINT_VALUE
	 * @generated
	 * @ordered
	 */
	BIGINT(10, "BIGINT", "BIGINT"),

	/**
	 * The '<em><b>FLOAT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FLOAT_VALUE
	 * @generated
	 * @ordered
	 */
	FLOAT(11, "FLOAT", "FLOAT"),

	/**
	 * The '<em><b>REAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #REAL_VALUE
	 * @generated
	 * @ordered
	 */
	REAL(12, "REAL", "REAL"),

	/**
	 * The '<em><b>DOUBLE PRECISION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DOUBLE_PRECISION_VALUE
	 * @generated
	 * @ordered
	 */
	DOUBLE_PRECISION(13, "DOUBLE_PRECISION", "DOUBLE"),

	/**
	 * The '<em><b>BOOLEAN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BOOLEAN_VALUE
	 * @generated
	 * @ordered
	 */
	BOOLEAN(14, "BOOLEAN", "BOOLEAN"),

	/**
	 * The '<em><b>DATE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DATE_VALUE
	 * @generated
	 * @ordered
	 */
	DATE(15, "DATE", "DATE"),

	/**
	 * The '<em><b>TIME</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TIME_VALUE
	 * @generated
	 * @ordered
	 */
	TIME(16, "TIME", "TIME"),

	/**
	 * The '<em><b>TIME WITH TIMEZONE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TIME_WITH_TIMEZONE_VALUE
	 * @generated
	 * @ordered
	 */
	TIME_WITH_TIMEZONE(17, "TIME_WITH_TIMEZONE", "TIME WITH TIMEZONE"),

	/**
	 * The '<em><b>TIMESTAMP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TIMESTAMP_VALUE
	 * @generated
	 * @ordered
	 */
	TIMESTAMP(18, "TIMESTAMP", "TIMESTAMP"),

	/**
	 * The '<em><b>TIMESTAMP WITH TIMEZONE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TIMESTAMP_WITH_TIMEZONE_VALUE
	 * @generated
	 * @ordered
	 */
	TIMESTAMP_WITH_TIMEZONE(19, "TIMESTAMP_WITH_TIMEZONE", "TIMESTAMP WITH TIMEZONE"),

	/**
	 * The '<em><b>INTERVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #INTERVAL_VALUE
	 * @generated
	 * @ordered
	 */
	INTERVAL(20, "INTERVAL", "INTERVAL"),

	/**
	 * The '<em><b>XML</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #XML_VALUE
	 * @generated
	 * @ordered
	 */
	XML(21, "XML", "XML"),

	/**
	 * The '<em><b>DATALINK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DATALINK_VALUE
	 * @generated
	 * @ordered
	 */
	DATALINK(22, "DATALINK", "DATALINK"),

	/**
	 * The '<em><b>ROWID</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ROWID_VALUE
	 * @generated
	 * @ordered
	 */
	ROWID(23, "ROWID", "ROWID");

	/**
	 * The '<em><b>CHARACTER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CHARACTER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHARACTER
	 * @model literal="CHAR"
	 * @generated
	 * @ordered
	 */
	public static final int CHARACTER_VALUE = 0;

	/**
	 * The '<em><b>CHARACTER VARYING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CHARACTER VARYING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHARACTER_VARYING
	 * @model literal="VARCHAR"
	 * @generated
	 * @ordered
	 */
	public static final int CHARACTER_VARYING_VALUE = 1;

	/**
	 * The '<em><b>CHARACTER LARGE OBJECT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CHARACTER LARGE OBJECT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHARACTER_LARGE_OBJECT
	 * @model literal="CLOB"
	 * @generated
	 * @ordered
	 */
	public static final int CHARACTER_LARGE_OBJECT_VALUE = 2;

	/**
	 * The '<em><b>BINARY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BINARY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BINARY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int BINARY_VALUE = 3;

	/**
	 * The '<em><b>BINARY VARYING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BINARY VARYING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BINARY_VARYING
	 * @model literal="VARBINARY"
	 * @generated
	 * @ordered
	 */
	public static final int BINARY_VARYING_VALUE = 4;

	/**
	 * The '<em><b>BINARY LARGE OBJECT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BINARY LARGE OBJECT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BINARY_LARGE_OBJECT
	 * @model literal="BLOB"
	 * @generated
	 * @ordered
	 */
	public static final int BINARY_LARGE_OBJECT_VALUE = 5;

	/**
	 * The '<em><b>NUMERIC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NUMERIC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NUMERIC
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NUMERIC_VALUE = 6;

	/**
	 * The '<em><b>DECIMAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DECIMAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DECIMAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DECIMAL_VALUE = 7;

	/**
	 * The '<em><b>SMALLINT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SMALLINT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SMALLINT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SMALLINT_VALUE = 8;

	/**
	 * The '<em><b>INTEGER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>INTEGER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #INTEGER
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int INTEGER_VALUE = 9;

	/**
	 * The '<em><b>BIGINT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BIGINT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BIGINT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int BIGINT_VALUE = 10;

	/**
	 * The '<em><b>FLOAT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FLOAT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FLOAT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FLOAT_VALUE = 11;

	/**
	 * The '<em><b>REAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>REAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #REAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int REAL_VALUE = 12;

	/**
	 * The '<em><b>DOUBLE PRECISION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DOUBLE PRECISION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DOUBLE_PRECISION
	 * @model literal="DOUBLE"
	 * @generated
	 * @ordered
	 */
	public static final int DOUBLE_PRECISION_VALUE = 13;

	/**
	 * The '<em><b>BOOLEAN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BOOLEAN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BOOLEAN
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int BOOLEAN_VALUE = 14;

	/**
	 * The '<em><b>DATE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DATE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DATE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DATE_VALUE = 15;

	/**
	 * The '<em><b>TIME</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TIME</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TIME
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int TIME_VALUE = 16;

	/**
	 * The '<em><b>TIME WITH TIMEZONE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TIME WITH TIMEZONE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TIME_WITH_TIMEZONE
	 * @model literal="TIME WITH TIMEZONE"
	 * @generated
	 * @ordered
	 */
	public static final int TIME_WITH_TIMEZONE_VALUE = 17;

	/**
	 * The '<em><b>TIMESTAMP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TIMESTAMP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TIMESTAMP
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int TIMESTAMP_VALUE = 18;

	/**
	 * The '<em><b>TIMESTAMP WITH TIMEZONE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TIMESTAMP WITH TIMEZONE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TIMESTAMP_WITH_TIMEZONE
	 * @model literal="TIMESTAMP WITH TIMEZONE"
	 * @generated
	 * @ordered
	 */
	public static final int TIMESTAMP_WITH_TIMEZONE_VALUE = 19;

	/**
	 * The '<em><b>INTERVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>INTERVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #INTERVAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int INTERVAL_VALUE = 20;

	/**
	 * The '<em><b>XML</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>XML</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #XML
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int XML_VALUE = 21;

	/**
	 * The '<em><b>DATALINK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DATALINK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DATALINK
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DATALINK_VALUE = 22;

	/**
	 * The '<em><b>ROWID</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ROWID</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ROWID
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ROWID_VALUE = 23;

	/**
	 * An array of all the '<em><b>SQL Primitive Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SQLPrimitiveType[] VALUES_ARRAY =
		new SQLPrimitiveType[] {
			CHARACTER,
			CHARACTER_VARYING,
			CHARACTER_LARGE_OBJECT,
			BINARY,
			BINARY_VARYING,
			BINARY_LARGE_OBJECT,
			NUMERIC,
			DECIMAL,
			SMALLINT,
			INTEGER,
			BIGINT,
			FLOAT,
			REAL,
			DOUBLE_PRECISION,
			BOOLEAN,
			DATE,
			TIME,
			TIME_WITH_TIMEZONE,
			TIMESTAMP,
			TIMESTAMP_WITH_TIMEZONE,
			INTERVAL,
			XML,
			DATALINK,
			ROWID,
		};

	/**
	 * A public read-only list of all the '<em><b>SQL Primitive Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<SQLPrimitiveType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>SQL Primitive Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SQLPrimitiveType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SQLPrimitiveType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>SQL Primitive Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SQLPrimitiveType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SQLPrimitiveType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>SQL Primitive Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SQLPrimitiveType get(int value) {
		switch (value) {
			case CHARACTER_VALUE: return CHARACTER;
			case CHARACTER_VARYING_VALUE: return CHARACTER_VARYING;
			case CHARACTER_LARGE_OBJECT_VALUE: return CHARACTER_LARGE_OBJECT;
			case BINARY_VALUE: return BINARY;
			case BINARY_VARYING_VALUE: return BINARY_VARYING;
			case BINARY_LARGE_OBJECT_VALUE: return BINARY_LARGE_OBJECT;
			case NUMERIC_VALUE: return NUMERIC;
			case DECIMAL_VALUE: return DECIMAL;
			case SMALLINT_VALUE: return SMALLINT;
			case INTEGER_VALUE: return INTEGER;
			case BIGINT_VALUE: return BIGINT;
			case FLOAT_VALUE: return FLOAT;
			case REAL_VALUE: return REAL;
			case DOUBLE_PRECISION_VALUE: return DOUBLE_PRECISION;
			case BOOLEAN_VALUE: return BOOLEAN;
			case DATE_VALUE: return DATE;
			case TIME_VALUE: return TIME;
			case TIME_WITH_TIMEZONE_VALUE: return TIME_WITH_TIMEZONE;
			case TIMESTAMP_VALUE: return TIMESTAMP;
			case TIMESTAMP_WITH_TIMEZONE_VALUE: return TIMESTAMP_WITH_TIMEZONE;
			case INTERVAL_VALUE: return INTERVAL;
			case XML_VALUE: return XML;
			case DATALINK_VALUE: return DATALINK;
			case ROWID_VALUE: return ROWID;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private SQLPrimitiveType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //SQLPrimitiveType
