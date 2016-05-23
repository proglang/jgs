package classfiletests.utils;

import static org.junit.Assert.assertEquals;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

import utils.exceptions.IllegalFlowException;
import utils.logging.L1Logger;

import java.util.logging.Logger;

/**
 * @author Regina Koenig
 *
 */
public class ClassRunner {

	static String s = null;
	static boolean error_recognized = false;
	static String fileName = "";
	static Logger logger = L1Logger.getLogger();
	static String targetName = "exec";

	/**
	 * @param className
	 */
	public static void runClass(String className) {
		Exception catchedException = null;
		try {
		
		Project project = new Project();
		project.setName("ClassRunner");
		// project.setUserProperty("ant.file", ".");
		project.init();
		
		Target target = new Target();
		target.setName(targetName);
		
		Java task = new Java();
		Path path = new Path(project);
		path.setPath("./sootOutput:./bin/"
				+ ":./../../dependencies/commons-collections4-4.0/"
				+ "commons-collections4-4.0.jar");
		System.out.println("Path: " + path.toString());
		task.setClasspath(path);
		task.setClassname("main.testclasses." + className);
		task.setFork(false);
		task.setProject(project);
		
		target.addTask(task);
		
		project.addTarget(target);
		project.setDefault(targetName);

		System.out.println("Class: " + project.getTargets().toString());

		target.execute();
		project.executeTarget(project.getDefaultTarget());
	} catch (Exception e) {
		catchedException = e;
		assertEquals(catchedException.getCause().getClass(),
				IllegalFlowException.class);
		System.out.println(e.toString());
		e.printStackTrace();
	}
	}
}