package analysis;

import java.util.ArrayList;
import java.util.List;

import exception.SootException.*;
import model.*;
import model.SecurityMethod.MethodParameter;
import security.LevelEquation;
import security.LevelEquationVisitor.LevelEquationEvaluationVisitor;
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
	public SecurityLevelLookupValueSwitch(MethodAnalysisEnvironment methodAnalysisEnvironment, LocalMap in,	LocalMap out) {
		super(methodAnalysisEnvironment, in, out);
	}
	
	/**
	 * 
	 * @return
	 * @throws NoSecurityLevelException
	 */
	public String getLevel() throws NoSecurityLevelException {
		if (this.level == null) {
			throw new NoSecurityLevelException(SecurityMessages.accessToValueWithoutSecurityLevel(methodAnalysisEnvironment.getSourceLine(), SootUtils.generateMethodSignature(methodAnalysisEnvironment.getSootMethod())));
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
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseFloatConstant(soot.jimple.FloatConstant)
	 */
	@Override
	public void caseFloatConstant(FloatConstant v) {
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseIntConstant(soot.jimple.IntConstant)
	 */
	@Override
	public void caseIntConstant(IntConstant v) {
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseLongConstant(soot.jimple.LongConstant)
	 */
	@Override
	public void caseLongConstant(LongConstant v) {
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseNullConstant(soot.jimple.NullConstant)
	 */
	@Override
	public void caseNullConstant(NullConstant v) {
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseStringConstant(soot.jimple.StringConstant)
	 */
	@Override
	public void caseStringConstant(StringConstant v) {
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseClassConstant(soot.jimple.ClassConstant)
	 */
	@Override
	public void caseClassConstant(ClassConstant v) {
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#defaultCase(java.lang.Object)
	 */
	@Override
	public void defaultCase(Object object) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("default", "SecurityLevelLookupValueSwitch", object.toString(), getSourceLine()));
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
		SecurityLevelLookupValueSwitch leftSecurityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
		SecurityLevelLookupValueSwitch rightSecurityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
		try {
			left.apply(leftSecurityLevelLookupValueSwitch);
			right.apply(rightSecurityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), expr.toString()), e);
		}
		try {
			levels.add(leftSecurityLevelLookupValueSwitch.getLevel());
		} catch (NoSecurityLevelException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), left), e);
		}
		try {
			levels.add(rightSecurityLevelLookupValueSwitch.getLevel());
		} catch (NoSecurityLevelException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), right), e);
		}
		this.level = methodAnalysisEnvironment.getSecurityAnnotation().getStrongestLevelOf(levels);
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
		SecurityMethod securityInvokedMethod = new SecurityMethod(invokeExpr.getMethod(), true, methodAnalysisEnvironment.getSecurityAnnotation());
		String invokedMethodSignature = SootUtils.generateMethodSignature(securityInvokedMethod.getSootMethod());
		if (!securityInvokedMethod.isLibraryMethod()) {
			List<MethodParameter> invokedMethodParameter = securityInvokedMethod.getMethodParameters();
			if (invokeExpr.getArgCount() == invokedMethodParameter.size()) {
				List<String> parameterLevels = new ArrayList<String>();
				List<String> argumentLevels = new ArrayList<String>();
				for (int j = 0; j < invokeExpr.getArgCount(); j++) {
					Value value = invokeExpr.getArg(j);
					String argumentLevel = methodAnalysisEnvironment.getWeakestSecurityLevel();
					String parameterLevel = invokedMethodParameter.get(j).getLevel();
					String parameterName = invokedMethodParameter.get(j).getName();
					SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
					try {
						value.apply(securityLevelLookupValueSwitch);
					} catch (SwitchException e) {
						methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), invokeExpr.toString()), e);
					}
					try {
						argumentLevel = securityLevelLookupValueSwitch.getLevel();
					} catch (NoSecurityLevelException e) {
						methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), value), e);
					}
					if (!methodAnalysisEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(argumentLevel, parameterLevel)) {
						methodAnalysisEnvironment.getLog().security(getFileName(), getSourceLine(), SecurityMessages.weakerArgumentExpected(getMethodSignature(), invokedMethodSignature, getSourceLine(), argumentLevel, parameterLevel, parameterName));
					}
					argumentLevels.add(argumentLevel);
					parameterLevels.add(parameterLevel);
				}
				if (!securityInvokedMethod.isReturnSecurityValid(methodAnalysisEnvironment.getLog())) {
					methodAnalysisEnvironment.getLog().error(getFileName(), getSourceLine(), SecurityMessages.invalidReturnAnnotation(getMethodSignature(), invokedMethodSignature, getSourceLine())); 
				}
				LevelEquation levelEquation = securityInvokedMethod.getReturnLevelEquation();
				LevelEquationEvaluationVisitor levelEquationEvaluationVisitor = methodAnalysisEnvironment.getSecurityAnnotation().getLevelEquationEvaluationVisitor(argumentLevels, parameterLevels); 
				levelEquation.accept(levelEquationEvaluationVisitor);
				this.level = levelEquationEvaluationVisitor.getResultLevel();
			} else {
				methodAnalysisEnvironment.getLog().error(getFileName(), getSourceLine(), SecurityMessages.wrongArgumentParameterAmount(getMethodSignature(), invokedMethodSignature, getSourceLine()));
			}
		} else {
			List<String> argumentLevels = new ArrayList<String>();
			for (int j = 0; j < invokeExpr.getArgCount(); j++) {
				Value value = invokeExpr.getArg(j);
				String argumentLevel = methodAnalysisEnvironment.getWeakestSecurityLevel();
				SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
				try {
					value.apply(securityLevelLookupValueSwitch);
				} catch (SwitchException e) {
					methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), invokeExpr.toString()), e);
				}
				try {
					argumentLevel = securityLevelLookupValueSwitch.getLevel();
				} catch (NoSecurityLevelException e) {
					methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), value), e);
				}
				argumentLevels.add(argumentLevel);
			}
			this.level = methodAnalysisEnvironment.getSecurityAnnotation().getStrongestLevelOf(argumentLevels);
			methodAnalysisEnvironment.getLog().warning(getFileName(), getSourceLine(), SecurityMessages.invocationOfLibraryMethodMaxArgumentLevel(getMethodSignature(), invokedMethodSignature, getSourceLine(), level));
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
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNewMultiArrayExpr(soot.jimple.NewMultiArrayExpr)
	 */
	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNewExpr(soot.jimple.NewExpr)
	 */
	@Override
	public void caseNewExpr(NewExpr v) {
		this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
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
	@Override
	public void caseArrayRef(ArrayRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("array ref", "SecurityLevelLookupValueSwitch", v.toString(), getSourceLine()));
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
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
		try {
			value.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), expr.toString()), e);
		}
		try {
			this.level = securityLevelLookupValueSwitch.getLevel();
		} catch (NoSecurityLevelException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), value), e);
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
		SecurityField securityField = new SecurityField(sootField, true);
		if (! securityField.isLibraryClass()) {
			if (securityField.isFieldSecurityLevelValid(methodAnalysisEnvironment.getSecurityAnnotation(), methodAnalysisEnvironment.getLog())) {
				this.level = securityField.getLevel();
			} else {
				methodAnalysisEnvironment.getLog().error(getFileName(), getSourceLine(), SecurityMessages.invalidFieldAnnotation(getMethodSignature(), SootUtils.generateFieldSignature(sootField), getSourceLine()));
			}
		} else {
			this.level = methodAnalysisEnvironment.getWeakestSecurityLevel();
			methodAnalysisEnvironment.getLog().warning(getFileName(), getSourceLine(), SecurityMessages.accessOfLibraryField(getMethodSignature(), SootUtils.generateFieldSignature(sootField), getSourceLine()));
		}
	}
	
	/**
	 * 
	 * @param baseValue
	 */
	public void handleBase(Value baseValue) {
		String baseLevel = methodAnalysisEnvironment.getWeakestSecurityLevel();
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitch = new SecurityLevelLookupValueSwitch(methodAnalysisEnvironment, in, out);
		try {
			baseValue.apply(securityLevelLookupValueSwitch);
		} catch (SwitchException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.catchSwitchException(getMethodSignature(), getSourceLine(), baseValue.toString()), e);
		}
		try {
			baseLevel = securityLevelLookupValueSwitch.getLevel();
			
		} catch (NoSecurityLevelException e) {
			methodAnalysisEnvironment.getLog().exception(getFileName(), getSourceLine(), SecurityMessages.noSecurityLevel(getSourceLine(), getMethodSignature(), baseValue), e);
		}
		this.level = methodAnalysisEnvironment.getSecurityAnnotation().getMaxLevel(level, baseLevel);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseParameterRef(soot.jimple.ParameterRef)
	 */
	@Override
	public void caseParameterRef(ParameterRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("parameter ref", "SecurityLevelLookupValueSwitch", v.toString(), getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseCaughtExceptionRef(soot.jimple.CaughtExceptionRef)
	 */
	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("caught exception ref", "SecurityLevelLookupValueSwitch", v.toString(), getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseThisRef(soot.jimple.ThisRef)
	 */
	@Override
	public void caseThisRef(ThisRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("this ref", "SecurityLevelLookupValueSwitch", v.toString(), getSourceLine()));
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