package de.unifreiburg.cs.proglang.jgs.typing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.Variable;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import soot.Local;
import soot.Value;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.*;

/**
 * Environments: immutable maps from Locals to type variables.
 * 
 * @author fennell
 *
 */
public class Environment {

    private final Map<Local, TypeVar> env;

    public Environment(Map<Local, TypeVar> env) {
        this.env = new HashMap<>(env);
    }

    private Environment(Map<Local, TypeVar> env, Local k, TypeVar v) {
        this(env);
        env.put(k, v);
    }

    public Environment add(Local k, TypeVar v) {
        return new Environment(this.env, k, v);
    }

    public CType<Level> get(Local local) {
        return this.tryGet(local)
                   .orElseThrow(() -> new NoSuchElementException(local.toString()));
    }

    public Optional<CType<Level>> tryGet(Local local) {
        return Optional.ofNullable(this.env.get(local)).map(t -> variable(t));
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
