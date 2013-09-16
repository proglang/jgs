package taintTrackingFail;

import security.Annotations;
import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;
import security.SootSecurityLevel;

public class TaintTrackingStaticMethod {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static void failingSimpleVoidMethod() {
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public static void failingSimpleVoidMethod2() {
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingSimpleLowSecurityMethod() {
		return SootSecurityLevel.highId(42);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public static int simpleHighSecurityMethod() {
		return SootSecurityLevel.highId(42);
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeSimpleHighSecurityMethod() {
		return simpleHighSecurityMethod();
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public static void oneLowParameterVoidMethod(int low) {
		return;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("low")
	public static void failingOneLowParameterVoidMethod(int low) {
		return;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public static void failingOneLowParameterVoidMethod2(int low) {
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public static void failingInvokeOneLowParameterVoidMethod() {
		int high = SootSecurityLevel.highId(42);
		oneLowParameterVoidMethod(high);
		return;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("low")
	public static int oneLowParameterLowMethod(int low) {
		return low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeOneLowParameterLowMethod() {
		int high = SootSecurityLevel.highId(42);
		int result = oneLowParameterLowMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public static int oneLowParameterHighMethod(int low) {
		return SootSecurityLevel.highId(42);
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public static int failingInvokeOneLowParameterHighMethod() {
		int high = SootSecurityLevel.highId(42);
		int result = oneLowParameterHighMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("low")
	public static void failingOneHighParameterVoidMethod(int high) {
		return;
	}
	
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("high")
	public static void failingOneHighParameterVoidMethod2(int high) {
		return;
	}
	
	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("low")
	public static int twoLowLowParameterLowMethod(int low1, int low2) {
		return low1;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowLowParameterLowMethod() {
		int high = SootSecurityLevel.highId(42);
		int low = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(high, low);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowLowParameterLowMethod2() {
		int high = SootSecurityLevel.highId(42);
		int low = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(low, high);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowLowParameterLowMethod3() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		int result = twoLowLowParameterLowMethod(high1, high2);
		return result;
	}	
	
	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("low")
	public static int twoLowHighParameterLowMethod(int low, int high) {
		return low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowHighParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoLowHighParameterLowMethod(high, low);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowHighParameterLowMethod2() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		int result = twoLowHighParameterLowMethod(high1, high2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("high")
	public static int twoHighLowParameterHighMethod(int high, int low) {
		return high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public static int failingInvokeTwoHighLowParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoHighLowParameterHighMethod(low, high);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public static int failingInvokeTwoHighLowParameterLowMethod2() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		int result = twoHighLowParameterHighMethod(high1, high2);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoHighLowParameterLowMethod3() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoHighLowParameterHighMethod(high, low);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoHighLowParameterLowMethod4() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoHighLowParameterHighMethod(low, high);
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoHighLowParameterLowMethod5() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		int result = twoHighLowParameterHighMethod(high1, high2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("high")
	public static int twoHighHighParameterHighMethod(int high1, int high2) {
		return high1;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoHighHighParameterLowMethod() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		int result = twoHighHighParameterHighMethod(high1, high2);
		return result;
	}
}
