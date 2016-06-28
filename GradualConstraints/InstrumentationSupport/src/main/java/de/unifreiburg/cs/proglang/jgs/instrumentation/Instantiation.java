package de.unifreiburg.cs.proglang.jgs.instrumentation;

/**
 * Represents the instantiation of the type variables of a method's signature.
 */
public interface Instantiation<Level> {

    /**
     * Return the type for parameter number @{code param}.
     */
    Type<Level> get(int param);

    /**
     * Return the type for the return value.
     */
    Type<Level> getReturn();
}
