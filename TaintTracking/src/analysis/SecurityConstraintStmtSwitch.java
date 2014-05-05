package analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.AnalyzedMethodEnvironment;
import soot.G;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import constraints.ConstraintProgramCounterRef;
import constraints.ConstraintReturnRef;
import constraints.Constraints;
import constraints.IConstraint;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;
import extractor.UsedObjectStore;

public class SecurityConstraintStmtSwitch extends SecurityConstraintSwitch implements StmtSwitch {

	private Stmt returnStmt = null;

	protected SecurityConstraintStmtSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, Constraints in, Constraints out) {
		super(methodEnvironment, store, in, out);
	}

	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		InvokeExpr invokeExpr = stmt.getInvokeExpr();
		SootMethod invokedMethod = invokeExpr.getMethod();
		String signature = invokedMethod.getSignature();
		SecurityConstraintValueSwitch invokeExprSwitch = getNewValueSwitch();
		invokeExpr.apply(invokeExprSwitch);
		addNonReturnReferenceConstraintsToOut(invokeExprSwitch.getInnerConstraints(), signature);
		removeInvokedMethodArtifacts(invokeExprSwitch);
		removeStaticAccessArticfacts(invokeExprSwitch);
	}

	private void removeStaticAccessArticfacts(SecurityConstraintValueSwitch valueSwitch) {
		if (valueSwitch.hasStaticAccess()) {
			out.removeConstraintsContainingReferencesFor(valueSwitch.getStaticAccessSignature());
		}
	}

	private void removeInvokedMethodArtifacts(SecurityConstraintValueSwitch valueSwitch) {
		if (valueSwitch.isInvokeExpr()) {
			out.removeConstraintsContainingReferencesFor(valueSwitch.getInvokeMethod().getSignature());
		}
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		Value lhs = stmt.getLeftOp();
		Value rhs = stmt.getRightOp();
		SecurityConstraintValueSwitch lhsValueSwitch = getNewValueSwitch();
		SecurityConstraintValueSwitch rhsValueSwitch = getNewValueSwitch();
		lhs.apply(lhsValueSwitch);
		rhs.apply(rhsValueSwitch);
		removeAssignArtifacts(lhsValueSwitch);
		Set<IConstraintComponent> lComponents = rhsValueSwitch.getConstraintComponents();
		if (lhsValueSwitch.isField()) {
			G.v().out.println("Add");
			lComponents.add(new ConstraintProgramCounterRef(analyzedMethodEnvironment.getSootMethod().getSignature()));
		}
		generateConstraintsAndAddToOut(lComponents, lhsValueSwitch.getConstraintComponents());
		addConstraintsToOut(rhsValueSwitch.getInnerConstraints());
		removeInvokedMethodArtifacts(rhsValueSwitch);
		removeStaticAccessArticfacts(rhsValueSwitch);
	}

	private void addConstraintsToOut(Set<IConstraint> constraints) {
		out.addAllSmart(constraints);
	}

	private void addNonReturnReferenceConstraintsToOut(Set<IConstraint> constraints, String signature) {
		for (IConstraint constraint : constraints) {
			if (!constraint.containsReturnReferenceFor(signature)) {
				out.addSmart(constraint);
			}
		}
	}

	private void removeAssignArtifacts(SecurityConstraintValueSwitch valueSwitch) {
		if (valueSwitch.isLocal()) {
			out.removeConstraintsContaining(valueSwitch.getLocal());
		}
	}

	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		Value lhs = stmt.getLeftOp();
		Value rhs = stmt.getRightOp();
		SecurityConstraintValueSwitch lhsValueSwitch = getNewValueSwitch();
		SecurityConstraintValueSwitch rhsValueSwitch = getNewValueSwitch();
		lhs.apply(lhsValueSwitch);
		rhs.apply(rhsValueSwitch);
		generateConstraintsAndAddToOut(rhsValueSwitch.getConstraintComponents(), lhsValueSwitch.getConstraintComponents());
	}

	private void generateConstraintsAndAddToOut(Set<IConstraintComponent> lComponents, Set<IConstraintComponent> rComponents) {
		for (IConstraintComponent left : lComponents) {
			for (IConstraintComponent right : rComponents) {
				out.addSmart(new LEQConstraint(left, right));
			}
		}
	}

	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseGotoStmt(GotoStmt stmt) {}

	@Override
	public void caseIfStmt(IfStmt stmt) {
		Value condition = stmt.getCondition();
		SecurityConstraintValueSwitch valueSwitch = getNewValueSwitch();
		condition.apply(valueSwitch);
		Constraints tempConstraints = new Constraints(in.getConstraintsSet(), in.getProgramCounterStack(), getLevelMediator(), getLog());
		for (IConstraintComponent component : valueSwitch.getConstraintComponents()) {
			tempConstraints.addSmart(new LEQConstraint(component, new ConstraintProgramCounterRef(analyzedMethodEnvironment.getSootMethod().getSignature())));
		}
		out.addProgramCounterConstraints(stmt, tempConstraints.getTransitiveClosure().getConstraintsSet());
	}

	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseNopStmt(NopStmt stmt) {}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		returnStmt = stmt;
		Value value = stmt.getOp();
		String signature = getSootMethod().getSignature();

		SecurityConstraintValueSwitch valueSwitch = getNewValueSwitch();
		value.apply(valueSwitch);
		Set<IConstraintComponent> rComponents = new HashSet<IConstraintComponent>(
				Arrays.asList(new IConstraintComponent[] { new ConstraintReturnRef(signature) }));
		generateConstraintsAndAddToOut(valueSwitch.getConstraintComponents(), rComponents);
	}

	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		returnStmt = stmt;
	}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		throwNotImplementedException(stmt.getClass(), stmt.toString());
	}

	@Override
	public void defaultCase(Object object) {
		throwUnknownObjectException(object);
	}

	public boolean isReturnStmt() {
		return returnStmt != null;
	}

}
