package utils.staticResults;

import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import utils.parser.ArgumentContainer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicolas Müller on 31.01.17.
 */
public class FakeTypingStorage {



    public static void createAllDynamicTyping(ArgumentContainer sootOptionsContainer,
                                          Map<SootMethod, VarTyping> fakeVarTypingsMap,
                                          Map<SootMethod, CxTyping> fakeCxTypingsMap,
                                          Map<SootMethod, Instantiation> fakeInstantiationMap) {
        Collection<String> allClasses = sootOptionsContainer.getAdditionalFiles();
        allClasses.add(sootOptionsContainer.getMainclass());

        // add fake typings for methods from main and other classes
        for (String s : allClasses) {
            SootClass sootClass = Scene.v().loadClassAndSupport(s);
            sootClass.setApplicationClass();

            for (SootMethod sm : sootClass.getMethods()) {
                Body b = sootClass.getMethodByName(sm.getName()).retrieveActiveBody();

                fakeVarTypingsMap.put(sm, new VarTypingEverythingDynamic(b));
                fakeCxTypingsMap.put(sm, new CxTypingEverythingDynamic(b));
                fakeInstantiationMap.put(sm, new InstantiationEverythingDynamic(b));
            }
        }
    }
}
