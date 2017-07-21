package utils.ant;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.ManifestException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ZipFileSet;
import utils.exceptions.InternalAnalyzerException;
import utils.parser.ArgumentContainer;
import utils.parser.PathHelper;


// TODO: replace with an task that copies all required run-time classes into a directory... jar is just a bonus
// TODO: Adapt the jar-build-test (or what they are called) to use that directory
public class AntRunner {


	public static void run(ArgumentContainer sootArgsContainer) {


        // folder working dir
        String folder_working_dir = PathHelper.getBaseDirOfProjectViaClassloader().getPath();

        Jar j = new Jar();
        j.setTaskName("build-jar-task");
        j.setDestFile(new File(sootArgsContainer.getOutputFolderAbsolutePath(), sootArgsContainer.getMainclass().replace(".", "/") + ".jar"));

        // Set Manifest
        Manifest.Attribute mainClassMainifestAttribute = new Manifest.Attribute();
        mainClassMainifestAttribute.setName("Main-Class");
        mainClassMainifestAttribute.setValue(sootArgsContainer.getMainclass());
        Manifest manifest = new Manifest();
        try {
            manifest.addConfiguredAttribute(mainClassMainifestAttribute);
            j.addConfiguredManifest(manifest);
        } catch (ManifestException e) {
            e.printStackTrace();
        }

        // add stuff in output folder (temporary solution), which include instrumented main and other classes
        FileSet outputFolder = new FileSet();
        outputFolder.setDir(new File( sootArgsContainer.getOutputFolderAbsolutePath()));
        outputFolder.setIncludes("**/*.class");
        j.addFileset(outputFolder);

        // add the Handle Statement
        FileSet handleStatement = new FileSet();
        handleStatement.setDir(new File(folder_working_dir, "DynamicAnalyzer/target/scala-2.11/classes"));
        handleStatement.setIncludes("analyzer/level2/**");
        j.addFileset(handleStatement);

        // add the Utils Folder
        FileSet utilsFolder = new FileSet();
        utilsFolder.setDir(new File(folder_working_dir, "DynamicAnalyzer/target/scala-2.11/classes"));
        utilsFolder.setIncludes("utils/**");
        j.addFileset(utilsFolder);

        // add sec Domain
        ZipFileSet secDomain = new ZipFileSet();
        secDomain.setSrc(new File(folder_working_dir,
                "GradualConstraints/InstrumentationSupport/target/scala-2.11/gradualconstraints_instrumentationsupport_2.11-0.1-SNAPSHOT.jar" ));
        secDomain.setIncludes("de/unifreiburg/cs/proglang/jgs/constraints/SecDomain.class");
        secDomain.setIncludes("de/unifreiburg/cs/proglang/jgs/constraints/secdomains/UserDefined.class");
        secDomain.setIncludes("de/unifreiburg/cs/proglang/jgs/constraints/secdomains/UserDefined$Edge.class");
        j.addZipfileset(secDomain);

            // add JGSSupport
            FileSet jgsSupport = new ZipFileSet();
            jgsSupport.setDir(new File(folder_working_dir,
                                      "GradualConstraints/JGSSupport/target/scala-2.11/classes" ));
            jgsSupport.setIncludes("**/*.class");

//            secDomain.setIncludes("de/unifreiburg/cs/proglang/jgs/constraints/SecDomain.class");
//            secDomain.setIncludes("de/unifreiburg/cs/proglang/jgs/constraints/secdomains/UserDefined.class");
//            secDomain.setIncludes("de/unifreiburg/cs/proglang/jgs/constraints/secdomains/UserDefined$Edge.class");
        j.addFileset(jgsSupport);

        // add supplementary files
        ZipFileSet suppF = new ZipFileSet();
        suppF.setSrc(new File(folder_working_dir, "lib/commons-collections4-4.0.jar"));
        suppF.setIncludes("org*//**//**//**//**");
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
        defaultTarget.setProject(p);

		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(consoleLogger);
        j.setProject(p);

		p.fireBuildStarted();
		p.init();
		p.executeTarget(p.getDefaultTarget());
		p.fireBuildFinished(null);

	}
}
