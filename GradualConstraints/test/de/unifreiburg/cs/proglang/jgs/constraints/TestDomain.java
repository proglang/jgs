package de.unifreiburg.cs.proglang.jgs.constraints;

import de.unifreiburg.cs.proglang.jgs.constraints.secdomains.LH;

public class TestDomain {

    private static final LH levels = new LH();
    private static final TypeDomain<LH.Level> types = new TypeDomain<>(levels);
    private static final Constraints<LH.Level> cstrs = new Constraints<>(types);

    public static LH.Level lub(LH.Level l1, LH.Level l2) {
        return levels.lub(l1, l2);
    }

    public static LH.Level glb(LH.Level l1, LH.Level l2) {
        return levels.glb(l1, l2);
    }

    public static boolean le(LH.Level l1, LH.Level l2) {
        return levels.le(l1, l2);
    }

    public static TypeDomain<LH.Level>.Type tglb(TypeDomain<LH.Level>.Type l1,
                                                 TypeDomain<LH.Level>.Type l2) {
        return types.glb(l1, l2);
    }

    public static boolean tle(TypeDomain<LH.Level>.Type l1,
                              TypeDomain<LH.Level>.Type l2) {
        return types.le(l1, l2);
    }

    public static Constraint<LH.Level> leC(CTypeDomain<LH.Level>.CType lhs,
                                           CTypeDomain<LH.Level>.CType rhs) {
        return cstrs.le(lhs, rhs);
    }

    public static final LH.Level HIGH = levels.top();
    public static final LH.Level LOW = levels.bottom();
    public static final TypeDomain<LH.Level>.Type THIGH = types.level(HIGH);
    public static final TypeDomain<LH.Level>.Type TLOW = types.level(LOW);
    public static final TypeDomain<LH.Level>.Type DYN = types.dyn();
    public static final TypeDomain<LH.Level>.Type PUB = types.pub();

}
