package de.unifreiburg.cs.proglang.jgs.instrumentation;

/**
 * A class representing global effects.
 */
public interface Effect<Level> {

    /**
     * @return true, when it is the empty effect
     */
    boolean isEmpty();

    /**
     * @return The {@code instrumentation.Type} of the effect. Raises a
     * {@code IllegalArgumentException} when it is the empty effect (cf.
     * {@code isEmpty})
     */
    Type<Level> getType();

}
