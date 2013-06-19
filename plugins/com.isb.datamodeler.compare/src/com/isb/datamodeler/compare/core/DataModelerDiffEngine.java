package com.isb.datamodeler.compare.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.engine.GenericDiffEngine;
import org.eclipse.emf.compare.diff.engine.check.AttributesCheck;
import org.eclipse.emf.compare.diff.engine.check.ReferencesCheck;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.UnmatchElement;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;
import org.eclipse.gmf.runtime.notation.View;

import com.isb.datamodeler.accesscontrol.EAccesscontrolPackage;
import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.datatypes.EDatatypesPackage;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.datatypes.ESQLDataType;
import com.isb.datamodeler.datatypes.impl.EPredefinedDataTypeImpl;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.schema.ESQLObject;
import com.isb.datamodeler.schema.ESchema;
import com.isb.datamodeler.schema.ESchemaPackage;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETablesPackage;
import com.isb.datamodeler.ui.project.EProject;
import com.isb.datamodeler.ui.project.EProjectPackage;

/**
 * Motor de diferencias específico para DataModeler. Es necesario
 * porque hay elementos dentro del modelo que no debemos comparar y omitirlos.
 * 
 * (ejemplo el ID interno de los elementos DataModeler)
 * 
 * @author Alfonso
 *
 */
public class DataModelerDiffEngine extends GenericDiffEngine {

	class DataModelerAttributesCheck extends AttributesCheck {

		public DataModelerAttributesCheck(CrossReferencer referencer) {
			super(referencer);			
		}
		
		
		@Override
		protected boolean shouldBeIgnored(EAttribute attribute) {
			
			// No tenemos en cuenta el id interno
			if (attribute.getFeatureID()== EAccesscontrolPackage.ROLE_AUTHORIZATION__ID)
				return true;
			
			// No comparamos estos atributos, porque aunque no estan como derivados
			// son calculados, y provocan confusiones en los comparadores.
			if (!(attribute.getName().equals("description")) && attribute.getFeatureID() == ETablesPackage.COLUMN__PART_OF_FOREING_KEY)
				return true;
			
			if (!(attribute.getName().equals("description")) && attribute.getFeatureID() == ETablesPackage.COLUMN__PRIMARY_KEY)
				return true;
			
			if (attribute.getFeatureID() == ETablesPackage.COLUMN__PART_OF_UNIQUE_CONSTRAINT)
				return true;						
			
			if (attribute.getName().equals("description") && attribute.getFeatureID() == EConstraintsPackage.FOREIGN_KEY__DESCRIPTION)
				return true;
 			
			// No debemos comparar este atributo ya que es interno.
			if (attribute.getName().equals("defaultIdentifying") && attribute.getFeatureID() == EConstraintsPackage.FOREIGN_KEY__DEFAULT_IDENTIFYING)
				return true;
												
			// No se muestran las diferencias de los atributos de los EProject
			if (attribute.equals(EProjectPackage.eINSTANCE.getProject_Application()))
				return true;
			if (attribute.getFeatureID() == EProjectPackage.PROJECT__CAPABILITY)
				return true;			
			
			//if (attribute.equals(EProjectPackage.eINSTANCE.getProject_Database()))
			//	return true;
			
			// Los atributos calculados no tienen sentido que se marquen como diferencias
			// Un caso que nos hemos encontrado es el "partOfUniqueConstraint". Hemos puesto el derived
			// a true en el ecore porque no tiene sentido.
			if (attribute.isDerived())
				return true;							
								
			return super.shouldBeIgnored(attribute);
		}
				
		
		protected void checkAttributeUpdates(DiffGroup root, Match2Elements mapping, EAttribute attribute) throws FactoryException 
		{
			// No tenemos en cuenta el nombre de los proyectos al comparar. 
			if (attribute.getFeatureID() == ESchemaPackage.DATA_MODELER_NAMED_ELEMENT__NAME) {
					if (mapping.getLeftElement() instanceof EProject && mapping.getRightElement() instanceof EProject)			
						return;
					if (mapping.getLeftElement() instanceof ESQLDataType && mapping.getRightElement() instanceof ESQLDataType)
						return;					
			}
			
			if (attribute.getFeatureID() == EDatatypesPackage.PRIMITIVE_DATA_TYPE) {			
				if (mapping.getLeftElement() instanceof EPrimitiveDataType)
					if (((EPrimitiveDataType)mapping.getLeftElement()).getName().equals (((EPrimitiveDataType)mapping.getRightElement()).getName()))
						return;
			}			
						 			
			if (mapping.getLeftElement() instanceof EPredefinedDataTypeImpl && mapping.getRightElement() instanceof EPredefinedDataTypeImpl &&			 
			 !((((EPredefinedDataTypeImpl)mapping.getLeftElement()).getPrimitiveType().getId().equals( 
					 (((EPredefinedDataTypeImpl)mapping.getRightElement()).getPrimitiveType().getId())))))			
				return;				
			
			// Bugzilla 20562  [QA] Se muestra como diferencia '' y 'null'. 
			if (!attribute.isMany()) {
				 String attributeName = attribute.getName();
				final Object leftValue = EFactory.eGet(mapping.getLeftElement(), attributeName);
				final Object rightValue = EFactory.eGet(mapping.getRightElement(), attributeName);
				
				if ((leftValue == null && rightValue != null && rightValue instanceof String && ((String)rightValue).equals("")) ||
						(rightValue == null && leftValue != null && leftValue instanceof String && ((String)leftValue).equals("")))
							return;				
			}
													
			super.checkAttributeUpdates(root, mapping, attribute);
		}
		
		@Override
		protected boolean areDistinctValues(Object left, Object right) {
			if (left instanceof String) { 
				if(((String) left).equalsIgnoreCase((String) right))	
					return false;
				else
					return true;
			}
				
			return super.areDistinctValues(left, right);
		}
	}
	
	class DataModelerReferencesCheck extends ReferencesCheck {

		public DataModelerReferencesCheck(CrossReferencer referencer) {
			super(referencer);			
		}
		
		@Override
		protected void checkReferenceUpdates(DiffGroup root,
				Match2Elements mapping, EReference reference)
				throws FactoryException {
			
			if (DataModelerCompareFilterContent.getInstance().isActive() && 
					DataModelerCompareFilterContent.getInstance().takeFilter(mapping.getLeftElement()))
				return;
			
			/*if (mapping.getLeftElement() instanceof ESQLDataType)
				return;*/
			
			if (mapping.getLeftElement() instanceof EDatabase)
				return;
			
			if (mapping.getLeftElement() instanceof View)
				return;
			
			if (reference.getName().equals("uniqueConstraint") && 
			(mapping.getLeftElement().eGet(reference) == null ||
			mapping.getRightElement().eGet(reference) == null))
				return;					
			
			if (reference.getName().equals("primitiveType") && mapping.getLeftElement() instanceof EColumn &&
					!((ESQLObject)(mapping.getLeftElement().eGet(reference))).getLabel().equals(
					(((ESQLObject)(mapping.getRightElement().eGet(reference))).getLabel())))
			{
				ESQLObject e1 = (ESQLObject) mapping.getLeftElement().eGet(reference);
				ESQLObject e2 = (ESQLObject) mapping.getRightElement().eGet(reference);
				root.getSubDiffElements().add(
						createUpdatedReferenceOperation(mapping.getLeftElement(), mapping.getRightElement(), reference, e1, e2));
			} else if  (reference.getName().equals("primitiveType") &&
					((ESQLObject)(mapping.getLeftElement().eGet(reference))).getLabel().equals(
					(((ESQLObject)(mapping.getRightElement().eGet(reference))).getLabel())))
				return;
			
			super.checkReferenceUpdates(root, mapping, reference);
		}
		
		private UpdateReference createUpdatedReferenceOperation(EObject left, EObject right,
				EReference reference, EObject addedValue, EObject deletedValue) {
			final UpdateReference operation = DiffFactory.eINSTANCE.createUpdateReference();
			operation.setLeftElement(left);
			operation.setRightElement(right);
			operation.setReference(reference);

			EObject leftTarget = getMatchedEObject(deletedValue);
			EObject rightTarget = getMatchedEObject(addedValue);
			// checks if target are defined remotely
			if (leftTarget == null && deletedValue != null) {
				leftTarget = deletedValue;
			}
			if (rightTarget == null && addedValue != null) {
				rightTarget = addedValue;
			}

			operation.setLeftTarget(leftTarget);
			operation.setRightTarget(rightTarget);

			return operation;
		}
		
		/**		
		 * Por ahora tendremos en cuenta el orden en la comparaciï¿½n... 
		 * Pero desde aquï¿½ se puede no tener en cuenta el orden.
		 */
		@Override
		protected void checkReferenceOrderChange(DiffGroup root,
				EReference reference, EObject leftElement,
				EObject rightElement,
				List<ReferenceChangeLeftTarget> addedReferences,
				List<ReferenceChangeRightTarget> removedReferences)
				throws FactoryException {			
				//doNothing(); -->			
				if (reference.getName().equals("members") && (addedReferences.size()>0 || removedReferences.size() >0))
					return;

				super.checkReferenceOrderChange(root, reference, leftElement, rightElement, addedReferences, removedReferences);
		}
		
		
		
		@Override
		protected void checkContainmentReferenceOrderChange(DiffGroup root,
				Match2Elements mapping, EReference reference)
				throws FactoryException {			
				//doNothing() --> 
			
				// Bugzilla 21095. [QA] Se muestra como diferencia en el esquema el orden de las tablas.
				if (mapping != null && mapping.getLeftElement() instanceof ESchema && reference.getName().equals("tables"))
					return;
				super.checkContainmentReferenceOrderChange(root, mapping, reference);
		}
						
		@Override
		protected boolean shouldBeIgnored(EReference reference) {						
				return super.shouldBeIgnored(reference);
		}
	}
	
	@Override
	protected ReferencesCheck getReferencesChecker() {
		return new DataModelerReferencesCheck(matchCrossReferencer);
	}
	
		
	@Override
	protected AttributesCheck getAttributesChecker() {		
		return new DataModelerAttributesCheck(matchCrossReferencer);
	}
	
	@Override
	protected void processUnmatchedElements(DiffGroup diffRoot,
			List<UnmatchElement> unmatched) {
		
		List<UnmatchElement> deleteElements = new ArrayList<UnmatchElement> ();
		
		for (Iterator<UnmatchElement> it = unmatched.iterator(); it.hasNext(); )
		{
			UnmatchElement auxUnmatchElement = it.next();
			
			if (auxUnmatchElement.getElement() instanceof View)
				deleteElements.add(auxUnmatchElement);		
			else if (auxUnmatchElement.getElement() instanceof EDatabase)
				deleteElements.add(auxUnmatchElement);
			else if (DataModelerCompareFilterContent.getInstance().isActive() && 
					DataModelerCompareFilterContent.getInstance().takeFilter(auxUnmatchElement.getElement()))
				deleteElements.add(auxUnmatchElement);
		}
		
		unmatched.removeAll(deleteElements);
		
		super.processUnmatchedElements(diffRoot, unmatched);
	}
	
	@Override
	protected void checkContainmentUpdate(DiffGroup current,
			Match2Elements matchElement) {
		if (DataModelerCompareFilterContent.getInstance().isActive() && 
				DataModelerCompareFilterContent.getInstance().takeFilter(matchElement.getLeftElement()))
				return;
		
		super.checkContainmentUpdate(current, matchElement);
	}
}
