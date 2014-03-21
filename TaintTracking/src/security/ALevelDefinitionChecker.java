package security;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import annotation.JavaAnnotationDAO;

import resource.Configuration;
import static resource.Messages.getMsg;
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

import logging.AnalysisLog;

public abstract class ALevelDefinitionChecker implements ILevelDefinitionChecker {

	public static final String ID_SIGNATURE_PATTERN = "public static <T> T %s(T)";
	private final static String EXCEPTION_INVALID_IMPLEMENTATION = String.format("The implementation of the class '%s' is invalid.",
			Configuration.DEF_PATH_JAVA);
	private static final String SIGNATURE_ID_INVALID = "public <T> T %s(T)";
	private final List<ILevel> illegalLevelNames = new ArrayList<ILevel>();
	private final ILevelDefinition implementation;
	private boolean invalid;
	private final List<ILevel> invalidIdFunctionAnnotation = new ArrayList<ILevel>();
	private final List<ILevel> invalidIdFunctions = new ArrayList<ILevel>();
	private final List<ILevel> levels = new ArrayList<ILevel>();
	private final AnalysisLog logger;
	private final boolean logging;
	private final List<ILevel> unavailableIdFunctionAnnotation = new ArrayList<ILevel>();
	private final List<ILevel> unavailableIdFunctions = new ArrayList<ILevel>();

	public ALevelDefinitionChecker(ILevelDefinition implementation) throws SecurityLevelException {
		this(implementation, null, true, true);
	}

	protected ALevelDefinitionChecker(ILevelDefinition implementation, AnalysisLog logger, boolean logging, boolean throwException)
			throws SecurityLevelException {
		this.logging = logging;
		this.logger = logging ? logger : null;
		this.implementation = implementation;
		checkValidity(throwException);
	}

	public final List<ILevel> getLevels() {
		return levels;
	}

	public boolean isValid() {
		return !invalid;
	}

	private boolean checkAnnotationsOfAnnotationClass(Class<? extends Annotation> annotationClass, ElementType[] elementTypes) {
		boolean validity = true;
		if (annotationClass.isAnnotationPresent(Retention.class)) {
			Retention annotation = annotationClass.getAnnotation(Retention.class);
			if (!annotation.value().equals(RetentionPolicy.RUNTIME)) {
				printException(getMsg("checker.annotation.invalid_visibility", annotationClass.getName(), RetentionPolicy.RUNTIME.name()));
				validity = false;
			}
		} else {
			printException(getMsg("checker.annotation.missing_annotation", annotationClass.getName(), Retention.class.getName()));
			return false;
		}
		if (annotationClass.isAnnotationPresent(Target.class)) {
			Target annotation = annotationClass.getAnnotation(Target.class);
			List<ElementType> present = Arrays.asList(annotation.value());
			List<ElementType> required = Arrays.asList(elementTypes);
			for (ElementType elementType : required) {
				if (!present.contains(elementType)) {
					printException(getMsg("checker.annotation.missing_element_type", annotationClass.getName(), elementType.name()));
					validity = false;
				}
			}
			for (ElementType elementType : present) {
				if (!required.contains(elementType)) {
					printException(getMsg("checker.annotation.invalid_element_type", annotationClass.getName(), elementType.name()));
					validity = false;
				}
			}
		} else {
			printException(getMsg("checker.annotation.missing_annotation", annotationClass.getName(), Target.class.getName()));
			return false;
		}
		return validity;
	}

	private boolean checkConventionsOfAnnotationClass(Class<? extends Annotation> annotationClass, ElementType[] elementTypes) {
		boolean validity = true;
		if (annotationClass.getDeclaringClass() == null) {
			printException(getMsg("checker.annotation.invalid_declaration", annotationClass.getName()));
			validity = false;
		}
		if (!annotationClass.getPackage().getName().equals(Configuration.DEF_PACKAGE_NAME)) {
			printException(getMsg("checker.annotation.invalid_package", annotationClass.getName(), Configuration.DEF_PACKAGE_NAME));
			validity = false;
		}
		if (!annotationClass.isAnnotation()) {
			printException(getMsg("checker.annotation.invalid_annotation_class", annotationClass.getName()));
			validity = false;
		}
		return validity & checkAnnotationsOfAnnotationClass(annotationClass, elementTypes);
	}

	private boolean checkConventionsOfAnnotationClasses() {
		boolean validity = true;
		if (implementation.getAnnotationClassFieldLevel() == null) {
			printException(getMsg("checker.annotation.no_annotation_class", getMsg("other.field_level")));
			return false;
		}
		if (implementation.getAnnotationClassParameterLevel() == null) {
			printException(getMsg("checker.annotation.no_annotation_class", getMsg("other.parameter_levels")));
			return false;
		}
		if (implementation.getAnnotationClassReturnLevel() == null) {
			printException(getMsg("checker.annotation.no_annotation_class", getMsg("other.write_effects")));
			return false;
		}
		if (implementation.getAnnotationClassEffects() == null) {
			printException(getMsg("checker.annotation.no_annotation_class", getMsg("other.return_level")));
			return false;
		}
		Map<Class<? extends Annotation>, ElementType[]> annotations = new HashMap<Class<? extends Annotation>, ElementType[]>();
		annotations.put(implementation.getAnnotationClassFieldLevel(), new ElementType[] { ElementType.FIELD });
		annotations.put(implementation.getAnnotationClassParameterLevel(), new ElementType[] { ElementType.METHOD, ElementType.CONSTRUCTOR });
		annotations.put(implementation.getAnnotationClassReturnLevel(), new ElementType[] { ElementType.METHOD });
		annotations.put(implementation.getAnnotationClassEffects(), new ElementType[] { ElementType.METHOD, ElementType.CONSTRUCTOR,
				ElementType.TYPE });
		for (Class<? extends Annotation> annotation : annotations.keySet()) {
			if (!checkConventionsOfAnnotationClass(annotation, annotations.get(annotation))) {
				validity = false;
			}
		}
		return validity;
	}

	private boolean checkCorrectnesOfConventions(ILevel level) {
		return checkCorrectnessOfLevelName(level) & checkCorrectnessOfIdFunction(level);
	}

	private boolean checkCorrectnessOfConventionsForAllLevels() {
		boolean correctness = true;
		for (ILevel level : getLevels()) {
			correctness &= checkCorrectnesOfConventions(level);
		}
		return correctness;
	}

	private boolean checkCorrectnessOfConventionsForClass() {
		boolean validity = true;
		String packageName = getImplementationClass().getPackage().getName();
		String className = getImplementationClass().getSimpleName();
		if (!packageName.equals(Configuration.DEF_PACKAGE_NAME)) {
			printException(getMsg("checker.package.invalid_name", Configuration.DEF_PACKAGE_NAME));
			validity = false;
		}
		if (!className.equals(Configuration.DEF_CLASS_NAME)) {
			printException(getMsg("checker.class.invalid_name", Configuration.DEF_CLASS_NAME));
			validity = false;
		}
		return validity;
	}

	private boolean checkCorrectnessOfIdFunction(ILevel level) {
		String idFunctionName = level.getName() + Configuration.SUFFIX_METHOD_ID;
		String signatureIdFunction = String.format(ID_SIGNATURE_PATTERN, idFunctionName);
		if (!getImplementationMethodNames().contains(idFunctionName)) {
			printException(getMsg("checker.id_func.none", signatureIdFunction));
			unavailableIdFunctions.add(level);
			return false;
		} else {
			boolean valid = true;
			try {
				Method method = getImplementationClass().getMethod(idFunctionName, Object.class);
				if (!Modifier.isStatic(method.getModifiers())) {
					printException(getMsg("checker.id_func.not_static", String.format(SIGNATURE_ID_INVALID, idFunctionName)));
					invalidIdFunctions.add(level);
					valid = false;
				}
				boolean existsReturnAnnotation = false;
				boolean existsParameterAnnotation = false;
				for (Annotation annotation : method.getAnnotations()) {
					if (annotation.annotationType().equals(implementation.getAnnotationClassReturnLevel())) {
						try {
							ILevel returnLevel = implementation.extractReturnLevel(new JavaAnnotationDAO(annotation));
							existsReturnAnnotation = true;
							if (!returnLevel.equals(level)) {
								printException(getMsg("checker.id_func.invalid_return_level", signatureIdFunction));
								invalidIdFunctionAnnotation.add(level);
								valid = false;
							}
						} catch (Exception e) {
							printException(getMsg("checker.annotation.error_conversion", getMsg("other.return_level")));
							return false;
						}

					} else if (annotation.annotationType().equals(implementation.getAnnotationClassParameterLevel())) {
						try {
							existsParameterAnnotation = true;
							List<ILevel> parameterLevels = implementation.extractParameterLevels(new JavaAnnotationDAO(annotation));
							if (parameterLevels == null || parameterLevels.size() != 1 || !parameterLevels.get(0).equals(level)) {
								printException(getMsg("checker.id_func.invalid_parameter_levels", signatureIdFunction));
								invalidIdFunctionAnnotation.add(level);
								valid = false;
							}
						} catch (Exception e) {
							printException(getMsg("checker.annotation.error_conversion", getMsg("other.return_level")));
							return false;
						}
					}
				}
				if (!existsReturnAnnotation || !existsParameterAnnotation) {
					printException(getMsg("checker.id_func.no_annotation", signatureIdFunction));
					unavailableIdFunctionAnnotation.add(level);
					valid = false;
				}
			} catch (Exception e) {
				printException(getMsg("checker.id_func.no_access", signatureIdFunction));
				unavailableIdFunctions.add(level);
				valid = false;
			}
			return valid;
		}
	}

	private boolean checkCorrectnessOfLevelName(ILevel level) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(level.getName());
		boolean validity = m.find();
		if (validity) {
			printException(getMsg("checker.level.invalid_char", level.getName()));
			illegalLevelNames.add(level);
		}
		return !validity;
	}

	private boolean checkNormalValiditiyOfImplemenation() {
		return checkValidityOfImportantInstanceMethods()
				&& checkConventionsOfAnnotationClasses()
				&& (checkValidityOfAdditionalInstanceMethods() & checkCorrectnessOfConventionsForAllLevels()
						& checkCorrectnessOfConventionsForClass() & checkPossibleMistakes());
	}

	private boolean checkPossibleMistakes() {
		List<String> levelNames = getLevelNames();
		for (Method method : getImplementationMethods()) {
			String methodName = method.getName();
			if (methodName.endsWith(Configuration.SUFFIX_METHOD_ID)) {
				String possibleLevelName = methodName.substring(0, methodName.length() - Configuration.SUFFIX_METHOD_ID.length());
				if (!levelNames.contains(possibleLevelName)) {
					Class<?>[] parameters = method.getParameterTypes();
					Class<?> returnType = method.getReturnType();
					boolean isStatic = Modifier.isStatic(method.getModifiers());
					boolean isPublic = Modifier.isPublic(method.getModifiers());
					if (parameters.length == 1 && parameters[0].equals(Object.class) && returnType.equals(Object.class) && isStatic && isPublic) {
						printWarning(getMsg("checker.level.no_method", String.format(ID_SIGNATURE_PATTERN, methodName)));
					}
				}
			}
		}
		return true;
	}

	private void checkValidity(boolean throwException) throws SecurityLevelException {
		invalid = !checkNormalValiditiyOfImplemenation() || !checkAdditionalValidityOfImplementation();
		if (invalid) {
			if (throwException) {
				throw new SecurityLevelException(EXCEPTION_INVALID_IMPLEMENTATION);
			}
		}
	}

	private boolean checkValidityOfAdditionalInstanceMethods() {
		boolean validity = true;
		if (implementation.getDefaultVariableLevel() == null) {
			validity = false;
			printException(getMsg("checker.level.no_default_variable", ALevelDefinition.SIGNATURE_DEFAULT_VAR));
		}
		if (implementation.getLibraryClassWriteEffects(generateTestClass()) == null) {
			validity = false;
			printException(getMsg("checker.level.no_lib_class_effects", ALevelDefinition.SIGNATURE_LIB_CLASS));
		}
		if (implementation.getLibraryMethodWriteEffects(generatedTestMethod(0)) == null) {
			validity = false;
			printException(getMsg("checker.level.no_lib_method_effects", ALevelDefinition.SIGNATURE_LIB_METHOD));
		}
		if (implementation.getLibraryFieldLevel(generateTestField()) == null) {
			validity = false;
			printException(getMsg("checker.level.no_lib_field", ALevelDefinition.SIGNATURE_LIB_FIELD));
		}
		if (implementation.getLibraryParameterLevel(generatedTestMethod(0)) == null
				|| implementation.getLibraryParameterLevel(generatedTestMethod(1)) == null
				|| implementation.getLibraryParameterLevel(generatedTestMethod(1)).size() != 1) {
			validity = false;
			printException(getMsg("checker.level.no_lib_parameter", ALevelDefinition.SIGNATURE_LIB_PARAM));
		}
		if (implementation.getLibraryReturnLevel(generatedTestMethod(levels.size()), levels) == null) {
			validity = false;
			printException(getMsg("checker.level.no_lib_return", ALevelDefinition.SIGNATURE_LIB_RETURN));
		}
		return validity;
	}

	private boolean checkValidityOfImportantInstanceMethods() {
		if (implementation.getLevels() == null || implementation.getLevels().length < 1) {
			printException(getMsg("checker.level.no_levels", ALevelDefinition.SIGNATURE_LEVELS));
			return false;
		} else {
			levels.addAll(Arrays.asList(implementation.getLevels()));
		}
		ILevel glb = implementation.getGreatesLowerBoundLevel();
		if (glb == null || !levels.contains(glb)) {
			printException(getMsg("checker.level.no_glb", ALevelDefinition.SIGNATURE_GLB));
			return false;
		}
		ILevel lub = implementation.getLeastUpperBoundLevel();
		if (lub == null || !levels.contains(lub)) {
			printException(getMsg("checker.level.no_lub", ALevelDefinition.SIGNATURE_LUB));
			return false;
		}
		return true;
	}

	private SootMethod generatedTestMethod(int parameterCount) {
		List<RefType> parameterTypes = new ArrayList<RefType>();
		for (int i = 0; i < parameterCount; i++) {
			parameterTypes.add(RefType.v("int"));
		}
		SootClass sootClass = generateTestClass();
		SootMethod sootMethod = new SootMethod("testMethod", parameterTypes, RefType.v("int"));
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

	private Class<?> getImplementationClass() {
		return implementation.getClass();
	}

	private List<String> getImplementationMethodNames() {
		List<String> names = new ArrayList<String>();
		for (Method method : getImplementationMethods()) {
			names.add(method.getName());
		}
		return names;
	}

	private Method[] getImplementationMethods() {
		return getImplementationClass().getDeclaredMethods();
	}

	private List<String> getLevelNames() {
		List<String> names = new ArrayList<String>();
		for (ILevel level : levels) {
			names.add(level.getName());
		}
		return names;
	}

	protected final void addIllegalLevelNames(ILevel illegalLevelName) {
		this.illegalLevelNames.add(illegalLevelName);
	}

	protected final void addInvalidIdFunction(ILevel invalidIdFunction) {
		this.invalidIdFunctions.add(invalidIdFunction);
	}

	protected final void addInvalidIdFunctionAnnotation(ILevel invalidIdFunctionAnnotation) {
		this.invalidIdFunctionAnnotation.add(invalidIdFunctionAnnotation);
	}

	protected final void addUnavailableIdFunction(ILevel unavailableIdFunction) {
		this.unavailableIdFunctions.add(unavailableIdFunction);
	}

	protected final void addUnavailableIdFunctionAnnotation(ILevel unavailableIdFunctionAnnotation) {
		this.unavailableIdFunctionAnnotation.add(unavailableIdFunctionAnnotation);
	}

	protected abstract boolean checkAdditionalValidityOfImplementation();

	protected final List<ILevel> getIllegalLevelNames() {
		return this.illegalLevelNames;
	}

	protected final List<ILevel> getInvalidIdFunctionAnnotation() {
		return this.invalidIdFunctionAnnotation;
	}

	protected final List<ILevel> getInvalidIdFunctions() {
		return this.invalidIdFunctions;
	}

	protected final List<ILevel> getUnavailableIdFunctionAnnotations() {
		return this.unavailableIdFunctionAnnotation;
	}

	protected final List<ILevel> getUnavailableIdFunctions() {
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
				logger.warning(Configuration.DEF_CLASS_NAME, 0, msg);
			} else {
				System.out.println(msg);
			}
		}
	}

}