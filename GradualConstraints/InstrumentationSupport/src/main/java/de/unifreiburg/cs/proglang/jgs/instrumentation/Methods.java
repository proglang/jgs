package de.unifreiburg.cs.proglang.jgs.instrumentation;

import soot.SootMethod;

/**
 * A mapping from methods to their instantiations.
 */
public interface Methods<Level> {

    /**
     * Return the single instantiation for a monomorphic method. Throws an {@code IllegalArgumentException} if the method is not monomorphic.
     */
    Instantiation<Level> getMonomorphicInstantiation(SootMethod m);

}
