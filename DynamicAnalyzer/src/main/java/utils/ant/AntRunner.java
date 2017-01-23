package utils.ant;

import java.io.File;
import java.io.PrintWriter;
import org.apache.tools.ant.*;
import utils.parser.ArgumentContainer;
import utils.parser.PathHelper;


public class AntRunner {


	public static void run(ArgumentContainer sootArgsContainer) {
		
		// replace "." by "/", eg: testclasses.test1 => main/testclasses/test1
		createProperties(sootArgsContainer);

		File buildFile = new File("DynamicAnalyzer/src/main/java/utils/ant/build.xml");
		
		
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
			System.err.println(e);
			p.fireBuildFinished(e);
			
		}
	}


	private static void createProperties(ArgumentContainer argsContainer) {

        String mainClass = argsContainer.getMainclass().replace(".", "/");								// main/testclasses/DominatorNullPointer
        String outputFolder = argsContainer.getOutputFolderAbsolutePath();
        String fullPathForMainClassToCreate = new File(outputFolder, mainClass).getAbsolutePath();      // is absolute and points to the mainClass (without .class)

		try  {
			PrintWriter writer = new PrintWriter("DynamicAnalyzer/src/main/java/utils/ant/build.properties", "UTF-8");
			writer.write("# File to set mainClass and outputFolder\n");
			writer.write("mainClass=" + mainClass + "\n");
			writer.write("fullPathForMainClassToCreate=" + fullPathForMainClassToCreate + "\n");
			writer.write("outputFolder=" + outputFolder + "\n");
			writer.write("working_dir=" + System.getProperty("user.dir"));
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
