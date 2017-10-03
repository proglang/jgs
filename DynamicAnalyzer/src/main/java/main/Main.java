package main;

import analyzer.level1.BodyAnalyzer;
import de.unifreiburg.cs.proglang.jgs.instrumentation.*;
import soot.*;
import utils.logging.L1Logger;
import utils.parser.ArgParser;
import utils.parser.ArgumentContainer;
import de.unifreiburg.cs.proglang.jgs.typing.FixedTypings;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;

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
		ArgumentContainer sootOptionsContainer = ArgParser.getSootOptions(args);
		MethodTypings methodTypings;
		if (sootOptionsContainer.usePublicTyping()) {
			methodTypings = FixedTypings.allPublic();
		} else {
			methodTypings = FixedTypings.allDynamic();
		}

		execute(args, FixedTypings.allDynamic(), NoCasts.apply());
	}


	public static void execute(String[] args,
								MethodTypings m,
							    Casts c) {


	    doSootSetup(args);

		executeWithoutSootSetup(args, m, c);
	}


	// TODO: move to another package (or even project) as this kind of setup is used by the whole application, not only DA
    public static void doSootSetup(String[] args) {

        ArgumentContainer sootOptionsContainer = ArgParser.getSootOptions(args);

		try {
		    if (sootOptionsContainer.isVerbose()) {
		 		L1Logger.setup(Level.ALL);
                L1Logger.getLogger().info("Verbose logging activated");
			} else {
				L1Logger.setup(Level.FINE);
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
        // TODO: Lu: I'm also not sure what a "ContextClassLoader" is.
		// (pasted from https://stackoverflow.com/questions/11613988/how-to-get-classpath-from-classloader)
        ClassLoader cxClassloader = Thread.currentThread().getContextClassLoader();
        if (cxClassloader instanceof URLClassLoader) {
			for (URL url : Arrays.asList(((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs())) {

				cpath.add(url.getFile());
			}
		} else {
        	// throw new RuntimeException("Cannot get URLs needed for the soot classpath from current contextclassloader");
			L1Logger.getLogger().warning("Cannot get URLs needed for the soot classpath from current contextclassloader.\n "
										 + "(Ignore this warning when running under sbt).");
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
	public static <L> void executeWithoutSootSetup(String[] args,
												   MethodTypings<L> m,
												   Casts<L> c) {

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
		MethodTypings<L> methodTypings = m;
		Casts<L> casts = c;


        BodyAnalyzer<L> banalyzer = new BodyAnalyzer<>(methodTypings, casts);

		PackManager.v()
        	.getPack("jtp").add(new Transform("jtp.analyzer", banalyzer));


		soot.Main.main(sootOptions.toArray(new String[sootOptions.size()]));


		// for multiple runs, soot needs to be reset, which is done in the following line
		G.reset();

		// open question:
		// was ist der empfohlene weg, exceptions zu werfen aus einer analyse heraus.
		// unsere situation: Rufen main.Main in unit tests auf, wewnn wir einmal expcept werfen, bricht
		// alles ab, obwohl wir resetten.

	}


}
