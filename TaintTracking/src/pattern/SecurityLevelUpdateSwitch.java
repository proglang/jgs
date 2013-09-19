package pattern;

import model.*;
import model.MethodEnvironment.MethodParameter;
import soot.*;
import soot.jimple.*;
import utils.*;
import exception.SootException.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class SecurityLevelUpdateSwitch extends AbstractTaintTrackingSwitch implements JimpleValueSwitch {
	
	/** */
	private final String level;
	/** */
	private ParameterRef parameterRef = null;
	/** */
	private MethodParameter methodParameter = null;
	/** */
	private Value value = null;

	/**
	 * 
	 * @param methodAnalysisEnvironment
	 * @param in
	 * @param out
	 * @param level
	 */
	public SecurityLevelUpdateSwitch(MethodEnvironment methodEnvironment, LocalMap in, LocalMap out, String level) {
		super(methodEnvironment, in, out);
		this.level = level;
	}
	
	/**
	 * 
	 * @param parameterRef
	 * @param methodParameter
	 */
	public void setParameterInformation(ParameterRef parameterRef, MethodParameter methodParameter) {
		this.parameterRef = parameterRef;
		this.methodParameter = methodParameter;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setIdentityInformation(Value value) {
		this.value = value;
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseDoubleConstant(soot.jimple.DoubleConstant)
	 */
	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseFloatConstant(soot.jimple.FloatConstant)
	 */
	@Override
	public void caseFloatConstant(FloatConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseIntConstant(soot.jimple.IntConstant)
	 */
	@Override
	public void caseIntConstant(IntConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseLongConstant(soot.jimple.LongConstant)
	 */
	@Override
	public void caseLongConstant(LongConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseNullConstant(soot.jimple.NullConstant)
	 */
	@Override
	public void caseNullConstant(NullConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseStringConstant(soot.jimple.StringConstant)
	 */
	@Override
	public void caseStringConstant(StringConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#caseClassConstant(soot.jimple.ClassConstant)
	 */
	@Override
	public void caseClassConstant(ClassConstant v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("constant", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ConstantSwitch#defaultCase(java.lang.Object)
	 */
	@Override
	public void defaultCase(Object object) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("default", "SecurityLevelUpdateSwitch", object.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseAddExpr(soot.jimple.AddExpr)
	 */
	@Override
	public void caseAddExpr(AddExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseAndExpr(soot.jimple.AndExpr)
	 */
	@Override
	public void caseAndExpr(AndExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseCmpExpr(soot.jimple.CmpExpr)
	 */
	@Override
	public void caseCmpExpr(CmpExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseCmpgExpr(soot.jimple.CmpgExpr)
	 */
	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseCmplExpr(soot.jimple.CmplExpr)
	 */
	@Override
	public void caseCmplExpr(CmplExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseDivExpr(soot.jimple.DivExpr)
	 */
	@Override
	public void caseDivExpr(DivExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseEqExpr(soot.jimple.EqExpr)
	 */
	@Override
	public void caseEqExpr(EqExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNeExpr(soot.jimple.NeExpr)
	 */
	@Override
	public void caseNeExpr(NeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseGeExpr(soot.jimple.GeExpr)
	 */
	@Override
	public void caseGeExpr(GeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseGtExpr(soot.jimple.GtExpr)
	 */
	@Override
	public void caseGtExpr(GtExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseLeExpr(soot.jimple.LeExpr)
	 */
	@Override
	public void caseLeExpr(LeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseLtExpr(soot.jimple.LtExpr)
	 */
	@Override
	public void caseLtExpr(LtExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseMulExpr(soot.jimple.MulExpr)
	 */
	@Override
	public void caseMulExpr(MulExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseOrExpr(soot.jimple.OrExpr)
	 */
	@Override
	public void caseOrExpr(OrExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseRemExpr(soot.jimple.RemExpr)
	 */
	@Override
	public void caseRemExpr(RemExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseShlExpr(soot.jimple.ShlExpr)
	 */
	@Override
	public void caseShlExpr(ShlExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseShrExpr(soot.jimple.ShrExpr)
	 */
	@Override
	public void caseShrExpr(ShrExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseUshrExpr(soot.jimple.UshrExpr)
	 */
	@Override
	public void caseUshrExpr(UshrExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseSubExpr(soot.jimple.SubExpr)
	 */
	@Override
	public void caseSubExpr(SubExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseXorExpr(soot.jimple.XorExpr)
	 */
	@Override
	public void caseXorExpr(XorExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseInterfaceInvokeExpr(soot.jimple.InterfaceInvokeExpr)
	 */
	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseSpecialInvokeExpr(soot.jimple.SpecialInvokeExpr)
	 */
	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("invocation", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseStaticInvokeExpr(soot.jimple.StaticInvokeExpr)
	 */
	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("invocation", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseVirtualInvokeExpr(soot.jimple.VirtualInvokeExpr)
	 */
	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("invocation", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseDynamicInvokeExpr(soot.jimple.DynamicInvokeExpr)
	 */
	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("invocation", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseCastExpr(soot.jimple.CastExpr)
	 */
	@Override
	public void caseCastExpr(CastExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseInstanceOfExpr(soot.jimple.InstanceOfExpr)
	 */
	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNewArrayExpr(soot.jimple.NewArrayExpr)
	 */
	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNewMultiArrayExpr(soot.jimple.NewMultiArrayExpr)
	 */
	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNewExpr(soot.jimple.NewExpr)
	 */
	@Override
	public void caseNewExpr(NewExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseLengthExpr(soot.jimple.LengthExpr)
	 */
	@Override
	public void caseLengthExpr(LengthExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.ExprSwitch#caseNegExpr(soot.jimple.NegExpr)
	 */
	@Override
	public void caseNegExpr(NegExpr v) {
		throw new InvalidSwitchException(SecurityMessages.noLevelUpdatePossible("expression", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseArrayRef(soot.jimple.ArrayRef)
	 */
	@Override
	public void caseArrayRef(ArrayRef v) {
		String arrayLevel = methodEnvironment.getWeakestSecurityLevel();
		String indexLevel = methodEnvironment.getWeakestSecurityLevel();
		String rightLevel = level;
		if (out.hasProgramCounterLevel()) {
			String pcLevel = out.getStrongestProgramCounterLevel();
			if (methodEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(rightLevel, pcLevel)) {
				rightLevel = pcLevel;
			}
		}
		Value base = v.getBase();
		Value index = v.getIndex();
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitchBase = new SecurityLevelLookupValueSwitch(methodEnvironment, in, out);
		SecurityLevelLookupValueSwitch securityLevelLookupValueSwitchIndex = new SecurityLevelLookupValueSwitch(methodEnvironment, in, out);
		base.apply(securityLevelLookupValueSwitchBase);
		arrayLevel = securityLevelLookupValueSwitchBase.getLevel();
		index.apply(securityLevelLookupValueSwitchIndex);
		indexLevel = securityLevelLookupValueSwitchIndex.getLevel();
		if (!methodEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(rightLevel, arrayLevel)) {
			methodEnvironment.getLog().security(getFileName(), getSourceLine(), SecurityMessages.assignmentToWeakerArray(getMethodSignature(), getSourceLine(), arrayLevel, rightLevel));
		} 
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseStaticFieldRef(soot.jimple.StaticFieldRef)
	 */
	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {
		handleFieldRef(v);
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseInstanceFieldRef(soot.jimple.InstanceFieldRef)
	 */
	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		handleFieldRef(v);
	}
	
	/**
	 * 
	 * @param fieldRef
	 */
	private void handleFieldRef(FieldRef fieldRef) {
		String leftLevel = methodEnvironment.getWeakestSecurityLevel();
		String rightLevel = level;
		SootField sootField = fieldRef.getField();
		if (out.hasProgramCounterLevel()) {
			String pcLevel = out.getStrongestProgramCounterLevel();
			if (methodEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(rightLevel, pcLevel)) {
				rightLevel = pcLevel;
			}
		}
		FieldEnvironment securityField = new FieldEnvironment(sootField, methodEnvironment.getLog(), methodEnvironment.getSecurityAnnotation());
		if (!securityField.isLibraryClass()) {
			if (securityField.isFieldSecurityLevelValid()) {
				leftLevel = securityField.getLevel();
			} else {
				methodEnvironment.getLog().error(getFileName(), getSourceLine(), SecurityMessages.invalidFieldAnnotation(getMethodSignature(), SootUtils.generateFieldSignature(sootField), getSourceLine()));
			}
		} else {
			methodEnvironment.getLog().warning(getFileName(), getSourceLine(), SecurityMessages.assignmentToLibraryField(getMethodSignature(), SootUtils.generateFieldSignature(sootField), getSourceLine()));
		}
		if (!methodEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(rightLevel, leftLevel)) {
			methodEnvironment.getLog().security(getFileName(), getSourceLine(), SecurityMessages.assignmentToWeakerField(getMethodSignature(), SootUtils.generateFieldSignature(sootField), getSourceLine(), leftLevel, rightLevel));
		} 
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseParameterRef(soot.jimple.ParameterRef)
	 */
	@Override
	public void caseParameterRef(ParameterRef v) {
		if (level == null && value != null) {
			MethodParameter methodParameter = methodEnvironment.getMethodParameterAtIndex(v.getIndex());
			SecurityLevelUpdateSwitch securityLevelUpdateSwitch = new SecurityLevelUpdateSwitch(methodEnvironment, in, out, methodParameter.getLevel());
			securityLevelUpdateSwitch.setParameterInformation(v, methodParameter);
			value.apply(securityLevelUpdateSwitch);
		} else {
			throw new UnimplementedSwitchException("Parameter Ref not implemented");
		}
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseCaughtExceptionRef(soot.jimple.CaughtExceptionRef)
	 */
	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		throw new UnimplementedSwitchException(SecurityMessages.unimplementedSwitchCase("caught exception ref", "SecurityLevelUpdateSwitch", v.toString(), methodEnvironment.getSourceLine()));
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.RefSwitch#caseThisRef(soot.jimple.ThisRef)
	 */
	@Override
	public void caseThisRef(ThisRef v) {
		if (level == null && value != null) {
			String level = methodEnvironment.getWeakestSecurityLevel();
			SecurityLevelUpdateSwitch securityLevelUpdateSwitch = new SecurityLevelUpdateSwitch(methodEnvironment, in, out, level);
			value.apply(securityLevelUpdateSwitch);	
		} else {
			throw new UnimplementedSwitchException("This Ref not implemented");
		}
	}

	/**
	 * 
	 * @param v
	 * @see soot.jimple.JimpleValueSwitch#caseLocal(soot.Local)
	 */
	@Override
	public void caseLocal(Local l) {
		String localLevel = methodEnvironment.getWeakestSecurityLevel();
		String valueLevel = level; 
		if (in.containsLocal(l)) {
			localLevel = in.getLevelOfLocal(l);
		} else {
			methodEnvironment.getLog().error(getFileName(), getSourceLine(), SecurityMessages.localNotFoundUpdate(getMethodSignature(), getSourceLine(), l.getName(), valueLevel));
		}
		if (parameterRef != null && methodParameter != null) {
			if (! (methodParameter.getPosition() >= 0 && methodParameter.getName().equals(l.getName()) && methodParameter.getType().equals(parameterRef.getType()))) {
				methodEnvironment.getLog().error(getFileName(), getSourceLine(), SecurityMessages.noParameterMatch(getMethodSignature(), getSourceLine(), methodParameter.getName(), l.getName(), valueLevel));
			}
		}
		if (out.hasProgramCounterLevel()) {
			String pcLevel = out.getStrongestProgramCounterLevel();
			if (methodEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(valueLevel, pcLevel)) {
				valueLevel = pcLevel;
			}
		}
		if (methodEnvironment.getSecurityAnnotation().isWeakerOrEqualsThan(valueLevel, localLevel)) {
			if (! localLevel.equals(valueLevel)) {
				methodEnvironment.getLog().warning(getFileName(), getSourceLine(), SecurityMessages.weakenLocalVariable(getMethodSignature(), getSourceLine(), l.getName(), localLevel, valueLevel));
			}
		}
		if (!out.update(l, valueLevel)) {
			methodEnvironment.getLog().error(getFileName(), getSourceLine(), SecurityMessages.localNotFoundUpdate(getMethodSignature(), getSourceLine(), l.getName(), valueLevel));
		}
	}
}
