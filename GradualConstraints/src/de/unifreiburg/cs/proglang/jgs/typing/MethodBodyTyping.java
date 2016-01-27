package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Assumptions;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable;
import de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import org.apache.commons.lang3.tuple.Pair;
import soot.Unit;
import soot.jimple.*;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.DominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.literal;
import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.variable;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.unifreiburg.cs.proglang.jgs.signatures.MethodSignatures.effects;
import static de.unifreiburg.cs.proglang.jgs.typing.BodyTypingResult.addConstraints;
import static de.unifreiburg.cs.proglang.jgs.typing.BodyTypingResult.trivialCase;
import static java.util.Arrays.asList;
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
    final private FieldTable<Level> fields;

    /**
     * @param tvars      Factory for generating type variables
     * @param csets      Factory for constraint sets
     * @param cstrs      The domain of constraints
     * @param casts      Specification of cast methods
     * @param signatures
     * @param fields
     */
    public MethodBodyTyping(TypeVars tvars, ConstraintSetFactory<Level> csets, Constraints<Level> cstrs, Casts<Level> casts, SignatureTable<Level> signatures, FieldTable<Level> fields) {
        this.csets = csets;
        this.casts = casts;
        this.cstrs = cstrs;
        this.tvars = tvars;
        this.signatures = signatures;
        this.fields = fields;
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


    public BodyTypingResult<Level> generateResult(DirectedGraph<Unit> g, TypeVar pc, Environment env) throws TypingException {
        try {
            Assumptions.validUnitGraph(g);
        } catch (Assumptions.Violation e) {
            throw new TypingException("Unexpected unit graph: " + e.getMessage());
        }

        // valid unit graphs have exactly one head
        Stmt s = (Stmt) g.getHeads().get(0);

        @SuppressWarnings("unchecked") DominatorsFinder<Unit> postdoms = new MHGPostDominatorsFinder(g);


        BodyTypingResult<Level> r = generateResult(tvars.forMethod(g), g, postdoms, s, BodyTypingResult.fromEnv(csets, env), signatures, fields, pc, Collections.emptySet(), Optional.empty());

        return r;
    }


    private BodyTypingResult<Level> generateResult(MethodTypeVars sTvars,
                                                   DirectedGraph<Unit> g,
                                                   DominatorsFinder<Unit> postdoms,
                                                   Stmt s,
                                                   // previous: the previous result
                                                   BodyTypingResult<Level> previous,
                                                   SignatureTable<Level> signatures,
                                                   FieldTable<Level> fields,
                                                   TypeVar topLevelContext,
                                                   // visited: branches that were already in the current context visited, identified by their first statement
                                                   Set<Pair<TypeVar, Unit>> visited,
                                                   // until: a unit where result generation should stop
                                                   Optional<Unit> until) throws TypingException {

        // when there is an "until" unit and we reached it, stop
        if (until.map(s::equals).orElse(false)) {
            return previous;
        }
        // otherwise calculate the result

        BodyTypingResult<Level> r;
        List<Unit> successors = g.getSuccsOf(s);

        // check for context casts a context casts
        Optional<Casts.CxCast<Level>> maybeCxCast = casts.detectContextCastStartFromStmt(s);
        if (maybeCxCast.isPresent()) {
            //a context cast
            Casts.CxCast<Level> cxCast = maybeCxCast.get();

            if (successors.size() != 1) {
                throw new RuntimeException("Begin of context cast should have exatcly one successor " + s.toString());
            }
            ;
            Stmt startOfCast = (Stmt) successors.get(0); // there should be exactly this one

            // find the end unit
            Set<Unit> cxDoms = postdoms.getDominators(s).stream()
                    .filter(s2 -> casts.detectContextCastEndFromStmt((Stmt) s2))
                    .collect(Collectors.toSet());
            Unit endOfCast = cxDoms.stream()
                    .filter(u -> postdoms.isDominatedByAll(u, cxDoms))
                    // it has to exist, otherwise it is "syntactically" not a context casts
                    .findAny().orElseThrow(() -> new RuntimeException("Could not find end of context casts: " + s.toString()));
            // Type variable for the new context
            TypeVar newPc = sTvars.forContext(s);
            r = generateResult(sTvars, g, postdoms, startOfCast, previous, signatures, fields, newPc, visited, Optional.of(endOfCast));

            // add constraints: oldPc <= source, dest <= newPc, dest <= {effects}
            ConstraintSet<Level> additionalConstraints = csets.fromCollection(Stream.concat(
                    Stream.of(Constraints.le(variable(topLevelContext), literal(cxCast.sourceType)),
                            Constraints.le(literal(cxCast.destType), variable(newPc))),
                    r.getEffects().stream().map(e -> Constraints.le(literal(cxCast.destType), literal(e)))
            ).collect(Collectors.toList()));
            // modify effects: remove dest and add source
            Set<TypeDomain.Type<Level>> newEffects = r.getEffects().stream().collect(toSet());
            return new BodyTypingResult<Level>(r.getConstraints().add(additionalConstraints), effects(newEffects), r.getFinalEnv());
        } else if (successors.size() <= 1) {
            // a basic (non-branching) unit in a straight-line sequence

            BasicStatementTyping<Level> bsTyping = new BasicStatementTyping<>(csets, sTvars, cstrs);
            BodyTypingResult<Level> atomic = bsTyping.generate(s, previous.getFinalEnv(), Collections.singleton(topLevelContext), signatures, fields, casts);
            r = BodyTypingResult.addEffects(BodyTypingResult.addConstraints(atomic, previous.getConstraints()), previous.getEffects());
            // if the unit is the end of a sequence, stop
            if (successors.isEmpty()) {
                return r;
            } else {
                // otherwise continue with the rest of the sequence
                Stmt next = (Stmt) successors.get(0);
                // TODO-performance: this is a tailcall
                return generateResult(sTvars, g, postdoms, next, r, signatures, fields, topLevelContext, visited, until);
            }
        } else {
            // a branching statement. When the graph is checked with Assumptions.validUnitGraph then result should never be null
            Optional<Unit> end = postdoms.getDominators(s).equals(Collections.singletonList(s)) ?
                    // if we are our only postdom, then there will be no join point, i.e. no point where we can lower pc again. This property is ensured ruling out that an if is an exit node (in Assumptions.validUnitGraph)
                    Optional.empty() :
                    Optional.of(postdoms.getImmediateDominator(s));


            // get condition constrains and the new context
            TypeVar newPc = sTvars.forContext(s);
            Set<Constraint<Level>> conditionConstraints = constraintsForBranches(s, previous.getFinalEnv(), topLevelContext, newPc);
            BodyTypingResult<Level> conditionResult = addConstraints(previous, csets.fromCollection(conditionConstraints));
            // generate results for each branch and join to final results
            r = trivialCase(csets);
            for (Unit uBranch : successors) {
                // do not recurse into visited branches again
                if (visited.contains(Pair.of(newPc, uBranch))) {
                    return conditionResult;
                }
                Set<Pair<TypeVar, Unit>> newVisited = Stream.concat(Stream.of(Pair.of(newPc, uBranch)), visited.stream()).collect(toSet());
                BodyTypingResult<Level> branchResult = generateResult(sTvars, g, postdoms, (Stmt) uBranch, conditionResult, signatures, fields, newPc, newVisited, end);
                r = BodyTypingResult.join(r, branchResult, csets, sTvars);
            }
            // continue after join point, if there is any
            if (end.isPresent()) {
                //TODO-performance: this is a tailcall
                return generateResult(sTvars, g, postdoms, (Stmt) end.get(), r, signatures, fields, topLevelContext, visited, until);
            } else {
                return r;
            }
        }
    }
}
