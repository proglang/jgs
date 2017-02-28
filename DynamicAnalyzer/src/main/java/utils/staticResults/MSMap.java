package utils.staticResults;

import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import soot.Local;
import soot.SootMethod;
import soot.jimple.Stmt;
import utils.exceptions.InternalAnalyzerException;
import utils.staticResults.implementation.CxTypingImpl;
import utils.staticResults.implementation.InstantiationImpl;
import utils.staticResults.implementation.VarTypingImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * A _M_ethod, _S_tatement map
 */
public class MSMap<T> {
    Map<SootMethod, Map<Stmt, T>> map = new HashMap<>();

    public void put(SootMethod sm, Stmt stmt, T payload) {
        if (!map.containsKey(sm)) {
            map.put(sm, new HashMap<Stmt, T>());
        }
        if (map.get(sm).containsKey(stmt)) {
            throw new InternalAnalyzerException(String.format("Tried to add key( %s, %s ), which is already present in static analysis result", sm, stmt));
        } else {
            map.get(sm).put(stmt, payload);
        }
    }

    public CxTyping getCx(SootMethod sm) {
        return new CxTypingImpl(map.get(sm));
    }

    public T get(SootMethod sm, Stmt stmt, Local l ) {
        boolean valuePresent = false;
        if (map.containsKey(sm)) {
            if ( map.get(sm).containsKey(stmt)) {
                valuePresent = true;
            }
        }
        if (valuePresent) {
            return map.get(sm).get(stmt);
        } else {
            throw new InternalAnalyzerException(String.format("Tried to retrieve key( %s, ), which is not present in static analysis result", sm, stmt));
        }
    }
}
