package com.isb.datamodeler.model.triggers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.Transaction;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.isb.datamodeler.model.triggers.initializers.IDatamodelerInitializer;
import com.isb.datamodeler.model.triggers.internal.initializers.DatamodelerInitializerWrapper;
import com.isb.datamodeler.schema.EDatabase;
import com.isb.datamodeler.ui.project.EProject;



public class DataModelerTriggersPlugin implements BundleActivator {

	private static DataModelerTriggersPlugin instance;

	private Map<String, AbstractTriggerConfigurator> triggersToConfigurators = new HashMap<String, AbstractTriggerConfigurator>();

	// ID from the extension point
	private static final String INITIALIZER_EXTENSION_ID = "com.isb.datamodeler.model.triggers.initializerExtension";
		
	private Map<String, Map<String, DatamodelerInitializerWrapper>> eClass2InitializersMap = new HashMap<String,  Map<String, DatamodelerInitializerWrapper>>();
	
	private static Map<String, Integer> datamodelerProjectWizardExtensionPriorities;

	static{

		datamodelerProjectWizardExtensionPriorities = new HashMap<String, Integer>();
	
		datamodelerProjectWizardExtensionPriorities.put("lowest", 0);
		datamodelerProjectWizardExtensionPriorities.put("lower", 1);
		datamodelerProjectWizardExtensionPriorities.put("low", 2);
		datamodelerProjectWizardExtensionPriorities.put("medium", 3);
		datamodelerProjectWizardExtensionPriorities.put("high", 4);
		datamodelerProjectWizardExtensionPriorities.put("higher", 5);
		datamodelerProjectWizardExtensionPriorities.put("highest", 6);

	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		instance = this;

		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("com.isb.datamodeler.model.triggers.triggerConfigurator");
		
		for(IConfigurationElement config:configurationElements)
		{
			AbstractTriggerConfigurator configurator = (AbstractTriggerConfigurator)config.createExecutableExtension("configurator");
			String triggerClassName = config.getAttribute("trigger");
			
			triggersToConfigurators.put(triggerClassName, configurator);
		}
		
		loadInitializers();

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
	}
	
	public static DataModelerTriggersPlugin getInstance() {
		return instance;
	}
	
	/**
	 * Configura los triggers a traves de sus respectivos configuradores
	 * Se configuran de a grupos
	 * @param triggers
	 * @param rootTransaction
	 */
	public void configure(List<IParametrizableTrigger> triggers, Transaction rootTransaction)
	{
		
		for(IParametrizableTrigger trigger:triggers)
		{
			AbstractTriggerConfigurator configurator = triggersToConfigurators.get(trigger.getClass().getName());
			
			if(configurator!=null)
				configurator.configure(trigger, rootTransaction);
			
		}
		
	}
	
	/**
	 * load all available initializers
	 */
	private void loadInitializers() throws Exception
	{
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(INITIALIZER_EXTENSION_ID);

		for(IConfigurationElement config:configurationElements)
		{
			final Object object = config.createExecutableExtension("class");
			if (object instanceof IDatamodelerInitializer) {
				
				final String eclass = config.getAttribute("owner");
				final String database_id = config.getAttribute("database_id");

				String priorityName = config.getAttribute("priority");
				final int priorityValue;
				if(priorityName==null)
					priorityValue=0;
				else
					priorityValue = datamodelerProjectWizardExtensionPriorities.get(priorityName);	
					
				ISafeRunnable runnable = new ISafeRunnable() {
					@Override
					public void handleException(Throwable exception) {
						System.out.println("Datamodeler Core Activator: Exception in client");
					}

					@Override
					public void run() throws Exception {
						IDatamodelerInitializer initializer = (IDatamodelerInitializer)object;
						
						Map<String, DatamodelerInitializerWrapper> map;
						
						if(eClass2InitializersMap.containsKey(eclass))
						{
							map = eClass2InitializersMap.get(eclass);
							if(database_id != null)
							{
								if(map.containsKey(database_id))
								{
									if(priorityValue>((DatamodelerInitializerWrapper)map.get(database_id)).getPriority())
									{
										map.put(database_id, new DatamodelerInitializerWrapper(initializer, priorityValue));
									}
								}
								else
								{
									map.put(database_id, new DatamodelerInitializerWrapper(initializer, priorityValue));
								}
							}
						}
						else
						{
							map = new HashMap<String, DatamodelerInitializerWrapper>();
							map.put(database_id, new DatamodelerInitializerWrapper(initializer, priorityValue));
						}
						
						eClass2InitializersMap.put(eclass, map);
					}
				};
				SafeRunner.run(runnable);

			}
		}
	}
	
	public IDatamodelerInitializer getInitializer (EObject owner, EDatabase database) {
		DatamodelerInitializerWrapper initializerWrapper = null;
		
		if(eClass2InitializersMap.containsKey(owner.getClass().getName()))
		{
	
			Map<String, DatamodelerInitializerWrapper> map = eClass2InitializersMap.get(owner.getClass().getName());
			String id = database.getId();
					if(map.containsKey(id))
						initializerWrapper = map.get(id);
		}
		
		return initializerWrapper.getInitializer();
	}
	
	public IDatamodelerInitializer getInitializer(EObject owner)
	{
		DatamodelerInitializerWrapper initializerWrapper = null;
		
		if(eClass2InitializersMap.containsKey(owner.getClass().getName()))
		{
	
			Map<String, DatamodelerInitializerWrapper> map = eClass2InitializersMap.get(owner.getClass().getName());
			
			EObject parent = owner;
			// looking for the parent database of owner
			while(parent!=null)
			{
				if(parent instanceof EDatabase)
				{
					String id = ((EDatabase)parent).getId();
					if(map.containsKey(id))
						initializerWrapper = map.get(id);
				}
				else if(parent instanceof EProject)
				{
					EDatabase db = ((EProject)parent).getDatabase();
					if(db!=null && map.containsKey(db.getId()))
						initializerWrapper = map.get(db.getId());
					else
					{
						int priority = 0;
						for(Object iWrapper : map.values().toArray())
						{
							int wrapperPriority = ((DatamodelerInitializerWrapper)iWrapper).getPriority();
							if(wrapperPriority>=priority)
							{
								priority = wrapperPriority;
								initializerWrapper =  (DatamodelerInitializerWrapper)iWrapper;
							}
						}
					}
				}
				
				parent = parent.eContainer();
			}
			
			// Maybe there is a wrapper without database_id
			if(initializerWrapper==null)
				initializerWrapper = map.get(null);
		}
		
		// Or not...
		if(initializerWrapper==null)
			return null;
		
		return initializerWrapper.getInitializer();
	}
	
}
