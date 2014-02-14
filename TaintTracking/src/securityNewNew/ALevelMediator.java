package securityNewNew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.SootAnnotationDAO;
import resource.Configuration;
import securityNewNew.ILevel;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Host;
import soot.tagkit.VisibilityAnnotationTag;
import utils.SootUtils;

import exception.SootException.ExtractionException;
import exception.SootException.InvalidLevelException;

public abstract class  ALevelMediator implements
		ILevelMediator {

	protected final ILevelDefinition definition;

	public ALevelMediator(ILevelDefinition definition) {
		this.definition = definition;
	}

	public List<ILevel> getAvailableLevels() {
		return Arrays.asList(this.definition.getLevels());
	}

	public boolean checkLevelValidity(ILevel level) {
		for (ILevel available : this.definition.getLevels()) {
			if (available.equals(level))
				return true;
		}
		return false;
	}

	public boolean checkLevelsValidity(List<ILevel> levels) {
		for (ILevel level : levels) {
			if (!checkLevelValidity(level))
				return false;
		}
		return true;
	}

	public List<ILevel> getInvalidLevels(List<ILevel> levels) {
		List<ILevel> invalids = new ArrayList<ILevel>();
		for (ILevel level : levels) {
			if (!checkLevelValidity(level))
				invalids.add(level);
		}
		return invalids;
	}

	// TODO
	public abstract boolean checkParameterLevelsValidity(List<ILevel> levels);

	// TODO
	public abstract List<ILevel> getInvalidParameterLevels(List<ILevel> levels);

	public ILevel getLeastUpperBoundLevel() {
		return this.definition.getLeastUpperBoundLevel();
	}

	public ILevel getLeastUpperBoundLevelOf(List<ILevel> levels) throws InvalidLevelException {
		if (levels.size() == 0) {
			// DOC
			throw new InvalidLevelException(
					"can not return the strongest level of an empty list.");
		} else {
			ILevel result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = this.definition.getLeastUpperBoundLevel(result,
						levels.get(i));
			}
			return result;
		}
	}

	public ILevel getLeastUpperBoundLevelOf(ILevel level1, ILevel level2) {
		return this.definition.getLeastUpperBoundLevel(level1, level2);
	}

	public ILevel getGreatestLowerBoundLevel() {
		return this.definition.getGreatesLowerBoundLevel();
	}
	
	public ILevel getGreatestLowerBoundLevelOf(List<ILevel> levels)
			throws InvalidLevelException {
		if (levels.size() == 0) {
			// DOC
			throw new InvalidLevelException(
					"can not return the weakest level of an empty list.");
		} else {
			ILevel result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = this.definition.getGreatestLowerBoundLevel(result, levels.get(i));
			}
			return result;
		}
	}

	public ILevel getGreatestLowerBoundLevelOf(ILevel level1, ILevel level2) {
		return this.definition.getGreatestLowerBoundLevel(level1, level2);
	}

	public boolean isStronger(ILevel level1, ILevel level2) {
		return this.definition.compare(level1, level2) > 0;
	}

	public boolean isStrongerOrEquals(ILevel level1, ILevel level2)
			throws InvalidLevelException {
		return this.definition.compare(level1, level2) >= 0;
	}

	public boolean isEquals(ILevel level1, ILevel level2) {
		return level1.equals(level2);
	}

	public boolean isWeakerOrEquals(ILevel level1, ILevel level2) {
		return this.definition.compare(level1, level2) <= 0;
	}

	public boolean isWeaker(ILevel level1, ILevel level2) {
		return this.definition.compare(level1, level2) < 0;
	}

	public boolean isIdFunction(SootMethod sootMethod) {
		if (isMethodOfDefinitionClass(sootMethod)
				&& sootMethod.getParameterCount() == 1 
				&& sootMethod.isStatic()
				&& sootMethod.isPublic()) {
			for (ILevel level : this.definition.getLevels()) {
				if (sootMethod.getName().equals(
						level.getName() + Configuration.SUFFIX_METHOD_ID))
					return true;
			}
		}
		return false;
	}

	public boolean isMethodOfDefinitionClass(SootMethod sootMethod) {
		return sootMethod.getDeclaringClass().getName()
				.equals(Configuration.DEF_PATH_JAVA);
	}

	public final boolean hasReturnSecurityAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod,
				SootUtils.getJNISignature(this.definition.getAnnotationClassReturnLevel()));
	}

	public final ILevel extractReturnSecurityLevel(SootMethod sootMethod)
			throws ExtractionException {
		VisibilityAnnotationTag vt = SootUtils.extractVisibilityAnnotationTag(sootMethod);
		AnnotationTag at = SootUtils.extractAnnotationTagWithType(vt, SootUtils.getJNISignature(definition.getAnnotationClassReturnLevel()));
		SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassReturnLevel(), at);
		return definition.extractReturnLevel(dao);
	}

	public ILevel getLibraryReturnSecurityLevel(SootMethod sootMethod, List<ILevel> levels) {
		return this.definition.getLibraryReturnLevel(sootMethod, levels);
	}

	public final boolean hasParameterSecurityAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod,
				SootUtils.getJNISignature(this.definition.getAnnotationClassParameterLevel()));
	}

	public final List<ILevel> extractParameterSecurityLevels(SootMethod sootMethod)
			throws ExtractionException {
		VisibilityAnnotationTag vt = SootUtils.extractVisibilityAnnotationTag(sootMethod);
		AnnotationTag at = SootUtils.extractAnnotationTagWithType(vt, SootUtils.getJNISignature(definition.getAnnotationClassParameterLevel()));
		SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassParameterLevel(), at);
		return definition.extractParameterLevels(dao);
	}

	public List<ILevel> getLibraryParameterSecurityLevel(SootMethod sootMethod) {
		return this.definition.getLibraryParameterLevel(sootMethod);
	}

	public final boolean hasMethodWriteEffectAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod,
				SootUtils.getJNISignature(this.definition.getAnnotationClassEffects()));
	}

	public final List<ILevel> extractMethodEffects(SootMethod sootMethod)
			throws ExtractionException {
		VisibilityAnnotationTag vt = SootUtils.extractVisibilityAnnotationTag(sootMethod);
		AnnotationTag at = SootUtils.extractAnnotationTagWithType(vt, SootUtils.getJNISignature(definition.getAnnotationClassEffects()));
		SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassEffects(), at);
		return definition.extractEffects(dao);
	}

	public List<ILevel> getLibraryWriteEffects(SootMethod sootMethod) {
		return this.definition.getLibraryMethodWriteEffects(sootMethod);
	}

	public final boolean hasClassWriteEffectAnnotation(SootClass sootClass) {
		return hasAnnotationWithType(sootClass,
				SootUtils.getJNISignature(this.definition.getAnnotationClassEffects()));
	}

	public final List<ILevel> extractClassEffects(SootClass sootClass)
			throws ExtractionException {
		VisibilityAnnotationTag vt = SootUtils.extractVisibilityAnnotationTag(sootClass);
		AnnotationTag at = SootUtils.extractAnnotationTagWithType(vt, SootUtils.getJNISignature(definition.getAnnotationClassEffects()));
		SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassEffects(), at);
		return definition.extractEffects(dao);
	}

	public List<ILevel> getLibraryClassWriteEffects(SootClass sootClass) {
		return this.definition.getLibraryClassWriteEffects(sootClass);
	}

	public final boolean hasFieldSecurityAnnotation(SootField sootField) {
		return hasAnnotationWithType(sootField,
				SootUtils.getJNISignature(this.definition.getAnnotationClassFieldLevel()));
	}

	public final ILevel extractFieldSecurityLevel(SootField sootField) 
			throws ExtractionException {
		VisibilityAnnotationTag vt = SootUtils.extractVisibilityAnnotationTag(sootField);
		AnnotationTag at = SootUtils.extractAnnotationTagWithType(vt, SootUtils.getJNISignature(definition.getAnnotationClassFieldLevel()));
		SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassFieldLevel(), at);
		return definition.extractFieldLevel(dao);
	}

	public ILevel getLibraryFieldSecurityLevel(SootField sootField) {
		return this.definition.getLibraryFieldLevel(sootField);
	}

	public ILevel getDefaultVariableSecurityLevel() {
		return this.definition.getDefaultVariableLevel();
	}

	private final boolean hasAnnotationWithType(Host host, String type) {
		try {
			if (SootUtils.hasVisibilityAnnnotationTag(host)) {
				VisibilityAnnotationTag tag = SootUtils
						.extractVisibilityAnnotationTag(host);
				if (SootUtils.hasAnnotationOfType(tag, type))
					return true;
			}
			return false;
		} catch (ExtractionException e) {
			return false;
		}
	}
	
}