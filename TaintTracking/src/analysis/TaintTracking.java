package analysis;

import java.util.Set;

import pattern.StatementSwitch;

import logging.SecurityLogger;
import model.LocalMap;
import model.MethodAnalysisEnvironment;
import model.SecurityMethod;

import security.SecurityAnnotation;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import utils.SecurityMessages;
import utils.SootUtils;

/**
 * This analysis tracks the high-security variables.
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class TaintTracking extends ForwardFlowAnalysis<Unit, LocalMap> {
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 *
	 */
	public static class DominatorContainer {
		
		/** */
		private final MHGDominatorsFinder<Unit> dominatorFinder;
		/** */
		private final MHGPostDominatorsFinder postDominatorFinder;
		
		/**
		 * 
		 * @param graph
		 */
		public DominatorContainer(DirectedGraph<Unit> graph) {
			this.dominatorFinder = new MHGDominatorsFinder<Unit>(graph);
			this.postDominatorFinder = new MHGPostDominatorsFinder(graph);
		}
		
		/**
		 * 
		 * @return
		 */
		public MHGDominatorsFinder<Unit> getDominatorFinder() {
			return dominatorFinder;
		}

		/**
		 * 
		 * @return
		 */
		public MHGPostDominatorsFinder getPostDominatorFinder() {
			return postDominatorFinder;
		}
		
		/**
		 * 
		 * @param ifStmt
		 * @param s
		 * @return
		 */
		public boolean postDomSetOfIfContainsS(IfStmt ifStmt, Stmt s) {
			return postDominatorFinder.getDominators(ifStmt).contains(s);
		}
		
		/**
		 * 
		 * @param s
		 * @param ifStmt
		 * @return
		 */
		public boolean domSetOfSContainsIf(Stmt s, IfStmt ifStmt) {
			return dominatorFinder.getDominators(s).contains(ifStmt);
		}
		
	}

	/** */
	private final MethodAnalysisEnvironment methodAnalysisEnvironment;
	/** */
	private final DominatorContainer container;

	/**
	 * 
	 * @param log
	 * @param sootMethod
	 * @param securityAnnotations
	 * @param graph
	 */	
	public TaintTracking(SecurityLogger log, SootMethod sootMethod, SecurityAnnotation securityAnnotations, DirectedGraph<Unit> graph) {
		super(graph);
		container = new DominatorContainer(graph);
		methodAnalysisEnvironment  = new MethodAnalysisEnvironment(log, new SecurityMethod(sootMethod, true, securityAnnotations), securityAnnotations);
		long sourceLine = 0;
		if (methodAnalysisEnvironment.getSecurityMethod().areMethodParameterSecuritiesValid(log)) {
			if (methodAnalysisEnvironment.getSecurityMethod().isReturnSecurityValid(log)) {
				doAnalysis();
			} else {
				methodAnalysisEnvironment.getLog().error(SootUtils.generateFileName(sootMethod), sourceLine, SecurityMessages.interruptInvalidReturn(SootUtils.generateMethodSignature(sootMethod)));
			}
		} else {
			methodAnalysisEnvironment.getLog().error(SootUtils.generateFileName(sootMethod), sourceLine, SecurityMessages.interruptInvalidParameters(SootUtils.generateMethodSignature(sootMethod)));
		}
	}

	/**
	 * 
	 * 
	 * @param in
	 * @param d
	 * @param out
	 * @see soot.toolkits.scalar.FlowAnalysis#flowThrough(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void flowThrough(LocalMap in, Unit d, LocalMap out) {
		copy(in, out);
		Stmt stmt = (Stmt) d;
		methodAnalysisEnvironment.setStmt(stmt);
		checkEndOfImplicitFlow(stmt, in, out);
		StatementSwitch stmtSwitch = new StatementSwitch(in, out, this.methodAnalysisEnvironment);
		try {
			stmt.apply(stmtSwitch);
		} catch (Exception e) {
			// TODO: Throw Exception
			System.err.println("Exception --> Stmt");
		}
	}

	/**
	 * 
	 * 
	 * @return
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#newInitialFlow()
	 */
	@Override
	protected LocalMap newInitialFlow() {
		LocalMap map = new LocalMap(methodAnalysisEnvironment.getSootMethod().getActiveBody().getLocals(), methodAnalysisEnvironment.getSecurityAnnotation());
		return map;
	}

	/**
	 * 
	 * 
	 * @return
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#entryInitialFlow()
	 */
	@Override
	protected LocalMap entryInitialFlow() {
		LocalMap map = new LocalMap(methodAnalysisEnvironment.getSootMethod().getActiveBody().getLocals(), methodAnalysisEnvironment.getSecurityAnnotation());
		return map;
	}

	/**
	 * 
	 * 
	 * @param in1
	 * @param in2
	 * @param out
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#merge(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void merge(LocalMap in1, LocalMap in2, LocalMap out) {
		copy(in1, out);
		out.addAllStronger(in2.getExtendedLocals());
	}

	/**
	 * 
	 * 
	 * @param source
	 * @param dest
	 * @see soot.toolkits.scalar.AbstractFlowAnalysis#copy(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void copy(LocalMap source, LocalMap dest) {
		dest.clear();
		dest.addAll(source.getExtendedLocals(), source.getProgramCounter());
	}
	
	/**
	 * 
	 * @param statement
	 * @param in
	 * @param out
	 */
	private void checkEndOfImplicitFlow(Stmt statement, LocalMap in, LocalMap out) {
		Set<IfStmt> ifStmts = in.getProgramCounter().keySet();
		for (IfStmt ifStmt : ifStmts) {
			if (container.postDomSetOfIfContainsS(ifStmt, statement)) {
				if (container.domSetOfSContainsIf(statement, ifStmt)) {
					if (! (statement instanceof NopStmt)) {
						out.removeProgramCounterLevel(ifStmt);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void checkAnalysis() {
		
	}
}
