package extractor;

import interfaces.Cancelable;

import java.util.ArrayList;
import java.util.List;

import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import exception.SootException.InvalidSwitchException;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class AnnotationStmtSwitch implements StmtSwitch, Cancelable {

	/**
	 * DOC
	 */
	private static final String EXCEPTION_UNKNOWN_STMT_OBJ = "Unknown object for the statement switch in the environment extractor.";
	/**
	 * DOC
	 */
	private final List<Cancelable> listeners = new ArrayList<Cancelable>();

	/**
	 * DOC
	 */
	private final AnnotationValueSwitch valueSwitch;

	/**
	 * @param annotationExtractor
	 */
	protected AnnotationStmtSwitch(AnnotationExtractor extractor) {
		this.valueSwitch = new AnnotationValueSwitch(extractor);
		addCancelListener(extractor);
	}

	@Override
	public void addCancelListener(Cancelable cancelable) {
		this.listeners.add(cancelable);

	}

	@Override
	public void cancel() {
		for (Cancelable cancelable : listeners) {
			cancelable.cancel();
		}
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseAssignStmt(soot.jimple.AssignStmt)
	 */
	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		stmt.getLeftOp().apply(valueSwitch);
		stmt.getRightOp().apply(valueSwitch);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseBreakpointStmt(soot.jimple.BreakpointStmt)
	 */
	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseEnterMonitorStmt(soot.jimple.EnterMonitorStmt)
	 */
	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		stmt.getOp().apply(valueSwitch);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseExitMonitorStmt(soot.jimple.ExitMonitorStmt)
	 */
	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		stmt.getOp().apply(valueSwitch);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseGotoStmt(soot.jimple.GotoStmt)
	 */
	@Override
	public void caseGotoStmt(GotoStmt stmt) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseIdentityStmt(soot.jimple.IdentityStmt)
	 */
	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		stmt.getLeftOp().apply(valueSwitch);
		stmt.getRightOp().apply(valueSwitch);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseIfStmt(soot.jimple.IfStmt)
	 */
	@Override
	public void caseIfStmt(IfStmt stmt) {
		stmt.getCondition().apply(valueSwitch);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseInvokeStmt(soot.jimple.InvokeStmt)
	 */
	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		stmt.getInvokeExpr().apply(valueSwitch);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseLookupSwitchStmt(soot.jimple.LookupSwitchStmt)
	 */
	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		// TODO: Consider reaction
		// stmt.getKey().apply(valueSwitch);
		// for (int i = 0; i < stmt.getTargetCount(); i++) {
		// stmt.getTarget(i).apply(valueSwitch);
		// }
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseNopStmt(soot.jimple.NopStmt)
	 */
	@Override
	public void caseNopStmt(NopStmt stmt) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseRetStmt(soot.jimple.RetStmt)
	 */
	@Override
	public void caseRetStmt(RetStmt stmt) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseReturnStmt(soot.jimple.ReturnStmt)
	 */
	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		stmt.getOp().apply(valueSwitch);

	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseReturnVoidStmt(soot.jimple.ReturnVoidStmt)
	 */
	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseTableSwitchStmt(soot.jimple.TableSwitchStmt)
	 */
	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		// TODO: Consider reaction
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#caseThrowStmt(soot.jimple.ThrowStmt)
	 */
	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		// TODO: Consider reaction
		stmt.getOp().apply(valueSwitch);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.StmtSwitch#defaultCase(java.lang.Object)
	 */
	@Override
	public void defaultCase(Object obj) {
		throw new InvalidSwitchException(EXCEPTION_UNKNOWN_STMT_OBJ);

	}

	@Override
	public void removeCancelListener(Cancelable cancelable) {
		this.listeners.remove(cancelable);

	}
}