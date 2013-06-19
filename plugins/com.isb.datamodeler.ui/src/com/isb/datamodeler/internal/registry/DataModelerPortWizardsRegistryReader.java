package com.isb.datamodeler.internal.registry;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.ui.activities.WorkbenchActivityHelper;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.internal.ui.wizards.port.DataModelerPortWizardCollectionElement;
import com.isb.datamodeler.internal.ui.wizards.port.DataModelerPortWizardElement;
import com.isb.datamodeler.ui.DataModelerUI;

public class DataModelerPortWizardsRegistryReader extends RegistryReader {

	private String pluginPoint;
    
    private DataModelerPortWizardCollectionElement wizardElements = null;

    private ArrayList deferWizards = null;

    private ArrayList deferCategories = null;

    private Set deferPrimary;

    // constants
    /**
     * Examples wizard category id
     */    
    public final static String FULL_EXAMPLES_WIZARD_CATEGORY = "org.eclipse.ui.Examples";//$NON-NLS-1$
    /**
     * Other wizard category id
     */
    final public static String UNCATEGORIZED_WIZARD_CATEGORY = "org.eclipse.ui.Other";//$NON-NLS-1$
    /**
     * General wizard category id
     */
    final public static String GENERAL_WIZARD_CATEGORY = "org.eclipse.ui.Basic";	//$NON-NLS-1$

    final private static String UNCATEGORIZED_WIZARD_CATEGORY_LABEL = Messages.bind("DataModelerImportAction.NewWizardsRegistryReader_otherCategory");
    
    private final static String CATEGORY_SEPARATOR = "/";//$NON-NLS-1$

    private DataModelerPortWizardElement[] primaryWizards = new DataModelerPortWizardElement[0];
    
    private class CategoryNode {
        private Category category;

        private String path;

        CategoryNode(Category cat) {
            category = cat;
            path = ""; //$NON-NLS-1$
            String[] categoryPath = category.getParentPath();
            if (categoryPath != null) {
                for (int nX = 0; nX < categoryPath.length; nX++) {
                    path += categoryPath[nX] + '/';
                }
            }
            path += cat.getId();
        }

        String getPath() {
            return path;
        }

        Category getCategory() {
            return category;
        }
    }

    private static final Comparator comparer = new Comparator() {
        private Collator collator = Collator.getInstance();

        public int compare(Object arg0, Object arg1) {
            String s1 = ((CategoryNode) arg0).getPath();
            String s2 = ((CategoryNode) arg1).getPath();
            return collator.compare(s1, s2);
        }
    };

	private boolean readAll = true;

	private String plugin;

    /**
     *Create an instance of this class.
     *
     * @param pluginId the plugin id
     * @param pluginPointId java.lang.String
     */
    public DataModelerPortWizardsRegistryReader(String pluginId, String pluginPointId) {
        pluginPoint = pluginPointId;
        plugin = pluginId;
    }

    /* (non-Javadoc)
     * Method declared on WizardRegistryReader.  
     * <p>
     * This implementation uses a defering strategy.  For more info see
     * <code>readWizards</code>.
     * </p>
     */
    protected void addNewElementToResult(DataModelerPortWizardElement element,
            IConfigurationElement config) {
        // TODO: can we remove the config parameter?
        deferWizard(element);
    }

    /**
     * 
     * @param parent
     * @param element
     * @since 3.1
     */
    private DataModelerPortWizardCollectionElement createCollectionElement(DataModelerPortWizardCollectionElement parent, IConfigurationElement element) {
    	DataModelerPortWizardCollectionElement newElement = new DataModelerPortWizardCollectionElement(
				element, parent);

        parent.add(newElement);
        return newElement;		
	}
    /**
     *	Create and answer a new WizardCollectionElement, configured as a
     *	child of <code>parent</code>
     *
     *	@return org.eclipse.ui.internal.model.WizardCollectionElement
     *	@param parent org.eclipse.ui.internal.model.WizardCollectionElement
     *  @param id the id of the new collection
     *  @param pluginId the originating plugin id of the collection, if any. <code>null</code> otherwise.
     *	@param label java.lang.String
     */
    protected DataModelerPortWizardCollectionElement createCollectionElement(
    		DataModelerPortWizardCollectionElement parent, String id, String pluginId,
            String label) {
    	DataModelerPortWizardCollectionElement newElement = new DataModelerPortWizardCollectionElement(id,
                pluginId, label, parent);

        parent.add(newElement);
        return newElement;
    }

    /**
     * Creates empty element collection. Overrider to fill
     * initial elements, if needed.
     */
    protected void createEmptyWizardCollection() {
        wizardElements = new DataModelerPortWizardCollectionElement("root", null, "root", null);//$NON-NLS-2$//$NON-NLS-1$
    }
    
    /**
     * Set the initial wizard set for supplemental reading via dynamic plugin loading.
     * 
     * @param wizards the wizards
     * @since 3.1
     */
    public void setInitialCollection(DataModelerPortWizardCollectionElement wizards) {
    	wizardElements = wizards;
    	readAll = false;
    }

    /**
     * Stores a category element for deferred addition.
     */
    private void deferCategory(IConfigurationElement config) {
        // Create category.
        Category category = null;
        try {
            category = new Category(config);
        } catch (CoreException e) {
            DataModelerUI.log("Cannot create category: ", e.getStatus());//$NON-NLS-1$
            return;
        }

        // Defer for later processing.
        if (deferCategories == null) {
			deferCategories = new ArrayList(20);
		}
        deferCategories.add(category);
    }


    /**
     * Stores a wizard element for deferred addition.
     */
    private void deferWizard(DataModelerPortWizardElement element) {
        if (deferWizards == null) {
			deferWizards = new ArrayList(50);
		}
        deferWizards.add(element);
    }

    /**
     * Finishes the addition of categories.  The categories are sorted and
     * added in a root to depth traversal.
     */
    private void finishCategories() {
        // If no categories just return.
        if (deferCategories == null) {
			return;
		}

        // Sort categories by flattened name.
        CategoryNode[] flatArray = new CategoryNode[deferCategories.size()];
        for (int i = 0; i < deferCategories.size(); i++) {
            flatArray[i] = new CategoryNode((Category) deferCategories.get(i));
        }
        Collections.sort(Arrays.asList(flatArray), comparer);

        // Add each category.
        for (int nX = 0; nX < flatArray.length; nX++) {
            Category cat = flatArray[nX].getCategory();
            finishCategory(cat);
        }

        // Cleanup.
        deferCategories = null;
    }

    /**
     * Save new category definition.
     */
    private void finishCategory(Category category) {
        String[] categoryPath = category.getParentPath();
        DataModelerPortWizardCollectionElement parent = wizardElements; // ie.- root

        // Traverse down into parent category.	
        if (categoryPath != null) {
            for (int i = 0; i < categoryPath.length; i++) {
            	DataModelerPortWizardCollectionElement tempElement = getChildWithID(parent,
                        categoryPath[i]);
                if (tempElement == null) {
                    // The parent category is invalid.  By returning here the
                    // category will be dropped and any wizard within the category
                    // will be added to the "Other" category.
                    return;
                }
                parent = tempElement;
            }
        }

        // If another category already exists with the same id ignore this one.
        Object test = getChildWithID(parent, category.getId());
        if (test != null) {
			return;
		}

        if (parent != null) {
			createCollectionElement(parent, (IConfigurationElement) getAdapter(category,
					IConfigurationElement.class));
		}
    }


	/**
     * Finishes the recognition of primary wizards.
     */
    private void finishPrimary() {
        if (deferPrimary != null) {
            ArrayList primary = new ArrayList();
            for (Iterator i = deferPrimary.iterator(); i.hasNext();) {
                String id = (String) i.next();
                DataModelerPortWizardElement element = wizardElements == null ? null : wizardElements.findWizard(id, true);
                if (element != null) {
                    primary.add(element);
                }
            }

            primaryWizards = (DataModelerPortWizardElement[]) primary
                    .toArray(new DataModelerPortWizardElement[primary.size()]);

            deferPrimary = null;
        }
    }


    /**
     *	Insert the passed wizard element into the wizard collection appropriately
     *	based upon its defining extension's CATEGORY tag value
     *
     *	@param element WorkbenchWizardElement
     *	@param config configuration element
     */
    private void finishWizard(DataModelerPortWizardElement element,
            IConfigurationElement config) {
        StringTokenizer familyTokenizer = new StringTokenizer(
                getCategoryStringFor(config), CATEGORY_SEPARATOR);

        // use the period-separated sections of the current Wizard's category
        // to traverse through the NamedSolution "tree" that was previously created
        DataModelerPortWizardCollectionElement currentCollectionElement = wizardElements; // ie.- root
        boolean moveToOther = false;

        while (familyTokenizer.hasMoreElements()) {
        	DataModelerPortWizardCollectionElement tempCollectionElement = getChildWithID(
                    currentCollectionElement, familyTokenizer.nextToken());

            if (tempCollectionElement == null) { // can't find the path; bump it to uncategorized
                moveToOther = true;
                break;
            } 
            currentCollectionElement = tempCollectionElement;
        }

        if (moveToOther) {
			moveElementToUncategorizedCategory(wizardElements, element);
		} else {
            currentCollectionElement.add(element);
            element.setParent(currentCollectionElement);
        }
    }

    /**
     * Finishes the addition of wizards.  The wizards are processed and categorized.
     */
    private void finishWizards() {
        if (deferWizards != null) {
            Iterator iter = deferWizards.iterator();
            while (iter.hasNext()) {
            	DataModelerPortWizardElement wizard = (DataModelerPortWizardElement) iter
                        .next();
                IConfigurationElement config = wizard.getConfigurationElement();
                finishWizard(wizard, config);
            }
            deferWizards = null;
        }
    }

    /**
     *	Return the appropriate category (tree location) for this Wizard.
     *	If a category is not specified then return a default one.
     */
    protected String getCategoryStringFor(IConfigurationElement config) {
        String result = config.getAttribute(IDataModelerUIRegistryConstants.TAG_CATEGORY);
        if (result == null) {
			result = UNCATEGORIZED_WIZARD_CATEGORY;
		}

        return result;
    }

    /**
     *	Go through the children of  the passed parent and answer the child
     *	with the passed name.  If no such child is found then return null.
     *
     *	@return org.eclipse.ui.internal.model.WizardCollectionElement
     *	@param parent org.eclipse.ui.internal.model.WizardCollectionElement
     *	@param id java.lang.String
     */
    protected DataModelerPortWizardCollectionElement getChildWithID(
    		DataModelerPortWizardCollectionElement parent, String id) {
        Object[] children = parent.getChildren(null);
        for (int i = 0; i < children.length; ++i) {
        	DataModelerPortWizardCollectionElement currentChild = (DataModelerPortWizardCollectionElement) children[i];
            if (currentChild.getId().equals(id)) {
				return currentChild;
			}
        }
        return null;
    }

    /**
     *	Moves given element to "Other" category, previously creating one if missing.
     */
    protected void moveElementToUncategorizedCategory(
    		DataModelerPortWizardCollectionElement root, DataModelerPortWizardElement element) {
    	DataModelerPortWizardCollectionElement otherCategory = getChildWithID(root,
                UNCATEGORIZED_WIZARD_CATEGORY);

        if (otherCategory == null) {
			otherCategory = createCollectionElement(root,
                    UNCATEGORIZED_WIZARD_CATEGORY, null,
                    UNCATEGORIZED_WIZARD_CATEGORY_LABEL);
		}

        otherCategory.add(element);
        element.setParent(otherCategory);
    }

    /**
     * Removes the empty categories from a wizard collection. 
     */
    private void pruneEmptyCategories(DataModelerPortWizardCollectionElement parent) {
        Object[] children = parent.getChildren(null);
        for (int nX = 0; nX < children.length; nX++) {
        	DataModelerPortWizardCollectionElement child = (DataModelerPortWizardCollectionElement) children[nX];
            pruneEmptyCategories(child);
            boolean shouldPrune = child.getId().equals(FULL_EXAMPLES_WIZARD_CATEGORY);
            if (child.isEmpty() && shouldPrune) {
				parent.remove(child);
			}
        }
    }

    /**
     * Implement this method to read element attributes.
     */
    public boolean readElement(IConfigurationElement element) {
        if (element.getName().equals(IDataModelerUIRegistryConstants.TAG_CATEGORY)) {
            deferCategory(element);
            return true;
        } else if (element.getName().equals(IDataModelerUIRegistryConstants.TAG_PRIMARYWIZARD)) {
            if (deferPrimary == null) {
				deferPrimary = new HashSet();
			}
            deferPrimary.add(element.getAttribute(IDataModelerUIRegistryConstants.ATT_ID));

            return true;
        } else {
            if (!element.getName().equals(IDataModelerUIRegistryConstants.TAG_WIZARD)) {
				return false;
			}
            DataModelerPortWizardElement wizard = createWizardElement(element);
            if (wizard != null) {
				addNewElementToResult(wizard, element);
			}
            return true;
        }
    }

    /**
     * Reads the wizards in a registry.  
     * <p>
     * This implementation uses a defering strategy.  All of the elements 
     * (categories, wizards) are read.  The categories are created as the read occurs. 
     * The wizards are just stored for later addition after the read completes.
     * This ensures that wizard categorization is performed after all categories
     * have been read.
     * </p>
     */
    protected void readWizards() {
    	if (readAll) {
    	       if (!areWizardsRead()) {
                createEmptyWizardCollection();
                IExtensionRegistry registry = Platform.getExtensionRegistry();
                readRegistry(registry, plugin, pluginPoint);
            }
    	}
        finishCategories();
        finishWizards();
        finishPrimary();
        if (wizardElements != null) {
            pruneEmptyCategories(wizardElements);
        }
    }

    /**
     * Returns the list of wizards that are considered 'primary'.
     * 
     * The return value for this method is cached since computing its value
     * requires non-trivial work.  
     * 
     * @return the primary wizards
     */
    public DataModelerPortWizardElement [] getPrimaryWizards() {
        if (!areWizardsRead()) {
            readWizards();
        }
        return (DataModelerPortWizardElement[]) WorkbenchActivityHelper.restrictArray(primaryWizards);
    }


    /**
     * Returns whether the wizards have been read already
     */
    protected boolean areWizardsRead() {
        return wizardElements != null && readAll;
    }

    /**
     * Returns a list of wizards, project and not.
     *
     * The return value for this method is cached since computing its value
     * requires non-trivial work.  
     * 
     * @return the wizard collection
     */
    public DataModelerPortWizardCollectionElement getWizardElements() {
        if (!areWizardsRead()) {
            readWizards();
        }
        return wizardElements;
    }

    protected Object[] getWizardCollectionElements() {
        if (!areWizardsRead()) {
            readWizards();
        }
        return wizardElements.getChildren();
    }
    
    /**
     * Returns a new WorkbenchWizardElement configured according to the parameters
     * contained in the passed Registry.  
     *
     * May answer null if there was not enough information in the Extension to create 
     * an adequate wizard
     */
    protected DataModelerPortWizardElement createWizardElement(
            IConfigurationElement element) {
        // WizardElements must have a name attribute
        if (element.getAttribute(IDataModelerUIRegistryConstants.ATT_NAME) == null) {
            logMissingAttribute(element, IDataModelerUIRegistryConstants.ATT_NAME);
            return null;
        }
        
        if (getClassValue(element, IDataModelerUIRegistryConstants.ATT_CLASS) == null) {       
            logMissingAttribute(element, IDataModelerUIRegistryConstants.ATT_CLASS);
            return null;
        }
        return new DataModelerPortWizardElement(element);
    }

    /**
     * Returns the first wizard with a given id.
     * 
     * @param id wizard id to search for
     * @return WorkbenchWizardElement matching the given id, if found; null otherwise
     */
    public DataModelerPortWizardElement findWizard(String id) {
        Object[] wizards = getWizardCollectionElements();
        for (int nX = 0; nX < wizards.length; nX++) {
        	DataModelerPortWizardCollectionElement collection = (DataModelerPortWizardCollectionElement) wizards[nX];
        	DataModelerPortWizardElement element = collection.findWizard(id, true);
            if (element != null && !WorkbenchActivityHelper.restrictUseOf(element)) {
				return element;
			}
        }
        return null;
    }
    
    /**
     * If it is possible to adapt the given object to the given type, this
     * returns the adapter. Performs the following checks:
     * 
     * <ol>
     * <li>Returns <code>sourceObject</code> if it is an instance of the
     * adapter type.</li>
     * <li>If sourceObject implements IAdaptable, it is queried for adapters.</li>
     * <li>If sourceObject is not an instance of PlatformObject (which would have
     * already done so), the adapter manager is queried for adapters</li>
     * </ol>
     * 
     * Otherwise returns null.
     * 
     * @param sourceObject
     *            object to adapt, or null
     * @param adapterType
     *            type to adapt to
     * @return a representation of sourceObject that is assignable to the
     *         adapter type, or null if no such representation exists
     */
    private Object getAdapter(Object sourceObject, Class adapterType) {
    	Assert.isNotNull(adapterType);
        if (sourceObject == null) {
            return null;
        }
        if (adapterType.isInstance(sourceObject)) {
            return sourceObject;
        }

        if (sourceObject instanceof IAdaptable) {
            IAdaptable adaptable = (IAdaptable) sourceObject;

            Object result = adaptable.getAdapter(adapterType);
            if (result != null) {
                // Sanity-check
                Assert.isTrue(adapterType.isInstance(result));
                return result;
            }
        } 
        
        if (!(sourceObject instanceof PlatformObject)) {
            Object result = Platform.getAdapterManager().getAdapter(sourceObject, adapterType);
            if (result != null) {
                return result;
            }
        }

        return null;
    }
}
