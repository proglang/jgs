package security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract Class which gives the possibility to define a security level hierarchy and which
 * also defines the id functions for each security level. The programmer which uses the analysis
 * tool has to implement this class. Some restrictions he had to consider:
 * <ul>
 * <li>Soot Security Analysis requires a implementation of the class {@link SecurityLevel}</li>
 * <li>The name of this subclass must be {@code SootSecurityLevel}</li>
 * <li>The subclass must be located in the package {@code security}</li>
 * <li>In the subclass the method {@link SecurityLevel#getOrderedSecurityLevels()} is
 * implemented and returns a list of minimal one sercurity level name.</li>
 * <li>In the subclass exists for each by {@link SecurityLevel#getOrderedSecurityLevels()}
 * defined security level a static id function. The name of this function must be e.g. if the level
 * name is high: {@code highId()}.</li>
 * <li>The implementation of such an id function looks like the following:
 * 
 * <pre>
 * &#064;Security(&quot;low&quot;)
 * public static &lt;T&gt; T lowId(T object) {
 * 	return object;
 * }
 * </pre>
 * 
 * <li>Each id function is annotated with the specific security return level ({@link Annotations.ReturnSecurity}).</li>
 * </ul>
 * 
 * To test whether the implemented class is valid, just create a new instance, if something is
 * invalid the error console will output this fact.
 * 
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public abstract class SecurityLevel {

	/** */
	protected static final String VOID_LEVEL = "void";
	/** */
	protected static final List<String> ADDITIONAL_LEVELS = new ArrayList<String>(Arrays.asList(VOID_LEVEL));
	/** */
	public static final String IMPLEMENTED_CLASS_NAME = "security.SootSecurityLevel";
	/** */
	protected static final String SUFFIX_ID_METHOD = "Id";
	
	
	/**
	 * Should return an array where all security levels are specified as a String. The array
	 * should start with the strongest security level and should end with the weakest security
	 * level. E.g. <pre>
	 * public String[] getOrderedSecurityLevels() {
	 * 	return new String[] {"high","low"}; 
	 * }</pre>
	 * 
	 * @return
	 */
	public abstract String[] getOrderedSecurityLevels();
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	@Annotations.ReturnSecurity("*")
	public static <T> T variableId(T object) {
		return object;
	}
	
	/**
	 * 
	 */
	@Annotations.ReturnSecurity("void")
	public static void voidSecurity() {
		return;
	}	
}
