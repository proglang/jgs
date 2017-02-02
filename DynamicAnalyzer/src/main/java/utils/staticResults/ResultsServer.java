package utils.staticResults;

import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import utils.staticResults.storage.CxTypingEverythingDynamic;
import utils.staticResults.storage.CxTypingEverythingPublic;
import utils.staticResults.storage.InstantiationEverythingDynamic;
import utils.staticResults.storage.VarTypingEverythingDynamic;

import java.util.Collection;
import java.util.Map;

/**
 * A Helper that fills the var, cx and instantiation maps with the approprioate typings
 */
public class ResultsServer {


    /**
     * Fill the varTyping map with all dynamic entries
     * @param varTyping
     * @param allClasses
     */
    public static void setDynamicVar(Map<SootMethod, VarTyping> varTyping, Collection<String> allClasses) {


        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();

                varTyping.put(sm, new VarTypingEverythingDynamic(b));
            }
        }
    }


    public static void setDynamicCx(Map<SootMethod, CxTyping> cxTyping, Collection<String> allClasses) {
        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();

                cxTyping.put(sm, new CxTypingEverythingDynamic(b));
            }
        }
    }

    public static void setPublicCx(Map<SootMethod, CxTyping> cxTyping, Collection<String> allClasses) {
        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();

                cxTyping.put(sm, new CxTypingEverythingPublic(b));
            }
        }
    }

    public static void setDynamicInst(Map<SootMethod, Instantiation> instantiation, Collection<String> allClasses) {
        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();

                instantiation.put(sm, new InstantiationEverythingDynamic(b));
            }
        }
    }
}
