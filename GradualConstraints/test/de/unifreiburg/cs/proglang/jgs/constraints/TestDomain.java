package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;

import de.unifreiburg.cs.proglang.jgs.constraints.CTypeDomain.CType;
import de.unifreiburg.cs.proglang.jgs.constraints.TypeDomain.Type;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;
import de.unifreiburg.cs.proglang.jgs.typing.Typing;

public class TestDomain {

    public static final LowHigh levels = new LowHigh();
    public static final TypeDomain<Level> types = new TypeDomain<>(levels);
    public static final Constraints<Level> cstrs = new Constraints<>(types);

    //////
    // Levels
    public static final Level HIGH = levels.top();
    public static final Level LOW = levels.bottom();

    public static Level lub(Level l1, Level l2) {
        return levels.lub(l1, l2);
    }

    public static Level glb(Level l1, Level l2) {
        return levels.glb(l1, l2);
    }

    public static boolean le(Level l1, Level l2) {
        return levels.le(l1, l2);
    }

    ///////
    // Types
    public static final Type<Level> THIGH = types.level(HIGH);
    public static final Type<Level> TLOW = types.level(LOW);
    public static final Type<Level> DYN = types.dyn();
    public static final Type<Level> PUB = types.pub();

    public static Type<Level> tglb(Type<Level> l1,
                                                 Type<Level> l2) {
        return types.glb(l1, l2);
    }

    public static boolean tle(Type<Level> l1,
                              Type<Level> l2) {
        return types.le(l1, l2);
    }

    ////////////
    // Constraints
    public static Constraint<Level> leC(CType<Level> lhs,
                                           CType<Level> rhs) {
        return cstrs.le(lhs, rhs);
    }

    public static Constraint<Level> compC(CType<Level> lhs,
                                           CType<Level> rhs) {
        return cstrs.comp(lhs, rhs);
    }

    public static Constraint<Level> dimplC(CType<Level> lhs,
                                           CType<Level> rhs) {
        return cstrs.dimpl(lhs, rhs);
    }
    
    //////////////
    // Constraint sets
    public static ConstraintSet<Level> makeNaive(Collection<Constraint<Level>> cs) {
        return new NaiveConstraints<>(types, cs);
    }
    
    ///////
    // Typing
    public static Typing<Level> mkTyping(CTypeDomain<Level> ctypes) {
        return new Typing<>(ctypes, types);
    }
    
}
