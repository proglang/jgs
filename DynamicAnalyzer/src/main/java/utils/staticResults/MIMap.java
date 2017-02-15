package utils.staticResults;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import soot.SootMethod;
import utils.exceptions.InternalAnalyzerException;
import utils.staticResults.implementation.InstantiationImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nicolas Müller on 15.02.17.
 * A Mapping from _M_ethods, _I_ntegers
 */
public class MIMap<T> {
    Map<SootMethod, Map<Integer, T>> map = new HashMap<>();

    public void put(SootMethod sm, Integer argumentPosition, T payload) {
        if (!map.containsKey(sm)) {
            map.put(sm, new HashMap<Integer, T>());
        }
        if (map.get(sm).containsKey(argumentPosition)) {
            throw new InternalAnalyzerException(String.format("Tried to add key( %s, %s ), " +
                    "which is already present in static analysis result ", sm, argumentPosition));
        } else {
            map.get(sm).put(argumentPosition, payload);
        }
    }

    /**
     * Return instantiation of soot method
     * @param sm    soot method you want an instantiation for
     * @return      returns corresponding instantiation
     */
    public Instantiation getInstantiation(SootMethod sm) {
        return new InstantiationImpl(map.get(sm));
    }
}
