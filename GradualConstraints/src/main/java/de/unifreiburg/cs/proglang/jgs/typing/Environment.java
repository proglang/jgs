package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypes;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraint;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraints;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import scala.Option;

import java.util.*;

/**
 * Environments: immutable maps from Locals to type variables.
 * 
 * @author fennell
 *
 */
public class Environment {

    public static class JoinResult<Level> {
        public final List<Constraint<Level>> constraints;
        public final Environment env;
        private final Map<TypeVar, TypeVar> translations;

        public JoinResult(List<Constraint<Level>> constraints, Environment env, Map<TypeVar, TypeVar> translations) {
            this.constraints = constraints;
            this.env = env;
            this.translations = translations;
        }

        public TypeVar translate(TypeVar v) {
            if (this.translations.containsKey(v)) {
                return this.translations.get(v);
            } else {
                return v;
            }
        }

    }

    /**
     * The domain of environments "Var"s which are soot.Local and soot.jimple.ThisRef
     * (and perhaps soot.jimple.ParameterRef). (Hopefully) Their only purpose is to
     * have an identity
     */
    private final Map<Var<?>, TypeVar> env;

    /**
     * Joint two environments. Returns a <code>JoinResult</code> that
     * contains the joined environment, the constraints that force
     * unification of local variables, and a translation map that records
     * which original variables were replaced by joins.
     */
    public static <Level> JoinResult<Level> join(TypeVars tvars, Environment r1, Environment r2) {
        Map<Var<?>, TypeVar> joinedEnv = new HashMap<>();
        Map<TypeVar, TypeVar> translations = new HashMap<>();
        List<Constraint<Level>> cs = new LinkedList<>();
        for (Map.Entry<Var<?>, TypeVar> e : r1.env.entrySet()) {
            Var<?> k = e.getKey();
            TypeVar v1 = e.getValue();
            if (r2.env.containsKey(k)) {
                TypeVar v2 = r2.get(k);
                if (!v1.equals(v2)) {
                    TypeVar vNew = tvars.join(v1, v2);
                    cs.add(Constraints.<Level>le(CTypes.<Level>variable(v1), CTypes.<Level>variable(vNew)));
                    cs.add(Constraints.<Level>le(CTypes.<Level>variable(v2), CTypes.<Level>variable(vNew)));
                    joinedEnv.put(k, vNew);
                    translations.put(v1, vNew);
                    translations.put(v2, vNew);
                } else {
                    joinedEnv.put(k, v1);
                }
            } else {
                joinedEnv.put(k, v1);
            }
        }
        for (Map.Entry<Var<?>, TypeVar> e : r2.env.entrySet()) {
            Var<?> k = e.getKey();
            if (!r1.env.containsKey(k)) {
                joinedEnv.put(k, e.getValue());
            }
        }
        return new JoinResult<>(cs, Environments.fromMap(joinedEnv), translations);
    }

    public Environment(Map<Var<?>, TypeVar> env) {
        this.env = new HashMap<>(env);
    }

    private Environment(Map<Var<?>, TypeVar> env, Map<Var<?>, TypeVar> newEntries) {
        this(env);
        this.env.putAll(newEntries);
    }

    public Environment add(Var<?> k, TypeVar v) {
        return new Environment(this.env, Collections.<Var<?>, TypeVar>singletonMap(k, v));
    }

    public Environment add(Environment other) {
        return new Environment(this.env, other.env);
    }

    public TypeVar get(Var<?> local) {
        Option<TypeVar> mv = this.tryGet(local);
        if (mv.isEmpty()) {
            throw new NoSuchElementException(local.toString());
        }
        return mv.get();
    }

    public Option<TypeVar> tryGet(Var<?> local) {
        return Option.apply(this.env.get(local));
    }

    public Map<Var<?>, TypeVar> getMap() {
        return Collections.unmodifiableMap(this.env);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Environment that = (Environment) o;

        return !(env != null ? !env.equals(that.env) : that.env != null);

    }

    @Override
    public int hashCode() {
        return env != null ? env.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Environment [env=" + env + "]";
    }

}
