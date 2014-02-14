package securityNewNew;

import java.util.List;

import securityNewNew.ILevel;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

import exception.SootException.ExtractionException;
import exception.SootException.InvalidLevelException;

public interface ILevelMediator {

	public List<ILevel> getAvailableLevels();
	
	public boolean checkLevelValidity(ILevel level);
	
	public boolean checkLevelsValidity(List<ILevel> levels);
	public List<ILevel> getInvalidLevels(List<ILevel> levels);
	
	public boolean checkParameterLevelsValidity(List<ILevel> levels);
	public List<ILevel> getInvalidParameterLevels(List<ILevel> levels);
	
	public ILevel getLeastUpperBoundLevel();
	public ILevel getLeastUpperBoundLevelOf(List<ILevel> levels) throws InvalidLevelException;
	public ILevel getLeastUpperBoundLevelOf(ILevel level1, ILevel level2);
	
	public ILevel getGreatestLowerBoundLevel();
	public ILevel getGreatestLowerBoundLevelOf(List<ILevel> levels) throws InvalidLevelException;
	public ILevel getGreatestLowerBoundLevelOf(ILevel level1, ILevel level2);
	
	public boolean isStronger(ILevel level1, ILevel level2);
	public boolean isStrongerOrEquals(ILevel level1, ILevel level2);
	public boolean isEquals(ILevel level1, ILevel level2);
	public boolean isWeakerOrEquals(ILevel level1, ILevel level2);
	public boolean isWeaker(ILevel level1, ILevel level2);
	
	public boolean isIdFunction(SootMethod sootMethod);
	public boolean isMethodOfDefinitionClass(SootMethod sootMethod);
	
	
	public boolean hasReturnSecurityAnnotation(SootMethod sootMethod);
	public ILevel extractReturnSecurityLevel(SootMethod sootMethod) throws ExtractionException;
	public ILevel getLibraryReturnSecurityLevel(SootMethod sootMethod, List<ILevel> levels);
	
	public boolean hasParameterSecurityAnnotation(SootMethod sootMethod);
	public List<ILevel> extractParameterSecurityLevels(SootMethod sootMethod) throws ExtractionException;
	public List<ILevel> getLibraryParameterSecurityLevel(SootMethod sootMethod);
	
	public boolean hasMethodWriteEffectAnnotation(SootMethod sootMethod);
	public List<ILevel> extractMethodEffects(SootMethod sootMethod) throws ExtractionException;
	public List<ILevel> getLibraryWriteEffects(SootMethod sootMethod);
	
	public boolean hasClassWriteEffectAnnotation(SootClass sootClass);
	public List<ILevel> extractClassEffects(SootClass sootClass) throws ExtractionException;
	public List<ILevel> getLibraryClassWriteEffects(SootClass sootClass);
	
	public boolean hasFieldSecurityAnnotation(SootField sootField);
	public ILevel extractFieldSecurityLevel(SootField sootField) throws ExtractionException;
	public ILevel getLibraryFieldSecurityLevel(SootField sootField);
	
	public ILevel getDefaultVariableSecurityLevel();
	
}
