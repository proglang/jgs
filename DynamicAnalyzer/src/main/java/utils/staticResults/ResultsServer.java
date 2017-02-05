package utils.staticResults;

import soot.*;
import soot.jimple.Stmt;
import utils.staticResults.implementation.Dynamic;
import utils.staticResults.implementation.Public;
import utils.staticResults.implementation.Types;

import java.util.Collection;

/**
 * A Helper that fills the var, cx and instantiation maps with the approprioate typings
 */
public class ResultsServer {


    // ======================= SET VAR TYPING ==========================
    public static void setDynamic(MSLMap<BeforeAfterContainer> mslMap, Collection<String> allClasses) {
        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();
                for (Unit u: b.getUnits()) {
                    Stmt stmt = (Stmt) u;
                    for (Local l: b.getLocals()) {
                        BeforeAfterContainer tmp = new BeforeAfterContainer<>(new Dynamic<>(), new Dynamic<>());
                        mslMap.put(sm, stmt, l, tmp);
                    }
                }
            }
        }
    }

    public static void setPublic(MSLMap<BeforeAfterContainer> mslMap, Collection<String> allClasses) {
        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();
                for (Unit u: b.getUnits()) {
                    Stmt stmt = (Stmt) u;
                    for (Local l: b.getLocals()) {
                        mslMap.put(sm, stmt, l, new BeforeAfterContainer<>(new Public<>(), new Public<>()));
                    }
                }
            }
        }
    }


    // ========================= SET CX / Instantiation ===========================
    public static void setDynamic(MSMap<Types> msMap, Collection<String> allClasses) {
        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();
                for (Unit u: b.getUnits()) {
                    Stmt stmt = (Stmt) u;
                    msMap.put(sm, stmt, new Dynamic<>());
                }
            }
        }
    }

    public static void setPublic(MSMap<Types> msMap, Collection<String> allClasses) {
        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();
                for (Unit u: b.getUnits()) {
                    Stmt stmt = (Stmt) u;
                    msMap.put(sm, stmt, new Public<>());
                }
            }
        }
    }
}
