package analysis;

import static resource.Messages.getMsg;
import model.AnalyzedMethodEnvironment;
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
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import constraints.ConstraintReturnRef;
import constraints.Constraints;
import constraints.IConstraint;
import constraints.IConstraintComponent;
import constraints.LEQConstraint;
import exception.SwitchException;
import extractor.UsedObjectStore;

public class SecurityConstraintStmtSwitch extends SecurityConstraintSwitch implements StmtSwitch {

	protected SecurityConstraintStmtSwitch(AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store, Constraints in, Constraints out) {
		super(methodEnvironment, store, in, out);
	}

	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		InvokeExpr invokeExpr = stmt.getInvokeExpr();
		SootMethod invokedMethod = invokeExpr.getMethod();
		String signature = invokedMethod.getSignature();
		SecurityConstraintValueSwitch invokeExprSwitch = new SecurityConstraintValueSwitch(analyzedMethodEnvironment, store, in, out);
		invokeExpr.apply(invokeExprSwitch);
		for (IConstraint constraint : invokeExprSwitch.getInnerConstraints()) {
			if (!constraint.containsReturnReferenceFor(signature)) {
				out.addSmart(constraint);
			}
		}
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		Value lhs = stmt.getLeftOp();
		Value rhs = stmt.getRightOp();
		SecurityConstraintValueSwitch lhsValueSwitch = new SecurityConstraintValueSwitch(analyzedMethodEnvironment, store, in, out);
		SecurityConstraintValueSwitch rhsValueSwitch = new SecurityConstraintValueSwitch(analyzedMethodEnvironment, store, in, out);
		lhs.apply(lhsValueSwitch);
		rhs.apply(rhsValueSwitch);
		for (IConstraintComponent right : rhsValueSwitch.getConstraintComponents()) {
			for (IConstraintComponent left : lhsValueSwitch.getConstraintComponents()) {
				out.addSmart(new LEQConstraint(right, left));
			}
		}
		for (IConstraint constraint : rhsValueSwitch.getInnerConstraints()) {
			out.addSmart(constraint);
		}

	}

	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		Value lhs = stmt.getLeftOp();
		Value rhs = stmt.getRightOp();
		SecurityConstraintValueSwitch lhsValueSwitch = new SecurityConstraintValueSwitch(analyzedMethodEnvironment, store, in, out);
		SecurityConstraintValueSwitch rhsValueSwitch = new SecurityConstraintValueSwitch(analyzedMethodEnvironment, store, in, out);
		lhs.apply(lhsValueSwitch);
		rhs.apply(rhsValueSwitch);
		for (IConstraintComponent right : rhsValueSwitch.getConstraintComponents()) {
			for (IConstraintComponent left : lhsValueSwitch.getConstraintComponents()) {
				out.addSmart(new LEQConstraint(right, left));
			}
		}
	}

	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	@Override
	public void caseGotoStmt(GotoStmt stmt) {}

	@Override
	public void caseIfStmt(IfStmt stmt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	@Override
	public void caseNopStmt(NopStmt stmt) {}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		Value value = stmt.getOp();
		String signature = getSootMethod().getSignature();
		IConstraintComponent returnRef = new ConstraintReturnRef(signature);
		SecurityConstraintValueSwitch valueSwitch = new SecurityConstraintValueSwitch(analyzedMethodEnvironment, store, in, out);
		value.apply(valueSwitch);
		for (IConstraintComponent val : valueSwitch.getConstraintComponents()) {
			out.addSmart(new LEQConstraint(val, returnRef));
		}
	}

	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	@Override
	public void defaultCase(Object object) {
		throw new SwitchException(getMsg("exception.analysis.switch.unknown_object", object.toString(), this.getClass().getSimpleName()));
	}

}
