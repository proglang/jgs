package resource;

public class Configuration {

    /**
     * Indicates, whether the package of the class should be printed in the
     * signature.
     */
    public static final boolean CLASS_SIGNATURE_PRINT_PACKAGE = false;
    /**
     * Name of the class the developer has to implement and which inherits from
     * the class {@code ALevelDefinition}.
     */
    public static final String DEF_CLASS_NAME = "Definition";
    /**
     * DOC
     */
    public static final String DEF_EXT_JAVA_CLASS = ".class";
    /**
     * DOC
     */
    public static final String DEF_EXT_JAVA_SOURCE = ".java";
    /**
     * Name of the package in which the class is located that the developer has
     * to implement and which inherits from the class {@code ALevelDefinition}.
     */
    public static final String DEF_PACKAGE_NAME = "security";
    /**
     * File path of the class which the developer has to implement and which
     * inherits from the class {@code ALevelDefinition} (divider is the
     * character '{@code /}').
     */
    public static final String DEF_PATH_FILE = String.format("%s/%s",
                                                             DEF_PACKAGE_NAME,
                                                             DEF_CLASS_NAME);
    /**
     * DOC
     */
    public static final String DEF_PATH_FILE_EXT =
        String.format("%s/%s%s",
                      DEF_PACKAGE_NAME,
                      DEF_CLASS_NAME,
                      DEF_EXT_JAVA_SOURCE);
    /**
     * Java path of the class which the developer has to implement and which
     * inherits from the class {@code ALevelDefinition} (divider is the
     * character '{@code .}').
     */
    public static final String DEF_PATH_JAVA = String.format("%s.%s",
                                                             DEF_PACKAGE_NAME,
                                                             DEF_CLASS_NAME);
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
     * DOC
     */
    public static final String PREFIX_LEVEL_FUNCTION = "mk";
    /**
     * DOC
     */
    public static final String PREFIX_ARRAY_FUNCTION = "array";
    /**
     * DOC
     */
    public static final boolean DEBUG = false;
    /**
     * DOC
     */
    public static final int DISPLAY_SIZE = 150;

}
