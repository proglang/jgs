package de.unifreiburg.cs.proglang.jgs.examples;

import de.unifreiburg.cs.proglang.jgs.examples.ExampleTypes.Public;
import de.unifreiburg.cs.proglang.jgs.instrumentation.*;
import soot.Local;
import soot.SootMethod;
import soot.jimple.Stmt;

/**
 * Some analysis results for the methods of {@code exClass} (see examples
 * .Code.java)
 */
public class AnalysisResults<Level> {


    /****************************
     * max
     ****************************/
    public VarTyping<Level> max_varTyping = new VarTyping<Level>() {
        @Override
        public Type<Level> getBefore(Instantiation<Level> instantiation, Stmt s, Local l) {
            if (!l.equals(Code.localZ)) {
                throw new RuntimeException("Local variable " + l + " not known for method max");
            }
            if (s.equals(Code.max_1_assign_Z_p0)) {
                return new Public<>();

            } else if (s.equals(Code.max_2_if_Z_lt_p1)) {
                return instantiation.get(0);

            } else if (s.equals(Code.max_3_assign_Z_p1)) {
                return instantiation.get(0);

            } else if (s.equals(Code.max_4_return_Z)) {
                if (instantiation.get(0).equals(instantiation.get(1)))
                return ExampleTypes.lub(instantiation.get(0), instantiation.get(1));

            } else {
                throw new RuntimeException("Not aware of statement " + s + " in method max");
            }
        }

        @Override
        public Type<Level> getAfter(Instantiation<Level> instantiation,
                                    Stmt s,
                                    Local l) {
            throw new RuntimeException("Not Implemented!");
        }
    };

    CxTyping<Level> max_cxTyping = new CxTyping<Level>() {
        @Override
        public Type<Level> get(Instantiation<Level> instantiation, Stmt s) {
            throw new RuntimeException("Not Implemented!");
        }
    };
    // Instantiate "max" with param0:STATIC and param1:STATIC
    Methods<Level> max_methods_STATIC_STATIC__STATIC =
            new Methods<Level>() {
                @Override
                public Instantiation<Level> getMonomorphicInstantiation
                        (SootMethod m) {
                    return new Instantiation<Level>() {
                        @Override
                        public Type<Level> get(int param) {
                            throw new RuntimeException("Not Implemented!");
                        }

                        @Override
                        public Type<Level> getReturn() {
                            throw new RuntimeException("Not Implemented!");
                        }
                    };
                }

                @Override
                public Type<Level> getEffectType(SootMethod m) {
                    throw new RuntimeException("Not Implemented!");
                }
            };
}
