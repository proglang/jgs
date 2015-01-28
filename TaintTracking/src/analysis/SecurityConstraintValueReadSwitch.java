package analysis;

import static resource.Messages.getMsg;
import static utils.AnalysisUtils.getDimension;

import java.util.HashSet;
import java.util.Set;

import model.AnalyzedMethodEnvironment;
import model.ClassEnvironment;
import model.FieldEnvironment;
import model.MethodEnvironment;
import soot.ArrayType;
import soot.Local;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
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
import soot.jimple.InvokeExpr;
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
import constraints.ComponentArrayRef;
import constraints.ComponentLocal;
import constraints.ComponentParameterRef;
import constraints.ComponentProgramCounterRef;
import constraints.ComponentReturnRef;
import constraints.ConstraintsSet;
import constraints.ConstraintsUtils;
import constraints.IComponent;
import constraints.LEQConstraint;
import exception.CastInvalidException;
import exception.IllegalNewArrayException;
import extractor.UsedObjectStore;

public class SecurityConstraintValueReadSwitch extends
        ASecurityConstraintValueSwitch implements JimpleValueSwitch {

    protected SootClass klass = null;
    protected SootMethod method = null;
    protected Set<LEQConstraint> writeEffects = new HashSet<LEQConstraint>();

    protected SecurityConstraintValueReadSwitch(Value value,
            AnalyzedMethodEnvironment methodEnvironment, UsedObjectStore store,
            ConstraintsSet in, ConstraintsSet out) {
        super(methodEnvironment, store, in, out);
        value.apply(this);
    }

    protected boolean isMethod() {
        return method != null;
    }

    protected SootMethod getMethod() {
        return method;
    }

    protected boolean usesStaticAccess() {
        return klass != null;
    }

    protected SootClass getStaticClass() {
        return klass;
    }

    protected Set<LEQConstraint> getInheritedWriteEffects() {
        return writeEffects;
    }

    protected void addInheritedWriteEffects(Set<LEQConstraint> writeEffects) {
        writeEffects.addAll(writeEffects);
    }

    @Override
    public void caseDoubleConstant(DoubleConstant v) {
        addReadComponent(getWeakestSecurityLevel());
    }

    @Override
    public void caseFloatConstant(FloatConstant v) {
        addReadComponent(getWeakestSecurityLevel());
    }

    @Override
    public void caseIntConstant(IntConstant v) {
        addReadComponent(getWeakestSecurityLevel());
    }

    @Override
    public void caseLongConstant(LongConstant v) {
        addReadComponent(getWeakestSecurityLevel());
    }

    @Override
    public void caseNullConstant(NullConstant v) {
        addReadComponent(getWeakestSecurityLevel());
    }

    @Override
    public void caseStringConstant(StringConstant v) {
        addReadComponent(getWeakestSecurityLevel());
    }

    @Override
    public void caseClassConstant(ClassConstant v) {
        addReadComponent(getWeakestSecurityLevel());
    }

    @Override
    public void defaultCase(Object object) {
        throwUnknownObjectException(object);
    }

    @Override
    public void caseAddExpr(AddExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseAndExpr(AndExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseCmpExpr(CmpExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseCmpgExpr(CmpgExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseCmplExpr(CmplExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseDivExpr(DivExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseEqExpr(EqExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseNeExpr(NeExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseGeExpr(GeExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseGtExpr(GtExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseLeExpr(LeExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseLtExpr(LtExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseMulExpr(MulExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseOrExpr(OrExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseRemExpr(RemExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseShlExpr(ShlExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseShrExpr(ShrExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseUshrExpr(UshrExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseSubExpr(SubExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseXorExpr(XorExpr v) {
        handleBinaryExpr(v.getOp1(), v.getOp2());
    }

    @Override
    public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
        handleBase(v.getBase());
        handleInvoke(v);
    }

    @Override
    public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
        handleBase(v.getBase());
        handleInvoke(v);
    }

    @Override
    public void caseStaticInvokeExpr(StaticInvokeExpr v) {
        SootClass sootClass = v.getMethod().getDeclaringClass();
        handleStatic(sootClass);
        handleInvoke(v);
        addProgramCounterConstraint(sootClass.getName());
    }

    @Override
    public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
        handleBase(v.getBase());
        handleInvoke(v);
    }

    @Override
    public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
        SootClass sootClass = v.getMethod().getDeclaringClass();
        handleStatic(sootClass);
        handleInvoke(v);
        addProgramCounterConstraint(sootClass.getName());
    }

    @Override
    public void caseCastExpr(CastExpr v) {
        if (v.getCastType() instanceof ArrayType) {
            throw new CastInvalidException(getMsg("exception.analysis.other.cast",
                                                  v.getOp().toString()));
        }
        handleUnaryExpr(v.getOp());
    }

    @Override
    public void caseInstanceOfExpr(InstanceOfExpr v) {
        handleUnaryExpr(v.getOp());
    }

    @Override
    public void caseNewArrayExpr(NewArrayExpr v) {
        throwIllegalNewArrayException(v);
    }

    @Override
    public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
        throwIllegalNewArrayException(v);
    }

    @Override
    public void caseNewExpr(NewExpr v) {
        // Nothing to do...
    }

    @Override
    public void caseLengthExpr(LengthExpr v) {
        handleUnaryExpr(v.getOp());
    }

    @Override
    public void caseNegExpr(NegExpr v) {
        handleUnaryExpr(v.getOp());
    }

    @Override
    public void caseArrayRef(ArrayRef v) {
        Value array = v.getBase();
        Value index = v.getIndex();
        SecurityConstraintValueReadSwitch baseSwitch = getReadSwitch(array);
        SecurityConstraintValueReadSwitch indexSwitch = getReadSwitch(index);
        addInheritedWriteEffects(baseSwitch.getInheritedWriteEffects());
        addInheritedWriteEffects(indexSwitch.getInheritedWriteEffects());
        addReadComponents(baseSwitch.getReadComponents());
        addReadComponents(indexSwitch.getReadComponents());
        setComponentDimension(getDimension(v.getType()));
        if (baseSwitch.getEqualComponents().size() > 0) {
            addReadComponent(baseSwitch.getEqualComponents().get(0));
            for (int i = 1; i < baseSwitch.getEqualComponents().size(); i++) {
                appendEqualComponent(baseSwitch.getEqualComponents().get(1));
            }
        }
    }

    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        SootClass sootClass = v.getField().getDeclaringClass();
        handleStatic(sootClass);
        handleField(v.getField());
        addProgramCounterConstraint(sootClass.getName());
    }

    @Override
    public void caseInstanceFieldRef(InstanceFieldRef v) {
        handleBase(v.getBase());
        handleField(v.getField());
    }

    @Override
    public void caseParameterRef(ParameterRef v) {
        ComponentParameterRef paramRef =
            new ComponentParameterRef(v.getIndex(), getAnalyzedSignature());
        addReadComponent(paramRef);
        handleDimension(v.getType(), paramRef);
    }

    @Override
    public void caseCaughtExceptionRef(CaughtExceptionRef v) {
        throwNotImplementedException(v.getClass(), v.toString());
    }

    @Override
    public void caseThisRef(ThisRef v) {
        addReadComponent(getWeakestSecurityLevel());
    }

    @Override
    public void caseLocal(Local l) {
        ComponentLocal cl = new ComponentLocal(local = l);
        addReadComponent(cl);
        handleDimension(l.getType(), cl);
    }

    private void handleBinaryExpr(Value op1, Value op2) {
        SecurityConstraintValueReadSwitch switchOp1 = getReadSwitch(op1);
        SecurityConstraintValueReadSwitch switchOp2 = getReadSwitch(op2);
        addInheritedWriteEffects(switchOp1.getInheritedWriteEffects());
        addInheritedWriteEffects(switchOp2.getInheritedWriteEffects());
        addReadComponents(switchOp1.getReadComponents());
        addReadComponents(switchOp2.getReadComponents());
        addConstraints(switchOp1.getConstraints());
        addConstraints(switchOp2.getConstraints());
    }

    private void handleUnaryExpr(Value op) {
        SecurityConstraintValueReadSwitch switchOp = getReadSwitch(op);
        addInheritedWriteEffects(switchOp.getInheritedWriteEffects());
        addReadComponents(switchOp.getReadComponents());
        addConstraints(switchOp.getConstraints());
    }

    private void handleStatic(SootClass sootClass) {
        ClassEnvironment ce = getStore().getClassEnvironment(klass = sootClass);
        addConstraints(ce.getSignatureContraints());
        handleInheritedWriteEffect(ce.getSignatureContraints());
    }

    private void handleField(SootField sootField) {
        FieldEnvironment fe = getStore().getFieldEnvironment(field = sootField);
        addReadComponent(fe.getLevel());
        handleFieldDimension(fe);
    }

    private void handleInvoke(InvokeExpr invokeExpr) {
        method = invokeExpr.getMethod();
        MethodEnvironment me = getStore().getMethodEnvironment(method);
        addConstraints(me.getCalleeSignatureConstraints());
        handleInheritedWriteEffect(me.getCalleeSignatureConstraints());
        String signature = me.getCalleeSiganture();
        for (int i = 0; i < invokeExpr.getArgCount(); i++) {
            SecurityConstraintValueReadSwitch argSwitch =
                getReadSwitch(invokeExpr.getArg(i));
            addInheritedWriteEffects(argSwitch.getInheritedWriteEffects());
            addParameterConstraints(argSwitch, signature, i);
            addConstraints(argSwitch.getConstraints());
        }
        addProgramCounterConstraint(signature);
        ComponentReturnRef retRef = new ComponentReturnRef(signature);
        addReadComponent(retRef);
        handleDimension(method.getReturnType(), retRef);
    }

    private void addParameterConstraints(SecurityConstraintValueReadSwitch argSwitch,
                                         String signature,
                                         int position) {
        ComponentParameterRef right =
            new ComponentParameterRef(position, signature);
        for (IComponent left : argSwitch.getReadComponents()) {
            addConstraint(new LEQConstraint(left, right));
        }
        for (int i = 0; i < argSwitch.getEqualComponents().size(); i++) {
            ComponentArrayRef paramArrayRef =
                new ComponentArrayRef(right, i + 1);
            IComponent localArrayRef = argSwitch.getEqualComponents().get(i);
            addConstraint(new LEQConstraint(localArrayRef, paramArrayRef));
            addConstraint(new LEQConstraint(paramArrayRef, localArrayRef));
        }
    }

    private void addProgramCounterConstraint(String signature) {
        addConstraint(new LEQConstraint(new ComponentProgramCounterRef(getAnalyzedSignature()),
                                        new ComponentProgramCounterRef(signature)));
    }

    private void handleBase(Value base) {
        SecurityConstraintValueReadSwitch baseSwitch = getReadSwitch(base);
        addInheritedWriteEffects(baseSwitch.getInheritedWriteEffects());
        addReadComponents(baseSwitch.getReadComponents());
        addConstraints(baseSwitch.getConstraints());
    }

    private void throwIllegalNewArrayException(Value value) {
        throw new IllegalNewArrayException(getMsg("exception.analysis.other.illegal_new_array",
                                                  value.toString()));
    }

    private void handleInheritedWriteEffect(Set<LEQConstraint> constraints) {
        writeEffects.addAll(ConstraintsUtils.getUpdatedPCConstraints(constraints,
                                                                     getAnalyzedSignature()));
    }
}
