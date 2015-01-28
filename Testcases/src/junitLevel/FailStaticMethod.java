package junitLevel;

import static security.Definition.*;

@WriteEffect({})
public class FailStaticMethod {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    @ReturnSecurity("low")
    public static int failingSimpleLowSecurityMethod() {
        // @security("The returned value has a stronger security level than expected.")
        return mkHigh(42);
    }

    @ReturnSecurity("high")
    public static int simpleHighSecurityMethod() {
        return mkHigh(42);
    }

    @ReturnSecurity("low")
    public static int failingInvokeSimpleHighSecurityMethod() {
        // @security("The returned value has a stronger security level than expected.")
        return simpleHighSecurityMethod();
    }

    @ParameterSecurity({ "low" })
    public static void oneLowParameterVoidMethod(int low) {
        return;
    }

    public static void failingInvokeOneLowParameterVoidMethod() {
        int high = mkHigh(42);
        // @security("Security level of argument is stronger than the expected level of the parameter.")
        oneLowParameterVoidMethod(high);
        return;
    }

    @ParameterSecurity({ "low" })
    @ReturnSecurity("low")
    public static int oneLowParameterLowMethod(int low) {
        return low;
    }

    @ReturnSecurity("low")
    public static int failingInvokeOneLowParameterLowMethod() {
        int high = mkHigh(42);
        // @security("Security level of argument is stronger than the expected level of the parameter.")
        int result = oneLowParameterLowMethod(high);
        return result;
    }

    @ParameterSecurity({ "low" })
    @ReturnSecurity("high")
    public static int oneLowParameterHighMethod(int low) {
        return mkHigh(42);
    }

    @ReturnSecurity("high")
    public static int failingInvokeOneLowParameterHighMethod() {
        int high = mkHigh(42);
        // @security("Security level of argument is stronger than the expected level of the parameter.")
        int result = oneLowParameterHighMethod(high);
        return result;
    }

    @ParameterSecurity({ "low", "low" })
    @ReturnSecurity("low")
    public static int twoLowLowParameterLowMethod(int low1, int low2) {
        return low1;
    }

    @ReturnSecurity("low")
    public static int failingInvokeTwoLowLowParameterLowMethod() {
        int high = mkHigh(42);
        int low = mkLow(42);
        // @security("Security level of argument 1 is stronger than the expected level of the parameter.")
        int result = twoLowLowParameterLowMethod(high, low);
        return result;
    }

    @ReturnSecurity("low")
    public static int failingInvokeTwoLowLowParameterLowMethod2() {
        int high = mkHigh(42);
        int low = mkLow(42);
        // @security("Security level of argument 2 is stronger than the expected level of the parameter.")
        int result = twoLowLowParameterLowMethod(low, high);
        return result;
    }

    @ReturnSecurity("low")
    public static int failingInvokeTwoLowLowParameterLowMethod3() {
        int high1 = mkHigh(42);
        int high2 = mkHigh(42);
        // @security("Security level of argument 1 is stronger than the expected level of the parameter.")
        // @security("Security level of argument 2 is stronger than the expected level of the parameter.")
        int result = twoLowLowParameterLowMethod(high1, high2);
        return result;
    }

    @ParameterSecurity({ "low", "high" })
    @ReturnSecurity("low")
    public static int twoLowHighParameterLowMethod(int low, int high) {
        return low;
    }

    @ReturnSecurity("low")
    public static int failingInvokeTwoLowHighParameterLowMethod() {
        int low = mkLow(42);
        int high = mkHigh(42);
        // @security("Security level of argument 1 is stronger than the expected level of the parameter.")
        int result = twoLowHighParameterLowMethod(high, low);
        return result;
    }

    @ReturnSecurity("low")
    public static int failingInvokeTwoLowHighParameterLowMethod2() {
        int high1 = mkHigh(42);
        int high2 = mkHigh(42);
        // @security("Security level of argument 1 is stronger than the expected level of the parameter.")
        int result = twoLowHighParameterLowMethod(high1, high2);
        return result;
    }

    @ParameterSecurity({ "high", "low" })
    @ReturnSecurity("high")
    public static int twoHighLowParameterHighMethod(int high, int low) {
        return high;
    }

    @ReturnSecurity("high")
    public static int failingInvokeTwoHighLowParameterLowMethod() {
        int low = mkLow(42);
        int high = mkHigh(42);
        // @security("Security level of argument 2 is stronger than the expected level of the parameter.")
        int result = twoHighLowParameterHighMethod(low, high);
        return result;
    }

    @ReturnSecurity("high")
    public static int failingInvokeTwoHighLowParameterLowMethod2() {
        int high1 = mkHigh(42);
        int high2 = mkHigh(42);
        // @security("Security level of argument 2 is stronger than the expected level of the parameter.")
        int result = twoHighLowParameterHighMethod(high1, high2);
        return result;
    }

    @ReturnSecurity("low")
    public static int failingInvokeTwoHighLowParameterLowMethod3() {
        int low = mkLow(42);
        int high = mkHigh(42);
        int result = twoHighLowParameterHighMethod(high, low);
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public static int failingInvokeTwoHighLowParameterLowMethod4() {
        int low = mkLow(42);
        int high = mkHigh(42);
        // @security("Security level of argument 2 is stronger than the expected level of the parameter.")
        int result = twoHighLowParameterHighMethod(low, high);
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ReturnSecurity("low")
    public static int failingInvokeTwoHighLowParameterLowMethod5() {
        int high1 = mkHigh(42);
        int high2 = mkHigh(42);
        // @security("Security level of argument 2 is stronger than the expected level of the parameter.")
        int result = twoHighLowParameterHighMethod(high1, high2);
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

    @ParameterSecurity({ "high", "high" })
    @ReturnSecurity("high")
    public static int twoHighHighParameterHighMethod(int high1, int high2) {
        return high1;
    }

    @ReturnSecurity("low")
    public static int failingInvokeTwoHighHighParameterLowMethod() {
        int high1 = mkHigh(42);
        int high2 = mkHigh(42);
        int result = twoHighHighParameterHighMethod(high1, high2);
        // @security("The returned value has a stronger security level than expected.")
        return result;
    }

}
