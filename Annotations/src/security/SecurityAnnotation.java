package security;

import java.util.*;

import exception.SootException.*;
import security.LevelEquation.*;
import security.LevelEquationVisitor.*;
import soot.*;
import utils.SecurityMessages;

/**
 * Class provides Java annotations which allows to specify the security level of method signatures.
 * 
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class SecurityAnnotation {
	
	/** */
	public static final String VOID_LEVEL = SecurityLevel.VOID_LEVEL;
	/** */
	public static final List<String> ADDITIONAL_LEVELS = SecurityLevel.ADDITIONAL_LEVELS;
	/** */
	public static final String LEVEL_PATTERN_SIGN = "*";
	/** */	
	private final List<String> availableLevels;
	/** */
	private List<String> idMethodNames = new ArrayList<String>();
	
	/**
	 * 
	 * @param availableLevels
	 */
	public SecurityAnnotation(List<String> availableLevels) {
		super();
		this.availableLevels = availableLevels;
		for (String level : this.availableLevels) {
			idMethodNames.add(level + SecurityLevel.SUFFIX_ID_METHOD);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getAvailableLevels() {
		return availableLevels;
	}
	
	/**
	 * 
	 * @param cl
	 * @return
	 */
	public static String getSootAnnotationTag(Class<?> cl) {
		String packageName = cl.getPackage().getName();
		String parentClassName = cl.getDeclaringClass().getSimpleName();
		String className = cl.getSimpleName();
		return "L" + packageName + "/" + parentClassName + "$" + className + ";";
	}

	/**
	 * 
	 * @param returnLevel
	 * @param methodParameterLevels
	 * @return
	 * @throws InvalidLevelException
	 * @throws InvalidEquationException
	 */
	public LevelEquationValidityVistitor checkValidityOfReturnLevel(String returnLevel,  List<String> methodParameterLevels) throws InvalidLevelException, InvalidEquationException {
		List<String> allLevels = new ArrayList<String>(Arrays.asList(VOID_LEVEL));
		allLevels.addAll(availableLevels);
		for (String level : methodParameterLevels) {
			if (!allLevels.contains(level))
				allLevels.add(level);
		}
		LevelEquation equation = LevelEquationCreator.createFrom(returnLevel);
		LevelEquationValidityVistitor visitor = new LevelEquationValidityVistitor(equation, this);
		equation.accept(visitor);
		return visitor;
	}
	
	/**
	 * 
	 * @param returnLevel
	 * @param methodParameterLevels
	 * @return
	 * @throws InvalidLevelException
	 * @throws InvalidEquationException
	 */
	public LevelEquation getReturnLevelEquation(String returnLevel,  List<String> methodParameterLevels) throws InvalidLevelException, InvalidEquationException {
		List<String> allLevels = new ArrayList<String>(Arrays.asList(VOID_LEVEL));
		allLevels.addAll(availableLevels);
		for (String level : methodParameterLevels) {
			if (! allLevels.contains(level)) allLevels.add(level);
		}
		LevelEquation equation = LevelEquationCreator.createFrom(returnLevel);
		return equation;
	}
	
	/**
	 * 
	 * @param levelEquation
	 * @return
	 */
	public LevelEquationValidityVistitor getLevelEquationValidityVistitor(LevelEquation levelEquation) {
		return new LevelEquationValidityVistitor(levelEquation, this);
	}
	
	/**
	 * 
	 * @return
	 */
	public LevelEquationCalculateVoidVisitor getLevelEquationCalculateVoidVisitor() {
		return new LevelEquationCalculateVoidVisitor();
	}
	
	/**
	 * 
	 * @param argumentLevels
	 * @param parameterLevels
	 * @return
	 */
	public LevelEquationEvaluationVisitor getLevelEquationEvaluationVisitor(List<String> argumentLevels, List<String>parameterLevels) {
		return new LevelEquationEvaluationVisitor(argumentLevels, parameterLevels, this);
	}

	/**
	 * 
	 * @param listOfParameterLevels
	 * @return
	 */
	public boolean checkValidityOfParameterLevels(List<String> listOfParameterLevels) {
		if (checkValidityOfLevels(listOfParameterLevels)) {
			return true;
		} else {
			List<String> invalids = getInvalidLevels(listOfParameterLevels);
			List<String> variable = new ArrayList<String>();
			for (String invalid : invalids) {
				if (! invalid.startsWith(LEVEL_PATTERN_SIGN)) {
					return false;
				} else {
					variable.add(invalid);
				}
			}
			for (int i = 0; i < variable.size(); i++) {
				if (! variable.contains(LEVEL_PATTERN_SIGN + i)) return false;
			}
			return true;
		}
	}
	
	/**
	 * 
	 * @param listOfLevels
	 * @return
	 */
	public boolean checkValidityOfLevels(List<String> listOfLevels) {
		for (String level : listOfLevels) {
			if (! availableLevels.contains(level)) return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param listOfLevels
	 * @return
	 */
	public List<String> getInvalidLevels(List<String> listOfLevels) {
		List<String> invalid = new ArrayList<String>(listOfLevels);
		for (String level : listOfLevels) {
			if (availableLevels.contains(level)) invalid.remove(level);
		}
		return invalid;
	}
	
	/**
	 * 
	 * @param listOfParameterLevels
	 * @return
	 */
	public List<String> getInvalidParameterLevels(List<String> listOfParameterLevels) {
		List<String> invalids = getInvalidLevels(listOfParameterLevels);
		List<String> variable = new ArrayList<String>();
		for (String invalid : invalids) {
			if (invalid.startsWith(LEVEL_PATTERN_SIGN)) variable.add(invalid);
		}
		for (int i = 0; i < variable.size(); i++) {
			String level = LEVEL_PATTERN_SIGN + i;
			if (variable.contains(level)) invalids.remove(level);
		}
		return invalids;
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	public boolean checkValidityOfLevel(String level) {
		return availableLevels.contains(level);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getWeakestSecurityLevel() {
		if (availableLevels.size() > 0) {
			return availableLevels.get(availableLevels.size() - 1);
		}
		return null;
	}
	
	/**
	 * 
	 * @param argumentLevels
	 * @return
	 * @throws InvalidLevelException
	 */
	public String getStrongestLevelOf(List<String> argumentLevels) throws InvalidLevelException {
		if (availableLevels.size() > 0) {
			for (int i = 0; i < availableLevels.size(); i++) {
				if (argumentLevels.contains(availableLevels.get(i))) {
					return availableLevels.get(i);
				}
			}
			return getWeakestSecurityLevel();
		}
		throw new InvalidLevelException(SecurityMessages.invalidLevelsComparison(argumentLevels));
	}

	/**
	 * 
	 * @param argumentLevel
	 * @param parameterLevel
	 * @return
	 * @throws InvalidLevelException
	 */
	public boolean isWeakerOrEqualsThan(String argumentLevel, String parameterLevel) throws InvalidLevelException {
		int indexArgument = availableLevels.indexOf(argumentLevel);
		int indexParameter = availableLevels.indexOf(parameterLevel);
		if (indexArgument >= 0 && indexParameter >= 0) {
			return indexParameter <= indexArgument;
		}
		throw new InvalidLevelException(SecurityMessages.invalidLevelComparison(argumentLevel, parameterLevel));
	}

	/**
	 * 
	 * @param level1
	 * @param level2
	 * @return
	 * @throws InvalidLevelException
	 */
	public String getMinLevel(String level1, String level2) throws InvalidLevelException {
		if (level1.equals(VOID_LEVEL) || level2.equals(VOID_LEVEL)) {
			return VOID_LEVEL;
		} else {
			int indexLevel1 = availableLevels.indexOf(level1);
			int indexLevel2 = availableLevels.indexOf(level2);
			if (indexLevel1 >= 0 && indexLevel2 >= 0) {
				if (indexLevel1 <= indexLevel2) {
					return level2;
				} else {
					return level1;
				}
			}
			throw new InvalidLevelException(SecurityMessages.invalidLevelComparison(level1, level2));
		}
	}

	/**
	 * 
	 * @param level1
	 * @param level2
	 * @return
	 * @throws InvalidLevelException
	 */
	public String getMaxLevel(String level1, String level2) throws InvalidLevelException {
		if (level1.equals(VOID_LEVEL) || level2.equals(VOID_LEVEL)) {
			return VOID_LEVEL;
		} else {
			int indexLevel1 = availableLevels.indexOf(level1);
			int indexLevel2 = availableLevels.indexOf(level2);
			if (indexLevel1 >= 0 && indexLevel2 >= 0) {
				if (indexLevel1 <= indexLevel2) {
					return level1;
				} else {
					return level2;
				}
			}
			throw new InvalidLevelException(SecurityMessages.invalidLevelComparison(level1, level2));
		}
	}
	
	/**
	 * 
	 * @param sootMethod
	 * @return
	 */
	public boolean isMethodOfSootSecurity(SootMethod sootMethod) {
		return sootMethod.getDeclaringClass().getName().equals(SecurityLevel.IMPLEMENTED_CLASS_NAME);
	}
	
	/**
	 * 
	 * @param sootMethod
	 * @return
	 */
	public boolean isIdMethod(SootMethod sootMethod) {
		boolean packageValid = sootMethod.getDeclaringClass().getName().equals(SecurityLevel.IMPLEMENTED_CLASS_NAME);
		boolean methodNameValid = idMethodNames.contains(sootMethod.getName());
		boolean parameterValid = sootMethod.getParameterCount() == 1;
		return packageValid && methodNameValid && parameterValid;
	}

	/**
	 * 
	 * @param sootMethod
	 * @return
	 */
	public String getReturnSecurityLevelOfIdMethod(SootMethod sootMethod) {
		String methodName = sootMethod.getName();
		String level = "";
		if (methodName.endsWith(SecurityLevel.SUFFIX_ID_METHOD)) {
			level = methodName.substring(0, methodName.length() - 2);
		}
		return level;
	}

	/**
	 * 
	 * @param cl
	 * @return
	 */
	public static String getEffectIdentifier(Class<?> cl) {
		return cl.getSimpleName().toUpperCase(Locale.ENGLISH);
	}
	
}
