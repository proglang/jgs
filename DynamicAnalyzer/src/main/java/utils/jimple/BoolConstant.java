package utils.jimple;

import soot.BooleanType;
import soot.Type;
import soot.jimple.IntConstant;

public class BoolConstant extends IntConstant {

    protected BoolConstant(boolean b) { super(b ? 1 : 0); }

    public static BoolConstant v(boolean b) { return new BoolConstant(b); }

    @Override
    public Type getType() { return BooleanType.v(); }

    @Override
    public String toString() { return value == 1 ? "true" : "false"; }
}
