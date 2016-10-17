package main;

import analyzer.level1.BodyAnalyzer;
import soot.PackManager;
import soot.Scene;
import soot.Transform;
import utils.logging.L1Logger;
import utils.parser.ArgumentParser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author Regina Koenig
 * @version 0.6
 */
public class Main {

	private static Level LOGGER_LEVEL;

	private static ArgumentParser argparser;


	public static void main(String[] args) {
		execute(args);
	}

	/**
     * Method which configures and executes soot.Main.
     * @param args This arguments are delivered by Main.main.
     */
	private static void execute(String[] args) {
    	
		argparser = new ArgumentParser(args);
    	
		LOGGER_LEVEL = argparser.getLoggerLevel();
		String[] sootOptions = argparser.getSootOptions();
    	
		try {
			System.out.println("Logger Init1");
			L1Logger.setup(LOGGER_LEVEL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String javaHome = System.getProperty("java.home");
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

	}
}
