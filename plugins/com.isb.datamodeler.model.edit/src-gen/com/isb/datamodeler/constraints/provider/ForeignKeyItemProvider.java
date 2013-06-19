/**
 * <copyright>
 * </copyright>
 *
 * $Id: ForeignKeyItemProvider.java,v 1.17 2012/01/26 17:31:29 rdedios Exp $
 */
package com.isb.datamodeler.constraints.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.isb.datamodeler.constraints.EConstraintsPackage;
import com.isb.datamodeler.constraints.EForeignKey;
import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.schema.provider.DatamodelerEditPlugin;

/**
 * This is the item provider adapter for a {@link com.isb.datamodeler.constraints.EForeignKey} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ForeignKeyItemProvider
	extends ReferenceConstraintItemProvider
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
	public ForeignKeyItemProvider(AdapterFactory adapterFactory) {
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

			addOnUpdatePropertyDescriptor(object);
			addOnDeletePropertyDescriptor(object);
			addUniqueConstraintPropertyDescriptor(object);
			addReferencedMembersPropertyDescriptor(object);
			addUniqueIndexPropertyDescriptor(object);
			addParentTablePropertyDescriptor(object);
			addParentTableSchemaPropertyDescriptor(object);
			addIdentifyingPropertyDescriptor(object);
			addBaseCardinalityPropertyDescriptor(object);
			addParentCardinalityPropertyDescriptor(object);
			addBaseRolePropertyDescriptor(object);
			addParentRolePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the On Update feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOnUpdatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_onUpdate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_onUpdate_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__ON_UPDATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the On Delete feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOnDeletePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_onDelete_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_onDelete_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__ON_DELETE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Unique Constraint feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUniqueConstraintPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_uniqueConstraint_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_uniqueConstraint_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__UNIQUE_CONSTRAINT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Referenced Members feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReferencedMembersPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_referencedMembers_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_referencedMembers_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__REFERENCED_MEMBERS,
				 false,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Unique Index feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUniqueIndexPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_uniqueIndex_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_uniqueIndex_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__UNIQUE_INDEX,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Parent Table feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addParentTablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_parentTable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_parentTable_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__PARENT_TABLE,
				 false,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Parent Table Schema feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addParentTableSchemaPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_parentTableSchema_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_parentTableSchema_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__PARENT_TABLE_SCHEMA,
				 false,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Identifying feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIdentifyingPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_identifying_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_identifying_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__IDENTIFYING,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Cardinality feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseCardinalityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_baseCardinality_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_baseCardinality_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__BASE_CARDINALITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Parent Cardinality feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addParentCardinalityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_parentCardinality_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_parentCardinality_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__PARENT_CARDINALITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Role feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseRolePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_baseRole_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_baseRole_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__BASE_ROLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Parent Role feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addParentRolePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ForeignKey_parentRole_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ForeignKey_parentRole_feature", "_UI_ForeignKey_type"),
				 EConstraintsPackage.Literals.FOREIGN_KEY__PARENT_ROLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns ForeignKey.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ForeignKey"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((EForeignKey)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_ForeignKey_type") :
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

		switch (notification.getFeatureID(EForeignKey.class)) {
			case EConstraintsPackage.FOREIGN_KEY__MATCH:
			case EConstraintsPackage.FOREIGN_KEY__ON_UPDATE:
			case EConstraintsPackage.FOREIGN_KEY__ON_DELETE:
			case EConstraintsPackage.FOREIGN_KEY__REFERENCED_MEMBERS:
			case EConstraintsPackage.FOREIGN_KEY__IDENTIFYING:
			case EConstraintsPackage.FOREIGN_KEY__DEFAULT_IDENTIFYING:
			case EConstraintsPackage.FOREIGN_KEY__BASE_CARDINALITY:
			case EConstraintsPackage.FOREIGN_KEY__PARENT_CARDINALITY:
			case EConstraintsPackage.FOREIGN_KEY__BASE_ROLE:
			case EConstraintsPackage.FOREIGN_KEY__PARENT_ROLE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
	}

    /**
	 * @generated NOT
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
		
		if(feature.getFeatureID() == EConstraintsPackage.FOREIGN_KEY__MEMBERS || 
				feature.getFeatureID() == EConstraintsPackage.FOREIGN_KEY__REFERENCED_MEMBERS)
		{
			staticImage = DatamodelerEditPlugin.INSTANCE.getImage("full/obj16/Column");
		}
		
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
