package com.isb.datamodeler.internal.registry;

import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;

import com.isb.datamodeler.internal.ui.wizards.port.DataModelerPortWizardCollectionElement;
import com.isb.datamodeler.internal.ui.wizards.port.DataModelerPortWizardElement;

public abstract class AbstractWizardRegistry implements IWizardRegistry {

	private boolean initialized = false;

	private DataModelerPortWizardElement[] primaryWizards;

	private DataModelerPortWizardCollectionElement wizardElements;

	/**
	 * Create a new instance of this class.
	 */
	public AbstractWizardRegistry() {
		super();
	}
	
	/**
	 * Dispose of this registry.
	 */
	public void dispose() {
		primaryWizards = null;
		wizardElements = null;
		initialized = false;
	}

	/**
	 * Perform initialization of this registry. Should never be called by
	 * implementations. 
	 */
	protected abstract void doInitialize();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardRegistry#findCategory(java.lang.String)
	 */
	public IWizardCategory findCategory(String id) {
		initialize();
		return wizardElements.findCategory(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardRegistry#findWizard(java.lang.String)
	 */
	public IWizardDescriptor findWizard(String id) {
		initialize();
		return wizardElements.findWizard(id, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardRegistry#getPrimaryWizards()
	 */
	public IWizardDescriptor[] getPrimaryWizards() {
		initialize();
		return primaryWizards;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.wizards.IWizardRegistry#getRootCategory()
	 */
	public IWizardCategory getRootCategory() {
		initialize();
		return wizardElements;
	}

	/**
	 * Return the wizard elements.
	 * 
	 * @return the wizard elements
	 */
	protected DataModelerPortWizardCollectionElement getWizardElements() {
		initialize();
		return wizardElements;
	}

	/**
	 * Read the contents of the registry if necessary.
	 */
	protected final synchronized void initialize() {
		if (isInitialized()) {
			return;
		}

		initialized = true;
		doInitialize();
	}

	/**
	 * Return whether the registry has been read.
	 * 
	 * @return whether the registry has been read
	 */
	private boolean isInitialized() {
		return initialized;
	}

	/**
	 * Set the primary wizards.
	 * 
	 * @param primaryWizards
	 *            the primary wizards
	 */
	protected void setPrimaryWizards(DataModelerPortWizardElement[] primaryWizards) {
		this.primaryWizards = primaryWizards;
	}

	/**
	 * Set the wizard elements.
	 * 
	 * @param wizardElements
	 *            the wizard elements
	 */
	protected void setWizardElements(DataModelerPortWizardCollectionElement wizardElements) {
		this.wizardElements = wizardElements;
	}
}
