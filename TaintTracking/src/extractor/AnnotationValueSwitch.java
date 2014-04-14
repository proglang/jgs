package extractor;

import static resource.Messages.getMsg;
import soot.Local;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EqExpr;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.JimpleValueSwitch;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import exception.SwitchException;

public class AnnotationValueSwitch implements JimpleValueSwitch {

	/**
	 * 
	 */
	private final AnnotationExtractor extractor;

	/**
	 * @param extractor
	 */
	protected AnnotationValueSwitch(AnnotationExtractor extractor) {
		this.extractor = extractor;
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseAddExpr(soot.jimple.AddExpr)
	 */
	@Override
	public void caseAddExpr(AddExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseAndExpr(soot.jimple.AndExpr)
	 */
	@Override
	public void caseAndExpr(AndExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.RefSwitch#caseArrayRef(soot.jimple.ArrayRef)
	 */
	@Override
	public void caseArrayRef(ArrayRef v) {
		v.getBase().apply(this);
		v.getIndex().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseCastExpr(soot.jimple.CastExpr)
	 */
	@Override
	public void caseCastExpr(CastExpr v) {
		v.getOp().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.RefSwitch#caseCaughtExceptionRef(soot.jimple.CaughtExceptionRef)
	 */
	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ConstantSwitch#caseClassConstant(soot.jimple.ClassConstant)
	 */
	@Override
	public void caseClassConstant(ClassConstant v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseCmpExpr(soot.jimple.CmpExpr)
	 */
	@Override
	public void caseCmpExpr(CmpExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseCmpgExpr(soot.jimple.CmpgExpr)
	 */
	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseCmplExpr(soot.jimple.CmplExpr)
	 */
	@Override
	public void caseCmplExpr(CmplExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseDivExpr(soot.jimple.DivExpr)
	 */
	@Override
	public void caseDivExpr(DivExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ConstantSwitch#caseDoubleConstant(soot.jimple.DoubleConstant)
	 */
	@Override
	public void caseDoubleConstant(DoubleConstant v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseDynamicInvokeExpr(soot.jimple.DynamicInvokeExpr)
	 */
	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		this.extractor.addMethodEnvironmentForMethod(v.getMethod());
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseEqExpr(soot.jimple.EqExpr)
	 */
	@Override
	public void caseEqExpr(EqExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ConstantSwitch#caseFloatConstant(soot.jimple.FloatConstant)
	 */
	@Override
	public void caseFloatConstant(FloatConstant v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseGeExpr(soot.jimple.GeExpr)
	 */
	@Override
	public void caseGeExpr(GeExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseGtExpr(soot.jimple.GtExpr)
	 */
	@Override
	public void caseGtExpr(GtExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.RefSwitch#caseInstanceFieldRef(soot.jimple.InstanceFieldRef)
	 */
	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		this.extractor.addFieldEvironmentForField(v.getField());
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseInstanceOfExpr(soot.jimple.InstanceOfExpr)
	 */
	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		v.getOp().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ConstantSwitch#caseIntConstant(soot.jimple.IntConstant)
	 */
	@Override
	public void caseIntConstant(IntConstant v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseInterfaceInvokeExpr(soot.jimple.InterfaceInvokeExpr)
	 */
	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		this.extractor.addMethodEnvironmentForMethod(v.getMethod());
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseLeExpr(soot.jimple.LeExpr)
	 */
	@Override
	public void caseLeExpr(LeExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseLengthExpr(soot.jimple.LengthExpr)
	 */
	@Override
	public void caseLengthExpr(LengthExpr v) {
		v.getOp().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.JimpleValueSwitch#caseLocal(soot.Local)
	 */
	@Override
	public void caseLocal(Local l) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ConstantSwitch#caseLongConstant(soot.jimple.LongConstant)
	 */
	@Override
	public void caseLongConstant(LongConstant v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseLtExpr(soot.jimple.LtExpr)
	 */
	@Override
	public void caseLtExpr(LtExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseMulExpr(soot.jimple.MulExpr)
	 */
	@Override
	public void caseMulExpr(MulExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseNeExpr(soot.jimple.NeExpr)
	 */
	@Override
	public void caseNeExpr(NeExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseNegExpr(soot.jimple.NegExpr)
	 */
	@Override
	public void caseNegExpr(NegExpr v) {
		v.getOp().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseNewArrayExpr(soot.jimple.NewArrayExpr)
	 */
	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		v.getSize().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseNewExpr(soot.jimple.NewExpr)
	 */
	@Override
	public void caseNewExpr(NewExpr v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseNewMultiArrayExpr(soot.jimple.NewMultiArrayExpr)
	 */
	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		for (int i = 0; i < v.getSizeCount(); i++) {
			v.getSize(i).apply(this);
		}
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ConstantSwitch#caseNullConstant(soot.jimple.NullConstant)
	 */
	@Override
	public void caseNullConstant(NullConstant v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseOrExpr(soot.jimple.OrExpr)
	 */
	@Override
	public void caseOrExpr(OrExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.RefSwitch#caseParameterRef(soot.jimple.ParameterRef)
	 */
	@Override
	public void caseParameterRef(ParameterRef v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseRemExpr(soot.jimple.RemExpr)
	 */
	@Override
	public void caseRemExpr(RemExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseShlExpr(soot.jimple.ShlExpr)
	 */
	@Override
	public void caseShlExpr(ShlExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseShrExpr(soot.jimple.ShrExpr)
	 */
	@Override
	public void caseShrExpr(ShrExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseSpecialInvokeExpr(soot.jimple.SpecialInvokeExpr)
	 */
	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		this.extractor.addMethodEnvironmentForMethod(v.getMethod());
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.RefSwitch#caseStaticFieldRef(soot.jimple.StaticFieldRef)
	 */
	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {
		this.extractor.addFieldEvironmentForField(v.getField());
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseStaticInvokeExpr(soot.jimple.StaticInvokeExpr)
	 */
	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		this.extractor.addMethodEnvironmentForMethod(v.getMethod());
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ConstantSwitch#caseStringConstant(soot.jimple.StringConstant)
	 */
	@Override
	public void caseStringConstant(StringConstant v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseSubExpr(soot.jimple.SubExpr)
	 */
	@Override
	public void caseSubExpr(SubExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.RefSwitch#caseThisRef(soot.jimple.ThisRef)
	 */
	@Override
	public void caseThisRef(ThisRef v) {}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseUshrExpr(soot.jimple.UshrExpr)
	 */
	@Override
	public void caseUshrExpr(UshrExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseVirtualInvokeExpr(soot.jimple.VirtualInvokeExpr)
	 */
	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		this.extractor.addMethodEnvironmentForMethod(v.getMethod());
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ExprSwitch#caseXorExpr(soot.jimple.XorExpr)
	 */
	@Override
	public void caseXorExpr(XorExpr v) {
		v.getOp1().apply(this);
		v.getOp2().apply(this);
	}

	/**
	 * DOC
	 * 
	 * @see soot.jimple.ConstantSwitch#defaultCase(java.lang.Object)
	 */
	@Override
	public void defaultCase(Object object) {
		throw new SwitchException(getMsg("exception.extractor.switch.unknown_object", object.toString(), this.getClass().getSimpleName()));
	}

}