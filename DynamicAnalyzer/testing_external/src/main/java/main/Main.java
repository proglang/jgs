package main;

import analyzer.level1.BodyAnalyzer;
import soot.G;
import soot.PackManager;
import soot.Scene;
import soot.Transform;
import util.logging.L1Logger;
import util.parser.ArgumentParser;
import util.parser.Parser2;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.cli.ParseException;


/**
 * @author Regina Koenig, Nicolas Müller
 */
public class Main {

	private static Level LOGGER_LEVEL;

	private static ArgumentParser argparser;

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
     * Method which configures and executes soot.Main.
     * @param args This arguments are delivered by Main.main.
	 * @throws ParseException 
     */
	private static void execute(String[] args) {
		
		//argparser = new ArgumentParser(args);	//args are the arguments for soot, like "-f c --classes testclasses.Simple ..."
    	
		// LOGGER_LEVEL = argparser.getLoggerLevel();
		// String[] sootOptions = argparser.getSootOptions();	// sootOptions is basically the same as args (it misses --classes, for some reason)
		
		LOGGER_LEVEL = Level.ALL;
		String[] sootOptions = Parser2.getSootOptions(args);
		
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
    	
		// TODO: specify the standard library classpath as a command line argument
		Scene.v().setSootClassPath(Scene.v().getSootClassPath()
				+ ":.:"
				+ new File(javaHome, "lib/jce.jar").toString()
			    + ":"
				+ new File(javaHome, "lib/rt.jar").toString());
		Scene.v().addBasicClass("analyzer.level2.HandleStmt");
		Scene.v().addBasicClass("analyzer.level2.SecurityLevel");

		
		BodyAnalyzer banalyzer = new BodyAnalyzer();
        

		PackManager.v()
        	.getPack("jtp").add(new Transform("jtp.analyzer", banalyzer)); 

        	   
		soot.Main.main(sootOptions);
		
		//compile to JAR. Currently, sootOptions[3] is the mainClass (like testclasses.test1).
		// it gets compiled to sootOutput/junit/main/testclasses/test1.class
		// we want to output it to ant/main/testclasses/test1.jar
		util.ant.AntRunner.run(sootOptions[3], "sootOutput", "antOutput");
		
		// for multiple runs, soot needs to be reset, which is done in the following line
		G.reset();

	}
}
