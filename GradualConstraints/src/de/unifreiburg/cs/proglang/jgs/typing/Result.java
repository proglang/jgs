package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.emptyEffect;

/**
 * The result of typing a statement: a set of constraints, an
 * environment that holds _after_ execution of the statement, and an effect.
 *
 * @author fennell
 */
public class Result<LevelT> {
    private final ConstraintSet<LevelT> constraints;
    private final MethodSignatures.Effects<LevelT> effects;
    private final Environment env;

    // factory methods
    public static <Level> Result<Level> empty(ConstraintSetFactory<Level> csets) {
        return new Result<>(csets.empty(),
                emptyEffect(),
                Environments.makeEmpty());
    }

    public static <Level> Result<Level> fromParameters(TypeVars tvars) {
        throw new RuntimeException("Not Implemented!");
    }

    // impl

    Result(ConstraintSet<LevelT> constraints,
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

//    public TypeVars.TypeVar initialTypeVariableOf(Var<?> local) {
//        return this.transition.getInit().get(local);
//    }
//
    public TypeVars.TypeVar finalTypeVariableOf(Var<?> local) {
        return this.env.get(local);
    }

}
