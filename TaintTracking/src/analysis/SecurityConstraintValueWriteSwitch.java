package analysis;

import model.AnalyzedMethodEnvironment;
import model.FieldEnvironment;
import soot.Local;
import soot.SootField;
import soot.Value;
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
import utils.AnalysisUtils;
import constraints.ComponentPlaceholder;
import constraints.ConstraintsSet;
import extractor.UsedObjectStore;

public class SecurityConstraintValueWriteSwitch extends ASecurityConstraintValueSwitch implements JimpleValueSwitch {

	protected SecurityConstraintValueWriteSwitch(Value value, AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store,
			ConstraintsSet in, ConstraintsSet out) {
		super(methodEnvironment, store, in, out);
		value.apply(this);
	}

	@Override
	public void caseDoubleConstant(DoubleConstant v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseFloatConstant(FloatConstant v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseIntConstant(IntConstant v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseLongConstant(LongConstant v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseNullConstant(NullConstant v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseStringConstant(StringConstant v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseClassConstant(ClassConstant v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void defaultCase(Object object) {
		throwUnknownObjectException(object);
	}

	@Override
	public void caseAddExpr(AddExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseAndExpr(AndExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseCmpExpr(CmpExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseCmplExpr(CmplExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseDivExpr(DivExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseEqExpr(EqExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseNeExpr(NeExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseGeExpr(GeExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseGtExpr(GtExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseLeExpr(LeExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseLtExpr(LtExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseMulExpr(MulExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseOrExpr(OrExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseRemExpr(RemExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseShlExpr(ShlExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseShrExpr(ShrExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseUshrExpr(UshrExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseSubExpr(SubExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseXorExpr(XorExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseCastExpr(CastExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseNewExpr(NewExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseLengthExpr(LengthExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseNegExpr(NegExpr v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseArrayRef(ArrayRef v) {
		Value array = v.getBase();
		Value index = v.getIndex();
		SecurityConstraintValueReadSwitch baseSwitch = getReadSwitch(array);
		SecurityConstraintValueReadSwitch indexSwitch = getReadSwitch(index);
		addReadComponents(baseSwitch.getReadComponents());
		addReadComponents(indexSwitch.getReadComponents());
		setComponentDimension(AnalysisUtils.getDimension(v.getType()));
		if (baseSwitch.getEqualComponents().size() > 0) {
			addWriteComponent(baseSwitch.getEqualComponents().get(0));
			for (int i = 1; i < baseSwitch.getEqualComponents().size(); i++) {
				appendEqualComponent(baseSwitch.getEqualComponents().get(1));
			}
		}
	}

	@Override
	public void caseStaticFieldRef(StaticFieldRef v) {
		handleField(v.getField());
	}

	@Override
	public void caseInstanceFieldRef(InstanceFieldRef v) {
		handleBase(v.getBase());
		handleField(v.getField());
	}

	private void handleBase(Value base) {
		SecurityConstraintValueReadSwitch baseSwitch = getReadSwitch(base);
		addReadComponents(baseSwitch.getReadComponents());
	}

	private void handleField(SootField f) {
		FieldEnvironment fe = getStore().getFieldEnvironment(field = f);
		addWriteComponent(fe.getLevel());
		handleFieldDimension(fe);
	}

	@Override
	public void caseParameterRef(ParameterRef v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseCaughtExceptionRef(CaughtExceptionRef v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseThisRef(ThisRef v) {
		throwInvalidWriteException(v);
	}

	@Override
	public void caseLocal(Local l) {
		local = l;
		ComponentPlaceholder cp = ComponentPlaceholder.getInstance();
		addWriteComponent(cp);
		handleDimension(l.getType(), cp);
	}

}
