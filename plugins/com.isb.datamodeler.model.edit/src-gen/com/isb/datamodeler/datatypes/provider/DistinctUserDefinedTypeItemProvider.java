/**
 * <copyright>
 * </copyright>
 *
 * $Id: DistinctUserDefinedTypeItemProvider.java,v 1.9 2011/12/20 12:28:45 rdedios Exp $
 */
package com.isb.datamodeler.datatypes.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.isb.datamodeler.datatypes.EDatatypesFactory;
import com.isb.datamodeler.datatypes.EDatatypesPackage;
import com.isb.datamodeler.datatypes.EDistinctUserDefinedType;
import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;

/**
 * This is the item provider adapter for a {@link com.isb.datamodeler.datatypes.EDistinctUserDefinedType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DistinctUserDefinedTypeItemProvider
	extends UserDefinedTypeItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DistinctUserDefinedTypeItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns DistinctUserDefinedType.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/DistinctUserDefinedType"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((EDistinctUserDefinedType)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_DistinctUserDefinedType_type") :
			label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(EDistinctUserDefinedType.class)) {
			case EDatatypesPackage.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createCharacterStringDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createRowIDDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createBooleanDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createIntervalDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createBinaryStringDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createTimeDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createFixedPrecisionDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createDataLinkDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createDateDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createApproximateNumericDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createIntegerDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createXMLDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createBigCharacterStringDataType()));

		newChildDescriptors.add
			(createChildParameter
				(EDatatypesPackage.Literals.DISTINCT_USER_DEFINED_TYPE__PREDEFINED_REPRESENTATION,
				 EDatatypesFactory.eINSTANCE.createBigBinaryStringDataType()));
	}

    /**
	 * @generated
     */
	protected ItemPropertyDescriptor createItemPropertyDescriptor(AdapterFactory adapterFactory,
															       ResourceLocator resourceLocator,
															       String displayName,
															       String description,
															       EStructuralFeature feature, 
															       boolean isSettable,
															       boolean multiLine,
															       boolean sortChoices,
															       Object staticImage,
															       String category,
															       String [] filterFlags) {
		
		ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		
		IItemPropertyDescriptorProvider descriptorProvider = (IItemPropertyDescriptorProvider)composedAdapterFactory.adapt(getTarget(), IItemPropertyDescriptorProvider.class);
		
		if(descriptorProvider!=null)
			return descriptorProvider.createItemPropertyDescriptor(
					adapterFactory, 
					resourceLocator, 
					displayName, 
					description, 
					feature, 
					isSettable, 
					multiLine, 
					sortChoices, 
					staticImage, 
					category, 
					filterFlags);

		return super.createItemPropertyDescriptor(
				adapterFactory, 
				resourceLocator, 
				displayName, 
				description, 
				feature, 
				isSettable, 
				multiLine, 
				sortChoices, 
				staticImage, 
				category, 
				filterFlags);
	}
}
