package cn.vigor.modules.server.util;

/**
 * ambari component status
 * @author 38342
 *
 */
public class AmbariComStatusUtil {
	
	/**
	 * Initial/Clean state
	 */
	public static String INIT = "INIT";
	
	/**
	 * In the process of installing
	 */
	public static String INSTALLING = "INSTALLING";
	
	/**
	 * Install failed
	 */
	public static String INSTALL_FAILED = "INSTALL_FAILED";
	
	/**
	 * State when install completed successfully
	 */
	public static String INSTALLED = "INSTALLED";
	
	/**
	 * In the process of starting
	 */
	public static String STARTING = "STARTING";
	
	/**
	 * State when start completed successfully
	 */
	public static String STARTED = "STARTED";
	
	/**
	 * In the process of stopping
	 */
	public static String STOPPING = "STOPPING";
	
	/**
	 * In the process of uninstalling
	 */
	public static String UNINSTALLING = "UNINSTALLING";
	
	/**
	 * State when uninstall completed successfully
	 */
	public static String UNINSTALLED = "UNINSTALLED";
	
	/**
	 * In the process of wiping out the install
	 */
	public static String WIPING_OUT = "WIPING_OUT";
	
	/**
	 * In the process of upgrading the deployed bits
	 */
	public static String UPGRADING = "UPGRADING";
	
	/**
	 * Disabled master’s backup state
	 */
	public static String DISABLED = "DISABLED";
	
	/**
	 * State could not be determined
	 */
	public static String UNKNOWN = "UNKNOWN";
	
	/**
	 * FAILED
	 */
	public static String FAILED = "FAILED";
}
