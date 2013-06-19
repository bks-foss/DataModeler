package com.isb.datamodeler.diagram.navigator;

import org.eclipse.core.internal.runtime.AdapterManager;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

/**
 * @generated
 */
public class DatamodelerDomainNavigatorItem extends PlatformObject {

	/**
	 * @generated
	 */
	static {
		final Class[] supportedTypes = new Class[] { EObject.class,
				IPropertySource.class };
		Platform.getAdapterManager().registerAdapters(
				new IAdapterFactory() {

					public Object getAdapter(Object adaptableObject,
							Class adapterType) {
						if (adaptableObject instanceof com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem) {
							com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem domainNavigatorItem = (com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem) adaptableObject;
							EObject eObject = domainNavigatorItem.getEObject();
							if (adapterType == EObject.class) {
								return eObject;
							}
							if (adapterType == IPropertySource.class) {
								return domainNavigatorItem
										.getPropertySourceProvider()
										.getPropertySource(eObject);
							}
						}

						return null;
					}

					public Class[] getAdapterList() {
						return supportedTypes;
					}
				},
				com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem.class);
	}

	/**
	 * @generated
	 */
	private Object myParent;

	/**
	 * @generated
	 */
	private EObject myEObject;

	/**
	 * @generated
	 */
	private IPropertySourceProvider myPropertySourceProvider;

	private int hashCode = -1;

	/**
	 * @generated not
	 */
	public DatamodelerDomainNavigatorItem(EObject eObject, Object parent,
			IPropertySourceProvider propertySourceProvider) {
		myParent = parent;
		myEObject = eObject;
		//Calculamos y guardamos el hashcode en vez de calcularlo cada vez con el getHashCode (Bug# 20283)
		hashCode = EcoreUtil.getURI(myEObject).hashCode();
		myPropertySourceProvider = propertySourceProvider;
	}

	/**
	 * @generated
	 */
	public Object getParent() {
		return myParent;
	}

	/**
	 * @generated
	 */
	public EObject getEObject() {
		return myEObject;
	}

	/**
	 * @generated
	 */
	public IPropertySourceProvider getPropertySourceProvider() {
		return myPropertySourceProvider;
	}

	/**
	 * @generated
	 */
	public boolean equals(Object obj) {
		if (obj instanceof com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem) {
			return EcoreUtil
					.getURI(getEObject())
					.equals(EcoreUtil
							.getURI(((com.isb.datamodeler.diagram.navigator.DatamodelerDomainNavigatorItem) obj)
									.getEObject()));
		}
		return super.equals(obj);
	}

	/**
	 * @generated not
	 */
	public int hashCode() {
		return hashCode;
	}
}
