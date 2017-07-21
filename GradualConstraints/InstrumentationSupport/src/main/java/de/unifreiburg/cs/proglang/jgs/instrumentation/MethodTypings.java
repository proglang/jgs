package de.unifreiburg.cs.proglang.jgs.instrumentation;

import soot.SootMethod;

/**
 * A mapping from methods to their typing information and instantiations.
 *
 * The type checker generates this information. It is consumed the
 * instrumentation (DynamicAnalyzer) to identify dynamic code to instrument.
 */
public interface MethodTypings<Level> {

    /**
     * Return the single instantiation for a monomorphic method. Throws an {@code IllegalArgumentException} if the method is not monomorphic.
     */
    Instantiation<Level> getMonomorphicInstantiation(SootMethod m);

    /**
     * @return The variable typing for method m.
     * @throws java.util.NoSuchElementException if there is no vartyping for m.
     */
    VarTyping<Level> getVarTyping(SootMethod m);

    /**
     * @return The cxtyping for method m.
     * @throws java.util.NoSuchElementException if there is no cxtyping for m.
     */
    CxTyping<Level> getCxTyping(SootMethod m);

    /**
     * Return the effect type (i.e. global context) of method {@code m}.
     */
    Effect<Level> getEffectType(SootMethod m);

}
