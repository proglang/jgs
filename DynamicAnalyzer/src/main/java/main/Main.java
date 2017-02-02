package main;

import analyzer.level1.BodyAnalyzer;
import analyzer.level2.storage.LowMediumHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import soot.*;
import utils.logging.L1Logger;
import utils.parser.ArgParser;
import utils.parser.ArgumentContainer;
import utils.staticResults.ResultsServer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;


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
	public static void main(String[] args) {
		execute(args, false, null, null, null);
	}

	/**
	 * Run main with fake variable typing results.
	 * @param args			arguments as list of strings
	 * @param varTyping		fake var Typing map
	 * @param cxTyping		fake cx Typing map
	 * @param instantiation	fake instantiation map
	 */
	public static void mainWithFakeResults(String[] args,
										   Map<SootMethod, VarTyping> varTyping,
										   Map<SootMethod, CxTyping> cxTyping,
										   Map<SootMethod, Instantiation> instantiation) {
		execute(args, true, varTyping, cxTyping, instantiation);
	}

	/**
     * Method which configures and executes soot.main.Main.
     * @param args This arguments are delivered by main.Main.main.
     */
	private static void execute(String[] args,
								boolean useFakeTyping,
								Map<SootMethod, VarTyping> varTyping,
								Map<SootMethod, CxTyping> cxTyping,
								Map<SootMethod, Instantiation> instantiation) {
        Level LOGGER_LEVEL = Level.ALL;
		ArgumentContainer sootOptionsContainer = ArgParser.getSootOptions(args);
        LinkedList<String> sootOptions = new LinkedList<>(Arrays.asList(
                sootOptionsContainer.getMainclass(),                    // adds the mainclass file
                "-main-class", sootOptionsContainer.getMainclass(),     // specifies which file should be the mainclass
                "-f", sootOptionsContainer.getOutputFormat(),           // sets output format
                "--d", sootOptionsContainer.getOutputFolderAbsolutePath()));         // sets output folder
		for (String s : sootOptionsContainer.getAdditionalFiles()) {
		    sootOptions.add(s);                                                         // add further files to be instrumented (-f flag)
        }
        try {
			System.out.println("Logger Init1");
			L1Logger.setup(LOGGER_LEVEL);
		} catch (IOException e) {
			e.printStackTrace();
		}

        String javaHome = System.getProperty("java.home");	//gets the path to java home, here: "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre"

		if (javaHome == null) {
			throw new IllegalStateException("System property `java.home' is undefined");
		}
    	
		// Setting the soot classpath
		String classPath = Scene.v().getSootClassPath()
				+ ":.:"
				+ new File(javaHome, "lib/jce.jar").toString()
			    + ":"
				+ new File(javaHome, "lib/rt.jar").toString();

		// Adding the arguments given by the user via the -p flag. See utils.parser.ArgParser
		for (String s : sootOptionsContainer.getAddDirsToClasspath()) {
			classPath += ":" + s;
		}
		Scene.v().setSootClassPath(classPath);


        // ====== Create / load fake static analysis results =====
        Map<SootMethod, VarTyping> fakeVarTypingsMap;
		Map<SootMethod, CxTyping> fakeCxTypingsMap;
		Map<SootMethod, Instantiation> fakeInstantiationMap;

		// if no fake Typing is supplied, make everything dynamic
		if (! useFakeTyping) {
			fakeVarTypingsMap = new HashMap<>();
			fakeCxTypingsMap = new HashMap<>();
			fakeInstantiationMap = new HashMap<>();

            Collection<String> allClasses = sootOptionsContainer.getAdditionalFiles();
			allClasses.add(sootOptionsContainer.getMainclass());

			ResultsServer.setDynamicVar(fakeVarTypingsMap, allClasses);
			ResultsServer.setDynamicCx(fakeCxTypingsMap, allClasses);
			ResultsServer.setDynamicInst(fakeInstantiationMap, allClasses);

		} else {
			fakeVarTypingsMap = varTyping;
			fakeCxTypingsMap = cxTyping;
			fakeInstantiationMap = instantiation;
		}
        // =================================

        // those are needed because of soot-magic i guess
        Scene.v().addBasicClass("analyzer.level2.HandleStmt");
		Scene.v().addBasicClass("analyzer.level2.SecurityLevel");

        BodyAnalyzer<LowMediumHigh.Level> banalyzer = new BodyAnalyzer(fakeVarTypingsMap, fakeCxTypingsMap, fakeInstantiationMap);

		PackManager.v()
        	.getPack("jtp").add(new Transform("jtp.analyzer", banalyzer)); 

        soot.Main.main(sootOptions.toArray(new String[sootOptions.size()]));
        
		// compile to JAR.
		utils.ant.AntRunner.run(sootOptionsContainer);
        
		// for multiple runs, soot needs to be reset, which is done in the following line
		G.reset();

		// open question:
		// was ist der empfohlene weg, exceptions zu werfen aus einer analyse heraus.
		// unsere situation: Rufen main.Main in unit tests auf, wewnn wir einmal expcept werfen, bricht
		// alles ab, obwohl wir resetten.
        
	}


}
