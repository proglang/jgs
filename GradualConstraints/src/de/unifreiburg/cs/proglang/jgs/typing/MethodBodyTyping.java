package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Assumptions;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import org.apache.commons.lang3.tuple.Pair;
import soot.Unit;
import soot.jimple.*;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.variable;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;

import java.util.*;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.typing.Result.addConstraints;
import static de.unifreiburg.cs.proglang.jgs.typing.Result.trivialCase;
import static java.util.stream.Collectors.toSet;

/**
 * Context for generating typing constraints and environments for whole method bodies.
 * <p>
 * Created by fennell on 11/15/15.
 */
public class MethodBodyTyping<Level> {

    final private Casts<Level> casts;
    final private ConstraintSetFactory<Level> csets;
    final private Constraints<Level> cstrs;
    final private TypeVars tvars;
    final private SignatureTable<Level> signatures;

    /**
     * @param tvars Factory for generating type variables
     * @param csets Factory for constraint sets
     * @param cstrs The domain of constraints
     * @param casts Specification of cast methods
     * @param signatures
     */
    public MethodBodyTyping(TypeVars tvars, ConstraintSetFactory<Level> csets, Constraints<Level> cstrs, Casts<Level> casts, SignatureTable<Level> signatures) {
        this.csets = csets;
        this.casts = casts;
        this.cstrs = cstrs;
        this.tvars = tvars;
        this.signatures = signatures;
    }

    Set<Constraint<Level>> constraintsForBranches(Unit s,
                                      Environment env,
                                      TypeVar oldPc,
                                      TypeVar newPc) throws TypingException {
        AbstractStmtSwitch g = new AbstractStmtSwitch() {

            @Override
            public void defaultCase(Object obj) {
                throw new RuntimeException("Case not implemented: " + obj);
            }

            @Override
            public void caseIfStmt(IfStmt stmt) {

                Stream.Builder<Constraint<Level>> cs = Stream.builder();

                // add new pc as upper bound to old pc
                cs.add(Constraints.le(variable(oldPc), variable(newPc)));

                Var.getAllFromValueBoxes(stmt.getUseBoxes()).forEach(v ->
                        cs.add(Constraints.le(variable(env.get(v)), variable(newPc))));

                setResult(cs.build().collect(toSet()));
            }

        };
        s.apply(g);
        //noinspection unchecked
        return (Set<Constraint<Level>>) g.getResult();
    }



    public Result<Level> generateResult(DirectedGraph<Unit> g, TypeVar pc, Environment env) throws TypingException {
        try {
            Assumptions.validUnitGraph(g);
        } catch (Assumptions.Violation e) {
            throw new TypingException("Unexpected unit graph: " + e.getMessage());
        }

        // valid unit graphs have exactly one head
        Stmt s = (Stmt) g.getHeads().get(0);

        @SuppressWarnings("unchecked") DominatorsFinder<Unit> postdoms = new MHGPostDominatorsFinder(g);

        return generateResult(tvars.forMethod(g), g, postdoms, s, Result.fromEnv(csets, env), signatures, pc, Collections.emptySet(), Optional.empty());
    }



    private Result<Level> generateResult(MethodTypeVars sTvars,
                                         DirectedGraph<Unit> g,
                                         DominatorsFinder<Unit> postdoms,
                                         Stmt s,
                                         // previous: the previous result
                                         Result<Level> previous,
                                         SignatureTable<Level> signatures,
                                         TypeVar topLevelContext,
                                         // visited: branches that were already in the current context visited, identified by their first statement
                                         Set<Pair<TypeVar,Unit>> visited,
                                         // until: a unit where result generation should stop
                                         Optional<Unit> until) throws TypingException {

        // when there is an "until" unit and we reached it, stop
        if (until.map(s::equals).orElse(false)) {
            return previous;
        }
        // otherwise calculate the result

        Result<Level> r;
        List<Unit> successors = g.getSuccsOf(s);

        // a basic (non-branching) unit in a straight-line sequence
        if (successors.size() <= 1) {
            BasicStatementTyping<Level> bsTyping = new BasicStatementTyping<>(csets, sTvars, cstrs);
            Result<Level> atomic = bsTyping.generate(s, previous.getFinalEnv(), Collections.singleton(topLevelContext), signatures, casts);
            r = Result.addEffects(Result.addConstraints(atomic, previous.getConstraints()), previous.getEffects());
            // if the unit is the end of a sequence, stop
            if (successors.isEmpty()) {
                return r;
            } else{
                // otherwise continue with the rest of the sequence
                Stmt next = (Stmt) successors.get(0);
                // TODO-performance: this is a tailcall
                return generateResult(sTvars, g, postdoms, next, r, signatures, topLevelContext, visited, until);
            }
        } else {
            // a branching statement. When the graph is checked with Assumptions.validUnitGraph then result should never be null
            Unit end = postdoms.getImmediateDominator(s);


            // get condition constrains and the new context
            TypeVar newPc = sTvars.forContext(s);
            Set<Constraint<Level>> conditionConstraints = constraintsForBranches(s, previous.getFinalEnv(), topLevelContext, newPc);
            Result<Level> conditionResult = addConstraints(previous,csets.fromCollection(conditionConstraints));
            // generate results for each branch and join to final results
            r = trivialCase(csets);
            for (Unit uBranch : successors) {
                // do not recurse into visited branches again
                if (visited.contains(Pair.of(newPc, uBranch))) {
                    return conditionResult;
                }
                Set<Pair<TypeVar, Unit>> newVisited = Stream.concat(Stream.of(Pair.of(newPc, uBranch)), visited.stream()).collect(toSet());
                Result<Level> branchResult = generateResult(sTvars, g, postdoms, (Stmt)uBranch, conditionResult, signatures, newPc, newVisited, Optional.of(end));
                r = Result.join(r, branchResult, csets, sTvars);
            }
            // continue after join point
            // TODO-performance: this is a tailcall
            return generateResult(sTvars, g, postdoms, (Stmt)end, r, signatures, topLevelContext, visited, until);
        }
    }
}
