/**
 * <copyright>
 * </copyright>
 *
 * $Id: EProjectImpl.java,v 1.12 2012/06/11 09:31:05 aalvamat Exp $
 */
package com.isb.datamodeler.ui.project.impl;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EcorePackage;

import com.isb.datamodeler.schema.EDataModelerNamedElement;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.schema.impl.EFunctionalElementImpl;
import com.isb.datamodeler.ui.project.EProject;
import com.isb.datamodeler.ui.project.EProjectPackage;
import com.isb.datamodeler.ui.project.util.ProjectValidator;
import java.lang.reflect.InvocationTargetException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.isb.datamodeler.ui.project.impl.EProjectImpl#getEAnnotations <em>EAnnotations</em>}</li>
 *   <li>{@link com.isb.datamodeler.ui.project.impl.EProjectImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.isb.datamodeler.ui.project.impl.EProjectImpl#getDatabase <em>Database</em>}</li>
 *   <li>{@link com.isb.datamodeler.ui.project.impl.EProjectImpl#getSchemas <em>Schemas</em>}</li>
 *   <li>{@link com.isb.datamodeler.ui.project.impl.EProjectImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.isb.datamodeler.ui.project.impl.EProjectImpl#getApplication <em>Application</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EProjectImpl extends EFunctionalElementImpl implements EProject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EProjectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EProjectPackage.Literals.PROJECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<EAnnotation> getEAnnotations() {
		return (EList<EAnnotation>)eGet(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return (String)eGet(ESchemaPackage.Literals.DATA_MODELER_NAMED_ELEMENT__NAME, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		eSet(ESchemaPackage.Literals.DATA_MODELER_NAMED_ELEMENT__NAME, newName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDatabase getDatabase() {
		return (EDatabase)eGet(EProjectPackage.Literals.PROJECT__DATABASE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDatabase(EDatabase newDatabase) {
		eSet(EProjectPackage.Literals.PROJECT__DATABASE, newDatabase);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<ESchema> getSchemas() {
		return (EList<ESchema>)eGet(EProjectPackage.Literals.PROJECT__SCHEMAS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return (String)eGet(EProjectPackage.Literals.PROJECT__DESCRIPTION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		eSet(EProjectPackage.Literals.PROJECT__DESCRIPTION, newDescription);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getApplication() {
		return (String)eGet(EProjectPackage.Literals.PROJECT__APPLICATION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setApplication(String newApplication) {
		eSet(EProjectPackage.Literals.PROJECT__APPLICATION, newApplication);
	}

	/**
	 * The cached validation expression for the '{@link #isValid(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Is Valid</em>}' invariant operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isValid(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 * @ordered
	 */
	protected static final String IS_VALID_DIAGNOSTIC_CHAIN_MAP__EEXPRESSION = "indiferent";

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isValid(DiagnosticChain diagnostics, Map<Object, Object> context) {
		return
			ProjectValidator.validate
				(EProjectPackage.Literals.PROJECT,
				 this,
				 diagnostics,
				 context,
				 "com.isb.datamodeler.ui.validation.delegates.ProjectValidationDelegate",
				 EProjectPackage.Literals.PROJECT.getEOperations().get(0),
				 IS_VALID_DIAGNOSTIC_CHAIN_MAP__EEXPRESSION,
				 Diagnostic.ERROR,
				 ProjectValidator.DIAGNOSTIC_SOURCE,
				 ProjectValidator.PROJECT__IS_VALID);
	}

	/**
	 * The cached invocation delegate for the '{@link #getIProject() <em>Get IProject</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIProject()
	 * @generated
	 * @ordered
	 */
	protected static final EOperation.Internal.InvocationDelegate GET_IPROJECT__EINVOCATION_DELEGATE = ((EOperation.Internal)ESchemaPackage.Literals.DATA_MODELER_NAMED_ELEMENT___GET_IPROJECT).getInvocationDelegate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IProject getIProject() {
		try {
			return (IProject)GET_IPROJECT__EINVOCATION_DELEGATE.dynamicInvoke(this, null);
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
	public EAnnotation getEAnnotation(String source) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == EModelElement.class) {
			switch (derivedFeatureID) {
				case EProjectPackage.PROJECT__EANNOTATIONS: return EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;
				default: return -1;
			}
		}
		if (baseClass == EDataModelerNamedElement.class) {
			switch (derivedFeatureID) {
				case EProjectPackage.PROJECT__NAME: return ESchemaPackage.DATA_MODELER_NAMED_ELEMENT__NAME;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == EModelElement.class) {
			switch (baseFeatureID) {
				case EcorePackage.EMODEL_ELEMENT__EANNOTATIONS: return EProjectPackage.PROJECT__EANNOTATIONS;
				default: return -1;
			}
		}
		if (baseClass == EDataModelerNamedElement.class) {
			switch (baseFeatureID) {
				case ESchemaPackage.DATA_MODELER_NAMED_ELEMENT__NAME: return EProjectPackage.PROJECT__NAME;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //EProjectImpl
