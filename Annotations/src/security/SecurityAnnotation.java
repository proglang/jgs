package security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import security.Annotations.ReturnSecurity;
import security.Annotations.WriteEffect;
import security.Annotations.FieldSecurity;
import security.Annotations.ParameterSecurity;
import security.LevelEquation.LevelEquationCreator;
import security.LevelEquationVisitor.LevelEquationCalculateVoidVisitor;
import security.LevelEquationVisitor.LevelEquationEvaluationVisitor;
import security.LevelEquationVisitor.LevelEquationValidityVistitor;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Host;
import soot.tagkit.VisibilityAnnotationTag;
import utils.Predefined;
import utils.SecurityMessages;
import utils.SootUtils;
import exception.SootException.InvalidEquationException;
import exception.SootException.InvalidLevelException;
import exception.SootException.ExtractionException;

/**
 * <h1>Security annotation wrapper</h1>
 * 
 * The {@link SecurityAnnotation} class wraps the ordered
 * <em>security level</em> list which is given by the {@code SootSecurityLevel}
 * implementation of the developer. This implementation should inherit from the
 * class {@link SecurityLevel} and thus this implementation implements also the
 * method {@link SecurityLevel#getOrderedSecurityLevels()} which returns the
 * ordered list of <em>security levels</em>. The {@link SecurityAnnotation}
 * class provides methods which have a relation to the <em>security levels</em>,
 * the corresponding annotations and the id functions of the developer
 * implementation of the class {@link SecurityLevel}, e.g.
 * <em>security level</em> comparison methods and methods which check the
 * validity of levels.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class SecurityAnnotation {

	/** Default element name of the annotations. */
	private static final String ANNOTATION_ELEMENT_NAME = "value";
	/**
	 * List of additional <em>security levels</em> which are used internally,
	 * e.g. {@link SecurityAnnotation#VOID_LEVEL}.
	 * 
	 * @see SecurityLevel#SUPPLEMENTARY_LEVELS
	 */
	public static final List<String> ADDITIONAL_LEVELS = SecurityLevel.SUPPLEMENTARY_LEVELS;
	/**
	 * Prefix of variable <em>security levels</em>. A variable level consists of
	 * the character ' {@code *}' followed by a number in the range from 0 to
	 * {@code number of variable levels - 1}. This pattern applies to every
	 * method.
	 */
	public static final String LEVEL_PATTERN_SIGN = "*";
	/**
	 * <em>Security level</em> that indicates that the method has no return
	 * value. Such methods should be annotated with the
	 * {@link Annotations.ReturnSecurity} annotation and the value '
	 * {@code void}'.
	 * 
	 * @see SecurityLevel#LEVEL_VOID
	 */
	public static final String VOID_LEVEL = SecurityLevel.LEVEL_VOID;

	/**
	 * Generates an effect identifier for a given effect annotation class, e.g.
	 * for the class {@link Annotations.WriteEffect}.
	 * 
	 * @param cl
	 *            Class for which the effect identifier should be generated.
	 * @return Effect identifier for the given effect annotation class.
	 */
	public static String getEffectIdentifier(Class<?> cl) {
		String className = cl.getSimpleName().toUpperCase(Locale.ENGLISH);
		return String.format("EFFECT_ID#%s", className);
	}

	/**
	 * Generates the Soot annotation tag for a given annotation class, e.g. for
	 * the class {@link Annotations.WriteEffect}. The tag consists of the
	 * package name, the name of the parent class and the class name itself. The
	 * generated tag can be used to identify a specific annotation in the jimple
	 * code.
	 * 
	 * @param cl
	 *            Class for which the annotation tag should be generated.
	 * @return Soot annotation tag for the given annotation class.
	 */
	public static String getSootAnnotationTag(Class<?> cl) {
		String packageName = cl.getPackage().getName();
		String parentClassName = cl.getDeclaringClass().getSimpleName();
		String className = cl.getSimpleName();
		return "L" + packageName + "/" + parentClassName + "$" + className
				+ ";";
	}

	/**
	 * List that contains the available <em>security levels</em>. The levels are
	 * added by the constructor
	 * {@link SecurityAnnotation#SecurityAnnotation(List)}.
	 */
	private final List<String> availableLevels;
	/**
	 * List that contains the names of the id functions. These function names
	 * consists of the <em>security level</em> and the id function suffix
	 * {@link SecurityLevel#SUFFIX_ID_METHOD}. The names are added by the
	 * constructor {@link SecurityAnnotation#SecurityAnnotation(List)}.
	 */
	private final List<String> idFunctionNames = new ArrayList<String>();

	/**
	 * Constructor for the {@link SecurityAnnotation} class. The constructor
	 * requires a ordered list of <em>security levels</em> which are defined by
	 * the {@code SootSecurityLevel} implementation of the developer. Only these
	 * given levels are considered as valid <em>security level</em> and the
	 * order of the given list defines the hierarchy of the
	 * <em>security levels</em>.
	 * 
	 * @param levels
	 *            Ordered list which contains all valid <em>security level</em>.
	 */
	public SecurityAnnotation(List<String> levels) {
		super();
		this.availableLevels = levels;
		for (String level : this.availableLevels) {
			this.idFunctionNames.add(level + SecurityLevel.SUFFIX_ID_METHOD);
		}
	}

	/**
	 * Checks whether the given <em>security level</em> is valid. Only if the
	 * list of available <em>security levels</em>
	 * {@link SecurityAnnotation#availableLevels} contains the given level, this
	 * level is valid.
	 * 
	 * @param level
	 *            Level, which should be checked whether it is valid or not.
	 * @return {@code true} if the given level is valid, i.e. if the list of
	 *         available levels contains the given level. Otherwise
	 *         {@code false}.
	 */
	public boolean checkValidityOfLevel(String level) {
		return availableLevels.contains(level);
	}

	/**
	 * Checks whether the given list of <em>security levels</em> contains only
	 * valid levels. Only if the list of available <em>security levels</em>
	 * {@link SecurityAnnotation#availableLevels} contains all the given levels,
	 * this given list of level is valid. <br />
	 * <u>Note: 'void' isn't a valid <em>security level</em>.</u>
	 * 
	 * @param levels
	 *            List of levels, which should be checked whether it contains
	 *            only valid <em>security levels</em> or not.
	 * @return {@code true} if the given list of levels is valid, i.e. if the
	 *         list of available levels contains all the given levels. Otherwise
	 *         {@code false}.
	 */
	public boolean checkValidityOfLevels(List<String> levels) {
		for (String level : levels) {
			if (!availableLevels.contains(level))
				return false;
		}
		return true;
	}

	/**
	 * Checks whether the given list of levels contains only valid levels or
	 * valid variable levels. Only if each level of the given list is contained
	 * by the list of available <em>security levels</em>
	 * {@link SecurityAnnotation#availableLevels} or it is an valid variable
	 * <em>security level</em>, this given list of parameter level is valid. The
	 * variable levels are only valid, if they start with the variable
	 * <em>security level</em> pattern character
	 * {@link SecurityAnnotation#LEVEL_PATTERN_SIGN} followed by a number in the
	 * range from 0 to {@code number of variable level - 1}. Each specific
	 * variable <em>security level</em> must be unique. <br />
	 * <u>Note: 'void' isn't a valid <em>security level</em>.</u>
	 * 
	 * @param paramLevels
	 *            List of parameter levels, which should be checked whether it
	 *            contains only valid parameter <em>security levels</em> or not.
	 * @return {@code true} if the given list of parameter levels is valid, i.e.
	 *         if the given list only contains levels which are contained by the
	 *         list of available levels or each level which is not contained has
	 *         to be a valid variable <em>security level</em>. Otherwise
	 *         {@code false}.
	 */
	public boolean checkValidityOfParameterLevels(List<String> paramLevels) {
		if (checkValidityOfLevels(paramLevels)) {
			return true;
		} else {
			List<String> invalids = getInvalidLevels(paramLevels);
			List<String> variable = new ArrayList<String>();
			for (String invalid : invalids) {
				if (!invalid.startsWith(LEVEL_PATTERN_SIGN)) {
					return false;
				} else {
					variable.add(invalid);
				}
			}
			for (int i = 0; i < variable.size(); i++) {
				if (!variable.contains(LEVEL_PATTERN_SIGN + i))
					return false;
			}
			return true;
		}
	}

	/**
	 * TODO: documentation
	 * 
	 * @param returnLevel
	 * @param methodParameterLevels
	 * @return
	 * @throws InvalidLevelException
	 * @throws InvalidEquationException
	 */
	public LevelEquationValidityVistitor checkValidityOfReturnLevel(
			String returnLevel, List<String> methodParameterLevels)
			throws InvalidLevelException, InvalidEquationException {
		List<String> allLevels = new ArrayList<String>(
				Arrays.asList(VOID_LEVEL));
		allLevels.addAll(availableLevels);
		for (String level : methodParameterLevels) {
			if (!allLevels.contains(level))
				allLevels.add(level);
		}
		LevelEquation equation = LevelEquationCreator.createFrom(returnLevel);
		LevelEquationValidityVistitor visitor = new LevelEquationValidityVistitor(
				equation, this);
		equation.accept(visitor);
		return visitor;
	}

	/**
	 * Returns the list of valid and available <em>security levels</em>
	 * {@link SecurityAnnotation#availableLevels}. The content of this list
	 * depends on the given argument of the constructor
	 * {@link SecurityAnnotation#SecurityAnnotation(List)}.
	 * 
	 * @return List which contains all valid <em>security levels</em>.
	 */
	public List<String> getAvailableLevels() {
		return availableLevels;
	}

	/**
	 * Returns a list which contains all levels of the given list that are
	 * invalid <em>security levels</em>, i.e. not contained by the list of
	 * available <em>security levels</em>
	 * {@link SecurityAnnotation#availableLevels}.
	 * 
	 * @param levels
	 *            List for which the invalid <em>security levels</em> should be
	 *            found.
	 * @return List which contains all invalid levels of the given level list.
	 * @see SecurityAnnotation#checkValidityOfLevel(String)
	 */
	public List<String> getInvalidLevels(List<String> levels) {
		List<String> invalid = new ArrayList<String>(levels);
		for (String level : levels) {
			if (checkValidityOfLevel(level))
				invalid.remove(level);
		}
		return invalid;
	}

	/**
	 * Returns a list which contains all parameter levels of the given parameter
	 * level list that are invalid parameter <em>security levels</em>, i.e. not
	 * contained by the list of available <em>security levels</em>
	 * {@link SecurityAnnotation#availableLevels} and each parameter level which
	 * isn't contained by the available level list is also an invalid variable
	 * <em>security level</em>.
	 * 
	 * @param paramLevels
	 *            List for which the invalid parameter <em>security levels</em>
	 *            should be found.
	 * @return List which contains all invalid parameter levels of the given
	 *         parameter level list.
	 * @see SecurityAnnotation#getInvalidLevels(List)
	 */
	public List<String> getInvalidParameterLevels(List<String> paramLevels) {
		List<String> invalids = getInvalidLevels(paramLevels);
		List<String> variable = new ArrayList<String>();
		for (String invalid : invalids) {
			if (invalid.startsWith(LEVEL_PATTERN_SIGN))
				variable.add(invalid);
		}
		for (int i = 0; i < variable.size(); i++) {
			String level = LEVEL_PATTERN_SIGN + i;
			if (variable.contains(level))
				invalids.remove(level);
		}
		return invalids;
	}

	/**
	 * TODO: documentation
	 * 
	 * @return
	 */
	public LevelEquationCalculateVoidVisitor getLevelEquationCalculateVoidVisitor() {
		return new LevelEquationCalculateVoidVisitor();
	}

	/**
	 * TODO: documentation
	 * 
	 * @param argumentLevels
	 * @param parameterLevels
	 * @return
	 */
	public LevelEquationEvaluationVisitor getLevelEquationEvaluationVisitor(
			List<String> argumentLevels, List<String> parameterLevels) {
		return new LevelEquationEvaluationVisitor(argumentLevels,
				parameterLevels, this);
	}

	/**
	 * TODO: documentation
	 * 
	 * @param levelEquation
	 * @return
	 */
	public LevelEquationValidityVistitor getLevelEquationValidityVistitor(
			LevelEquation levelEquation) {
		return new LevelEquationValidityVistitor(levelEquation, this);
	}

	/**
	 * Returns the strongest <em>security level</em> of the given level list. If
	 * one of the given levels is invalid or 'void' (
	 * {@link SecurityAnnotation#VOID_LEVEL}), the method will throw an
	 * exception. Otherwise the strongest level, i.e. the level with smallest
	 * index in the list of available levels
	 * {@link SecurityAnnotation#availableLevels} which is also in the given
	 * list, will be returned.
	 * 
	 * @param levels
	 *            List of <em>security levels</em> for which the strongest
	 *            contained level should be returned.
	 * @return If all given levels of the list are valid, the strongest
	 *         <em>security level</em> contained by the given list will be
	 *         returned.
	 * @throws InvalidLevelException
	 *             If one of the given levels isn't valid or 'void' (
	 *             {@link SecurityAnnotation#VOID_LEVEL}).
	 * @see SecurityAnnotation#checkValidityOfLevels(List)
	 */
	public String getMaxLevel(List<String> levels) throws InvalidLevelException {
		if (checkValidityOfLevels(levels)) {
			for (int i = 0; i < availableLevels.size(); i++) {
				if (levels.contains(availableLevels.get(i))) {
					return availableLevels.get(i);
				}
			}
			return getWeakestSecurityLevel();
		}
		throw new InvalidLevelException(
				SecurityMessages.invalidLevelsComparison(levels));
	}

	/**
	 * Returns the strongest <em>security level</em> of the given levels. If one
	 * of the two given levels is 'void' ({@link SecurityAnnotation#VOID_LEVEL}
	 * ), the resulting level is also 'void'. Otherwise the level with smallest
	 * index in the list of available levels
	 * {@link SecurityAnnotation#availableLevels} will be returned. The method
	 * will throw an exception if one of the given levels is not valid and also
	 * not 'void' ( {@link SecurityAnnotation#VOID_LEVEL}).
	 * 
	 * @param level1
	 *            First level which should be compared.
	 * @param level2
	 *            Second level which should be compared.
	 * @return If none of the given level is 'void' and invalid, the strongest
	 *         level of the both given levels will be returned. 'void' will be
	 *         returned, if one of the given levels is 'void' (
	 *         {@link SecurityAnnotation#VOID_LEVEL}).
	 * @throws InvalidLevelException
	 *             If one of the given levels isn't valid and also not 'void' (
	 *             {@link SecurityAnnotation#VOID_LEVEL}).
	 */
	public String getMaxLevel(String level1, String level2)
			throws InvalidLevelException {
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
			throw new InvalidLevelException(
					SecurityMessages.invalidLevelComparison(level1, level2));
		}
	}

	/**
	 * Returns the weakest <em>security level</em> of the given level list. If
	 * one of the given levels is invalid or 'void' (
	 * {@link SecurityAnnotation#VOID_LEVEL}), the method will throw an
	 * exception. Otherwise the weakest level, i.e. the level with the largest
	 * index in the list of available levels
	 * {@link SecurityAnnotation#availableLevels} which is also in the given
	 * list, will be returned.
	 * 
	 * @param levels
	 *            List of <em>security levels</em> for which the weakest
	 *            contained level should be returned.
	 * @return If all given levels of the list are valid, the weakest
	 *         <em>security level</em> contained by the given list will be
	 *         returned.
	 * @throws InvalidLevelException
	 *             If one of the given levels isn't valid or 'void' (
	 *             {@link SecurityAnnotation#VOID_LEVEL}).
	 * @see SecurityAnnotation#checkValidityOfLevels(List)
	 */
	public String getMinLevel(List<String> levels) throws InvalidLevelException {
		if (checkValidityOfLevels(levels)) {
			for (int i = availableLevels.size() - 1; i >= 0; i--) {
				if (levels.contains(availableLevels.get(i))) {
					return availableLevels.get(i);
				}
			}
			return getWeakestSecurityLevel();
		}
		throw new InvalidLevelException(
				SecurityMessages.invalidLevelsComparison(levels));
	}

	/**
	 * Returns the weakest <em>security level</em> of the given levels. If one
	 * of the two given levels is 'void' ({@link SecurityAnnotation#VOID_LEVEL}
	 * ), the resulting level is also 'void'. Otherwise the level with largest
	 * index in the list of available levels
	 * {@link SecurityAnnotation#availableLevels} will be returned. The method
	 * will throw an exception if one of the given levels is not valid and also
	 * not 'void' ( {@link SecurityAnnotation#VOID_LEVEL}).
	 * 
	 * @param level1
	 *            First level which should be compared.
	 * @param level2
	 *            Second level which should be compared.
	 * @return If none of the given level is 'void' and invalid, the weakest
	 *         level of the both given levels will be returned. 'void' will be
	 *         returned, if one of the given levels is 'void' (
	 *         {@link SecurityAnnotation#VOID_LEVEL}).
	 * @throws InvalidLevelException
	 *             If one of the given levels isn't valid and also not 'void' (
	 *             {@link SecurityAnnotation#VOID_LEVEL}).
	 */
	public String getMinLevel(String level1, String level2)
			throws InvalidLevelException {
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
			throw new InvalidLevelException(
					SecurityMessages.invalidLevelComparison(level1, level2));
		}
	}

	/**
	 * TODO: documentation
	 * 
	 * @param returnLevel
	 * @param methodParameterLevels
	 * @return
	 * @throws InvalidLevelException
	 * @throws InvalidEquationException
	 */
	public LevelEquation getReturnLevelEquation(String returnLevel,
			List<String> methodParameterLevels) throws InvalidLevelException,
			InvalidEquationException {
		List<String> allLevels = new ArrayList<String>(
				Arrays.asList(VOID_LEVEL));
		allLevels.addAll(availableLevels);
		for (String level : methodParameterLevels) {
			if (!allLevels.contains(level))
				allLevels.add(level);
		}
		LevelEquation equation = LevelEquationCreator.createFrom(returnLevel);
		return equation;
	}

	/**
	 * Generates for a given id function the <em>security level</em> of the
	 * returned value. If the given method isn't an id function, the weakest
	 * valid <em>security level</em> will be return.
	 * 
	 * @param sootMethod
	 *            SootMethod for which the return <em>security level</em> should
	 *            be generated.
	 * @return If the given SootMethod is a valid id function, the corresponding
	 *         <em>security level</em> will be returned. If the given SootMethod
	 *         is invalid, the generated return <em>security level</em> will be
	 *         the weakest level from the list of available levels
	 *         {@link SecurityAnnotation#availableLevels}.
	 * @see SecurityAnnotation#isIdFunction(SootMethod)
	 */
	public String getReturnSecurityLevelOfIdFunction(SootMethod sootMethod) {
		String methodName = sootMethod.getName();
		String level = getWeakestSecurityLevel();
		if (isIdFunction(sootMethod)) {
			level = methodName.substring(0, methodName.length() - 2);
		}
		return level;
	}

	/**
	 * Returns the weakest <em>security level</em> from the list of available
	 * and valid <em>security levels</em>
	 * {@link SecurityAnnotation#availableLevels}, i.e. the level with the index
	 * {@code number of levels - 1}.
	 * 
	 * @return The weakest valid <em>security level</em>.
	 */
	public String getWeakestSecurityLevel() {
		return availableLevels.get(availableLevels.size() - 1);
	}

	/**
	 * Returns the strongest <em>security level</em> from the list of available
	 * and valid <em>security levels</em>
	 * {@link SecurityAnnotation#availableLevels}, i.e. the level with the index
	 * {@code 0}.
	 * 
	 * @return The strongest valid <em>security level</em>.
	 */
	public String getStrongestSecurityLevel() {
		return availableLevels.get(0);
	}

	/**
	 * Checks whether the given method is an id function or not. It checks
	 * whether the package and the class name are correct and that a level
	 * corresponding to the method name exists. Also it checks whether the
	 * number of parameters is exactly one and whether it is a 'public static'
	 * method.
	 * 
	 * @param sootMethod
	 *            SootMethod for which should be checked whether it is an id
	 *            function or not.
	 * @return {@code true} if the given method is an id function, otherwise
	 *         {@code false}.
	 * @see Predefined#IMPL_SL_PATH_JAVA
	 * @see SecurityAnnotation#idFunctionNames
	 */
	public boolean isIdFunction(SootMethod sootMethod) {
		boolean packageValid = sootMethod.getDeclaringClass().getName()
				.equals(Predefined.IMPL_SL_PATH_JAVA);
		boolean methodNameValid = idFunctionNames
				.contains(sootMethod.getName());
		boolean parameterValid = sootMethod.getParameterCount() == 1;
		boolean methodModifierValid = sootMethod.isStatic()
				&& sootMethod.isPublic();
		return packageValid && methodNameValid && parameterValid
				&& methodModifierValid;
	}

	/**
	 * Checks whether the declaring class of the given method is the class which
	 * the developer has to implement (with the class name defined by
	 * {@link Predefined#IMPL_SL_PATH_JAVA}).
	 * 
	 * @param sootMethod
	 *            SootMethod for which should be checked whether it is a method
	 *            of the class with the class name
	 *            {@link Predefined#IMPL_SL_PATH_JAVA} or not.
	 * @return {@code true} if the given method is declared by the class with
	 *         the class name {@link Predefined#IMPL_SL_PATH_JAVA}, otherwise
	 *         {@code false}.
	 */
	public boolean isMethodOfSootSecurityLevelClass(SootMethod sootMethod) {
		return sootMethod.getDeclaringClass().getName()
				.equals(Predefined.IMPL_SL_PATH_JAVA);
	}

	/**
	 * Checks whether the first given level is weaker or equals than the second
	 * given level. If one of the both given levels is an invalid
	 * <em>security level</em> the method will throw an exception. I.e. the
	 * method returns {@code true} if the index of the first given level in the
	 * list of available levels {@link SecurityAnnotation#availableLevels} is
	 * greater or equals than the index of the second given level.
	 * 
	 * <h2>Example:</h2>
	 * 
	 * <pre>
	 * <code>
	 * isWeakerOrEqualsThan("low","high") == true;
	 * isWeakerOrEqualsThan("high","high") == true;
	 * isWeakerOrEqualsThan("high","low") == false;
	 * </code>
	 * </pre>
	 * 
	 * @param level1
	 *            Level for which should be checked whether it is weaker or
	 *            equals than the second given level.
	 * @param level2
	 *            Level for which should be checked whether it is stronger than
	 *            the first given level.
	 * @return {@code true} if the first given level is weaker or equals than
	 *         the second <em>security level</em>, {@code false} otherwise.
	 * @throws InvalidLevelException
	 *             Throws an exception if one of the given levels isn't a valid
	 *             <em>security level</em>.
	 * @see SecurityAnnotation#availableLevels
	 */
	public boolean isWeakerOrEqualsThan(String level1, String level2)
			throws InvalidLevelException {
		int indexLevel1 = availableLevels.indexOf(level1);
		int indexLevel2 = availableLevels.indexOf(level2);
		if (indexLevel1 >= 0 && indexLevel2 >= 0) {
			return indexLevel2 <= indexLevel1;
		}
		throw new InvalidLevelException(
				SecurityMessages.invalidLevelComparison(level1, level2));
	}

	/*
	 * ------------------------------------------------------------------------
	 */

	/**
	 * Checks whether the specified SootMethod has a ReturnSecurity annotation,
	 * no matter if the annotation contains a value or not.
	 * 
	 * @param sootMethod
	 *            The method for which should be checked whether it contains a
	 *            ReturnSecurity annotation.
	 * @return {@code true} only if the {@link ReturnSecurity} annotation is
	 *         present at the method, otherwise {@code false}. The result
	 *         doesn't indicate whether the annotation has also a value or not.
	 */
	public boolean hasReturnSecurityAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod,
				getSootAnnotationTag(Annotations.ReturnSecurity.class));
	}

	/**
	 * Checks whether the specified SootMethod has a ParameterSecurity
	 * annotation, no matter if the annotation contains a value or not.
	 * 
	 * @param sootMethod
	 *            The method for which should be checked whether it contains a
	 *            ParameterSecurity annotation.
	 * @return {@code true} only if the {@link ParameterSecurity} annotation is
	 *         present at the method, otherwise {@code false}. The result
	 *         doesn't indicate whether the annotation has also a value or not.
	 */
	public boolean hasParameterSecurityAnnotation(SootMethod sootMethod) {
		return hasAnnotationWithType(sootMethod,
				getSootAnnotationTag(Annotations.ParameterSecurity.class));
	}

	/**
	 * Checks whether the specified SootMethod has a WriteEffect annotation, no
	 * matter if the annotation contains a value or not.
	 * 
	 * @param sootMethod
	 *            The method for which should be checked whether it contains a
	 *            WriteEffect annotation.
	 * @return {@code true} only if the {@link WriteEffect} annotation is
	 *         present at the method, otherwise {@code false}. The result
	 *         doesn't indicate whether the annotation has also a value or not.
	 */
	public boolean hasMethodWriteEffectAnnotation(SootMethod sootMethod) {
		return hasWriteEffectAnnotation(sootMethod);
	}

	/**
	 * Checks whether the specified SootClass has a WriteEffect annotation, no
	 * matter if the annotation contains a value or not.
	 * 
	 * @param sootClass
	 *            The class for which should be checked whether it contains a
	 *            WriteEffect annotation.
	 * @return {@code true} only if the {@link WriteEffect} annotation is
	 *         present at the class, otherwise {@code false}. The result doesn't
	 *         indicate whether the annotation has also a value or not.
	 */
	public boolean hasClassWriteEffectAnnotation(SootClass sootClass) {
		return hasWriteEffectAnnotation(sootClass);
	}

	/**
	 * Checks whether the specified host has a WriteEffect annotation, no matter
	 * if the annotation contains a value or not.
	 * 
	 * @param host
	 *            The host for which should be checked whether it contains a
	 *            WriteEffect annotation.
	 * @return {@code true} only if the {@link WriteEffect} annotation is
	 *         present at the host, otherwise {@code false}. The result doesn't
	 *         indicate whether the annotation has also a value or not.
	 */
	private boolean hasWriteEffectAnnotation(Host host) {
		return hasAnnotationWithType(host,
				getSootAnnotationTag(Annotations.WriteEffect.class));
	}

	/**
	 * Checks whether the specified host has an annotation with the specified
	 * type, no matter if the annotation contains a value or not.
	 * 
	 * @param host
	 *            The host for which should be checked whether it contains such
	 *            an annotation.
	 * @param type
	 *            The type of the annotation for which should be checked whether
	 *            it is present at the host.
	 * @return {@code true} only if the annotation of the specified type is
	 *         present at the host, otherwise {@code false}. The result doesn't
	 *         indicate whether the annotation has also a value or not.
	 */
	private boolean hasAnnotationWithType(Host host, String type) {
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

	/**
	 * Checks whether the specified SootField has a FieldSecurity annotation, no
	 * matter if the annotation contains a value or not.
	 * 
	 * @param sootField
	 *            The field for which should be checked whether it contains a
	 *            FieldSecurity annotation.
	 * @return {@code true} only if the {@link FieldSecurity} annotation is
	 *         present at the field, otherwise {@code false}. The result doesn't
	 *         indicate whether the annotation has also a value or not.
	 */
	public boolean hasFieldSecurityAnnotation(SootField sootField) {
		return hasAnnotationWithType(sootField,
				getSootAnnotationTag(Annotations.FieldSecurity.class));
	}

	/**
	 * Checks whether the specified method has a <em>security level</em> as
	 * value of the corresponding annotation, i.e. not only an empty annotation.
	 * 
	 * @param sootMethod
	 *            {@link SootMethod} for which should be checked whether it has
	 *            a <em>security level</em>.
	 * @return {@code true} only if the specified method has the ReturnSecurity
	 *         annotation and if this annotation contains a value, otherwise
	 *         {@code false}.
	 */
	public boolean hasReturnSecurityLevel(SootMethod sootMethod) {
		if (hasReturnSecurityAnnotation(sootMethod)) {
			return hasHostAnnotationWithElementOfKind(sootMethod,
					getSootAnnotationTag(Annotations.ReturnSecurity.class),
					ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_STRING);
		}
		return false;
	}

	/**
	 * Checks whether the specified method has a list of
	 * <em>security levels</em> as value of the corresponding annotation, i.e.
	 * not only an empty annotation.
	 * 
	 * @param sootMethod
	 *            {@link SootMethod} for which should be checked whether it has
	 *            a list of <em>security levels</em>.
	 * @return {@code true} only if the specified method has the
	 *         ParameterSecurity annotation and if this annotation contains a
	 *         value, otherwise {@code false}.
	 */
	public boolean hasParameterSecurityLevels(SootMethod sootMethod) {
		if (hasParameterSecurityAnnotation(sootMethod)) {
			return hasHostAnnotationWithElementOfKind(sootMethod,
					getSootAnnotationTag(Annotations.ParameterSecurity.class),
					ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_ARRAY);
		}
		return false;
	}

	/**
	 * Checks whether the specified method has a list of <em>write effects</em>
	 * as value of the corresponding annotation, i.e. not only an empty
	 * annotation.
	 * 
	 * @param sootMethod
	 *            {@link SootMethod} for which should be checked whether it has
	 *            a list of <em>write effects</em>.
	 * @return {@code true} only if the specified method has the WriteEffect
	 *         annotation and if this annotation contains a value, otherwise
	 *         {@code false}.
	 */
	public boolean hasMethodWriteEffects(SootMethod sootMethod) {
		if (hasMethodWriteEffectAnnotation(sootMethod)) {
			return hasHostAnnotationWithElementOfKind(sootMethod,
					getSootAnnotationTag(Annotations.WriteEffect.class),
					ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_ARRAY);
		}
		return false;
	}

	/**
	 * Checks whether the specified class has a list of <em>write effects</em>
	 * as value of the corresponding annotation, i.e. not only an empty
	 * annotation.
	 * 
	 * @param sootClass
	 *            {@link SootClass} for which should be checked whether it has a
	 *            list of <em>write effects</em>.
	 * @return {@code true} only if the specified class has the WriteEffect
	 *         annotation and if this annotation contains a value, otherwise
	 *         {@code false}.
	 */
	public boolean hasClassWriteEffects(SootClass sootClass) {
		if (hasClassWriteEffectAnnotation(sootClass)) {
			return hasHostAnnotationWithElementOfKind(sootClass,
					getSootAnnotationTag(Annotations.WriteEffect.class),
					ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_ARRAY);
		}
		return false;
	}

	/**
	 * Checks whether the specified field has a <em>security level</em> as value
	 * of the corresponding annotation, i.e. not only an empty annotation.
	 * 
	 * @param sootField
	 *            {@link SootField} for which should be checked whether it has a
	 *            <em>security level</em>.
	 * @return {@code true} only if the specified class has the FieldSecurity
	 *         annotation and if this annotation contains a value, otherwise
	 *         {@code false}.
	 */
	public boolean hasFieldSecurityLevel(SootField sootField) {
		if (hasFieldSecurityAnnotation(sootField)) {
			return hasHostAnnotationWithElementOfKind(sootField,
					getSootAnnotationTag(Annotations.FieldSecurity.class),
					ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_STRING);
		}
		return false;
	}

	/**
	 * Checks whether the specified host has an annotation with the specified
	 * annotation type and this annotation also contains an element with the
	 * specified name and the specified kind.
	 * 
	 * @param host
	 *            Host for which should be checked whether it contains the
	 *            annotation including the element with the specified name and
	 *            type.
	 * @param annotationType
	 *            The type of the annotation.
	 * @param elementName
	 *            The name of the element.
	 * @param elementKind
	 *            The character indicating the kind of the element.
	 * @return {@code true} only if the specified host has an annotation of the
	 *         specified type and if this annotation contains a value mit the
	 *         specified name and the specified kind, otherwise {@code false}.
	 */
	private boolean hasHostAnnotationWithElementOfKind(Host host,
			String annotationType, String elementName, char elementKind) {
		try {
			VisibilityAnnotationTag tag = SootUtils
					.extractVisibilityAnnotationTag(host);
			AnnotationTag annotation = SootUtils.extractAnnotationTagWithType(
					tag, annotationType);
			for (int i = 0; i < annotation.getNumElems(); i++) {
				AnnotationElem element = annotation.getElemAt(i);
				if (element.getName().equals(elementName)
						&& element.getKind() == elementKind)
					return true;
			}
			return false;
		} catch (ExtractionException e) {
			return false;
		}
	}

	/**
	 * Extracts the return <em>security level</em> of the specified
	 * {@link SootMethod} if such a level exists, i.e. the
	 * {@link ReturnSecurity} annotation as well as a value are available,
	 * otherwise the method throws an exception.
	 * 
	 * @param sootMethod
	 *            The method from which the return <em>security level</em>
	 *            should be extracted.
	 * @return The return <em>security level</em> of the specified method if
	 *         such a level is available.
	 * @throws ExtractionException
	 *             If no return <em>security level</em> is present at the
	 *             specified {@link SootMethod}, then the method will throw a
	 *             exception.
	 */
	public String extractReturnSecurityLevel(SootMethod sootMethod)
			throws ExtractionException {
		AnnotationElem annotationElem = extractAnnotationElementFromHost(
				sootMethod,
				getSootAnnotationTag(Annotations.ReturnSecurity.class),
				ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_STRING);
		return SootUtils.convertAnnotationElementIntoString(annotationElem);
	}

	/**
	 * Extracts the parameter <em>security levels</em> of the specified
	 * {@link SootMethod} if such a levels exist, i.e. the
	 * {@link ParameterSecurity} annotation as well as a value are available,
	 * otherwise the method throws an exception.
	 * 
	 * @param sootMethod
	 *            The method from which the parameter <em>security levels</em>
	 *            should be extracted.
	 * @return The parameter <em>security levels</em> of the specified method as
	 *         a list of <em>security levels</em> if such a levels are
	 *         available.
	 * @throws ExtractionException
	 *             If no parameter <em>security levels</em> are present at the
	 *             specified {@link SootMethod}, then the method will throw a
	 *             exception.
	 */
	public List<String> extractParameterSecurityLevels(SootMethod sootMethod)
			throws ExtractionException {
		AnnotationElem annotationElem = extractAnnotationElementFromHost(
				sootMethod,
				getSootAnnotationTag(Annotations.ParameterSecurity.class),
				ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_ARRAY);
		return SootUtils.convertAnnotationElementIntoStringList(annotationElem);
	}

	/**
	 * Extracts the <em>write effects</em> of the specified {@link SootMethod}
	 * if such a effects exist, i.e. the {@link WriteEffect} annotation as well
	 * as a value are available, otherwise the method throws an exception.
	 * 
	 * @param sootMethod
	 *            The method from which the <em>write effects</em> should be
	 *            extracted.
	 * @return The <em>write effects</em> of the specified method as a list of
	 *         <em>write effects</em> if such a effects are available.
	 * @throws ExtractionException
	 *             If no <em>write effects</em> are present at the specified
	 *             {@link SootMethod}, then the method will throw a exception.
	 */
	public List<String> extractMethodEffects(SootMethod sootMethod)
			throws ExtractionException {
		AnnotationElem annotationElem = extractAnnotationElementFromHost(
				sootMethod,
				getSootAnnotationTag(Annotations.WriteEffect.class),
				ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_ARRAY);
		return SootUtils.convertAnnotationElementIntoStringList(annotationElem);
	}

	/**
	 * Extracts the <em>write effects</em> of the specified {@link SootClass} if
	 * such a effects exist, i.e. the {@link WriteEffect} annotation as well as
	 * a value are available, otherwise the method throws an exception.
	 * 
	 * @param sootClass
	 *            The class from which the <em>write effects</em> should be
	 *            extracted.
	 * @return The <em>write effects</em> of the specified class as a list of
	 *         <em>write effects</em> if such a effects are available.
	 * @throws ExtractionException
	 *             If no <em>write effects</em> are present at the specified
	 *             {@link SootClass}, then the method will throw a exception.
	 */
	public List<String> extractClassEffects(SootClass sootClass)
			throws ExtractionException {
		AnnotationElem annotationElem = extractAnnotationElementFromHost(
				sootClass, getSootAnnotationTag(Annotations.WriteEffect.class),
				ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_ARRAY);
		return SootUtils.convertAnnotationElementIntoStringList(annotationElem);
	}

	/**
	 * Extracts the <em>security level</em> of the specified {@link SootField}
	 * if such a level exists, i.e. the {@link FieldSecurity} annotation as well
	 * as a value are available, otherwise the method throws an exception.
	 * 
	 * @param sootField
	 *            The field from which the <em>security level</em> should be
	 *            extracted.
	 * @return The <em>security level</em> of the specified field if such a
	 *         level is available.
	 * @throws ExtractionException
	 *             If no <em>security level</em> is present at the specified
	 *             {@link SootField}, then the method will throw a exception.
	 */
	public String extractFieldSecurityLevel(SootField sootField)
			throws ExtractionException {
		AnnotationElem annotationElem = extractAnnotationElementFromHost(
				sootField,
				getSootAnnotationTag(Annotations.FieldSecurity.class),
				ANNOTATION_ELEMENT_NAME, SootUtils.ELEMENT_KIND_STRING);
		return SootUtils.convertAnnotationElementIntoString(annotationElem);
	}

	/**
	 * Returns the annotation element with the specified name and kind from the
	 * annotation with the specified type if such an element and annotation is
	 * present at the specified host. Otherwise the method will throw an
	 * exception.
	 * 
	 * @param host
	 *            Host from which the annotation element should be extracted.
	 * @param annotationType
	 *            Type of the annotation which contains the element that should
	 *            be extracted.
	 * @param elementName
	 *            Name of the element which should be extracted.
	 * @return If exists, the annotation element with the specified name and
	 *         kind from the annotation with the specified type.
	 * @throws ExtractionException
	 *             If the element can't be extracted the method throws an
	 *             exception, this is because the no annotation of the specified
	 *             type is available or because no element with the specified
	 *             name and kind is available.
	 */
	private AnnotationElem extractAnnotationElementFromHost(Host host,
			String annotationType, String elementName, char elementKind)
			throws ExtractionException {
		VisibilityAnnotationTag tag = SootUtils
				.extractVisibilityAnnotationTag(host);
		AnnotationTag annotation = SootUtils.extractAnnotationTagWithType(tag,
				annotationType);
		return SootUtils.extractAnnotationElemWithNameAndKind(annotation,
				elementName, elementKind);
	}

	/**
	 * Interface for receiving the <em>write effects</em> of a library class,
	 * i.e. this method looks up (or calculates, guesses, ...) the
	 * <em>write effects</em> of the specified class and returns them in a list.
	 * 
	 * @param sootClass
	 *            Library class for which the <em>write effects</em> should be
	 *            returned.
	 * @return List of the <em>write effects</em> of the specified library
	 *         class.
	 */
	public List<String> getLibraryClassWriteEffects(SootClass sootClass) {
		List<String> writeEffects = new ArrayList<String>();
		return writeEffects;
	}

	/**
	 * Interface for receiving the <em>security level</em> of a library field,
	 * i.e. this method looks up (or calculates, guesses, ...) the
	 * <em>security level</em> of the specified field and returns it.
	 * 
	 * @param sootField
	 *            Library field for which the field <em>security level</em>
	 *            should be returned.
	 * @return The field <em>security level</em> of the specified library field.
	 */
	public String getLibraryFieldSecurityLevel(SootField sootField) {
		return getWeakestSecurityLevel();
	}

	/**
	 * Interface for receiving the parameter <em>security levels</em> of a
	 * library method, i.e. this method looks up (or calculates, guesses, ...)
	 * the parameter <em>security levels</em> of the specified method and returns them as
	 * a ordered list.
	 * 
	 * @param sootMethod
	 *            Library method for which the parameter
	 *            <em>security levels</em> should be returned.
	 * @return The parameter <em>security levels</em> of the specified method as
	 *         a ordered list of <em>security levels</em>.
	 */
	public List<String> getLibraryParameterSecurityLevel(SootMethod sootMethod) {
		List<String> levels = new ArrayList<String>();
		for (int i = 0; i < sootMethod.getParameterCount(); i++) {
			levels.add(getStrongestSecurityLevel());
		}
		return levels;
	}

	/**
	 * Interface for receiving the return <em>security levels</em> of a
	 * library method, i.e. this method looks up (or calculates, guesses, ...)
	 * the return <em>security level</em> of the specified method and returns it.
	 * 
	 * @param sootMethod
	 *            Library method for which the return
	 *            <em>security level</em> should be returned.
	 * @param parameterLevels 
	 * @return The return <em>security level</em> of the specified method.
	 */
	public String getLibraryReturnSecurityLevel(SootMethod sootMethod, List<String> levels) {
		return getMaxLevel(levels);
	}

	/**
	 * 
	 * @return
	 */
	public String getDefaultVariableSecurityLevel() {
		return getWeakestSecurityLevel();
	}

	// TODO
	public List<String> getLibraryWriteEffects(SootMethod sootMethod) {
		List<String> writeEffects = new ArrayList<String>();
		return writeEffects;
	}

	/*
	 * ------------------------------------------------------------------------
	 */
}