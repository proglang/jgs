package utils.staticResults;

import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

import java.util.Collection;
import java.util.Map;

/**
 * A Helper that fills the var, cx and instantiation maps with the approprioate typings
 */
public class ResultsServer {
    /**
     * Fill the maps with all dynamic entries.
     * @param varTyping             var typing map to be filled with dynamic varTypings for all classes and methods in allClasses
     * @param cxTyping              cx typing map to be filled with dynamic varTypings for all classes and methods in allClasses
     * @param instantiation         instantiation typing map to be filled with dynamic varTypings for all classes and methods in allClasses
     * @param allClasses            all classes for whose methods to create typings in the map for
     */
    public static void setAllDynamic(Map<SootMethod, VarTyping> varTyping,
                                     Map<SootMethod, CxTyping> cxTyping,
                                     Map<SootMethod, Instantiation> instantiation,
                                     Collection<String> allClasses) {

        setDynamicVar(varTyping, allClasses);
        setDynamicCx(cxTyping, allClasses);
        setDynamicInst(instantiation, allClasses);
    }

    /**
     * Fill the varTyping map with all dynamic entries
     * @param varTyping
     * @param allClasses
     */
    private static void setDynamicVar(Map<SootMethod, VarTyping> varTyping, Collection<String> allClasses) {


        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();

                varTyping.put(sm, new VarTypingEverythingDynamic(b));
            }
        }
    }


    private static void setDynamicCx(Map<SootMethod, CxTyping> cxTyping, Collection<String> allClasses) {
        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();

                cxTyping.put(sm, new CxTypingEverythingDynamic(b));
            }
        }
    }

    private static void setDynamicInst(Map<SootMethod, Instantiation> instantiation, Collection<String> allClasses) {
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
