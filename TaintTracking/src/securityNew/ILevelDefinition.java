package securityNew;

import java.util.List;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

/**
 * Frueher SecurityLevel
 * 
 * @author Thomas Vogel
 * 
 */
public interface ILevelDefinition<T extends ILevel> {

	public static final String DEF_CLASS_NAME = "Definition";

	public static final String DEF_PACKAGE_NAME = "security";
	
	public static final String DEF_PATH_FILE = String.format("%s/%s",
			DEF_PACKAGE_NAME, DEF_CLASS_NAME);
	
	public static final String DEF_PATH_JAVA = String.format("%s.%s",
			DEF_PACKAGE_NAME, DEF_CLASS_NAME);

	public static final String SUFFIX_METHOD_ID = "Id";
	
	public static final String METHOD_LEVELS = "getLevels";
	
	public static final String METHOD_GLB = "getGreatesLowerBoundLevel";
	
	public static final String METHOD_LUB = "getLeastUpperBoundLevel";
	
	public static final String METHOD_DEFAULT_VAR = "getDefaultVariableLevel";
	
	public static final String METHOD_LIB_CLASS_EFFECTS = "getLibraryClassWriteEffects";
	
	public static final String METHOD_LIB_METHOD_EFFECTS = "getLibraryMethodWriteEffects";
	
	public static final String METHOD_LIB_FIELD = "getLibraryFieldLevel";
	
	public static final String METHOD_LIB_PARAMETER = "getLibraryParameterLevel";
	
	public static final String METHOD_LIB_RETURN = "getLibraryReturnLevel";
	
	public Class<T> getBaseClass();
	
	public int compare(T level1, T level2);

	public T getDefaultVariableLevel();

	public T getGreatesLowerBoundLevel();

	public T getGreatestLowerBoundLevel(T level1, T level2);

	public T getLeastUpperBoundLevel();

	public T getLeastUpperBoundLevel(T level1, T level2);

	public T[] getLevels();

	public List<T> getLibraryClassWriteEffects(SootClass sootClass);

	public T getLibraryFieldLevel(SootField sootField);

	public List<T> getLibraryMethodWriteEffects(SootMethod sootMethod);

	public List<T> getLibraryParameterLevel(SootMethod sootMethod);

	public T getLibraryReturnLevel(SootMethod sootMethod, List<T> levels);

}
