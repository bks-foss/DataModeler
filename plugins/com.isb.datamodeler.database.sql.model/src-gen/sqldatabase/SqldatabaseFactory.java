/**
 * <copyright>
 * </copyright>
 *
 * $Id: SqldatabaseFactory.java,v 1.1 2012/03/06 10:18:35 rdedios Exp $
 */
package sqldatabase;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see sqldatabase.SqldatabasePackage
 * @generated
 */
public interface SqldatabaseFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SqldatabaseFactory eINSTANCE = sqldatabase.impl.SqldatabaseFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>SQL Database</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>SQL Database</em>'.
	 * @generated
	 */
	SQLDatabase createSQLDatabase();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SqldatabasePackage getSqldatabasePackage();

} //SqldatabaseFactory
