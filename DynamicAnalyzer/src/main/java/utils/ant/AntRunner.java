package utils.ant;

import java.io.File;
import java.io.PrintWriter;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.ManifestException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ZipFileSet;
import utils.exceptions.InternalAnalyzerException;
import utils.parser.ArgumentContainer;
import utils.parser.PathHelper;


public class AntRunner {


	public static void run(ArgumentContainer sootArgsContainer) {
		


        Jar j = new Jar();
        j.setTaskName("build-jar-task");
        j.setBasedir(new File(""));
        j.setDestFile(new File(sootArgsContainer.getMainclassAbsolutePath()));

        // Set Manifest
        Manifest.Attribute mainClassMainifestAttribute = new Manifest.Attribute();
        mainClassMainifestAttribute.setName("Main-Class");
        mainClassMainifestAttribute.setValue(sootArgsContainer.getMainclass().replace("/", "."));
        Manifest manifest = new Manifest();
        try {
            manifest.addConfiguredAttribute(mainClassMainifestAttribute);
            j.addConfiguredManifest(manifest);
        } catch (ManifestException e) {
            e.printStackTrace();
        }


        // add stuff in output folder (temporary solution)
        FileSet outputFolder = new FileSet();
        outputFolder.setDir(new File(System.getProperty("user.dir"), "/DynamicAnalyzer/target/scala-2.11/classes"));
        outputFolder.setIncludes("**/*.class");
        j.addFileset(outputFolder);

        // add the Handle Statement
        FileSet handleStatement = new FileSet();
        handleStatement.setDir(new File(System.getProperty("user.dir"), "/DynamicAnalyzer/target/scala-2.11/classes"));
        handleStatement.setIncludes("**/*.class");
        j.addFileset(handleStatement);

        // add the Utils Folder
        FileSet utilsFolder = new FileSet();
        utilsFolder.setDir(new File(System.getProperty("user.dir"), "/DynamicAnalyzer/target/scala-2.11/classes"));
        utilsFolder.setIncludes("utils/**");
        j.addFileset(utilsFolder);

        // add sec Domain
        ZipFileSet secDomain = new ZipFileSet();
        secDomain.setSrc(new File(System.getProperty("user.dir"),
                "/GradualConstraints/InstrumentationSupport/target/scala-2.11/gradualconstraints_instrumentationsupport_2.11-0.1-SNAPSHOT.jar" ));
        secDomain.setIncludes("de/unifreiburg/cs/proglang/jgs/constraints/SecDomain.class");
        secDomain.setIncludes("de/unifreiburg/cs/proglang/jgs/constraints/secdomains/UserDefined.class");
        j.addZipfileset(secDomain);

        // add supplementary files
        ZipFileSet suppF = new ZipFileSet();
        suppF.setSrc(new File(System.getProperty("user.dir"), "/lib/commons-collections4-4.0.jar"));
        suppF.setIncludes("org/**");
        j.addZipfileset(suppF);

        // Add Jar Task to defaultTarget
        Target defaultTarget = new Target();
        defaultTarget.setName("build-jar");
        defaultTarget.addTask(j);

        // add defaultTarget to some Project P
        Project p = new Project();
        p.setName("Compile to Jar");
        p.addTarget(defaultTarget);
        p.setDefault(defaultTarget.toString());

		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(consoleLogger);

		if (p.getDefaultTarget().equals(null)) {
		    throw new InternalAnalyzerException("Default target is null");
        }

		try {
			p.fireBuildStarted();
			p.init();
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
