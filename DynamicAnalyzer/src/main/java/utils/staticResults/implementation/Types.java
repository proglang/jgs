package utils.staticResults.implementation;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Type;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Nicolas Müller on 23.01.17.
 */
public class Types<Level> implements Type<Level> {
    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Override
    public Level getLevel() {
        throw new NotImplementedException();
    }
}
