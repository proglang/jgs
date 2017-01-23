package analyzer.level1.storage;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;
import de.unifreiburg.cs.proglang.jgs.instrumentation.VarTyping;
import soot.Local;
import soot.jimple.Stmt;

/**
 * Created by Nicolas Müller on 23.01.17.
 */
public class SecValueTuple<Level> implements VarTyping<Level> {
    Type<Level> before;
    Type<Level> after;

    public SecValueTuple(Type<Level> before, Type<Level> after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public Type<Level> getBefore(Instantiation<Level> instantiation, Stmt s, Local l) {
        return before;
    }

    @Override
    public Type<Level> getAfter(Instantiation<Level> instantiation, Stmt s, Local l) {
        return after;
    }
}
