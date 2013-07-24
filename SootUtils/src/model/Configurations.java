package model;

import java.util.HashMap;
import java.util.Map;

public class Configurations {
	
	public static class Config {
		
		protected String name;
		protected String value;
		
		public Config(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}
		
	}
	
	Map<String, String> configurations = new HashMap<String, String>();
	
	public Configurations(Config... configurations) {
		for (Config configuration : configurations) {
			this.configurations.put(configuration.name, configuration.value);
		}
	}
	
	public Map<String, String> getConfigurationMap() {
		return this.configurations;
	}

}
