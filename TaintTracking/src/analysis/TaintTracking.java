package analysis;

import java.util.Set;

import logging.SecurityLogger;
import model.LocalMap;
import model.MethodAnalysisEnvironment;
import model.SecurityMethod;

import security.SecurityAnnotation;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.SimpleDominatorsFinder;
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
	
	public static class DominatorContainer {
		
		private final MHGDominatorsFinder<Unit> dominatorFinder;
		private final MHGPostDominatorsFinder postDominatorFinder;
		private final SimpleDominatorsFinder simpleDominatorsFinder;
		
		public DominatorContainer(DirectedGraph<Unit> graph) {
			this.dominatorFinder = new MHGDominatorsFinder<Unit>(graph);
			this.postDominatorFinder = new MHGPostDominatorsFinder(graph);
			this.simpleDominatorsFinder = new SimpleDominatorsFinder(graph);
		}

		public MHGDominatorsFinder<Unit> getDominatorFinder() {
			return dominatorFinder;
		}

		public MHGPostDominatorsFinder getPostDominatorFinder() {
			return postDominatorFinder;
		}

		public SimpleDominatorsFinder getSimpleDominatorsFinder() {
			return simpleDominatorsFinder;
		}
		
	}

	/** */
	private final MethodAnalysisEnvironment methodAnalysisEnvironment;
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
		//in.prettyPrint(log, "flowThrough() in for " +  securityMethod.getSootMethod().getName());
		//out.prettyPrint(log, "flowThrough() out@begin for " +  securityMethod.getSootMethod().getName());
		checkEndOfImplicitFlow(stmt, in, out);
		StatementSwitch stmtSwitch = new StatementSwitch(in, out, this.methodAnalysisEnvironment);
		try {
			stmt.apply(stmtSwitch);
		} catch (Exception e) {
			System.err.println("Exception --> Stmt");
		}
		out.prettyPrint(methodAnalysisEnvironment.getLog(), "flowThrough() out@result for " +  methodAnalysisEnvironment.getSootMethod().getName());
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
		map.prettyPrint(methodAnalysisEnvironment.getLog(), "newIntialFlow() for " + methodAnalysisEnvironment.getSootMethod().getName());
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
		map.prettyPrint(methodAnalysisEnvironment.getLog(), "entryInitialFlow() for " +  methodAnalysisEnvironment.getSootMethod().getName());
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
		in1.prettyPrint(methodAnalysisEnvironment.getLog(), "merge() in1 for " +  methodAnalysisEnvironment.getSootMethod().getName());
		in2.prettyPrint(methodAnalysisEnvironment.getLog(), "merge() in2 for " +  methodAnalysisEnvironment.getSootMethod().getName());
		out.prettyPrint(methodAnalysisEnvironment.getLog(), "merge() out@begin for " +  methodAnalysisEnvironment.getSootMethod().getName());
		copy(in1, out);
		out.addAllStronger(in2.getExtendedLocals());
		// TODO out.removeLastProgramCounterLevel();
		out.prettyPrint(methodAnalysisEnvironment.getLog(), "merge() out@result for " +  methodAnalysisEnvironment.getSootMethod().getName());
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
		source.prettyPrint(methodAnalysisEnvironment.getLog(), "copy() source for " +  methodAnalysisEnvironment.getSootMethod().getName());
		dest.prettyPrint(methodAnalysisEnvironment.getLog(), "copy() dest@begin for " +  methodAnalysisEnvironment.getSootMethod().getName());
		dest.clear();
		dest.addAll(source.getExtendedLocals(), source.getProgramCounter());
		dest.prettyPrint(methodAnalysisEnvironment.getLog(), "copy() dest@result for " +  methodAnalysisEnvironment.getSootMethod().getName());
		
	}
	
	/**
	 * 
	 * @param statement
	 * @param out 
	 */
	private void checkEndOfImplicitFlow(Stmt statement, LocalMap in, LocalMap out) {
		
		Set<IfStmt> ifStmts = in.getProgramCounter().keySet();
		StringBuilder complete = new StringBuilder("");
		for (IfStmt ifStmt : ifStmts) {
			boolean sInPostDomIf = container.postDominatorFinder.getDominators(ifStmt).contains(statement);
			boolean ifInDomS = container.dominatorFinder.getDominators(statement).contains(ifStmt);
			/*complete.append("> If-statement '" + ifStmt.toString() + "' [ s in postDom(if) = " + String.valueOf(sInPostDomIf) + " && if in dom(s) = " + String.valueOf(ifInDomS) + "]:\n");
			int postCount = 0;
			StringBuilder mhgPostDom = new StringBuilder("MHG Post Dominator:\n");
			for (Object obj : container.postDominatorFinder.getDominators(statement)) {
				if (obj != null) {
					if (postCount++ != 0) mhgPostDom.append(", ");
					mhgPostDom.append(obj.toString());
				}
			}
			complete.append(mhgPostDom.toString() + "\n");
			StringBuilder mhgDom = new StringBuilder("MHG Dominator:\n");
			int count = 0;
			for (Unit unit : container.dominatorFinder.getDominators(statement)) {
				if (count++ != 0) mhgDom.append(", ");
				mhgDom.append(unit.toString());
			}
			complete.append(mhgDom.toString() + "\n");*/
			if (sInPostDomIf) {
				if (ifInDomS) {
					complete.append("Dominator Information for statement '" + statement.toString() + "':\n");
					complete.append(">> If remove: " + ifStmt.toString() + " will be removed at statement " + statement.toString());
				}
			}
			if (! complete.toString().equals(""))
				methodAnalysisEnvironment.getLog().debug(complete.toString());
		}
	}
	
	/**
	 * 
	 */
	public void checkAnalysis() {
		
	}
}
