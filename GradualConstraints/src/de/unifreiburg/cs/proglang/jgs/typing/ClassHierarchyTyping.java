package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet.RefinementCheckResult;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Supertypes;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.EffectRefinementResult;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Signature;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import org.apache.commons.lang3.tuple.Pair;
import soot.SootMethod;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Check that all overriding methods are subsumed by their "super"-implementations
 */
public class ClassHierarchyTyping {

    /**
     * Result of a class hierarchy check. If the error is present, then it also contains a counterexample, i.e.
     * error.counterExample.isPresent() returns true.
     */
    public static class Result<Level> {

        public final Optional<SubtypeError<Level>> error;

        private Result(Optional<SubtypeError<Level>> error) {
            this.error = error;
        }

        public boolean isSuccess() {
            return !error.isPresent();
        }

        @Override
        public String toString() {
            return error.toString();
        }
    }

    public static class SubtypeError<Level> {
        public final SootMethod subtypeMethod;
        public final SootMethod superTypeMethod;
        public final RefinementCheckResult<Level> constraintsCheckResult;
        public final EffectRefinementResult<Level> effectCheckResult;

        public SubtypeError(SootMethod subtypeMethod,
                            SootMethod superTypeMethod,
                            RefinementCheckResult<Level> constraintsCheckResult,
                            EffectRefinementResult<Level> effectCheckResult) {
            this.subtypeMethod = subtypeMethod;
            this.superTypeMethod = superTypeMethod;
            this.effectCheckResult = effectCheckResult;
            if (!constraintsCheckResult.isSuccess() && !effectCheckResult.isSuccess()) {
                throw new IllegalArgumentException("Subtyping check is successful");
            }
            this.constraintsCheckResult = constraintsCheckResult;
        }

        @Override
        public String toString() {
            return String.format(
                    "Overriding method: %s\n" +
                            "Super method: %s\n" +
                            "Constraint check: %s\n" +
                            "Effect check: %s", subtypeMethod, superTypeMethod, constraintsCheckResult, effectCheckResult);
        }
    }

    /**
     * Check if the overriding methods of {@code methodStream} refine their super-implementations.
     * <p>
     * The signature table <code>Signatures</code> has to include a signature for all methods.
     */
    public static <Level> Result<Level> checkMethods(
            ConstraintSetFactory<Level> csets,
            TypeDomain<Level> types,
            SignatureTable<Level> signatures,
            Stream<SootMethod> methodStream) {
        Stream<SubtypeError<Level>> errors = methodStream.flatMap(m1 -> {

            Signature<Level> sig1 = signatures.get(m1).orElseThrow(
                            () -> new IllegalArgumentException("No signature found for method "
                                                               + m1
                                                               + "\n "
                                                               + signatures.toString()));

            Stream<SootMethod> overridden = Supertypes.findOverridden(m1);
            return overridden.flatMap(m2 -> {

                Signature<Level> sig2 = signatures.get(m2).orElseThrow(
                        () -> new IllegalArgumentException("No signature found for method "
                                                           + m2));

                Pair<RefinementCheckResult<Level>, EffectRefinementResult<Level>> result =
                        sig1.refines(csets, types, sig2);
                if (result.getLeft().isSuccess() && result.getRight().isSuccess()) {
                    return Stream.empty();
                } else {
                    return Stream.of(new SubtypeError<Level>(m1,
                                                             m2,
                                                             result.getLeft(),
                                                             result.getRight()));
                }
            });
        });
        return new Result<>(errors.findAny());
    }
}
