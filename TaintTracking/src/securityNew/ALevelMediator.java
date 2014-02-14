package securityNew;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import resource.Configuration;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

import exception.SootException.ExtractionException;
import exception.SootException.InvalidLevelException;

public abstract class ALevelMediator<T extends ILevel> implements
		ILevelMediator<T> {

	protected final ILevelDefinition<T> definition;

	public ALevelMediator(ILevelDefinition<T> definition) {
		this.definition = definition;
	}

	public List<T> getAvailableLevels() {
		return Arrays.asList(this.definition.getLevels());
	}

	public ILevelDefinition<T> getDefintion() {
		return this.definition;
	}

	public boolean checkLevelValidity(T level) {
		for (T available : this.definition.getLevels()) {
			if (available.equals(level))
				return true;
		}
		return false;
	}

	public boolean checkLevelsValidity(List<T> levels) {
		for (T level : levels) {
			if (!checkLevelValidity(level))
				return false;
		}
		return true;
	}

	public List<T> getInvalidLevels(List<T> levels) {
		List<T> invalids = new ArrayList<T>();
		for (T level : levels) {
			if (!checkLevelValidity(level))
				invalids.add(level);
		}
		return invalids;
	}

	// TODO
	public abstract boolean checkParameterLevelsValidity(List<T> levels);

	// TODO
	public abstract List<T> getInvalidParameterLevels(List<T> levels);

	public T getLeastUpperBoundLevel() {
		return this.definition.getLeastUpperBoundLevel();
	}

	public T getLeastUpperBoundLevelOf(List<T> levels) throws InvalidLevelException {
		if (levels.size() == 0) {
			// DOC
			throw new InvalidLevelException(
					"can not return the strongest level of an empty list.");
		} else {
			T result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = this.definition.getLeastUpperBoundLevel(result,
						levels.get(i));
			}
			return result;
		}
	}

	public T getLeastUpperBoundLevelOf(T level1, T level2) {
		return this.definition.getLeastUpperBoundLevel(level1, level2);
	}

	public T getGreatestLowerBoundLevel() {
		return this.definition.getGreatesLowerBoundLevel();
	}
	
	public T getGreatestLowerBoundLevelOf(List<T> levels)
			throws InvalidLevelException {
		if (levels.size() == 0) {
			// DOC
			throw new InvalidLevelException(
					"can not return the weakest level of an empty list.");
		} else {
			T result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = this.definition.getGreatestLowerBoundLevel(result, levels.get(i));
			}
			return result;
		}
	}

	public T getGreatestLowerBoundLevelOf(T level1, T level2) {
		return this.definition.getGreatestLowerBoundLevel(level1, level2);
	}

	public boolean isStronger(T level1, T level2) {
		return this.definition.compare(level1, level2) > 0;
	}

	public boolean isStrongerOrEquals(T level1, T level2)
			throws InvalidLevelException {
		return this.definition.compare(level1, level2) >= 0;
	}

	public boolean isEquals(T level1, T level2) {
		return level1.equals(level2);
	}

	public boolean isWeakerOrEquals(T level1, T level2) {
		return this.definition.compare(level1, level2) <= 0;
	}

	public boolean isWeaker(T level1, T level2) {
		return this.definition.compare(level1, level2) < 0;
	}

	public boolean isIdFunction(SootMethod sootMethod) {
		if (isMethodOfDefinitionClass(sootMethod)
				&& sootMethod.getParameterCount() == 1 
				&& sootMethod.isStatic()
				&& sootMethod.isPublic()) {
			for (T level : this.definition.getLevels()) {
				if (sootMethod.getName().equals(
						level.getName() + ILevelDefinition.SUFFIX_METHOD_ID))
					return true;
			}
		}
		return false;
	}

	public boolean isMethodOfDefinitionClass(SootMethod sootMethod) {
		return sootMethod.getDeclaringClass().getName()
				.equals(Configuration.DEF_PATH_JAVA);
	}

	public abstract boolean hasReturnSecurityAnnotation(SootMethod sootMethod);

	public abstract boolean hasReturnSecurityLevel(SootMethod sootMethod);

	public abstract T extractReturnSecurityLevel(SootMethod sootMethod)
			throws ExtractionException;

	public T getLibraryReturnSecurityLevel(SootMethod sootMethod, List<T> levels) {
		return this.definition.getLibraryReturnLevel(sootMethod, levels);
	}

	public abstract boolean hasParameterSecurityAnnotation(SootMethod sootMethod);

	public abstract boolean hasParameterSecurityLevels(SootMethod sootMethod);

	public abstract List<T> extractParameterSecurityLevels(SootMethod sootMethod)
			throws ExtractionException;

	public List<T> getLibraryParameterSecurityLevel(SootMethod sootMethod) {
		return this.definition.getLibraryParameterLevel(sootMethod);
	}

	public abstract boolean hasMethodWriteEffectAnnotation(SootMethod sootMethod);

	public abstract boolean hasMethodWriteEffects(SootMethod sootMethod);

	public abstract List<T> extractMethodEffects(SootMethod sootMethod)
			throws ExtractionException;

	public List<T> getLibraryWriteEffects(SootMethod sootMethod) {
		return this.definition.getLibraryMethodWriteEffects(sootMethod);
	}

	public abstract boolean hasClassWriteEffectAnnotation(SootClass sootClass);

	public abstract boolean hasClassWriteEffects(SootClass sootClass);

	public abstract List<T> extractClassEffects(SootClass sootClass)
			throws ExtractionException;

	public List<T> getLibraryClassWriteEffects(SootClass sootClass) {
		return this.definition.getLibraryClassWriteEffects(sootClass);
	}

	public abstract boolean hasFieldSecurityAnnotation(SootField sootField);

	public abstract boolean hasFieldSecurityLevel(SootField sootField);

	public abstract T extractFieldSecurityLevel(SootField sootField)
			throws ExtractionException;

	public T getLibraryFieldSecurityLevel(SootField sootField) {
		return this.definition.getLibraryFieldLevel(sootField);
	}

	public T getDefaultVariableSecurityLevel() {
		return this.definition.getDefaultVariableLevel();
	}

}