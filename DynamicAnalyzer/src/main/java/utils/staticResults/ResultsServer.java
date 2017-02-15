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

    /**
     * Creates a mapping (methods, statements and variables) => (Dynamic, Dynamic)
     * @param mslMap            Map to fill
     * @param allClasses        Collection of Strings specifying the soot classes that contain the keys
     */
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

    /**
     * Creates a mapping (methods, statements and variables) => (Public, Public)
     * @param mslMap            Map to fill
     * @param allClasses        Collection of Strings specifying the soot classes that contain the keys
     */
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


    // ========================= SET CX ===========================
    /**
     * Creates a mapping (methods, statements) => Dynamic
     * @param msMap            Map to fill
     * @param allClasses        Collection of Strings specifying the soot classes that contain the keys
     */
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
    /**
     * Creates a mapping (methods, statements) => Public
     * @param msMap            Map to fill
     * @param allClasses        Collection of Strings specifying the soot classes that contain the keys
     */
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


    // =============== SET INSTANTIATION ==================
    public static void setDynamic(MIMap<Types> miMap, Collection<String> allClasses) {
        for (String s: allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm: sootClass.getMethods()) {
                // add return type after last parameter!
                for (int parameter = 0; parameter <= sm.getParameterCount(); parameter++) {
                    miMap.put(sm, parameter, new Dynamic());
                }
            }
        }
    }

    public static void setPublic(MIMap<Types> miMap, Collection<String> allClasses) {
        for (String s: allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm: sootClass.getMethods()) {
                // add return type after last parameter!
                for (int parameter = 0; parameter <= sm.getParameterCount(); parameter++) {
                    miMap.put(sm, parameter, new Public());
                }
            }
        }
    }
}
