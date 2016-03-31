package de.unifreiburg.cs.proglang.jgs.typing;

import static main.java.de.unifreiburg.cs.proglang.jgs.constraints.CTypes.variable;

import java.util.*;
import java.util.stream.Stream;

import de.unifreiburg.cs.proglang.jgs.constraints.Constraint;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraints;
import main.java.de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import main.java.de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import main.java.de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;

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
     * Joint two environments. Returns a <code>BodyTypingResult</code> that contains the joined environment and constraints that force unification of local variables. The effects of the result are not needed (left trivialCase) which is a bit of a hack...
     */
    public static <Level> JoinResult<Level> join(TypeVars tvars, Environment r1, Environment r2) {
        Map<Var<?>, TypeVar> joinedEnv = new HashMap<>();
        Stream.Builder<Constraint<Level>> cs = Stream.builder();
        r1.env.entrySet().stream().forEach( e -> {
            Var<?> k = e.getKey();
            TypeVar v1 = e.getValue();
            if (r2.env.containsKey(k)) {
                TypeVar v2 = r2.get(k);
                if (!v1.equals(v2)) {
                    TypeVar vNew = tvars.join(v1, v2);
                    Stream.<Constraint<Level>>of(Constraints.le(variable(v1), variable(vNew)), Constraints.le(variable(v2), variable(vNew))).forEach(cs);
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
            if (!r1.env.containsKey(k)) {
                joinedEnv.put(k, e.getValue());
            }
        });
        return new JoinResult<>(cs.build(), Environments.fromMap(joinedEnv));
    }

    public Environment(Map<Var<?>, TypeVar> env) {
        this.env = new HashMap<>(env);
    }

    private Environment(Map<Var<?>, TypeVar> env, Map<Var<?>, TypeVar> newEntries) {
        this(env);
        this.env.putAll(newEntries);
    }

    public Environment add(Var<?> k, TypeVar v) {
        return new Environment(this.env, Collections.singletonMap(k, v));
    }

    public Environment add(Environment other) {
        return new Environment(this.env, other.env);
    }

    public TypeVar get(Var<?> local) {
        return this.tryGet(local)
                   .orElseThrow(() -> new NoSuchElementException(local.toString()));
    }

    public Optional<TypeVar> tryGet(Var<?> local) {
        return Optional.ofNullable(this.env.get(local));
    }

    public Map<Var<?>, TypeVar> debug_getMap() {
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
