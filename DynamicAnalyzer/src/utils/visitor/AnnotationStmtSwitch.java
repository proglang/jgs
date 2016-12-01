package utils.visitor;

import analyzer.level1.JimpleInjector;
import soot.Body;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.Constant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.ParameterRef;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import utils.exceptions.InternalAnalyzerException;
import utils.exceptions.NotSupportedStmtException;
import utils.logging.L1Logger;
import utils.visitor.AnnotationValueSwitch.RightElement;
import utils.visitor.AnnotationValueSwitch.StmtContext;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AnnotationStmtSwitch implements StmtSwitch {

	AnnotationValueSwitch valueSwitch = new AnnotationValueSwitch();
	Logger logger = L1Logger.getLogger();
	Body body;

	public AnnotationStmtSwitch(Body body) {
		this.body = body;
	}

	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		logger.fine("\n > > > Breakpoint statement identified < < <");
		valueSwitch.callingStmt = stmt;
	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {

		valueSwitch.callingStmt = stmt;

		InvokeStmt iStmt = stmt;
		valueSwitch.actualContext = StmtContext.INVOKE;

		logger.fine("\n > > > Invoke Statement identified < < <");

		InvokeExpr invokeExpr = iStmt.getInvokeExpr();

		invokeExpr.apply(valueSwitch);

		logger.finer("Method has return type: " + invokeExpr.getType());

		valueSwitch.actualContext = StmtContext.UNDEF;
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {

		AssignStmt aStmt = stmt;
		valueSwitch.callingStmt = aStmt;

		valueSwitch.actualContext = StmtContext.ASSIGNRIGHT;

		if (aStmt.getDefBoxes().size() != 1) {
			throw new InternalAnalyzerException(
					"Unexpected number of elements on "
							+ "the left side of assign statement");
		}

		logger.fine("\n > > > ASSIGN STATEMENT identified: " + aStmt + " < < <");
		// two lines below are useless information?!
		// logger.finer(" > > > left side: " + aStmt.getDefBoxes().toString() +
		// " < < <");
		// logger.finer(" > > > right side: " + aStmt.getUseBoxes().toString() +
		// " < < <");

		for (int i = 0; i < aStmt.getUseBoxes().size(); i++) {
			aStmt.getUseBoxes().get(i).getValue().apply(valueSwitch);
		}

		valueSwitch.actualContext = StmtContext.ASSIGNLEFT;

		Value leftOperand = aStmt.getLeftOp();

		switch (AnnotationValueSwitch.rightElement) {
		case NOT:
			break; // That means that the right element is already handeled
		case NEW_ARRAY:
			JimpleInjector.addArrayToObjectMap((Local) leftOperand, aStmt);
			break;
		case NEW_UNDEF_OBJECT:
			break;
		case MAKE_HIGH:
			break; // This two cases are treated later
		case MAKE_LOW:
			break;
		default:
			new InternalAnalyzerException("Unexpected Context: "
					+ AnnotationValueSwitch.rightElement);
		}

		leftOperand.apply(valueSwitch);

		/*
		 * This must be done after the regular valueSwitch. Otherwise the
		 * returnlevel of the MakeHigh/MakeLow Method would overwrite the new
		 * level of the local.
		 */
		switch (AnnotationValueSwitch.rightElement) {
		case MAKE_HIGH:
			logger.finest("Make left operand high");
			JimpleInjector.makeLocalHigh((Local) leftOperand, aStmt);
			break;
		case MAKE_LOW:
			logger.finest("Make left operand low");
			JimpleInjector.makeLocalLow((Local) leftOperand, aStmt);
			break;
		default:
			break;
		}

		// Information of rightElement must be reset after the assignstatement.
		AnnotationValueSwitch.rightElement = RightElement.NOT;
		// Reset actualContext to UNDEF
		valueSwitch.actualContext = StmtContext.UNDEF;
	}

	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {

		valueSwitch.callingStmt = stmt;

		valueSwitch.actualContext = StmtContext.IDENTITY;

		logger.fine("\n > > > Identity statement identified < < <");

		if (stmt.getRightOp() instanceof ParameterRef) {
			if (!body.getMethod().isMain()) {
				int posInArgList = ((ParameterRef) stmt.getRightOp())
						.getIndex();
				JimpleInjector.assignArgumentToLocal(posInArgList,
						(Local) stmt.getLeftOp(), stmt);
			}
		} else if (stmt.getRightOp() instanceof ThisRef) {
			// TODO im Grunde nicht nötig...
		} else if (stmt.getRightOp() instanceof CaughtExceptionRef) {
			logger.fine("Right operand in IdentityStmt is a CaughtException");
		} else {
			throw new InternalAnalyzerException(
					"Unexpected type of right value "
							+ stmt.getRightOp().toString() + " in IdentityStmt");
		}

		valueSwitch.actualContext = StmtContext.UNDEF;
	}

	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		logger.fine("\n > > > Enter monitor statement identified < < <");
		valueSwitch.callingStmt = stmt;
		throw new NotSupportedStmtException("EnterMonitorStmt");
	}

	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		logger.fine("\n > > > Exit monitor statement identified < < <");
		valueSwitch.callingStmt = stmt;
		throw new NotSupportedStmtException("ExitMonitorStmt");
	}

	@Override
	public void caseGotoStmt(GotoStmt stmt) {
		logger.fine("\n > > > Goto statement identified < < <");
		valueSwitch.callingStmt = stmt;
		logger.fine("GOTO: " + stmt.toString());
	}

	/*
	 * If an IfStmt is identified the condition must be checked. At this
	 * procedure a new lpc is added to the lpc stack.
	 */
	@Override
	public void caseIfStmt(IfStmt stmt) {
		logger.fine("\n > > > If statement identified < < <");

		logger.finest("Use and def boxes of IfStmt: "
				+ stmt.getUseAndDefBoxes().toString());

		// Check for all values in the condition if they are a constant value
		// or if they are stored in a local. In the second case the local is
		// added
		// to a list for the locals.
		List<ValueBox> valueList = stmt.getUseBoxes();
		ArrayList<Local> localList = new ArrayList<Local>();
		for (ValueBox v : valueList) {
			Value val = v.getValue();
			if (val instanceof Local) {
				localList.add((Local) val);
				logger.fine("New local added to local-list of IfStmt: " + val);
			}
		}

		int localListLength = localList.size();

		Local[] arguments = new Local[localListLength];

		for (int i = 0; i < localListLength; i++) {
			arguments[i] = localList.get(i);
		}

		JimpleInjector.checkCondition(stmt, arguments);
	}

	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		logger.fine("\n > > > Lookup switch statement identified < < <");
		valueSwitch.callingStmt = stmt;
		logger.finest("Use and def boxes of SwitchStmt: "
				+ stmt.getUseAndDefBoxes().toString());

		// Check for all values in the condition if they are a constant value
		// or if they are stored in a local. In the second case the local is
		// added
		// to a list for the locals.
		List<ValueBox> valueList = stmt.getUseBoxes();
		ArrayList<Local> localList = new ArrayList<Local>();
		for (ValueBox v : valueList) {
			Value val = v.getValue();
			if (val instanceof Local) {
				localList.add((Local) val);
				logger.fine("New local added to local-list of SwitchStmt: "
						+ val);
			}
		}

		int localListLength = localList.size();

		Local[] arguments = new Local[localListLength];

		for (int i = 0; i < localListLength; i++) {
			arguments[i] = localList.get(i);
		}

		JimpleInjector.checkCondition(stmt, arguments);
	}

	@Override
	public void caseNopStmt(NopStmt stmt) {
		logger.fine("\n > > > Nop statement identified < < <");
	}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		logger.fine("\n > > > Ret statement identified < < <");
		valueSwitch.callingStmt = stmt;
		throw new NotSupportedStmtException("RetStmt");
	}

	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		logger.fine("\n > > > Return statement identified < < <");
		valueSwitch.callingStmt = stmt;
		logger.finer("Use boxes: " + stmt.getUseBoxes().toString());
		Value val = stmt.getUseBoxes().get(0).getValue();
		// JimpleInjector.popGlobalPC();
		if (val instanceof Constant) {
			JimpleInjector.returnConstant(stmt);
		} else if (val instanceof Local) {
			
			JimpleInjector.returnLocal((Local) val, stmt);
		}
	}

	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		logger.fine("\n > > > Return void statement identified < < <");
		valueSwitch.callingStmt = stmt;
	}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		logger.fine("\n > > > Table switch statement identified < < <");
		valueSwitch.callingStmt = stmt;
		logger.finest("Use and def boxes of SwitchStmt: "
				+ stmt.getUseAndDefBoxes().toString());

		// Check for all values in the condition if they are a constant value
		// or if they are stored in a local. In the second case the local is
		// added
		// to a list for the locals.
		List<ValueBox> valueList = stmt.getUseBoxes();
		ArrayList<Local> localList = new ArrayList<Local>();
		for (ValueBox v : valueList) {
			Value val = v.getValue();
			if (val instanceof Local) {
				localList.add((Local) val);
				logger.fine("New local added to local-list of SwitchStmt: "
						+ val);
			}
		}

		int localListLength = localList.size();

		Local[] arguments = new Local[localListLength];

		for (int i = 0; i < localListLength; i++) {
			arguments[i] = localList.get(i);
		}

		JimpleInjector.checkCondition(stmt, arguments);

	}

	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		logger.fine("\n > > > Throw statement identified < < <");
		valueSwitch.callingStmt = stmt;
	}

	@Override
	public void defaultCase(Object obj) {
		logger.fine("\n > > > Default case of statements identified < < <");
		throw new NotSupportedStmtException("DefaultCase");
		// valueSwitch.callingStmt = stmt;

	}
}