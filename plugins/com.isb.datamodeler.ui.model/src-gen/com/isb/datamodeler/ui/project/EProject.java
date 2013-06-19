/**
 * <copyright>
 * </copyright>
 *
 * $Id: EProject.java,v 1.8 2011/12/20 12:28:52 rdedios Exp $
 */
package com.isb.datamodeler.ui.project;

import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;

import com.isb.datamodeler.schema.EDataModelerNamedElement;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.EFunctionalElement;
import com.isb.datamodeler.schema.ESchema;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.isb.datamodeler.ui.project.EProject#getDatabase <em>Database</em>}</li>
 *   <li>{@link com.isb.datamodeler.ui.project.EProject#getSchemas <em>Schemas</em>}</li>
 *   <li>{@link com.isb.datamodeler.ui.project.EProject#getDescription <em>Description</em>}</li>
 *   <li>{@link com.isb.datamodeler.ui.project.EProject#getApplication <em>Application</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.isb.datamodeler.ui.project.EProjectPackage#getProject()
 * @model
 * @generated
 */
public interface EProject extends EFunctionalElement, EDataModelerNamedElement {
	/**
	 * Returns the value of the '<em><b>Database</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Database</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Database</em>' containment reference.
	 * @see #setDatabase(EDatabase)
	 * @see com.isb.datamodeler.ui.project.EProjectPackage#getProject_Database()
	 * @model containment="true"
	 * @generated
	 */
	EDatabase getDatabase();

	/**
	 * Sets the value of the '{@link com.isb.datamodeler.ui.project.EProject#getDatabase <em>Database</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Database</em>' containment reference.
	 * @see #getDatabase()
	 * @generated
	 */
	void setDatabase(EDatabase value);

	/**
	 * Returns the value of the '<em><b>Schemas</b></em>' containment reference list.
	 * The list contents are of type {@link com.isb.datamodeler.schema.ESchema}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schemas</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schemas</em>' containment reference list.
	 * @see com.isb.datamodeler.ui.project.EProjectPackage#getProject_Schemas()
	 * @model containment="true"
	 * @generated
	 */
	EList<ESchema> getSchemas();

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see com.isb.datamodeler.ui.project.EProjectPackage#getProject_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link com.isb.datamodeler.ui.project.EProject#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Application</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Application</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Application</em>' attribute.
	 * @see #setApplication(String)
	 * @see com.isb.datamodeler.ui.project.EProjectPackage#getProject_Application()
	 * @model
	 * @generated
	 */
	String getApplication();

	/**
	 * Sets the value of the '{@link com.isb.datamodeler.ui.project.EProject#getApplication <em>Application</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Application</em>' attribute.
	 * @see #getApplication()
	 * @generated
	 */
	void setApplication(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="com.isb.datamodeler.ui.validation.delegates.ProjectValidationDelegate body='indiferent'"
	 * @generated
	 */
	boolean isValid(DiagnosticChain diagnostics, Map<Object, Object> context);

} // EProject
