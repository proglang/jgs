package model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class Configurations {
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class Config {
		
		/** */
		protected String name;
		/** */
		protected String value;
		
		/**
		 * 
		 * @param name
		 * @param value
		 */
		public Config(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}
		
	}
	
	/** */
	Map<String, String> configurations = new HashMap<String, String>();
	
	/**
	 * 
	 * @param configurations
	 */
	public Configurations(Config... configurations) {
		for (Config configuration : configurations) {
			this.configurations.put(configuration.name, configuration.value);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> getConfigurationMap() {
		return this.configurations;
	}

}
