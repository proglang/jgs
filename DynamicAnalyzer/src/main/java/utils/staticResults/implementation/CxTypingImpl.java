package utils.staticResults.implementation;

import de.unifreiburg.cs.proglang.jgs.instrumentation.CxTyping;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import soot.Local;
import soot.jimple.Stmt;
import utils.staticResults.BeforeAfterContainer;

import java.util.Map;

/**
 * Created by Nicolas Müller on 05.02.17.
 */
public class CxTypingImpl <Level> implements CxTyping<Level> {

    Map<Stmt, Type<Level>> map;

    public CxTypingImpl(Map<Stmt, Type<Level>> map) {
        this.map = map;
    }

    @Override
    public Type<Level> get(Instantiation<Level> instantiation, Stmt s) {
        return map.get(s);
    }
}
