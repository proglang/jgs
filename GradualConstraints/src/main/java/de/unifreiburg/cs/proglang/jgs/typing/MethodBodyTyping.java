package de.unifreiburg.cs.proglang.jgs.typing;

import de.unifreiburg.cs.proglang.jgs.constraints.*;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.*;
import de.unifreiburg.cs.proglang.jgs.signatures.Effects;
import de.unifreiburg.cs.proglang.jgs.signatures.FieldTable;
import de.unifreiburg.cs.proglang.jgs.signatures.SignatureTable;
import de.unifreiburg.cs.proglang.jgs.util.NotImplemented;
import org.apache.commons.lang3.tuple.Pair;
import scala.Option;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.SimpleLiveLocals;
import soot.toolkits.scalar.SmartLocalDefs;

import java.util.*;

import static de.unifreiburg.cs.proglang.jgs.constraints.CTypes.literal;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.MethodTypeVars;
import static de.unifreiburg.cs.proglang.jgs.constraints.TypeVars.TypeVar;
import static de.unifreiburg.cs.proglang.jgs.typing.BodyTypingResult.addConstraints;
import static de.unifreiburg.cs.proglang.jgs.typing.BodyTypingResult.trivialCase;
import static scala.collection.JavaConverters.seqAsJavaListConverter;

/**
 * Context for generating typing constraints and environments for whole method bodies.
 * <p>
 * Created by fennell on 11/15/15.
 */
public class MethodBodyTyping<Level> {

    final private SootMethod method;
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
    public MethodBodyTyping(SootMethod method, TypeVars tvars, ConstraintSetFactory<Level> csets, Constraints<Level> cstrs, Casts<Level> casts, SignatureTable<Level> signatures, FieldTable<Level> fields) {
        this.method = method;
        this.csets = csets;
        this.casts = casts;
        this.cstrs = cstrs;
        this.tvars = tvars;
        this.signatures = signatures;
        this.fields = fields;
    }

    Set<Constraint<Level>> constraintsForBranches(Unit s,
                                                  final Environment env,
                                                  final TypeVar oldPc,
                                                  final TypeVar newPc) throws TypingException {
        AbstractStmtSwitch g = new AbstractStmtSwitch() {

            @Override
            public void defaultCase(Object obj) {
                throw new NotImplemented(String.format("Branching statement %s not supported yet.", obj));
            }

            @Override
            public void caseIfStmt(IfStmt stmt) {

                Set<Constraint<Level>> cs = new HashSet<>();

                // add new pc as upper bound to old pc
                cs.add(Constraints.<Level>le(CTypes.<Level>variable(oldPc), CTypes.<Level>variable(newPc)));

                for (Var<?> v : ((List<Var<?>>)seqAsJavaListConverter(Vars.getAllFromValueBoxes(stmt.getUseBoxes()).toSeq()).asJava())) {
                        cs.add(Constraints.le(CTypes.<Level>variable(env.get(v)), CTypes.<Level>variable(newPc)));
                }
                setResult(cs);
            }

        };
        s.apply(g);
        //noinspection unchecked
        return (Set<Constraint<Level>>) g.getResult();
    }


    public BodyTypingResult<Level> generateResult(Body b, TypeVar pc, Environment env) throws TypingException {
        UnitGraph g = new BriefUnitGraph(b);
        LocalDefs localDefs = new SmartLocalDefs(g, new SimpleLiveLocals(g));
        try {
            Assumptions.validUnitGraph(g);
        } catch (Assumptions.Violation e) {
            throw new TypingException(
                    "Unexpected unit graph: " + e.getMessage() + "\n body "
                    + b.toString() + "first elem" + b.getUnits().getFirst());
        }
        if (b.getUnits().isEmpty()) {
            throw new TypingException("Unexpected empty body:" + b.toString());
        }
        Stmt entry = (Stmt)b.getUnits().getFirst();
        return generateResult(entry, g, pc, env, localDefs);

    }

    private BodyTypingResult<Level> generateResult(Stmt s, DirectedGraph<Unit> g , TypeVar pc, Environment env, LocalDefs localDefs) throws TypingException {

        @SuppressWarnings("unchecked") DominatorsFinder<Unit> postdoms = new MHGPostDominatorsFinder(g);


        BodyTypingResult<Level> r = generateResult(tvars.forMethod(g), g, localDefs, postdoms, s, BodyTypingResult.fromEnv(csets, env), signatures, fields, pc, Collections.<Pair<TypeVar, Unit>>emptySet(), Option.<Unit>empty());

        return r;
    }


    private BodyTypingResult<Level> generateResult(MethodTypeVars sTvars,
                                                   DirectedGraph<Unit> g,
                                                   LocalDefs localDefs,
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
                                                   Option<Unit> until) throws TypingException {

        // when there is an "until" unit and we reached it, stop
        if (until.isDefined() && until.get().equals(s)) {
            return previous;
        }
        // otherwise calculate the result

        BodyTypingResult<Level> r;
        List<Unit> successors = g.getSuccsOf(s);

        // check for context casts a context casts
        Option<Casts.CxCast<Level>> maybeCxCast = casts.detectContextCastStartFromStmt(s);
        if (maybeCxCast.isDefined()) {
            //a context cast
            Casts.CxCast<Level> cxCast = maybeCxCast.get();

            if (successors.size() != 1) {
                throw new TypingAssertionFailure("Begin of context cast should have exatcly one successor " + s.toString());
            }
            ;
            Stmt startOfCast = (Stmt) successors.get(0); // there should be exactly this one

            // find the end unit
            Set<Unit> cxDoms = new HashSet<>();
            for (Unit s2 : postdoms.getDominators(s))  {
               if (casts.detectContextCastEndFromStmt((Stmt)s2)) {
                   cxDoms.add(s2);
               }
            }
            Unit endOfCast = null;
            for (Unit u : cxDoms) {
                if (postdoms.isDominatedByAll(u, cxDoms)) {
                    endOfCast = u;
                    break;
                }
            }
            if (endOfCast == null) {
                new TypingAssertionFailure("Could not find end of context casts: " + s.toString());
            }

            // Type variable for the new context
            TypeVar newPc = sTvars.forContext(s);
            r = generateResult(sTvars, g, localDefs, postdoms, startOfCast, previous, signatures, fields, newPc, visited, Option.apply(endOfCast));

            // add constraints: oldPc <= source, dest <= newPc, dest <= {effects}
            Constraint<Level> srcConstraint = Constraints.<Level>le(CTypes.<Level>variable(topLevelContext), literal(cxCast.sourceType));
            Constraint<Level> destConstraint = Constraints.<Level>le(literal(cxCast.destType), CTypes.<Level>variable(newPc));
            List<Constraint<Level>> additionalConstraintsList = new LinkedList<>(Arrays.asList(srcConstraint, destConstraint));
            for (TypeDomain.Type<Level> e : r.getEffects()) {
                additionalConstraintsList.add(Constraints.<Level>le(literal(cxCast.destType), literal(e)));
            }
            Map<Constraint<Level>, TypeVarTags.TypeVarTag> tagMap = new HashMap<>();
            for (Constraint<Level> c : additionalConstraintsList) {
                tagMap.put(c, new TypeVarTags.CxCast(new CastsFromMapping.Conversion<Level>(cxCast.sourceType, cxCast.destType)));
            }

            // modify effects: remove dest and add source
            Set<TypeDomain.Type<Level>> newEffects = new HashSet<>();
            newEffects.add(cxCast.sourceType);
            // TODO: why not a factory method?
            return new BodyTypingResult<Level>(r.getConstraints().add(csets.fromCollection(additionalConstraintsList)), Effects.makeEffects(newEffects), r.getFinalEnv(), r.getTags().addAll(TagMap.of(tagMap)));
        } else if (successors.size() <= 1) {
            // a basic (non-branching) unit in a straight-line sequence

            BasicStatementTyping<Level> bsTyping = new BasicStatementTyping<Level>(csets, sTvars, cstrs, method);
            BodyTypingResult<Level> atomic = bsTyping.generate(s, localDefs, previous.getFinalEnv(), Collections.singleton(topLevelContext), signatures, fields, casts);
            // TODO: the following two statements are so unclear because we need to include all the tags.. abstract away the sequencing to make this clearer (or more transparent)
            r = BodyTypingResult.addEffects(BodyTypingResult.addConstraints(atomic, previous.getConstraints()), previous.getEffects());
            r = new BodyTypingResult<>(r.getConstraints(), r.getEffects(), r.getFinalEnv(), r.getTags().addAll(previous.getTags()).addAll(atomic.getTags()));
            // if the unit is the end of a sequence, stop
            if (successors.isEmpty()) {
                return r;
            } else {
                // otherwise continue with the rest of the sequence
                Stmt next = (Stmt) successors.get(0);
                // TODO-performance: this is a tailcall
                return generateResult(sTvars, g, localDefs, postdoms, next, r, signatures, fields, topLevelContext, visited, until);
            }
        } else {
            // a branching statement. When the graph is checked with Assumptions.validUnitGraph then result should never be null
            Option<Unit> end = postdoms.getDominators(s).equals(Collections.singletonList(s)) ?
                    // if we are our only postdom, then there will be no join point, i.e. no point where we can lower pc again. This property is ensured ruling out that an if is an exit node (in Assumptions.validUnitGraph)
                    Option.<Unit>empty() :
                    Option.<Unit>apply(postdoms.getImmediateDominator(s));


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
                Set<Pair<TypeVar, Unit>> newVisited = new HashSet<>(visited);
                newVisited.add(Pair.of(newPc, uBranch));
                BodyTypingResult<Level> branchResult = generateResult(sTvars, g, localDefs, postdoms, (Stmt) uBranch, conditionResult, signatures, fields, newPc, newVisited, end);
                r = BodyTypingResult.join(r, branchResult, csets, sTvars, s.toString());
            }
            // continue after join point, if there is any
            if (end.isDefined()) {
                //TODO-performance: this is a tailcall
                return generateResult(sTvars, g, localDefs, postdoms, (Stmt) end.get(), r, signatures, fields, topLevelContext, visited, until);
            } else {
                return r;
            }
        }
    }
}
