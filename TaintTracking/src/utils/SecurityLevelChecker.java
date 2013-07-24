package utils;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import javax.tools.*;

import security.SecurityAnnotation;
import security.SecurityAnnotation.*;

import logging.SecurityLogger;

public class SecurityLevelChecker {
	
	private static final String SIGNATURE = "public static <T> T security.SootSecurityLevel.";
	private static final String JAVA_PATH = "security/SootSecurityLevel.java";
	private static final String CLASS_PATH = "security/SootSecurityLevel.class";
	private static final String CLASS_NAME = "security.SootSecurityLevel";
	private static final String ORDERED_LIST_METHOD = "getOrderedSecurityLevels";
	private static String[] ORDERED_SECURITY_LEVELS = null;
	

	public static boolean checkSecurityLevel(SecurityLogger log) {
		log.structure("CHECKING THE SOOTSECURITYLEVEL FILE");
		boolean valid = true;
		if (compileSootSecurityLevelFile() == 0) {
			try {
				URLClassLoader classLoader = new URLClassLoader( new URL[]{  generateURLOfSootSecurityLevelClass() } );
				Class<?> sootSecurityLevel = classLoader.loadClass(CLASS_NAME);
				valid = checkSecurityLevelClass(sootSecurityLevel, log);
				
				classLoader.close();
				removeSootSecurityLevelClass();
				return valid;
			} catch (MalformedURLException e) {
				log.securitychecker("Couldn't create the URL to the directory which contains the SootSecurityLevel class.", e);
				return false;
			} catch (ClassNotFoundException e) {
				log.securitychecker("SootSecurityLevel class doesn't exists.", e);
				return false;
			} catch (IOException | SecurityException e) {
				log.securitychecker("Couldn't finish the checking of the SootSecurityLevel class correctly.", e);
				return valid;
			}
		} else {
			log.securitychecker("Couldn't compile the SootSecurityLevel.java file.");
			return false;
		}
	}
	
	private static void removeSootSecurityLevelClass() throws IOException, SecurityException  {
		File file = new File(CLASS_PATH);
		file.delete();
	}

	private static boolean checkSecurityLevelClass(Class<?> sootSecurityLevel, SecurityLogger log) {
		Class<?> securityLevel = sootSecurityLevel.getSuperclass();
		try {
			Field validityCheckField = securityLevel.getDeclaredField("CHECK_VALIDITY");
			validityCheckField.setAccessible(true);
			validityCheckField.setBoolean(null, false);
			try {
				Method orderLevelMethod = sootSecurityLevel.getDeclaredMethod(ORDERED_LIST_METHOD);
				SecurityLevel instance = (SecurityLevel) sootSecurityLevel.newInstance();
				String[] levelsArray = (String[]) orderLevelMethod.invoke(instance);
				ORDERED_SECURITY_LEVELS = levelsArray;
				Method[] methodsArray = sootSecurityLevel.getDeclaredMethods();
				if (levelsArray == null || levelsArray.length < 1) {
					log.securitychecker("No security levels defined in the method 'public String[] " + ORDERED_LIST_METHOD + "()'.");
					return false;
				} else {
					boolean valid = true;
					List<String> levels = new ArrayList<String>(Arrays.asList(levelsArray));
					List<String> methodNames = new ArrayList<String>();
					for (Method method : methodsArray) {
						methodNames.add(method.getName());
					}
					for (String level : levels) {
						if (SecurityAnnotation.ADDITIONAL_LEVELS.contains(level)) {
							log.securitychecker("The level '" + level + "' is an internal level which can't be used as customized level.");
							valid = false;
						}
						if (level.contains("(") || level.contains(")")) {
							log.securitychecker("Is not allowed to use brackets inside of the level '" + level + "' because the brackets are used for level equations.");
							valid = false;
						}
						if (level.contains(",")) {
							log.securitychecker("Is not allowed to use commas inside of the level '" + level + "' because the commas are used for level equations.");
							valid = false;
						}
						if (level.contains("*")) {
							log.securitychecker("Is not allowed to use stars inside of the level '" + level + "' because the stars are used for variable levels.");
							valid = false;
						}
						if (! methodNames.contains(level+ "Id")) { // There exists no id function for current level.
							log.securitychecker("Method '" + SIGNATURE + level + "Id(T)' is missing.");
							valid = false;
						} else {
							try {
								Method method = sootSecurityLevel.getMethod(level+"Id", Object.class);
								if (! Modifier.isStatic(method.getModifiers())) {
									log.securitychecker("Method 'public <T> T security.SootSecurityLevel." + level + "Id(T)' is not static.");
									valid = false;
								}
								boolean exists = false;
								for (Annotation annotation : method.getAnnotations()) {
									if (annotation.annotationType().equals(ReturnSecurity.class)) {
										exists = true;
										ReturnSecurity security = (ReturnSecurity) annotation;
										if (! security.value().equals(level)) { // The annotation for the current id function is not valid.
											log.securitychecker("Security annotation of method '" + SIGNATURE + level + "Id(T)' is not valid.");
											valid = false;
										}
									}
								}
								if(! exists) { // The current id function has no security level annotation.
									log.securitychecker("No security annotation for method '" + SIGNATURE + level + "Id(T)'.");
									valid = false;
								}
							} catch (Exception e) {
								log.securitychecker("Method '" + SIGNATURE + level + "Id(T)' is not accessible.");
								valid = false;
							}
						}
					}
					for (String methodName : methodNames) {
						if ((!levels.contains(methodName.substring(0, methodName.length() - 2))) && methodName.endsWith("Id")) {
							try {
								Method method = sootSecurityLevel.getMethod(methodName, Object.class);
								Class<?>[] parameters = method.getParameterTypes();
								Class<?> returnType = method.getReturnType();
								if (parameters.length == 1 && parameters[0].equals(Object.class) && returnType.equals(Object.class)) {
									log.securitychecker("Method 'public static <T> T security.SootSecurityLevel." + methodName + "(T)' has no corresponding level.");
								}
							} catch (NoSuchMethodException | SecurityException e) {
								log.securitychecker("In SootSecurityLevel exists a method with the name '" + methodName + "' which is quite similar to the id functions.");
							}
						}
					}
					return valid;
				}
			} catch (NoSuchMethodException | SecurityException e) {
				log.securitychecker("Couldn't find the method 'public String[] " + ORDERED_LIST_METHOD + "()'.", e);
				return false;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.securitychecker("Couldn't access the method 'public String[] " + ORDERED_LIST_METHOD + "()'.", e);
				return false;
			} catch (InstantiationException e) {
				log.securitychecker("Couldn't instantiate an object of the type SootSecurityLevel", e);
				return false;
			} 
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
			log.securitychecker("Couldn't prepare the superclass SecurityLevel for the check.", e);
			return false;
		} 
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 */
	private static URL generateURLOfSootSecurityLevelClass() throws MalformedURLException {
		URL url = new File("").toURI().toURL();
		return url;
	}

	/**
	 * @return
	 */
	private static int compileSootSecurityLevelFile() {
		OutputStream nullStream = new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				
			}
		};
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		return javaCompiler.run(null, null, nullStream, JAVA_PATH);
	}
	
	public static String[] getOrderSecurityLevels() throws NullPointerException {
		if (ORDERED_SECURITY_LEVELS == null) throw new NullPointerException("Ordered list has until now not yet been generated. Check first for the validity of the SootSecurityLevel file.");
		return ORDERED_SECURITY_LEVELS;
	}

}
