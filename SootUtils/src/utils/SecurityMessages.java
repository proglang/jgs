package utils;

import java.util.List;

public class SecurityMessages {

	/** */
	private static final String ACCESS_LIBRARY_FIELD = "The accessed field <%s> at source line %d in method <%s> is a library field. No security check can be done.";
	/** */
	private static final String ARRAY_WEAKEST_ASSIGNMENT = "In method <%s> at source line %d a value with '%s' security level is assigned to an array field. However, because of security restrictions only assignments of value with '%s' security level are valid.";
	/** */
	private static final String ASSIGNMENT_LIBRARY_FIELD = "Assignment to the library field <%s> at source line %d in method <%s>. Weakest security level is assumed for this field.";
	/** */
	private static final String ASSIGNMENT_STRONGER_ARRAY_INDEX = "In method <%s> at source line %d occurs an assignment to an '%s' level array with '%s' level index.";
	/** */
	private static final String ASSIGNMENT_WEAKER_ARRAY = "In method <%s> at source line %d a value with '%s' security level is assigned to an array which has the weaker security level '%s'.";
	/** */
	private static final String ASSIGNMENT_WEAKER_FIELD = "In method <%s> at source line %d a value with '%s' security level is assigned to the field <%s> which has the weaker security level '%s'.";
	/** */
	private static final String EFFECT_WEAKER_THAN_PC = "In method <%s> at source line %d a write effect to '%s' inside of a '%s' branch.";
	/** */
	private static final String ERROR_COMPILATION_SECURITY_LEVEL_CLASS = "Couldn't compile the file '%s'.";
	/** */
	private static final String ERROR_INSTANTIATION_SECURITY_LEVEL_CLASS = "Couldn't finish the instantiation of the SootSecurityLevel class correctly.";
	/** */
	private static final String EXCEPTION_LEVELS_COMPARISON = "Invalid comparison of the levels in method <%s> at source line %d. At least one of the levels { %s } is invalid.";
	/** */
	private static final String EXCEPTION_LEVELS_COMPARISON_LOCALMAP = "Invalid comparison of the levels in method <%s> at source line %d during processing of the program counter.";
	/** */
	private static final String IMPOSSIBLE_LEVEL_UPDATE = "Can't update the security level of %s '%s' at source line %d.";
	/** */
	private static final String INCOMPATIBLE_EQUATION_LEVEL = "Method <%s> has an invalid return security level equation. The level '%s' is not compatible with other levels.";
	/** */
	private static final String INTERRUPT_INVALID_PARAMETERS = "Analysis will not performed for method <%s> because of invalid parameter security level.";
	/** */
	private static final String INTERRUPT_INVALID_RETURN = "Analysis will not performed for method <%s> because of an invalid return security level.";
	/** */
	private static final String INVALID_CLASS_WRITE_EFFECT = "At class <%s> the write effects to '%s' is invalid. This effect doesn't exist.";
	/** */
	private static final String INVALID_CLASS_WRITE_EFFECT_USING = "The class <%s> which is used in method <%s> at source line %d has an invalid write effects.";
	/** */
	private static final String INVALID_FIELD_ANNOTATION = "The accessed field <%s> at source line %d in method <%s> has invalid field annotation.";
	/** */
	private static final String INVALID_FIELD_LEVEL = "Field <%s> has an invalid security level '%s'.";
	/** */
	private static final String INVALID_INVOKED_CLASS_WRITE_EFFECTS = "Method <%s> invokes at sourceline %d the method <%s>. This invocation requires also the class write effects which are invalid.";
	/** */
	private static final String INVALID_INVOKED_WRITE_EFFECTS = "Method <%s> invokes at sourceline %d the method <%s> which has invalid write effects.";
	/** */
	private static final String INVALID_LEVEL_COMPARISON = "The comparison of the levels '%s' and '%s' isn't possible. At least one of these levels is invalid.";
	/** */
	private static final String INVALID_LEVELS_COMPARISON = "The comparison of the levels { %s } isn't possible. At least one of these levels is invalid.";
	/** */
	private static final String INVALID_PARAMETER_LEVEL = "Method <%s> has an invalid parameter security level named '%s'.";
	/** */
	private static final String INVALID_RETURN_ANNOTATION = "The invoked method <%s> at source line %d in method <%s> has invalid return security level annotation.";
	/** */
	private static final String INVALID_RETURN_EQUATION = "Method <%s> has an invalid return security level equation.";
	/** */
	private static final String INVALID_RETURN_LEVEL = "Method <%s> has an invalid return security level '%s'.";
	/** */
	private static final String INVALID_WORKING_URL = "Couldn't create the URL to the directory which contains the SootSecurityLevel class.";
	/** */
	private static final String INVALID_WRITE_EFFECT = "At method <%s> the write effects to '%s' is invalid. This effect doesn't exist.";
	/** */
	private static final String INVALID_WRITE_EFFECT_USING = "In method <%s> at source line %d the write effect to '%s' is invalid. This effect doesn't exist.";
	/** */
	private static final String INVOCATION_LIBRARY_METHOD_MAX_LEVEL = "The invoked method <%s> at source line %d in method <%s> is a library method. Therefore the maximal security level '%s' of the arguments will be taken as result security level.";
	/** */
	private static final String INVOCATION_LIBRARY_METHOD_SECURITY = "The invoked method <%s> at source line %d in method <%s> is a library method. No security check can be done.";
	/** */
	private static final String INVOCATION_LIBRARY_METHOD_SIDEEFFECT = "The invoked method <%s> at source line %d in method <%s> is a library method. No side effect check can be done.";
	/** */
	private static final String LOCAL_NOT_FOUND = "The local \"%s\" in method <%s> at source line %d couldn't be updated to the level '%s', because this local is unknown.";
	/** */
	private static final String MISSING_WRITE_EFFECT = "In method <%s> at source line %d a write effect to '%s' occurs caused by %s%s.";
	/** */
	private static final String MORE_LEVEL_THAN_PARAMETER = "Method <%s> has %d %s. But %d parameter security %s are defined.";
	/** */
	private static final String MORE_PARAMETER_THAN_LEVEL = "Method <%s> has %d %s. But unfortunately only %d parameter security %s are defined.";
	/** */
	private static final String NO_CLASS_WRITE_EFFECT_ANNOTATION = "Class <%s> has no write effect annotation.";
	/** */
	private static final String NO_CLASS_WRITE_EFFECT_ANNOTATION_USING = "Class <%s> which is used in method <%s> at source line %d has no write effect annotation.";
	/** */
	private static final String NO_COUNTER = "Analysis tries to remove the statement '%s' from the program counter but the program counter is empty.";
	/** */
	private static final String NO_FIELD_ANNOTATION = "Field <%s> has no security level annotation.";
	/** */
	private static final String NO_FIELD_LEVEL = "Field <%s> has no security level.";
	/** */
	private static final String NO_INSTANTIATION_SECURITY_LEVEL_CLASS = "Couldn't instantiate an object of the type SootSecurityLevel.";
	/** */
	private static final String NO_METHOD_ANNOTATION = "Method <%s> has no return security level annotation.";
	/** */
	private static final String NO_METHOD_LEVEL = "Method <%s> has no return security level.";
	/** */
	private static final String NO_PARAMETER_ANNOTATION = "Method <%s> has no parameter security levels annotation.";
	/** */
	private static final String NO_PARAMETER_MATCH = "Couldn't attach the '%s' security level to the local \"%s\" in method <%s> at source line %d, because the given information about method parameter \"%s\" doesn't match to the local information.";
	/** */
	private static final String NO_SECURITY_LEVEL = "In method <%s> at source line %d the value '%s' has no security level.";
	/** */
	private static final String NO_SUCH_COUNTER = "Analysis tries to remove the statement '%s' from the program counter but this statement is not in the program counter.";
	/** */
	private static final String NO_WRITE_EFFECT_ANNOTATION = "Method <%s> has no write effect annotation.";
	/** */
	private static final String NOT_LOAD_SECURITY_LEVEL_CLASS = "Couldn't load the SootSecurityLevel class with path '%s'.";
	/** */
	private static final String NOT_REQUIRED_METHOD_ANNOTATION = "Constructor <%s> do not need a return security level.";
	/** */
	private static final String REFLECTION_ILLEGAL_LEVEL_NAME = "The level '%s' is an internal level which can't be used as customized level.";
	/** */
	private static final String REFLECTION_ILLEGAL_LEVEL_SIGN = "Is not allowed to use the sign %s inside of the level '%s' because the usage for %s.";
	/** */
	private static final String REFLECTION_INVALID_ANNOTATION = "Security annotation of method <%s> is not valid.";
	/** */
	private static final String REFLECTION_INVALID_SECURITY_LEVEL_CLASS = "The implementation of the SootSecurityLevel class is invalid.";
	/** */
	private static final String REFLECTION_NO_ANNOTATION = "No security annotation for method <%s>.";
	/** */
	private static final String REFLECTION_NO_CORRESPONDING_LEVEL = "Method <%s> has no corresponding level.";
	/** */
	private static final String REFLECTION_NO_LEVEL = "No or only one security level defined in the method <%s>.";
	/** */
	private static final String REFLECTION_NO_METHOD = "Couldn't find the method <%s> in the class SootSecurityLevel.";
	/** */
	private static final String REFLECTION_NO_METHOD_ACCESS = "Couldn't access the method <%s> in the class SootSecurityLevel.";
	/** */
	private static final String REFLECTION_NON_STATIC_METHOD = "Method <%s> is not static.";
	/** */
	private static final String SUPERFLUOUS_WRITE_EFFECT = "At method <%s> a write effect to '%s' is maybe superfluous.";
	/** */
	private static final String SWITCH_EXCEPTION_CATCHED = "During processing '%s' in method <%s> at source line %d an exception was thrown.";
	/** */
	private static final String UNIMPLEMENTED_SWITCH_CASE = "Invoke of the '%s' case in the %s by the statement '%s' at line %d [NOT IMPLEMENTED].";
	/** */
	private static final String UNKNOWN_CONDITION_CONTENT = "The IF-condition at source line %d in method <%s> contains an unknown usebox type '%s' of class [%s].";
	/** */
	private static final String USING_LIBRARY_CLASS_WRITE_EFFECT = "In method <%s> at source line %d the library class <%s> is used. No side effect check can be done.";
	/** */
	private static final String USING_LIBRARY_WRITE_EFFECT = "In method <%s> at source line %d the library field <%s> is used. No side effect check can be done.";
	/** */
	private static final String VALUE_WITHOUT_LEVEL = "The inspected value at source line %d in method <%s> has no security level.";
	/** */
	private static final String VOID_RETURN = "Method <%s> has a void return statement at source line %d. But the expected return security level '%s' doesn't match the level '%s'.";
	/** */
	private static final String VOID_RETURN_EXPECTED = "Method <%s> has a '%s' security level return at source line %d. But the expected return security level '%s' doesn't match this level.";
	/** */
	private static final String WEAKEN_LOCAL_VARIABLE = "In method <%s> at source line %d the variable \"%s\" was weakened from '%s' to '%s' security level.";
	/** */
	private static final String WEAKER_ARGUMENT_EXPECTED = "In method <%s> at source line %d another method <%s> will be invoked. For parameter \"%s\" a '%s' security level or weaker is expected, but the argument has a '%s' security level.";
	/** */
	private static final String WEAKER_RETURN_EXPECTED = "Method <%s> returns value with '%s' security level at source line %d. Expected was '%s' or weaker.";
	/** */
	private static final String WRONG_PARAMETER_ARGUMENT_AMOUNT = "The invocation of method <%s> in the method <%s> at source line %d has another amount of arguments than the security level annotation for this method defines.";


	/**
	 * 
	 * @param methodSignature
	 * @param accessedField
	 * @param srcLn
	 * @return
	 */
	public static String accessOfLibraryField(String methodSignature, String accessedField,
			long srcLn) {
		return String.format(ACCESS_LIBRARY_FIELD, accessedField, srcLn, methodSignature);
	}

	/**
	 * 
	 * @param srcLn
	 * @param methodSignature
	 * @return
	 */
	public static String accessToValueWithoutSecurityLevel(long srcLn, String methodSignature) {
		return String.format(VALUE_WITHOUT_LEVEL, srcLn, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param arrayLevel
	 * @param indexLevel
	 * @return
	 */
	public static String assignmentToArrayWithStrongerIndex(String methodSignature, long srcLn,	String arrayLevel, String indexLevel) {
		return String.format(ASSIGNMENT_STRONGER_ARRAY_INDEX, methodSignature, srcLn, arrayLevel, indexLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param fieldSignature
	 * @param srcLn
	 * @return
	 */
	public static String assignmentToLibraryField(String methodSignature,
			String fieldSignature, long srcLn) {
		return String.format(ASSIGNMENT_LIBRARY_FIELD, fieldSignature, srcLn, 
				methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param arrayLevel
	 * @param rightLevel
	 * @return
	 */
	public static String assignmentToWeakerArray(String methodSignature, long srcLn, String arrayLevel, String rightLevel) {
		return String.format(ASSIGNMENT_WEAKER_ARRAY, methodSignature, srcLn, rightLevel, arrayLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param fieldSignature
	 * @param srcLn
	 * @param fieldLevel
	 * @param level
	 * @return
	 */
	public static String assignmentToWeakerField(String methodSignature,
			String fieldSignature, long srcLn, String fieldLevel, String level) {
		return String.format(ASSIGNMENT_WEAKER_FIELD, methodSignature, srcLn, level, 
				fieldSignature, fieldLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param stmt
	 * @return
	 */
	public static String catchSwitchException(String methodSignature, long srcLn, String stmt) {
		return String.format(SWITCH_EXCEPTION_CATCHED, stmt, methodSignature, srcLn);
	}

	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String constructorReturnNotRequired(String methodSignature) {
		return String.format(NOT_REQUIRED_METHOD_ANNOTATION, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param effect
	 * @param pcLevel
	 * @return
	 */
	public static String effectWeakerThanPC(String methodSignature, long srcLn, String effect, String pcLevel) {
		return String.format(EFFECT_WEAKER_THAN_PC, methodSignature, srcLn, effect, pcLevel);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String errorCompilationSecurityLevelClass(String path) {
		return String.format(ERROR_COMPILATION_SECURITY_LEVEL_CLASS, path);
	}

	/**
	 * 
	 * @return
	 */
	public static String errorInstantiationSecurityLevelClass() {
		return String.format(ERROR_INSTANTIATION_SECURITY_LEVEL_CLASS);
	}

	/**
	 * 
	 * @return
	 */
	public static String impossibleToInstantiateSecurityLevelClass() {
		return String.format(NO_INSTANTIATION_SECURITY_LEVEL_CLASS);
	}
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	public static String impossibleToLoadSecurityLevelClass(String className) {
		return String.format(NOT_LOAD_SECURITY_LEVEL_CLASS, className);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param voidLevel
	 * @return
	 */
	public static String incompatibaleParameterLevels(String methodSignature, String voidLevel) {
		return String.format(INCOMPATIBLE_EQUATION_LEVEL, methodSignature, voidLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String interruptInvalidParameters(String methodSignature) {
		return String.format(INTERRUPT_INVALID_PARAMETERS, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String interruptInvalidReturn(String methodSignature) {
		return String.format(INTERRUPT_INVALID_RETURN, methodSignature);
	}

	/**
	 * 
	 * @param classSignature
	 * @param invalidEffect
	 * @return
	 */
	public static String invalidClassWriteEffect(String classSignature, String invalidEffect ) {
		return String.format(INVALID_CLASS_WRITE_EFFECT, classSignature, invalidEffect);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param classSignature
	 * @return
	 */
	public static String invalidClassWriteEffectUsingClass(String methodSignature, long srcLn, String classSignature) {
		return String.format(INVALID_CLASS_WRITE_EFFECT_USING, classSignature, methodSignature, srcLn);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param accessedFieldSignature
	 * @param srcLn
	 * @return
	 */
	public static String invalidFieldAnnotation(String methodSignature,
			String accessedFieldSignature, long srcLn) {
		return String.format(INVALID_FIELD_ANNOTATION, accessedFieldSignature, srcLn,
				methodSignature);
	}

	/**
	 * 
	 * @param fieldSignature
	 * @param level
	 * @return
	 */
	public static String invalidFieldLevel(String fieldSignature, String level) {
		return String.format(INVALID_FIELD_LEVEL, fieldSignature, level);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param invokedMethodSignature
	 * @param classSignature
	 * @return
	 */
	public static String invalidInvokedClassWriteEffects(String methodSignature, long srcLn, String invokedMethodSignature, String classSignature) {
		return String.format(INVALID_INVOKED_CLASS_WRITE_EFFECTS, methodSignature, srcLn, invokedMethodSignature, classSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param invokedMethodSignature
	 * @return
	 */
	public static String invalidInvokedWriteEffects(String methodSignature, long srcLn, String invokedMethodSignature) {
		return String.format(INVALID_INVOKED_WRITE_EFFECTS, methodSignature, srcLn, invokedMethodSignature);
	}

	/**
	 * 
	 * @param level1
	 * @param level2
	 * @return
	 */
	public static String invalidLevelComparison(String level1, String level2) {
		return String.format(INVALID_LEVEL_COMPARISON, level1, level2);
	}

	/**
	 * 
	 * @param levelList
	 * @return
	 */
	public static String invalidLevelsComparison(List<String> levelList) {
		String levels = "";
		for (String level : levelList) {
			if (! levels.equals("")) levels += ", ";
			levels += level;
		}
		return String.format(INVALID_LEVELS_COMPARISON, levels);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param levelList
	 * @return
	 */
	public static String invalidLevelsComparison(String methodSignature, long srcLn, String... levelList) {
		String levels = "";
		for (String level : levelList) {
			if (! levels.equals("")) levels += ", ";
			levels += level;
		}
		return String.format(EXCEPTION_LEVELS_COMPARISON, methodSignature, srcLn, levels);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @return
	 */
	public static String invalidLevelsComparisonInMap(String methodSignature, long srcLn) {
		return String.format(EXCEPTION_LEVELS_COMPARISON_LOCALMAP, methodSignature, srcLn);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param level
	 * @return
	 */
	public static String invalidParameterLevel(String methodSignature, String level) {
		return String.format(INVALID_PARAMETER_LEVEL, methodSignature, level);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param srcLn
	 * @return
	 */
	public static String invalidReturnAnnotation(String methodSignature,
			String invokedMethodSignature, long srcLn) {
		return String.format(INVALID_RETURN_ANNOTATION, invokedMethodSignature, srcLn,
				methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String invalidReturnEquation(String methodSignature) {
		return String.format(INVALID_RETURN_EQUATION, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param level
	 * @return
	 */
	public static String invalidReturnLevel(String methodSignature, String level) {
		return String.format(INVALID_RETURN_LEVEL, methodSignature, level);
	}

	/**
	 * 
	 * @return
	 */
	public static String invalidURLOfWorkingFolder() {
		return String.format(INVALID_WORKING_URL);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param effected
	 * @return
	 */
	public static String invalidWriteEffect(String methodSignature, long srcLn, String effected) {
		return String.format(INVALID_WRITE_EFFECT_USING, methodSignature, srcLn, effected);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invalidEffect
	 * @return
	 */
	public static String invalidWriteEffect(String methodSignature, String invalidEffect ) {
		return String.format(INVALID_WRITE_EFFECT, methodSignature, invalidEffect);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param srcLn
	 * @param maxLevel
	 * @return
	 */
	public static String invocationOfLibraryMethodMaxArgumentLevel(String methodSignature,
			String invokedMethodSignature, long srcLn, String maxLevel) {
		return String.format(INVOCATION_LIBRARY_METHOD_MAX_LEVEL, invokedMethodSignature,
				srcLn, methodSignature, maxLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param srcLn
	 * @return
	 */
	public static String invocationOfLibraryMethodNoSecurityLevel(String methodSignature,
			String invokedMethodSignature, long srcLn) {
		return String.format(INVOCATION_LIBRARY_METHOD_SECURITY, invokedMethodSignature, srcLn,
				methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param srcLn
	 * @return
	 */
	public static String invocationOfLibraryMethodNoSideEffect(String methodSignature, String invokedMethodSignature, long srcLn) {
		return String.format(INVOCATION_LIBRARY_METHOD_SIDEEFFECT, invokedMethodSignature, srcLn, methodSignature);
	}
	
	/**
	 * 
	 * @param methodSignature
	 * @param countParam
	 * @param countLev
	 * @return
	 */
	public static String lessParameterLevels(String methodSignature, int countParam, int countLev) {
		String level = countLev == 1 ? "level" : "levels";
		String parameter = countParam == 1 ? "parameter" : "parameters";
		return String.format(MORE_PARAMETER_THAN_LEVEL, methodSignature, countParam, parameter, countLev, level);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param localName
	 * @param level
	 * @return
	 */
	public static String localNotFoundUpdate(String methodSignature, long srcLn, String localName,
			String level) {
		return String.format(LOCAL_NOT_FOUND, localName, methodSignature, srcLn, level);
	}
	
	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param effected
	 * @param cause
	 * @return
	 */
	public static String missingWriteEffect(String methodSignature, long srcLn, String effected,	String cause) {
		boolean isVocal = false;
		for (String vocal : new String[] {"a", "e", "i", "o", "u"}) {
			if (cause.startsWith(vocal)) isVocal = true;
		}
		String a = isVocal ? "an " : "a ";
		return String.format(MISSING_WRITE_EFFECT, methodSignature, srcLn, effected, a, cause);
	}
	
	/**
	 * 
	 * @param methodSignature
	 * @param countParam
	 * @param countLev
	 * @return
	 */
	public static String moreParameterLevels(String methodSignature, int countParam, int countLev) {
		String level = countLev == 1 ? "level" : "levels";
		String parameter = countParam == 1 ? "parameter" : "parameters";
		return String.format(MORE_LEVEL_THAN_PARAMETER, methodSignature, countParam, parameter, countLev, level);
	}

	/**
	 * 
	 * @param classSignature
	 * @return
	 */
	public static String noClassWriteEffectAnnotation(String classSignature) {
		return String.format(NO_CLASS_WRITE_EFFECT_ANNOTATION, classSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param classSignature
	 * @return
	 */
	public static String noClassWriteEffectUsingClass(String methodSignature, long srcLn, String classSignature) {
		return String.format(NO_CLASS_WRITE_EFFECT_ANNOTATION_USING, classSignature, methodSignature, srcLn);
	}
	
	/**
	 * 
	 * @param fieldSignature
	 * @return
	 */
	public static String noFieldAnnotation(String fieldSignature) {
		return String.format(NO_FIELD_ANNOTATION, fieldSignature);
	}

	/**
	 * 
	 * @param fieldSignature
	 * @return
	 */
	public static String noFieldLevel(String fieldSignature) {
		return String.format(NO_FIELD_LEVEL, fieldSignature);
	}

	/**
	 * 
	 * @param type
	 * @param sourceCode
	 * @param srcLn
	 * @return
	 */
	public static String noLevelUpdatePossible(String type, String sourceCode, long srcLn) {
		return String.format(IMPOSSIBLE_LEVEL_UPDATE, type, sourceCode, srcLn);
	}
	
	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String noMethodAnnotation(String methodSignature) {
		return String.format(NO_METHOD_ANNOTATION, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String noMethodLevel(String methodSignature) {
		return String.format(NO_METHOD_LEVEL, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String noParameterAnnotation(String methodSignature) {
		return String.format(NO_PARAMETER_ANNOTATION, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param parameterName
	 * @param localName
	 * @param level
	 * @return
	 */
	public static String noParameterMatch(String methodSignature, long srcLn,
			String parameterName, String localName, String level) {
		return String.format(NO_PARAMETER_MATCH, level, localName, methodSignature, srcLn,
				parameterName);
	}

	/**
	 * 
	 * @param ifStmt
	 * @return
	 */
	public static String noProgramCounter(String ifStmt) {
		return String.format(NO_COUNTER,  ifStmt);
	}

	/**
	 * 
	 * @param srcLn
	 * @param methodSignature
	 * @param value
	 * @return
	 */
	public static String noSecurityLevel(long srcLn, String methodSignature, String value) {
		return String.format(NO_SECURITY_LEVEL, methodSignature, srcLn, value);
	}

	/**
	 * 
	 * @param ifStmt
	 * @return
	 */
	public static String noSuchProgramCounter(String ifStmt) {
		return String.format(NO_SUCH_COUNTER,  ifStmt);
	}
	
	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String noWriteEffectAnnotation(String methodSignature) {
		return String.format(NO_WRITE_EFFECT_ANNOTATION, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param rightLevel
	 * @param weakestSecurityLevel
	 * @return
	 */
	public static String onlyLowArrayElements(String methodSignature, long srcLn, String rightLevel, String weakestSecurityLevel) {
		return String.format(ARRAY_WEAKEST_ASSIGNMENT, methodSignature, srcLn, rightLevel, weakestSecurityLevel);
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	public static String reflectionIllegalLevelName(String level) {
		return String.format(REFLECTION_ILLEGAL_LEVEL_NAME, level);
	}

	/**
	 * 
	 * @param level
	 * @param sign
	 * @param usageOfSign
	 * @return
	 */
	public static String reflectionIllegalLevelSign(String level, String sign, String usageOfSign) {
		return String.format(REFLECTION_ILLEGAL_LEVEL_SIGN, sign, level, usageOfSign);
	}

	/**
	 * 
	 * @param methodSignatureId
	 * @return
	 */
	public static String reflectionInvalidMethodAnnotation(String methodSignatureId) {
		return String.format(REFLECTION_INVALID_ANNOTATION, methodSignatureId);
	}

	/**
	 * 
	 * @return
	 */
	public static String reflectionInvalidSootSecurityLevelClass() {
		return String.format(REFLECTION_INVALID_SECURITY_LEVEL_CLASS);
	}

	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String reflectionNoLevels(String methodSignature) {
		return String.format(REFLECTION_NO_LEVEL, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String reflectionNoMethod(String methodSignature) {
		return String.format(REFLECTION_NO_METHOD, methodSignature);
	}
	
	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String reflectionNoMethodAccess(String methodSignature) {
		return String.format(REFLECTION_NO_METHOD_ACCESS, methodSignature);
	}

	/**
	 * 
	 * @param methodSignatureId
	 * @return
	 */
	public static String reflectionNoMethodAnnotation(String methodSignatureId) {
		return String.format(REFLECTION_NO_ANNOTATION, methodSignatureId);
	}
	
	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String reflectionNonStaticMethod(String methodSignature) {
		return String.format(REFLECTION_NON_STATIC_METHOD, methodSignature);
	}
	
	/**
	 * 
	 * @param methodSignature
	 * @return
	 */
	public static String reflectionPossibleMistake(String methodSignature) {
		return String.format(REFLECTION_NO_CORRESPONDING_LEVEL, methodSignature);
	}
	
	/**
	 * 
	 * @param methodSignature
	 * @param effected
	 * @return
	 */
	public static String superfluousWriteEffect(String methodSignature, String effected) {
		return String.format(SUPERFLUOUS_WRITE_EFFECT, methodSignature, effected);
	}

	/**
	 * 
	 * @param switchCase
	 * @param switchName
	 * @param sourceCode
	 * @param srcLn
	 * @return
	 */
	public static String unimplementedSwitchCase(String switchCase, String switchName,
			String sourceCode, long srcLn) {
		return String.format(UNIMPLEMENTED_SWITCH_CASE, switchCase, switchName, sourceCode,
				srcLn);
	}

	/**
	 * 
	 * @param srcLn
	 * @param methodSignature
	 * @param obj
	 * @return
	 */
	public static String unknownConditionContent(long srcLn, String methodSignature, Object obj) {
		return String.format(UNKNOWN_CONDITION_CONTENT, srcLn, methodSignature,
				obj.toString(), obj.getClass().getName());
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param classSignature
	 * @return
	 */
	public static String usingLibraryClassNoClassWriteEffect(String methodSignature, long srcLn, String classSignature) {
		return String.format(USING_LIBRARY_CLASS_WRITE_EFFECT, methodSignature, srcLn, classSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param fieldSignature
	 * @return
	 */
	public static String usingLibraryFieldNoWriteEffect(String methodSignature, long srcLn,	String fieldSignature) {
		return String.format(USING_LIBRARY_WRITE_EFFECT, methodSignature, srcLn, fieldSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param expectedReturnLevel
	 * @param voidLevel
	 * @return
	 */
	public static String voidReturn(String methodSignature, long srcLn,
			String expectedReturnLevel, String voidLevel) {
		return String.format(VOID_RETURN, methodSignature, srcLn, expectedReturnLevel, voidLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param level
	 * @param voidLevel
	 * @return
	 */
	public static String voidReturnExpected(String methodSignature, long srcLn,	String level, String voidLevel) {
		return String.format(VOID_RETURN_EXPECTED, methodSignature, level, srcLn, voidLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param name
	 * @param localLevel
	 * @param valueLevel
	 * @return
	 */
	public static String weakenLocalVariable(String methodSignature, long srcLn, String name,
			String localLevel, String valueLevel) {
		return String.format(WEAKEN_LOCAL_VARIABLE, methodSignature, srcLn, name, localLevel, 
				valueLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param srcLn
	 * @param level
	 * @param expectedLevel
	 * @param parameterName
	 * @return
	 */
	public static String weakerArgumentExpected(String methodSignature,
			String invokedMethodSignature, long srcLn, String level, String expectedLevel,
			String parameterName) {
		return String.format(WEAKER_ARGUMENT_EXPECTED, methodSignature, srcLn,
				invokedMethodSignature, parameterName, expectedLevel, level);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param srcLn
	 * @param level
	 * @param expectedLevel
	 * @return
	 */
	public static String weakerReturnExpected(String methodSignature, long srcLn,
			String level, String expectedLevel) {
		return String.format(WEAKER_RETURN_EXPECTED, methodSignature, level, srcLn,
				expectedLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param srcLn
	 * @return
	 */
	public static String wrongArgumentParameterAmount(String methodSignature,
			String invokedMethodSignature, long srcLn) {
		return String.format(WRONG_PARAMETER_ARGUMENT_AMOUNT, invokedMethodSignature,
				methodSignature, srcLn);
	}
	
}
