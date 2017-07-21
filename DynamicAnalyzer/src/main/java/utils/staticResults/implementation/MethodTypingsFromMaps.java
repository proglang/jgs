package utils.staticResults.implementation;

import de.unifreiburg.cs.proglang.jgs.instrumentation.*;
import soot.SootMethod;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utils.staticResults.MIMap;
import utils.staticResults.MSLMap;
import utils.staticResults.MSMap;

/**
 * Fake type analysis results that take their information
 */
public class MethodTypingsFromMaps implements MethodTypings {
    MSLMap map1;
    MSMap map2;
    MIMap map3;
    public MethodTypingsFromMaps(MSLMap m1, MSMap m2, MIMap m3){
        map1 = m1;
        map2 = m2;
        map3 = m3;
    }

    @Override
    public VarTyping getVarTyping(SootMethod m) {
        return map1.getVar(m);
    }

    @Override
    public CxTyping getCxTyping(SootMethod m) {
        return map2.getCx(m);
    }

    @Override
    public Instantiation getMonomorphicInstantiation(SootMethod m) {
        return map3.getInstantiation(m);
    }

    @Override
    public Effect getEffectType(SootMethod m) {
        throw new NotImplementedException();
    }
}
