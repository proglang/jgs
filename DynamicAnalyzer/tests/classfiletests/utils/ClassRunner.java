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

import java.util.logging.Logger;

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
		Path path = new Path(project);
		path.setPath("./sootOutput:./bin/"
				+ ":./../../dependencies/commons-collections4-4.0/"
				+ "commons-collections4-4.0.jar");
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
	 * Run class and check whether the expected exception is found.
	 * @param className Name of the class.
	 * @param expectedException true if an exception is expected
	 */
	public static void testClass(String className, 
			boolean expectedException, String... involvedVars) {

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
			assertEquals(e.getCause().getClass().toString(),
					IllegalFlowException.class.toString());
			
			// Check if the expected variables are involved
			for (String var : involvedVars) {
				assertTrue(e.getMessage().contains(var));
			}

		}

	}	
	
}