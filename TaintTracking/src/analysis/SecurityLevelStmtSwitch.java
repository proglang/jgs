package analysis;

import static resource.Messages.getMsg;
import model.AnalyzedMethodEnvironment;
import security.ILevel;
import soot.Value;
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
import extractor.UsedObjectStore;

/**
 * <h1>Statement switch for the {@link SecurityLevelAnalysis} analysis</h1>
 * 
 * The {@link SecurityLevelStmtSwitch} extends the switch {@link SecurityLevelSwitch} and contains incoming and outgoing locals map of a
 * specific state in the progress of the method {@link SecurityLevelAnalysis#flowThrough(LocalsMap, soot.Unit, LocalsMap)} as well as the
 * environment of the current analyzed method. The class provides for every possible statement a method that progress the statement, i.e.
 * calculates or updates the <em>security levels</em> of the statement components with the help of other switches. Also the methods check
 * for security violations, such as an invalid return <em>security level</em> of a method, etc. This switch is also responsible for checking
 * whether a implicit flow starts.<br />
 * Note, that the current version doesn't implement all possible statements.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.1
 * @see SecurityLevelSwitch
 * @see SecurityLevelReadValueSwitch
 * @see SecurityLevelWriteValueSwitch
 */
public class SecurityLevelStmtSwitch extends SecurityLevelSwitch implements StmtSwitch {

	/**
	 * Constructor of a {@link SecurityLevelStmtSwitch} that requires the current incoming and outgoing map of local variables as well as the
	 * environment of the current analyzed method.
	 * 
	 * @param analysisEnvironment
	 *          The environment of the method that is currently analyzed.
	 * @param in
	 *          Current incoming map of the local variables.
	 * @param out
	 *          Current outgoing map of the local variables.
	 */
	protected SecurityLevelStmtSwitch(AnalyzedMethodEnvironment analyzedMethodEnvironment, UsedObjectStore store, LocalsMap in, LocalsMap out) {
		super(analyzedMethodEnvironment, store, in, out);
	}

	/**
	 * Method, which should process the given statement of type {@link AssignStmt}. Therefore the method looks up the <em>security level</em>
	 * of the right hand side value and tries to update the <em>security level</em> of the left hand side value.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseAssignStmt(soot.jimple.AssignStmt)
	 */
	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		Value right = stmt.getRightOp();
		ILevel rightLevel = calculateLevel(right, stmt.toString());
		Value left = stmt.getLeftOp();
		updateLevel(left, rightLevel, stmt.toString());
	}

	/**
	 * Method, which should process the given statement of type {@link BreakpointStmt}, but is not implemented in the current version of this
	 * method. If method will be called an exception is thrown.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseBreakpointStmt(soot.jimple.BreakpointStmt)
	 * @throws UnimplementedSwitchException
	 *           Method throws always this exception, because the method is not implemented.
	 */
	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	/**
	 * Method, which should process the given statement of type {@link EnterMonitorStmt}, but is not implemented in the current version of
	 * this method. If method will be called an exception is thrown.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseEnterMonitorStmt(soot.jimple.EnterMonitorStmt)
	 * @throws UnimplementedSwitchException
	 *           Method throws always this exception, because the method is not implemented.
	 */
	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	/**
	 * Method, which should process the given statement of type {@link ExitMonitorStmt}, but is not implemented in the current version of this
	 * method. If method will be called an exception is thrown.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseExitMonitorStmt(soot.jimple.ExitMonitorStmt)
	 * @throws UnimplementedSwitchException
	 *           Method throws always this exception, because the method is not implemented.
	 */
	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	/**
	 * Method, which should process the given statement of type {@link GotoStmt} . In this case, there is no reason to check the statement in
	 * more detail. Because of that nothing will be done for a goto statement.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseGotoStmt(soot.jimple.GotoStmt)
	 */
	@Override
	public void caseGotoStmt(GotoStmt stmt) {
		// Nothing to do in case of a goto stmt
	}

	/**
	 * Method, which should process the given statement of type {@link IdentityStmt}. Therefore the method looks up the
	 * <em>security level</em> of the right hand side (the level depends on the annotation if it is a parameter reference, but if it is a this
	 * reference, then the level is the weakest available <em>security level</em>). For the assignment of parameter to a local variable also
	 * the name and type of them are checked.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseIdentityStmt(soot.jimple.IdentityStmt)
	 */
	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		Value left = stmt.getLeftOp();
		Value right = stmt.getRightOp();
		updateIdentityLevel(right, left, stmt.toString());
	}

	/**
	 * Method, which should process the given statement of type {@link IfStmt}. Therefore the method looks up which <em>security level</em>
	 * the condition of the if statement has and adds the {@link IfStmt} as well as the <em>security level</em> of the condition to the
	 * program counter, i.e. a new implicit flow will be started.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseIfStmt(soot.jimple.IfStmt)
	 */
	@Override
	public void caseIfStmt(IfStmt stmt) {
		Value condition = stmt.getCondition();
		ILevel level = calculateLevel(condition, stmt.toString());
		out.addProgramCounterLevel(stmt, level);
	}

	/**
	 * Method, which should process the given statement of type {@link InvokeStmt}. Therefore the method processes the invoke expression
	 * without calculating the return <em>security level</em> of the invoked method. I.e. the method checks the <em>security levels</em> of
	 * the method parameters and also the <em>write effects</em> of the method.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseInvokeStmt(soot.jimple.InvokeStmt)
	 * @see SecurityLevelSwitch#calculateInvokeExprLevel(soot.jimple.InvokeExpr, boolean)
	 */
	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		calculateInvokeExprLevel(stmt.getInvokeExpr(), false);
	}

	/**
	 * Method, which should process the given statement of type {@link LookupSwitchStmt}, but is not implemented in the current version of
	 * this method. If method will be called an exception is thrown.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseLookupSwitchStmt(soot.jimple.LookupSwitchStmt)
	 * @throws UnimplementedSwitchException
	 *           Method throws always this exception, because the method is not implemented.
	 */
	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	/**
	 * Method, which should process the given statement of type {@link NopStmt}. In this case, there is no reason to check the statement in
	 * more detail. Because of that nothing will be done for a nop statement.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseNopStmt(soot.jimple.NopStmt)
	 */
	@Override
	public void caseNopStmt(NopStmt stmt) {
		// Nothing to do in case of a nop stmt
	}

	/**
	 * Method, which should process the given statement of type {@link RetStmt}, but is not implemented in the current version of this method.
	 * If method will be called an exception is thrown.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseRetStmt(soot.jimple.RetStmt)
	 * @throws UnimplementedSwitchException
	 *           Method throws always this exception, because the method is not implemented.
	 */
	@Override
	public void caseRetStmt(RetStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	/**
	 * Method, which should process the given statement of type {@link ReturnStmt}. Therefore the method looks up which
	 * <em>security level</em> the returned value has and checks also whether this level is weaker than the program counter level. If the
	 * program counter <em>security level</em> is stronger, then this level will be compared with the expected return <em>security level</em>,
	 * otherwise the level of the returned value will be compared. In both cases the expected return <em>security level</em> has to be
	 * stronger or equals, if not then an error will be logged.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseReturnStmt(soot.jimple.ReturnStmt)
	 */
	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		ILevel expectedLevel = analyzedMethodEnvironment.getReturnLevel();
		Value value = stmt.getOp();
		ILevel level = takePCintoAccount(calculateLevel(value, stmt.toString()));
		if (!isWeakerOrEqualLevel(level, expectedLevel)) {
			logSecurity(getMsg("security.return.stronger", getMethodSignature(), level.getName(), getSrcLn(), expectedLevel.getName()));
		}
	}

	/**
	 * Method, which should process the given statement of type {@link ReturnVoidStmt}. Therefore the method checks whether the analyzed
	 * method is a 'void' return <em>security level</em>, if not then an error will be logged.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseReturnVoidStmt(soot.jimple.ReturnVoidStmt)
	 */
	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		// Nothing to do in case of a void return
	}

	/**
	 * Method, which should process the given statement of type {@link TableSwitchStmt}, but is not implemented in the current version of this
	 * method. If method will be called an exception is thrown.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseTableSwitchStmt(soot.jimple.TableSwitchStmt)
	 * @throws UnimplementedSwitchException
	 *           Method throws always this exception, because the method is not implemented.
	 */
	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	/**
	 * Method, which should process the given statement of type {@link ThrowStmt}, but is not implemented in the current version of this
	 * method. If method will be called an exception is thrown.
	 * 
	 * @param stmt
	 *          Statement that should be processed to check for security violations.
	 * @see soot.jimple.StmtSwitch#caseThrowStmt(soot.jimple.ThrowStmt)
	 * @throws UnimplementedSwitchException
	 *           Method throws always this exception, because the method is not implemented.
	 */
	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		throw new SwitchException(getMsg("exception.analysis.switch.not_implemented", stmt.toString(), getSrcLn(), stmt.getClass()
				.getSimpleName(), this.getClass().getSimpleName()));
	}

	/**
	 * Default case: Method, which should process the given object, but an object can't be processed to check for security violations. If
	 * method will be called an exception is thrown.
	 * 
	 * @param obj
	 *          Object that should be checked for security violations.
	 * @see soot.jimple.StmtSwitch#defaultCase(java.lang.Object)
	 * @throws UnimplementedSwitchException
	 *           Method throws always this exception, because this method may not be invoked.
	 */
	@Override
	public void defaultCase(Object object) {
		throw new SwitchException(getMsg("exception.analysis.switch.unknown_object", object.toString(), this.getClass().getSimpleName()));
	}

}
