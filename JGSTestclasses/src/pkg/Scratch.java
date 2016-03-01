package pkg;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class Scratch {

    int answer = 42;

    @Sec("HIGH")
    int high = 123;

    @Sec("HIGH")
    Object highObject = new Object();


    static int aStatic = 0;

    @Constraints({"@0 <= LOW", "@0 <= @ret"})
    @Effects({})
    static int aStaticMethod(int i) {
        if (i == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Effects({"pub"})
    void conflicts() {
        this.answer = this.high;
    }

    static int returnAStatic() {
        return aStatic;
    }

    @Constraints({"? <= @ret"})
    Object returnDynWithCast() {
        return Casts.castHighToDyn(this.highObject);
    }

    @Constraints({"? <= @ret"})
    Object returnDynWithCast_illegal() {
        return Casts.castLowToDyn(this.highObject);
    }

    @Constraints({"? <= @ret"})
    int returnFromDynCastCx() {
        int result;
        if (this.high == 1) {
            Casts.castCxHighToDyn();
            result = 1;
            Casts.castCxEnd();
        } else {
            Casts.castCxHighToDyn();
            result = 0;
            Casts.castCxEnd();
        }
        return result;
    }

    @Constraints({"? <= @ret"})
    int returnFromDynCastCx_shorter() {
        int result = 0;
        if (this.high == 1) {
            Casts.castCxHighToDyn();
            result = 1;
            Casts.castCxEnd();
        }
        return result;
    }

    @Constraints({"? <= @ret"})
    int returnFromDynCastCx_illegalInIf() {
        int result;
        if (this.high == 1) {
            Casts.castCxLowToDyn();
            result = 1;
            Casts.castCxEnd();
        } else {
            Casts.castCxHighToDyn();
            result = 0;
            Casts.castCxEnd();
        }
        return result;
    }

    @Constraints({"? <= @ret"})
    int returnFromDynCastCx_illegalInJoin() {
        int result = this.high;
        if (this.high == 1) {
            Casts.castCxHighToDyn();
            result = 1;
            Casts.castCxEnd();
        }
        return result;
    }

    @Constraints({"@0 <= @ret"})
    public int aVirtualMethod(int i) {
        if (i == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Constraints({"pub <= @ret"})
    public int aVirtualMethodWithForgottenImplicitFlows(int i, int j) {
        if (i == 0) {
            return 1;
        }
        if (j == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Constraints({"@0 <= @ret", "@1 <= @ret"})
    public int methodWithImplicitFlows(boolean i, boolean j) {
        int z = 0;
        if (i) {
            z += 10;
        }
        if (j) {
            z += 1;
        }
        return z;
    }

    @Constraints({"@0 ~ @1"})
    public int methodWithDeadCode(boolean i, boolean j) {
        int z = 0;
        if (i) {
            z += 10;
        }
        if (j) {
            z += 1;
        }
        return 0;
    }

    public int methodWithDeadCode_missingCompatibiliy(boolean i, boolean j) {
        int z = 0;
        if (i) {
            z += 10;
        }
        if (j) {
            z += 1;
        }
        return 0;
    }

    @Constraints({"@0 <= @ret"})
    int aVirtualMethodUsingThis(int i) {
        return i + this.answer;
    }

    int aVirtualMethodUsingThisWhereSignatureWasForgotten(int i) {
        return i + this.answer;
    }

}
