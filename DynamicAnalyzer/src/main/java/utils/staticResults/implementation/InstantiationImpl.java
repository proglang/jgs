package utils.staticResults.implementation;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;
import soot.jimple.Stmt;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

/**
 * Created by Nicolas Müller on 05.02.17.
 */
public class InstantiationImpl<Level> implements Instantiation<Level> {

    Map<Integer, Type<Level>> map;

    public InstantiationImpl(Map<Integer, Type<Level>> map) {
        this.map = map;
    }

    @Override
    public Type<Level> get(int param) {
        if (param == map.size() - 1) {
            throw new IllegalArgumentException("Required to get type of argument " + param + "," +
                    "but this argument does not exist in this instantiation! At position " + param +
                    ", the return value is stored!");
        }
        return map.get(param);
    }

    /**
     * Return the sec-value the method returns. Internally, this value
     * is stored at the very last position of a map containing the sec-values of
     * the arguments of the method.
     * @return      sec-value of method return
     */
    @Override
    public Type<Level> getReturn() {
        return map.get(map.size() - 1);
    }
}
