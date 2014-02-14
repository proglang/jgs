package resource;

public class Configuration {

	/**
	 * Indicates, whether the package of the field should be printed in the
	 * signature.
	 */
	public static final boolean FIELD_SIGNATURE_PRINT_PACKAGE = false;
	/**
	 * Indicates, whether the type of the field should be printed in the
	 * signature.
	 */
	public static final boolean FIELD_SIGNATURE_PRINT_TYPE = true;
	/**
	 * Indicates, whether the visibility of the field should be printed in the
	 * signature.
	 */
	public static final boolean FIELD_SIGNATURE_PRINT_VISIBILITY = true;
	/**
	 * Indicates, whether the package of the class should be printed in the
	 * signature.
	 */
	public static final boolean CLASS_SIGNATURE_PRINT_PACKAGE = false;
	/**
	 * Indicates, whether the package of the method should be printed in the
	 * signature.
	 */
	public static final boolean METHOD_SIGNATURE_PRINT_PACKAGE = false;
	/**
	 * Indicates, whether the type of the method should be printed in the
	 * signature.
	 */
	public static final boolean METHOD_SIGNATURE_PRINT_TYPE = true;
	/**
	 * Indicates, whether the visibility of the method should be printed in the
	 * signature.
	 */
	public static final boolean METHOD_SIGNATURE_PRINT_VISIBILITY = true;
	/**
	 * Name of the class the developer has to implement and which inherits from the class
	 * {@code ALevelDefinition}.
	 */
	public static final String DEF_CLASS_NAME = "Definition";
	/**
	 * Name of the package in which the class is located that the developer has to implement and
	 * which inherits from the class {@code ALevelDefinition}.
	 */
	public static final String DEF_PACKAGE_NAME = "security";
	/**
	 * File path of the class which the developer has to implement and which inherits from the class
	 * {@code ALevelDefinition} (divider is the character '{@code /}').
	 */
	public static final String DEF_PATH_FILE = String.format("%s/%s", DEF_PACKAGE_NAME,
			DEF_CLASS_NAME);
	/**
	 * Java path of the class which the developer has to implement and which inherits from the class
	 * {@code ALevelDefinition} (divider is the character '{@code .}').
	 */
	public static final String DEF_PATH_JAVA = String.format("%s.%s", DEF_PACKAGE_NAME,
			DEF_CLASS_NAME);
	
	public static final String SUFFIX_METHOD_ID = "Id";


}
