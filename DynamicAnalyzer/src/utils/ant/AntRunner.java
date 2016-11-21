package utils.ant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.tools.ant.*;

/**
 * Helper to run ant for us.
 * 
 * @author Nicolas Müller
 *
 */
public class AntRunner {
	public static void main(String[] args) {
		
		createProperties("main/testclasses/ImplicitFlow2", "sootOutput/ant");
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
	 * @param mainClass		which file to compile
	 * @param outputFolder 	where to output
	 */
	private static void createProperties(String mainClass, String outputFolder) {
		try  { 
			PrintWriter writer = new PrintWriter("src/utils/ant/build.properties", "UTF-8");
			writer.write("# File to set mainClass and outputFolder\n");
			writer.write("mainClass=" + mainClass + "\n");
			writer.write("outputFolder=" + outputFolder + "\n");	
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
