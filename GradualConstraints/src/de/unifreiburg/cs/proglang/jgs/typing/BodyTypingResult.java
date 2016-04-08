package de.unifreiburg.cs.proglang.jgs.typing;

import com.sun.istack.internal.NotNull;
import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.Effects;

import java.util.List;
import java.util.stream.Collectors;

import static de.unifreiburg.cs.proglang.jgs.signatures.Effects.emptyEffect;

/**
 * The result of typing a statement: a set of constraints, an environment that
 * holds _after_ execution of the statement, and an effect.
 *
 * @author fennell
 */
// TODO: make a case class out of this
public class BodyTypingResult<LevelT> {
    @NotNull
    private final ConstraintSet<LevelT> constraints;
    @NotNull
    private final Effects<LevelT> effects;
    @NotNull
    private final Environment env;
    private final TagMap<LevelT> tags;

    // factory methods
    public static <Level> BodyTypingResult<Level> trivialCase(ConstraintSetFactory<Level> csets) {
        return new BodyTypingResult<>(csets.empty(),
                                      emptyEffect(),
                                      Environments.makeEmpty(), TagMap.empty());
    }

    public static <Level> BodyTypingResult<Level> fromEnv(ConstraintSetFactory<Level> csets, Environment env) {
        return new BodyTypingResult<>(csets.empty(), emptyEffect(), env, TagMap.empty());
    }


    public static <Level> BodyTypingResult<Level> join(BodyTypingResult<Level> r1, BodyTypingResult<Level> r2, ConstraintSetFactory<Level> csets, TypeVars tvars, String condition) {
        Environment.JoinResult<Level> envJoin =
                Environment.join(tvars, r1.getFinalEnv(), r2.getFinalEnv());
        List<Constraint<Level>> csList =
                envJoin.constraints.collect(Collectors.toList());
        TagMap.Builder<Level> joinTags = TagMap.builder();
        csList.forEach(c -> joinTags.add(c, new TypeVarTags.Join(condition)));
        ConstraintSet<Level> cs =
                r1.constraints.add(r2.constraints).add(csets.fromCollection(csList));
        return new BodyTypingResult<>(cs, r1.effects.add(r2.effects), envJoin.env, r1.tags.addAll(r2.tags).addAll(joinTags.build()));
    }

    public static <Level> BodyTypingResult<Level> addConstraints(BodyTypingResult<Level> r, ConstraintSet<Level> constraints) {
        return new BodyTypingResult<>(r.constraints.add(constraints), r.effects, r.env, r.tags);
    }

    public static <Level> BodyTypingResult<Level> addEffects(BodyTypingResult<Level> r, Effects<Level> effects) {
        return new BodyTypingResult<>(r.constraints, r.effects.add(effects), r.env, r.tags);
    }

    // impl

    BodyTypingResult(@NotNull ConstraintSet<LevelT> constraints,
                     @NotNull Effects<LevelT> effects,
                     @NotNull Environment env,
                     TagMap<LevelT> tags) {
        super();
        if (constraints == null || effects == null || env == null) {
            throw new NullPointerException("when instantiating results");
        }
        this.constraints = constraints;
        this.effects = effects;
        this.env = env;
        this.tags = tags;
    }

    public ConstraintSet<LevelT> getConstraints() {
        return this.constraints;
    }

    public Environment getFinalEnv() {
        return env;
    }

    public Effects<LevelT> getEffects() {
        return effects;
    }

    public TagMap<LevelT> getTags() {
        return tags;
    }


    public TypeVars.TypeVar finalTypeVariableOf(Var<?> local) {
        return this.env.get(local);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BodyTypingResult<?> result = (BodyTypingResult<?>) o;

        if (constraints != null ? !constraints.equals(result.constraints) :
            result.constraints != null) return false;
        if (effects != null ? !effects.equals(result.effects) : result.effects
                                                                != null)
            return false;
        return !(env != null ? !env.equals(result.env) : result.env != null);

    }

    @Override
    public int hashCode() {
        int result = constraints != null ? constraints.hashCode() : 0;
        result = 31 * result + (effects != null ? effects.hashCode() : 0);
        result = 31 * result + (env != null ? env.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BodyTypingResult{" +
               "constraints=" + constraints +
               ", effects=" + effects +
               ", env=" + env +
               '}';
    }
}
