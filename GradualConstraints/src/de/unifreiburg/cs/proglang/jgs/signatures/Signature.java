package de.unifreiburg.cs.proglang.jgs.signatures;

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import org.apache.commons.lang3.tuple.Pair;

import java.util.stream.Stream;

/**
 * Signatures: constraints + effects
 */
public class Signature<Level> {
    public final MethodSignatures.SigConstraintSet<Level> constraints;
    public final Effects<Level> effects;

    Signature(MethodSignatures.SigConstraintSet<Level> constraints, Effects<Level> effects) {
        this.constraints = constraints;
        this.effects = effects;
    }

    @Override
    public String toString() {
        return String.format("C:%s, E:%s", constraints.toString(), effects.toString());
    }

    public Signature<Level> addConstraints(scala.collection.Iterator<SigConstraint<Level>> sigs) {
        MethodSignatures.SigConstraintSet<Level> newConstraints =
                this.constraints.addAll(sigs);
        return new Signature<>(newConstraints, this.effects);
    }

    // TODO: use a dedicated result type (to be able to name the second component sensibly)
    public Pair<ConstraintSet.RefinementCheckResult<Level>, Effects.EffectRefinementResult<Level>> refines(ConstraintSetFactory<Level> csets, TypeDomain<Level> types, Signature<Level> other) {
        return Pair.of(this.constraints.refines(csets, types, other.constraints), this.effects.refines(types, other.effects));
    }
}
