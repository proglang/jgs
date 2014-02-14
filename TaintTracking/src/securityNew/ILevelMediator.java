package securityNew;

import java.util.List;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

import exception.SootException.ExtractionException;
import exception.SootException.InvalidLevelException;

public interface ILevelMediator<T extends ILevel> {

public List<T> getAvailableLevels();
	
	public boolean checkLevelValidity(T level);
	
	public boolean checkLevelsValidity(List<T> levels);
	public List<T> getInvalidLevels(List<T> levels);
	
	public boolean checkParameterLevelsValidity(List<T> levels);
	public List<T> getInvalidParameterLevels(List<T> levels);
	
	public T getLeastUpperBoundLevel();
	public T getLeastUpperBoundLevelOf(List<T> levels) throws InvalidLevelException;
	public T getLeastUpperBoundLevelOf(T level1, T level2);
	
	public T getGreatestLowerBoundLevel();
	public T getGreatestLowerBoundLevelOf(List<T> levels) throws InvalidLevelException;
	public T getGreatestLowerBoundLevelOf(T level1, T level2);
	
	public boolean isStronger(T level1, T level2);
	public boolean isStrongerOrEquals(T level1, T level2);
	public boolean isEquals(T level1, T level2);
	public boolean isWeakerOrEquals(T level1, T level2);
	public boolean isWeaker(T level1, T level2);
	
	public boolean isIdFunction(SootMethod sootMethod);
	public boolean isMethodOfDefinitionClass(SootMethod sootMethod);
	
	
	public boolean hasReturnSecurityAnnotation(SootMethod sootMethod);
	public boolean hasReturnSecurityLevel(SootMethod sootMethod);
	public T extractReturnSecurityLevel(SootMethod sootMethod) throws ExtractionException;
	public T getLibraryReturnSecurityLevel(SootMethod sootMethod, List<T> levels);
	
	public boolean hasParameterSecurityAnnotation(SootMethod sootMethod);
	public boolean hasParameterSecurityLevels(SootMethod sootMethod);
	public List<T> extractParameterSecurityLevels(SootMethod sootMethod) throws ExtractionException;
	public List<T> getLibraryParameterSecurityLevel(SootMethod sootMethod);
	
	public boolean hasMethodWriteEffectAnnotation(SootMethod sootMethod);
	public boolean hasMethodWriteEffects(SootMethod sootMethod);
	public List<T> extractMethodEffects(SootMethod sootMethod) throws ExtractionException;
	public List<T> getLibraryWriteEffects(SootMethod sootMethod);
	
	public boolean hasClassWriteEffectAnnotation(SootClass sootClass);
	public boolean hasClassWriteEffects(SootClass sootClass);
	public List<T> extractClassEffects(SootClass sootClass) throws ExtractionException;
	public List<T> getLibraryClassWriteEffects(SootClass sootClass);
	
	public boolean hasFieldSecurityAnnotation(SootField sootField);
	public boolean hasFieldSecurityLevel(SootField sootField);
	public T extractFieldSecurityLevel(SootField sootField) throws ExtractionException;
	public T getLibraryFieldSecurityLevel(SootField sootField);
	
	public T getDefaultVariableSecurityLevel();
	
}
