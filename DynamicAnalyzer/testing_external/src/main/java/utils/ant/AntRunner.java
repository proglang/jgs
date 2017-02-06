package utils.ant;

import java.io.File;
import java.io.PrintWriter;
import org.apache.tools.ant.*;


public class AntRunner {
	public static void main(String[] args) {
		
		String mainClass = "testclasses/AccessFieldsOfObjectsFail";
		String pathToMainClass ="sootOutput";
		String outputFolder = "ant";
		
		run(mainClass, pathToMainClass, outputFolder);
	}
	
	/***
	 * Helper to run ant	
	 * @param mainClass				mainClass
	 * @param pathToMainClass		path to the mainClass
	 * @param outputFolder			output folder
	 */
	public static void run(String mainClass, String pathToMainClass, String outputFolder) {
		
		// replace "." by "/", eg: testclasses.test1 => main/testclasses/test1
		mainClass = mainClass.replace(".", "/");
		pathToMainClass = pathToMainClass.replace(".", "/");
		outputFolder = outputFolder.replace(".", "/");
		
		createProperties(mainClass, pathToMainClass, outputFolder);
		File buildFile = new File("src/utils/ant/build.xml");
		
		
		Project p = new Project();
		p.setUserProperty("ant.file", buildFile.getAbsolutePath());
		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(consoleLogger);

		try {
			p.fireBuildStarted();
			p.init();
			ProjectHelper helper = ProjectHelper.getProjectHelper();
			p.addReference("ant.projectHelper", helper);
			helper.parse(p, buildFile);
			p.executeTarget(p.getDefaultTarget());
			p.fireBuildFinished(null);
		} catch (BuildException e) {
			p.fireBuildFinished(e);
		}
	}

	/**
	 * Helper method to create properties for our ant file
	 * @param mainClass			which file to compile
	 * @param pathToMainClass	folder where the mainClass is located
	 * @param outputFolder 		where to output
	 */
	private static void createProperties(String mainClass, String pathToMainClass, String outputFolder) {
		try  { 
			PrintWriter writer = new PrintWriter("src/utils/ant/build.properties", "UTF-8");
			writer.write("# File to set mainClass and outputFolder\n");
			writer.write("mainClass=" + mainClass + "\n");
			writer.write("pathToMainClass=" + pathToMainClass + "\n");
			writer.write("outputFolder=" + outputFolder + "\n");	
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
