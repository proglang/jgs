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

    Map<Stmt, Type<Level>> map;

    public InstantiationImpl(Map<Stmt, Type<Level>> map) {
        this.map = map;
    }

    @Override
    public Type<Level> get(int param) {
        throw new NotImplementedException();
    }

    @Override
    public Type<Level> getReturn() {
        throw new NotImplementedException();
    }
}
