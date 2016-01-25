package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSet;
import de.unifreiburg.cs.proglang.jgs.constraints.ConstraintSetFactory;
import de.unifreiburg.cs.proglang.jgs.constraints.Constraints;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeVars;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Methods;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;

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
        public final ConstraintSet.RefinementCheckResult refinementCheckResult;
        public final Optional<MethodSignatures.Effects> missedEffects;

        public Result(ConstraintSet.RefinementCheckResult refinementCheckResult, Optional<MethodSignatures.Effects> missedEffects) {
            this.refinementCheckResult = refinementCheckResult;
            this.missedEffects = missedEffects;
        }

        public boolean isSuccess() {
            return !(refinementCheckResult.counterExample.isPresent() || missedEffects.isPresent());
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
    public Result<Level> check(TypeVars tvars, SignatureTable<Level> signatures, SootMethod method) throws TypingException {
        // Get the signature of "method"
        MethodSignatures.Signature<Level> signatureToCheck = signatures.get(method)
                .orElseThrow(() -> new TypingException("No signature found for method " + method.toString()));


        // type check the body and connect signature with the typing result through a symbol mapping
        Map<Symbol.Param<Level>, TypeVars.TypeVar> paramMapping = Methods.symbolMapForMethod(tvars, method);
        Environment init = Environments.forParamMap(tvars, paramMapping);


        DirectedGraph<Unit> body = new BriefUnitGraph(method.getActiveBody());
        BodyTypingResult<Level> r = new MethodBodyTyping<>(tvars, csets, cstrs, casts, signatures).generateResult(body, tvars.topLevelContext(), init);

        // Symbol map is the parameter map plus an entry that maps "@ret" to "ret"
        Map<Symbol<Level>, TypeVars.TypeVar> symbolMapping = new HashMap<>(paramMapping);
        symbolMapping.put(Symbol.ret(), tvars.ret());

        ConstraintSet<Level> sigConstraints = csets.fromCollection(signatureToCheck.constraints.toTypingConstraints(symbolMapping).collect(Collectors.toList()));

        return new Result(sigConstraints.signatureCounterExample(tvars, methodParameters(method).stream().map(Var::fromParam), r.getConstraints())
        ,signatureToCheck.effects.checkSubsumptionOf(r.getEffects()));
    }

}
