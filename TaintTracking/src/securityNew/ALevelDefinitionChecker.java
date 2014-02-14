package securityNew;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import resource.Messages;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.util.Chain;
import exception.SootException.SecurityLevelException;

import logging.SecurityLogger;

public abstract class ALevelDefinitionChecker<U extends ILevel, T extends ILevelDefinition<U>>
		implements ILevelDefinitionChecker<U, T> {

	private static final String SIGNATURE_ID_INVALID = "public <T> T %s(T)";
	private static final String SIGNATURE_GLB = "public %s %s()";
	private static final String SIGNATURE_LUB = "public %s %s()";
	private static final String SIGNATURE_LEVELS = "public %s[] %s()";
	private static final String SIGNATURE_DEFAULT_VAR = "public %s %s()";
	private static final String SIGNATURE_LIB_CLASS_EFFECTS = "public List<%s> %s(SootClass sootClass)";
	private static final String SIGNATURE_LIB_METHOD_EFFECTS = "public List<%s> %s(SootMethod sootMethod)";
	private static final String SIGNATURE_LIB_FIELD = "public %s %s(SootField sootField)";
	private static final String SIGNATURE_LIB_PARAMETER = "public List<%s> %s(SootMethod sootMethod)";
	private static final String SIGNATURE_LIB_RETURN = "public %s %s(SootMethod sootMethod, List<%s> levels)";
	private final List<U> illegalLevelNames = new ArrayList<U>();
	private final T implementation;
	private final List<U> invalidIdFunctionAnnotation = new ArrayList<U>();
	private final List<U> invalidIdFunctions = new ArrayList<U>();
	private final List<U> levels = new ArrayList<U>();
	private final SecurityLogger logger;
	private final boolean logging;
	private final List<U> unavailableIdFunctionAnnotation = new ArrayList<U>();
	private final List<U> unavailableIdFunctions = new ArrayList<U>();
	private final Class<? extends Annotation> annotationClassReturnLevel;
	private final Class<? extends Annotation> annotationClassParameterLevels;
	private final static String EXCEPTION_INVALID_IMPLEMENTATION = String
			.format("The implementation of the class '%s' is invalid.",
					ILevelDefinition.DEF_PATH_JAVA);

	public ALevelDefinitionChecker(T implementation,
			Class<? extends Annotation> annotationClassReturnLevel,
			Class<? extends Annotation> annotationClassParameterLevels)
			throws SecurityLevelException {
		this(implementation, annotationClassReturnLevel,
				annotationClassParameterLevels, null, true, true);
	}

	protected ALevelDefinitionChecker(T implementation,
			Class<? extends Annotation> annotationClassReturnLevel,
			Class<? extends Annotation> annotationClassParameterLevels, SecurityLogger logger,
			boolean logging, boolean throwException)
			throws SecurityLevelException {
		this.logging = logging;
		this.logger = logging ? logger : null;
		this.implementation = implementation;
		this.annotationClassReturnLevel = annotationClassReturnLevel;
		this.annotationClassParameterLevels = annotationClassParameterLevels;
		checkValidity(throwException);
	}

	private void checkValidity(boolean throwException)
			throws SecurityLevelException {
		if (!checkNormalValiditiyOfImplemenation()
				|| !checkAdditionalValidityOfImplementation()) {
			if (throwException) {
				throw new SecurityLevelException(
						EXCEPTION_INVALID_IMPLEMENTATION);
			}
		}
	}

	private boolean checkNormalValiditiyOfImplemenation() {
		return checkValidityOfImportantInstanceMethods()
				&& (checkValidityOfAdditionalInstanceMethods()
						& checkCorrectnessOfConventionsForAllLevels()
						& checkCorrectnessOfConventionsForClass() & checkPossibleMistakes());
	}

	private boolean checkCorrectnessOfConventionsForClass() {
		boolean validity = true;
		String packageName = getImplementationClass().getPackage().getName();
		String className = getImplementationClass().getSimpleName();
		if (!packageName.equals(ILevelDefinition.DEF_PACKAGE_NAME)) {
			printException(Messages.get("checker.package.invalid_name",
					ILevelDefinition.DEF_PACKAGE_NAME));
			validity = false;
		}
		if (!className.equals(ILevelDefinition.DEF_CLASS_NAME)) {
			printException(Messages.get("checker.class.invalid_name",
					ILevelDefinition.DEF_CLASS_NAME));
			validity = false;
		}
		return validity;
	}

	private boolean checkPossibleMistakes() {
		List<String> levelNames = getLevelNames();
		for (Method method : getImplementationMethods()) {
			String methodName = method.getName();
			if (methodName.endsWith(ILevelDefinition.SUFFIX_METHOD_ID)) {
				String possibleLevelName = methodName.substring(
						0,
						methodName.length()
								- ILevelDefinition.SUFFIX_METHOD_ID.length());
				if (!levelNames.contains(possibleLevelName)) {
					Class<?>[] parameters = method.getParameterTypes();
					Class<?> returnType = method.getReturnType();
					boolean isStatic = Modifier.isStatic(method.getModifiers());
					boolean isPublic = Modifier.isPublic(method.getModifiers());
					if (parameters.length == 1
							&& parameters[0].equals(Object.class)
							&& returnType.equals(Object.class) && isStatic
							&& isPublic) {
						printWarning(Messages
								.get("checker.level.no_method",
										String.format(
												ILevelDefinitionChecker.ID_SIGNATURE_PATTERN,
												methodName)));
					}
				}
			}
		}
		return true;
	}

	private boolean checkCorrectnessOfConventionsForAllLevels() {
		boolean correctness = true;
		for (U level : getLevels()) {
			correctness &= checkCorrectnesOfConventions(level);
		}
		return correctness;
	}

	private List<String> getLevelNames() {
		List<String> names = new ArrayList<String>();
		for (U level : levels) {
			names.add(level.getName());
		}
		return names;
	}

	private List<String> getImplementationMethodNames() {
		List<String> names = new ArrayList<String>();
		for (Method method : getImplementationMethods()) {
			names.add(method.getName());
		}
		return names;
	}

	private boolean checkCorrectnesOfConventions(U level) {
		return checkCorrectnessOfLevelName(level)
				& checkCorrectnessOfIdFunction(level);
	}

	private boolean checkCorrectnessOfIdFunction(U level) {
		String idFunctionName = level.getName()
				+ ILevelDefinition.SUFFIX_METHOD_ID;
		String signatureIdFunction = String.format(ID_SIGNATURE_PATTERN,
				idFunctionName);
		if (!getImplementationMethodNames().contains(idFunctionName)) {
			printException(Messages.get("checker.id_func.none",
					signatureIdFunction));
			unavailableIdFunctions.add(level);
			return false;
		} else {
			boolean valid = true;
			try {
				Method method = getImplementationClass().getMethod(
						idFunctionName, Object.class);
				if (!Modifier.isStatic(method.getModifiers())) {
					printException(Messages
							.get("checker.id_func.not_static", String.format(
									SIGNATURE_ID_INVALID, idFunctionName)));
					invalidIdFunctions.add(level);
					valid = false;
				}
				boolean existsReturnAnnotation = false;
				boolean existsParameterAnnotation = false;
				for (Annotation annotation : method.getAnnotations()) {
					if (annotation.annotationType().equals(
							annotationClassReturnLevel)) {
						U returnLevel = extractLevelFromReturnAnnotation(annotation);
						existsReturnAnnotation = true;
						if (!returnLevel.equals(level)) {
							printException(Messages.get(
									"checker.id_func.invalid_return_level",
									signatureIdFunction));
							invalidIdFunctionAnnotation.add(level);
							valid = false;
						}
					} else if (annotation.annotationType().equals(
							annotationClassParameterLevels)) {
						existsParameterAnnotation = true;
						U[] parameterLevels = extractLevelsFromParameterAnnotation(annotation);
						if (parameterLevels == null
								|| parameterLevels.length != 1
								|| !parameterLevels[0].equals(level)) {
							printException(Messages.get(
									"checker.id_func.invalid_parameter_levels",
									signatureIdFunction));
							invalidIdFunctionAnnotation.add(level);
							valid = false;
						}
					}
				}
				if (!existsReturnAnnotation || !existsParameterAnnotation) {
					printException(Messages.get(
							"checker.id_func.no_annotation",
							signatureIdFunction));
					unavailableIdFunctionAnnotation.add(level);
					valid = false;
				}
			} catch (Exception e) {
				printException(Messages.get("checker.id_func.no_access",
						signatureIdFunction));
				unavailableIdFunctions.add(level);
				valid = false;
			}
			return valid;
		}
	}

	protected abstract U extractLevelFromReturnAnnotation(Annotation annotation);

	protected abstract U[] extractLevelsFromParameterAnnotation(
			Annotation annotation);

	private boolean checkCorrectnessOfLevelName(U level) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(level.getName());
		boolean validity = m.find();
		if (validity) {
			printException(Messages.get("checker.level.invalid_char",
					level.getName()));
			illegalLevelNames.add(level);
		}
		return !validity;
	}

	private boolean checkValidityOfAdditionalInstanceMethods() {
		boolean validity = true;
		Class<U> base = implementation.getBaseClass();
		if (implementation.getDefaultVariableLevel() == null) {
			validity = false;
			printException(Messages.get("checker.level.no_default_variable",
					String.format(SIGNATURE_DEFAULT_VAR, base.getSimpleName(),
							ILevelDefinition.METHOD_DEFAULT_VAR)));
		}
		if (implementation.getLibraryClassWriteEffects(generateTestClass()) == null) {
			validity = false;
			printException(Messages.get("checker.level.no_lib_class_effects",
					String.format(SIGNATURE_LIB_CLASS_EFFECTS,
							base.getSimpleName(),
							ILevelDefinition.METHOD_LIB_CLASS_EFFECTS)));
		}
		if (implementation.getLibraryMethodWriteEffects(generatedTestMethod(0)) == null) {
			validity = false;
			printException(Messages.get("checker.level.no_lib_method_effects",
					String.format(SIGNATURE_LIB_METHOD_EFFECTS,
							base.getSimpleName(),
							ILevelDefinition.METHOD_LIB_METHOD_EFFECTS)));
		}
		if (implementation.getLibraryFieldLevel(generateTestField()) == null) {
			validity = false;
			printException(Messages.get("checker.level.no_lib_field", String
					.format(SIGNATURE_LIB_FIELD, base.getSimpleName(),
							ILevelDefinition.METHOD_LIB_FIELD)));
		}
		if (implementation.getLibraryParameterLevel(generatedTestMethod(0)) == null
				|| implementation
						.getLibraryParameterLevel(generatedTestMethod(1)) == null
				|| implementation.getLibraryParameterLevel(
						generatedTestMethod(1)).size() != 1) {
			validity = false;
			printException(Messages.get("checker.level.no_lib_parameter",
					String.format(SIGNATURE_LIB_PARAMETER,
							base.getSimpleName(),
							ILevelDefinition.METHOD_LIB_PARAMETER)));
		}
		if (implementation.getLibraryReturnLevel(
				generatedTestMethod(levels.size()), levels) == null) {
			validity = false;
			printException(Messages.get("checker.level.no_lib_return", String
					.format(SIGNATURE_LIB_RETURN, base.getSimpleName(),
							ILevelDefinition.METHOD_LIB_RETURN,
							base.getSimpleName())));
		}
		return validity;
	}

	private boolean checkValidityOfImportantInstanceMethods() {
		Class<U> base = implementation.getBaseClass();
		if (implementation.getLevels() == null
				|| implementation.getLevels().length < 1) {
			printException(Messages.get("checker.level.no_levels", String
					.format(SIGNATURE_LEVELS, base.getSimpleName(),
							ILevelDefinition.METHOD_LEVELS)));
			return false;
		} else {
			levels.addAll(Arrays.asList(implementation.getLevels()));
		}
		U glb = implementation.getGreatesLowerBoundLevel();
		if (glb == null || !levels.contains(glb)) {
			printException(Messages.get("checker.level.no_glb", String.format(
					SIGNATURE_GLB, base.getSimpleName(),
					ILevelDefinition.METHOD_GLB)));
			return false;
		}
		U lub = implementation.getLeastUpperBoundLevel();
		if (lub == null || !levels.contains(lub)) {
			printException(Messages.get("checker.level.no_lub", String.format(
					SIGNATURE_LUB, base.getSimpleName(),
					ILevelDefinition.METHOD_LUB)));
			return false;
		}
		return true;
	}

	public final List<U> getLevels() {
		return levels;
	}

	protected final void addIllegalLevelNames(U illegalLevelName) {
		this.illegalLevelNames.add(illegalLevelName);
	}

	protected final void addInvalidIdFunction(U invalidIdFunction) {
		this.invalidIdFunctions.add(invalidIdFunction);
	}

	protected final void addInvalidIdFunctionAnnotation(
			U invalidIdFunctionAnnotation) {
		this.invalidIdFunctionAnnotation.add(invalidIdFunctionAnnotation);
	}

	protected final void addUnavailableIdFunction(U unavailableIdFunction) {
		this.unavailableIdFunctions.add(unavailableIdFunction);
	}

	protected final void addUnavailableIdFunctionAnnotation(
			U unavailableIdFunctionAnnotation) {
		this.unavailableIdFunctionAnnotation
				.add(unavailableIdFunctionAnnotation);
	}

	protected abstract boolean checkAdditionalValidityOfImplementation();

	protected final List<U> getIllegalLevelNames() {
		return this.illegalLevelNames;
	}

	protected final T getImplementation() {
		return implementation;
	}

	private Class<?> getImplementationClass() {
		return implementation.getClass();
	}

	private Method[] getImplementationMethods() {
		return getImplementationClass().getDeclaredMethods();
	}

	protected final List<U> getInvalidIdFunctionAnnotation() {
		return this.invalidIdFunctionAnnotation;
	}

	protected final List<U> getInvalidIdFunctions() {
		return this.invalidIdFunctions;
	}

	protected final List<U> getUnavailableIdFunctionAnnotations() {
		return this.unavailableIdFunctionAnnotation;
	}

	protected final List<U> getUnavailableIdFunctions() {
		return this.unavailableIdFunctions;
	}

	protected final void printException(Exception e, String msg) {
		if (logging) {
			if (this.logger != null) {
				logger.securitychecker(msg, e);
			} else {
				System.err.println(msg);
			}
		}
	}

	protected final void printException(String msg) {
		if (logging) {
			if (this.logger != null) {
				logger.securitychecker(msg);
			} else {
				System.err.println(msg);
			}
		}
	}

	protected final void printWarning(String msg) {
		if (logging) {
			if (this.logger != null) {
				logger.warning(ILevelDefinition.DEF_CLASS_NAME, 0, msg);
			} else {
				System.out.println(msg);
			}
		}
	}

	private SootMethod generatedTestMethod(int parameterCount) {
		List<RefType> parameterTypes = new ArrayList<RefType>();
		for (int i = 0; i < parameterCount; i++) {
			parameterTypes.add(RefType.v("int"));
		}
		SootClass sootClass = generateTestClass();
		SootMethod sootMethod = new SootMethod("testMethod", parameterTypes,
				RefType.v("int"));
		sootMethod.setDeclaringClass(sootClass);
		sootClass.addMethod(sootMethod);
		JimpleBody body = Jimple.v().newBody(sootMethod);
		sootMethod.setActiveBody(body);
		Chain<Unit> units = body.getUnits();
		units.add(Jimple.v().newReturnStmt(IntConstant.v(42)));
		return sootMethod;
	}

	private SootClass generateTestClass() {
		return new SootClass("checker.TestClass");
	}

	private SootField generateTestField() {
		SootClass sootClass = generateTestClass();
		SootField sootField = new SootField("test", RefType.v("int"));
		sootClass.addField(sootField);
		return sootField;
	}

}