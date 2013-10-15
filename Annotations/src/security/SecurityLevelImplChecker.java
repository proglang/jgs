package security;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logging.SecurityLogger;
import security.Annotations.ReturnSecurity;
import exception.SootException.SecurityLevelException;
import utils.Predefined;
import utils.SecurityMessages;

/**
 * <h1>Validity checker for {@code SecurityLevel} implementations</h1>
 * 
 * <h2>Checking an implementation</h2>
 * 
 * For checking an implementation, just invoke the constructor
 * {@link SecurityLevelImplChecker#SecurityLevelImplChecker(SecurityLevel)} with an instance of the
 * implementation. All violations against the guidelines below will be printed to the console.
 * 
 * <h2>Class description</h2>
 * 
 * The {@link SecurityLevelImplChecker} class provides the possibility to check a subclass
 * implementation of the class {@link SecurityLevel}. Also, the {@link SecurityLevelImplChecker}
 * provides information about missing id functions, missing annotations and invalid levels. But
 * these information aren't accessible from outside the package. <b>Note: the
 * {@link SecurityLevelImplChecker} doesn't consider the correct class name and correct package! But
 * the analysis will not find the implementation of the developer if the class name and package
 * aren't correct.</b>
 * 
 * <h2>Validity guidelines</h2>
 * 
 * An implementation of the class {@link SecurityLevel} is only valid, if:
 * <ul>
 * <li>The implementation extends the class {@link SecurityLevel}.</li>
 * <li>The class name is 'SootSecurityLevel' and the package is 'security' (see
 * {@link Predefined#IMPL_SL_PATH_JAVA}) <b>[the {@link SecurityLevelImplChecker} doesn't consider
 * the correct class name and correct package! But the analysis will not find the implementation of
 * the developer if the class name and package aren't correct.]</b></li>
 * <li>The class implements the abstract method {@link SecurityLevel#getOrderedSecurityLevels()}:
 * <ul>
 * <li>The method returns a list of at least 2 <em>security levels</em> as String.</li>
 * <li>The returned list is ordered, i.e. the strongest <em>security level</em> has the smallest
 * index and the weakest <em>security level</em> has the greatest index in the returned list.</li>
 * <li>Each <em>security level</em> provided by the returned list is valid, i.e. contains none of
 * the characters '{@code *}', '{@code (}', '{@code )}' and '{@code ,}' as well as none of the
 * provided <em>security levels</em> is equals to the internal '{@code void}' non return
 * <em>security level</em> (see {@link SecurityLevel#LEVEL_VOID}).</li>
 * 
 * <h3>Example:</h3>
 * 
 * <pre>
 * <code>
 * &#064Override 
 * public String[] getOrderedSecurityLevels() {
 * 	return new String[] {"high", "normal", "low"};
 * }
 * </code>
 * </pre>
 * 
 * </ul>
 * </li>
 * <li>For each provided <em>security level</em> by the method
 * {@link SecurityLevel#getOrderedSecurityLevels()} an id function exists in the implemented class:
 * <ul>
 * <li>Each id function is {@code public} and {@code static}.</li>
 * <li>Each id function takes a object of type {@code T} and returns this object with the same type
 * {@code T}.</li>
 * <li>The name of such an id function starts with the corresponding <em>security level</em> name
 * and ends with the suffix 'Id' (see {@link SecurityLevel#SUFFIX_ID_METHOD}), e.g. for level 'high'
 * the id function name will be {@code highId}.</li>
 * <li>Each id function is annotated with the corresponding return <em>security level</em>, e.g. for
 * level 'high' the id function is annotated with {@link ReturnSecurity} and the value {@code high}.
 * </li>
 * 
 * <h3>Example:</h3>
 * 
 * <pre>
 * <code>
 * &#064;ReturnSecurity(&quot;high&quot;)
 * public static &lt;T&gt; T highId(T obj) {
 * 	return obj;
 * }
 * </code>
 * </pre>
 * 
 * </ul>
 * </li>
 * </ul>
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class SecurityLevelImplChecker {

	/**
	 * Defines the correct class path of the class which the developer has to implement. This
	 * implementation should be located in the package 'security' and should be called
	 * 'SootSecurityLevel'.
	 * 
	 * @see Predefined#IMPL_SL_PATH_FILE
	 */
	protected static final String CLASS_PATH = Predefined.IMPL_SL_PATH_FILE + ".class";
	/** The signature prefix of the id functions. */
	protected static final String ID_SIGNATURE_PREFIX = "public static <T> T ";
	/**
	 * Defines the correct path of the java source file which the developer has to implement. This
	 * implementation should be located in the package 'security' and should be called
	 * 'SootSecurityLevel'.
	 * 
	 * @see Predefined#IMPL_SL_PATH_FILE
	 */
	protected static final String JAVA_PATH = Predefined.IMPL_SL_PATH_FILE + ".java";
	/**
	 * Name of the method which returns the ordered list of <em>security levels</em>
	 * {@link SecurityLevel#getOrderedSecurityLevels()} and which the developer has to implement in
	 * his subclass of {@link SecurityLevel}.
	 */
	protected static final String ORDERED_LIST_METHOD = "getOrderedSecurityLevels";
	/** Signature of the method which returns the ordered list of <em>security levels</em>. */
	protected static final String ORDERED_LIST_SIGNATURE = "public String[] " + ORDERED_LIST_METHOD
			+ "()";
	/**
	 * List which stores illegal <em>security level</em> names. During the check such illegal names
	 * will be added.
	 */
	private List<String> illegalLevelNames = new ArrayList<String>();
	/**
	 * List which stores the names of id functions with invalid annotations. During the check such
	 * names of id functions with invalid annotations will be added.
	 */
	private List<String> invalidIdFunctionAnnotation = new ArrayList<String>();
	/**
	 * List which stores the names of invalid id functions. During the check such names of invalid
	 * id functions will be added.
	 */
	private List<String> invalidIdFunctions = new ArrayList<String>();
	/**
	 * Indicates whether the method which returns the ordered list of <em>security levels</em> is
	 * available or not. This value will be adapted during the check of the implementation.
	 */
	private boolean orderedLevelMethodAvailable = false;
	/**
	 * Indicates whether the method which returns the ordered list of <em>security levels</em> is
	 * correct or not. This value will be adapted during the check of the implementation.
	 */
	private boolean orderedLevelMethodCorrect = false;
	/**
	 * Stores the ordered list of <em>security levels</em> which is returned by the implementation
	 * of the method {@link SecurityLevel#getOrderedSecurityLevels()}.
	 */
	private String[] orderLevels = new String[] {};
	/**
	 * Indicates whether the error and warning messages should be printed by the logger or the
	 * output streams.
	 */
	private boolean shouldLog = true;
	/**
	 * List which contains the id function names for which the annotation of the id function is not
	 * available. During the check such levels of unavailable annotation of id functions will be
	 * added.
	 */
	private List<String> unavailableIdFunctionAnnotation = new ArrayList<String>();
	/**
	 * List which contains the id function names for which the id function is not available. During
	 * the check such levels of unavailable id functions will be added.
	 */
	private List<String> unavailableIdFunctions = new ArrayList<String>();
	/**
	 * Logger which prints formatted messages of different types to the console and depending on the
	 * settings to file, too. If this field is {@code null} during the check, all messages will be
	 * printed by the standard and the error output stream.
	 */
	protected SecurityLogger log = null;

	/**
	 * Constructor of the class {@link SecurityLevelImplChecker} which checks the given instance of
	 * a subclass of the class {@link SecurityLevel}. By invoking this constructor all violations
	 * against the guidelines of an implementation of a subclass of {@link SecurityLevel} will be
	 * printed to the console. Also, if the implementation is not valid the constructor will throw
	 * an exception.
	 * 
	 * @param impl
	 *            Instance of a {@link SecurityLevel} subclass, which should be check whether this
	 *            subclass satisfies the guidelines of a valid implementation of
	 *            {@link SecurityLevel} subclasses.
	 * @throws SecurityLevelException
	 *             Throws an exception if the class of the given instance violates the guidelines of
	 *             valid implementation of {@link SecurityLevel} subclasses.
	 */
	public SecurityLevelImplChecker(SecurityLevel impl) throws SecurityLevelException {
		super();
		if (!checkValidityOfImplementation(impl))
			throw new SecurityLevelException(
					SecurityMessages.reflectionInvalidSootSecurityLevelClass());
	}

	/**
	 * Protected constructor of the class {@link SecurityLevelImplChecker} which checks the given
	 * instance of a subclass of the class {@link SecurityLevel}. By invoking this constructor all
	 * violations against the guidelines of an implementation of a subclass of {@link SecurityLevel}
	 * will be printed to the given logger (if it is {@code null}, the standard and error output
	 * stream will be used). The printing itself depends on the given flag 'shouldLog', too. Also,
	 * if the implementation is not valid and the flag 'throwException' is enabled, the constructor
	 * will throw an exception.
	 * 
	 * @param log
	 *            {@link SecurityLogger} which should print the warnings and errors that occur
	 *            during the check of the class of the given instance.
	 * @param impl
	 *            Instance of a {@link SecurityLevel} subclass, which should be check whether this
	 *            subclass satisfies the guidelines of a valid implementation of
	 *            {@link SecurityLevel} subclasses.
	 * @param shouldLog
	 *            Indicates whether warnings and errors should be printed ({@code true}), or should
	 *            not be printed ({@code false}). This value dis- and enables the printing to the
	 *            logger as well as the printing to the standard and error output stream.
	 * @param throwException
	 *            Indicates whether an exception should be thrown if the implementation isn't valid
	 *            ({@code true}), or no exception should be thrown ({@code false}).
	 * @throws SecurityLevelException
	 *             Throws an exception if the class of the given instance violates the guidelines of
	 *             valid implementation of {@link SecurityLevel} subclasses and if the given
	 *             argument 'throwException' is {@code true}.
	 */
	protected SecurityLevelImplChecker(SecurityLogger log, SecurityLevel impl, boolean shouldLog,
			boolean throwException) throws SecurityLevelException {
		super();
		this.shouldLog = shouldLog;
		if (shouldLog) {
			this.log = log;
		}
		if (!checkValidityOfImplementation(impl)) {
			if (throwException) {
				throw new SecurityLevelException(
						SecurityMessages.reflectionInvalidSootSecurityLevelClass());
			}
		}
	}

	/**
	 * Returns the ordered <em>security level</em> list which is given by the implementation of the
	 * {@link SecurityLevel} subclass. Note that this list can also include invalid
	 * <em>security levels</em>, because all levels given by the implementation are referenced by
	 * the returned list.
	 * 
	 * @return The ordered <em>security level</em> list of the implemented {@link SecurityLevel}
	 *         subclass.
	 */
	public String[] getOrderedLevels() {
		return orderLevels;
	}

	/**
	 * Checks whether the given method name has similarities to an id function, but the given levels
	 * didn't contain an level which has similarities to the method name. The given class should be
	 * the class which declares the method. If the given method has clear indications (method ends
	 * with 'id', only one parameter, is static and is public) and no level corresponding to the
	 * method name exists, then a warning will be printed.
	 * 
	 * @param subClass
	 *            Class which declares the method with the given method name.
	 * @param levels
	 *            All available <em>security levels</em> of this implementation.
	 * @param methodName
	 *            Name of the method for which should be check whether it has similarities to an id
	 *            function.
	 */
	private void checkForPossibleMistakes(Class<?> subClass, List<String> levels, String methodName) {
		if ((!levels.contains(methodName.substring(0, methodName.length() - 2)))
				&& methodName.endsWith(SecurityLevel.SUFFIX_ID_METHOD)) {
			try {
				Method method = subClass.getMethod(methodName, Object.class);
				Class<?>[] parameters = method.getParameterTypes();
				Class<?> returnType = method.getReturnType();
				boolean isStatic = Modifier.isStatic(method.getModifiers());
				boolean isPublic = Modifier.isPublic(method.getModifiers());
				if (parameters.length == 1 && parameters[0].equals(Object.class)
						&& returnType.equals(Object.class) && isStatic && isPublic) {
					printWarning(SecurityMessages.reflectionPossibleMistake(ID_SIGNATURE_PREFIX
							+ methodName + "(T)"));
				}
			} catch (NoSuchMethodException | SecurityException e) {
				printWarning(SecurityMessages.reflectionNoMethodAccess(methodName));
			}
		}
	}

	/**
	 * Method checks whether the given class contains a valid id function for the given level. If
	 * this is the case the method also checks whether the id function is annotated with a valid
	 * return <em>security level</em> annotation or not. If the id function is not accessible or
	 * {@code static} and if the annotation is not available or invalid this method will print an
	 * error.
	 * 
	 * @param sootSecurityLevelClass
	 *            Class for which should be checked whether the id function for the given
	 *            <em>security level</em> is valid. This class should be a subclass of the class
	 *            {@link SecurityLevel}.
	 * @param level
	 *            <em>Security level</em> for which the id function should be checked whether the
	 *            given class contains such an id function and whether this function is also valid.
	 * @return {@code true} if the given class contains a valid id function as well as a valid
	 *         return <em>security level</em> annotation for the given level.
	 */
	private boolean checkValidityOfIdFunction(Class<?> sootSecurityLevelClass, String level) {
		boolean valid = true;
		String signatureIdFunction = ID_SIGNATURE_PREFIX + level + SecurityLevel.SUFFIX_ID_METHOD
				+ "(T)";
		try {
			Method method = sootSecurityLevelClass.getMethod(
					level + SecurityLevel.SUFFIX_ID_METHOD, Object.class);
			if (!Modifier.isStatic(method.getModifiers())) {
				printException(SecurityMessages.reflectionNonStaticMethod("'public <T> T " + level
						+ SecurityLevel.SUFFIX_ID_METHOD + "(T)'"));
				invalidIdFunctions.add(level + SecurityLevel.SUFFIX_ID_METHOD);
				valid = false;
			}
			boolean exists = false;
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation.annotationType().equals(Annotations.ReturnSecurity.class)) {
					exists = true;
					Annotations.ReturnSecurity security = (Annotations.ReturnSecurity) annotation;
					if (!security.value().equals(level)) {
						printException(SecurityMessages
								.reflectionInvalidMethodAnnotation(signatureIdFunction));
						invalidIdFunctionAnnotation.add(level + SecurityLevel.SUFFIX_ID_METHOD);
						valid = false;
					}
				}
			}
			if (!exists) {
				printException(SecurityMessages.reflectionNoMethodAnnotation(signatureIdFunction));
				unavailableIdFunctionAnnotation.add(level + SecurityLevel.SUFFIX_ID_METHOD);
				valid = false;
			}
		} catch (Exception e) {
			printException(SecurityMessages.reflectionNoMethodAccess(signatureIdFunction));
			unavailableIdFunctions.add(level + SecurityLevel.SUFFIX_ID_METHOD);
			valid = false;
		}
		return valid;
	}

	/**
	 * Method combines multiple checks which should be done to distinguish whether the given
	 * implementation is valid or not. If the method which returns the ordered
	 * <em>security level</em> list doesn't exist, the check will not be continued.
	 * 
	 * @param impl
	 *            Instance of a {@link SecurityLevel} subclass, which should be check whether this
	 *            subclass satisfies the guidelines of a valid implementation of
	 *            {@link SecurityLevel} subclasses.
	 * @return {@code true} if the implementation is valid, {@code false} otherwise.
	 * @see SecurityLevelImplChecker#checkValidityOfOrderedListMethod(SecurityLevel, Class)
	 * @see SecurityLevelImplChecker#checkValidityOfLevels(Class)
	 */
	private boolean checkValidityOfImplementation(SecurityLevel impl) {
		Class<?> sootSecurityLevelClass = impl.getClass();
		return checkValidityOfOrderedListMethod(impl, sootSecurityLevelClass)
				&& checkValidityOfLevels(sootSecurityLevelClass);
	}

	/**
	 * Checks whether the given level is valid, i.e. has a valid id function, has a valid return
	 * <em>security level</em> annotation (see
	 * {@link SecurityLevelImplChecker#checkValidityOfIdFunction(Class, String)}), the level is not
	 * equals the '{@code void}' return <em>security level</em> and the level doesn't contain an
	 * invalid character such as '{@code *}', '{@code (}', '{@code )}' and '{@code ,}'. In every
	 * exceptional case the method will print an error.
	 * 
	 * @param sootSecurityLevelClass
	 *            Class for which should be checked whether it contains an id function for the given
	 *            <em>security level</em> and whether this function is valid. This class should be a
	 *            subclass of the class {@link SecurityLevel}.
	 * @param methodNames
	 *            List containing all available method names of the given class.
	 * @param level
	 *            <em>Security level</em> which should be check and for which the id function should
	 *            be checked whether the given class contains such an id function and whether this
	 *            function is also valid.
	 * @return {@code true} if the given class contains a valid id function as well as a valid
	 *         return <em>security level</em> annotation for the given level and if the level itself
	 *         is valid.
	 * @see SecurityLevelImplChecker#checkValidityOfIdFunction(Class, String)
	 */
	private boolean checkValidityOfLevel(Class<?> sootSecurityLevelClass, List<String> methodNames,
			String level) {
		boolean valid = true;
		if (SecurityAnnotation.ADDITIONAL_LEVELS.contains(level)) {
			printException(SecurityMessages.reflectionIllegalLevelName(level));
			illegalLevelNames.add(level);
			valid = false;
		}
		if (level.contains("(") || level.contains(")")) {
			printException(SecurityMessages.reflectionIllegalLevelSign(level, "'(' or ')'",
					"level equations"));
			illegalLevelNames.add(level);
			valid = false;
		}
		if (level.contains(",")) {
			printException(SecurityMessages.reflectionIllegalLevelSign(level, "','",
					"level equations"));
			illegalLevelNames.add(level);
			valid = false;
		}
		if (level.contains("*")) {
			printException(SecurityMessages.reflectionIllegalLevelSign(level, "'*'",
					"variable levels in level equations"));
			illegalLevelNames.add(level);
			valid = false;
		}
		if (!methodNames.contains(level + SecurityLevel.SUFFIX_ID_METHOD)) {
			printException(SecurityMessages.reflectionNoMethod(ID_SIGNATURE_PREFIX + level
					+ SecurityLevel.SUFFIX_ID_METHOD + "(T)"));
			unavailableIdFunctions.add(level + SecurityLevel.SUFFIX_ID_METHOD);
			valid = false;
		} else {
			boolean result = checkValidityOfIdFunction(sootSecurityLevelClass, level);
			valid = valid && result;
		}
		return valid;
	}

	/**
	 * Method checks whether the available <em>security levels</em> are valid. I.e. at least 2
	 * <em>security levels</em> are defined and each level is valid (see
	 * {@link SecurityLevelImplChecker#checkValidityOfLevel(Class, List, String)}). In the case of
	 * an error, this error will be printed. Note: this method requires that the method
	 * {@link SecurityLevelImplChecker#checkValidityOfOrderedListMethod(SecurityLevel, Class)} was
	 * invoked previously.
	 * 
	 * @param sootSecurityLevelClass
	 *            Class for which the provided levels should be checked for validity and which is a
	 *            subclass of the class {@link SecurityLevel}.
	 * @return {@code true} if at least 2 <em>security levels</em> are defined and all defined
	 *         levels are valid. Otherwise {@code false}.
	 * @see SecurityLevelImplChecker#checkValidityOfLevel(Class, List, String)
	 * @see SecurityLevelImplChecker#checkForPossibleMistakes(Class, List, String)
	 * @see SecurityLevelImplChecker#checkValidityOfOrderedListMethod(SecurityLevel, Class)
	 */
	private boolean checkValidityOfLevels(Class<?> sootSecurityLevelClass) {
		if (orderLevels == null || orderLevels.length <= 1) {
			printException(SecurityMessages.reflectionNoLevels(ORDERED_LIST_SIGNATURE));
			return false;
		} else {
			orderedLevelMethodCorrect = true;
			Method[] methodsArray = sootSecurityLevelClass.getDeclaredMethods();
			boolean valid = true;
			List<String> levels = new ArrayList<String>(Arrays.asList(orderLevels));
			List<String> methodNames = new ArrayList<String>();
			for (Method method : methodsArray) {
				methodNames.add(method.getName());
			}
			for (String level : levels) {
				boolean result = checkValidityOfLevel(sootSecurityLevelClass, methodNames, level);
				valid = valid && result;
			}
			for (String methodName : methodNames) {
				checkForPossibleMistakes(sootSecurityLevelClass, levels, methodName);
			}
			return valid;
		}
	}

	/**
	 * Checks whether the given {@link SecurityLevel} subclass implementation contains also an
	 * implementation of the abstract method {@link SecurityLevel#getOrderedSecurityLevels()}. If
	 * this is the case, the method will be invoked and the result will be stored to
	 * {@link SecurityLevelImplChecker#orderLevels}. Otherwise an error will be printed.
	 * 
	 * @param impl
	 *            Instance of a {@link SecurityLevel} subclass, for which should be checked whether
	 *            it contains an implementation of the method
	 *            {@link SecurityLevel#getOrderedSecurityLevels()}.
	 * @param sootSecurityLevelClass
	 *            Class of the given instance which is a subclass of the class {@link SecurityLevel}
	 *            .
	 * @return {@code true} if the given {@link SecurityLevel} subclass implements the method which
	 *         returns the ordered list of <em>security levels</em>. Otherwise {@code false}.
	 */
	private boolean checkValidityOfOrderedListMethod(SecurityLevel impl,
			Class<?> sootSecurityLevelClass) {
		try {
			Method orderLevelMethod = sootSecurityLevelClass.getDeclaredMethod(ORDERED_LIST_METHOD);
			orderLevels = (String[]) orderLevelMethod.invoke(impl);
			orderedLevelMethodAvailable = true;
			return true;
		} catch (NoSuchMethodException | SecurityException e) {
			printException(e, SecurityMessages.reflectionNoMethod(ORDERED_LIST_SIGNATURE));
			return false;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			printException(e, SecurityMessages.reflectionNoMethodAccess(ORDERED_LIST_SIGNATURE));
			return false;
		}
	}

	/**
	 * Prints the given message. Depending on whether a logger {@link SecurityLevelImplChecker#log}
	 * exists, the message as well as the exception are printed as a security checker error by the
	 * logger {@link SecurityLogger#securitychecker(String)} or if no logger exists only the message
	 * is printed by the standard error output stream {@link System#err}. The printing depends also
	 * on the value of {@link SecurityLevelImplChecker#shouldLog}.
	 * 
	 * @param e
	 *            The exception that triggered the printing of an error.
	 * @param msg
	 *            The message which should be printed as error.
	 */
	private void printException(Exception e, String msg) {
		if (shouldLog) {
			if (this.log != null) {
				log.securitychecker(msg, e);
			} else {
				System.err.println(msg);
			}
		}
	}

	/**
	 * Prints the given message. Depending on whether a logger {@link SecurityLevelImplChecker#log}
	 * exists, the message is printed as a security checker error by the logger
	 * {@link SecurityLogger#securitychecker(String)} or by the standard error output stream
	 * {@link System#err}.The printing depends also on the value of
	 * {@link SecurityLevelImplChecker#shouldLog}.
	 * 
	 * @param msg
	 *            The message which should be printed as error.
	 */
	private void printException(String msg) {
		if (shouldLog) {
			if (this.log != null) {
				log.securitychecker(msg);
			} else {
				System.err.println(msg);
			}
		}
	}

	/**
	 * Prints the given message. Depending on whether a logger {@link SecurityLevelImplChecker#log}
	 * exists, the message is printed as a warning by the logger
	 * {@link SecurityLogger#warning(String, long, String)} or by the standard output stream
	 * {@link System#out}. The printing depends also on the value of
	 * {@link SecurityLevelImplChecker#shouldLog}.
	 * 
	 * @param msg
	 *            The message which should be printed as warning.
	 */
	private void printWarning(String msg) {
		if (shouldLog) {
			if (this.log != null) {
				log.warning("SootSecurityLevel", 0, msg);
			} else {
				System.out.println(msg);
			}
		}
	}

	/**
	 * Returns the list which contains all invalid <em>security levels</em>. I.e. all the levels
	 * which are given by the implementation of the {@link SecurityLevel} subclass and which are
	 * invalid. A <em>security level</em> is invalid if it is equals to the '{@code void}' return
	 * <em>security level</em> or if it contains at least one of the characters '{@code *}', '
	 * {@code (}', '{@code )}' or '{@code ,}'.
	 * 
	 * @return List of invalid <em>security levels</em> which are defined by the implemented
	 *         {@link SecurityLevel} subclass.
	 */
	protected List<String> getIllegalLevelNames() {
		return illegalLevelNames;
	}

	/**
	 * Returns the list which contains all method names of id functions which have an invalid return
	 * <em>security level</em> annotation. Note that this list includes only those method names
	 * which exist and which are valid id functions.
	 * 
	 * @return List containing method names of id function with invalid return
	 *         <em>security level</em> annotation.
	 */
	protected List<String> getInvalidIdFunctionAnnotation() {
		return invalidIdFunctionAnnotation;
	}

	/**
	 * Returns the list which contains all method names of id functions which are invalid. I.e. the
	 * list contains the id function which are not {@code static}. Note that non {@code public} id
	 * functions are not contained by the resulting list.
	 * 
	 * @return List containing the method names of invalid id functions.
	 */
	protected List<String> getInvalidIdFunctions() {
		return invalidIdFunctions;
	}

	/**
	 * Returns the list which contains all method names of id functions which do not have a return
	 * <em>security level</em> annotation. Note that this list includes only those method names
	 * which exist and which are valid id functions.
	 * 
	 * @return List containing the method names of id function with unavailable return
	 *         <em>security level</em> annotation.
	 */
	protected List<String> getUnavailableIdFunctionAnnotation() {
		return unavailableIdFunctionAnnotation;
	}

	/**
	 * Returns the list which contains all method names of id functions which not exist. I.e. for
	 * all defined <em>security levels</em> for which no method exist with the name {@code levelId}
	 * or for which the method isn't {@code public}. Note that non {@code static} id functions are
	 * not included by this list.
	 * 
	 * @return List containing the method names of unavailable id functions.
	 */
	protected List<String> getUnavailableIdFunctions() {
		return unavailableIdFunctions;
	}

	/**
	 * Indicates whether the implementation of {@link SecurityLevel#getOrderedSecurityLevels()} is
	 * available or not. The case that this method will return {@code false} is implausible, because
	 * a subclass has to implement the abstract methods of the super class.
	 * 
	 * @return {@code true} if the implementation of the method which returns the ordered
	 *         <em>security level</em> list is available. Otherwise {@code false}.
	 */
	protected boolean isOrderedLevelMethodAvailable() {
		return orderedLevelMethodAvailable;
	}

	/**
	 * Indicates whether the implementation of {@link SecurityLevel#getOrderedSecurityLevels()} is
	 * correct or not. If it isn't correct, the method returns less than 2 <em>security levels</em>
	 * or {@code null}.
	 * 
	 * @return {@code true} if the implementation of the method which returns the ordered
	 *         <em>security level</em> list is correct, i.e. it returns at least 2
	 *         <em>security levels</em>. Otherwise {@code false}.
	 */
	protected boolean isOrderedLevelMethodCorrect() {
		return orderedLevelMethodCorrect;
	}
}
