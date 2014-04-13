package security;

import static resource.Messages.getMsg;
import static security.ALevelDefinition.SIGNATURE_DEFAULT_VAR;
import static security.ALevelDefinition.SIGNATURE_GLB;
import static security.ALevelDefinition.SIGNATURE_LEVELS;
import static security.ALevelDefinition.SIGNATURE_LIB_CLASS;
import static security.ALevelDefinition.SIGNATURE_LIB_FIELD;
import static security.ALevelDefinition.SIGNATURE_LIB_METHOD;
import static security.ALevelDefinition.SIGNATURE_LIB_PARAM;
import static security.ALevelDefinition.SIGNATURE_LIB_RETURN;
import static security.ALevelDefinition.SIGNATURE_LUB;

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

import logging.AnalysisLog;
import resource.Configuration;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.util.Chain;
import utils.AnalysisUtils;
import annotation.JavaAnnotationDAO;
import constraints.ConstraintParameterRef;
import constraints.ConstraintReturnRef;
import constraints.Constraints;
import constraints.LEQConstraint;
import exception.AnnotationElementNotFoundException;
import exception.AnnotationInvalidConstraintsException;
import exception.DefinitionInvalidException;

public abstract class ALevelDefinitionChecker implements ILevelDefinitionChecker {

	public static final String ID_SIGNATURE_PATTERN = "public static <T> T %s(T)";
	private static final String SIGNATURE_ID_INVALID = "public <T> T %s(T)";
	private final ILevelDefinition implementation;
	private final List<ILevel> levels = new ArrayList<ILevel>();
	private final AnalysisLog logger;
	private final boolean logging;

	public ALevelDefinitionChecker(ILevelDefinition implementation) {
		this(implementation, null, true);
	}

	protected ALevelDefinitionChecker(ILevelDefinition implementation, AnalysisLog logger, boolean logging) {
		this.logging = logging;
		this.logger = logging ? logger : null;
		this.implementation = implementation;
		checkValidity();
	}

	public final List<ILevel> getLevels() {
		return levels;
	}

	private void checkAnnotationsOfAnnotationClass(Class<? extends Annotation> annotationClass, ElementType[] elementTypes) {
		if (annotationClass.isAnnotationPresent(Retention.class)) {
			Retention annotation = annotationClass.getAnnotation(Retention.class);
			if (!annotation.value().equals(RetentionPolicy.RUNTIME)) {
				throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.visibility_invalid", annotationClass.getName(),
						RetentionPolicy.RUNTIME.name()));
			}
		} else {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.missing_annotation", annotationClass.getName(),
					Retention.class.getName()));
		}
		if (annotationClass.isAnnotationPresent(Target.class)) {
			Target annotation = annotationClass.getAnnotation(Target.class);
			List<ElementType> present = Arrays.asList(annotation.value());
			List<ElementType> required = Arrays.asList(elementTypes);
			for (ElementType elementType : required) {
				if (!present.contains(elementType)) {
					throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.missing_element_type", annotationClass.getName(),
							elementType.name()));
				}
			}
			for (ElementType elementType : present) {
				if (!required.contains(elementType)) {
					throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.invalid_element_type", annotationClass.getName(),
							elementType.name()));
				}
			}
		} else {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.missing_annotation", annotationClass.getName(),
					Target.class.getName()));
		}
	}

	private void checkConventionsOfAnnotationClass(Class<? extends Annotation> annotationClass, ElementType[] elementTypes) {
		if (annotationClass.getDeclaringClass() == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.invalid_declaration", annotationClass.getName()));
		}
		if (!annotationClass.getPackage().getName().equals(Configuration.DEF_PACKAGE_NAME)) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.invalid_package", annotationClass.getName(),
					Configuration.DEF_PACKAGE_NAME));
		}
		if (!annotationClass.isAnnotation()) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.invalid_class", annotationClass.getName()));
		}
		checkAnnotationsOfAnnotationClass(annotationClass, elementTypes);
	}

	private void checkConventionsOfAnnotationClasses() {
		if (implementation.getAnnotationClassFieldLevel() == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.no_class", getMsg("other.field_level")));
		}
		if (implementation.getAnnotationClassParameterLevel() == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.no_class", getMsg("other.parameter_levels")));
		}
		if (implementation.getAnnotationClassReturnLevel() == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.no_class", getMsg("other.return_level")));
		}
		if (implementation.getAnnotationClassEffects() == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.no_class", getMsg("other.write_effects")));
		}
		if (implementation.getAnnotationClassConstraints() == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.annotation.no_class", getMsg("other.constraints")));
		}
		Map<Class<? extends Annotation>, ElementType[]> annotations = new HashMap<Class<? extends Annotation>, ElementType[]>();
		annotations.put(implementation.getAnnotationClassFieldLevel(), new ElementType[] { ElementType.FIELD });
		annotations.put(implementation.getAnnotationClassParameterLevel(), new ElementType[] { ElementType.METHOD, ElementType.CONSTRUCTOR });
		annotations.put(implementation.getAnnotationClassReturnLevel(), new ElementType[] { ElementType.METHOD });
		annotations.put(implementation.getAnnotationClassEffects(), new ElementType[] { ElementType.METHOD, ElementType.CONSTRUCTOR,
				ElementType.TYPE });
		annotations.put(implementation.getAnnotationClassConstraints(), new ElementType[] { ElementType.METHOD, ElementType.CONSTRUCTOR });
		for (Class<? extends Annotation> annotation : annotations.keySet()) {
			checkConventionsOfAnnotationClass(annotation, annotations.get(annotation));
		}
	}

	private void checkCorrectnesOfConventions(ILevel level) {
		checkCorrectnessOfLevelName(level);
		checkCorrectnessOfLevelFunction(level);
	}

	private void checkCorrectnessOfConventionsForAllLevels() {
		for (ILevel level : getLevels()) {
			checkCorrectnesOfConventions(level);
		}
	}

	private void checkCorrectnessOfConventionsForClass() {
		String packageName = getImplementationClass().getPackage().getName();
		String className = getImplementationClass().getSimpleName();
		if (!packageName.equals(Configuration.DEF_PACKAGE_NAME)) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.package.invalid_name", Configuration.DEF_PACKAGE_NAME));
		}
		if (!className.equals(Configuration.DEF_CLASS_NAME)) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.class.invalid_name", Configuration.DEF_PACKAGE_NAME));
		}
	}

	private void checkCorrectnessOfLevelFunction(ILevel level) {
		String levelFunctionName = AnalysisUtils.generateLevelFunctionName(level);
		String signatureLevelFunction = String.format(ID_SIGNATURE_PATTERN, levelFunctionName);
		if (!getImplementationMethodNames().contains(levelFunctionName)) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.none", signatureLevelFunction));
		} else {
			try {
				Method method = getImplementationClass().getMethod(levelFunctionName, Object.class);
				if (!Modifier.isStatic(method.getModifiers())) {
					throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.not_static",
							String.format(SIGNATURE_ID_INVALID, levelFunctionName)));
				}
				boolean existsReturnAnnotation = false;
				boolean existsParameterAnnotation = false;
				boolean existsConstraintsAnnotation = false;
				for (Annotation annotation : method.getAnnotations()) {
					if (annotation.annotationType().equals(implementation.getAnnotationClassReturnLevel())) {
						try {
							ILevel returnLevel = implementation.extractReturnLevel(new JavaAnnotationDAO(annotation));
							existsReturnAnnotation = true;
							if (!returnLevel.equals(level)) {
								throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.invalid_return_level", signatureLevelFunction));
							}
						} catch (AnnotationElementNotFoundException e) {
							throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.error_level_conversion",
									getMsg("other.return_level"), signatureLevelFunction), e);
						}
					} else if (annotation.annotationType().equals(implementation.getAnnotationClassParameterLevel())) {
						try {
							existsParameterAnnotation = true;
							List<ILevel> parameterLevels = implementation.extractParameterLevels(new JavaAnnotationDAO(annotation));
							if (parameterLevels == null || parameterLevels.size() != 1 || !parameterLevels.get(0).equals(level)) {
								throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.invalid_parameter_levels",
										signatureLevelFunction));
							}
						} catch (AnnotationElementNotFoundException e) {
							throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.error_level_conversion",
									getMsg("other.parameter_levels"), signatureLevelFunction), e);
						}
					} else if (annotation.annotationType().equals(implementation.getAnnotationClassConstraints())) {
						try {
							existsConstraintsAnnotation = true;
							Constraints constraints = implementation.extractConstraints(new JavaAnnotationDAO(annotation));
							if (constraints.contains(new LEQConstraint(new ConstraintParameterRef(0), level))
									&& constraints.contains(new LEQConstraint(new ConstraintReturnRef(), level))
									&& constraints.contains(new LEQConstraint(level, new ConstraintReturnRef())) && constraints.size() == 3) {
								throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.invalid_constraints", signatureLevelFunction));
							}
						} catch (AnnotationElementNotFoundException | AnnotationInvalidConstraintsException e) {
							throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.error_constraint_conversion",
									signatureLevelFunction), e);
						}
					}
				}
				if (!existsReturnAnnotation || !existsParameterAnnotation || !existsConstraintsAnnotation) {
					throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.missing_annotation", signatureLevelFunction));
				}
			} catch (NoSuchMethodException | SecurityException e) {
				throw new DefinitionInvalidException(getMsg("exception.def_class.level_func.no_access", signatureLevelFunction), e);
			}
		}
	}

	private void checkCorrectnessOfLevelName(ILevel level) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(level.getName());
		boolean match = m.find();
		if (match) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.level.invalid_char", level.getName()));
		}
	}

	private void checkNormalValiditiyOfImplemenation() {
		checkCorrectnessOfConventionsForClass();
		checkConventionsOfAnnotationClasses();
		checkValidityOfImportantInstanceMethods();
		checkCorrectnessOfConventionsForAllLevels();
		checkValidityOfAdditionalInstanceMethods();
		checkPossibleMistakes();
	}

	private void checkPossibleMistakes() {
		List<String> levelNames = getLevelNames();
		for (Method method : getImplementationMethods()) {
			String methodName = method.getName();
			if (methodName.startsWith(Configuration.PREFIX_LEVEL_FUNCTION)) {
				String possibleLevelName = methodName.substring(Configuration.PREFIX_LEVEL_FUNCTION.length(), methodName.length()).toLowerCase();
				if (!levelNames.contains(possibleLevelName)) {
					Class<?>[] parameters = method.getParameterTypes();
					Class<?> returnType = method.getReturnType();
					boolean isStatic = Modifier.isStatic(method.getModifiers());
					boolean isPublic = Modifier.isPublic(method.getModifiers());
					if (parameters.length == 1 && parameters[0].equals(Object.class) && returnType.equals(Object.class) && isStatic && isPublic) {
						printWarning(getMsg("warning.def_class.level.no_level", String.format(ID_SIGNATURE_PATTERN, methodName)));
					}
				}
			}
		}
	}

	private void checkValidity() {
		checkNormalValiditiyOfImplemenation();
		checkAdditionalValidityOfImplementation();
	}

	private void checkValidityOfAdditionalInstanceMethods() {
		if (implementation.getDefaultVariableLevel() == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.methods.no_default_variable", SIGNATURE_DEFAULT_VAR));
		}
		SootClass sootClass = generateTestClass();
		SootMethod sootMethod = generatedTestMethod(sootClass);
		List<ILevel> testLevels = Arrays.asList(new ILevel[] { getLevels().get(0), getLevels().get(0) });
		SootField sootField = generateTestField(sootClass);
		if (implementation.getLibraryClassWriteEffects(sootClass) == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.methods.no_lib_class_effects", SIGNATURE_LIB_CLASS));
		}
		if (implementation.getLibraryMethodWriteEffects(sootMethod) == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.methods.no_lib_method_effects", SIGNATURE_LIB_METHOD));
		}
		if (implementation.getLibraryFieldLevel(sootField) == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.methods.no_lib_field", SIGNATURE_LIB_FIELD));
		}
		if (implementation.getLibraryParameterLevel(sootMethod) == null || implementation.getLibraryParameterLevel(sootMethod).size() != 2) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.methods.no_lib_parameter", SIGNATURE_LIB_PARAM));
		}
		if (implementation.getLibraryReturnLevel(sootMethod, testLevels) == null) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.methods.no_lib_return", SIGNATURE_LIB_RETURN));
		}
	}

	private void checkValidityOfImportantInstanceMethods() {
		if (implementation.getLevels() == null || implementation.getLevels().length < 1) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.methods.no_levels", SIGNATURE_LEVELS));
		} else {
			levels.addAll(Arrays.asList(implementation.getLevels()));
		}
		ILevel glb = implementation.getGreatesLowerBoundLevel();
		if (glb == null || !levels.contains(glb)) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.methods.no_glb", SIGNATURE_GLB));
		}
		ILevel lub = implementation.getLeastUpperBoundLevel();
		if (lub == null || !levels.contains(lub)) {
			throw new DefinitionInvalidException(getMsg("exception.def_class.methods.no_lub", SIGNATURE_LUB));
		}
	}

	private SootMethod generatedTestMethod(SootClass sootClass) {
		List<RefType> parameterTypes = new ArrayList<RefType>();
		for (int i = 0; i < 2; i++) {
			parameterTypes.add(RefType.v("int"));
		}
		SootMethod sootMethod = new SootMethod("compare", parameterTypes, RefType.v("int"));
		sootMethod.setDeclaringClass(sootClass);
		sootClass.addMethod(sootMethod);
		JimpleBody body = Jimple.v().newBody(sootMethod);
		sootMethod.setActiveBody(body);
		Chain<Unit> units = body.getUnits();
		units.add(Jimple.v().newReturnStmt(IntConstant.v(42)));
		return sootMethod;
	}

	private SootClass generateTestClass() {
		return new SootClass("java.lang.Integer");
	}

	private SootField generateTestField(SootClass sootClass) {
		SootField sootField = new SootField("MAX_VALUE", RefType.v("int"));
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

	protected abstract void checkAdditionalValidityOfImplementation();

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