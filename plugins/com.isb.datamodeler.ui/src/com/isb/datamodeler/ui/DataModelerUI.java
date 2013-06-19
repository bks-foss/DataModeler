package com.isb.datamodeler.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.wizards.IWizardRegistry;
import org.osgi.framework.BundleContext;

import com.isb.datamodeler.Messages;
import com.isb.datamodeler.internal.ui.wizards.port.DataModelerExportWizardRegistry;
import com.isb.datamodeler.internal.ui.wizards.port.DataModelerImportWizardRegistry;
import com.isb.datamodeler.ui.views.DataModelerNavigatorLabelProvider;
import com.isb.datamodeler.ui.wizards.DataModelerProjectWizardPage;

/**
 * The activator class controls the plug-in life cycle
 */
public class DataModelerUI extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.isb.datamodeler.ui"; //$NON-NLS-1$
	
	public static final String DATA_MODELER_SELECTED_PROJECTS_PROPERTY = "dataModelerProjects"; //$NON-NLS-1$
	
	private static final String ENVIRONMENT_PROPERTIES = "environment.properties"; //$NON-NLS-1$
	
	public static final String NEW_DATAMODELER_PROJECT_CONF_PAGE_EXTENSION_POINT = "com.isb.datamodeler.ui.newDataModelerProjectConfigurationPage";
	
	public static final String INTERNAL_DATAMODELER_NAVIGATOR_LABEL_PROVIDER = "com.isb.datamodeler.ui.dataModelerNavigatorLabelProvider";
	
	private DataModelerProjectWizardPage dataModelerProjectWizardPage = new DataModelerProjectWizardPage();
	
	private DataModelerNavigatorLabelProvider dataModelerNavigatorLabelProvider = new DataModelerNavigatorLabelProvider();
	
	private static Map<String, Integer> datamodelerProjectWizardExtensionPriorities;
	
	private Properties _properties;
	
	static{
		
		datamodelerProjectWizardExtensionPriorities = new HashMap<String, Integer>();
		
		//TODO This values could be obtained reading the extension point definition ?
		datamodelerProjectWizardExtensionPriorities.put("lowest", 0);
		datamodelerProjectWizardExtensionPriorities.put("lower", 1);
		datamodelerProjectWizardExtensionPriorities.put("low", 2);
		datamodelerProjectWizardExtensionPriorities.put("medium", 3);
		datamodelerProjectWizardExtensionPriorities.put("high", 4);
		datamodelerProjectWizardExtensionPriorities.put("higher", 5);
		datamodelerProjectWizardExtensionPriorities.put("highest", 6);

	}


	// The shared instance
	private static DataModelerUI plugin;
	
	/**
	 * The constructor
	 */
	public DataModelerUI() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		deleteIncorrectPerspectives();
		
		loadDataModelerProjectConfigurationPage();
		
		loadInternalDataModelerNavigatorLabelProvider();
		
		_properties = new Properties();
		URL environmentPropFile = FileLocator.find(context.getBundle(), new Path(ENVIRONMENT_PROPERTIES), null);
		if(environmentPropFile!=null)
			_properties.load(environmentPropFile.openStream());
	}
	
	private void loadDataModelerProjectConfigurationPage() throws Exception
	{
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(NEW_DATAMODELER_PROJECT_CONF_PAGE_EXTENSION_POINT);
		
		int currentPriorityValue = -1;
		
		for(IConfigurationElement config:configurationElements)
		{
			DataModelerProjectWizardPage wizardPage = (DataModelerProjectWizardPage)config.createExecutableExtension("page");
			String priorityName = config.getAttribute("priority");
			int priorityValue = datamodelerProjectWizardExtensionPriorities.get(priorityName);
			
			if(priorityValue>currentPriorityValue)
			{
				dataModelerProjectWizardPage = wizardPage;
				currentPriorityValue = priorityValue;
			}
				 
		}
	}
	
	private void loadInternalDataModelerNavigatorLabelProvider() throws Exception
	{
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(INTERNAL_DATAMODELER_NAVIGATOR_LABEL_PROVIDER);
		
		if(configurationElements.length>0)
			dataModelerNavigatorLabelProvider = (DataModelerNavigatorLabelProvider)configurationElements[0].createExecutableExtension("provider");
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static DataModelerUI getDefault() {
		return plugin;
	}
	
	public DataModelerProjectWizardPage getDataModelerProjectConfigurationPage() {
		return dataModelerProjectWizardPage;
	}
	
	public DataModelerNavigatorLabelProvider getInternalDataModelerNavigatorLabelProvider()
	{
		return dataModelerNavigatorLabelProvider;
	}

	/**
	 * Utility method.
	 *
	 * @return Shell
	 */
	public static Shell getActiveWorkbenchShell()
	{
		if(getActiveWorkbenchWindow() == null)
		{
			System.err.println("DataModelerUI.getActiveWorkbenchShell(): getActiveWorkbenchWindow()=null"); //$NON-NLS-1$
			return null;
		}
		return getActiveWorkbenchWindow().getShell();
	}
	
	/**
	 * Utility method
	 *
	 * @return IWorkbenchWindow
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow()
	{
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}
	/**
	 * Utility method. Return active page
	 *
	 * @return IWorkbenchPage
	 */
	public static IWorkbenchPage getActivePage()
	{
		return getDefault().internalGetActivePage();
	}
	/**
	 * Utility method. Return internal active page
	 *
	 * @return IWorkbenchPage
	 */
	private IWorkbenchPage internalGetActivePage()
	{
		IWorkbenchWindow window= getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
		{
			return null;
		}
		return getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
	
	/**
	 * Logs errors.
	 * @param message The message to log
	 * @param status The status to log
	 */
	public static void log(String message, IStatus status) {
		if (message != null) {
			getDefault().getLog().log(
					new Status(IStatus.ERROR, PLUGIN_ID, 0, message, null));
			System.err.println(message + "\nReason:"); //$NON-NLS-1$
		}
		if(status != null) {
			getDefault().getLog().log(status);
			System.err.println(status.getMessage());
		}
	}

    
    /**
     * Return the import wizard registry.
     * 
     * @return the import wizard registry
     * @since 3.1
     */
    public IWizardRegistry getImportWizardRegistry() {
    	return DataModelerImportWizardRegistry.getInstance();
    }
    
    /**
     * Return the export wizard registry.
     * 
     * @return the export wizard registry
     * @since 3.1
     */
    public IWizardRegistry getExportWizardRegistry() {
    	return DataModelerExportWizardRegistry.getInstance();
    }
    public String getPropertyValue(String propName)
	{
		return _properties.getProperty(propName);
	}
	/**
	 * Borra perspectivas que arrancaron mal.
	 */
	private void deleteIncorrectPerspectives()
	{
		try 
		{
			if(DataModelerUI.getActiveWorkbenchWindow() == null || DataModelerUI.getActiveWorkbenchWindow().getWorkbench() == null)
				return;
			
			IPerspectiveRegistry registry = DataModelerUI.getActiveWorkbenchWindow().getWorkbench().getPerspectiveRegistry();
			IPerspectiveDescriptor[] perspectives = registry.getPerspectives();
			for (int i=0; i<perspectives.length; i++)
			{
				if(perspectives[i].getId().startsWith("<"))
					registry.deletePerspective(perspectives[i]);
			}
		} 
		catch (Exception e) 
		{
			System.err.println("DataModelerUI.deleteIncorrectVegaPerspectives(): " + e);
		}
	}
    
	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, Messages.bind("DataModelerProjectWizard.MessageDialog.error"), e));
	}
	
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
}
