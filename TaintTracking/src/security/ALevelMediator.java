package security;

import static resource.Messages.getMsg;
import static utils.AnalysisUtils.extractAnnotationTagWithType;
import static utils.AnalysisUtils.extractVisibilityAnnotationTag;
import static utils.AnalysisUtils.generateClassSignature;
import static utils.AnalysisUtils.generateFieldSignature;
import static utils.AnalysisUtils.generateMethodSignature;
import static utils.AnalysisUtils.getJNISignature;
import static utils.AnalysisUtils.hasAnnotationOfType;
import static utils.AnalysisUtils.hasVisibilityAnnnotationTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Host;
import soot.tagkit.VisibilityAnnotationTag;
import annotation.SootAnnotationDAO;
import constraints.IConstraint;
import exception.AnnotationElementNotFoundException;
import exception.AnnotationExtractionException;
import exception.AnnotationInvalidConstraintsException;
import exception.OperationInvalidException;

public abstract class ALevelMediator implements ILevelMediator {

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

	public final List<ILevel> extractClassEffects(SootClass sootClass) {
		try {
			VisibilityAnnotationTag vt = extractVisibilityAnnotationTag(sootClass);
			AnnotationTag at = extractAnnotationTagWithType(vt, getJNISignature(definition.getAnnotationClassEffects()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassEffects(), at);
			return definition.extractEffects(dao);
		} catch (AnnotationExtractionException | AnnotationElementNotFoundException e) {
			throw new AnnotationExtractionException(
					getMsg("exception.annotation.extract_class_effects_error", generateClassSignature(sootClass)), e);
		}

	}

	public final List<IConstraint> extractConstraints(SootMethod sootMethod) {
		try {
			VisibilityAnnotationTag vt = extractVisibilityAnnotationTag(sootMethod);
			AnnotationTag at = extractAnnotationTagWithType(vt, getJNISignature(definition.getAnnotationClassConstraints()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassConstraints(), at);
			return definition.extractConstraints(dao, sootMethod.getSignature());
		} catch (AnnotationExtractionException | AnnotationElementNotFoundException | AnnotationInvalidConstraintsException e) {
			throw new AnnotationExtractionException(
					getMsg("exception.annotation.extract_method_constraints_error", generateMethodSignature(sootMethod)), e);
		}
	}
	
	public final List<IConstraint> extractConstraints(SootClass sootClass) {
		try {
			VisibilityAnnotationTag vt = extractVisibilityAnnotationTag(sootClass);
			AnnotationTag at = extractAnnotationTagWithType(vt, getJNISignature(definition.getAnnotationClassConstraints()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassConstraints(), at);
			return definition.extractConstraints(dao, sootClass.getName());
		} catch (AnnotationExtractionException | AnnotationElementNotFoundException | AnnotationInvalidConstraintsException e) {
			throw new AnnotationExtractionException(
					getMsg("exception.annotation.extract_class_constraints_error", generateClassSignature(sootClass)), e);
		}
	}

	public final ILevel extractFieldSecurityLevel(SootField sootField) {
		try {
			VisibilityAnnotationTag vt = extractVisibilityAnnotationTag(sootField);
			AnnotationTag at = extractAnnotationTagWithType(vt, getJNISignature(definition.getAnnotationClassFieldLevel()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassFieldLevel(), at);
			return definition.extractFieldLevel(dao);
		} catch (AnnotationExtractionException | AnnotationElementNotFoundException e) {
			throw new AnnotationExtractionException(getMsg("exception.annotation.extract_field_level_error", generateFieldSignature(sootField)),
					e);
		}
	}

	public final List<ILevel> extractMethodEffects(SootMethod sootMethod) {
		try {
			VisibilityAnnotationTag vt = extractVisibilityAnnotationTag(sootMethod);
			AnnotationTag at = extractAnnotationTagWithType(vt, getJNISignature(definition.getAnnotationClassEffects()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassEffects(), at);
			return definition.extractEffects(dao);
		} catch (AnnotationExtractionException | AnnotationElementNotFoundException e) {
			throw new AnnotationExtractionException(getMsg("exception.annotation.extract_method_effects_error",
					generateMethodSignature(sootMethod)), e);
		}
	}

	public final List<ILevel> extractParameterSecurityLevels(SootMethod sootMethod) {
		try {
			VisibilityAnnotationTag vt = extractVisibilityAnnotationTag(sootMethod);
			AnnotationTag at = extractAnnotationTagWithType(vt, getJNISignature(definition.getAnnotationClassParameterLevel()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassParameterLevel(), at);
			return definition.extractParameterLevels(dao);
		} catch (AnnotationExtractionException | AnnotationElementNotFoundException e) {
			throw new AnnotationExtractionException(getMsg("exception.annotation.extract_method_parameter_error",
					generateMethodSignature(sootMethod)), e);
		}
	}

	public final ILevel extractReturnSecurityLevel(SootMethod sootMethod) {
		try {
			VisibilityAnnotationTag vt = extractVisibilityAnnotationTag(sootMethod);
			AnnotationTag at = extractAnnotationTagWithType(vt, getJNISignature(definition.getAnnotationClassReturnLevel()));
			SootAnnotationDAO dao = new SootAnnotationDAO(definition.getAnnotationClassReturnLevel(), at);
			return definition.extractReturnLevel(dao);
		} catch (AnnotationExtractionException | AnnotationElementNotFoundException e) {
			throw new AnnotationExtractionException(getMsg("exception.annotation.extract_method_return_error",
					generateMethodSignature(sootMethod)), e);
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

	public ILevel getGreatestLowerBoundLevelOf(List<ILevel> levels) {
		if (levels.size() > 0) {
			ILevel result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = this.definition.getGreatestLowerBoundLevel(result, levels.get(i));
			}
			return result;
		}
		throw new OperationInvalidException(getMsg("exception.level.operation.empty_list_glb"));
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

	public ILevel getLeastUpperBoundLevelOf(List<ILevel> levels) {
		if (levels.size() > 0) {
			ILevel result = levels.get(0);
			for (int i = 1; i < levels.size(); i++) {
				result = this.definition.getLeastUpperBoundLevel(result, levels.get(i));
			}
			return result;
		}
		throw new OperationInvalidException(getMsg("exception.level.operation.empty_list_lub"));
	}

	public List<ILevel> getLibraryClassWriteEffects(SootClass sootClass) {
		return this.definition.getLibraryClassWriteEffects(sootClass.getName());
	}

	public List<IConstraint> getLibraryConstraints(SootMethod sootMethod) {
		List<String> paramType = new ArrayList<String>();
		for (Object type : sootMethod.getParameterTypes()) {
			paramType.add(type.toString());
		}
		String returnType = sootMethod.getReturnType().toString();
		return this.definition.getLibraryConstraints(sootMethod.getName(), paramType, returnType, sootMethod.getDeclaringClass().getName(),
				sootMethod.getSignature());
	}
	
	public List<IConstraint> getLibraryConstraints(SootClass sootClass) {
		return this.definition.getLibraryConstraints(sootClass.getName());
	}

	public ILevel getLibraryFieldSecurityLevel(SootField sootField) {
		return this.definition.getLibraryFieldLevel(sootField.getName(), sootField.getDeclaringClass().getName(), sootField.getSignature());
	}

	public List<ILevel> getLibraryParameterSecurityLevel(SootMethod sootMethod) {
		List<String> paramType = new ArrayList<String>();
		for (Object type : sootMethod.getParameterTypes()) {
			paramType.add(type.toString());
		}
		return this.definition.getLibraryParameterLevel(sootMethod.getName(), paramType, sootMethod.getDeclaringClass().getName(),
				sootMethod.getSignature());
	}

	public ILevel getLibraryReturnSecurityLevel(SootMethod sootMethod, List<ILevel> levels) {
		List<String> paramType = new ArrayList<String>();
		for (Object type : sootMethod.getParameterTypes()) {
			paramType.add(type.toString());
		}
		return this.definition.getLibraryReturnLevel(sootMethod.getName(), paramType, sootMethod.getDeclaringClass().getName(),
				sootMethod.getSignature(), levels);
	}

	public List<ILevel> getLibraryWriteEffects(SootMethod sootMethod) {
		List<String> paramType = new ArrayList<String>();
		for (Object type : sootMethod.getParameterTypes()) {
			paramType.add(type.toString());
		}
		return this.definition.getLibraryMethodWriteEffects(sootMethod.getName(), paramType, sootMethod.getDeclaringClass().getName(),
				sootMethod.getSignature());
	}

	public final boolean hasClassWriteEffectAnnotation(SootClass sootClass) {
		return hasAnnotationWithType(sootClass, getJNISignature(this.definition.getAnnotationClassEffects()));
	}
	
	public final boolean hasConstraintsAnnotation(SootClass sootClass) {
		return hasAnnotationWithType(sootClass, getJNISignature(this.definition.getAnnotationClassConstraints()));
	}

	public final boolean hasConstraintsAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod, getJNISignature(this.definition.getAnnotationClassConstraints()));
	}

	public final boolean hasFieldSecurityAnnotation(SootField sootField) {
		return hasAnnotationWithType(sootField, getJNISignature(this.definition.getAnnotationClassFieldLevel()));
	}

	public final boolean hasMethodWriteEffectAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod, getJNISignature(this.definition.getAnnotationClassEffects()));
	}

	public final boolean hasParameterSecurityAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod, getJNISignature(this.definition.getAnnotationClassParameterLevel()));
	}

	public final boolean hasReturnSecurityAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod, getJNISignature(this.definition.getAnnotationClassReturnLevel()));
	}

	public boolean isEquals(ILevel level1, ILevel level2) {
		return level1.equals(level2);
	}

	public boolean isStronger(ILevel level1, ILevel level2) {
		return this.definition.compare(level1, level2) > 0;
	}

	public boolean isStrongerOrEquals(ILevel level1, ILevel level2) {
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
			if (hasVisibilityAnnnotationTag(host)) {
				VisibilityAnnotationTag tag = extractVisibilityAnnotationTag(host);
				if (hasAnnotationOfType(tag, type)) return true;
			}
			return false;
		} catch (AnnotationExtractionException e) {
			return false;
		}
	}

}