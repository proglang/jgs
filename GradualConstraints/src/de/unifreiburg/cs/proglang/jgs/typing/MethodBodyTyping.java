package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Assumptions;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
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
    // TODO: the following two could moved somewhere else (?)
    final private Constraints<Level> cstrs;
    final private TypeDomain<Level> types;
    final private TypeVars tvars;

    public MethodBodyTyping(TypeVars tvars, ConstraintSetFactory<Level> csets, TypeDomain<Level> types, Constraints<Level> cstrs, Casts<Level> casts) {
        this.csets = csets;
        this.casts = casts;
        this.types = types;
        this.cstrs = cstrs;
        this.tvars = tvars;
    }

    Set<Constraint<Level>> constraintsForBranches(Unit s,
                                      Environment env,
                                      TypeVar oldPc,
                                      TypeVar newPc) throws TypeError {
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



    public Result<Level> generateResult(DirectedGraph<Unit> g, TypeVar pc, Environment env,
                                        SignatureTable<Level> signatures) throws TypeError {
        try {
            Assumptions.validUnitGraph(g);
        } catch (Assumptions.Violation e) {
            throw new TypeError("Unexpected unit graph: " + e.getMessage());
        }
        List<Unit> heads = g.getHeads();
        if (heads.size() != 1) {
            throw new TypeError("Analyzing graphs with more than a single head is not supported");
        }

        Stmt s = (Stmt) heads.get(0);

        @SuppressWarnings("unchecked") DominatorsFinder<Unit> postdoms = new MHGPostDominatorsFinder(g);

        // TODO: not using tvars here but tvars.forMethod is important.. make this more typesafe, as it is a mistake waiting to happen
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
                                         Set<Unit> visited,
                                         // until: a unit where result generation should stop
                                         Optional<Unit> until) throws TypeError {

        // when there is an "until" unit and we reached it, stop
        if (until.map(s::equals).orElse(false)) {
            return previous;
        }
        // otherwise calculate the result

        Result<Level> r;
        List<Unit> successors = g.getSuccsOf(s);

        // a basic (non-branching) unit in a straight-line sequence
        if (successors.size() <= 1) {
            BasicStatementTyping<Level> bsTyping = new BasicStatementTyping<>(csets, types, sTvars, cstrs);
            Result<Level> atomic = bsTyping.generate(s, previous.getFinalEnv(), Collections.singleton(topLevelContext), signatures, casts);
            r = Result.addEffects(Result.addConstraints(atomic, previous.getConstraints()), previous.getEffects());
            // if the unit is the end of a sequence, stop
            if (successors.isEmpty()) {
                return r;
            } else{
                // otherwise continue with the rest of the sequence
                Stmt next = (Stmt) successors.get(0);
                // TODO: this is a tailcall
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
                // TODO: not sure if this is correct for spaghetti code!
                if (visited.contains(uBranch)) {
                    return previous;
                }
                Set<Unit> newVisited = Stream.concat(Stream.of(uBranch), visited.stream()).collect(toSet());
                Result<Level> branchResult = generateResult(sTvars, g, postdoms, (Stmt)uBranch, conditionResult, signatures, newPc, newVisited, Optional.of(end));
                r = Result.join(r, branchResult, csets, sTvars);
            }
            // continue after join point
            // TODO: this is a tailcall
            return generateResult(sTvars, g, postdoms, (Stmt)end, r, signatures, topLevelContext, visited, until);
        }
    }
}
