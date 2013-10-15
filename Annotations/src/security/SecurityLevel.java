package security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>Security level definition</h1>
 * 
 * Abstract Class which gives the possibility to define a <em>security level</em> hierarchy and
 * which also defines the id functions for each <em>security level</em>. The developer which uses
 * the analysis tool has to implement this class. Some restrictions he had to consider:
 * <ul>
 * <li>Soot Security Analysis requires a implementation of the class {@link SecurityLevel}.</li>
 * <li>The name of this subclass must be {@code SootSecurityLevel}.</li>
 * <li>The subclass must be located in the package {@code security}.</li>
 * <li>In the subclass the method {@link SecurityLevel#getOrderedSecurityLevels()} is implemented
 * and returns a list of minimal two <em>security level</em> name.</li>
 * <li>In the subclass exists for each by {@link SecurityLevel#getOrderedSecurityLevels()} defined
 * <em>security level</em> a static id function. The name of this function must be e.g. if the level
 * name is high: {@code highId()}.</li>
 * <li>The implementation of such an id function looks like the following:
 * 
 * <pre>
 * &#064;ReturnSecurity(&quot;low&quot;)
 * public static &lt;T&gt; T lowId(T object) {
 * 	return object;
 * }
 * </pre>
 * 
 * <li>Each id function is annotated with the specific security return level (
 * {@link Annotations.ReturnSecurity}).</li>
 * </ul>
 * 
 * To test whether the implemented class is valid, the developer has to create a new instance of the
 * class {@link SecurityLevelImplChecker}, if the implementation is invalid the console will output this
 * fact.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public abstract class SecurityLevel {

	/**
	 * <em>Security level</em> that indicates that the method has no return value. Such methods
	 * should be annotated with the {@link Annotations.ReturnSecurity} annotation and the value '
	 * {@code void}'.
	 */
	protected static final String LEVEL_VOID = "void";
	/**
	 * The suffix of an id function. The name of each id function starts with the name of the
	 * <em>security level</em> followed by the keyword '{@code Id}', which is the suffix.
	 */
	protected static final String SUFFIX_ID_METHOD = "Id";
	/**
	 * List of additional <em>security levels</em> which are used internally, e.g.
	 * {@link SecurityLevel#LEVEL_VOID}.
	 */
	protected static final List<String> SUPPLEMENTARY_LEVELS = new ArrayList<String>(
			Arrays.asList(LEVEL_VOID));

	/**
	 * Should return an array where all <em>security levels</em> are specified as a String. The
	 * array should start with the strongest <em>security level</em> and should end with the weakest
	 * <em>security
	 * level</em>. E.g.
	 * 
	 * <pre>
	 * public String[] getOrderedSecurityLevels() {
	 * 	return new String[] { &quot;high&quot;, &quot;normal&quot;, &quot;low&quot; };
	 * }
	 * </pre>
	 * 
	 * @return The ordered <em>security level</em> String array. The index of the strongest level
	 *         has to be {@literal 0}, the index of the weakest <em>security level</em> has to be
	 *         {@code number of security level - 1}.
	 */
	public abstract String[] getOrderedSecurityLevels();

}
