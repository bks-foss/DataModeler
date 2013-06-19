/**
 * <copyright>
 * </copyright>
 *
 * $Id: SQLdatabaseEditPlugin.java,v 1.1 2012/03/06 10:18:50 rdedios Exp $
 */
package sqldatabase.provider;

import java.util.ArrayList;
import java.util.List;

import com.isb.datamodeler.schema.provider.DatamodelerEditPlugin;

import com.isb.datamodeler.ui.project.provider.DatamodelerUIEditPlugin;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.EMFPlugin;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.provider.EcoreEditPlugin;

import org.eclipse.gmf.runtime.notation.NotationEditPlugin;
import org.osgi.framework.BundleContext;

import sqldatabase.SQLDatabase;

/**
 * This is the central singleton for the SQLdatabase edit plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class SQLdatabaseEditPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final SQLdatabaseEditPlugin INSTANCE = new SQLdatabaseEditPlugin();

	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static Implementation plugin;

	/**
	 * Create the instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SQLdatabaseEditPlugin() {
		super
		  (new ResourceLocator [] {
		     DatamodelerEditPlugin.INSTANCE,
		     DatamodelerUIEditPlugin.INSTANCE,
		     EcoreEditPlugin.INSTANCE,
		     NotationEditPlugin.INSTANCE,
		   });
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	public static Implementation getPlugin() {
		return plugin;
	}

	/**
	 * The actual implementation of the Eclipse <b>Plugin</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static class Implementation extends EclipsePlugin {
		
		// ID from the extension point
		private static final String IDATABASE_ID = "com.isb.datamodeler.database.sql.model.edit.databaseExtension";

		private ArrayList<SQLDatabase> _databases = new ArrayList<SQLDatabase>();
		
		/**
		 * Creates an instance.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public Implementation() {
			super();

			// Remember the static instance.
			//
			plugin = this;
		}
		
		
	    @Override
	    public void start(BundleContext context) throws Exception
	    {
	    	super.start(context);

			IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(IDATABASE_ID);


			for(IConfigurationElement config:configurationElements)
			{
				final Object object = config.createExecutableExtension("class");
				if (object instanceof SQLDatabase) {
					
					final String id = config.getAttribute("id");
					final String name = config.getAttribute("name");
					final String vendor = config.getAttribute("vendor");
					final String version = config.getAttribute("version");
					final String description = config.getAttribute("description");
					
					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable exception) {
							System.out.println("SQLdatabaseEditPlugin: Exception in client");
						}

						@Override
						public void run() throws Exception {
							SQLDatabase db = (SQLDatabase)object;
							db.setId(id);
							db.setName(name);
							if(vendor!=null)db.setVendor(vendor);
							if(version!=null)db.setVersion(version);
							if(description!=null)db.setDescription(description);
							_databases.add(db);
						}
					};
					SafeRunner.run(runnable);

				}
			}
	    }
	    
		@Override
		public void stop(BundleContext context) throws Exception {
			plugin = null;
		}
		
		public List<SQLDatabase> getExtendedDatabases()
		{
			return _databases;
		}
	}

}
