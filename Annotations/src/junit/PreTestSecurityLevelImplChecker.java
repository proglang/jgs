package junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import logging.SecurityLogger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exception.SootException.SecurityLevelException;

import security.SecurityLevel;
import security.SecurityLevelImplChecker;

/**
 * <h1>JUnit tests of class {@link SecurityLevelImplChecker}</h1>
 * 
 * JUnit tests which verify the accuracy of the methods of the class
 * {@link SecurityLevelImplChecker}. Therefore, the classes
 * {@link JUnitSootSecurityLevelImplementation1}, {@link JUnitSootSecurityLevelImplementation2},
 * {@link JUnitSootSecurityLevelImplementation3} and {@link JUnitSootSecurityLevelImplementation4}
 * represents (in-) valid test classes which will be checked by the {@link SecurityLevelImplChecker}.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class PreTestSecurityLevelImplChecker {

	private static SecurityLevelImplChecker getChecker(SecurityLevel securityLevel,
			boolean throwException) throws SecurityLevelException {
		SecurityLevelImplChecker checker = null;
		try {
			Constructor<SecurityLevelImplChecker> constructor = SecurityLevelImplChecker.class
					.getDeclaredConstructor(SecurityLogger.class, SecurityLevel.class,
							boolean.class, boolean.class);
			constructor.setAccessible(true);
			checker = constructor.newInstance(null, securityLevel, false, throwException);
		} catch (SecurityLevelException | SecurityException | NoSuchMethodException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			if (e.getCause() instanceof SecurityLevelException) {
				SecurityLevelException exception = (SecurityLevelException) e.getCause();
				throw exception;
			} else {
				fail("Can't create an instance of the SecurityLevelImplChecker class ["
						+ e.toString() + "].");
			}

		}
		return checker;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testInvalidSootSecurityImplementation1() {
		JUnitSootSecurityLevelImplementation2 impl = new JUnitSootSecurityLevelImplementation2();
		SecurityLevelImplChecker checker = null;
		try {
			checker = getChecker(impl, false);
		} catch (Exception e) {
			fail("No exception expected if calling constructor with the used setting.");
		}
		if (checker != null) {
			String[] levels = checker.getOrderedLevels();
			assertTrue("6 security levels are expected, including invalid levels.",
					levels.length == 6);
			for (int i = 0; i < levels.length; i++) {
				String expectedLevel = new String[] { "void", "1*", "high", "normal", "low", "h()" }[i];
				assertTrue("Correct expected (invalid) level '" + expectedLevel + "'",
						levels[i].equals(expectedLevel));
			}
			try {
				Method isOrderedLevelMethodAvailable = SecurityLevelImplChecker.class
						.getDeclaredMethod("isOrderedLevelMethodAvailable");
				isOrderedLevelMethodAvailable.setAccessible(true);
				assertTrue("Method which returns ordered levels is available.",
						(boolean) isOrderedLevelMethodAvailable.invoke(checker));
				Method isOrderLevelMethodCorrect = SecurityLevelImplChecker.class
						.getDeclaredMethod("isOrderedLevelMethodCorrect");
				isOrderLevelMethodCorrect.setAccessible(true);
				assertTrue("Method which returns ordered levels is correct.",
						(boolean) isOrderLevelMethodCorrect.invoke(checker));
				Method getIllegalLevelNames = SecurityLevelImplChecker.class
						.getDeclaredMethod("getIllegalLevelNames");
				getIllegalLevelNames.setAccessible(true);
				List<?> getIllegalLevelNamesResult = (List<?>) getIllegalLevelNames.invoke(checker);
				assertTrue(
						"Correct illegal level names.",
						getIllegalLevelNamesResult.containsAll(Arrays.asList(new String[] { "void",
								"1*", "h()" }))
								&& getIllegalLevelNamesResult.size() == 3);
				Method getInvalidIdFunctions = SecurityLevelImplChecker.class
						.getDeclaredMethod("getInvalidIdFunctions");
				getInvalidIdFunctions.setAccessible(true);
				List<?> getInvalidIdFunctionsResult = (List<?>) getInvalidIdFunctions
						.invoke(checker);
				assertTrue(
						"Correct invalid id functions.",
						getInvalidIdFunctionsResult.containsAll(Arrays
								.asList(new String[] { "lowId" }))
								&& getInvalidIdFunctionsResult.size() == 1);
				Method getInvalidIdFunctionAnnotation = SecurityLevelImplChecker.class
						.getDeclaredMethod("getInvalidIdFunctionAnnotation");
				getInvalidIdFunctionAnnotation.setAccessible(true);
				List<?> getInvalidIdFunctionAnnotationResult = (List<?>) getInvalidIdFunctionAnnotation
						.invoke(checker);
				assertTrue("No invalid id function annotations.",
						getInvalidIdFunctionAnnotationResult.isEmpty());
				Method getUnavailableIdFunctions = SecurityLevelImplChecker.class
						.getDeclaredMethod("getUnavailableIdFunctions");
				getUnavailableIdFunctions.setAccessible(true);
				List<?> getUnavailableIdFunctionsResult = (List<?>) getUnavailableIdFunctions
						.invoke(checker);
				assertTrue(
						"Corret unavailable id functions.",
						getUnavailableIdFunctionsResult.containsAll(Arrays.asList(new String[] {
								"voidId", "1*Id", "h()Id", "normalId" }))
								&& getUnavailableIdFunctionsResult.size() == 4);
				Method getUnavailableIdFunctionAnnotation = SecurityLevelImplChecker.class
						.getDeclaredMethod("getUnavailableIdFunctionAnnotation");
				getUnavailableIdFunctionAnnotation.setAccessible(true);
				List<?> getUnavailableIdFunctionAnnotationResult = (List<?>) getUnavailableIdFunctionAnnotation
						.invoke(checker);
				assertTrue(
						"Correct unavailable id function annotations.",
						getUnavailableIdFunctionAnnotationResult.containsAll(Arrays
								.asList(new String[] { "highId" }))
								&& getUnavailableIdFunctionAnnotationResult.size() == 1);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			fail("Unexpectedly, the implementation is invalid.");
		}
	}

	@Test
	public final void testInvalidSootSecurityImplementation2() {
		JUnitSootSecurityLevelImplementation3 impl = new JUnitSootSecurityLevelImplementation3();
		SecurityLevelImplChecker checker = null;
		try {
			checker = getChecker(impl, false);
		} catch (Exception e) {
			fail("No exception expected if calling constructor with the used setting.");
		}
		if (checker != null) {
			String[] levels = checker.getOrderedLevels();
			assertTrue("1 security levels is expected.", levels.length == 1);
			for (int i = 0; i < levels.length; i++) {
				String expectedLevel = new String[] { "high" }[i];
				assertTrue("Correct expected level '" + expectedLevel + "'",
						levels[i].equals(expectedLevel));
			}
			try {
				Method isOrderedLevelMethodAvailable = SecurityLevelImplChecker.class
						.getDeclaredMethod("isOrderedLevelMethodAvailable");
				isOrderedLevelMethodAvailable.setAccessible(true);
				assertTrue("Method which returns ordered levels is available.",
						(boolean) isOrderedLevelMethodAvailable.invoke(checker));
				Method isOrderLevelMethodCorrect = SecurityLevelImplChecker.class
						.getDeclaredMethod("isOrderedLevelMethodCorrect");
				isOrderLevelMethodCorrect.setAccessible(true);
				assertTrue("Method which returns ordered levels is incorrect.",
						!(boolean) isOrderLevelMethodCorrect.invoke(checker));
				Method getIllegalLevelNames = SecurityLevelImplChecker.class
						.getDeclaredMethod("getIllegalLevelNames");
				getIllegalLevelNames.setAccessible(true);
				List<?> getIllegalLevelNamesResult = (List<?>) getIllegalLevelNames.invoke(checker);
				assertTrue("No illegal level names.", getIllegalLevelNamesResult.isEmpty());
				Method getInvalidIdFunctions = SecurityLevelImplChecker.class
						.getDeclaredMethod("getInvalidIdFunctions");
				getInvalidIdFunctions.setAccessible(true);
				List<?> getInvalidIdFunctionsResult = (List<?>) getInvalidIdFunctions
						.invoke(checker);
				assertTrue("No invalid id functions.", getInvalidIdFunctionsResult.isEmpty());
				Method getInvalidIdFunctionAnnotation = SecurityLevelImplChecker.class
						.getDeclaredMethod("getInvalidIdFunctionAnnotation");
				getInvalidIdFunctionAnnotation.setAccessible(true);
				List<?> getInvalidIdFunctionAnnotationResult = (List<?>) getInvalidIdFunctionAnnotation
						.invoke(checker);
				assertTrue("No invalid id function annotations.",
						getInvalidIdFunctionAnnotationResult.isEmpty());
				Method getUnavailableIdFunctions = SecurityLevelImplChecker.class
						.getDeclaredMethod("getUnavailableIdFunctions");
				getUnavailableIdFunctions.setAccessible(true);
				List<?> getUnavailableIdFunctionsResult = (List<?>) getUnavailableIdFunctions
						.invoke(checker);
				assertTrue("No unavailable id functions.",
						getUnavailableIdFunctionsResult.isEmpty());
				Method getUnavailableIdFunctionAnnotation = SecurityLevelImplChecker.class
						.getDeclaredMethod("getUnavailableIdFunctionAnnotation");
				getUnavailableIdFunctionAnnotation.setAccessible(true);
				List<?> getUnavailableIdFunctionAnnotationResult = (List<?>) getUnavailableIdFunctionAnnotation
						.invoke(checker);
				assertTrue("No unavailable id function annotations.",
						getUnavailableIdFunctionAnnotationResult.isEmpty());
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			fail("Unexpectedly, the implementation is invalid.");
		}
	}

	@Test(expected = SecurityLevelException.class)
	public final void testInvalidSootSecurityImplementation3() throws SecurityException {
		JUnitSootSecurityLevelImplementation2 impl = new JUnitSootSecurityLevelImplementation2();
		getChecker(impl, true);
	}

	@Test
	public final void testInvalidSootSecurityImplementation4() {
		JUnitSootSecurityLevelImplementation4 impl = new JUnitSootSecurityLevelImplementation4();
		SecurityLevelImplChecker checker = null;
		try {
			checker = getChecker(impl, false);
		} catch (Exception e) {
			fail("No exception expected if calling constructor with the used setting.");
		}
		if (checker != null) {
			String[] levels = checker.getOrderedLevels();
			assertTrue("2 security levels are expected.", levels.length == 2);
			for (int i = 0; i < levels.length; i++) {
				String expectedLevel = new String[] { "high", "low" }[i];
				assertTrue("Correct expected level '" + expectedLevel + "'",
						levels[i].equals(expectedLevel));
			}
			try {
				Method isOrderedLevelMethodAvailable = SecurityLevelImplChecker.class
						.getDeclaredMethod("isOrderedLevelMethodAvailable");
				isOrderedLevelMethodAvailable.setAccessible(true);
				assertTrue("Method which returns ordered levels is available.",
						(boolean) isOrderedLevelMethodAvailable.invoke(checker));
				Method isOrderLevelMethodCorrect = SecurityLevelImplChecker.class
						.getDeclaredMethod("isOrderedLevelMethodCorrect");
				isOrderLevelMethodCorrect.setAccessible(true);
				assertTrue("Method which returns ordered levels is correct.",
						(boolean) isOrderLevelMethodCorrect.invoke(checker));
				Method getIllegalLevelNames = SecurityLevelImplChecker.class
						.getDeclaredMethod("getIllegalLevelNames");
				getIllegalLevelNames.setAccessible(true);
				List<?> getIllegalLevelNamesResult = (List<?>) getIllegalLevelNames.invoke(checker);
				assertTrue("No illegal level names.", getIllegalLevelNamesResult.isEmpty());
				Method getInvalidIdFunctions = SecurityLevelImplChecker.class
						.getDeclaredMethod("getInvalidIdFunctions");
				getInvalidIdFunctions.setAccessible(true);
				List<?> getInvalidIdFunctionsResult = (List<?>) getInvalidIdFunctions
						.invoke(checker);
				assertTrue("No invalid id functions.", getInvalidIdFunctionsResult.isEmpty());
				Method getInvalidIdFunctionAnnotation = SecurityLevelImplChecker.class
						.getDeclaredMethod("getInvalidIdFunctionAnnotation");
				getInvalidIdFunctionAnnotation.setAccessible(true);
				List<?> getInvalidIdFunctionAnnotationResult = (List<?>) getInvalidIdFunctionAnnotation
						.invoke(checker);
				assertTrue(
						"Correct invalid id function annotations.",
						getInvalidIdFunctionAnnotationResult.containsAll(Arrays
								.asList(new String[] { "highId", "lowId" }))
								&& getInvalidIdFunctionAnnotationResult.size() == 2);
				Method getUnavailableIdFunctions = SecurityLevelImplChecker.class
						.getDeclaredMethod("getUnavailableIdFunctions");
				getUnavailableIdFunctions.setAccessible(true);
				List<?> getUnavailableIdFunctionsResult = (List<?>) getUnavailableIdFunctions
						.invoke(checker);
				assertTrue("No unavailable id functions.",
						getUnavailableIdFunctionsResult.isEmpty());
				Method getUnavailableIdFunctionAnnotation = SecurityLevelImplChecker.class
						.getDeclaredMethod("getUnavailableIdFunctionAnnotation");
				getUnavailableIdFunctionAnnotation.setAccessible(true);
				List<?> getUnavailableIdFunctionAnnotationResult = (List<?>) getUnavailableIdFunctionAnnotation
						.invoke(checker);
				assertTrue("No unavailable id function annotations.",
						getUnavailableIdFunctionAnnotationResult.isEmpty());
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			fail("Unexpectedly, the implementation is invalid.");
		}
	}

	@Test
	public final void testValidSootSecurityImplementation() {
		JUnitSootSecurityLevelImplementation1 impl = new JUnitSootSecurityLevelImplementation1();
		SecurityLevelImplChecker checker = null;
		try {
			checker = getChecker(impl, true);
		} catch (Exception e) {
			fail("Unexpectedly, the implementation is invalid.");
		}
		if (checker != null) {
			String[] levels = checker.getOrderedLevels();
			assertTrue("3 security levels are expected.", levels.length == 3);
			for (int i = 0; i < levels.length; i++) {
				String expectedLevel = new String[] { "high", "normal", "low" }[i];
				assertTrue("Correct expected level '" + expectedLevel + "'",
						levels[i].equals(expectedLevel));
			}
			try {
				Method isOrderedLevelMethodAvailable = SecurityLevelImplChecker.class
						.getDeclaredMethod("isOrderedLevelMethodAvailable");
				isOrderedLevelMethodAvailable.setAccessible(true);
				assertTrue("Method which returns ordered levels is available.",
						(boolean) isOrderedLevelMethodAvailable.invoke(checker));
				Method isOrderLevelMethodCorrect = SecurityLevelImplChecker.class
						.getDeclaredMethod("isOrderedLevelMethodCorrect");
				isOrderLevelMethodCorrect.setAccessible(true);
				assertTrue("Method which returns ordered levels is correct.",
						(boolean) isOrderLevelMethodCorrect.invoke(checker));
				Method getIllegalLevelNames = SecurityLevelImplChecker.class
						.getDeclaredMethod("getIllegalLevelNames");
				getIllegalLevelNames.setAccessible(true);
				List<?> getIllegalLevelNamesResult = (List<?>) getIllegalLevelNames.invoke(checker);
				assertTrue("No illegal level names.", getIllegalLevelNamesResult.isEmpty());
				Method getInvalidIdFunctions = SecurityLevelImplChecker.class
						.getDeclaredMethod("getInvalidIdFunctions");
				getInvalidIdFunctions.setAccessible(true);
				List<?> getInvalidIdFunctionsResult = (List<?>) getInvalidIdFunctions
						.invoke(checker);
				assertTrue("No invalid id functions.", getInvalidIdFunctionsResult.isEmpty());
				Method getInvalidIdFunctionAnnotation = SecurityLevelImplChecker.class
						.getDeclaredMethod("getInvalidIdFunctionAnnotation");
				getInvalidIdFunctionAnnotation.setAccessible(true);
				List<?> getInvalidIdFunctionAnnotationResult = (List<?>) getInvalidIdFunctionAnnotation
						.invoke(checker);
				assertTrue("No invalid id function annotations.",
						getInvalidIdFunctionAnnotationResult.isEmpty());
				Method getUnavailableIdFunctions = SecurityLevelImplChecker.class
						.getDeclaredMethod("getUnavailableIdFunctions");
				getUnavailableIdFunctions.setAccessible(true);
				List<?> getUnavailableIdFunctionsResult = (List<?>) getUnavailableIdFunctions
						.invoke(checker);
				assertTrue("No unavailable id functions.",
						getUnavailableIdFunctionsResult.isEmpty());
				Method getUnavailableIdFunctionAnnotation = SecurityLevelImplChecker.class
						.getDeclaredMethod("getUnavailableIdFunctionAnnotation");
				getUnavailableIdFunctionAnnotation.setAccessible(true);
				List<?> getUnavailableIdFunctionAnnotationResult = (List<?>) getUnavailableIdFunctionAnnotation
						.invoke(checker);
				assertTrue("No unavailable id function annotations.",
						getUnavailableIdFunctionAnnotationResult.isEmpty());
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			fail("Unexpectedly, the implementation is invalid.");
		}
	}

}
