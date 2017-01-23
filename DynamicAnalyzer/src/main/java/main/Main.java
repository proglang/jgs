package main;

import analyzer.level1.BodyAnalyzer;
import analyzer.level1.storage.Dynamic;
import analyzer.level1.storage.SecValueTuple;
import analyzer.level2.storage.LowMediumHigh;
import soot.*;
import soot.jimple.Stmt;
import soot.options.Options;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L1Logger;
import utils.parser.ArgParser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.cli.ParseException;
import utils.parser.ArgumentContainer;
import utils.staticResults.staticResultFaker;


/**
 * @author Regina Koenig, Nicolas Müller
 */
public class Main {


    /**
	 * The entry point for compilation and instrumentation (that is, adding the appropriate
	 * code to check for information leak). Use appropriate arguments to indicate
	 * which test will be compiled, and what the output format should be.
	 * 
	 * Note for eclipse users: Comfortable execution via different run configurations,
	 * where you can choose between compilation to instrumented binary (RunMainAnalyzerSingleC) 
	 * and compilation to the intermediate, instrumented jimple formate (RunMainAnalyzerSingleJ)
	 * 
	 * For illustration, we supply the command line arguments to compile a single file to 
	 * instrumented binary code:
	 * -f c --classes testclasses.Simple  --main_class testclasses.Simple
	 * 
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) {
		execute(args);
	}



	/**
     * Method which configures and executes soot.main.Main.
     * @param args This arguments are delivered by main.Main.main.
	 * @throws ParseException 
     */
	private static void execute(String[] args) {
		
		//argparser = new ArgumentParser(args);	//args are the arguments for soot, like "-f c --classes testclasses.Simple ..."
    	
		// LOGGER_LEVEL = argparser.getLoggerLevel();
		// String[] sootOptions = argparser.getSootOptions();	// sootOptions is basically the same as args (it misses --classes, for some reason)



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

        // ====== Create fake results =====
        Map<Stmt, Map<Local, SecValueTuple>> var_result = new HashMap<>();
        SootClass sootClass = Scene.v().loadClassAndSupport(sootOptionsContainer.getMainclass());
        sootClass.setApplicationClass();
        Body sootBody = sootClass.getMethodByName("main").retrieveActiveBody();

        staticResultFaker.make(sootBody, var_result);




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
		Scene.v().addBasicClass("analyzer.level2.HandleStmt");
		Scene.v().addBasicClass("analyzer.level2.SecurityLevel");

        BodyAnalyzer banalyzer = new BodyAnalyzer();

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
