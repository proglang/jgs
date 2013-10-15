package utils;

/**
 * <h1>Predefined properties</h1>
 * 
 * The {@link Predefined} class contains predefined properties. E.g. the package name and the class
 * name of the class which the developer has to implement and which inherits from the class
 * {@code SecurityLevel}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
final public class Predefined {

	/**
	 * Name of the class the developer has to implement and which inherits from the class
	 * {@code SecurityLevel}.
	 */
	public static final String IMPL_SL_CLASS_NAME = "SootSecurityLevel";
	/**
	 * Name of the package in which the class is located that the developer has to implement and
	 * which inherits from the class {@code SecurityLevel}.
	 */
	public static final String IMPL_SL_PACKAGE_NAME = "security";
	/**
	 * File path of the class which the developer has to implement and which inherits from the class
	 * {@code SecurityLevel} (divider is the character '{@code /}').
	 */
	public static final String IMPL_SL_PATH_FILE = String.format("%s/%s", IMPL_SL_PACKAGE_NAME,
			IMPL_SL_CLASS_NAME);
	/**
	 * Java path of the class which the developer has to implement and which inherits from the class
	 * {@code SecurityLevel} (divider is the character '{@code .}').
	 */
	public static final String IMPL_SL_PATH_JAVA = String.format("%s.%s", IMPL_SL_PACKAGE_NAME,
			IMPL_SL_CLASS_NAME);

}
