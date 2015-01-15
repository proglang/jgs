package security;

import java.util.List;
import java.util.Set;

import constraints.LEQConstraint;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public interface ILevelMediator {

	public boolean checkLevelsValidity(List<ILevel> levels);

	public boolean checkLevelValidity(ILevel level);

	public boolean checkParameterLevelsValidity(List<ILevel> levels);

	public List<ILevel> extractClassEffects(SootClass sootClass);

	public Set<LEQConstraint> extractConstraints(SootMethod sootMethod);

	public Set<LEQConstraint> extractConstraints(SootClass sootClass);

	public List<ILevel> extractFieldSecurityLevel(SootField sootField);

	public List<ILevel> extractMethodEffects(SootMethod sootMethod);

	public List<ILevel> extractParameterSecurityLevels(SootMethod sootMethod);

	public ILevel extractReturnSecurityLevel(SootMethod sootMethod);

	public List<ILevel> getAvailableLevels();

	public ILevel getDefaultVariableSecurityLevel();

	public ILevel getGreatestLowerBoundLevel();

	public ILevel getGreatestLowerBoundLevelOf(ILevel level1, ILevel level2);

	public ILevel getGreatestLowerBoundLevelOf(List<ILevel> levels);

	public List<ILevel> getInvalidLevels(List<ILevel> levels);

	public List<ILevel> getInvalidParameterLevels(List<ILevel> levels);

	public ILevel getLeastUpperBoundLevel();

	public ILevel getLeastUpperBoundLevelOf(ILevel level1, ILevel level2);

	public ILevel getLeastUpperBoundLevelOf(List<ILevel> levels);

	public List<ILevel> getLibraryClassWriteEffects(SootClass sootClass);

	public Set<LEQConstraint> getLibraryConstraints(SootMethod sootMethod);

	public Set<LEQConstraint> getLibraryConstraints(SootClass sootClass);

	public List<ILevel> getLibraryFieldSecurityLevel(SootField sootField);

	public List<ILevel> getLibraryParameterSecurityLevel(SootMethod sootMethod);

	public ILevel getLibraryReturnSecurityLevel(SootMethod sootMethod, List<ILevel> levels);

	public List<ILevel> getLibraryWriteEffects(SootMethod sootMethod);

	public boolean hasClassWriteEffectAnnotation(SootClass sootClass);

	public boolean hasConstraintsAnnotation(SootMethod sootMethod);

	public boolean hasConstraintsAnnotation(SootClass sootClass);

	public boolean hasFieldSecurityAnnotation(SootField sootField);

	public boolean hasMethodWriteEffectAnnotation(SootMethod sootMethod);

	public boolean hasParameterSecurityAnnotation(SootMethod sootMethod);

	public boolean hasReturnSecurityAnnotation(SootMethod sootMethod);

	public boolean isEquals(ILevel level1, ILevel level2);

	public boolean isStronger(ILevel level1, ILevel level2);

	public boolean isStrongerOrEquals(ILevel level1, ILevel level2);

	public boolean isWeaker(ILevel level1, ILevel level2);

	public boolean isWeakerOrEquals(ILevel level1, ILevel level2);
	
	public List<ILevel> translateNamesIntoLevels(List<String> names);

}
