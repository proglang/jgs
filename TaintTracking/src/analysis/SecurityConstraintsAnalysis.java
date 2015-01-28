package analysis;

import static constraints.ConstraintsUtils.constraintsAsString;
import static constraints.ConstraintsUtils.isSubSignature;
import static resource.Messages.getMsg;
import static utils.AnalysisUtils.generateFileName;
import static utils.AnalysisUtils.getSignatureOfMethod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logging.AnalysisLog;
import security.ILevelMediator;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import utils.Debugger;
import utils.Debugger.Header;
import constraints.ConstraintsSet;
import constraints.ConstraintsUtils;
import constraints.IProgramCounterTrigger;
import constraints.LEQConstraint;
import error.ISubSignatureError;
import exception.AnalysisException;
import exception.CastInvalidException;
import exception.EnvironmentNotFoundException;
import exception.IllegalNewArrayException;
import exception.LevelNotFoundException;
import exception.MethodParameterNotFoundException;
import exception.ProgramCounterException;
import exception.SwitchException;
import extractor.UsedObjectStore;

/**
 * <h1>TaintTracking analysis</h1>
 * 
 * The {@link SecurityConstraintsAnalysis} is the security analysis and extends
 * the {@link ForwardFlowAnalysis}. The class allows the analysis of the flow of
 * a single analyzed method in order to detect security violations. Note, the
 * call of the constructor will analyze violations of the
 * <em>security level</em> and calculates the <em>write effects</em> of the
 * analyzed method. In order to check those calculated effects the method
 * {@link SecurityConstraintsAnalysis#checkAnalysis()} has to be called.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see SecurityLevelStmtSwitch
 */
public class SecurityConstraintsAnalysis extends
        ASecurityAnalysis<Unit, ConstraintsSet> {

    private boolean consistent = false;

    /**
     * Constructor of {@link SecurityConstraintsAnalysis} which checks
     * automatically the given graph of the also given method for security
     * violations, i.e. violations of <em>security levels</em> or violations of
     * <em>write effects</em>. The constructor requires in addition a logger
     * that allows the logging of exceptions and violations, and a security
     * annotation instance that allows the handling of <em>security levels</em>
     * for this method. Note, that the method
     * {@link SecurityConstraintsAnalysis#checkAnalysis()} has to be called for
     * the final check of the <em>write effects</em>.
     * 
     * @param log
     *            The {@link AnalysisLog} which provides the logging of
     *            exceptions, errors, violations, etc.
     * @param sootMethod
     *            The method which should be analyzed.
     * @param mediator
     *            A {@link LevelMediator} in order to provide the handling of
     *            <em>security levels</em>.
     * @param graph
     *            The generated graph for the given method.
     * @param store
     */
    protected SecurityConstraintsAnalysis(AnalysisLog log,
            SootMethod sootMethod, ILevelMediator mediator,
            DirectedGraph<Unit> graph, UsedObjectStore store) {
        super(log, sootMethod, mediator, graph, store);
    }

    /**
     * Checks, whether the calculated <em>write effects</em> of the analyzed
     * method match the expected effects which are given by the
     * <em>write effect</em> annotation. Note, that this method should be called
     * after the analysis of each method.
     */
    protected void checkAnalysis() {
        if (getAnalyzedEnvironment() != null)
            getAnalyzedEnvironment().checkEffectAnnotations();
    }

    /**
     * Creates a copy of the <code>source</code> flow object, i.e. the locals
     * map, in <code>dest</code>.
     * 
     * @param source
     *            The locals map that should be copied in the given outgoing
     *            map.
     * @param dest
     *            The locals map in which the state of the first given locals
     *            map should be copied.
     * @see soot.toolkits.scalar.AbstractFlowAnalysis#copy(java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    protected void copy(ConstraintsSet source, ConstraintsSet dest) {
        dest.clear();
        dest.addAll(source.getConstraintsSet());
        dest.addAllProgramCounterConstraints(source.getProgramCounter());
        dest.addAllWriteEffects(source.getWriteEffects());
    }

    /**
     * Returns the initial flow value for entry/exit graph nodes.
     * 
     * @return The initial flow object for entry/exit graph nodes.
     * @see soot.toolkits.scalar.AbstractFlowAnalysis#entryInitialFlow()
     */
    @Override
    protected ConstraintsSet entryInitialFlow() {
        return new ConstraintsSet(getMediator());
    }

    /**
     * Given the merge of the <code>out</code> sets, compute the <code>in</code>
     * set for <code>d</code>.
     * 
     * Processes the analysis for the given {@link Unit}, i.e. checks the end of
     * an implicit flow for the given unit and after that tries to apply a
     * {@link SecurityLevelStmtSwitch} switch to the statement, i.e. calculates
     * or updates the <em>security levels</em> of the statement components to
     * check for security violations.
     * 
     * @param in
     *            Current incoming map of the local variables for the given
     *            unit.
     * @param d
     *            The current unit which should be checked for security
     *            violations.
     * @param out
     *            Current outgoing map of the local variables for the given
     *            unit.
     * @see soot.toolkits.scalar.FlowAnalysis#flowThrough(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     * @see SecurityLevelStmtSwitch
     */
    @Override
    protected void flowThrough(ConstraintsSet in, Unit d, ConstraintsSet out) {
        copy(in, out);
        Stmt stmt = (Stmt) d;
        getAnalyzedEnvironment().setStmt(stmt);
        checkEndOfImplicitFlow(stmt, in, out);
        try {
            SecurityConstraintStmtSwitch stmtSwitch =
                getStmtSwitch(in, out, stmt);
            consistencyCheck(out, in, stmtSwitch, stmt);
            if (stmtSwitch.isReturnStmt())
                completnessCheck(out);
        } catch (ProgramCounterException | EnvironmentNotFoundException
                | SwitchException | MethodParameterNotFoundException
                | LevelNotFoundException | CastInvalidException
                | IllegalNewArrayException e) {
            Debugger.show(new Header("Boundaries before the exceptional statement: "
                                             + getAnalyzedEnvironment().getStmt()
                                                                       .toString(),
                                     "at line "
                                             + getAnalyzedEnvironment().getSrcLn()),
                          in.toBoundaryStrings());
            Debugger.show(new Header("Boundaries after the exceptional statement: "
                                             + getAnalyzedEnvironment().getStmt()
                                                                       .toString(),
                                     "at line "
                                             + getAnalyzedEnvironment().getSrcLn()),
                          out.toBoundaryStrings(),
                          new AnalysisException(getMsg("exception.analysis.other.error_switch",
                                                       stmt.toString(),
                                                       getSignatureOfMethod(getAnalyzedEnvironment().getSootMethod()),
                                                       getAnalyzedEnvironment().getSrcLn()),
                                                e));
        }
    }

    private SecurityConstraintStmtSwitch getStmtSwitch(ConstraintsSet in,
                                                       ConstraintsSet out,
                                                       Stmt stmt) {
        SecurityConstraintStmtSwitch stmtSwitch =
            new SecurityConstraintStmtSwitch(getAnalyzedEnvironment(),
                                             getStore(),
                                             in,
                                             out);
        stmt.apply(stmtSwitch);
        return stmtSwitch;
    }

    private void completnessCheck(ConstraintsSet out) {
        ConstraintsSet constraints =
            new ConstraintsSet(out.getConstraintsSet(),
                               out.getProgramCounter(),
                               out.getWriteEffects(),
                               getMediator());
        constraints.addAll(out.getAllProgramCounterConstraints());
        constraints.removeConstraintsContainingLocal();
        constraints.addAll(out.getWriteEffects());
        ConstraintsSet signature =
            new ConstraintsSet(getAnalyzedEnvironment().getSignatureContraints(),
                               getMediator());
        signature.calculateTransitiveClosure();
        List<ISubSignatureError> errors =
            isSubSignature(signature.getConstraintsSet(),
                           getAnalyzedEnvironment().getSootMethod()
                                                   .getSignature(),
                           constraints.getConstraintsSet());
        if (errors.size() != 0) {
            Set<LEQConstraint> missingConstraints =
                extractConstraintsFrom(errors);
            getLog().security(generateFileName(getAnalyzedEnvironment().getSootMethod()),
                              getAnalyzedEnvironment().getSrcLn(),
                              getMsg("security.constraints.missing",
                                     getSignatureOfMethod(getAnalyzedEnvironment().getSootMethod()),
                                     constraintsAsString(missingConstraints)));
        }
    }

    private Set<LEQConstraint> extractConstraintsFrom(List<ISubSignatureError> errors) {
        Set<LEQConstraint> constraints = new HashSet<LEQConstraint>();
        for (ISubSignatureError error : errors) {
            constraints.add(error.getConstraint());
        }
        return constraints;
    }

    private void consistencyCheck(ConstraintsSet out,
                                  ConstraintsSet in,
                                  SecurityConstraintStmtSwitch stmtSwitch,
                                  Stmt stmt) {
        out.addAll(out.getAllProgramCounterConstraints());
        out.removeConstraintsContainingProgramCounter();
        if (!consistent) {
            ConstraintsSet constraints =
                new ConstraintsSet(out.getConstraintsSet(),
                                   out.getProgramCounter(),
                                   out.getWriteEffects(),
                                   getMediator());
            constraints.addAll(getAnalyzedEnvironment().getSignatureContraints());
            constraints.calculateTransitiveClosure();
            constraints.addAll(out.getWriteEffects());
            Set<LEQConstraint> inconsistentConstraints =
                constraints.getInconsistent();
            if (inconsistentConstraints.size() != 0) {
                consistent = true;
                Debugger.show(new Header("Boundaries before the inconsistent state",
                                         "at line "
                                                 + getAnalyzedEnvironment().getSrcLn()),
                              in.toBoundaryStrings());
                Debugger.show(new Header("Constraints leading to the inconsistent state",
                                         "at line "
                                                 + getAnalyzedEnvironment().getSrcLn()),
                              ConstraintsUtils.constraintsAsStringArray(stmtSwitch.getRecentlyAdded()));
                getLog().security(generateFileName(getAnalyzedEnvironment().getSootMethod()),
                                  getAnalyzedEnvironment().getSrcLn(),
                                  getMsg("security.constraints.inequality",
                                         getAnalyzedEnvironment().getSrcLn(),
                                         getSignatureOfMethod(getAnalyzedEnvironment().getSootMethod()),
                                         stmt.toString(),
                                         constraintsAsString(inconsistentConstraints)));
            }
        }

    }

    private void checkEndOfImplicitFlow(Stmt statement,
                                        ConstraintsSet in,
                                        ConstraintsSet out) {
        for (IProgramCounterTrigger trigger : in.getProgramCounterTriggers()) {
            if (getContainer().postDomSetOfStmtContainsS(trigger.getStmt(),
                                                         statement)) {
                out.removeProgramCounterConstraintsFor(trigger);
            }
        }
    }

    /**
     * Compute the merge of the <code>in1</code> and <code>in2</code> locals
     * maps, putting the result into <code>out</code>. The behavior of this
     * function is that the strongest <em>security level</em> which exists for a
     * local variable in <code>in1</code> or <code>in2</code>, is taken for the
     * <code>out</code> locals map. Used by the
     * {@link SecurityConstraintsAnalysis#doAnalysis()} method.
     * 
     * @param in1
     *            First locals map that should be merged together with the
     *            second map.
     * @param in2
     *            Second locals map that should be merged together with the
     *            first map.
     * @param out
     *            Outgoing locals map that should store the result of the merge
     *            of <code>in1</code> and <code>in2</code>.
     * @see soot.toolkits.scalar.AbstractFlowAnalysis#merge(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    @Override
    protected void merge(ConstraintsSet in1,
                         ConstraintsSet in2,
                         ConstraintsSet out) {
        copy(in1, out);
        out.addAll(in2.getConstraintsSet());
        out.addAllProgramCounterConstraints(in2.getProgramCounter());
    }

    /**
     * Returns the flow object, i.e. a locals map, corresponding to the initial
     * values for each graph node.
     * 
     * @return The initial locals map.
     * @see soot.toolkits.scalar.AbstractFlowAnalysis#newInitialFlow()
     */
    @Override
    protected ConstraintsSet newInitialFlow() {
        return new ConstraintsSet(getMediator());
    }

    private ILevelMediator getMediator() {
        return getAnalyzedEnvironment().getLevelMediator();
    }

    private AnalysisLog getLog() {
        return getAnalyzedEnvironment().getLog();
    }

}