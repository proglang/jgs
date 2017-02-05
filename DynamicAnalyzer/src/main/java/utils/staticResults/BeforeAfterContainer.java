package utils.staticResults;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;

/**
 * A simple tuple container. Intended to store Security Types of a local before and after a given statement.
 */
public class BeforeAfterContainer<Level>  {
    private Type<Level> before;
    private Type<Level> after;

    BeforeAfterContainer(Type<Level> before, Type<Level> after) {
        this.before = before;
        this.after = after;
    }

    public Type<Level> getBefore() {
        return before;
    }

    public Type<Level> getAfter() {
        return after;
    }

}
