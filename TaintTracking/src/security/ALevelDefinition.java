package security;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import annotation.IAnnotationDAO;
import constraints.Constraints;

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

	public abstract Constraints extractConstraints(IAnnotationDAO dao);

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

	public List<ILevel> getLibraryClassWriteEffects(SootClass sootClass) {
		return new ArrayList<ILevel>();
	}

	public Constraints getLibraryConstraints(SootMethod sootMethod) {
		return new Constraints();
	}

	public ILevel getLibraryFieldLevel(SootField sootField) {
		return this.getGreatesLowerBoundLevel();
	}

	public List<ILevel> getLibraryMethodWriteEffects(SootMethod sootMethod) {
		return new ArrayList<ILevel>();
	}

	public List<ILevel> getLibraryParameterLevel(SootMethod sootMethod) {
		List<ILevel> param = new ArrayList<ILevel>();
		for (int i = 0; i < sootMethod.getParameterCount(); i++) {
			param.add(getLeastUpperBoundLevel());
		}
		return param;
	}

	public ILevel getLibraryReturnLevel(SootMethod sootMethod, List<ILevel> levels) {
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
