package security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exception.SootException.InvalidEquationException;
import exception.SootException.InvalidLevelException;

import security.LevelEquation.LevelEquationCreator;
import security.LevelEquationVisitor.*;
import soot.SootMethod;

/**
 * Class provides Java annotations which allows to specify the security level of method signatures.
 * 
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class SecurityAnnotation {
	
	/** */
	public static String VOID_LEVEL = SecurityLevel.VOID_LEVEL;
	/** */
	public static List<String> ADDITIONAL_LEVELS = SecurityLevel.ADDITIONAL_LEVELS;
	/** */
	public static String LEVEL_PATTERN_SIGN = "*";
	/** */	
	private List<String> availableLevels = new ArrayList<String>();
	/** */
	private List<String> idMethodNames = new ArrayList<String>();
	
	/**
	 * 
	 * @param availableLevels
	 */
	public SecurityAnnotation(List<String> availableLevels) {
		super();
		this.availableLevels.addAll(availableLevels);
		for (String level : this.availableLevels) {
			idMethodNames.add(level + "Id");
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getAvailableLevels() {
		return this.availableLevels;
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
		List<String> variable = new ArrayList<String>();
		boolean valid = true;
		for (String level : listOfParameterLevels) {
			if (! availableLevels.contains(level)) {
				if (! level.startsWith(LEVEL_PATTERN_SIGN)) {
					valid = false;
				} else {
					variable.add(level);
				}
			}
		}
		for (int i = 0; i < variable.size(); i++) {
			if (! variable.contains(LEVEL_PATTERN_SIGN + i)) valid = false;
		}
		return valid;
	}
	
	/**
	 * 
	 * @param listOfParameterLevels
	 * @return
	 */
	public List<String> getInvalidParameterLevels(List<String> listOfParameterLevels) {
		List<String> invalid = new ArrayList<String>(listOfParameterLevels);
		List<String> variable = new ArrayList<String>();
		for (String level : listOfParameterLevels) {
			if (availableLevels.contains(level)) invalid.remove(level);
			if (level.startsWith(LEVEL_PATTERN_SIGN)) variable.add(level);
		}
		for (int i = 0; i < variable.size(); i++) {
			String level = LEVEL_PATTERN_SIGN + i;
			if (variable.contains(level)) invalid.remove(level);
		}
		return invalid;
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	public boolean checkValidityOfFieldLevel(String level) {
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
	 */
	public String getStrongestLevelOf(List<String> argumentLevels) {
		if (availableLevels.size() > 0) {
			for (int i = 0; i < availableLevels.size(); i++) {
				if (argumentLevels.contains(availableLevels.get(i))) {
					return availableLevels.get(i);
				}
			}
			return getWeakestSecurityLevel();
		}
		return null;
	}

	/**
	 * 
	 * @param argumentLevel
	 * @param parameterLevel
	 * @return
	 */
	public boolean isWeakerOrEqualsThan(String argumentLevel, String parameterLevel) {
		int indexArgument = availableLevels.indexOf(argumentLevel);
		int indexParameter = availableLevels.indexOf(parameterLevel);
		if (indexArgument >= 0 && indexParameter >= 0) {
			return indexParameter <= indexArgument;
		} else {
			// TODO Throw exception invalid parameter or argument
			return false;
		}
	}

	/**
	 * 
	 * @param level1
	 * @param level2
	 * @return
	 */
	public String getMinLevel(String level1, String level2) {
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
			} else {
				// TODO Throw exception invalid parameter or argument
				return null;
			}
		}
	}

	/**
	 * 
	 * @param level1
	 * @param level2
	 * @return
	 */
	public String getMaxLevel(String level1, String level2) {
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
			} else {
				// TODO Throw exception invalid parameter or argument
				return null;
			}
		}
	}
	
	/**
	 * 
	 * @param sootMethod
	 * @return
	 */
	public boolean isMethodOfSootSecurity(SootMethod sootMethod) {
		return sootMethod.getDeclaringClass().getName().equals("security.SootSecurityLevel");
	}
	
	/**
	 * 
	 * @param sootMethod
	 * @return
	 */
	public boolean isIdMethod(SootMethod sootMethod) {
		boolean packageValid = sootMethod.getDeclaringClass().getName().equals("security.SootSecurityLevel");
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
		if (methodName.substring(methodName.length() - 2).equals("Id")) {
			level = methodName.substring(0, methodName.length() - 2);
		}
		return level;
	}
}
