package utils.staticResults.storage;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;

/**
 * Created by Nicolas Müller on 23.01.17.
 */
public class BeforeAfterContainer<Level>  {
    Type<Level> before;

    public Type<Level> getBefore() {
        return before;
    }

    public Type<Level> getAfter() {
        return after;
    }

    Type<Level> after;

    public BeforeAfterContainer(Type<Level> before, Type<Level> after) {
        this.before = before;
        this.after = after;
    }

}
