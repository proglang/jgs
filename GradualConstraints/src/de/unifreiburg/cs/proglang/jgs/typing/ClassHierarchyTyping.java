package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet.RefinementCheckResult;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Supertypes;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.EffectRefinementResult;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Signature;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import org.apache.commons.lang3.tuple.Pair;
import soot.SootClass;
import soot.SootMethod;
import soot.util.Chain;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Check that all overriding methods are subsumed by their
 * "super"-implementations
 */
public class ClassHierarchyTyping {

    /**
     * Result of a class hierarchy check. If the error is present, then it also
     * contains a counterexample, i.e. error.counterExample.isPresent() returns
     * true.
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
            if (constraintsCheckResult.isSuccess()
                && effectCheckResult.isSuccess()) {
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

    public static <Level> Result<Level> checkTwoMethods(
            ConstraintSetFactory<Level> csets,
            TypeDomain<Level> types,
            SignatureTable<Level> signatures,
            SootMethod subMethod, SootMethod superMethod) {

        String errorMsgTail =
                "when checking that " + subMethod.toString() + " refines "
                + superMethod.toString();
        Signature<Level> sig1 = signatures.get(subMethod).orElseGet(() -> {
            throw new TypingAssertionFailure(String.format("No signature found for %s %s", subMethod.toString(), errorMsgTail));
        });
        Signature<Level> sig2 = signatures.get(superMethod).orElseGet(() -> {
            throw new TypingAssertionFailure(String.format("No signature found for %s %s", superMethod.toString(), errorMsgTail));
        });

        Pair<RefinementCheckResult<Level>, EffectRefinementResult<Level>>
                result =
                sig1.refines(csets, types, sig2);
        if (result.getLeft().isSuccess()
            && result.getRight().isSuccess()) {
            return new Result<Level>(Optional.empty());
        } else {
            return new Result<Level>(Optional.of(new SubtypeError<Level>(subMethod,
                                                     superMethod,
                                                     result.getLeft(),
                                                     result.getRight())));
        }
    }

    /**
     * Check if the overriding methods of {@code methodStream} refine their
     * super-implementations.
     * <p>
     * The signature table <code>Signatures</code> has to include a signature
     * for all methods.
     */
    public static <Level> Result<Level> checkMethods(
            ConstraintSetFactory<Level> csets,
            TypeDomain<Level> types,
            SignatureTable<Level> signatures,
            Stream<SootMethod> methodStream) {
        Stream<SubtypeError<Level>> errors = methodStream.flatMap(m1 -> {

            Stream<SootMethod> overridden = Supertypes.findOverridden(m1);
            return overridden.flatMap(m2 -> {
                return checkTwoMethods(csets, types, signatures, m1, m2).error.map(Stream::of).orElse(Stream.empty());
            });
        });
        return new Result<>(errors.findAny());
    }


    public static <Level> Result<Level> check(
            ConstraintSetFactory<Level> csets,
            TypeDomain<Level> types,
            SignatureTable<Level> signatures,
            Stream<SootClass> classes) {
        return checkMethods(csets, types, signatures,
                            classes.flatMap(c -> c.getMethods().stream()));

    }

}
