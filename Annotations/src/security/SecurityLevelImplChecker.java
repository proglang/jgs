package security;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.SecurityMessages;

import logging.SecurityLogger;

/**
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class SecurityLevelImplChecker {
	
	/** */
	protected static final String ID_SIGNATURE = "public static <T> T ";
	/** */
	protected static final String JAVA_PATH = "security/SootSecurityLevel.java";
	/** */
	protected static final String CLASS_PATH = "security/SootSecurityLevel.class";
	/** */
	protected static final String ORDERED_LIST_METHOD = "getOrderedSecurityLevels";
	/** */
	private static final String ORDERED_LIST_SIGNATURE = "public String[] " + ORDERED_LIST_METHOD + "()";
	/** */
	protected SecurityLogger log = null;
	/** */
	private String[] orderLevels = new String[] {};
	/** */
	private boolean orderedLevelMethodAvailable = false;
	/** */
	private boolean orderedLevelMethodCorrect = false;
	/** */
	private List<String> illegalLevelNames = new ArrayList<String>();
	/** */
	private List<String> unavailableIdMethods = new ArrayList<String>();
	/** */
	private List<String> unavailableIdMethodsAnnotation = new ArrayList<String>();
	/** */
	private List<String> invalidIdMethods = new ArrayList<String>();
	/** */
	private List<String> invalidIdMethodsAnnotation = new ArrayList<String>();
	/** */
	private boolean shouldLog = true;
	
	
	/**
	 * 
	 * @param impl
	 * @throws SecurityException
	 */
	public SecurityLevelImplChecker(SecurityLevel impl) throws SecurityException {
		super();
		if (! checkValidityOfImplementation(impl)) 
			throw new SecurityException(SecurityMessages.reflectionInvalidSootSecurityLevelClass());
	}
	
	/**
	 * 
	 * @param log
	 * @param impl
	 * @param shouldLog
	 * @param throwException
	 * @throws SecurityException
	 */
	protected SecurityLevelImplChecker(SecurityLogger log, SecurityLevel impl, boolean shouldLog, boolean throwException) throws SecurityException {
		super();
		this.shouldLog = shouldLog;
		if (shouldLog) {
			this.log = log;
		}
		if (! checkValidityOfImplementation(impl)) {
			if (throwException) {
				throw new SecurityException(SecurityMessages.reflectionInvalidSootSecurityLevelClass());
			}
		}		
	}
	
	/**
	 * 
	 * @param impl
	 * @return
	 */
	private boolean checkValidityOfImplementation(SecurityLevel impl) {
		Class<?> sootSecurityLevelClass = impl.getClass();
		return checkValidityOfOrderedListMethod(impl, sootSecurityLevelClass) && checkValidityOfLevels(sootSecurityLevelClass);
	}

	/**
	 * 
	 * @param sootSecurityLevelClass
	 * @return
	 */
	private boolean checkValidityOfLevels(Class<?> sootSecurityLevelClass) {
		if (orderLevels == null || orderLevels.length < 1) {
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
				valid = valid && checkValidityOfLevel(sootSecurityLevelClass, methodNames, level);
			}
			for (String methodName : methodNames) {
				checkForPossibleMistakes(sootSecurityLevelClass, levels, methodName);
			}
			return valid;
		}
	}

	/**
	 * 
	 * @param sootSecurityLevelClass
	 * @param levels
	 * @param methodName
	 */
	private void checkForPossibleMistakes(Class<?> sootSecurityLevelClass, List<String> levels,	String methodName) {
		if ((!levels.contains(methodName.substring(0, methodName.length() - 2))) && methodName.endsWith(SecurityLevel.SUFFIX_ID_METHOD)) {
			try {
				Method method = sootSecurityLevelClass.getMethod(methodName, Object.class);
				Class<?>[] parameters = method.getParameterTypes();
				Class<?> returnType = method.getReturnType();
				boolean isStatic = Modifier.isStatic(method.getModifiers());
				if (parameters.length == 1 && parameters[0].equals(Object.class) && returnType.equals(Object.class) && isStatic) {
					printWarning(SecurityMessages.reflectionPossibleMistake(ID_SIGNATURE + methodName + "(T)"));
				}
			} catch (NoSuchMethodException | SecurityException e) {
				printWarning(SecurityMessages.reflectionNoMethodAccess(methodName));
			}
		}
	}

	/**
	 * 
	 * @param sootSecurityLevelClass
	 * @param methodNames
	 * @param level
	 * @return
	 */
	private boolean checkValidityOfLevel(Class<?> sootSecurityLevelClass, List<String> methodNames, String level) {
		boolean valid = true;
		if (SecurityAnnotation.ADDITIONAL_LEVELS.contains(level)) {
			printException(SecurityMessages.reflectionIllegalLevelName(level));
			illegalLevelNames.add(level);
			valid = false;
		}
		if (level.contains("(") || level.contains(")")) {
			printException(SecurityMessages.reflectionIllegalLevelSign(level, "'(' or ')'", "level equations"));
			illegalLevelNames.add(level);
			valid = false;
		}
		if (level.contains(",")) {
			printException(SecurityMessages.reflectionIllegalLevelSign(level, "','", "level equations"));
			illegalLevelNames.add(level);
			valid = false;
		}
		if (level.contains("*")) {
			printException(SecurityMessages.reflectionIllegalLevelSign(level, "'*'", "variable levels in level equations"));
			illegalLevelNames.add(level);
			valid = false;
		}
		if (! methodNames.contains(level + SecurityLevel.SUFFIX_ID_METHOD)) {
			printException(SecurityMessages.reflectionNoMethod(ID_SIGNATURE + level + SecurityLevel.SUFFIX_ID_METHOD +"(T)"));
			unavailableIdMethods.add(level + SecurityLevel.SUFFIX_ID_METHOD);
			valid = false;
		} else {
			valid = valid && checkValidityOfIdMethod(sootSecurityLevelClass, level);
		}
		return valid;
	}

	/**
	 * 
	 * @param sootSecurityLevelClass
	 * @param level
	 * @return
	 */
	private boolean checkValidityOfIdMethod(Class<?> sootSecurityLevelClass, String level) {
		boolean valid = true;
		String methodSignatureId = ID_SIGNATURE + level + SecurityLevel.SUFFIX_ID_METHOD + "(T)";
		try {
			Method method = sootSecurityLevelClass.getMethod(level + SecurityLevel.SUFFIX_ID_METHOD, Object.class);
			if (! Modifier.isStatic(method.getModifiers())) {
				printException(SecurityMessages.reflectionNonStaticMethod("'public <T> T " + level + SecurityLevel.SUFFIX_ID_METHOD + "(T)'"));
				invalidIdMethods.add(level + SecurityLevel.SUFFIX_ID_METHOD);
				valid = false;
			}
			boolean exists = false;
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation.annotationType().equals(Annotations.ReturnSecurity.class)) {
					exists = true;
					Annotations.ReturnSecurity security = (Annotations.ReturnSecurity) annotation;
					if (!security.value().equals(level)) { 
						printException(SecurityMessages.reflectionInvalidMethodAnnotation(methodSignatureId));
						invalidIdMethodsAnnotation.add(level + SecurityLevel.SUFFIX_ID_METHOD);
						valid = false;
					}
				}
			}
			if (!exists) {
				printException(SecurityMessages.reflectionNoMethodAnnotation(methodSignatureId));
				unavailableIdMethodsAnnotation.add(level + SecurityLevel.SUFFIX_ID_METHOD);
				valid = false;
			}
		} catch (Exception e) {
			printException(SecurityMessages.reflectionNoMethodAccess(methodSignatureId));
			unavailableIdMethods.add(level + SecurityLevel.SUFFIX_ID_METHOD);
			valid = false;
		}
		return valid;
	}

	/**
	 * 
	 * @param impl
	 * @param sootSecurityLevelClass
	 * @param methodSignatureOrderedList
	 * @return
	 */
	private boolean checkValidityOfOrderedListMethod(SecurityLevel impl, Class<?> sootSecurityLevelClass) {
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
	 * 
	 * @param e
	 * @param msg
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
	 * 
	 * @param msg
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
	 * 
	 * @param msg
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
	 * 
	 * @return
	 */
	public String[] getOrderedLevels() {
		return orderLevels;
	}
	
	/**
	 * 
	 * @return
	 */
	protected boolean isOrderedLevelMethodAvailable() {
		return orderedLevelMethodAvailable;
	}

	/**
	 * 
	 * @return
	 */
	protected boolean isOrderedLevelMethodCorrect() {
		return orderedLevelMethodCorrect;
	}

	/**
	 * 
	 * @return
	 */
	protected List<String> getIllegalLevelNames() {
		return illegalLevelNames;
	}

	/**
	 * 
	 * @return
	 */
	protected List<String> getUnavailableIdMethods() {
		return unavailableIdMethods;
	}

	/**
	 * 
	 * @return
	 */
	protected List<String> getUnavailableIdMethodsAnnotation() {
		return unavailableIdMethodsAnnotation;
	}

	/**
	 * 
	 * @return
	 */
	protected List<String> getInvalidIdMethods() {
		return invalidIdMethods;
	}

	/**
	 * 
	 * @return
	 */
	protected List<String> getInvalidIdMethodsAnnotation() {
		return invalidIdMethodsAnnotation;
	}
}
