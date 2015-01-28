package junitLevel;

import static security.Definition.*;

public class SuccessStaticMethod {

    @ParameterSecurity({ "low" })
    public static void main(String[] args) {
    }

    public static void simpleVoidMethod() {
        return;
    }

    public static void invokeSimpleVoidMethod() {
        simpleVoidMethod();
        return;
    }

    @ReturnSecurity("low")
    public static int simpleLowSecurityMethod() {
        return mkLow(42);
    }

    @ReturnSecurity("low")
    public static int invokeSimpleLowSecurityMethod() {
        return simpleLowSecurityMethod();
    }

    @ReturnSecurity("high")
    public static int simpleHighSecurityMethod() {
        return mkHigh(42);
    }

    @ReturnSecurity("high")
    public static int invokeSimpleHighSecurityMethod() {
        return simpleHighSecurityMethod();
    }

    @ReturnSecurity("high")
    public static int invokeSimpleHighSecurityMethod2() {
        return simpleLowSecurityMethod();
    }

    @ParameterSecurity({ "low" })
    public static void oneLowParameterVoidMethod(int low) {
        return;
    }

    public static void invokeOneLowParameterVoidMethod() {
        int low = mkLow(42);
        oneLowParameterVoidMethod(low);
        return;
    }

    @ParameterSecurity({ "low" })
    @ReturnSecurity("low")
    public static int oneLowParameterLowMethod(int low) {
        return low;
    }

    @ReturnSecurity("low")
    public static int invokeOneLowParameterLowMethod() {
        int low = mkLow(42);
        int result = oneLowParameterLowMethod(low);
        return result;
    }

    @ParameterSecurity({ "low" })
    @ReturnSecurity("high")
    public static int oneLowParameterHighMethod(int low) {
        return mkHigh(42);
    }

    @ReturnSecurity("high")
    public static int invokeOneLowParameterHighMethod() {
        int low = mkLow(42);
        int result = oneLowParameterHighMethod(low);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeOneLowParameterHighMethod2() {
        int low = mkLow(42);
        int result = oneLowParameterLowMethod(low);
        return result;
    }

    @ParameterSecurity({ "high" })
    public static void oneHighParameterVoidMethod(int high) {
        return;
    }

    public static void invokeOneHighParameterVoidMethod() {
        int high = mkHigh(42);
        oneHighParameterVoidMethod(high);
        return;
    }

    public static void invokeOneHighParameterVoidMethod2() {
        int low = mkLow(42);
        oneHighParameterVoidMethod(low);
        return;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("low")
    public static int oneHighParameterLowMethod(int high) {
        return mkLow(42);
    }

    @ReturnSecurity("low")
    public static int invokeOneHighParameterLowMethod() {
        int high = mkHigh(42);
        int result = oneHighParameterLowMethod(high);
        return result;
    }

    @ReturnSecurity("low")
    public static int invokeOneHighParameterLowMethod2() {
        int low = mkLow(42);
        int result = oneHighParameterLowMethod(low);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeOneHighParameterLowMethod3() {
        int high = mkHigh(42);
        int result = oneHighParameterLowMethod(high);
        return result;
    }

    @ParameterSecurity({ "high" })
    @ReturnSecurity("high")
    public static int oneHighParameterHighMethod(int high) {
        return high;
    }

    @ReturnSecurity("high")
    public static int invokeOneHighParameterHighMethod() {
        int high = mkHigh(42);
        int result = oneHighParameterHighMethod(high);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeOneHighParameterHighMethod2() {
        int low = mkLow(42);
        int result = oneHighParameterHighMethod(low);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeOneHighParameterHighMethod3() {
        int high = mkHigh(42);
        int result = oneHighParameterLowMethod(high);
        return result;
    }

    @ParameterSecurity({ "low", "low" })
    @ReturnSecurity("low")
    public static int twoLowLowParameterLowMethod(int low1, int low2) {
        return low1;
    }

    @ReturnSecurity("low")
    public static int invokeTwoLowLowParameterLowMethod() {
        int low1 = mkLow(42);
        int low2 = mkLow(42);
        int result = twoLowLowParameterLowMethod(low1, low2);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeTwoLowLowParameterLowMethod2() {
        int low1 = mkLow(42);
        int low2 = mkLow(42);
        int result = twoLowLowParameterLowMethod(low1, low2);
        return result;
    }

    @ParameterSecurity({ "low", "high" })
    @ReturnSecurity("low")
    public static int twoLowHighParameterLowMethod(int low, int high) {
        return low;
    }

    @ReturnSecurity("low")
    public static int invokeTwoLowHighParameterLowMethod() {
        int low = mkLow(42);
        int high = mkHigh(42);
        int result = twoLowHighParameterLowMethod(low, high);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeTwoLowHighParameterLowMethod2() {
        int low = mkLow(42);
        int high = mkHigh(42);
        int result = twoLowHighParameterLowMethod(low, high);
        return result;
    }

    @ReturnSecurity("low")
    public static int invokeTwoLowHighParameterLowMethod3() {
        int low1 = mkLow(42);
        int low2 = mkLow(42);
        int result = twoLowHighParameterLowMethod(low1, low2);
        return result;
    }

    @ParameterSecurity({ "high", "low" })
    @ReturnSecurity("high")
    public static int twoHighLowParameterHighMethod(int high, int low) {
        return high;
    }

    @ReturnSecurity("high")
    public static int invokeTwoHighLowParameterLowMethod() {
        int low = mkLow(42);
        int high = mkHigh(42);
        int result = twoHighLowParameterHighMethod(high, low);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeTwoHighLowParameterLowMethod2() {
        int low1 = mkLow(42);
        int low2 = mkLow(42);
        int result = twoHighLowParameterHighMethod(low1, low2);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeTwoHighLowParameterLowMethod3() {
        int low1 = mkLow(42);
        int low2 = mkLow(42);
        int result = twoLowLowParameterLowMethod(low1, low2);
        return result;
    }

    @ParameterSecurity({ "high", "high" })
    @ReturnSecurity("high")
    public static int twoHighHighParameterHighMethod(int high1, int high2) {
        return high1;
    }

    @ReturnSecurity("high")
    public static int invokeTwoHighHighParameterLowMethod() {
        int high1 = mkHigh(42);
        int high2 = mkHigh(42);
        int result = twoHighHighParameterHighMethod(high1, high2);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeTwoHighHighParameterLowMethod2() {
        int low1 = mkLow(42);
        int low2 = mkLow(42);
        int result = twoHighHighParameterHighMethod(low1, low2);
        return result;
    }

    @ReturnSecurity("high")
    public static int invokeTwoHighHighParameterLowMethod3() {
        int low1 = mkLow(42);
        int low2 = mkLow(42);
        int result = twoLowLowParameterLowMethod(low1, low2);
        return result;
    }

}
