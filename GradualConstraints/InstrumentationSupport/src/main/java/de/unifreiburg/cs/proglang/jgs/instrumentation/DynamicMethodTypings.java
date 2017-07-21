package de.unifreiburg.cs.proglang.jgs.instrumentation;

import soot.Local;
import soot.SootMethod;
import soot.jimple.Stmt;

public class DynamicMethodTypings<Level> implements MethodTypings<Level> {
    private final Type<Level> dyn = new Type<Level>() {
        @Override
        public boolean isStatic() {
            return false;
        }

        @Override
        public boolean isDynamic() {
            return true;
        }

        @Override
        public boolean isPublic() {
            return false;
        }

        @Override
        public Level getLevel() {
            throw new RuntimeException("Not Implemented!");
        }
    };

    @Override
    public Instantiation<Level> getMonomorphicInstantiation(SootMethod m) {
        return new Instantiation<Level>() {
            @Override
            public Type<Level> get(int param) {
                return dyn;
            }

            @Override
            public Type<Level> getReturn() {
                return dyn;
            }
        };
    }

    @Override
    public VarTyping<Level> getVarTyping(SootMethod m) {
        return new VarTyping<Level>() {
            @Override
            public Type<Level> getBefore(Instantiation<Level> instantiation, Stmt s, Local l) {
                return dyn;
            }

            @Override
            public Type<Level> getAfter(Instantiation<Level> instantiation,
                                        Stmt s, Local l) {
                return dyn;
            }
        };
    }

    @Override
    public CxTyping<Level> getCxTyping(SootMethod m) {
        return new CxTyping<Level>() {
            @Override
            public Type<Level> get(Instantiation<Level> instantiation, Stmt s) {
                return dyn;
            }
        };
    }

    @Override
    public Effect<Level> getEffectType(SootMethod m) {
        return new Effect<Level>() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Type<Level> getType() {
                return dyn;
            }
        };
    }
}
