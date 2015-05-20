package main;

import java.io.IOException;

import logging.L1Logger;
import analyzer.level1.BodyAnalyzer;
import soot.*;

/**
 * @author Regina Koenig
 * @version 0.6
 */
public class Main {


    public static void main(String[] args) {
        execute(args);
    }

    /**
     * DOC
     * 
     * @param args
     */
    private static void execute(String[] args) {
    	
    	try {
			L1Logger.setup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        Scene.v().setSootClassPath(Scene.v().getSootClassPath() + ":.:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar");
        // Scene.v().setSootClassPath(Scene.v().getSootClassPath() + ":.:/home/koenigr/Eclipse/eclipse/jre/lib/jce.jar:/home/koenigr/Eclipse/eclipse/jre/lib/rt.jar");
        Scene.v().addBasicClass("analyzer.level2.HandleStmt");
        Scene.v().addBasicClass("analyzer.level2.SecurityLevel");

        
        BodyAnalyzer banalyzer = new BodyAnalyzer();
        
        
        PackManager.v()
        		   .getPack("jtp")
        		   .add(new Transform("jtp.analyzer", banalyzer));  
        		   

        // TODO ich kann den Classpath auch als Argument übergeben
       soot.Main.main(new String[]{"-f","J", "-main-class", "main.Test", "main.Test"});

    }

}
