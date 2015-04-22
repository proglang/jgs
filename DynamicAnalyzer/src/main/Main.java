package main;

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
    	
        Scene.v().setSootClassPath(Scene.v().getSootClassPath() + ":.:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar");
        // Scene.v().setSootClassPath(Scene.v().getSootClassPath() + ":.:/home/koenigr/Eclipse/eclipse/jre/lib/jce.jar:/home/koenigr/Eclipse/eclipse/jre/lib/rt.jar");
        Scene.v().addBasicClass("analyzer.level2.HandleStmt");

        
        BodyAnalyzer banalyzer = new BodyAnalyzer();
        
        
        PackManager.v()
        		   .getPack("jtp")
        		   .add(new Transform("jtp.analyzer", banalyzer));  

        // TODO ich kann den Classpath auch als Argument übergeben
       soot.Main.main(new String[]{"-f","J", "-main-class", "main.Test", "main.Test"});

    }

}
