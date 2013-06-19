/**
 * <copyright>
 * </copyright>
 *
 * $Id: ColumnItemProvider.java,v 1.21 2012/01/23 15:15:11 aalvamat Exp $
 */
package com.isb.datamodeler.tables.provider;


import java.util.Collection;
import java.util.Collections;
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

import com.isb.datamodeler.constraints.EPrimaryKey;
import com.isb.datamodeler.constraints.EReferenceConstraint;
import com.isb.datamodeler.constraints.EUniqueConstraint;
import com.isb.datamodeler.provider.IItemPropertyDescriptorProvider;
import com.isb.datamodeler.schema.provider.DatamodelerEditPlugin;
import com.isb.datamodeler.schema.provider.TypedElementItemProvider;
import com.isb.datamodeler.tables.EColumn;
import com.isb.datamodeler.tables.ETablesPackage;

/**
 * This is the item provider adapter for a {@link com.isb.datamodeler.tables.EColumn} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ColumnItemProvider
	extends TypedElementItemProvider
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
	public ColumnItemProvider(AdapterFactory adapterFactory) {
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

			addNullablePropertyDescriptor(object);
			addDefaultValuePropertyDescriptor(object);
			addPrimaryKeyPropertyDescriptor(object);
			addPartOfForeingKeyPropertyDescriptor(object);
			addPrimitiveTypePropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Nullable feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNullablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Column_nullable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Column_nullable_feature", "_UI_Column_type"),
				 ETablesPackage.Literals.COLUMN__NULLABLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Default Value feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDefaultValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Column_defaultValue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Column_defaultValue_feature", "_UI_Column_type"),
				 ETablesPackage.Literals.COLUMN__DEFAULT_VALUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Primary Key feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPrimaryKeyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Column_primaryKey_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Column_primaryKey_feature", "_UI_Column_type"),
				 ETablesPackage.Literals.COLUMN__PRIMARY_KEY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Part Of Foreing Key feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPartOfForeingKeyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Column_partOfForeingKey_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Column_partOfForeingKey_feature", "_UI_Column_type"),
				 ETablesPackage.Literals.COLUMN__PART_OF_FOREING_KEY,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Primitive Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPrimitiveTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Column_primitiveType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Column_primitiveType_feature", "_UI_Column_type"),
				 ETablesPackage.Literals.COLUMN__PRIMITIVE_TYPE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDescriptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Column_description_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Column_description_feature", "_UI_Column_type"),
				 ETablesPackage.Literals.COLUMN__DESCRIPTION,
				 true,
				 true,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
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
			childrenFeatures.add(ETablesPackage.Literals.COLUMN__IDENTITY_SPECIFIER);
			childrenFeatures.add(ETablesPackage.Literals.COLUMN__GENERATE_EXPRESSION);
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
	 * This returns Column.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated not
	 */
	@Override
	public Object getImage(Object object) {
		
		Object image = getResourceLocator().getImage("full/obj16/Column");
		
		EColumn column  = (EColumn)object;
		
		if(column.isPrimaryKey() && !column.isPartOfForeingKey())
			image = getResourceLocator().getImage("full/obj16/Column_pkey");
		
		if(column.isPrimaryKey() && column.isPartOfForeingKey())
			image = getResourceLocator().getImage("full/obj16/Column_pkfk");
	
		if(!column.isPrimaryKey() && column.isPartOfForeingKey())
			image = getResourceLocator().getImage("full/obj16/Column_fk");
		
		return overlayImage(object, image);
	}
	
	

	@Override
	public Collection<?> getChildren(Object object) {
		
		//El navegador muestra hasta las columnas
		if(object instanceof EColumn)
			return Collections.EMPTY_LIST;
		
		return super.getChildren(object);
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		
		EColumn column = (EColumn)object;
		
		String label = column.getName();
		
		if(label == null || label.length() == 0)
			return getString("_UI_Column_type");

		if(column.getDataType()!=null)
		{
			IItemLabelProvider dataTypeLabelProvider = (IItemLabelProvider)((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory().adapt(((EColumn)object).getDataType(), IItemLabelProvider.class);
			
			boolean isPK = false;
			boolean isUK = false;
			
			for(EReferenceConstraint constraint: column.getReferencingConstraints())
			{
				if(constraint instanceof EPrimaryKey)
					isPK = true;
				else if(constraint instanceof EUniqueConstraint)
					isUK = true;
			}
			
			String dataType = dataTypeLabelProvider.getText(((EColumn)object).getDataType());
			String labelPK = isPK?"PK ":"";
			String labelFK = column.isPartOfForeingKey()?"FK ":"";
			String labelUK = isUK?"UK ":"";
			String notNullLabel = column.isNullable()?"NULL":"NOT NULL";
			
			String indicators = " : " + dataType + " [" +labelPK+labelFK+labelUK+notNullLabel+ "]";
			
			label = label + indicators;
		}
		
		return label;
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

		switch (notification.getFeatureID(EColumn.class)) {
			case ETablesPackage.COLUMN__IMPLEMENTATION_DEPENDENT:
			case ETablesPackage.COLUMN__NULLABLE:
			case ETablesPackage.COLUMN__DEFAULT_VALUE:
			case ETablesPackage.COLUMN__SCOPE_CHECK:
			case ETablesPackage.COLUMN__SCOPE_CHECKED:
			case ETablesPackage.COLUMN__PRIMARY_KEY:
			case ETablesPackage.COLUMN__PART_OF_FOREING_KEY:
			case ETablesPackage.COLUMN__PART_OF_UNIQUE_CONSTRAINT:
			case ETablesPackage.COLUMN__DESCRIPTION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ETablesPackage.COLUMN__IDENTITY_SPECIFIER:
			case ETablesPackage.COLUMN__GENERATE_EXPRESSION:
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
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return DatamodelerEditPlugin.INSTANCE;
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
