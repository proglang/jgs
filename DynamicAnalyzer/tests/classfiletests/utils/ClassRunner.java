package classfiletests.utils;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

import utils.logging.L1Logger;

import java.util.logging.Logger;

public class ClassRunner {

	static String s = null;
	static boolean error_recognized = false;
	static String fileName = "";
	static Logger logger = L1Logger.getLogger();

	public static void runClass(String className) {
		
		Project project = new Project();
		project.setUserProperty("ant.file", ".");
		project.init();
		
		Target target = new Target();
		target.setName("abc");
		
		Java task = new Java();
		Path path = new Path(project);
		path.setPath("./sootOutput:../bin/"
				+ ":../../../dependencies/commons-collections4-4.0/"
				+ "commons-collections4-4.0.jar");
		System.out.println("Path: " + path.toString());
		task.setClasspath(path);
		task.setClassname("main.testclasses." + className);
		task.setFork(true);
		
		target.addTask(task);
		
		project.addTarget(target);

		System.out.println("Class: " + project.getTargets().toString());

		project.executeTarget("exec");
	}
}