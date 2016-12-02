package main;

import analyzer.level1.BodyAnalyzer;
import soot.G;
import soot.PackManager;
import soot.Scene;
import soot.Transform;
import utils.logging.L1Logger;
import utils.parser.ArgParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.commons.cli.ParseException;


/**
 * @author Regina Koenig, Nicolas Müller
 */
public class Main {

	private static Level LOGGER_LEVEL;


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
	 * -f c --classes main.testclasses.Simple  --main_class main.testclasses.Simple
	 * 
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) {
		execute(args);
	}

	/**
     * Method which configures and executes soot.Main.
     * @param args This arguments are delivered by Main.main.
	 * @throws ParseException 
     */
	private static void execute(String[] args) {
		
		//argparser = new ArgumentParser(args);	//args are the arguments for soot, like "-f c --classes main.testclasses.Simple ..."
    	
		// LOGGER_LEVEL = argparser.getLoggerLevel();
		// String[] sootOptions = argparser.getSootOptions();	// sootOptions is basically the same as args (it misses --classes, for some reason)
		
		LOGGER_LEVEL = Level.ALL;
		ArrayList<String> pathArgs = new ArrayList<String>();
		String[] sootOptions = ArgParser.getSootOptions(args, pathArgs);
		
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
		// Adding the arguments given by the user via the -p flag. See ArgParser.java
		for (String s : pathArgs) {
			classPath += ":" + s;
		}
		Scene.v().setSootClassPath(classPath);
		Scene.v().addBasicClass("analyzer.level2.HandleStmt");
		Scene.v().addBasicClass("analyzer.level2.SecurityLevel");

		
		BodyAnalyzer banalyzer = new BodyAnalyzer();
        

		PackManager.v()
        	.getPack("jtp").add(new Transform("jtp.analyzer", banalyzer)); 

        	   
		soot.Main.main(sootOptions);
		
		//compile to JAR. Currently, sootOptions[3] is the mainClass (like main.testclasses.test1).
		// it gets compiled to sootOutput/junit/main/testclasses/test1.class
		// we want to output it to ant/main/testclasses/test1.jar
		// [-f, c, -main-class, main.testclasses.NSU_FieldAccess, main.testclasses.NSU_FieldAccess, --d, sootOutput/junit]
		File pathToMain = new File(new File(sootOptions[6]).getAbsolutePath(), sootOptions[4]);
		utils.ant.AntRunner.run(args[0], pathToMain.getAbsolutePath(), sootOptions[6]);
		
		// for multiple runs, soot needs to be reset, which is done in the following line
		G.reset();

	}
}
