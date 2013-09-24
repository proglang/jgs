package pattern;

import java.util.*;
import model.*;

import model.MethodEnvironment.MethodParameter;
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
	public StatementSwitch(AnalyzedMethodEnvironment analyzedMethodEnvironment, LocalMap in, LocalMap out) {
		super(analyzedMethodEnvironment, in, out);
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseBreakpointStmt(soot.jimple.BreakpointStmt)
	 */
	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("break point stmt", "StatementSwitch", stmt.toString(), getSrcLn()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseInvokeStmt(soot.jimple.InvokeStmt)
	 */
	@Override
	public void caseInvokeStmt(InvokeStmt stmt) { // Try to reduce to switch
		InvokeExpr invokeExpr =  stmt.getInvokeExpr();
		MethodEnvironment invokedMethod = new MethodEnvironment(invokeExpr.getMethod(), getLog(), getSecurityAnnotation());
		String invokedMethodSignature = SootUtils.generateMethodSignature(invokedMethod.getSootMethod(), false, true, true);
		if (! invokedMethod.isLibraryMethod()) {
			List<MethodParameter> invokedMethodParameter = invokedMethod.getMethodParameters();
			if (invokeExpr.getArgCount() == invokedMethodParameter.size()) {
				for (int j = 0; j < invokeExpr.getArgCount(); j++) {
					Value value = invokeExpr.getArg(j);
					String argumentLevel = getWeakestSecurityLevel();
					String parameterLevel = invokedMethodParameter.get(j).getLevel();
					String parameterName = invokedMethodParameter.get(j).getName();
					SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
					try {
						value.apply(securityLevelLookupValueSwitch);
					} catch (SwitchException e) {
						getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), stmt.toString()), e);
					}
					try {
						argumentLevel = securityLevelLookupValueSwitch.getLevel();
					} catch (NoSecurityLevelException e) {
						getLog().exception(getFileName(), getSrcLn(), SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(), value.toString()), e);
					}
					boolean isArgWeakerEqualsParam = false;
					try {
						isArgWeakerEqualsParam = getSecurityAnnotation().isWeakerOrEqualsThan(argumentLevel, parameterLevel);
					} catch (InvalidLevelException e) {
						getLog().exception(getFileName(), getSrcLn(), SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(), argumentLevel, parameterLevel), e);
					}
					if (! isArgWeakerEqualsParam) {
						getLog().security(getFileName(), getSrcLn(), SecurityMessages.weakerArgumentExpected(getMethodSignature(), invokedMethodSignature, getSrcLn(), argumentLevel, parameterLevel, parameterName));
					}
				}
				// SIDE-EFFECTS: |-----> 
				if (invokedMethod.areWriteEffectsValid()) {
					for (String effected : invokedMethod.getExpectedWriteEffects()) {
						// TODO: CHECK PC
						analyzedMethodEnvironment.addWriteEffectCausedByMethodInvocation(effected, getSrcLn(), getSootMethod());
					}
					if (SootUtils.isInitMethod(invokedMethod.getSootMethod()) || invokedMethod.getSootMethod().isStatic()) {
						if (invokedMethod.areClassWriteEffectsValid()) {
							for (String effected : invokedMethod.getExpectedClassWriteEffects()) {
								// TODO: CHECK PC
								analyzedMethodEnvironment.addWriteEffectCausedByClass(effected, getSrcLn(), getSootMethod().getDeclaringClass());
							}
						} else {
							String invokedClassSignature = SootUtils.generateClassSignature(invokedMethod.getSootMethod().getDeclaringClass(), false);
							getLog().error(getFileName(), getSrcLn(),  SecurityMessages.invalidInvokedClassWriteEffects(getMethodSignature(), getSrcLn(), invokedMethodSignature, invokedClassSignature));
						}
					}
				} else {
					getLog().error(getFileName(), getSrcLn(), SecurityMessages.invalidInvokedWriteEffects(getMethodSignature(), getSrcLn(), invokedMethodSignature));
				}
				// <-----| SIDE-EFFECTS
			} else {
				getLog().error(getFileName(), getSrcLn(), SecurityMessages.wrongArgumentParameterAmount(getMethodSignature(), invokedMethodSignature, getSrcLn()));
			}
		} else {
			getLog().warning(getFileName(), getSrcLn(), SecurityMessages.invocationOfLibraryMethodNoSecurityLevel(getMethodSignature(), invokedMethodSignature, getSrcLn()));
			getLog().warning(getFileName(), getSrcLn(), SecurityMessages.invocationOfLibraryMethodNoSideEffect(getMethodSignature(), invokedMethodSignature, getSrcLn()));
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
		String rightLevel = getWeakestSecurityLevel();
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
		try {
			right.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), stmt.toString()), e);
		}
		try {
			rightLevel = securityLevelLookupValueSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(), right.toString()), e);
		}
		Value left = stmt.getLeftOp();
		SecurityLevelUpdateSwitch securityLevelUpdateSwitch = new SecurityLevelUpdateSwitch(analyzedMethodEnvironment, in, out, rightLevel);
		try {
			left.apply(securityLevelUpdateSwitch);
		} catch (SwitchException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), stmt.toString()), e);
		}
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
		SecurityLevelUpdateSwitch securityLevelUpdateSwitch = new SecurityLevelUpdateSwitch(analyzedMethodEnvironment, in, out, null);
		securityLevelUpdateSwitch.setIdentityInformation(left);
		try {
			right.apply(securityLevelUpdateSwitch);
		} catch (SwitchException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), stmt.toString()), e);
		}
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseEnterMonitorStmt(soot.jimple.EnterMonitorStmt)
	 */
	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("enter monitor stmt", "StatementSwitch", stmt.toString(), getSrcLn()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseExitMonitorStmt(soot.jimple.ExitMonitorStmt)
	 */
	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("exit monitor stmt", "StatementSwitch", stmt.toString(), getSrcLn()));
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
		String conditionMaxLevel = getWeakestSecurityLevel();
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
		try {
			condition.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), stmt.toString()), e);
		}
		String level = getWeakestSecurityLevel();
		try {
			level = securityLevelLookupValueSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(), condition.toString()), e);
		}
		boolean isCondWeakerEqualsLevel = false;
		try {
			isCondWeakerEqualsLevel = getSecurityAnnotation().isWeakerOrEqualsThan(conditionMaxLevel, level);
		} catch (InvalidLevelException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(), conditionMaxLevel, level), e);
		}
		if (isCondWeakerEqualsLevel) {
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
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("lookup switch stmt", "StatementSwitch", stmt.toString(), getSrcLn()));
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
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("ret stmt", "StatementSwitch", stmt.toString(), getSrcLn()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseReturnStmt(soot.jimple.ReturnStmt)
	 */
	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		String expectedLevel = analyzedMethodEnvironment.getReturnLevel();
		Value value = stmt.getOp();
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
		try {
			value.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), stmt.toString()), e);
		}
		String level = getWeakestSecurityLevel();
		try {
			level = securityLevelLookupValueSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(), value.toString()), e);
		}
		if (out.hasProgramCounterLevel()){
			String pcLevel = getWeakestSecurityLevel();
			try {
				pcLevel = out.getStrongestProgramCounterLevel();
			} catch (InvalidLevelException e) {
				getLog().exception(getFileName(), getSrcLn(), SecurityMessages.invalidLevelsComparisonInMap(getMethodSignature(), getSrcLn()), e);
			}
			boolean isLevelWeakerEqualsPC = false;
			try {
				isLevelWeakerEqualsPC = getSecurityAnnotation().isWeakerOrEqualsThan(level, pcLevel);
			} catch (InvalidLevelException e) {
				getLog().exception(getFileName(), getSrcLn(), SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(), level, pcLevel), e);
			}
			if (isLevelWeakerEqualsPC) {
				level = pcLevel;
			}
		}
		
		if (expectedLevel.equals(SecurityAnnotation.VOID_LEVEL)) {
			getLog().security(getFileName(), getSrcLn(), SecurityMessages.voidReturnExpected(getFileName(), getSrcLn(), level, SecurityAnnotation.VOID_LEVEL));
		} else {
			boolean isLevelWeakerEqualsExpected = false;
			try {
				isLevelWeakerEqualsExpected = getSecurityAnnotation().isWeakerOrEqualsThan(level, expectedLevel);
			} catch (InvalidLevelException e) {
				getLog().exception(getFileName(), getSrcLn(), SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(), level, expectedLevel), e);
			}
			if (! isLevelWeakerEqualsExpected) {
				getLog().security(getFileName(), getSrcLn(), SecurityMessages.weakerReturnExpected(getMethodSignature(), getSrcLn(), level, expectedLevel));
			}
		}
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseReturnVoidStmt(soot.jimple.ReturnVoidStmt)
	 */
	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		if (! analyzedMethodEnvironment.isReturnSecurityVoid()) {
			getLog().security(getFileName(), getSrcLn(), SecurityMessages.voidReturn(getMethodSignature(), getSrcLn(), analyzedMethodEnvironment.getReturnLevel(), SecurityAnnotation.VOID_LEVEL));
		}
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseTableSwitchStmt(soot.jimple.TableSwitchStmt)
	 */
	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("table switch stmt", "StatementSwitch", stmt.toString(), getSrcLn()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#caseThrowStmt(soot.jimple.ThrowStmt)
	 */
	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("throw stmt", "StatementSwitch", stmt.toString(), getSrcLn()));
	}

	/**
	 * 
	 * @param stmt
	 * @see soot.jimple.StmtSwitch#defaultCase(java.lang.Object)
	 */
	@Override
	public void defaultCase(Object obj) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("default", "StatementSwitch", obj.toString(), getSrcLn()));
	}

}
