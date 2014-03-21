package security;

import java.util.List;

import security.ILevel;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

import exception.SootException.ExtractionException;
import exception.SootException.InvalidLevelException;

public interface ILevelMediator {

	public boolean checkLevelsValidity(List<ILevel> levels);

	public boolean checkLevelValidity(ILevel level);

	public boolean checkParameterLevelsValidity(List<ILevel> levels);

	public List<ILevel> extractClassEffects(SootClass sootClass) throws ExtractionException;

	public ILevel extractFieldSecurityLevel(SootField sootField) throws ExtractionException;

	public List<ILevel> extractMethodEffects(SootMethod sootMethod) throws ExtractionException;

	public List<ILevel> extractParameterSecurityLevels(SootMethod sootMethod) throws ExtractionException;

	public ILevel extractReturnSecurityLevel(SootMethod sootMethod) throws ExtractionException;

	public List<ILevel> getAvailableLevels();

	public ILevel getDefaultVariableSecurityLevel();

	public ILevel getGreatestLowerBoundLevel();

	public ILevel getGreatestLowerBoundLevelOf(ILevel level1, ILevel level2);

	public ILevel getGreatestLowerBoundLevelOf(List<ILevel> levels) throws InvalidLevelException;

	public List<ILevel> getInvalidLevels(List<ILevel> levels);

	public List<ILevel> getInvalidParameterLevels(List<ILevel> levels);

	public ILevel getLeastUpperBoundLevel();

	public ILevel getLeastUpperBoundLevelOf(ILevel level1, ILevel level2);

	public ILevel getLeastUpperBoundLevelOf(List<ILevel> levels) throws InvalidLevelException;

	public List<ILevel> getLibraryClassWriteEffects(SootClass sootClass);

	public ILevel getLibraryFieldSecurityLevel(SootField sootField);

	public List<ILevel> getLibraryParameterSecurityLevel(SootMethod sootMethod);

	public ILevel getLibraryReturnSecurityLevel(SootMethod sootMethod, List<ILevel> levels);

	public List<ILevel> getLibraryWriteEffects(SootMethod sootMethod);

	public boolean hasClassWriteEffectAnnotation(SootClass sootClass);

	public boolean hasFieldSecurityAnnotation(SootField sootField);

	public boolean hasMethodWriteEffectAnnotation(SootMethod sootMethod);

	public boolean hasParameterSecurityAnnotation(SootMethod sootMethod);

	public boolean hasReturnSecurityAnnotation(SootMethod sootMethod);

	public boolean isEquals(ILevel level1, ILevel level2);

	public boolean isStronger(ILevel level1, ILevel level2);

	public boolean isStrongerOrEquals(ILevel level1, ILevel level2);

	public boolean isWeaker(ILevel level1, ILevel level2);

	public boolean isWeakerOrEquals(ILevel level1, ILevel level2);

}
