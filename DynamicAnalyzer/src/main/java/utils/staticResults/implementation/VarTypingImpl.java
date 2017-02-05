package utils.staticResults.implementation;

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
public class VarTypingImpl<Level> implements VarTyping<Level> {

    Map<Stmt, Map<Local, BeforeAfterContainer>> map;

    public VarTypingImpl(Map<Stmt, Map<Local, BeforeAfterContainer>> map) {
        this.map = map;
    }

    @Override
    public Type<Level> getBefore(Instantiation<Level> instantiation, Stmt s, Local l) {
        return map.get(s).get(l).getBefore();
    }

    @Override
    public Type<Level> getAfter(Instantiation<Level> instantiation, Stmt s, Local l) {
        return map.get(s).get(l).getAfter();
    }
}
