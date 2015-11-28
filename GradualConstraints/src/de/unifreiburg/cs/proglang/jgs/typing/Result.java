package de.unifreiburg.cs.proglang.jgs.typing;

import com.sun.istack.internal.NotNull;
import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Collectors.*;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.emptyEffect;
import static java.util.stream.Collectors.toList;

/**
 * The result of typing a statement: a set of constraints, an
 * environment that holds _after_ execution of the statement, and an effect.
 *
 * @author fennell
 */
public class Result<LevelT> {
    private final ConstraintSet<LevelT> constraints;

    @NotNull
    private final MethodSignatures.Effects<LevelT> effects;
    private final Environment env;

    // factory methods
    public static <Level> Result<Level> trivialCase(ConstraintSetFactory<Level> csets) {
        return new Result<>(csets.empty(),
                emptyEffect(),
                Environments.makeEmpty());
    }

    public static <Level> Result<Level> fromEnv(ConstraintSetFactory<Level> csets, Environment env) {
        return new Result<>(csets.empty(), emptyEffect(), env);
    }



    public static <Level> Result<Level> join(Result<Level> r1, Result<Level> r2, ConstraintSetFactory<Level> csets, TypeVars tvars) {
        Environment.JoinResult<Level> envJoin = Environment.join(r1.getFinalEnv(),r2.getFinalEnv(), tvars);
        List<Constraint<Level>> csList = envJoin.constraints.collect(Collectors.toList());
        ConstraintSet<Level> cs = r1.constraints.add(r2.constraints).add(csets.fromCollection(csList));
        return new Result<>(cs, r1.effects.add(r2.effects), envJoin.env) ;
    }

    public static <Level> Result<Level> addConstraints(Result<Level> r, ConstraintSet<Level> constraints) {
        return new Result<>(r.constraints.add(constraints), r.effects, r.env);
    }

    public static <Level> Result<Level> addEffects(Result<Level> r, MethodSignatures.Effects<Level> effects) {
        return new Result<>(r.constraints, r.effects.add(effects), r.env);
    }

    // impl

    Result(ConstraintSet<LevelT> constraints,
           @NotNull
           MethodSignatures.Effects<LevelT> effects,
           Environment env) {
        super();
        this.constraints = constraints;
        this.effects = effects;
        this.env = env;
    }

    public ConstraintSet<LevelT> getConstraints() {
        return this.constraints;
    }

    public Environment getFinalEnv() {
        return env;
    }

    public MethodSignatures.Effects<LevelT> getEffects() {
        return effects;
    }

//    public TypeVars.TypeVar initialTypeVariableOf(Var<?> local) {
//        return this.transition.getInit().get(local);
//    }
//
    public TypeVars.TypeVar finalTypeVariableOf(Var<?> local) {
        return this.env.get(local);
    }

}
