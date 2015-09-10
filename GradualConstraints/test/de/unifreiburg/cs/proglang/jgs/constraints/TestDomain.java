package de.unifreiburg.cs.proglang.jgs.constraints;

import java.util.Collection;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh;
import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LowHigh.Level;

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
    public static final TypeDomain<Level>.Type THIGH = types.level(HIGH);
    public static final TypeDomain<Level>.Type TLOW = types.level(LOW);
    public static final TypeDomain<Level>.Type DYN = types.dyn();
    public static final TypeDomain<Level>.Type PUB = types.pub();

    public static TypeDomain<Level>.Type tglb(TypeDomain<Level>.Type l1,
                                                 TypeDomain<Level>.Type l2) {
        return types.glb(l1, l2);
    }

    public static boolean tle(TypeDomain<Level>.Type l1,
                              TypeDomain<Level>.Type l2) {
        return types.le(l1, l2);
    }

    ////////////
    // Constraints
    public static Constraint<Level> leC(CTypeDomain<Level>.CType lhs,
                                           CTypeDomain<Level>.CType rhs) {
        return cstrs.le(lhs, rhs);
    }

    public static Constraint<Level> compC(CTypeDomain<Level>.CType lhs,
                                           CTypeDomain<Level>.CType rhs) {
        return cstrs.comp(lhs, rhs);
    }

    public static Constraint<Level> dimplC(CTypeDomain<Level>.CType lhs,
                                           CTypeDomain<Level>.CType rhs) {
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
