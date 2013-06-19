package com.isb.datamodeler.compare.merge;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.diff.merge.DefaultMerger;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.isb.datamodeler.datatypes.ECharacterStringDataType;
import com.isb.datamodeler.datatypes.EPredefinedDataType;
import com.isb.datamodeler.datatypes.EPrimitiveDataType;
import com.isb.datamodeler.datatypes.ESQLDataType;
import com.isb.datamodeler.schema.ETypedElement;
import com.isb.datamodeler.tables.EColumn;

/**
 * Merge específico pensado para mergear conflictos con tipos de datos 
 * definidos en columnas que son tipos de datos diferentes.
 * 
 * @author Alfonso
 *
 */
public class DataModelerUpdateReferenceMerger extends DefaultMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#applyInOrigin()
	 * 
	 * Existe una Excepción. Como nos interesa marcar un conflicto en el tipo de dato
	 * contenido en una columna al realizar el merge tiene que hacer una copia del original.
	 */
	@Override
	public void applyInOrigin() {		
		
		final UpdateReference theDiff = (UpdateReference)this.diff;
		final EReference reference = theDiff.getReference();
		final EObject leftElement = theDiff.getLeftElement();
		final EObject rightElement = theDiff.getRightElement();		
		final EObject leftTarget = (EObject)theDiff.getRightElement().eGet(reference);
		final EObject matchedLeftTarget = theDiff.getLeftTarget();
		
		String columnLeftName = "";
		String columnRightName = "";
		
		// Si estamos mergeando EColumn la referencia primitiveType
		if (leftElement instanceof EColumn && reference.getName().equals("primitiveType")) //$NON-NLS-1$
		{
			columnLeftName = ((EColumn)leftElement).getName();
			columnRightName = ((EColumn)rightElement).getName();
			
			if (columnLeftName.equals(columnRightName))
				if (((EColumn)leftElement).getContainedType().getClass() != ((EColumn)rightElement).getContainedType().getClass()) {					
					List<EObject> auxCopierList = new ArrayList<EObject>();
					auxCopierList.add(((EColumn)rightElement).getContainedType());
					auxCopierList.add(((EPredefinedDataType)((EColumn)rightElement).getContainedType()).getPrimitiveType());
					List<EObject> copies = (List<EObject>) EcoreUtil.copyAll(auxCopierList);								
										
					// Modificar la Referencia 					
					((EPredefinedDataType)copies.get(0)).setPrimitiveType((EPrimitiveDataType) matchedLeftTarget);
					((EColumn)leftElement).setContainedType((ESQLDataType) copies.get(0));
				}
				else
				{
					// Nombre de la columna...
					((ESQLDataType)((EColumn)leftElement).getContainedType()).setName(
							((ESQLDataType)((EColumn)rightElement).getContainedType()).getName());
					
					// El tamaño...
					if (((EColumn)leftElement).getContainedType() instanceof ECharacterStringDataType)
						((ECharacterStringDataType)((EColumn)leftElement).getContainedType()).setLength(
								((ECharacterStringDataType)((EColumn)rightElement).getContainedType()).getLength());
					
					//La referencia PrimitiveType...
					((EPredefinedDataType)((EColumn)leftElement).getContainedType()).setPrimitiveType((EPrimitiveDataType) matchedLeftTarget);
				}
		}
		
		// Si estamos mergeando su contentType asociado a una columna. (Columnas iguales referencian el mismo contentType).
		if (leftElement instanceof ESQLDataType && rightElement instanceof ESQLDataType && 
				((ESQLDataType)leftElement).eContainer() instanceof EColumn &&  
				((ESQLDataType)rightElement).eContainer() instanceof EColumn) {
			
			 columnLeftName = ((EColumn)((ESQLDataType)leftElement).eContainer()).getName().trim();
			 columnRightName = ((EColumn)((ESQLDataType)rightElement).eContainer()).getName().trim();
			
			if (columnLeftName.equals(columnRightName)) {
				if (leftElement.getClass() != rightElement.getClass()) {
									
					((ETypedElement)((ESQLDataType)leftElement).eContainer()).setContainedType(
							(ESQLDataType) EcoreUtil.copy(rightElement));						
				}
				else
				{
					// Cambiamos el atributo Name, en algún caso especial por ejemploc cuando coincidan el tipo como en (char y varchar).
					((ESQLDataType)leftElement).setName(
							((ESQLDataType)rightElement).getName());
					
					// El tamaño
					if (leftElement instanceof ECharacterStringDataType)
						((ECharacterStringDataType)leftElement).setLength(((ECharacterStringDataType)rightElement).getLength());
					
					// La referencia en este caso se modifica por el comportamiento por defecto...(no hace falta añadirlo aqui más
					//abajo realiza la copia de la referencia).
				}
			}
								
		}

		if (leftTarget == null) {			
			leftElement.eUnset(reference);
		} else {
			DataModelerMergeService.getCopier(diff).copyReferenceValue(reference, leftElement, leftTarget,
					matchedLeftTarget, -1);
		}
		
		super.applyInOrigin();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.DefaultMerger#undoInTarget()
	 */
	@Override
	public void undoInTarget() {
		
		final UpdateReference theDiff = (UpdateReference)this.diff;
		final EReference reference = theDiff.getReference();
		final EObject leftElement = theDiff.getLeftElement();
		final EObject rightElement = theDiff.getRightElement();		
		final EObject rightTarget = (EObject)theDiff.getLeftElement().eGet(reference);
		final EObject matchedRightTarget = theDiff.getRightTarget();
		
		String columnLeftName = "";
		String columnRightName = "";
		
		// Si estamos mergeando EColumn la referencia primitiveType
		if (leftElement instanceof EColumn && reference.getName().equals("primitiveType"))
		{
			columnLeftName = ((EColumn)leftElement).getName();
			columnRightName = ((EColumn)rightElement).getName();
			
			if (columnLeftName.equals(columnRightName))
				if (((EColumn)rightElement).getContainedType().getClass() != ((EColumn)leftElement).getContainedType().getClass()) {					
					List<EObject> auxCopierList = new ArrayList<EObject>();
					auxCopierList.add(((EColumn)leftElement).getContainedType());
					auxCopierList.add(((EPredefinedDataType)((EColumn)leftElement).getContainedType()).getPrimitiveType());
					List<EObject> copies = (List<EObject>) EcoreUtil.copyAll(auxCopierList);								
										
					// Modificar la Referencia 					
					((EPredefinedDataType)copies.get(0)).setPrimitiveType((EPrimitiveDataType) matchedRightTarget);
					((EColumn)rightElement).setContainedType((ESQLDataType) copies.get(0));
				}
				else
				{
					// Nombre de la columna...
					((ESQLDataType)((EColumn)rightElement).getContainedType()).setName(
							((ESQLDataType)((EColumn)leftElement).getContainedType()).getName());
					
					// El tamaño...
					if (((EColumn)rightElement).getContainedType() instanceof ECharacterStringDataType)
						((ECharacterStringDataType)((EColumn)rightElement).getContainedType()).setLength(
								((ECharacterStringDataType)((EColumn)leftElement).getContainedType()).getLength());
					
					//La referencia PrimitiveType...
					((EPredefinedDataType)((EColumn)rightElement).getContainedType()).setPrimitiveType((EPrimitiveDataType) matchedRightTarget);
				}
		}
		
		// Si estamos mergeando su contentType asociado a una columna. (Columnas iguales referencian el mismo contentType).
		if (leftElement instanceof ESQLDataType && rightElement instanceof ESQLDataType && 
				((ESQLDataType)leftElement).eContainer() instanceof EColumn &&  
				((ESQLDataType)rightElement).eContainer() instanceof EColumn) {
			
			 columnLeftName = ((EColumn)((ESQLDataType)leftElement).eContainer()).getName().trim();
			 columnRightName = ((EColumn)((ESQLDataType)rightElement).eContainer()).getName().trim();
			
			if (columnLeftName.equals(columnRightName)) {
				if (leftElement.getClass() != rightElement.getClass()) {
									
					((ETypedElement)((ESQLDataType)rightElement).eContainer()).setContainedType(
							(ESQLDataType) EcoreUtil.copy(leftElement));						
				}
				else
				{
					// Cambiamos el atributo Name, en algún caso especial por ejemploc cuando coincidan el tipo como en (char y varchar).
					((ESQLDataType)rightElement).setName(
							((ESQLDataType)leftElement).getName());
					
					// El tamaño
					if (rightElement instanceof ECharacterStringDataType)
						((ECharacterStringDataType)rightElement).setLength(((ECharacterStringDataType)leftElement).getLength());
					
					// La referencia en este caso se modifica por el comportamiento por defecto...(no hace falta añadirlo aqui más
					//abajo realiza la copia de la referencia).
				}
			}
								
		}

		if (rightTarget == null) {			
			rightElement.eUnset(reference);
		} else {
			DataModelerMergeService.getCopier(diff).copyReferenceValue(reference, rightElement, rightTarget,
					matchedRightTarget, -1);
		}
		
		super.undoInTarget();
	}
}
