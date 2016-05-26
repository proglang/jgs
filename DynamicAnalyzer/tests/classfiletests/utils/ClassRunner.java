package classfiletests.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

import utils.exceptions.IllegalFlowException;
import utils.logging.L1Logger;

import java.util.logging.Logger;

/**
 * Runs given classes.
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
		task.setProject(project);
		
		target.addTask(task);
		
		project.addTarget(target);
		project.setDefault(TARGET_NAME);

		target.execute();
		project.executeTarget(project.getDefaultTarget());

	}
	
	/**
	 * @param className
	 * @param expectedException
	 */
	public static void testClass(String className, boolean expectedException) {

		Exception catchedException = null;
		try {
			runClass(className);
			if (expectedException) {
				System.out.println("ABCDEF"); 
				fail();
			}
		} catch (Exception e) {
			System.out.println("Exception"); 
			catchedException = e;
			System.out.println(e.getClass());
			assertEquals(catchedException.getCause().getClass(),
					IllegalFlowException.class);
			System.out.println(e.toString());
			System.out.println("GHIJKLM");
			e.printStackTrace();
			if (!expectedException) {
				fail();
			}
		}

	}	
	
}