package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts;
import de.unifreiburg.cs.proglang.jgs.jimpleutils.Var;
import de.unifreiburg.cs.proglang.jgs.util.FunctionsForJava;
import de.unifreiburg.cs.proglang.jgs.util.Interop;
import scala.Option;
import scala.collection.JavaConverters;
import soot.*;
import soot.jimple.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.*;
import static de.unifreiburg.cs.proglang.jgs.jimpleutils.Casts.*;

/**
 * A Value-Switch that abstracts over the concreteConstraints expressions and is only
 * concerned with the differences of right-hand-sides that are relevant to
 * information flow. Created by fennell on 11/5/15.
 */
public abstract class RhsSwitch<Level> extends AbstractJimpleValueSwitch {

    public final Casts<Level> casts;

    protected RhsSwitch(Casts<Level> casts) {
        this.casts = casts;
    }

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
     * @param args    The arguments for the method call. empty arguments are constant (for which we do not care)
     * @param thisPtr variable of the method call receiver
     */
    public abstract void caseCall(SootMethod m,
                                  Option<Var<?>> thisPtr,
                                  List<Option<Var<?>>> args);

    /**
     * Read operation on fields.
     *
     * @param field The field reference that is read
     * @param thisPtr The object from which the field is read
     */
    public abstract void caseGetField(FieldRef field, Option<Var<?>> thisPtr);

    public abstract void caseCast(ValueCast<Level> cast);

    public abstract void caseNew(RefType type);


    /*
     * constants and compound expressions.
     */
    public abstract void caseConstant(Value v);

    private void caseCompoundExpr(Value v) {
        Value val = (Value) v;
        List<Value> useValues = new ArrayList<>();
        for (Object b : val.getUseBoxes()) {
            useValues.add(((ValueBox) b).getValue());
        }
        this.caseLocalExpr(useValues);
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
        // for our purposes, this is a public constant
        caseConstant(v);
    }

    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        caseGetField(v, Option.empty());
    }

    @Override
    public void caseInstanceFieldRef(InstanceFieldRef v) {
        // the base of a field ref is always a local in Jimple
        caseGetField(v, Option.apply(Var.fromLocal((Local)v.getBase())));
    }


    /*
     *  Calls
     */
    private void caseCall(InvokeExpr m, Option<Value> baseValue) {
        Option<Var<?>> base =
                baseValue.isDefined() ? Vars.getAll(baseValue.get()).find(FunctionsForJava.constantTrue()) : Option.empty();
                // baseValue.flatMap(v -> Interop.asJavaStream().findFirst());
        List<Option<Var<?>>> args = new ArrayList<>();
        for (soot.Value v : m.getArgs()) {
            List<Var<?>> vars = (List<Var<?>>)JavaConverters.seqAsJavaListConverter(Vars.getAll(v).toSeq()).asJava();
            if (vars.isEmpty()) {
                args.add(Option.empty());
            } else if (vars.size() == 1) {
                args.add(Option.<Var<?>>apply(vars.get(0)));
            } else {
                throw new RuntimeException("Unexpected: multiple variables contained in a call argumnent");
            }
        }
        caseCall(m.getMethod(), base, args);
    }

    @Override public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
        caseCall(v, Option.apply(v.getBase()));
    }

    @Override
    public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
        caseCall(v, Option.apply(v.getBase()));
    }

    @Override
    public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
        caseCall(v, Option.apply(v.getBase()));
    }

    @Override
    public void caseStaticInvokeExpr(StaticInvokeExpr v) {

        Option<ValueCast<Level>> c = casts.detectValueCastFromCall(v);
        if (c.isDefined()) {
            caseCast(c.get());
        } else {
            caseCall(v, Option.empty());
        }
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

    @Override
    public void caseCastExpr(CastExpr v) {
        caseLocal((Local)v.getOp());
    }

    /*
     * TODO-missing-cases: misc cases
     */


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
        caseNew(v.getBaseType());
    }

    @Override
    public void caseLengthExpr(LengthExpr v) {
        super.caseLengthExpr(v);
    }

    @Override
    public void caseCaughtExceptionRef(CaughtExceptionRef v) {
        super.caseCaughtExceptionRef(v);
    }
}
