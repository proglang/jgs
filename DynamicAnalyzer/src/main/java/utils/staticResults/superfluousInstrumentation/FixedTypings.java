package utils.staticResults.superfluousInstrumentation;

import analyzer.level2.storage.LowMediumHigh;
import de.unifreiburg.cs.proglang.jgs.instrumentation.*;
import soot.Local;
import soot.SootMethod;
import soot.jimple.Stmt;
import utils.staticResults.implementation.Dynamic;
import utils.staticResults.implementation.Public;

/**
 * Fixed method typings, like "ALL_DYNAMIC" that work without type analysis.
 */
public class FixedTypings {
    // Var, Cx & Instantiation all return Dynamic on any requests
    public static final <L> MethodTypings<L> allDynamic() {
        return new All<>(new Dynamic<L>());
    }

    // Var, Cx & Instantiation all return Public on any requests
    public static final <L> MethodTypings<L> allPublic() {
        return new All<>(new Public<L>());
    }

    private static class All<T> implements MethodTypings<T> {

        private final Type<T> constantType;

        private All(Type<T> constantType) {
            this.constantType = constantType;
        }

        @Override
        public Instantiation<T> getMonomorphicInstantiation(SootMethod m) {
            return new Instantiation<T>() {
                @Override
                public Type<T> get(int param) {
                    return constantType;
                }

                @Override
                public Type<T> getReturn() {
                    return constantType;
                }
            };
        }

        @Override
        public VarTyping<T> getVarTyping(SootMethod m) {
            return new VarTyping<T>() {
                @Override
                public Type<T> getBefore(Instantiation<T> instantiation, Stmt
                        s, Local l) {
                    return constantType;
                }

                @Override
                public Type<T> getAfter(Instantiation<T> instantiation, Stmt s, Local l) {
                    return constantType;
                }
            };
        }

        @Override
        public CxTyping<T> getCxTyping(SootMethod m) {
            return new CxTyping<T>() {
                @Override
                public Type<T> get(Instantiation<T> instantiation, Stmt s) {
                    return constantType;
                }
            };
        }

        @Override
        public Effect<T> getEffectType(SootMethod m) {
            return new Effect<T>() {
                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public Type<T> getType() {
                    return constantType;
                }
            };
        }
    }
}
