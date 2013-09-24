package taintTrackingSuccess;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingStaticMethod {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public static void simpleVoidMethod() {
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public static void invokeSimpleVoidMethod() {
		simpleVoidMethod();
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int simpleLowSecurityMethod() {
		return SootSecurityLevel.lowId(42);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int invokeSimpleLowSecurityMethod() {
		return simpleLowSecurityMethod();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int simpleHighSecurityMethod() {
		return SootSecurityLevel.highId(42);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeSimpleHighSecurityMethod() {
		return simpleHighSecurityMethod();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeSimpleHighSecurityMethod2() {
		return simpleLowSecurityMethod();
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public static void oneLowParameterVoidMethod(int low) {
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public static void invokeOneLowParameterVoidMethod() {
		int low = SootSecurityLevel.lowId(42);
		oneLowParameterVoidMethod(low);
		return;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int oneLowParameterLowMethod(int low) {
		return low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int invokeOneLowParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneLowParameterLowMethod(low);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int oneLowParameterHighMethod(int low) {
		return SootSecurityLevel.highId(42);
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeOneLowParameterHighMethod() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneLowParameterHighMethod(low);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeOneLowParameterHighMethod2() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneLowParameterLowMethod(low);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public static void oneHighParameterVoidMethod(int high) {
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public static void invokeOneHighParameterVoidMethod() {
		int high = SootSecurityLevel.highId(42);
		oneHighParameterVoidMethod(high);
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public static void invokeOneHighParameterVoidMethod2() {
		int low = SootSecurityLevel.lowId(42);
		oneHighParameterVoidMethod(low);
		return;
	}
	
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int oneHighParameterLowMethod(int high) {
		return SootSecurityLevel.lowId(42);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int invokeOneHighParameterLowMethod() {
		int high = SootSecurityLevel.highId(42);
		int result = oneHighParameterLowMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int invokeOneHighParameterLowMethod2() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneHighParameterLowMethod(low);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeOneHighParameterLowMethod3() {
		int high = SootSecurityLevel.highId(42);
		int result = oneHighParameterLowMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int oneHighParameterHighMethod(int high) {
		return high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeOneHighParameterHighMethod() {
		int high = SootSecurityLevel.highId(42);
		int result = oneHighParameterHighMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeOneHighParameterHighMethod2() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneHighParameterHighMethod(low);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeOneHighParameterHighMethod3() {
		int high = SootSecurityLevel.highId(42);
		int result = oneHighParameterLowMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int twoLowLowParameterLowMethod(int low1, int low2) {
		return low1;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int invokeTwoLowLowParameterLowMethod() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeTwoLowLowParameterLowMethod2() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int twoLowHighParameterLowMethod(int low, int high) {
		return low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int invokeTwoLowHighParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoLowHighParameterLowMethod(low, high);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeTwoLowHighParameterLowMethod2() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoLowHighParameterLowMethod(low, high);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public static int invokeTwoLowHighParameterLowMethod3() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowHighParameterLowMethod(low1, low2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int twoHighLowParameterHighMethod(int high, int low) {
		return high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeTwoHighLowParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoHighLowParameterHighMethod(high, low);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeTwoHighLowParameterLowMethod2() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoHighLowParameterHighMethod(low1, low2);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeTwoHighLowParameterLowMethod3() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int twoHighHighParameterHighMethod(int high1, int high2) {
		return high1;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeTwoHighHighParameterLowMethod() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		int result = twoHighHighParameterHighMethod(high1, high2);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeTwoHighHighParameterLowMethod2() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoHighHighParameterHighMethod(low1, low2);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public static int invokeTwoHighHighParameterLowMethod3() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
}
