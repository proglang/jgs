package classfiletests.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

import utils.exceptions.IllegalFlowException;
import utils.logging.L1Logger;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;
import java.io.File;

import junit.framework.Test;

/**
 * Helper Class to run a given binary class file, and see whether of not it
 * throws the desired exception. Used by the end-to-end tests.
 * 
 * @author Regina Koenig, Nicolas Müller
 */
public class ClassRunner {

	private static Logger logger = L1Logger.getLogger();
	private static final String TARGET_NAME = "exec";

	/**
	 * Runs the given class via Apache Ant.
	 * 
	 * @param className
	 *            class to be run
	 */
	private static void runClass(String className, String outputDir) {

		Project project = new Project();
		project.setName("ClassRunner");
		project.init();

		Target target = new Target();
		target.setName(TARGET_NAME);

		Java task = new Java();
		Path path = task.createClasspath();
		path.createPathElement().setPath("./sootOutput/" + outputDir);
		path.createPathElement().setPath("./bin");

		for (URL url : ((URLClassLoader) ClassLoader.getSystemClassLoader())
				.getURLs()) {
			path.createPathElement().setPath(url.getPath());
		}
		task.setClasspath(path);
		task.setClassname("testclasses." + className);
		task.setFork(false);
		task.setFailonerror(true);
		task.setProject(project);

		target.addTask(task);

		project.addTarget(target);
		project.setDefault(TARGET_NAME);

		target.execute();
		project.executeTarget(project.getDefaultTarget());

	}

	/**
	 * Run class and check whether the expected exception is found.
	 * 
	 * @param className
	 *            Name of the class.
	 * @param exceptionExpected
	 *            true if an exception is expected
	 * 
	 * @author Regina Koenig, Nicolas Müller
	 */
	public static void testClass(String className, String outputDir,
			boolean exceptionExpected, String... involvedVars) {
		
		logger.info("Trying to run test " + className);

		String fullPath = System.getProperty("user.dir") + "/sootOutput/"
				+ outputDir + "/testclasses/" + className + ".class";

		if (!new File(fullPath).isFile()) {
			logger.severe("File "
					+ fullPath
					+ " not found. Something weird is going on, because the test should automatically"
					+ "compile the desired file and put it into the correct folder.");
			fail();
		}

		try {
			runClass(className, outputDir);

			// Fail if the class was running but an exception was expected
			if (exceptionExpected) {
				logger.severe("Expected exception is not found");
				fail();
			}
		} catch (Exception e) {
			logger.severe("Found exception " + e.getClass().toString());
			e.printStackTrace();

			// Fail if an exception is thrown but no exception was expected
			if (!exceptionExpected) {
				logger.severe("Fail because exception was not expected");
				fail();
			}

			// Fail if an exception is thrown which is not the expected
			// exception
			assertEquals(IllegalFlowException.class.toString(), e.getCause()
					.getClass().toString());
			
			// Fail if illegalFlowException is thrown, expected, but no
			// involvedVars are supplied
			if (exceptionExpected && e.getCause().getClass().toString().contains("IllegalFlow")) {
				if (involvedVars.length < 1) {
					logger.warning("No variables supplied for IllegalFlowException");					
				}
			}
			

			// Check if the expected variables are involved
			for (String var : involvedVars) {
				logger.info("Check wheter " + var + " is contained in: "
						+ e.getMessage() + " ... ");
				if (e.getMessage().contains(var)) {
					logger.info("Success!");
				} else {
					logger.severe("Fail, Exception thrown, but incorrect variable!");
				}
				assertTrue(e.getMessage().contains(var));
			}

		}

	}

}