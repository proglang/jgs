package analysis;

import static resource.Messages.getMsg;

import interfaces.Cancelable;

import java.util.ArrayList;
import java.util.List;

import analysis.LocalsMap;

import logging.AnalysisLog;
import model.AnalyzedMethodEnvironment;
import model.MethodEnvironment;
import resource.Configuration;
import security.ILevelMediator;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import utils.AnalysisUtils;
import exception.SootException.InvalidLevelException;
import exception.SootException.SwitchException;
import exception.SootException.NoSuchElementException;
import extractor.UsedObjectStore;

/**
 * <h1>TaintTracking analysis</h1>
 * 
 * The {@link SecurityTypeAnalysis} is the security analysis and extends the {@link ForwardFlowAnalysis}. The class allows the analysis of
 * the flow of a single analyzed method in order to detect security violations. Note, the call of the constructor will analyze violations of
 * the <em>security level</em> and calculates the <em>write effects</em> of the analyzed method. In order to check those calculated effects
 * the method {@link SecurityTypeAnalysis#checkAnalysis()} has to be called.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see SecurityTypeStmtSwitch
 */
public class SecurityTypeAnalysis extends ForwardFlowAnalysis<Unit, LocalsMap> implements Cancelable {

	/** The environment of the current analyzed method. */
	private final AnalyzedMethodEnvironment analyzedMethodEnvironment;
	/**
	 * The dominator container, which provides the post dominator and the dominator set for a specific unit.
	 */
	private final Dominator container;
	/**
	 * DOC
	 */
	private final List<Cancelable> listeners = new ArrayList<Cancelable>();
	/**
	 * DOC
	 */
	private final UsedObjectStore store;
	/**
	 * DOC
	 */
	private boolean valid = true;

	/**
	 * Constructor of {@link SecurityTypeAnalysis} which checks automatically the given graph of the also given method for security
	 * violations, i.e. violations of <em>security levels</em> or violations of <em>write effects</em>. The constructor requires in addition a
	 * logger that allows the logging of exceptions and violations, and a security annotation instance that allows the handling of
	 * <em>security levels</em> for this method. Note, that the method {@link SecurityTypeAnalysis#checkAnalysis()} has to be called for the
	 * final check of the <em>write effects</em>.
	 * 
	 * @param log
	 *          The {@link AnalysisLog} which provides the logging of exceptions, errors, violations, etc.
	 * @param sootMethod
	 *          The method which should be analyzed.
	 * @param mediator
	 *          A {@link LevelMediator} in order to provide the handling of <em>security levels</em>.
	 * @param graph
	 *          The generated graph for the given method.
	 * @param store
	 */
	protected SecurityTypeAnalysis(AnalysisLog log, SootMethod sootMethod, ILevelMediator mediator, DirectedGraph<Unit> graph,
			UsedObjectStore store, Cancelable cancelable) {
		super(graph);
		addCancelListener(cancelable);
		this.container = new Dominator(graph);
		this.store = store;
		AnalyzedMethodEnvironment ame = null;
		try {
			MethodEnvironment me = this.store.getMethodEnvironment(sootMethod.retrieveActiveBody().getMethod());
			ame = new AnalyzedMethodEnvironment(me);
		} catch (NoSuchElementException e) {
			exception(getMsg("env_store.not_found_method", AnalysisUtils.generateMethodSignature(sootMethod,
					Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
					Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)), e);
			cancel();
		}
		analyzedMethodEnvironment = ame;
		if (valid) {
			doAnalysis();
		} else {
			error(getMsg("analysis.fail.creation_analyzed_method", AnalysisUtils.generateMethodSignature(sootMethod,
					Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
					Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)));
		}
	}

	/**
	 * DOC
	 * 
	 * @param cancelable
	 * @see interfaces.Cancelable#addCancelListener(interfaces.Cancelable)
	 */
	@Override
	public void addCancelListener(Cancelable cancelable) {
		this.listeners.add(cancelable);

	}

	/**
	 * DOC
	 */
	@Override
	public void cancel() {
		for (Cancelable cancelable : listeners) {
			cancelable.cancel();
		}
		this.valid = false;
	}

	/**
	 * DOC
	 * 
	 * @param cancelable
	 * @see interfaces.Cancelable#removeCancelListener(Cancelable)
	 */
	@Override
	public void removeCancelListener(Cancelable cancelable) {
		this.listeners.remove(cancelable);

	}

	/**
	 * Checks whether the given statement is the end of an implicit flow. If this is the case, then the corresponding if statement which is
	 * the start of this implicit flow will be removed from the program counter in the outgoing locals map. Note, that the method will not
	 * change the incoming locals map.
	 * 
	 * @param statement
	 *          Statement for which should be checked whether it is the end of an implicit flow.
	 * @param in
	 *          Current incoming map of the local variables.
	 * @param out
	 *          Current outgoing map of the local variables.
	 */
	private void checkEndOfImplicitFlow(Stmt statement, LocalsMap in, LocalsMap out) {
		List<IfStmt> ifStmts = new ArrayList<IfStmt>(in.getProgramCounter().keySet());
		for (IfStmt ifStmt : ifStmts) {
			if (container.postDomSetOfIfContainsS(ifStmt, statement)) {
				out.removeProgramCounterLevel(ifStmt);
			}
		}
	}

	/**
	 * DOC
	 * 
	 * @param msg
	 */
	private void error(String msg) {
		String fileName = AnalysisUtils.generateFileName(analyzedMethodEnvironment.getSootMethod());
		long srcLn = analyzedMethodEnvironment.getSrcLn();
		analyzedMethodEnvironment.getLog().error(fileName, srcLn, msg);
	}

	/**
	 * DOC
	 * 
	 * @param msg
	 * @param e
	 */
	private void exception(String msg, Throwable e) {
		String fileName = AnalysisUtils.generateFileName(analyzedMethodEnvironment.getSootMethod());
		long srcLn = analyzedMethodEnvironment.getSrcLn();
		analyzedMethodEnvironment.getLog().exception(fileName, srcLn, msg, e);
	}

	/**
	 * Checks, whether the calculated <em>write effects</em> of the analyzed method match the expected effects which are given by the
	 * <em>write effect</em> annotation. Note, that this method should be called after the analysis of each method.
	 */
	protected void checkAnalysis() {
		if (analyzedMethodEnvironment != null) analyzedMethodEnvironment.checkEffectAnnotations();
	}

	/**
	 * Creates a copy of the <code>source</code> flow object, i.e. the locals map, in <code>dest</code>.
	 * 
	 * @param source
	 *          The locals map that should be copied in the given outgoing map.
	 * @param dest
	 *          The locals map in which the state of the first given locals map should be copied.
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#copy(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void copy(LocalsMap source, LocalsMap dest) {
		dest.clear();
		dest.addAll(source.getExtendedLocals(), source.getProgramCounter());
	}

	/**
	 * Returns the initial flow value for entry/exit graph nodes.
	 * 
	 * @return The initial flow object for entry/exit graph nodes.
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#entryInitialFlow()
	 */
	@Override
	protected LocalsMap entryInitialFlow() {
		LocalsMap map = new LocalsMap(analyzedMethodEnvironment.getSootMethod().getActiveBody().getLocals(),
				analyzedMethodEnvironment.getLevelMediator());
		return map;
	}

	/**
	 * Given the merge of the <code>out</code> sets, compute the <code>in</code> set for <code>d</code>.
	 * 
	 * Processes the analysis for the given {@link Unit}, i.e. checks the end of an implicit flow for the given unit and after that tries to
	 * apply a {@link SecurityTypeStmtSwitch} switch to the statement, i.e. calculates or updates the <em>security levels</em> of the
	 * statement components to check for security violations.
	 * 
	 * @param in
	 *          Current incoming map of the local variables for the given unit.
	 * @param d
	 *          The current unit which should be checked for security violations.
	 * @param out
	 *          Current outgoing map of the local variables for the given unit.
	 * @see soot.toolkits.scalar.FlowAnalysis#flowThrough(java.lang.Object, java.lang.Object, java.lang.Object)
	 * @see SecurityTypeStmtSwitch
	 */
	@Override
	protected void flowThrough(LocalsMap in, Unit d, LocalsMap out) {
		if (valid) {
			copy(in, out);
			Stmt stmt = (Stmt) d;
			analyzedMethodEnvironment.setStmt(stmt);
			checkEndOfImplicitFlow(stmt, in, out);
			SecurityTypeStmtSwitch stmtSwitch = new SecurityTypeStmtSwitch(analyzedMethodEnvironment, store, in, out);
			try {
				stmt.apply(stmtSwitch);
			} catch (SwitchException e) {
				exception(getMsg("analysis.fail.invalid_switch_argument", stmt.toString(), AnalysisUtils.generateMethodSignature(
						analyzedMethodEnvironment.getSootMethod(), Configuration.METHOD_SIGNATURE_PRINT_PACKAGE,
						Configuration.METHOD_SIGNATURE_PRINT_TYPE, Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)), e);
			}
		}
	}

	/**
	 * Compute the merge of the <code>in1</code> and <code>in2</code> locals maps, putting the result into <code>out</code>. The behavior of
	 * this function is that the strongest <em>security level</em> which exists for a local variable in <code>in1</code> or <code>in2</code>,
	 * is taken for the <code>out</code> locals map. Used by the {@link SecurityTypeAnalysis#doAnalysis()} method.
	 * 
	 * @param in1
	 *          First locals map that should be merged together with the second map.
	 * @param in2
	 *          Second locals map that should be merged together with the first map.
	 * @param out
	 *          Outgoing locals map that should store the result of the merge of <code>in1</code> and <code>in2</code>.
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#merge(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void merge(LocalsMap in1, LocalsMap in2, LocalsMap out) {
		copy(in1, out);
		try {
			out.addAllStronger(in2.getExtendedLocals());
		} catch (InvalidLevelException e) {
			exception(getMsg("pc.invalid_comparison", AnalysisUtils.generateMethodSignature(analyzedMethodEnvironment.getSootMethod(),
					Configuration.METHOD_SIGNATURE_PRINT_PACKAGE, Configuration.METHOD_SIGNATURE_PRINT_TYPE,
					Configuration.METHOD_SIGNATURE_PRINT_VISIBILITY)), e);
		}
	}

	/**
	 * Returns the flow object, i.e. a locals map, corresponding to the initial values for each graph node.
	 * 
	 * @return The initial locals map.
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#newInitialFlow()
	 */
	@Override
	protected LocalsMap newInitialFlow() {
		LocalsMap map = new LocalsMap(analyzedMethodEnvironment.getSootMethod().getActiveBody().getLocals(),
				analyzedMethodEnvironment.getLevelMediator());
		return map;
	}

}