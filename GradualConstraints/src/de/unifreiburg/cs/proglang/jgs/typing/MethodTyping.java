package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import main.java.de.unifreiburg.cs.proglang.jgs.constraints.CTypes.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet.RefinementCheckResult;
import main.java.de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import main.java.de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import main.java.de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods;
import main.java.de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.Effects;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import main.java.de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain;
import main.java.de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import soot.SootMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static main.java.de.unifreiburg.cs.proglang.jgs.constraints.CTypes.variable;
import static main.java.de.unifreiburg.cs.proglang.jgs.signatures.Symbol.methodParameters;
import static java.util.stream.Collectors.toList;

/**
 * Context for typing methods Created by fennell on 1/12/16.
 */
public class MethodTyping<Level> {
    final private ConstraintSetFactory<Level> csets;
    final private Constraints<Level> cstrs;
    private final Casts<Level> casts;

    static public class Result<Level> {
        private final TypeDomain<Level> types;
        public final ConstraintSet<Level> completeBodyConstraints;
        public final RefinementCheckResult<Level> refinementCheckResult;
        public final Effects<Level> missedEffects;
        public final Effects<Level> sigEffects;
        public final Effects<Level> inferredEffects;
        public final Supplier<List<ConflictCause<Level>>> conflictCauses;

        public Result(TypeDomain<Level> types, ConstraintSet<Level> completeBodyConstraints, RefinementCheckResult<Level> refinementCheckResult, Effects<Level> sigEffects, Effects<Level> inferredEffects, Effects<Level> missedEffects, Supplier<List<ConflictCause<Level>>> conflicCauses) {
            this.types = types;
            this.completeBodyConstraints = completeBodyConstraints;
            this.refinementCheckResult = refinementCheckResult;
            this.missedEffects = missedEffects;
            this.inferredEffects = inferredEffects;
            this.sigEffects = sigEffects;
            this.conflictCauses = conflicCauses;
        }

        /**
         * @return true, if the signature allows the method to be called, i.e.
         * its constraints can be solved
         */
        public boolean signatureHasSolution() {
            return this.refinementCheckResult.abstractConstraints.isSat(types);
        }

        public boolean bodyHasSolution() {
            return this.completeBodyConstraints.isSat(types);
        }

        /**
         * @return true if the type analysis determined that the method
         * implementation complies to the method signature and that the typing
         * constraints of the method's body are satisfiable if the signature is
         * satisfiable.
         */
        public boolean isSuccess() {
            return (!this.signatureHasSolution() || this.bodyHasSolution())
                   && !refinementCheckResult.counterExample.isPresent()
                   && missedEffects.isEmpty();
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder("Typing result: \n");
            if (!this.signatureHasSolution()) {
                b.append("- !! Method cannot be called (has an unsatisfiable signature constraints)");
            }
            if (!this.refinementCheckResult.concreteConstraints.isSat(types)) {
                b.append("- !! Conflicting constraints in method body\n");
            } else {
                b.append("- Method body has no typing conflicts\n");
            }
            if (this.refinementCheckResult.counterExample.isPresent()) {
                b.append("- Error in refining constraints (see below)!\n");
            } else {
                b.append("- The constraints of the abstractConstraints are sound\n");
            }
            if (!this.missedEffects.isEmpty()) {
                b.append("- The abstractConstraints is missing effects: ");
                b.append(this.missedEffects.toString());
                b.append("\n\n");
            } else {
                b.append("- The effects of the abstractConstraints are sound \n");
            }
            b.append("Result of constraint refinement check:\n");
            b.append(this.refinementCheckResult.toString());
            b.append("\nInferred effects: ")
             .append(this.inferredEffects.toString())
             .append(" abstractConstraints effects: ")
             .append(this.sigEffects.toString());
            return b.toString();
        }
    }

    /**
     * @param csets Factory for constraint sets
     * @param cstrs The domain of constraints
     * @param casts Specification of cast methods
     */
    public MethodTyping(ConstraintSetFactory<Level> csets, Constraints<Level> cstrs, Casts<Level> casts) {
        this.csets = csets;
        this.casts = casts;
        this.cstrs = cstrs;
    }

    /**
     * Check if the signature of a method complies (i.e. subsumes) the
     * constraints generated for the body.
     *
     * @param method The method to check
     * @return A pair of typing errors. If the left component is present there
     * was an error refining the constraints, if the right component is present,
     * it yields the effect types inferred for the body that the signature does
     * not cover.
     * @throws TypingException
     */
    // TODO: what's up with "this"?
    public Result<Level> check(TypeVars tvars, SignatureTable<Level> signatures, FieldTable<Level> fields, SootMethod method) throws TypingException {
        // Get the signature of "method"
        MethodSignatures.Signature<Level> signatureToCheck =
                signatures.get(method)
                          .orElseThrow(() -> new TypingException(
                                  "No signature found for method "
                                  + method.toString()));


        // type check the body and connect signature with the typing result through a symbol mapping
        Map<Symbol.Param<Level>, TypeVar> paramMapping =
                Methods.symbolMapForMethod(tvars, method);
        Environment init = Environments.forParamMap(tvars, paramMapping);


        BodyTypingResult<Level> r =
                new MethodBodyTyping<>(method, tvars, csets, cstrs, casts, signatures, fields).generateResult(method.retrieveActiveBody(), tvars.topLevelContext(), init);

        // Symbol map is the parameter map plus an entry that maps "@ret" to "ret"
        Map<Symbol<Level>, CType<Level>> symbolMapping = new HashMap<>();
        paramMapping.forEach((k,v) -> {
            symbolMapping.put(k, variable(v));
        });
        symbolMapping.put(Symbol.ret(), variable(tvars.ret()));

        ConstraintSet<Level> sigConstraints =
                csets.fromCollection(signatureToCheck.constraints.toTypingConstraints(symbolMapping).collect(toList()));

        ConstraintSet<Level> bodyConstraints =
                r.getConstraints().asSignatureConstraints(tvars, methodParameters(method).stream().map(Var::fromParam));


        return new Result<>(cstrs.types, r.getConstraints(),
                            bodyConstraints.refines(sigConstraints),
                            signatureToCheck.effects,
                            r.getEffects(), r.getEffects().refines(cstrs.types, signatureToCheck.effects).missingEffects,
                            () -> {
                                List<ConflictCause<Level>> causes = r.getConstraints().findConflictCause(r.getTags());
                                if (causes.isEmpty()) {
                                    Logger.getGlobal().info(r.getTags().toString());
                                }
                                return causes;
                            });
    }


}
