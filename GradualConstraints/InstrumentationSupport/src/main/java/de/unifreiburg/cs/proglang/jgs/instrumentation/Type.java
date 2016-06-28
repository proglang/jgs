package de.unifreiburg.cs.proglang.jgs.instrumentation;

/**
 * Types, as seen by the code generating the dynamic instrumentation
 */
public interface Type<Level> {

    boolean isStatic();
    boolean isDynamic();
    boolean isPublic();

    /**
     * @return The security level of a static type. Throws {@code
     * IllegalArgumentException} if {@code isStatic()} yields false.
     */
    Level getLevel();

}
