package pattern;

import java.util.*;

import exception.SootException.*;
import model.*;
import model.MethodEnvironment.*;
import security.LevelEquation;
import security.LevelEquationVisitor.*;
import soot.*;
import soot.jimple.*;
import utils.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class SecurityLevelLookupValueSwitch extends AbstractTaintTrackingSwitch implements JimpleValueSwitch {
	
	/** */
	private String level = null;

	/**
	 * 
	 * @param methodAnalysisEnvironment
	 * @param in
	 * @param out
	 */
	public SecurityLevelLookupValueSwitch(AnalyzedMethodEnvironment analyzedMethodEnvironment, LocalMap in,	LocalMap out) {
		super(analyzedMethodEnvironment, in, out);
	}
	
	/**
	 * 
	 * @return
	 * @throws NoSecurityLevelException
	 */
	public String getLevel() throws NoSecurityLevelException {
		if (this.level == null) {
			throw new NoSecurityLevelException(SecurityMessages.accessToValueWithoutSecurityLevel(getSrcLn(), getMethodSignature()));
		}
		return level;
		
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseDoubleConstant(soot.jimple.DoubleConstant)
	 */
	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseFloatConstant(soot.jimple.FloatConstant)
	 */
	@Override
	public void caseFloatConstant(FloatConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseIntConstant(soot.jimple.IntConstant)
	 */
	@Override
	public void caseIntConstant(IntConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseLongConstant(soot.jimple.LongConstant)
	 */
	@Override
	public void caseLongConstant(LongConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseNullConstant(soot.jimple.NullConstant)
	 */
	@Override
	public void caseNullConstant(NullConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseStringConstant(soot.jimple.StringConstant)
	 */
	@Override
	public void caseStringConstant(StringConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseClassConstant(soot.jimple.ClassConstant)
	 */
	@Override
	public void caseClassConstant(ClassConstant v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#defaultCase(java.lang.Object)
	 */
	@Override
	public void defaultCase(Object object) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("default", "SecurityLevelLookupValueSwitch", object.toString(), getSrcLn()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseAddExpr(soot.jimple.AddExpr)
	 */
	@Override
	public void caseAddExpr(AddExpr v) {
		handleBinaryOperation(v);		
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseAndExpr(soot.jimple.AndExpr)
	 */
	@Override
	public void caseAndExpr(AndExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseCmpExpr(soot.jimple.CmpExpr)
	 */
	@Override
	public void caseCmpExpr(CmpExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseCmpgExpr(soot.jimple.CmpgExpr)
	 */
	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseCmplExpr(soot.jimple.CmplExpr)
	 */
	@Override
	public void caseCmplExpr(CmplExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseDivExpr(soot.jimple.DivExpr)
	 */
	@Override
	public void caseDivExpr(DivExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseEqExpr(soot.jimple.EqExpr)
	 */
	@Override
	public void caseEqExpr(EqExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNeExpr(soot.jimple.NeExpr)
	 */
	@Override
	public void caseNeExpr(NeExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseGeExpr(soot.jimple.GeExpr)
	 */
	@Override
	public void caseGeExpr(GeExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseGtExpr(soot.jimple.GtExpr)
	 */
	@Override
	public void caseGtExpr(GtExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseLeExpr(soot.jimple.LeExpr)
	 */
	@Override
	public void caseLeExpr(LeExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseLtExpr(soot.jimple.LtExpr)
	 */
	@Override
	public void caseLtExpr(LtExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseMulExpr(soot.jimple.MulExpr)
	 */
	@Override
	public void caseMulExpr(MulExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseOrExpr(soot.jimple.OrExpr)
	 */
	@Override
	public void caseOrExpr(OrExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseRemExpr(soot.jimple.RemExpr)
	 */
	@Override
	public void caseRemExpr(RemExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseShlExpr(soot.jimple.ShlExpr)
	 */
	@Override
	public void caseShlExpr(ShlExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseShrExpr(soot.jimple.ShrExpr)
	 */
	@Override
	public void caseShrExpr(ShrExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseUshrExpr(soot.jimple.UshrExpr)
	 */
	@Override
	public void caseUshrExpr(UshrExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseSubExpr(soot.jimple.SubExpr)
	 */
	@Override
	public void caseSubExpr(SubExpr v) {
		handleBinaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseXorExpr(soot.jimple.XorExpr)
	 */
	@Override
	public void caseXorExpr(XorExpr v) {
		handleBinaryOperation(v);
	}
	
	/**
	 * 
	 * @param expr
	 */
	private void handleBinaryOperation(BinopExpr expr) {
		Value left = expr.getOp1();
		Value right = expr.getOp2();
		List<String>  levels = new ArrayList<String>();
		SecurityLevelLookupValueSwitch leftSecurityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
		SecurityLevelLookupValueSwitch rightSecurityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
		try {
			left.apply(leftSecurityLevelLookupValueSwitch);
			right.apply(rightSecurityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), expr.toString()), e);
		}
		try {
			levels.add(leftSecurityLevelLookupValueSwitch.getLevel());
		} catch (NoSecurityLevelException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(), left.toString()), e);
		}
		try {
			levels.add(rightSecurityLevelLookupValueSwitch.getLevel());
		} catch (NoSecurityLevelException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(), right.toString()), e);
		}
		try {
			this.level = getSecurityAnnotation().getStrongestLevelOf(levels);
		} catch (InvalidLevelException e) {
			String[] levelArray = new String[levels.size()];
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(), levels.toArray(levelArray)), e);
		}
		
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseInterfaceInvokeExpr(soot.jimple.InterfaceInvokeExpr)
	 */
	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		handleInvokeExpr(v);
		Value base = v.getBase();
		handleBase(base);		
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseSpecialInvokeExpr(soot.jimple.SpecialInvokeExpr)
	 */
	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		handleInvokeExpr(v);
		Value base = v.getBase();
		handleBase(base);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseStaticInvokeExpr(soot.jimple.StaticInvokeExpr)
	 */
	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		handleInvokeExpr(v);		
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseVirtualInvokeExpr(soot.jimple.VirtualInvokeExpr)
	 */
	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		handleInvokeExpr(v);
		Value base = v.getBase();
		handleBase(base);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseDynamicInvokeExpr(soot.jimple.DynamicInvokeExpr)
	 */
	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		handleInvokeExpr(v);
	}
	
	/**
	 * 
	 * @param invokeExpr
	 */
	private void handleInvokeExpr(InvokeExpr invokeExpr) {
		MethodEnvironment invokedMethod = new MethodEnvironment(invokeExpr.getMethod(), getLog(), getSecurityAnnotation());
		String invokedMethodSignature = SootUtils.generateMethodSignature(invokedMethod.getSootMethod(), false, true, true);
		if (!invokedMethod.isLibraryMethod()) {
			List<MethodParameter> invokedMethodParameter = invokedMethod.getMethodParameters();
			if (invokeExpr.getArgCount() == invokedMethodParameter.size()) {
				List<String> parameterLevels = new ArrayList<String>();
				List<String> argumentLevels = new ArrayList<String>();
				for (int j = 0; j < invokeExpr.getArgCount(); j++) {
					Value value = invokeExpr.getArg(j);
					String argumentLevel = getWeakestSecurityLevel();
					String parameterLevel = invokedMethodParameter.get(j).getLevel();
					String parameterName = invokedMethodParameter.get(j).getName();
					SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
					try {
						value.apply(securityLevelLookupValueSwitch);
					} catch (SwitchException e) {
						getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), invokeExpr.toString()), e);
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
					argumentLevels.add(argumentLevel);
					parameterLevels.add(parameterLevel);
				}
				if (!invokedMethod.isReturnSecurityValid()) {
					getLog().error(getFileName(), getSrcLn(), SecurityMessages.invalidReturnAnnotation(getMethodSignature(), invokedMethodSignature, getSrcLn())); 
				}
				LevelEquation levelEquation = invokedMethod.getReturnLevelEquation();
				LevelEquationEvaluationVisitor levelEquationEvaluationVisitor = getSecurityAnnotation().getLevelEquationEvaluationVisitor(argumentLevels, parameterLevels); 
				levelEquation.accept(levelEquationEvaluationVisitor);
				this.level = levelEquationEvaluationVisitor.getResultLevel();
			} else {
				getLog().error(getFileName(), getSrcLn(), SecurityMessages.wrongArgumentParameterAmount(getMethodSignature(), invokedMethodSignature, getSrcLn()));
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
			List<String> argumentLevels = new ArrayList<String>();
			for (int j = 0; j < invokeExpr.getArgCount(); j++) {
				Value value = invokeExpr.getArg(j);
				String argumentLevel = getWeakestSecurityLevel();
				SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
				try {
					value.apply(securityLevelLookupValueSwitch);
				} catch (SwitchException e) {
					getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), invokeExpr.toString()), e);
				}
				try {
					argumentLevel = securityLevelLookupValueSwitch.getLevel();
				} catch (NoSecurityLevelException e) {
					getLog().exception(getFileName(), getSrcLn(), SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(), value.toString()), e);
				}
				argumentLevels.add(argumentLevel);
			}
			try {
				this.level = getSecurityAnnotation().getStrongestLevelOf(argumentLevels);
			} catch (InvalidLevelException e) {
				String[] levelArray = new String[argumentLevels.size()];
				getLog().exception(getFileName(), getSrcLn(), SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(), argumentLevels.toArray(levelArray)), e);
			}
			getLog().warning(getFileName(), getSrcLn(), SecurityMessages.invocationOfLibraryMethodMaxArgumentLevel(getMethodSignature(), invokedMethodSignature, getSrcLn(), level));
			getLog().warning(getFileName(), getSrcLn(), SecurityMessages.invocationOfLibraryMethodNoSideEffect(getMethodSignature(), invokedMethodSignature, getSrcLn()));
		}
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseCastExpr(soot.jimple.CastExpr)
	 */
	@Override
	public void caseCastExpr(CastExpr v) {
		Value value = v.getOp();
		handleOneValue(value, v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseInstanceOfExpr(soot.jimple.InstanceOfExpr)
	 */
	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		Value value = v.getOp();
		handleOneValue(value, v);		
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNewArrayExpr(soot.jimple.NewArrayExpr)
	 */
	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNewMultiArrayExpr(soot.jimple.NewMultiArrayExpr)
	 */
	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNewExpr(soot.jimple.NewExpr)
	 */
	@Override
	public void caseNewExpr(NewExpr v) {
		this.level = getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseLengthExpr(soot.jimple.LengthExpr)
	 */
	@Override
	public void caseLengthExpr(LengthExpr v) {
		handleUnaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNegExpr(soot.jimple.NegExpr)
	 */
	@Override
	public void caseNegExpr(NegExpr v) {
		handleUnaryOperation(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseArrayRef(soot.jimple.ArrayRef)
	 */
	@SuppressWarnings("unused")
	@Override
	public void caseArrayRef(ArrayRef v) {
		level = getWeakestSecurityLevel();
		Value base = v.getBase();
		handleBase(base);
		Value index = v.getIndex();
	}
	
	/**
	 * 
	 * @param expr
	 */
	private void handleUnaryOperation(UnopExpr expr) {
		Value value = expr.getOp();
		handleOneValue(value, expr);
	}
	
	/**
	 * 
	 * @param value
	 * @param expr
	 */
	private void handleOneValue(Value value, Expr expr) {
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
		try {
			value.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), expr.toString()), e);
		}
		try {
			this.level = securityLevelLookupValueSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(), value.toString()), e);
		}
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseStaticFieldRef(soot.jimple.StaticFieldRef)
	 */
	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {
		handleFieldAccess(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseInstanceFieldRef(soot.jimple.InstanceFieldRef)
	 */
	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		handleFieldAccess(v);
		Value baseValue = v.getBase();
		handleBase(baseValue);
	}
	
	/**
	 * 
	 * @param fieldRef
	 */
	public void handleFieldAccess(FieldRef fieldRef) {
		SootField sootField = fieldRef.getField();
		FieldEnvironment field = new FieldEnvironment(sootField, getLog(), getSecurityAnnotation());
		if (! field.isLibraryClass()) {
			if (field.isFieldSecurityLevelValid()) {
				this.level = field.getLevel();
			} else {
				getLog().error(getFileName(), getSrcLn(), SecurityMessages.invalidFieldAnnotation(getMethodSignature(), SootUtils.generateFieldSignature(sootField, false, true, true), getSrcLn()));
			}
		} else {
			this.level = getWeakestSecurityLevel();
			getLog().warning(getFileName(), getSrcLn(), SecurityMessages.accessOfLibraryField(getMethodSignature(), SootUtils.generateFieldSignature(sootField, false, true, true), getSrcLn()));
		}
	}
	
	/**
	 * 
	 * @param baseValue
	 */
	public void handleBase(Value baseValue) {
		String baseLevel = getWeakestSecurityLevel();
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(analyzedMethodEnvironment, in, out);
		try {
			baseValue.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.catchSwitchException(getMethodSignature(), getSrcLn(), baseValue.toString()), e);
		}
		try {
			baseLevel = securityLevelLookupValueSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.noSecurityLevel(getSrcLn(), getMethodSignature(), baseValue.toString()), e);
		}
		try {
			this.level = getSecurityAnnotation().getMaxLevel(level, baseLevel);
		} catch (InvalidLevelException e) {
			getLog().exception(getFileName(), getSrcLn(), SecurityMessages.invalidLevelsComparison(getMethodSignature(), getSrcLn(), level, baseLevel), e);
		}
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseParameterRef(soot.jimple.ParameterRef)
	 */
	@Override
	public void caseParameterRef(ParameterRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("parameter ref", "SecurityLevelLookupValueSwitch", v.toString(), getSrcLn()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseCaughtExceptionRef(soot.jimple.CaughtExceptionRef)
	 */
	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("caught exception ref", "SecurityLevelLookupValueSwitch", v.toString(), getSrcLn()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseThisRef(soot.jimple.ThisRef)
	 */
	@Override
	public void caseThisRef(ThisRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("this ref", "SecurityLevelLookupValueSwitch", v.toString(), getSrcLn()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.JimpleValueSwitch#caseLocal(soot.Local)
	 */
	@Override
	public void caseLocal(Local l) {
		if (in.containsLocal(l)) {
			this.level = in.getLevelOfLocal(l);
		}
	}
}