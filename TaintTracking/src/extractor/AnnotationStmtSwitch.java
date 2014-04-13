package extractor;

import static resource.Messages.getMsg;
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
import exception.SwitchException;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class AnnotationStmtSwitch implements StmtSwitch {

	/**
	 * DOC
	 */
	private final AnnotationValueSwitch valueSwitch;

	/**
	 * @param annotationExtractor
	 */
	protected AnnotationStmtSwitch(AnnotationExtractor extractor) {
		this.valueSwitch = new AnnotationValueSwitch(extractor);
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
	public void defaultCase(Object object) {
		throw new SwitchException(getMsg("exception.extractor.switch.unknown_object", object.toString(), this.getClass().getSimpleName()));
	}
	
}