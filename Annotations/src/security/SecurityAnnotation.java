package security;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
	
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ReturnSecurity {
		
		String value();

	}
	
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ParameterSecurity {
		
		String[] value();

	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldSecurity {
		
		String value();

	}
	
	/**
	 * Abstract Class which gives the possibility to define a security level hierarchy and which
	 * also defines the id functions for each security level. The programmer which uses the analysis
	 * tool has to implement this class. Some restrictions he had to consider:
	 * <ul>
	 * <li>Soot Security Analysis requires a implementation of the class {@link SecurityLevel}</li>
	 * <li>The name of this subclass must be {@code SootSecurityLevel}</li>
	 * <li>The subclass must be located in the package {@code security}</li>
	 * <li>In the subclass the method {@link SecurityLevel#getOrderedSecurityLevels()} is
	 * implemented and returns a list of minimal one sercurity level name.</li>
	 * <li>In the subclass exists for each by {@link SecurityLevel#getOrderedSecurityLevels()}
	 * defined security level a static id function. The name of this function must be e.g. if the level
	 * name is high: {@code highId()}.</li>
	 * <li>The implementation of such an id function looks like the following:
	 * 
	 * <pre>
	 * &#064;Security(&quot;low&quot;)
	 * public static &lt;T&gt; T lowId(T object) {
	 * 	return object;
	 * }
	 * </pre>
	 * 
	 * <li>Each id function is annotated with the specific security return level ({@link ReturnSecurity}).</li>
	 * </ul>
	 * 
	 * To test whether the implemented class is valid, just create a new instance, if something is
	 * invalid the error console will output this fact.
	 * 
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static abstract class SecurityLevel {
		
		private static final String LINE_SEPARATOR = System.getProperty("line.separator");
		private static boolean CHECK_VALIDITY = true;
		
		public SecurityLevel() {
			if (CHECK_VALIDITY)
				this.validateImplementation();
		}
		
		/**
		 * Should return an array where all security levels are specified as a String. The array
		 * should start with the strongest security level and should end with the weakest security
		 * level. E.g. <pre>
		 * public String[] getOrderedSecurityLevels() {
		 * 	return new String[] {"high","low"}; 
		 * }</pre>
		 * 
		 * @return
		 */
		public abstract String[] getOrderedSecurityLevels();
		
		@ReturnSecurity("*")
		public static <T> T variableId(T object) {
			return object;
		}
		
		@ReturnSecurity("void")
		public static void voidSecurity() {
			return;
		}
		
		private boolean validateImplementation() {
			StringBuilder messages = new StringBuilder();
			boolean valid = true;
			List<Method> methods = new ArrayList<Method>(Arrays.asList(this.getClass().getDeclaredMethods()));
			
			List<String> methodNames = new ArrayList<String>();
			for (Method method : methods) {
				methodNames.add(method.getName());
			}
			List<String> levels = new ArrayList<String>(Arrays.asList(this.getOrderedSecurityLevels())); 
			if (levels.size() < 1) { // No security level was defined.
				messages.append("No security levels specified." + LINE_SEPARATOR);
				valid = false;
			}
			for (String level : levels) {
				if (ADDITIONAL_LEVELS.contains(level)) {
					messages.append("The level '" + level + "' is an internal level which can't be used as customized level." + LINE_SEPARATOR);
					valid = false;
				}
				if (level.contains("(") || level.contains(")")) {
					messages.append("Is not allowed to use brackets inside of the level '" + level + "' because the brackets are used for level equations." + LINE_SEPARATOR);
					valid = false;
				}
				if (level.contains(",")) {
					messages.append("Is not allowed to use commas inside of the level '" + level + "' because the commas are used for level equations." + LINE_SEPARATOR);
					valid = false;
				}
				if (level.contains("*")) {
					messages.append("Is not allowed to use stars inside of the level '" + level + "' because the stars are used for variable levels." + LINE_SEPARATOR);
					valid = false;
				}
				if (! methodNames.contains(level+ "Id")) { // There exists no id function for current level.
					messages.append("Method 'public static <T> T security.SootSecurityLevel." + level + "Id(T)' is missing." + LINE_SEPARATOR);
					valid = false;
				} else {
					try {
						Method method = this.getClass().getMethod(level+"Id", Object.class);
						if (! Modifier.isStatic(method.getModifiers())) {
							messages.append("Method 'public <T> T security.SootSecurityLevel." + level + "Id(T)' is not static." + LINE_SEPARATOR);
							valid = false;
						}
						boolean exists = false;
						for (Annotation annotation : method.getAnnotations()) {
							if (annotation.annotationType().equals(ReturnSecurity.class)) {
								exists = true;
								ReturnSecurity security = (ReturnSecurity) annotation;
								if (! security.value().equals(level)) { // The annotation for the current id function is not valid.
									messages.append("Security annotation of method 'public static <T> T security.SootSecurityLevel." + level + "Id(T)' is not valid." + LINE_SEPARATOR);
									valid = false;
								}
							}
						}
						if(! exists) { // The current id function has no security level annotation.
							messages.append("No security annotation for method 'public static <T> T security.SootSecurityLevel." + level + "Id(T)'." + LINE_SEPARATOR);
							valid = false;
						}
					} catch (Exception e) {
						messages.append("Method 'public static <T> T security.SootSecurityLevel." + level + "Id(T)' is not accessible." + LINE_SEPARATOR);
						valid = false;
					}
				}
			}
			for (String methodName : methodNames) {
				if ((! levels.contains(methodName.substring(0, methodName.length() - 2))) && methodName.endsWith("Id")) {
						try {
							Method method = this.getClass().getMethod(methodName, Object.class);
							Class<?>[] parameters = method.getParameterTypes();
							Class<?> returnType = method.getReturnType();
							if (parameters.length == 1 && parameters[0].equals(Object.class) && returnType.equals(Object.class)) {
								messages.append("Method 'public static <T> T security.SootSecurityLevel." + methodName + "(T)' has no corresponding level." + LINE_SEPARATOR);
							}
						} catch (NoSuchMethodException | SecurityException e) {
							messages.append("There exists a method with the name '" + methodName + "' which is quite similar to the id functions." + LINE_SEPARATOR);
						}
						
					
				}
			}
			printError(messages);
			return valid;
		}
		
		private void printError(StringBuilder stringBuilder) {
			String[] lines = stringBuilder.toString().split(LINE_SEPARATOR);
			if (lines.length == 1 && lines[0].equals(""))
				return;
			for (String line : lines) {
				System.err.println("- [ ERROR in security.SootSecurityLevel: " + line + " ] -");
				System.err.flush();
			}
		}
		
	}
	
	public static String VOID_LEVEL = "void";
	public static List<String> ADDITIONAL_LEVELS = new ArrayList<String>(Arrays.asList(VOID_LEVEL));
	public static String LEVEL_PATTERN_SIGN = "*";
	
	private List<String> availableLevels = new ArrayList<String>();
	private List<String> idMethodNames = new ArrayList<String>();
	
	public SecurityAnnotation(List<String> availableLevels) {
		super();
		this.availableLevels.addAll(availableLevels);
		for (String level : this.availableLevels) {
			idMethodNames.add(level + "Id");
		}
	}
	
	public List<String> getAvailableLevels() {
		return this.availableLevels;
	}
	
	public static String getSootAnnotationTag(Class<?> cl) {
		String packageName = cl.getPackage().getName();
		String parentClassName = cl.getDeclaringClass().getSimpleName();
		String className = cl.getSimpleName();
		return "L" + packageName + "/" + parentClassName + "$" + className + ";";
	}

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
	
	public LevelEquation getReturnLevelEquation(String returnLevel,  List<String> methodParameterLevels) throws InvalidLevelException, InvalidEquationException {
		List<String> allLevels = new ArrayList<String>(Arrays.asList(VOID_LEVEL));
		allLevels.addAll(availableLevels);
		for (String level : methodParameterLevels) {
			if (! allLevels.contains(level)) allLevels.add(level);
		}
		LevelEquation equation = LevelEquationCreator.createFrom(returnLevel);
		return equation;
	}
	
	public LevelEquationValidityVistitor getLevelEquationValidityVistitor(LevelEquation levelEquation) {
		return new LevelEquationValidityVistitor(levelEquation, this);
	}
	
	public LevelEquationCalculateVoidVisitor getLevelEquationCalculateVoidVisitor() {
		return new LevelEquationCalculateVoidVisitor();
	}
	
	public LevelEquationEvaluationVisitor getLevelEquationEvaluationVisitor(List<String> argumentLevels, List<String>parameterLevels) {
		return new LevelEquationEvaluationVisitor(argumentLevels, parameterLevels, this);
	}

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

	public boolean checkValidityOfFieldLevel(String level) {
		return availableLevels.contains(level);
	}
	
	public String getWeakestSecurityLevel() {
		if (availableLevels.size() > 0) {
			return availableLevels.get(availableLevels.size() - 1);
		}
		return null;
	}

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

	public String getMinLevel(String level1, String level2) {
		if (level1.equals(VOID_LEVEL) || level2.equals(VOID_LEVEL)) {
			return VOID_LEVEL;
		} else {
			int indexLevel1 = availableLevels.indexOf(level1);
			int indexLevel2 = availableLevels.indexOf(level2);
			if (indexLevel1 >= 0 && indexLevel2 >= 0) {
				if (indexLevel1 >= indexLevel2) {
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

	public String getMaxLevel(String level1, String level2) {
		if (level1.equals(VOID_LEVEL) || level2.equals(VOID_LEVEL)) {
			return VOID_LEVEL;
		} else {
			int indexLevel1 = availableLevels.indexOf(level1);
			int indexLevel2 = availableLevels.indexOf(level2);
			if (indexLevel1 >= 0 && indexLevel2 >= 0) {
				if (indexLevel1 >= indexLevel2) {
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
	
	public boolean isMethodOfSootSecurity(SootMethod sootMethod) {
		return sootMethod.getDeclaringClass().getName().equals("security.SootSecurityLevel");
	}
	
	public boolean isIdMethod(SootMethod sootMethod) {
		boolean packageValid = sootMethod.getDeclaringClass().getName().equals("security.SootSecurityLevel");
		boolean methodNameValid = idMethodNames.contains(sootMethod.getName());
		boolean parameterValid = sootMethod.getParameterCount() == 1;
		return packageValid && methodNameValid && parameterValid;
	}
}
