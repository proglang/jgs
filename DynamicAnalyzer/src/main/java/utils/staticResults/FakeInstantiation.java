package utils.staticResults;

import analyzer.level1.storage.Dynamic;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Instantiation;
import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;
import soot.Body;

/**
 * Created by Nicolas Müller on 24.01.17.
 */
public class FakeInstantiation<Level> implements Instantiation<Level> {
    Body body;
    public FakeInstantiation(Body b) {
        body = b;
    }

    @Override
    public Type<Level> get(int param) {
        return new Dynamic<Level>();
    }

    @Override
    public Type<Level> getReturn() {
        return new Dynamic<Level>();
    }
}
