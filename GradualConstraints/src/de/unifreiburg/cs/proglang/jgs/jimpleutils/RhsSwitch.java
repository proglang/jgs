package de.unifreiburg.cs.proglang.jgs.jimpleutils;

import soot.SootMethod;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.FieldRef;
import soot.jimple.VirtualInvokeExpr;

import java.util.List;
import java.util.Optional;

/**
 * A Value-Switch that abstracts over the concrete expressions and is only
 * concerned with the differences of right-hand-sides that are relevant to
 * information flow. Created by fennell on 11/5/15.
 */
public abstract class RhsSwitch extends AbstractJimpleValueSwitch {

    /**
     * Case of expressions based on local variables, parameters and constants.
     *
     * @param useBoxes The (local) value boxes used by the expression
     */
    public abstract void caseLocalExpr(List<ValueBox> useBoxes);

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

    @Override public void defaultCase(Object v) {
        // TODO: the default is a local expr... this is convenient but might be dangerous
        Value val = (Value) v;
        this.caseLocalExpr(((Value) v).getUseBoxes());
        // throw new RuntimeException("NOT IMPLEMENTED");
    }

    /*
    TODO: More to come.
     */

    @Override public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }
}
