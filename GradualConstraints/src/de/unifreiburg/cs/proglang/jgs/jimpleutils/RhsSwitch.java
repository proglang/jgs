package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import soot.Local;
import soot.SootMethod;
import soot.Value;
import soot.ValueBox;
import soot.jimple.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

/**
 * A Value-Switch that abstracts over the concrete expressions and is only
 * concerned with the differences of right-hand-sides that are relevant to
 * information flow. Created by fennell on 11/5/15.
 */
public abstract class RhsSwitch extends AbstractJimpleValueSwitch {

    /**
     * Case of expressions based on local variables, parameters and constants.
     *
     * @param atoms The atomic expressions contained in the expression
     */
    public abstract void caseLocalExpr(Collection<Value> atoms);

    /**
     * All kinds of method calls.
     *
     * @param m       The method which is called.
     * @param args    The arguments for the method call, including
     * @param thisPtr variable of the method call receiver
     */
    public abstract void caseCall(SootMethod m,
                                  Optional<Var<?>> thisPtr,
                                  List<Var<?>> args);

    /**
     * Read operation on fields.
     *
     * @param field The field reference that is read
     * @param thisPtr The object from which the field is read
     */
    public abstract void caseGetField(FieldRef field, Optional<Var<?>> thisPtr);


    /*
     * Utility methods for constants and compound expressions.
     */
    private void caseConstant(Value v) {
        // do nothing
    }

    private void caseCompoundExpr(Value v) {
        Value val = (Value) v;
        this.caseLocalExpr(val.getUseBoxes());
    }

    @Override public void defaultCase(Object v) {
        throw new RuntimeException(String.format("Case for %s NOT IMPLEMENTED", v.toString()));
    }

    /*
     ************************* Cases *****************************
     */

    /*
     * References
     */
    @Override
    public void caseLocal(Local v) {
        caseLocalExpr(singleton(v));
    }

    @Override
    public void caseParameterRef(ParameterRef v) {
        caseLocalExpr(singleton(v));
    }

    @Override public void caseArrayRef(ArrayRef v) {
        super.caseArrayRef(v);
    }

    @Override
    public void caseThisRef(ThisRef v) {
        super.caseThisRef(v);
    }

    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        super.caseStaticFieldRef(v);
    }

    /*
     *  Calls
     */
    private void caseCall(InvokeExpr m, Optional<Value> baseValue) {
        Optional<Var<?>> base =
                baseValue.flatMap(v -> Var.getAll(v).findFirst());
        List<Var<?>> args =
                Var.getAllFromValues(m.getArgs()).collect(toList());
        caseCall(m.getMethod(), base, args);
    }
    @Override public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
        caseCall(v, Optional.of(v.getBase()));
    }

    @Override
    public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
        caseCall(v, Optional.of(v.getBase()));
    }

    @Override
    public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
        caseCall(v, Optional.of(v.getBase()));
    }

    @Override
    public void caseStaticInvokeExpr(StaticInvokeExpr v) {
        caseCall(v, Optional.empty());
    }

    @Override
    public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    /*
     *  Constants
     */

    @Override
    public void caseDoubleConstant(DoubleConstant v) {
        caseConstant(v);
    }

    @Override
    public void caseFloatConstant(FloatConstant v) {
        caseConstant(v);
    }

    @Override
    public void caseIntConstant(IntConstant v) {
        caseConstant(v);
    }

    @Override
    public void caseLongConstant(LongConstant v) {
        caseConstant(v);
    }

    @Override
    public void caseNullConstant(NullConstant v) {
        caseConstant(v);
    }

    @Override
    public void caseStringConstant(StringConstant v) {
        caseConstant(v);
    }

    @Override
    public void caseClassConstant(ClassConstant v) {
        caseConstant(v);
    }

    /*
     * Compound expressions
     */
    @Override
    public void caseAddExpr(AddExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseAndExpr(AndExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseCmpExpr(CmpExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseCmpgExpr(CmpgExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseCmplExpr(CmplExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseDivExpr(DivExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseEqExpr(EqExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseGeExpr(GeExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseGtExpr(GtExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseLeExpr(LeExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseLtExpr(LtExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseMulExpr(MulExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseNeExpr(NeExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseOrExpr(OrExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseRemExpr(RemExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseShlExpr(ShlExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseShrExpr(ShrExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseSubExpr(SubExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseUshrExpr(UshrExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseXorExpr(XorExpr v) {
        caseCompoundExpr(v);
    }

    @Override
    public void caseNegExpr(NegExpr v) {
        super.caseNegExpr(v);
    }


    /*
     * TODO: misc cases
     */

    @Override
    public void caseCastExpr(CastExpr v) {
        super.caseCastExpr(v);
    }

    @Override
    public void caseInstanceOfExpr(InstanceOfExpr v) {
        super.caseInstanceOfExpr(v);
    }

    @Override
    public void caseNewArrayExpr(NewArrayExpr v) {
        super.caseNewArrayExpr(v);
    }

    @Override
    public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
        super.caseNewMultiArrayExpr(v);
    }

    @Override
    public void caseNewExpr(NewExpr v) {
        super.caseNewExpr(v);
    }

    @Override
    public void caseLengthExpr(LengthExpr v) {
        super.caseLengthExpr(v);
    }
    @Override
    public void caseInstanceFieldRef(InstanceFieldRef v) {
        super.caseInstanceFieldRef(v);
    }

    @Override
    public void caseCaughtExceptionRef(CaughtExceptionRef v) {
        super.caseCaughtExceptionRef(v);
    }
}
