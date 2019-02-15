package classfiletests.utils;

import static analyzer.level2.SecurityMonitoringEvent.ILLEGAL_FLOW;
import static analyzer.level2.SecurityMonitoringEvent.NSU_FAILURE;
import static analyzer.level2.SecurityMonitoringEvent.PASSED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import analyzer.level2.SecurityMonitoringEvent;
import end2endtest.EventChecker;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

import util.exceptions.IllegalFlowError;
import util.exceptions.InternalAnalyzerException;
import util.exceptions.NSUError;

import util.logging.L1Logger;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.io.File;

/**
 * Helper Class to run a given binary class file, and see whether or not it
 * throws the desired exception. Used by the end-to-end tests.
 * 
 * @author Regina Koenig, Nicolas Müller
 */
public class ClassRunner {

	private static Logger logger = Logger.getLogger(ClassRunner.class.getName());
	private static final String TARGET_NAME = "exec";

	/**
	 * Runs the given class via Apache Ant in the same JVM instance.
	 * 
	 * @param className
	 *            class to be run
	 */
	private static void runClass(String className, String packageName, String outputDir) {

		Project project = new Project();
		project.setName("ClassRunner");
		project.init();

		Target target = new Target();
		target.setName(TARGET_NAME);

		Java task = new Java();
		Path path = task.createClasspath();
		path.createPathElement().setPath("./sootOutput/" + outputDir);
		path.createPathElement().setPath("./bin");

		// TODO: if we don't fork, why do I need to set the classpath?
		for (URL url : ((URLClassLoader) ClassLoader.getSystemClassLoader())
				.getURLs()) {
			path.createPathElement().setPath(url.getPath());
		}
		task.setClasspath(path);
		task.setClassname(packageName + "." + className);
		task.setFork(false);
		task.setFailonerror(true);
		task.setProject(project);

		target.addTask(task);

		project.addTarget(target);
		project.setDefault(TARGET_NAME);

		// TODO: check if this is right and explain why (the double execute)
		target.execute();
		project.executeTarget(project.getDefaultTarget());

	}

	/**
	 * Run class and check whether the expected exception is found.
	 * @author Regina Koenig, Nicolas Müller
	 */
	public static void testClass(String className, String outputDir, String packageDir,
								 SecurityMonitoringEvent expectedException, // TODO: the type "SecurityMonitoringEvent" has the wrong name (or is the wrong type)
								 String... involvedVars) {

		logger.info("Trying to run test " + className);

		String fullPath = Paths.get(System.getProperty("user.dir"),"sootOutput",
						  outputDir, packageDir, className + ".class").toString();

		if (!new File(fullPath).isFile()) {
			logger.severe("File "
						  + fullPath
						  + " not found. Something weird is going on, because the test should automatically"

						  + "compile the desired file and put it into the correct folder.");
			fail();
		}

		try {
			try {
				// TODO: deal w/ package names correctly
				runClass(className, packageDir.replace('/', '.').replace(File.separatorChar, '.'), outputDir);

				// check if we did expect and Exception
				if (!expectedException.equals(PASSED)) {
					fail("No IFC error thrown. Expected: " + expectedException);

				}
			}
			// first check the security exceptions
			catch (BuildException buildExc) {
				// We have an exception thrown by ANT
				Throwable e = buildExc.getCause();
				// the ANT exceptions are built with a different classloader, so
				// we cannot use "instanceof" to compare with what we expect. So
				// we compare the names of the classes.
				if (e == null) {
					throw new InternalAnalyzerException("Unexpected BuildException", buildExc);
				} else if (hasClassNameOf(e, NSUError.class)) {
					assertExpectedException(e, NSU_FAILURE, expectedException);
				} else if (hasClassNameOf(e, IllegalFlowError.class)) {
					assertExpectedException(e, ILLEGAL_FLOW, expectedException);
				} else {
					String message = "Unexpected Exception: " + e.toString();
					logger.severe(message);
					e.printStackTrace();
					fail(message);
				}
			}
		}
		// TODO: check for instrumentation events here
		catch (EventChecker.MissingEventException | EventChecker.UnexpectedEventException e) {
		    e.printStackTrace();
		    fail(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Unexpected Exception (see cause)", e);
		}




		/*
		if (expectetEvents.contains(SecurityMonitoringEvent.ILLEGAL_FLOW))

			switch (expectetEvents) {
				case NONE:
				    // fail if no expection was expected
					logger.severe("Fail because exception was not expected");
					fail();
				case ILLEGAL_FLOW:
					// TODO: is there no better way of doing this comparison?
					assertEquals(IllegalFlowError.class.toString(), e.getCause()
														  .getClass().toString());

					if (involvedVars.length < 1) {
						logger.warning("No variables supplied for IllegalFlowError");
					}
					for (String var : involvedVars) {
						logger.info("Check whether " + var + " is contained in: "
								+ e.getMessage() + " ... ");
						if (e.getMessage().contains(var)) {
							logger.info("Success!");
						} else {
							logger.severe("Fail, Exception thrown, but incorrect variable!");
						}
						assertTrue(e.getMessage().contains(var));
					}
					break;
				case NSU_FAILURE:
					assertEquals(NSUError.class.toString(), e.getCause()
															 .getClass().toString());
					break;
				case CHECK_LOCAL_PC_CALLED:
					assertEquals(LocalPcCalledException.class.toString(), e.getCause()
							.getClass().toString());
					break;
				case ASSIGN_ARG_TO_LOCAL:
					assertEquals(AssignArgumentToLocalExcpetion.class.toString(), e.getCause()
							.getClass().toString());
					break;
				case JOIN_LEVEL_OF_LOCAL_AND_ASSIGNMENT_LEVEL:
					assertEquals(joinLevelOfLocalAndAssignmentLevelException.class.toString(), e.getCause()
							.getClass().toString());
					break;
				case SET_RETURN_AFTER_INVOKE:
					assertEquals(joinLevelOfLocalAndAssignmentLevelException.class.toString(), e.getCause()
							.getClass().toString());
					break;
				default:
					throw new InternalAnalyzerException("Unknown exception found!");
			}
		}
		*/
	}

	/**
	 *  Check that a particular (security) exception matches an expected one.
	 *  Call JUnits "fail" if not.
	 */
	private static void assertExpectedException(Throwable caughtException,
												SecurityMonitoringEvent currentEvent,
												SecurityMonitoringEvent expectedEvent) {
		if (!expectedEvent.equals(currentEvent)) {
			caughtException.printStackTrace();
			fail("Unexpected exception thrown:" + caughtException
				 + "\n Expected: " + expectedEvent);
		}
	}

	private static boolean hasClassNameOf(Throwable e, Class<? extends Throwable> eClass) {
		return e.getClass().toString().equals(eClass.toString());
	}
}
