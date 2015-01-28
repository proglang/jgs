package junitLevel;

import static security.Definition.*;

public class FailObject {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low", "high" })
    public int accessFieldLow() {
        FieldObject fo = new FieldObject();
        fo = mkHigh(fo);
        // @security("The returned value has a stronger security level than expected.")
        return fo.low;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low", "high" })
    public int accessFieldHigh() {
        FieldObject fo = new FieldObject();
        fo = mkLow(fo);
        // @security("The returned value has a stronger security level than expected.")
        return fo.high;
    }

    @ReturnSecurity("low")
    @WriteEffect({ "low", "high" })
    public int accessFieldHigh2() {
        FieldObject fo = new FieldObject();
        fo = mkHigh(fo);
        // @security("The returned value has a stronger security level than expected.")
        return fo.high;
    }

    @ReturnSecurity("low")
    public int accessStaticFieldHigh() {
        // @security("The returned value has a stronger security level than expected.")
        return StaticFieldObject.high;
    }

    @WriteEffect({ "low", "high" })
    public void assignFieldLow() {
        int high = mkHigh(42);
        FieldObject fo = new FieldObject();
        // @security("The security level of the assigned value is stronger than the security level of the field.")
        fo.low = high;
        return;
    }

    @WriteEffect({ "low", "high" })
    public void assignFieldLow2() {
        int high = mkHigh(42);
        FieldObject fo = new FieldObject();
        fo = mkHigh(fo);
        // @security("The security level of the assigned value is stronger than the security level of the field.")
        fo.low = high;
        return;
    }

    @WriteEffect({ "low", "high" })
    public void assignStaticFieldLow() {
        int high = mkHigh(42);
        // @security("The security level of the assigned value is stronger than the security level of the field.")
        StaticFieldObject.low = high;
        return;
    }

    @ReturnSecurity("low")
    public int invokeSimpleLowMethod() {
        MethodObject mo = new MethodObject();
        mo = mkHigh(mo);
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnLowSecurity();
    }

    @ReturnSecurity("low")
    public int invokeSimpleHighMethod() {
        MethodObject mo = new MethodObject();
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnHighSecurity();
    }

    @ReturnSecurity("low")
    public int invokeSimpleHighMethod2() {
        MethodObject mo = new MethodObject();
        mo = mkLow(mo);
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnHighSecurity();
    }

    @ReturnSecurity("low")
    public int invokeSimpleHighMethod3() {
        MethodObject mo = new MethodObject();
        mo = mkHigh(mo);
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnHighSecurity();
    }

    @ReturnSecurity("low")
    public int invokeLowMethod() {
        int low = mkLow(42);
        MethodObject mo = new MethodObject();
        mo = mkHigh(mo);
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnLowSecurity(low);
    }

    @ReturnSecurity("low")
    public int invokeHighMethod() {
        int high = mkHigh(42);
        MethodObject mo = new MethodObject();
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnHighSecurity(high);
    }

    @ReturnSecurity("low")
    public int invokeHighMethod2() {
        int low = mkLow(42);
        MethodObject mo = new MethodObject();
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnHighSecurity(low);
    }

    @ReturnSecurity("low")
    public int invokeHighMethod3() {
        int high = mkHigh(42);
        MethodObject mo = new MethodObject();
        mo = mkLow(mo);
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnHighSecurity(high);
    }

    @ReturnSecurity("low")
    public int invokeHighMethod4() {
        int high = mkHigh(42);
        MethodObject mo = new MethodObject();
        mo = mkHigh(mo);
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnHighSecurity(high);
    }

    @ReturnSecurity("low")
    public int invokeHighMethod5() {
        int low = mkLow(42);
        MethodObject mo = new MethodObject();
        mo = mkHigh(mo);
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnHighSecurity(low);
    }

    @ReturnSecurity("low")
    public int invokeHighMethod6() {
        int high = mkHigh(42);
        MethodObject mo = new MethodObject();
        // @security("Security level of argument is stronger than the expected level of the parameter.")
        mo = mkHigh(mo);
        // @security("The returned value has a stronger security level than expected.")
        return mo.returnLowSecurity(high);
    }

    @ReturnSecurity("low")
    public int invokeHighMethod7() {
        int high = mkHigh(42);
        MethodObject mo = new MethodObject();
        // @security("Security level of argument is stronger than the expected level of the parameter.")
        mo = mkLow(mo);
        return mo.returnLowSecurity(high);
    }

    @ReturnSecurity("low")
    public int invokeSimpleLowStaticMethod() {
        // @security("The returned value has a stronger security level than expected.")
        return StaticMethodObject.returnHighSecurity();
    }

    @ReturnSecurity("low")
    public int invokeLowStaticMethod() {
        // @security("Security level of argument is stronger than the expected level of the parameter.")
        int high = mkHigh(42);
        return StaticMethodObject.returnLowSecurity(high);
    }

    @ReturnSecurity("high")
    public int invokeLowStaticMethod2() {
        // @security("Security level of argument is stronger than the expected level of the parameter.")
        int high = mkHigh(42);
        return StaticMethodObject.returnLowSecurity(high);
    }

    @ReturnSecurity("low")
    public int invokeHighStaticMethod() {
        int low = mkLow(42);
        // @security("The returned value has a stronger security level than expected.")
        return StaticMethodObject.returnHighSecurity(low);
    }

    public static class FieldObject {

        @FieldSecurity("low")
        public int low = 42;

        @FieldSecurity("high")
        public int high = 42;

        @WriteEffect({ "low", "high" })
        public FieldObject() {
            super();
        }

    }

    @WriteEffect({ "low", "high" })
    public static class StaticFieldObject {

        @FieldSecurity("low")
        public static int low = 42;

        @FieldSecurity("high")
        public static int high = 42;

    }

    public static class MethodObject {

        @ReturnSecurity("low")
        public int returnLowSecurity() {
            return mkLow(42);
        }

        @ReturnSecurity("high")
        public int returnHighSecurity() {
            return mkHigh(42);
        }

        @ParameterSecurity({ "low" })
        @ReturnSecurity("low")
        public int returnLowSecurity(int low) {
            return low;
        }

        @ParameterSecurity({ "high" })
        @ReturnSecurity("high")
        public int returnHighSecurity(int high) {
            return high;
        }

    }

    public static class StaticMethodObject {

        @ReturnSecurity("low")
        public static int returnLowSecurity() {
            return mkLow(42);
        }

        @ReturnSecurity("high")
        public static int returnHighSecurity() {
            return mkHigh(42);
        }

        @ParameterSecurity({ "low" })
        @ReturnSecurity("low")
        public static int returnLowSecurity(int low) {
            return low;
        }

        @ParameterSecurity({ "high" })
        @ReturnSecurity("high")
        public static int returnHighSecurity(int high) {
            return high;
        }

    }

}