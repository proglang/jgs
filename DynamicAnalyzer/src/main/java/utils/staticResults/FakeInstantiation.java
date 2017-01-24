package utils.staticResults;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;

/**
 * Created by Nicolas Müller on 24.01.17.
 */
public class FakeInstantiation<Level> implements Instantiation<Level> {
    @Override
    public Type<Level> get(int param) {
        return null;
    }

    @Override
    public Type<Level> getReturn() {
        return null;
    }
}
