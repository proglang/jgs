package security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import annotation.SootAnnotationDAO;

import resource.Configuration;
import security.ILevel;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Host;
import soot.tagkit.VisibilityAnnotationTag;
import utils.AnalysisUtils;

import exception.SootException.ExtractionException;
import exception.SootException.InvalidLevelException;

public abstract class ALevelMediator implements ILevelMediator {

	private static final String EXCEPTION_EXTRACTION_CLASS = "Incorrect extraction of the write effects for class '%s'.";
	private static final String EXCEPTION_EXTRACTION_FIELD = "Incorrect extraction of the field level for field '%s'.";
	private static final String EXCEPTION_EXTRACTION_METHOD = "Incorrect extraction of the write effects for method '%s'.";
	private static final String EXCEPTION_EXTRACTION_PARAM = "Incorrect extraction of the parameter levels for method '%s'.";
	private static final String EXCEPTION_EXTRACTION_RETURN = "Incorrect extraction of the return level for method '%s'.";
	private static final String EXCEPTION_GLB_EMPTY_LIST = "Invalid attempt to get the greatest lower bound of an empty level list.";
	private static final String EXCEPTION_LUB_EMPTY_LIST = "Invalid attempt to get the least upper bound of an empty level list.";
	protected final ILevelDefinition definition;

	public ALevelMediator(ILevelDefinition definition) {
		this.definition = definition;
	}

	public boolean checkLevelsValidity(List<ILevel> levels) {
		for (ILevel level : levels) {
			if (!checkLevelValidity(level)) return false;
		}
		return true;
	}

	public boolean checkLevelValidity(ILevel level) {
		for (ILevel available : this.definition.getLevels()) {
			if (available.equals(level)) return true;
		}
		return false;
	}

	public abstract boolean checkParameterLevelsValidity(List<ILevel> levels);

	public final List<ILevel> extractClassEffects(SootClass sootClass) throws ExtractionException {
		try {
			VisibilityAnnotationTag vt = AnalysisUtils.extractVisibilityAnnotationTag(sootClass);
			AnnotationTag at = AnalysisUtils.extractAnnotationTagWithType(vt,
					AnalysisUtils.getJNISignature(definition.getAnnotationClassEffects()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassEffects(), at);
			return definition.extractEffects(dao);
		} catch (Exception e) {
			throw new ExtractionException(String.format(EXCEPTION_EXTRACTION_CLASS,
					AnalysisUtils.generateClassSignature(sootClass, Configuration.CLASS_SIGNATURE_PRINT_PACKAGE)));
		}

	}

	public final ILevel extractFieldSecurityLevel(SootField sootField) throws ExtractionException {
		try {
			VisibilityAnnotationTag vt = AnalysisUtils.extractVisibilityAnnotationTag(sootField);
			AnnotationTag at = AnalysisUtils.extractAnnotationTagWithType(vt,
					AnalysisUtils.getJNISignature(definition.getAnnotationClassFieldLevel()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassFieldLevel(), at);
			return definition.extractFieldLevel(dao);
		} catch (Exception e) {
			throw new ExtractionException(String.format(EXCEPTION_EXTRACTION_FIELD, AnalysisUtils.generateFieldSignature(sootField,
					Configuration.FIELD_SIGNATURE_PRINT_PACKAGE, Configuration.FIELD_SIGNATURE_PRINT_TYPE,
					Configuration.FIELD_SIGNATURE_PRINT_VISIBILITY)));
		}
	}

	public final List<ILevel> extractMethodEffects(SootMethod sootMethod) throws ExtractionException {
		try {
			VisibilityAnnotationTag vt = AnalysisUtils.extractVisibilityAnnotationTag(sootMethod);
			AnnotationTag at = AnalysisUtils.extractAnnotationTagWithType(vt,
					AnalysisUtils.getJNISignature(definition.getAnnotationClassEffects()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassEffects(), at);
			return definition.extractEffects(dao);
		} catch (Exception e) {
			throw new ExtractionException(String.format(EXCEPTION_EXTRACTION_METHOD, AnalysisUtils.generateMethodSignature(sootMethod,
					Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
					Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
		}

	}

	public final List<ILevel> extractParameterSecurityLevels(SootMethod sootMethod) throws ExtractionException {
		try {
			VisibilityAnnotationTag vt = AnalysisUtils.extractVisibilityAnnotationTag(sootMethod);
			AnnotationTag at = AnalysisUtils.extractAnnotationTagWithType(vt,
					AnalysisUtils.getJNISignature(definition.getAnnotationClassParameterLevel()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassParameterLevel(), at);
			return definition.extractParameterLevels(dao);
		} catch (Exception e) {
			throw new ExtractionException(String.format(EXCEPTION_EXTRACTION_PARAM, AnalysisUtils.generateMethodSignature(sootMethod,
					Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
					Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
		}
	}

	public final ILevel extractReturnSecurityLevel(SootMethod sootMethod) throws ExtractionException {
		try {
			VisibilityAnnotationTag vt = AnalysisUtils.extractVisibilityAnnotationTag(sootMethod);
			AnnotationTag at = AnalysisUtils.extractAnnotationTagWithType(vt,
					AnalysisUtils.getJNISignature(definition.getAnnotationClassReturnLevel()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassReturnLevel(), at);
			return definition.extractReturnLevel(dao);
		} catch (Exception e) {
			throw new ExtractionException(String.format(EXCEPTION_EXTRACTION_RETURN, AnalysisUtils.generateMethodSignature(sootMethod,
					Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
					Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
		}
	}

	public List<ILevel> getAvailableLevels() {
		return Arrays.asList(this.definition.getLevels());
	}

	public ILevel getDefaultVariableSecurityLevel() {
		return this.definition.getDefaultVariableLevel();
	}

	public ILevel getGreatestLowerBoundLevel() {
		return this.definition.getGreatesLowerBoundLevel();
	}

	public ILevel getGreatestLowerBoundLevelOf(ILevel level1, ILevel level2) {
		return this.definition.getGreatestLowerBoundLevel(level1, level2);
	}

	public ILevel getGreatestLowerBoundLevelOf(List<ILevel> levels) throws InvalidLevelException {
		if (levels.size() > 0) {
			ILevel result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = this.definition.getGreatestLowerBoundLevel(result, levels.get(i));
			}
			return result;
		}
		// return getLeastUpperBoundLevel();
		throw new InvalidLevelException(EXCEPTION_GLB_EMPTY_LIST);
	}

	public List<ILevel> getInvalidLevels(List<ILevel> levels) {
		List<ILevel> invalids = new ArrayList<ILevel>();
		for (ILevel level : levels) {
			if (!checkLevelValidity(level)) invalids.add(level);
		}
		return invalids;
	}

	public abstract List<ILevel> getInvalidParameterLevels(List<ILevel> levels);

	public ILevel getLeastUpperBoundLevel() {
		return this.definition.getLeastUpperBoundLevel();
	}

	public ILevel getLeastUpperBoundLevelOf(ILevel level1, ILevel level2) {
		return this.definition.getLeastUpperBoundLevel(level1, level2);
	}

	public ILevel getLeastUpperBoundLevelOf(List<ILevel> levels) throws InvalidLevelException {
		if (levels.size() > 0) {
			ILevel result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = this.definition.getLeastUpperBoundLevel(result, levels.get(i));
			}
			return result;
		}
		// return getGreatestLowerBoundLevel();
		throw new InvalidLevelException(EXCEPTION_LUB_EMPTY_LIST);
	}

	public List<ILevel> getLibraryClassWriteEffects(SootClass sootClass) {
		return this.definition.getLibraryClassWriteEffects(sootClass);
	}

	public ILevel getLibraryFieldSecurityLevel(SootField sootField) {
		return this.definition.getLibraryFieldLevel(sootField);
	}

	public List<ILevel> getLibraryParameterSecurityLevel(SootMethod sootMethod) {
		return this.definition.getLibraryParameterLevel(sootMethod);
	}

	public ILevel getLibraryReturnSecurityLevel(SootMethod sootMethod, List<ILevel> levels) {
		return this.definition.getLibraryReturnLevel(sootMethod, levels);
	}

	public List<ILevel> getLibraryWriteEffects(SootMethod sootMethod) {
		return this.definition.getLibraryMethodWriteEffects(sootMethod);
	}

	public final boolean hasClassWriteEffectAnnotation(SootClass sootClass) {
		return hasAnnotationWithType(sootClass, AnalysisUtils.getJNISignature(this.definition.getAnnotationClassEffects()));
	}

	public final boolean hasFieldSecurityAnnotation(SootField sootField) {
		return hasAnnotationWithType(sootField, AnalysisUtils.getJNISignature(this.definition.getAnnotationClassFieldLevel()));
	}

	public final boolean hasMethodWriteEffectAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod, AnalysisUtils.getJNISignature(this.definition.getAnnotationClassEffects()));
	}

	public final boolean hasParameterSecurityAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod, AnalysisUtils.getJNISignature(this.definition.getAnnotationClassParameterLevel()));
	}

	public final boolean hasReturnSecurityAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod, AnalysisUtils.getJNISignature(this.definition.getAnnotationClassReturnLevel()));
	}

	public boolean isEquals(ILevel level1, ILevel level2) {
		return level1.equals(level2);
	}

	public boolean isStronger(ILevel level1, ILevel level2) {
		return this.definition.compare(level1, level2) > 0;
	}

	public boolean isStrongerOrEquals(ILevel level1, ILevel level2) throws InvalidLevelException {
		return this.definition.compare(level1, level2) >= 0;
	}

	public boolean isWeaker(ILevel level1, ILevel level2) {
		return this.definition.compare(level1, level2) < 0;
	}

	public boolean isWeakerOrEquals(ILevel level1, ILevel level2) {
		return this.definition.compare(level1, level2) <= 0;
	}

	private final boolean hasAnnotationWithType(Host host, String type) {
		try {
			if (AnalysisUtils.hasVisibilityAnnnotationTag(host)) {
				VisibilityAnnotationTag tag = AnalysisUtils.extractVisibilityAnnotationTag(host);
				if (AnalysisUtils.hasAnnotationOfType(tag, type)) return true;
			}
			return false;
		} catch (ExtractionException e) {
			return false;
		}
	}

}