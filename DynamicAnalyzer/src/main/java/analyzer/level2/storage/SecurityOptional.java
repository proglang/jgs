package analyzer.level2.storage;

import util.exceptions.InternalAnalyzerException;

/**
 * This class is a container for Security Values.
 * Its purpose is to differentiate between security-values
 * of initialized and uninitialized locals:
 * - Initialized locals should carry some security-value
 * - uninitialized locals should not carry a specific security
 * 	 value, rather they should be marked as "uninitialized"
 * 
 * The name "SecurityOptional" is inspired by the
 * optional of the swift programming language.
 * @author Nicolas Müller
 *
 */
public class SecurityOptional {
	private Object securityLevel;
	private boolean initialized;
	
	public SecurityOptional(Object securityLevel, boolean isInitialized) {
		this.securityLevel = securityLevel;
		this.initialized = isInitialized;
	}

	/**
	 * get Security Level of Optional. Throw Exception if variable is uninitialised
	 * @return Security Level of Optional
	 */
	public Object getSecurityLevel() {
		if (!initialized) {
			throw new InternalAnalyzerException("Tried to get security level"
					+ " of uninitialised variable!");
		}
		return securityLevel;
	}
	
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Set internal initialized flag to true
	 */
	public void initialize() {
		initialized = true;
	}
	
	public String toString() {
		return initialized ? securityLevel.toString() : "Not-Init:" + securityLevel.toString();
	}
}
