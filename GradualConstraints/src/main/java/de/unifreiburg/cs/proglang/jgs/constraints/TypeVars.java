package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.instrumentation.Var;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;

import java.util.HashMap;
import java.util.Map;

/**
 * A context for (anonymous) type variables. Allows to create fresh variables
 * and to lookup existing ones.
 *
 * @author Luminous Fennell <fennell@informatik.uni-freiburg.de>
 */
// TODO: change the toString() method of type variables to be more generic.. they should not be read by users anyway. That's what "inspect" is for.
public class TypeVars {

    private int nextId = 0;

    protected int freshId() {
        return nextId++;
    }

    public TypeVars() {
    }


    public MethodTypeVars forMethod(DirectedGraph<Unit> g) {
        return new MethodTypeVars(g);
    }

    public MethodTypeVars forTestingSingleStatements() {
        return new MethodTypeVars();
    }

    public static class MethodTypeVars extends TypeVars {

        private Map<Stmt, Integer> stmtNumbers;

        private String stmtToString(Stmt s) {
            if (stmtNumbers == null) {
                return s.toString();
            } else {
                return String.format("%d:%s", stmtNumbers.get(s), s);
            }
        }

        private MethodTypeVars() {
        }

        private MethodTypeVars(DirectedGraph<Unit> g) {
            stmtNumbers = new HashMap<>();
            int count = 0;
            for (Unit u : g) {
                stmtNumbers.put((Stmt) u, count);
                count++;
            }
        }

        /**
         * Create a fresh type variable.
         *
         * @return A type variable which is unique for this context.
         */
        public TypeVar forLocal(Var<?> local, Stmt s) {
            return new ForLocal(local, s);
        }

        public TypeVar forContext(Stmt s) {
            return new ForContext(s);
        }

        public TypeVar forInternalUse(String description) {
            String id = "INTERNAL#" + freshId() + "[" + description + "]";
            return new TypeVar() {
                @Override
                public TypeVarViews.TypeVarView inspect() {
                    return new TypeVarViews.Internal(id);
                }

                @Override
                public String toString() {
                    return id;
                }
            };
        }

        private class ForLocal implements TypeVar {
            private final Var<?> local;
            private final Stmt stmt;

            private ForLocal(Var<?> local, Stmt stmt) {
                this.local = local;
                this.stmt = stmt;
            }

            @Override
            public String toString() {
                return String.format("%s[%s]", local, stmtToString(stmt));
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                ForLocal forLocal = (ForLocal) o;

                if (local != null ? !local.equals(forLocal.local) :
                    forLocal.local
                    != null)
                    return false;
                return !(stmt != null ? !stmt.equals(forLocal.stmt) :
                         forLocal.stmt
                         != null);

            }

            @Override
            public int hashCode() {
                int result = local != null ? local.hashCode() : 0;
                result = 31 * result + (stmt != null ? stmt.hashCode() : 0);
                return result;
            }

            @Override
            public TypeVarViews.TypeVarView inspect() {
                return new TypeVarViews.Internal(this.toString());
            }
        }

        private class ForContext implements TypeVar {
            private final Stmt s;

            private ForContext(Stmt s) {
                this.s = s;
            }

            @Override
            public String toString() {
                return "cx[" + stmtToString(s) + "]";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                ForContext that = (ForContext) o;

                return !(s != null ? !s.equals(that.s) : that.s != null);

            }

            @Override
            public int hashCode() {
                return s != null ? s.hashCode() : 0;
            }

            @Override
            public TypeVarViews.TypeVarView inspect() {
                return new TypeVarViews.Internal(this.toString());
            }
        }

    }

    public TypeVar join(TypeVar v1, TypeVar v2) {
        return new Join(v1, v2);
    }

    public TypeVar testParam(Var<?> param, String id) {
        return new TestParam(param, id);
    }

    public TypeVar param(Var<Integer> param) {
        return new Param(param);
    }

    private static TypeVar topLevelContext = new TypeVar() {

        @Override
        public TypeVarViews.TypeVarView inspect() {
            return new TypeVarViews.Cx();
        }
    };

    public TypeVar topLevelContext() {
        return topLevelContext;
    }

    private static TypeVar ret = new TypeVar() {
        @Override
        public TypeVarViews.TypeVarView inspect() {
            return new TypeVarViews.Ret();
        }
    };

    public TypeVar ret() {
        return ret;
    }

    public interface TypeVar {
        TypeVarViews.TypeVarView inspect();
    }

    private class Param implements TypeVar {
        private final Var<Integer> param;

        private Param(Var<Integer> param) {
            this.param = param;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Param param1 = (Param) o;

            return !(param != null ? !param.equals(param1.param) : param1.param
                                                                   != null);

        }

        @Override
        public int hashCode() {
            return param != null ? param.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "$" + this.param.toString();
        }

        @Override
        public TypeVarViews.TypeVarView inspect() {
            return new TypeVarViews.Param(param.repr);
        }
    }

    private class InternalConstant implements TypeVar {
        private final String name;

        private InternalConstant(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InternalConstant that = (InternalConstant) o;

            return !(
                    name != null ? !name.equals(that.name) : that.name != null);

        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public TypeVarViews.TypeVarView inspect() {
            return new TypeVarViews.Internal(this.name);
        }
    }


    private class TestParam implements TypeVar {
        private final Var<?> param;
        private final String id;

        private TestParam(Var<?> param, String id) {
            this.param = param;
            this.id = id;
        }

        @Override
        public String toString() {
            return String.format("%s%s", param, id);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestParam testParam = (TestParam) o;

            if (param != null ? !param.equals(testParam.param) : testParam.param
                                                                 != null)
                return false;
            return !(id != null ? !id.equals(testParam.id) : testParam.id
                                                             != null);

        }

        @Override
        public int hashCode() {
            int result = param != null ? param.hashCode() : 0;
            result = 31 * result + (id != null ? id.hashCode() : 0);
            return result;
        }

        @Override
        public TypeVarViews.TypeVarView inspect() {
            return new TypeVarViews.Internal(this.id);
        }
    }

    private class Join implements TypeVar {
        private final TypeVar v1;
        private final TypeVar v2;

        private Join(TypeVar v1, TypeVar v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        @Override
        public String toString() {
            return String.format("join(%s,%s)", v1, v2);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Join join = (Join) o;

            if (v1 != null ? !v1.equals(join.v1) : join.v1 != null)
                return false;
            return !(v2 != null ? !v2.equals(join.v2) : join.v2 != null);

        }

        @Override
        public int hashCode() {
            int result = v1 != null ? v1.hashCode() : 0;
            result = 31 * result + (v2 != null ? v2.hashCode() : 0);
            return result;
        }

        @Override
        public TypeVarViews.TypeVarView inspect() {
            return new TypeVarViews.Internal(this.toString());
        }
    }

}
