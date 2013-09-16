package analysis;

import java.util.*;
import model.*;

import model.SecurityMethod.MethodParameter;
import security.SecurityAnnotation;
import soot.*;
import soot.jimple.*;
import utils.*;
import exception.SootException.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 *
 */
public class StatementSwitch extends AbstractTaintTrackingSwitch implements StmtSwitch {

	/**
	 * 
	 * @param in
	 * @param out
	 * @param methodAnalysisEnvironment
	 */
	public StatementSwitch(LocalMap in, LocalMap out, MethodAnalysisEnvironment methodAnalysisEnvironment) {
		super(methodAnalysisEnvironment, in, out);
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseBreakpointStmt(soot.jimple.BreakpointStmt)
	 */
	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("break point stmt", "StatementSwitch", stmt.toString(), getSourceLine()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseInvokeStmt(soot.jimple.InvokeStmt)
	 */
	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		// TODO: Check SideEffects if stronger security
		// TODO: Can be reduced ???
		InvokeExpr invokeExpr =  stmt.getInvokeExpr();
		SecurityMethod securityInvokedMethod = new SecurityMethod(invokeExpr.getMethod(), true, methodAnalysisEnvironment.getSecurityAnnotation());
		String invokedMethodSignature = SootUtils.generateMethodSignature(securityInvokedMethod.getSootMethod());
		if (! securityInvokedMethod.isLibraryMethod()) {
			List<MethodParameter> invokedMethodParameter = securityInvokedMethod.getMethodParameters();
			if (invokeExpr.getArgCount() == invokedMethodParameter.size()) {
				for (int j = 0; j < invokeExpr.getArgCount(); j++) {
					Value value = invokeExpr.getArg(j);
					String argumentLevel = methodAnalysisEnvironment.getWeakestSecurityLevel();
					String parameterLevel = invokedMethodParameter.get(j).getLevel();
					String parameterName = invokedMethodParameter.get(j).getName();
					SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
					try {
						value.apply(securityLevelLookupValueSwitch);
					} catch (SwitchException e) {
						methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), stmt.toString()), e);
					}
					try {
						argumentLevel = securityLevelLookupValueSwitch.getLevel();
					} catch (NoSecurityLevelException e) {
						methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), value), e);
					}
					// TODO: Is generic result?
					if (!methodAnalysisEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(argumentLevel, parameterLevel)) {
						methodAnalysisEnvironment.getLog().security(getFileName(), getSourceLine(), SecurityMessages.weakerArgumentExpected(getMethodSignature(), invokedMethodSignature, getSourceLine(), argumentLevel, parameterLevel, parameterName));
					}
				}
			} else {
				methodAnalysisEnvironment.getLog().error(getFileName(), getSourceLine(), SecurityMessages.wrongArgumentParameterAmount(getMethodSignature(), invokedMethodSignature, getSourceLine()));
			}
		} else {
			methodAnalysisEnvironment.getLog().warning(getFileName(), getSourceLine(), SecurityMessages.invocationOfLibraryMethod(getMethodSignature(), invokedMethodSignature, getSourceLine()));
		}
	}
	
	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseAssignStmt(soot.jimple.AssignStmt)
	 */
	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		Value right = stmt.getRightOp();
		String rightLevel = methodAnalysisEnvironment.getWeakestSecurityLevel();
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
		try {
			right.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), stmt.toString()), e);
		}
		try {
			rightLevel = securityLevelLookupValueSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), right), e);
		}
		Value left = stmt.getLeftOp();
		SecurityLevelUpdateSwitch securityLevelUpdateSwitch = new SecurityLevelUpdateSwitch(methodAnalysisEnvironment, in, out, rightLevel);
		try {
			left.apply(securityLevelUpdateSwitch);
		} catch (SwitchException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), stmt.toString()), e);
		}
		/**
		 * TODO Ask PC-Level: 
		 * - Local: change level of Local to PC-level if pc-level is stronger than rightLevel
		 * - Field: Error if field level is weaker than PC-level
		 */
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseIdentityStmt(soot.jimple.IdentityStmt)
	 */
	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		Value left = stmt.getLeftOp();
		Value right = stmt.getRightOp();
		SecurityLevelUpdateSwitch securityLevelUpdateSwitch = new SecurityLevelUpdateSwitch(methodAnalysisEnvironment, in, out, null);
		securityLevelUpdateSwitch.setIdentityInformation(left);
		try {
			right.apply(securityLevelUpdateSwitch);
		} catch (SwitchException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), stmt.toString()), e);
		}
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseEnterMonitorStmt(soot.jimple.EnterMonitorStmt)
	 */
	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("enter monitor stmt", "StatementSwitch", stmt.toString(), getSourceLine()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseExitMonitorStmt(soot.jimple.ExitMonitorStmt)
	 */
	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("exit monitor stmt", "StatementSwitch", stmt.toString(), getSourceLine()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseGotoStmt(soot.jimple.GotoStmt)
	 */
	@Override
	public void caseGotoStmt(GotoStmt stmt) {
		// Nothing to do in case of a goto stmt
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseIfStmt(soot.jimple.IfStmt)
	 */
	@Override
	public void caseIfStmt(IfStmt stmt) {
		Value condition = stmt.getCondition();
		String conditionMaxLevel = methodAnalysisEnvironment.getWeakestSecurityLevel();
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
		try {
			condition.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), stmt.toString()), e);
		}
		String level = methodAnalysisEnvironment.getWeakestSecurityLevel();
		try {
			level = securityLevelLookupValueSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), condition), e);
		}
		if (methodAnalysisEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(conditionMaxLevel, level)) {
			conditionMaxLevel = level;
		}
		out.addProgramCounterLevel(stmt, conditionMaxLevel);
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseLookupSwitchStmt(soot.jimple.LookupSwitchStmt)
	 */
	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("lookup switch stmt", "StatementSwitch", stmt.toString(), getSourceLine()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseNopStmt(soot.jimple.NopStmt)
	 */
	@Override
	public void caseNopStmt(NopStmt stmt) {
		// Nothing to do in case of a nop stmt
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseRetStmt(soot.jimple.RetStmt)
	 */
	@Override
	public void caseRetStmt(RetStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("ret stmt", "StatementSwitch", stmt.toString(), getSourceLine()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseReturnStmt(soot.jimple.ReturnStmt)
	 */
	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		String expectedLevel = methodAnalysisEnvironment.getSecurityMethod().getReturnLevel();
		Value value = stmt.getOp();
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
		try {
			value.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), stmt.toString()), e);
		}
		String level = methodAnalysisEnvironment.getWeakestSecurityLevel();
		try {
			level = securityLevelLookupValueSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), value), e);
		}
		if (out.hasProgramCounterLevel()){
			String pcLevel = out.getStrongestProgramCounterLevel();
			if (methodAnalysisEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(level, pcLevel)) {
				level = pcLevel;
			}
		}
		if (!methodAnalysisEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(level, expectedLevel)) {
			methodAnalysisEnvironment.getLog().security(getFileName(), getSourceLine(), SecurityMessages.weakerReturnExpected(getMethodSignature(), getSourceLine(), level, expectedLevel));
		}
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseReturnVoidStmt(soot.jimple.ReturnVoidStmt)
	 */
	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		if (! methodAnalysisEnvironment.getSecurityMethod().isReturnSecurityVoid()) {
			methodAnalysisEnvironment.getLog().security(getFileName(), getSourceLine(), SecurityMessages.voidReturn(getMethodSignature(), getSourceLine(), methodAnalysisEnvironment.getSecurityMethod().getReturnLevel(), SecurityAnnotation.VOID_LEVEL));
		}
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseTableSwitchStmt(soot.jimple.TableSwitchStmt)
	 */
	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("table switch stmt", "StatementSwitch", stmt.toString(), getSourceLine()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseThrowStmt(soot.jimple.ThrowStmt)
	 */
	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("throw stmt", "StatementSwitch", stmt.toString(), getSourceLine()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#defaultCase(java.lang.Object)
	 */
	@Override
	public void defaultCase(Object obj) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("default", "StatementSwitch", obj.toString(), getSourceLine()));
	}

}
