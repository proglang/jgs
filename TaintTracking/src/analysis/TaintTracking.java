package analysis;

import java.util.*;

import exception.SootException.*;

import pattern.*;
import logging.*;
import model.*;

import security.SecurityAnnotation;
import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.*;
import soot.toolkits.scalar.*;
import utils.*;

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
	private final AnalyzedMethodEnvironment analyzedMethodEnvironment;
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
		analyzedMethodEnvironment = new AnalyzedMethodEnvironment(sootMethod, log, securityAnnotations);
		long srcLn = 0;
		String fileName = SootUtils.generateFileName(sootMethod);
		String methodSignature = SootUtils.generateMethodSignature(sootMethod, false, true, true);
		if (analyzedMethodEnvironment.areMethodParameterSecuritiesValid()) {
			if (analyzedMethodEnvironment.isReturnSecurityValid()) {
				doAnalysis();
			} else {
				analyzedMethodEnvironment.getLog().error(fileName, srcLn, SecurityMessages.interruptInvalidReturn(methodSignature));
			}
		} else {
			analyzedMethodEnvironment.getLog().error(fileName, srcLn, SecurityMessages.interruptInvalidParameters(methodSignature));
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
		analyzedMethodEnvironment.setStmt(stmt);
		checkEndOfImplicitFlow(stmt, in, out);
		StatementSwitch stmtSwitch = new StatementSwitch(analyzedMethodEnvironment, in, out);
		try {
			stmt.apply(stmtSwitch);
		} catch (SwitchException e) {
			String fileName = SootUtils.generateFileName(analyzedMethodEnvironment.getSootMethod());
			String methodSignature = SootUtils.generateMethodSignature(analyzedMethodEnvironment.getSootMethod(), false, true, true);
			long srcLn = analyzedMethodEnvironment.getSrcLn();
			analyzedMethodEnvironment.getLog().exception(fileName, srcLn, SecurityMessages.catchSwitchException(methodSignature, srcLn, stmt.toString()), e);
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
		LocalMap map = new LocalMap(analyzedMethodEnvironment.getSootMethod().getActiveBody().getLocals(), analyzedMethodEnvironment.getSecurityAnnotation());
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
		LocalMap map = new LocalMap(analyzedMethodEnvironment.getSootMethod().getActiveBody().getLocals(), analyzedMethodEnvironment.getSecurityAnnotation());
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
		try {
			out.addAllStronger(in2.getExtendedLocals());
		} catch (InvalidLevelException e) {
			String fileName = SootUtils.generateFileName(analyzedMethodEnvironment.getSootMethod());
			String methodSignature = SootUtils.generateMethodSignature(analyzedMethodEnvironment.getSootMethod(), false, true, true);
			long srcLn = analyzedMethodEnvironment.getSrcLn();
			analyzedMethodEnvironment.getLog().exception(fileName, srcLn, SecurityMessages.invalidLevelsComparisonInMap(methodSignature, srcLn), e);
		}
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
		analyzedMethodEnvironment.checkEffectAnnotations();
	}

}