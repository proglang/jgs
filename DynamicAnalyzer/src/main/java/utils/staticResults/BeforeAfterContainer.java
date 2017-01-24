package utils.staticResults;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;

/**
 * Created by Nicolas Müller on 23.01.17.
 */
public class BeforeAfterContainer<Level>  {
    Type<Level> before;
    Type<Level> after;

    public BeforeAfterContainer(Type<Level> before, Type<Level> after) {
        this.before = before;
        this.after = after;
    }

}
