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
 * Runs given class.
 * @author Regina Koenig
 */
public class ClassRunner {

	static Logger logger = L1Logger.getLogger();
	static final String TARGET_NAME = "exec";

	/**
	 * Runs the given class via Apache Ant.
	 * @param className class to be run
	 */
	private static void runClass(String className) {
		
		Project project = new Project();
		project.setName("ClassRunner");
		project.init();
		
		Target target = new Target();
		target.setName(TARGET_NAME);
		
		
		Java task = new Java();
		Path path = task.createClasspath();
		path.createPathElement().setPath("./sootOutput");
		path.createPathElement().setPath("./bin");
		
		for (URL url : ((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs()) {
			path.createPathElement().setPath(url.getPath());
		}
		task.setClasspath(path);
		task.setClassname("main.testclasses." + className);
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
	 * @author NicolasM
	 * 
	 * Run class and check whether the expected exception is found.
	 * Note that compiled binary must exist in sootOutput, else it will perform the following bug:
	 * a) If no invalid flow is specified, test will always pass.
	 * b) If invalid flow is specified, test will always pass.
	 * This is why we add a check that the compiled source file must exist, else we fail the test completely!
	 * @param className Name of the class.
	 * @param expectedException true if an exception is expected
	 */
	public static void testClass(String className, 
			boolean expectedException, String... involvedVars) {
	
		String fullPath = System.getProperty("user.dir") + "/sootOutput/main/testclasses/" + className + ".class";
		
		if(!new File(fullPath).isFile()) { 
			logger.severe("File " + fullPath + " not found. Please compile with soot and try again!"); 
			fail();
		}

		try {
			runClass(className);
			
			// Fail if the class was running but an exception was expected
			if (expectedException) {
				logger.severe("Expected exception is not found"); 
				fail();
			}
		} catch (Exception e) {
			logger.severe("Found exception " + e.getClass().toString()); 
			e.printStackTrace();
			
			// Fail if an exception is thrown but no exception was expected
			if (!expectedException) {
				logger.severe("Fail because exception was not expected");
				fail();
			}
			
			// Fail an exception is thrown which is not the expected exception
			assertEquals(IllegalFlowException.class.toString(),
					e.getCause().getClass().toString());
			
			// Check if the expected variables are involved
			for (String var : involvedVars) {
				System.out.print("Assert: Is " + var + " contained in: " + e.getMessage() + " ... ");
				if (e.getMessage().contains(var)) {
					System.out.println("Yes, it is!");
				} else {
					System.out.println("No, it is not! This makes the test fail!");
				}
				assertTrue(e.getMessage().contains(var));
			}

		}

	}	
	
}