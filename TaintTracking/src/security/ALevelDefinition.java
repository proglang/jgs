package security;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import annotation.IAnnotationDAO;
import constraints.ConstraintParameterRef;
import constraints.ConstraintReturnRef;
import constraints.LEQConstraint;

public abstract class ALevelDefinition implements ILevelDefinition {

	public final static String SIGNATURE_DEFAULT_VAR = "public ILevel getDefaultVariableLevel()";
	public final static String SIGNATURE_GLB = "public ILevel getLeastUpperBoundLevel()";
	public final static String SIGNATURE_LEVELS = "public ILevel[] getLevels()";
	public final static String SIGNATURE_LIB_CLASS = "public List<ILevel> getLibraryClassWriteEffects(SootClass sootClass)";
	public final static String SIGNATURE_LIB_FIELD = "public ILevel getLibraryFieldLevel(SootField sootField)";
	public final static String SIGNATURE_LIB_METHOD = "public List<ILevel> getLibraryMethodWriteEffects(SootMethod sootMethod)";
	public final static String SIGNATURE_LIB_PARAM = "public List<ILevel> getLibraryParameterLevel(SootMethod sootMethod)";
	public final static String SIGNATURE_LIB_RETURN = "public ILevel getLibraryReturnLevel(SootMethod sootMethod, List<ILevel> levels)";
	public final static String SIGNATURE_LUB = "public ILevel getLeastUpperBoundLevel()";

	private final Class<? extends Annotation> annotationClassConstraints;
	private final Class<? extends Annotation> annotationClassEffects;
	private final Class<? extends Annotation> annotationClassFieldLevel;
	private final Class<? extends Annotation> annotationClassParameterLevel;
	private final Class<? extends Annotation> annotationClassReturnLevel;

	public ALevelDefinition(Class<? extends Annotation> annotationClassFieldLevel, Class<? extends Annotation> annotationClassParameterLevel,
			Class<? extends Annotation> annotationClassReturnLevel, Class<? extends Annotation> annotationClassEffects,
			Class<? extends Annotation> annotationClassConstraints) {
		this.annotationClassFieldLevel = annotationClassFieldLevel;
		this.annotationClassParameterLevel = annotationClassParameterLevel;
		this.annotationClassReturnLevel = annotationClassReturnLevel;
		this.annotationClassEffects = annotationClassEffects;
		this.annotationClassConstraints = annotationClassConstraints;
	}

	public abstract int compare(ILevel level1, ILevel level2);

	public abstract Set<LEQConstraint> extractConstraints(IAnnotationDAO dao, String signature);

	public abstract List<ILevel> extractEffects(IAnnotationDAO dao);

	public abstract ILevel extractFieldLevel(IAnnotationDAO dao);

	public abstract List<ILevel> extractParameterLevels(IAnnotationDAO dao);

	public abstract ILevel extractReturnLevel(IAnnotationDAO dao);

	public final Class<? extends Annotation> getAnnotationClassConstraints() {
		return annotationClassConstraints;
	}

	public final Class<? extends Annotation> getAnnotationClassEffects() {
		return annotationClassEffects;
	}

	public final Class<? extends Annotation> getAnnotationClassFieldLevel() {
		return annotationClassFieldLevel;
	}

	public final Class<? extends Annotation> getAnnotationClassParameterLevel() {
		return annotationClassParameterLevel;
	}

	public final Class<? extends Annotation> getAnnotationClassReturnLevel() {
		return annotationClassReturnLevel;
	}

	public ILevel getDefaultVariableLevel() {
		return this.getGreatesLowerBoundLevel();
	}

	public abstract ILevel getGreatesLowerBoundLevel();

	public ILevel getGreatestLowerBoundLevel(ILevel level1, ILevel level2) {
		return this.compare(level1, level2) < 0 ? level1 : level2;
	}

	public abstract ILevel getLeastUpperBoundLevel();

	public ILevel getLeastUpperBoundLevel(ILevel level1, ILevel level2) {
		return this.compare(level1, level2) > 0 ? level1 : level2;
	}

	public abstract ILevel[] getLevels();

	public List<ILevel> getLibraryClassWriteEffects(String className) {
		
		return new ArrayList<ILevel>();
	}

	public Set<LEQConstraint> getLibraryConstraints(String methodName, List<String> parameterTypes, String returnType,
			String declaringClassName, String signature) {
		Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();
		ConstraintReturnRef returnRef = new ConstraintReturnRef(signature);
		if (!returnType.equals("void")) {
			for (int i = 0; i < parameterTypes.size(); i++) {
				ConstraintParameterRef paramRef = new ConstraintParameterRef(i, signature);
				constraints.add(new LEQConstraint(paramRef, returnRef));
			}
			if (parameterTypes.size() == 0) constraints.add(new LEQConstraint(getGreatesLowerBoundLevel(), returnRef));
		}
		return constraints;
	}

	public Set<LEQConstraint> getLibraryConstraints(String className) {
		Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();
		return constraints;
	}

	public ILevel getLibraryFieldLevel(String fieldName, String declaringClassName, String signature) {
		return this.getGreatesLowerBoundLevel();
	}

	public List<ILevel> getLibraryMethodWriteEffects(String methodName, List<String> parameterTypes, String declaringClassName,
			String signature) {
		return new ArrayList<ILevel>();
	}

	public List<ILevel> getLibraryParameterLevel(String methodName, List<String> parameterTypes, String declaringClassName, String signature) {
		List<ILevel> param = new ArrayList<ILevel>();
		for (int i = 0; i < parameterTypes.size(); i++) {
			param.add(getLeastUpperBoundLevel());
		}
		return param;
	}

	public ILevel getLibraryReturnLevel(String methodName, List<String> parameterTypes, String declaringClassName, String signature,
			List<ILevel> levels) {
		if (levels.size() == 0) {
			return getGreatesLowerBoundLevel();
		} else {
			ILevel result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = getLeastUpperBoundLevel(result, levels.get(i));
			}
			return result;
		}
	}

}
