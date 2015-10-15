package de.unifreiburg.cs.proglang.jgs.typing;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.variable;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;

/**
 * Environments: immutable maps from Locals to type variables.
 * 
 * @author fennell
 *
 */
public class Environment {

    /**
     * The domain of environments "Var"s which are soot.Local and soot.jimple.ThisRef
     * (and perhaps soot.jimple.ParameterRef). (Hopefully) Their only purpose is to
     * have an identity
     */
    private final Map<Var<?>, TypeVar> env;

    public Environment(Map<Var<?>, TypeVar> env) {
        this.env = new HashMap<>(env);
    }

    private Environment(Map<Var<?>, TypeVar> env, Var<?> k, TypeVar v) {
        this(env);
        env.put(k, v);
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
