package main;

import analyzer.level1.BodyAnalyzer;
import analyzer.level2.storage.LowMediumHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.instrumentation.*;
import soot.*;
import utils.jimple.JimpleFactory;
import utils.logging.L1Logger;
import utils.parser.ArgParser;
import utils.parser.ArgumentContainer;
import utils.staticResults.*;
import utils.staticResults.superfluousInstrumentation.FixedTypings;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.SignatureSpi;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static soot.SootClass.SIGNATURES;


/**
 * @author Regina Koenig, Nicolas Müller
 */
public class Main {


    /**
	 * The entry point for compilation and instrumentation (that is, adding the appropriate
	 * code to check for information leak). Use appropriate arguments to indicate
	 * which test will be compiled, and what the output format should be.
	 *
     * To see which command line args to use, go to the parser in utils.parser, or run the main (this one) without any
     * arguments, which'll print the help.
	 *
	 * @param args Commandline-Args for analysis
	 */
	public static void main(String[] args)
	{
		execute(args, false, null,  false, 0, null);
	}


	public static void execute(String[] args,
								boolean useExternalTyping,
								Methods m,
								boolean controllerIsActive,
								int expectedException,
							    Casts c) {


	    doSootSetup(args);

		executeWithoutSootSetup(args, useExternalTyping, m, controllerIsActive, expectedException, c);
	}


	// TODO: move to another package (or even project) as this kind of setup is used by the whole application, not only DA
    public static void doSootSetup(String[] args) {

        ArgumentContainer sootOptionsContainer = ArgParser.getSootOptions(args);

		try {
		    if (sootOptionsContainer.isVerbose()) {
		 		L1Logger.setup(Level.ALL);
                L1Logger.getLogger().info("Verbose logging activated");
			} else {
				L1Logger.setup(Level.WARNING);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

        String javaHome = System.getProperty("java.home");    //gets the path to java home, here: "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre"


        if (javaHome == null) {
            throw new IllegalStateException("System property `java.home' is undefined");
        }

        // Setting the soot classpath
        String classPath = Scene.v().getSootClassPath()
                + ":.:"
                + new File(javaHome, "lib/jce.jar").toString()
                + ":"
                + new File(javaHome, "lib/rt.jar").toString();
		/*
		String classPath = "."
						   + ":"
						   + new File(javaHome, "lib/jce.jar").toString()
						   + ":"
						   + new File(javaHome, "lib/rt.jar").toString();
						   */

        // Adding the arguments given by the user via the -p flag. See utils.parser.ArgParser
        for (String s : sootOptionsContainer.getAddDirsToClasspath()) {
            classPath = s + ":" + classPath;
        }
        // TODO: why both?
        for (String s : sootOptionsContainer.getAddClassesToClasspath()) {
            classPath += ":" + s;
        }

        // Add the current classpath to soots classpath
		// TODO: this is only a quick hack for testing. We should figure out precicely how the soot classpath should look.
		List<String> cpath = new ArrayList<>();
        // TODO: Lu: I'm also not sure what a "ContextClassLoader" is. (pasted from https://stackoverflow.com/questions/11613988/how-to-get-classpath-from-classloader)
        ClassLoader cxClassloader = Thread.currentThread().getContextClassLoader();
        if (cxClassloader instanceof URLClassLoader) {
			for (URL url : Arrays.asList(((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs())) {

				cpath.add(url.getFile());
			}
		} else {
        	throw new RuntimeException("Cannot get URLs needed for the soot classpath from current contextclassloader");
		}

		Scene.v().setSootClassPath(String.join(":", cpath) + ":" + classPath);

		L1Logger.getLogger().info("Soot classpath: " + Scene.v().getSootClassPath());

        // those are needed because of soot-magic i guess
        Scene.v().addBasicClass("analyzer.level2.HandleStmt", SIGNATURES);
        Scene.v().addBasicClass("analyzer.level2.SecurityLevel", SIGNATURES);
    }


	/**
     * Method which configures and executes soot.main.Main.
     * @param args This arguments are delivered by main.Main.main.
     */
	public static void executeWithoutSootSetup(String[] args,
								boolean useExternalTyping,
								Methods m,
								boolean controllerIsActive,
								int expectedException,
							    Casts c) {

		ArgumentContainer sootOptionsContainer = ArgParser.getSootOptions(args);
        LinkedList<String> sootOptions = new LinkedList<>(Arrays.asList(
                sootOptionsContainer.getMainclass(),                    // adds the mainclass file
                //"-main-class", sootOptionsContainer.getMainclass(),     // specifies which file should be the mainclass
                "-f", sootOptionsContainer.getOutputFormat(),           // sets output format
                "--d", sootOptionsContainer.getOutputFolderAbsolutePath()
				));         // sets output folder
		for (String s : sootOptionsContainer.getAdditionalFiles()) {
		    sootOptions.add(s);                                                         // add further files to be instrumented (-f flag)
        }

        // ====== Create / load fake static analysis results ======
		Methods methods = m;
		Casts casts = c;

		// if no external Typing is supplied, make one up
		if (! useExternalTyping) {

            Collection<String> allClasses = sootOptionsContainer.getAdditionalFiles();
			allClasses.add(sootOptionsContainer.getMainclass());

			// the default casts object. used only if explicitely specified that none is supplied externally
			casts =
					new CastsFromConstants<>(new TypeDomain<>(new LowMediumHigh()),
							"<de.unifreiburg.cs.proglang.jgs.support.Casts: java.lang.Object cast(java.lang.String,java.lang.Object)>",
							"de.unifreiburg.cs.proglang.jgs.instrumentation.Casts.castCx",
							"de.unifreiburg.cs.proglang.jgs.instrumentation.Casts.castCxEnd");


			if (sootOptionsContainer.usePublicTyping()) {
				methods = ResultsServer.createAllPublicMethods(allClasses);
			} else {
				methods = FixedTypings.allDynamic();
			}
		}
        // =================================



        BodyAnalyzer<LowMediumHigh.Level> banalyzer = new BodyAnalyzer(methods, controllerIsActive, expectedException, casts);

		PackManager.v()
        	.getPack("jtp").add(new Transform("jtp.analyzer", banalyzer));


		soot.Main.main(sootOptions.toArray(new String[sootOptions.size()]));

		// compile to JAR.
		try {
			utils.ant.AntRunner.run(sootOptionsContainer);
		} catch (Exception e) {
			L1Logger.getLogger().warning("ERROR trying to build the final jar: "
										 + e.getMessage());
		}

		// for multiple runs, soot needs to be reset, which is done in the following line
		G.reset();
		JimpleFactory.reset();

		// open question:
		// was ist der empfohlene weg, exceptions zu werfen aus einer analyse heraus.
		// unsere situation: Rufen main.Main in unit tests auf, wewnn wir einmal expcept werfen, bricht
		// alles ab, obwohl wir resetten.

	}


}
