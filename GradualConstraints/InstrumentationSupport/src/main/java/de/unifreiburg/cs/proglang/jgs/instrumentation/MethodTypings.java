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
     * Return a single instantiation for a method. In case a parameter is
     * polymorphic, the instantiation will return "defaultType".
     *
     * @param defaultType The type to return by the instantiation in case a
     *                    parameter is polymorphic (i.e. can be either
     *                    dynamic or static).
     */
    Instantiation<Level> getSingleInstantiation(SootMethod m, Type<Level> defaultType);

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
