package de.unifreiburg.cs.proglang.jgs.typing;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.variable;

import java.util.*;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraint;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraints;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;

/**
 * Environments: immutable maps from Locals to type variables.
 * 
 * @author fennell
 *
 */
public class Environment {

    public static class JoinResult<Level> {
        public final Stream<Constraint<Level>> constraints;
        public final Environment env;

        public JoinResult(Stream<Constraint<Level>> constraints, Environment env) {
            this.constraints = constraints;
            this.env = env;
        }
    }

    /**
     * The domain of environments "Var"s which are soot.Local and soot.jimple.ThisRef
     * (and perhaps soot.jimple.ParameterRef). (Hopefully) Their only purpose is to
     * have an identity
     */
    private final Map<Var<?>, TypeVar> env;


    /**
     * Joint two environments. Returns a <code>Result</code> that contains the joined environment and constraints that force unification of local variables. The effects of the result are not needed (left empty) which is a bit of a hack...
     */
    public static <Level> JoinResult<Level> join(Environment r1, Environment r2, TypeVars tvars) {
        Map<Var<?>, TypeVar> joinedEnv = new HashMap<>();
        Stream.Builder cs = Stream.builder();
        r1.env.entrySet().stream().forEach( e -> {
            Var<?> k = e.getKey();
            TypeVar v1 = e.getValue();
            if (r2.env.containsKey(k)) {
                TypeVar v2 = r2.get(k);
                if (!v1.equals(v2)) {
                    TypeVar vNew = tvars.fresh(k.toString());
                    Stream.of(Constraints.le(variable(v1), variable(vNew)), Constraints.le(variable(v2), variable(vNew))).forEach(cs);
                    joinedEnv.put(k, vNew);
                } else {
                    joinedEnv.put(k, v1);
                }
            } else {
                joinedEnv.put(k, v1);
            }
        });
        r2.env.entrySet().stream().forEach(e -> {
            Var<?> k = e.getKey();
            if (r1.env.containsKey(k)) {
                joinedEnv.put(k, e.getValue());
            }
        });
        return new JoinResult<>(cs.build(), Environments.fromMap(joinedEnv));
    }

    public Environment(Map<Var<?>, TypeVar> env) {
        this.env = new HashMap<>(env);
    }

    private Environment(Map<Var<?>, TypeVar> env, Var<?> k, TypeVar v) {
        this(env);
        this.env.put(k, v);
    }

    public Environment add(Var<?> k, TypeVar v) {
        return new Environment(this.env, k, v);
    }

    public TypeVar get(Var<?> local) {
        return this.tryGet(local)
                   .orElseThrow(() -> new NoSuchElementException(local.toString()));
    }

    public Optional<TypeVar> tryGet(Var<?> local) {
        return Optional.ofNullable(this.env.get(local));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((env == null) ? 0 : env.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Environment other = (Environment) obj;
        if (env == null) {
            if (other.env != null)
                return false;
        } else if (!env.equals(other.env))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Environment [env=" + env + "]";
    }

}
