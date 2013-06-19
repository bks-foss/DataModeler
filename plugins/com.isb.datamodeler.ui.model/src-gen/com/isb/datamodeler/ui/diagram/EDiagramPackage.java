/**
 * <copyright>
 * </copyright>
 *
 * $Id: EDiagramPackage.java,v 1.1 2011/11/10 14:27:36 aalvamat Exp $
 */
package com.isb.datamodeler.ui.diagram;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.isb.datamodeler.ui.diagram.EDiagramFactory
 * @model kind="package"
 * @generated
 */
public interface EDiagramPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "diagram";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///com/isb/datamodeler/ui/model/diagram.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "Diagram";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EDiagramPackage eINSTANCE = com.isb.datamodeler.ui.diagram.impl.EDiagramPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.isb.datamodeler.ui.diagram.impl.EDMDiagramImpl <em>DM Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.isb.datamodeler.ui.diagram.impl.EDMDiagramImpl
	 * @see com.isb.datamodeler.ui.diagram.impl.EDiagramPackageImpl#getDMDiagram()
	 * @generated
	 */
	int DM_DIAGRAM = 0;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__EANNOTATIONS = NotationPackage.DIAGRAM__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__VISIBLE = NotationPackage.DIAGRAM__VISIBLE;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__TYPE = NotationPackage.DIAGRAM__TYPE;

	/**
	 * The feature id for the '<em><b>Mutable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__MUTABLE = NotationPackage.DIAGRAM__MUTABLE;

	/**
	 * The feature id for the '<em><b>Source Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__SOURCE_EDGES = NotationPackage.DIAGRAM__SOURCE_EDGES;

	/**
	 * The feature id for the '<em><b>Target Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__TARGET_EDGES = NotationPackage.DIAGRAM__TARGET_EDGES;

	/**
	 * The feature id for the '<em><b>Persisted Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__PERSISTED_CHILDREN = NotationPackage.DIAGRAM__PERSISTED_CHILDREN;

	/**
	 * The feature id for the '<em><b>Styles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__STYLES = NotationPackage.DIAGRAM__STYLES;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__ELEMENT = NotationPackage.DIAGRAM__ELEMENT;

	/**
	 * The feature id for the '<em><b>Diagram</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__DIAGRAM = NotationPackage.DIAGRAM__DIAGRAM;

	/**
	 * The feature id for the '<em><b>Transient Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__TRANSIENT_CHILDREN = NotationPackage.DIAGRAM__TRANSIENT_CHILDREN;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__NAME = NotationPackage.DIAGRAM__NAME;

	/**
	 * The feature id for the '<em><b>Measurement Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__MEASUREMENT_UNIT = NotationPackage.DIAGRAM__MEASUREMENT_UNIT;

	/**
	 * The feature id for the '<em><b>Persisted Edges</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__PERSISTED_EDGES = NotationPackage.DIAGRAM__PERSISTED_EDGES;

	/**
	 * The feature id for the '<em><b>Transient Edges</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM__TRANSIENT_EDGES = NotationPackage.DIAGRAM__TRANSIENT_EDGES;

	/**
	 * The number of structural features of the '<em>DM Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DM_DIAGRAM_FEATURE_COUNT = NotationPackage.DIAGRAM_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link com.isb.datamodeler.ui.diagram.EDMDiagram <em>DM Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>DM Diagram</em>'.
	 * @see com.isb.datamodeler.ui.diagram.EDMDiagram
	 * @generated
	 */
	EClass getDMDiagram();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EDiagramFactory getDiagramFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.isb.datamodeler.ui.diagram.impl.EDMDiagramImpl <em>DM Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.isb.datamodeler.ui.diagram.impl.EDMDiagramImpl
		 * @see com.isb.datamodeler.ui.diagram.impl.EDiagramPackageImpl#getDMDiagram()
		 * @generated
		 */
		EClass DM_DIAGRAM = eINSTANCE.getDMDiagram();

	}

} //EDiagramPackage
