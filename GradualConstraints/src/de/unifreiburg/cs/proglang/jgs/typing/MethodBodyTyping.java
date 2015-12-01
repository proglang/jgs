package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
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

    public MethodBodyTyping(ConstraintSetFactory<Level> csets, TypeDomain<Level> types, Constraints<Level> cstrs, Casts<Level> casts) {
        this.csets = csets;
        this.casts = casts;
        this.types = types;
        this.cstrs = cstrs;
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
        List<Unit> heads = g.getHeads();
        if (heads.size() != 1) {
            throw new TypeError("Analyzing graphs with more than a single head is not supported");
        }

        Stmt s = (Stmt) heads.get(0);

        @SuppressWarnings("unchecked") DominatorsFinder<Unit> postdoms = new MHGPostDominatorsFinder(g);

        return generateResult(g, postdoms, s, Result.fromEnv(csets, env), signatures, pc, Collections.emptySet(), Optional.empty());
    }



    private Result<Level> generateResult(DirectedGraph<Unit> g, DominatorsFinder<Unit> postdoms, Stmt s, Result<Level> previous, SignatureTable<Level> signatures, TypeVar topLevelContext, Set<Unit> visited, Optional<Unit> until) throws TypeError {
        TypeVars sTvars = new TypeVars("{" + s.toString() + "}");
        Result<Level> r;

        List<Unit> successors = g.getSuccsOf(s);
        if (until.map(s::equals).orElse(false)) {
            return previous;
        }

        // a basic statement
        if (successors.size() <= 1) {
            BasicStatementTyping<Level> bsTyping = new BasicStatementTyping<>(csets, types, sTvars, cstrs);
            Result<Level> atomic = bsTyping.generate(s, previous.getFinalEnv(), Collections.singleton(topLevelContext), signatures, casts);
            r = Result.addEffects(Result.addConstraints(atomic, previous.getConstraints()), previous.getEffects());
            if (successors.isEmpty()) {
                return r;
            } else{
                Stmt next = (Stmt) successors.get(0);
                // TODO: this is a tailcall
                return generateResult(g, postdoms, next, r, signatures, topLevelContext, visited, until);
            }
        } else {
            // a branching statement
            Unit end = postdoms.getImmediateDominator(s);
            if (s.equals(end)) {
                throw new RuntimeException("Branching statement is its own postdominator: " + s);
            }
            // get condition constrains and the new context
            TypeVar newPc = sTvars.fresh("pc");
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
                Result<Level> branchResult = generateResult(g, postdoms, (Stmt)uBranch, conditionResult, signatures, newPc, newVisited, Optional.of(end));
                r = Result.join(r, branchResult, csets);
            }
            // continue after join point
            // TODO: this is a tailcall
            return generateResult(g, postdoms, (Stmt)end, r, signatures, topLevelContext, visited, until);
        }
    }
}
