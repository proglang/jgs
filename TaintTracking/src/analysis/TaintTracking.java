package analysis;

import java.util.ArrayList;
import java.util.List;

import logging.SecurityLogger;
import model.AnalyzedMethodEnvironment;
import model.LocalsMap;
import pattern.StatementSwitch;
import security.SecurityAnnotation;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import utils.SecurityMessages;
import utils.SootUtils;
import exception.SootException.InvalidLevelException;
import exception.SootException.SwitchException;

/**
 * <h1>TaintTracking analysis</h1>
 * 
 * The {@link TaintTracking} is the security analysis and extends the {@link ForwardFlowAnalysis}.
 * The class allows the analysis of the flow of a single analyzed method in order to detect security
 * violations. Note, the call of the constructor will analyze violations of the
 * <em>security level</em> and calculates the <em>write effects</em> of the analyzed method. In
 * order to check those calculated effects the method {@link TaintTracking#checkAnalysis()} has to
 * be called.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see StatementSwitch
 */
public class TaintTracking extends ForwardFlowAnalysis<Unit, LocalsMap> {

	/**
	 * <h1>Wrapper for (post-) dominators</h1>
	 * 
	 * The {@link Dominator} class provides the post dominator as well as the dominator objects.
	 * These allow to distinguish the (post-) dominator set for a specific unit. The class provides
	 * a method a method that checks whether the post dominator set of a given if-statement contains
	 * the second given statement.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	protected static class Dominator {

		/** Dominator finder object. */
		private final MHGDominatorsFinder<Unit> dominatorFinder;
		/** Post dominator finder object. */
		private final MHGPostDominatorsFinder postDominatorFinder;

		/**
		 * Constructor of a {@link Dominator} which requires a graph in order to generated the
		 * (post-) dominator finder.
		 * 
		 * @param graph
		 *            The <code>DirectedGraph</code> of the analyzed method.
		 */
		public Dominator(DirectedGraph<Unit> graph) {
			this.dominatorFinder = new MHGDominatorsFinder<Unit>(graph);
			this.postDominatorFinder = new MHGPostDominatorsFinder(graph);
		}

		/**
		 * Method returns the dominator finder object.
		 * 
		 * @return The dominator finder.
		 */
		public MHGDominatorsFinder<Unit> getDominatorFinder() {
			return dominatorFinder;
		}

		/**
		 * Method returns the post-dominator finder object.
		 * 
		 * @return The post-dominator finder.
		 */
		public MHGPostDominatorsFinder getPostDominatorFinder() {
			return postDominatorFinder;
		}

		/**
		 * Checks whether the post-dominator set of the given if-statement contains the given
		 * statement. I.e. the given statement is a post-dominator of the if-statement.
		 * 
		 * @param ifStmt
		 *            if-statement whose post-dominator set is generated.
		 * @param s
		 *            statement for which will be checked whether it is contained by the
		 *            post-dominator set.
		 * @return {@code true} if the post-dominator set of the given if-statement contains the
		 *         other given statement, otherwise {@code false}.
		 */
		public boolean postDomSetOfIfContainsS(IfStmt ifStmt, Stmt s) {
			return postDominatorFinder.getDominators(ifStmt).contains(s);
		}

	}

	/** The environment of the current analyzed method. */
	private final AnalyzedMethodEnvironment analyzedMethodEnvironment;
	/**
	 * The dominator container, which provides the post dominator and the dominator set for a
	 * specific unit.
	 */
	private final Dominator container;

	/**
	 * Constructor of {@link TaintTracking} which checks automatically the given graph of the also
	 * given method for security violations, i.e. violations of <em>security levels</em> or
	 * violations of <em>write effects</em>. The constructor requires in addition a logger that
	 * allows the logging of exceptions and violations, and a security annotation instance that
	 * allows the handling of <em>security levels</em> for this method. Note, that the method
	 * {@link TaintTracking#checkAnalysis()} has to be called for the final check of the
	 * <em>write effects</em>.
	 * 
	 * @param log
	 *            The {@link SecurityLogger} which provides the logging of exceptions, errors,
	 *            violations, etc.
	 * @param sootMethod
	 *            The method which should be analyzed.
	 * @param securityAnnotations
	 *            A {@link SecurityAnnotation} in order to provide the handling of
	 *            <em>security levels</em>.
	 * @param graph
	 *            The generated graph for the given method.
	 */
	public TaintTracking(SecurityLogger log, SootMethod sootMethod,
			SecurityAnnotation securityAnnotations, DirectedGraph<Unit> graph) {
		super(graph);
		container = new Dominator(graph);
		analyzedMethodEnvironment = new AnalyzedMethodEnvironment(sootMethod, log,
				securityAnnotations);
		long srcLn = 0;
		String fileName = SootUtils.generateFileName(sootMethod);
		String methodSignature = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		if (analyzedMethodEnvironment.areMethodParameterSecuritiesValid()) {
			if (analyzedMethodEnvironment.isReturnSecurityValid()) {
				doAnalysis();
			} else {
				analyzedMethodEnvironment.getLog().error(fileName, srcLn,
						SecurityMessages.interruptInvalidReturn(methodSignature));
			}
		} else {
			analyzedMethodEnvironment.getLog().error(fileName, srcLn,
					SecurityMessages.interruptInvalidParameters(methodSignature));
		}
	}

	/**
	 * Checks, whether the calculated <em>write effects</em> of the analyzed method match the
	 * expected effects which are given by the <em>write effect</em> annotation. Note, that this
	 * method should be called after the analysis of each method.
	 */
	public void checkAnalysis() {
		analyzedMethodEnvironment.checkEffectAnnotations();
	}

	/**
	 * Given the merge of the <code>out</code> sets, compute the <code>in</code> set for
	 * <code>d</code>.
	 * 
	 * Processes the analysis for the given {@link Unit}, i.e. checks the end of an implicit flow
	 * for the given unit and after that tries to apply a {@link StatementSwitch} switch to the
	 * statement, i.e. calculates or updates the <em>security levels</em> of the statement
	 * components to check for security violations.
	 * 
	 * @param in
	 *            Current incoming map of the local variables for the given unit.
	 * @param d
	 *            The current unit which should be checked for security violations.
	 * @param out
	 *            Current outgoing map of the local variables for the given unit.
	 * @see soot.toolkits.scalar.FlowAnalysis#flowThrough(java.lang.Object, java.lang.Object,
	 *      java.lang.Object)
	 * @see StatementSwitch
	 */
	@Override
	public void flowThrough(LocalsMap in, Unit d, LocalsMap out) {
		copy(in, out);
		Stmt stmt = (Stmt) d;
		analyzedMethodEnvironment.setStmt(stmt);
		checkEndOfImplicitFlow(stmt, in, out);
		StatementSwitch stmtSwitch = new StatementSwitch(analyzedMethodEnvironment, in, out);
		try {
			stmt.apply(stmtSwitch);
		} catch (SwitchException e) {
			String fileName = SootUtils.generateFileName(analyzedMethodEnvironment.getSootMethod());
			String methodSignature = SootUtils.generateMethodSignature(
					analyzedMethodEnvironment.getSootMethod(), false, true, true);
			long srcLn = analyzedMethodEnvironment.getSrcLn();
			analyzedMethodEnvironment.getLog().exception(fileName, srcLn,
					SecurityMessages.catchSwitchException(methodSignature, srcLn, stmt.toString()),
					e);
		}
	}

	/**
	 * Checks whether the given statement is the end of an implicit flow. If this is the case, then
	 * the corresponding if statement which is the start of this implicit flow will be removed from
	 * the program counter in the outgoing locals map. Note, that the method will not change the
	 * incoming locals map.
	 * 
	 * @param statement
	 *            Statement for which should be checked whether it is the end of an implicit flow.
	 * @param in
	 *            Current incoming map of the local variables.
	 * @param out
	 *            Current outgoing map of the local variables.
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
	 * Creates a copy of the <code>source</code> flow object, i.e. the locals map, in
	 * <code>dest</code>.
	 * 
	 * @param source
	 *            The locals map that should be copied in the given outgoing map.
	 * @param dest
	 *            The locals map in which the state of the first given locals map should be copied.
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
		LocalsMap map = new LocalsMap(analyzedMethodEnvironment.getSootMethod().getActiveBody()
				.getLocals(), analyzedMethodEnvironment.getSecurityAnnotation());
		return map;
	}

	/**
	 * Compute the merge of the <code>in1</code> and <code>in2</code> locals maps, putting the
	 * result into <code>out</code>. The behavior of this function is that the strongest
	 * <em>security level</em> which exists for a local variable in <code>in1</code> or
	 * <code>in2</code>, is taken for the <code>out</code> locals map. Used by the
	 * {@link TaintTracking#doAnalysis()} method.
	 * 
	 * @param in1
	 *            First locals map that should be merged together with the second map.
	 * @param in2
	 *            Second locals map that should be merged together with the first map.
	 * @param out
	 *            Outgoing locals map that should store the result of the merge of <code>in1</code>
	 *            and <code>in2</code>.
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#merge(java.lang.Object, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected void merge(LocalsMap in1, LocalsMap in2, LocalsMap out) {
		copy(in1, out);
		try {
			out.addAllStronger(in2.getExtendedLocals());
		} catch (InvalidLevelException e) {
			String fileName = SootUtils.generateFileName(analyzedMethodEnvironment.getSootMethod());
			String methodSignature = SootUtils.generateMethodSignature(
					analyzedMethodEnvironment.getSootMethod(), false, true, true);
			long srcLn = analyzedMethodEnvironment.getSrcLn();
			analyzedMethodEnvironment.getLog().exception(fileName, srcLn,
					SecurityMessages.invalidLevelsComparisonInMap(methodSignature, srcLn), e);
		}
	}

	/**
	 * Returns the flow object, i.e. a locals map, corresponding to the initial values for each
	 * graph node.
	 * 
	 * @return The initial locals map.
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#newInitialFlow()
	 */
	@Override
	protected LocalsMap newInitialFlow() {
		LocalsMap map = new LocalsMap(analyzedMethodEnvironment.getSootMethod().getActiveBody()
				.getLocals(), analyzedMethodEnvironment.getSecurityAnnotation());
		return map;
	}

}