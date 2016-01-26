package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraints;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.unifreiburg.cs.proglang.jgs.signatures.Symbol.methodParameters;

/**
 * Context for typing methods
 * Created by fennell on 1/12/16.
 */
public class MethodTyping<Level> {
    final private ConstraintSetFactory<Level> csets;
    final private Constraints<Level> cstrs;
    private final Casts<Level> casts;

    static public class Result<Level> {
        public final ConstraintSet.RefinementCheckResult<Level> refinementCheckResult;
        public final Optional<MethodSignatures.Effects<Level>> missedEffects;
        public final MethodSignatures.Effects sigEffects;
        public final MethodSignatures.Effects inferredEffects;

        public Result(ConstraintSet.RefinementCheckResult<Level> refinementCheckResult, MethodSignatures.Effects sigEffects, MethodSignatures.Effects inferredEffects, Optional<MethodSignatures.Effects<Level>> missedEffects) {
            this.refinementCheckResult = refinementCheckResult;
            this.missedEffects = missedEffects;
            this.inferredEffects = inferredEffects;
            this.sigEffects = sigEffects;
        }

        public boolean isSuccess() {
            return !(refinementCheckResult.counterExample.isPresent() || missedEffects.isPresent());
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder("Typing result: \n");
            if (this.refinementCheckResult.counterExample.isPresent()) {
                b.append("- Error in refining constraints (see below)!\n");
            } else {
                b.append("- The constraints of the signature are sound\n");
            }
            if (this.missedEffects.isPresent()) {
                b.append("- The signature is missing effects: ");
                b.append(this.missedEffects.toString());
                b.append("\n\n");
            } else {
                b.append("- The effects of the signature are sound \n");
            }
            b.append("Result of constraint refinement check:\n");
            b.append(this.refinementCheckResult.toString());
            b.append("\nInferred effects: " + this.inferredEffects.toString() + " signature effects: " + this.sigEffects.toString());
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
     * Check if the signature of a method complies (i.e. subsumes) the constraints generated for the body.
     * @param method The method to check
     * @return A pair of typing errors. If the left component is present there was an error refining the constraints, if the right component is present, it yields the effect types inferred for the body that the signature does not cover.
     * @throws TypingException
     */
    // TODO: what's up with "this"?
    public Result<Level> check(TypeVars tvars, SignatureTable<Level> signatures, FieldTable<Level> fields, SootMethod method) throws TypingException {
        // Get the signature of "method"
        MethodSignatures.Signature<Level> signatureToCheck = signatures.get(method)
                .orElseThrow(() -> new TypingException("No signature found for method " + method.toString()));


        // type check the body and connect signature with the typing result through a symbol mapping
        Map<Symbol.Param<Level>, TypeVars.TypeVar> paramMapping = Methods.symbolMapForMethod(tvars, method);
        Environment init = Environments.forParamMap(tvars, paramMapping);


        DirectedGraph<Unit> body = new BriefUnitGraph(method.getActiveBody());
        BodyTypingResult<Level> r = new MethodBodyTyping<>(tvars, csets, cstrs, casts, signatures, fields).generateResult(body, tvars.topLevelContext(), init);

        // Symbol map is the parameter map plus an entry that maps "@ret" to "ret"
        Map<Symbol<Level>, TypeVars.TypeVar> symbolMapping = new HashMap<>(paramMapping);
        symbolMapping.put(Symbol.ret(), tvars.ret());

        ConstraintSet<Level> sigConstraints = csets.fromCollection(signatureToCheck.constraints.toTypingConstraints(symbolMapping).collect(Collectors.toList()));

        return new Result(sigConstraints.signatureCounterExample(tvars, methodParameters(method).stream().map(Var::fromParam), r.getConstraints())
        ,signatureToCheck.effects, r.getEffects(), signatureToCheck.effects.checkSubsumptionOf(cstrs.types, r.getEffects()));
    }

}
