package org.cubrid.sqlformatter;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class SqlFormatPlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.cubrid.sqlformatter";

	// The shared instance
	private static SqlFormatPlugin plugin;

	/**
	 * The constructor
	 */
	public SqlFormatPlugin() {
		//do nothing.
	}

	/**
	 * When plugin start.
	 *
	 * @param context BundleContext
	 * @throws Exception when errors.
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * When plug-in stop.
	 *
	 * @param context BundleContext
	 * @throws Exception when errors.
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
	public static SqlFormatPlugin getDefault() {
		return plugin;
	}

}
