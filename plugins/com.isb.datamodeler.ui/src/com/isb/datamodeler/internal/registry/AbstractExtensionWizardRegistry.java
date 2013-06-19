package com.isb.datamodeler.internal.registry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.ExtensionTracker;
import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;

import com.isb.datamodeler.internal.ui.wizards.port.DataModelerPortWizardCollectionElement;
import com.isb.datamodeler.internal.ui.wizards.port.DataModelerPortWizardElement;

public abstract class AbstractExtensionWizardRegistry extends
		AbstractWizardRegistry implements IExtensionChangeHandler {

	/**
	 * Create a new instance of this class.
	 */
	public AbstractExtensionWizardRegistry() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.dynamicHelpers.IExtensionChangeHandler#addExtension
	 * (org.eclipse.core.runtime.dynamicHelpers.IExtensionTracker,
	 * org.eclipse.core.runtime.IExtension)
	 */
	public void addExtension(IExtensionTracker tracker, IExtension extension) {
		DataModelerPortWizardsRegistryReader reader = new DataModelerPortWizardsRegistryReader(getPlugin(),
				getExtensionPoint());
		reader.setInitialCollection(getWizardElements());
		IConfigurationElement[] configurationElements = extension
				.getConfigurationElements();
		for (int i = 0; i < configurationElements.length; i++) {
			reader.readElement(configurationElements[i]);
		}
		// no need to reset the wizard elements - getWizardElements will parse
		// the
		// results of the registry reading
		setWizardElements(reader.getWizardElements());
		// reregister all object handles - it'd be better to process the deltas
		// in this case
		registerWizards(getWizardElements());

		// handle the primary wizards
		DataModelerPortWizardElement[] additionalPrimary = reader.getPrimaryWizards();
		if (additionalPrimary.length == 0) {
			return;
		}
		IWizardDescriptor[] localPrimaryWizards = getPrimaryWizards();
		DataModelerPortWizardElement[] newPrimary = new DataModelerPortWizardElement[additionalPrimary.length
				+ localPrimaryWizards.length];
		System.arraycopy(localPrimaryWizards, 0, newPrimary, 0,
				localPrimaryWizards.length);
		System.arraycopy(additionalPrimary, 0, newPrimary,
				localPrimaryWizards.length, additionalPrimary.length);
		setPrimaryWizards(newPrimary);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.wizards.AbstractWizardRegistry#dispose()
	 */
	public void dispose() {
		super.dispose();
		PlatformUI.getWorkbench().getExtensionTracker().unregisterHandler(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.internal.wizards.AbstractWizardRegistry#doInitialize()
	 */
	protected void doInitialize() {

		PlatformUI
				.getWorkbench()
				.getExtensionTracker()
				.registerHandler(
						this,
						ExtensionTracker
								.createExtensionPointFilter(getExtensionPointFilter()));

		DataModelerPortWizardsRegistryReader reader = new DataModelerPortWizardsRegistryReader(getPlugin(),
				getExtensionPoint());
		setWizardElements(reader.getWizardElements());
		setPrimaryWizards(reader.getPrimaryWizards());
		registerWizards(getWizardElements());
	}

	/**
	 * Return the extension point id that should be used for extension registry
	 * queries.
	 * 
	 * @return the extension point id
	 */
	protected abstract String getExtensionPoint();

	private IExtensionPoint getExtensionPointFilter() {
		return Platform.getExtensionRegistry().getExtensionPoint(getPlugin(),
				getExtensionPoint());
	}

	/**
	 * Return the plugin id that should be used for extension registry queries.
	 * 
	 * @return the plugin id
	 */
	protected abstract String getPlugin();

	/**
	 * Register the object with the workbench tracker.
	 * 
	 * @param extension
	 *            the originating extension
	 * @param object
	 *            the object to track
	 */
	private void register(IExtension extension, Object object) {
		PlatformUI.getWorkbench().getExtensionTracker()
				.registerObject(extension, object, IExtensionTracker.REF_WEAK);
	}

	/**
	 * Register all wizards in the given collection with the extension tracker.
	 * 
	 * @param collection
	 *            the collection to register
	 */
	private void registerWizards(DataModelerPortWizardCollectionElement collection) {
		registerWizards(collection.getWorkbenchWizardElements());

		DataModelerPortWizardCollectionElement[] collections = collection
				.getCollectionElements();
		for (int i = 0; i < collections.length; i++) {
			IConfigurationElement configurationElement = collections[i]
					.getConfigurationElement();
			if (configurationElement != null) {
				register(configurationElement.getDeclaringExtension(),
						collections[i]);
			}
			registerWizards(collections[i]);
		}
	}

	/**
	 * Register all wizards in the given array.
	 * 
	 * @param wizards
	 *            the wizards to register
	 */
	private void registerWizards(DataModelerPortWizardElement[] wizards) {
		for (int i = 0; i < wizards.length; i++) {
			register(wizards[i].getConfigurationElement()
					.getDeclaringExtension(), wizards[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.dynamicHelpers.IExtensionChangeHandler#
	 * removeExtension(org.eclipse.core.runtime.IExtension, java.lang.Object[])
	 */
	public void removeExtension(IExtension extension, Object[] objects) {
		if (!extension.getExtensionPointUniqueIdentifier().equals(
				getExtensionPointFilter().getUniqueIdentifier())) {
			return;
		}
		for (int i = 0; i < objects.length; i++) {
			Object object = objects[i];
			if (object instanceof DataModelerPortWizardCollectionElement) {
				// TODO: should we move child wizards to the "other" node?
				DataModelerPortWizardCollectionElement collection = (DataModelerPortWizardCollectionElement) object;
				collection.getParentCollection().remove(collection);
			} else if (object instanceof DataModelerPortWizardElement) {
				DataModelerPortWizardElement wizard = (DataModelerPortWizardElement) object;
				DataModelerPortWizardCollectionElement parent = wizard.getCollectionElement();
				if (parent != null) {
					parent.remove(wizard);
				}
				IWizardDescriptor[] primaryWizards = getPrimaryWizards();
				for (int j = 0; j < primaryWizards.length; j++) {
					if (primaryWizards[j] == wizard) {
						DataModelerPortWizardElement[] newPrimary = new DataModelerPortWizardElement[primaryWizards.length - 1];
						arrayCopyWithRemoval(primaryWizards, newPrimary, j);
						primaryWizards = newPrimary;
						break;
					}
				}
				setPrimaryWizards((DataModelerPortWizardElement[]) primaryWizards);
			}
		}
	}
	
    private void arrayCopyWithRemoval(Object [] src, Object [] dst, int idxToRemove) {
    	if (src == null || dst == null || src.length - 1 != dst.length || idxToRemove < 0 || idxToRemove >= src.length) {
			throw new IllegalArgumentException();
		}
    	
    	if (idxToRemove == 0) {
    		System.arraycopy(src, 1, dst, 0, src.length - 1);    		
    	}
    	else if (idxToRemove == src.length - 1) {
    		System.arraycopy(src, 0, dst, 0, src.length - 1);
    	}
    	else {
    		System.arraycopy(src, 0, dst, 0, idxToRemove);
    		System.arraycopy(src, idxToRemove + 1, dst, idxToRemove, src.length - idxToRemove - 1);
    	}    	
    }
}