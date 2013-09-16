package utils;

import soot.Value;

public class SecurityMessages {
	
	private static final String MORE_PARAMETER_THAN_LEVEL = "Method <%s> has %d %s. But unfortunately only %d parameter security %s are defined.";
	private static final String MORE_LEVEL_THAN_PARAMETER = "Method <%s> has %d %s. But %d parameter security %s are defined.";
	private static final String INVALID_PARAMETER_LEVEL = "Method <%s> has an invalid parameter security level named '%s'.";
	private static final String NO_PARAMETER_ANNOTATION = "Method <%s> has no parameter security levels annotation.";
	private static final String INVALID_RETURN_EQUATION = "Method <%s> has an invalid return security level equation.";
	private static final String INVALID_RETURN_LEVEL = "Method <%s> has an invalid return security level '%s'.";
	private static final String INCOMPATIBLE_EQUATION_LEVEL = "Method <%s> has an invalid return security level equation. The level '%s' is not compatible with other levels.";
	private static final String NO_METHOD_LEVEL = "Method <%s> has no return security level.";
	private static final String NOT_REQUIRED_METHOD_ANNOTATION = "Constructor <%s> do not need a return security level.";
	private static final String NO_METHOD_ANNOTATION = "Method <%s> has no return security level annotation.";
	/** */
	private static final String NO_COUNTER = "Analysis tries to remove the statement '%s' from the program counter but the program counter is empty.";
	/** */
	private static final String NO_SUCH_COUNTER = "Analysis tries to remove the statement '%s' from the program counter but this statement is not in the program counter.";
	/** */
	private static final String IMPOSSIBLE_LEVEL_UPDATE = "Can't update the security level of %s '%s' at source line %d.";
	/** */
	private static final String NO_FIELD_ANNOTATION = "Field <%s> has no security level annotation.";
	/** */
	private static final String NO_FIELD_LEVEL = "Field <%s> has no security level.";
	/** */
	private static final String INVALID_FIELD_LEVEL = "Field <%s> has an invalid security level '%s'.";
	/** */
	private static final String INTERRUPT_INVALID_PARAMETERS = "Analysis will not performed for method <%s> because of invalid parameter security level.";
	/** */
	private static final String INTERRUPT_INVALID_RETURN = "Analysis will not performed for method <%s> because of an invalid return security level.";
	/** */
	private static final String VALUE_WITHOUT_LEVEL = "The inspected value at source line %d in method <%s> has no security level.";
	/** */
	private static final String REFLECTION_NO_CORRESPONDING_LEVEL = "Method <%s> has no corresponding level.";
	/** */
	private static final String REFLECTION_NO_ANNOTATION = "No security annotation for method <%s>.";
	/** */
	private static final String REFLECTION_INVALID_ANNOTATION = "Security annotation of method <%s> is not valid.";
	/** */
	private static final String REFLECTION_NON_STATIC_METHOD = "Method <%s> is not static.";
	/** */
	private static final String REFLECTION_INVALID_SECURITY_LEVEL_CLASS = "The implementation of the SootSecurityLevel class is invalid.";
	/** */
	private static final String REFLECTION_ILLEGAL_LEVEL_SIGN = "Is not allowed to use the sign %s inside of the level '%s' because the usage for %s.";
	/** */
	private static final String REFLECTION_ILLEGAL_LEVEL_NAME = "The level '%s' is an internal level which can't be used as customized level.";
	/** */
	private static final String REFLECTION_NO_LEVEL = "No or only one security level defined in the method <%s>.";
	/** */
	private static final String REFLECTION_NO_METHOD_ACCESS = "Couldn't access the method <%s> in the class SootSecurityLevel.";
	/** */
	private static final String REFLECTION_NO_METHOD = "Couldn't find the method <%s> in the class SootSecurityLevel.";
	/** */
	private static final String ERROR_COMPILATION_SECURITY_LEVEL_CLASS = "Couldn't compile the file '%s'.";
	/** */
	private static final String ERROR_INSTANTIATION_SECURITY_LEVEL_CLASS = "Couldn't finish the instantiation of the SootSecurityLevel class correctly.";
	/** */
	private static final String NO_INSTANTIATION_SECURITY_LEVEL_CLASS = "Couldn't instantiate an object of the type SootSecurityLevel.";
	/** */
	private static final String NOT_LOAD_SECURITY_LEVEL_CLASS = "Couldn't load the SootSecurityLevel class with path '%s'.";
	/** */
	private static final String INVALID_WORKING_URL = "Couldn't create the URL to the directory which contains the SootSecurityLevel class.";
	/** */
	private static final String WEAKEN_LOCAL_VARIABLE = "In method <%s> at source line %d the variable \"%s\" was weakened from '%s' to '%s' security level.";
	/** */
	private static final String ASSIGNMENT_LIBRARY_FIELD = "Assignment to the library field <%s> at source line %d in method <%s>. Weakest security level is assumed for this field.";
	/** */
	private static final String ASSIGNMENT_WEAKER_FIELD = "In method <%s> at source line %d a value with '%s' security level is assigned to the field <%s> which has the weaker security level '%s'.";
	/** */
	private static final String ACCESS_LIBRARY_FIELD = "The accessed field <%s> at source line %d in method <%s> is a library field. No security check can be done.";
	/** */
	private static final String INVALID_FIELD_ANNOTATION = "The accessed field <%s> at source line %d in method <%s> has invalid field annotation.";
	/** */
	private static final String INVALID_RETURN_ANNOTATION = "The invoked method <%s> at source line %d in method <%s> has invalid return security level annotation.";
	/** */
	private static final String INVOCATION_LIBRARY_METHOD = "The invoked method <%s> at source line %d in method <%s> is a library method. No security check can be done.";
	/** */
	private static final String INVOCATION_LIBRARY_METHOD_MAX_LEVEL = "The invoked method <%s> at source line %d in method <%s> is a library method. Therefore the maximal security level '%s' of the arguments will be taken as result security level.";
	/** */
	private static final String LOCAL_NOT_FOUND = "The local \"%s\" in method <%s> at source line %d couldn't be updated to the level '%s', because this local is unknown.";
	/** */
	private static final String NO_PARAMETER_MATCH = "Couldn't attach the '%s' security level to the local \"%s\" in method <%s> at source line %d, because the given information about method parameter \"%s\" doesn't match to the local information.";
	/** */
	private static final String NO_SECURITY_LEVEL = "In method <%s> at source line %d the value '%s' has no security level.";
	/** */
	private static final String SWITCH_EXCEPTION_CATCHED = "During processing '%s' in method <%s> at source line %d an exception was thrown.";
	/** */
	private static final String UNIMPLEMENTED_SWITCH_CASE = "Invoke of the '%s' case in the %s by the statement '%s' at line %d [NOT IMPLEMENTED].";
	/** */
	private static final String UNKNOWN_CONDITION_CONTENT = "The IF-condition at source line %d in method <%s> contains an unknown usebox type '%s' of class [%s].";
	/** */
	private static final String VOID_RETURN = "Method <%s> has a void return statement at source line %d. But the expected return security level '%s' doesn't match the level '%s'.";
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
	 * @param sourceLine
	 * @return
	 */
	public static String accessOfLibraryField(String methodSignature, String accessedField,
			long sourceLine) {
		return String.format(ACCESS_LIBRARY_FIELD, accessedField, sourceLine, methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param sourceLine
	 * @param stmt
	 * @return
	 */
	public static String catchSwitchException(String methodSignature, long sourceLine, String stmt) {
		return String.format(SWITCH_EXCEPTION_CATCHED, stmt, methodSignature, sourceLine);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param accessedFieldSignature
	 * @param sourceLine
	 * @return
	 */
	public static String invalidFieldAnnotation(String methodSignature,
			String accessedFieldSignature, long sourceLine) {
		return String.format(INVALID_FIELD_ANNOTATION, accessedFieldSignature, sourceLine,
				methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param sourceLine
	 * @return
	 */
	public static String invalidReturnAnnotation(String methodSignature,
			String invokedMethodSignature, long sourceLine) {
		return String.format(INVALID_RETURN_ANNOTATION, invokedMethodSignature, sourceLine,
				methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param sourceLine
	 * @return
	 */
	public static String invocationOfLibraryMethod(String methodSignature,
			String invokedMethodSignature, long sourceLine) {
		return String.format(INVOCATION_LIBRARY_METHOD, invokedMethodSignature, sourceLine,
				methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param sourceLine
	 * @param maxLevel
	 * @return
	 */
	public static String invocationOfLibraryMethodMaxArgumentLevel(String methodSignature,
			String invokedMethodSignature, long sourceLine, String maxLevel) {
		return String.format(INVOCATION_LIBRARY_METHOD_MAX_LEVEL, invokedMethodSignature,
				sourceLine, methodSignature, maxLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param sourceLine
	 * @param localName
	 * @param level
	 * @return
	 */
	public static String localNotFoundUpdate(String methodSignature, long sourceLine, String localName,
			String level) {
		return String.format(LOCAL_NOT_FOUND, localName, methodSignature, sourceLine, level);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param sourceLine
	 * @param parameterName
	 * @param localName
	 * @param level
	 * @return
	 */
	public static String noParameterMatch(String methodSignature, long sourceLine,
			String parameterName, String localName, String level) {
		return String.format(NO_PARAMETER_MATCH, level, localName, methodSignature, sourceLine,
				parameterName);
	}

	/**
	 * 
	 * @param sourceLine
	 * @param methodSignature
	 * @param value
	 * @return
	 */
	public static String noSecurityLevel(long sourceLine, String methodSignature, Value value) {
		return String.format(NO_SECURITY_LEVEL, methodSignature, sourceLine, value.toString());
	}

	/**
	 * 
	 * @param switchCase
	 * @param switchName
	 * @param sourceCode
	 * @param sourceLine
	 * @return
	 */
	public static String unimplementedSwitchCase(String switchCase, String switchName,
			String sourceCode, long sourceLine) {
		return String.format(UNIMPLEMENTED_SWITCH_CASE, switchCase, switchName, sourceCode,
				sourceLine);
	}

	/**
	 * 
	 * @param sourceLine
	 * @param methodSignature
	 * @param obj
	 * @return
	 */
	public static String unknownConditionContent(long sourceLine, String methodSignature, Object obj) {
		return String.format(UNKNOWN_CONDITION_CONTENT, sourceLine, methodSignature,
				obj.toString(), obj.getClass().getName());
	}

	/**
	 * 
	 * @param methodSignature
	 * @param sourceLine
	 * @param expectedReturnLevel
	 * @param voidLevel
	 * @return
	 */
	public static String voidReturn(String methodSignature, long sourceLine,
			String expectedReturnLevel, String voidLevel) {
		return String.format(VOID_RETURN, methodSignature, sourceLine, expectedReturnLevel, voidLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param sourceLine
	 * @param level
	 * @param expectedLevel
	 * @param parameterName
	 * @return
	 */
	public static String weakerArgumentExpected(String methodSignature,
			String invokedMethodSignature, long sourceLine, String level, String expectedLevel,
			String parameterName) {
		return String.format(WEAKER_ARGUMENT_EXPECTED, methodSignature, sourceLine,
				invokedMethodSignature, parameterName, expectedLevel, level);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param sourceLine
	 * @param level
	 * @param expectedLevel
	 * @return
	 */
	public static String weakerReturnExpected(String methodSignature, long sourceLine,
			String level, String expectedLevel) {
		return String.format(WEAKER_RETURN_EXPECTED, methodSignature, level, sourceLine,
				expectedLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param invokedMethodSignature
	 * @param sourceLine
	 * @return
	 */
	public static String wrongArgumentParameterAmount(String methodSignature,
			String invokedMethodSignature, long sourceLine) {
		return String.format(WRONG_PARAMETER_ARGUMENT_AMOUNT, invokedMethodSignature,
				methodSignature, sourceLine);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param fieldSignature
	 * @param sourceLine
	 * @param fieldLevel
	 * @param level
	 * @return
	 */
	public static String assignmentToWeakerField(String methodSignature,
			String fieldSignature, long sourceLine, String fieldLevel, String level) {
		return String.format(ASSIGNMENT_WEAKER_FIELD, methodSignature, sourceLine, level, 
				fieldSignature, fieldLevel);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param fieldSignature
	 * @param sourceLine
	 * @return
	 */
	public static String assignmentToLibraryField(String methodSignature,
			String fieldSignature, long sourceLine) {
		return String.format(ASSIGNMENT_LIBRARY_FIELD, fieldSignature, sourceLine, 
				methodSignature);
	}

	/**
	 * 
	 * @param methodSignature
	 * @param sourceLine
	 * @param name
	 * @param localLevel
	 * @param valueLevel
	 * @return
	 */
	public static String weakenLocalVariable(String methodSignature, long sourceLine, String name,
			String localLevel, String valueLevel) {
		return String.format(WEAKEN_LOCAL_VARIABLE, methodSignature, sourceLine, name, localLevel, 
				valueLevel);
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
	 * @param className
	 * @return
	 */
	public static String impossibleToLoadSecurityLevelClass(String className) {
		return String.format(NOT_LOAD_SECURITY_LEVEL_CLASS, className);
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
	 * @return
	 */
	public static String errorInstantiationSecurityLevelClass() {
		return String.format(ERROR_INSTANTIATION_SECURITY_LEVEL_CLASS);
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
	 * @param methodSignature
	 * @return
	 */
	public static String reflectionNoLevels(String methodSignature) {
		return String.format(REFLECTION_NO_LEVEL, methodSignature);
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
	public static String reflectionNonStaticMethod(String methodSignature) {
		return String.format(REFLECTION_NON_STATIC_METHOD, methodSignature);
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
	public static String reflectionPossibleMistake(String methodSignature) {
		return String.format(REFLECTION_NO_CORRESPONDING_LEVEL, methodSignature);
	}

	/**
	 * 
	 * @param sourceLine
	 * @param methodSignature
	 * @return
	 */
	public static String accessToValueWithoutSecurityLevel(long sourceLine, String methodSignature) {
		return String.format(VALUE_WITHOUT_LEVEL, sourceLine, methodSignature);
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
	 * @param methodSignature
	 * @return
	 */
	public static String interruptInvalidParameters(String methodSignature) {
		return String.format(INTERRUPT_INVALID_PARAMETERS, methodSignature);
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
	 * @param fieldSignature
	 * @param level
	 * @return
	 */
	public static String invalidFieldLevel(String fieldSignature, String level) {
		return String.format(INVALID_FIELD_LEVEL, fieldSignature, level);
	}

	/**
	 * 
	 * @param type
	 * @param sourceCode
	 * @param sourceLine
	 * @return
	 */
	public static String noLevelUpdatePossible(String type, String sourceCode, long sourceLine) {
		return String.format(IMPOSSIBLE_LEVEL_UPDATE, type, sourceCode, sourceLine);
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
	 * @param ifStmt
	 * @return
	 */
	public static String noProgramCounter(String ifStmt) {
		return String.format(NO_COUNTER,  ifStmt);
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
	public static String constructorReturnNotRequired(String methodSignature) {
		return String.format(NOT_REQUIRED_METHOD_ANNOTATION, methodSignature);
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
	 * @param voidLevel
	 * @return
	 */
	public static String incompatibaleParameterLevels(String methodSignature, String voidLevel) {
		return String.format(INCOMPATIBLE_EQUATION_LEVEL, methodSignature, voidLevel);
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
	 * @param methodSignature
	 * @return
	 */
	public static String invalidReturnEquation(String methodSignature) {
		return String.format(INVALID_RETURN_EQUATION, methodSignature);
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
	 * @param level
	 * @return
	 */
	public static String invalidParameterLevel(String methodSignature, String level) {
		return String.format(INVALID_PARAMETER_LEVEL, methodSignature, level);
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
	
}
