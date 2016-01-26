package main;

import analyzer.level1.BodyAnalyzer;
import soot.PackManager;
import soot.Scene;
import soot.Transform;
import utils.logging.L1Logger;
import utils.parser.ArgumentParser;

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
    	
    Scene.v().setSootClassPath(Scene.v().getSootClassPath() 
    	   + ":.:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/"
    	   + "java-7-openjdk-amd64/jre/lib/rt.jar");
    Scene.v().addBasicClass("analyzer.level2.HandleStmt");
    Scene.v().addBasicClass("analyzer.level2.SecurityLevel");

        
    BodyAnalyzer banalyzer = new BodyAnalyzer();
        
       
    PackManager.v()
        		   .getPack("jtp")
        		   .add(new Transform("jtp.analyzer", banalyzer)); 
        	   

    soot.Main.main(sootOptions);

  }
}
