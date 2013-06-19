package com.isb.datamodeler.model.core;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.core.util.CrossReferenceAdapter;

import com.isb.datamodeler.model.core.editingDomain.DataModelerEditingDomainFactory;

public class DataModelerUtils {

	public static TransactionalEditingDomain getDataModelerEditingDomain() 
	{
		return TransactionalEditingDomain.Registry.INSTANCE.getEditingDomain(DataModelerEditingDomainFactory.DATA_MODELER_EDITING_DOMAIN_ID);
	}

	/**
	 * @param idLength number of digits to return in the random string generated
	 * @return A random identifier of up to idLength digits
	 */
	public static String generateRandomID(int idLength)
	{
	    String finalString = ""; //$NON-NLS-1$

	    while(finalString.length()<idLength)
	    {
	    	// Generamos el nº aleatorio y quitamos el "0." para que no acabe nunca en "."
	    	String auxString = String.valueOf(Math.random()).substring(2);
	    	 finalString += auxString.substring(2);
	    }
	    
		return finalString.substring(0,idLength);
	}
//	public static String getDataModelerProject(EObject vegaElement)
//	{
//		Resource resource = vegaElement.eResource();
//		
//		return (resource!=null) ? resource.getURI().segment(1) : null;
//
//	}

	public static void destroyElement(EObject destructee)
	{
		// only destroy attached elements
		if ((destructee != null) && (destructee.eResource() != null)) {
			// tear down incoming references
			tearDownIncomingReferences(destructee);
			
			// also tear down outgoing references, because we don't want
			//    reverse-reference lookups to find destroyed objects
			tearDownOutgoingReferences(destructee);
			
			// remove the object from its container
			EcoreUtil.remove(destructee);
			
			// in case it was cross-resource-contained
			Resource res = destructee.eResource();
			if (res != null) {
				res.getContents().remove(destructee);
			}
		}

	}
	
	/**
	 * Tears down references to the object that we are destroying, from all other
	 * objects in the resource set.
	 * 
	 * @param destructee the object being destroyed
	 */
	public static void tearDownIncomingReferences(EObject destructee) {
		CrossReferenceAdapter crossReferencer = CrossReferenceAdapter
			.getExistingCrossReferenceAdapter(destructee);
		if (crossReferencer != null) {
			Collection<EStructuralFeature.Setting> inverseReferences = crossReferencer
				.getInverseReferences(destructee);
			if (inverseReferences != null) {
				int size = inverseReferences.size();
				if (size > 0) {
					Setting setting;
					EReference eRef;
					Setting[] settings = (Setting[]) inverseReferences
						.toArray(new Setting[size]);
					for (int i = 0; i < size; ++i) {
						setting = settings[i];
						eRef = (EReference) setting.getEStructuralFeature();
						if (eRef.isChangeable() && (eRef.isDerived() == false)
							&& (eRef.isContainment() == false)
							&& (eRef.isContainer() == false)) {
							EcoreUtil.remove(setting.getEObject(), eRef,
								destructee);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Tears down outgoing unidirectional references from the object being
	 * destroyed to all other elements in the resource set.  This is required
	 * so that reverse-reference queries will not find the destroyed object.
	 * 
	 * @param destructee the object being destroyed
	 */
	public static void tearDownOutgoingReferences(EObject destructee) {
		for (Iterator<EReference> iter = destructee.eClass().getEAllReferences().iterator(); iter.hasNext();) {
			EReference reference = iter.next();
			
			// container/containment features are handled separately, and
			//   bidirectional references were handled via incomings
			if (reference.isChangeable() && !reference.isDerived()
					&& !reference.isContainer() && !reference.isContainment()
					&& (reference.getEOpposite() == null)) {
				
				if (destructee.eIsSet(reference)) {
					destructee.eUnset(reference);
				}
			}
		}
	}
}
