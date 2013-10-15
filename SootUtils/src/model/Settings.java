package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import logging.SootLogger;

/**
 * <h1>Analysis settings</h1>
 * 
 * The class {@link Settings} stores specific settings of an analysis. Each single setting provides
 * the setting name as well as the value of this setting. The usage of the class is only for
 * printing the settings via the {@link SootLogger}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see SootLogger#configuration(Settings)
 */
public class Settings implements Serializable {

	/**
	 * <h1>Setting tuple</h1>
	 * 
	 * The class {@link Setting} represents a tuple that contains the name of a setting as well as
	 * the value of this setting. Thus the class is a simple representation of a specific setting
	 * and will be used for printing the setting of the analysis.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see SootLogger#configuration(Settings)
	 */
	public static class Setting implements Serializable {

		/**
		 * Version number, which is used during deserialization to verify that the sender and
		 * receiver of a serialized object have loaded classes for that object that are compatible
		 * with respect to serialization (see {@link Serializable}).
		 */
		private static final long serialVersionUID = -3643182787167392103L;
		/** Name of the setting. */
		protected String name;
		/** Value of the setting as String. */
		protected String value;

		/**
		 * The constructor of a setting tuple which provides the name of a specific setting as well
		 * as the value of this setting.
		 * 
		 * @param name
		 *            Name of the setting.
		 * @param value
		 *            Value of the setting as String.
		 */
		public Setting(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}

	}

	/**
	 * Version number, which is used during deserialization to verify that the sender and receiver
	 * of a serialized object have loaded classes for that object that are compatible with respect
	 * to serialization (see {@link Serializable}).
	 */
	private static final long serialVersionUID = -5055956354005763224L;
	/** Map which stores the setting tuples. */
	private Map<String, String> settings = new HashMap<String, String>();

	/**
	 * Constructor of a complete setting, which can contain multiple specific setting tuples. Each
	 * such a setting provides a setting name and its value.
	 * 
	 * @param settings
	 *            Unspecified number of setting tuples.
	 */
	public Settings(Setting... settings) {
		for (Setting setting : settings) {
			this.settings.put(setting.name, setting.value);
		}
	}

	/**
	 * Method returns all configuration tuples contained by this {@link Settings} instance. These
	 * tuples were given to the constructor.
	 * 
	 * @return Map which provides the configurations.
	 */
	public Map<String, String> getSettingsMap() {
		return this.settings;
	}

}