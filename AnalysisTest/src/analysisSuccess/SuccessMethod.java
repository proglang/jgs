package analysisSuccess;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

public class SuccessMethod {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	public void simpleVoidMethod() {
		return;
	}
	
	public void invokeSimpleVoidMethod() {
		simpleVoidMethod();
		return;
	}
	
	@Annotations.ReturnSecurity("low")
	public int simpleLowSecurityMethod() {
		return SootSecurityLevel.lowId(42);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeSimpleLowSecurityMethod() {
		return simpleLowSecurityMethod();
	}
	
	@Annotations.ReturnSecurity("high")
	public int simpleHighSecurityMethod() {
		return SootSecurityLevel.highId(42);
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleHighSecurityMethod() {
		return simpleHighSecurityMethod();
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeSimpleHighSecurityMethod2() {
		return simpleLowSecurityMethod();
	}
	
	@Annotations.ParameterSecurity({"low"})
	public void oneLowParameterVoidMethod(int low) {
		return;
	}
	
	public void invokeOneLowParameterVoidMethod() {
		int low = SootSecurityLevel.lowId(42);
		oneLowParameterVoidMethod(low);
		return;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("low")
	public int oneLowParameterLowMethod(int low) {
		return low;
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeOneLowParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneLowParameterLowMethod(low);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int oneLowParameterHighMethod(int low) {
		return SootSecurityLevel.highId(42);
	}

	@Annotations.ReturnSecurity("high")
	public int invokeOneLowParameterHighMethod() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneLowParameterHighMethod(low);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeOneLowParameterHighMethod2() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneLowParameterLowMethod(low);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high"})
	public void oneHighParameterVoidMethod(int high) {
		return;
	}
	
	public void invokeOneHighParameterVoidMethod() {
		int high = SootSecurityLevel.highId(42);
		oneHighParameterVoidMethod(high);
		return;
	}
	
	public void invokeOneHighParameterVoidMethod2() {
		int low = SootSecurityLevel.lowId(42);
		oneHighParameterVoidMethod(low);
		return;
	}
	
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("low")
	public int oneHighParameterLowMethod(int high) {
		return SootSecurityLevel.lowId(42);
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeOneHighParameterLowMethod() {
		int high = SootSecurityLevel.highId(42);
		int result = oneHighParameterLowMethod(high);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeOneHighParameterLowMethod2() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneHighParameterLowMethod(low);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeOneHighParameterLowMethod3() {
		int high = SootSecurityLevel.highId(42);
		int result = oneHighParameterLowMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("high")
	public int oneHighParameterHighMethod(int high) {
		return high;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeOneHighParameterHighMethod() {
		int high = SootSecurityLevel.highId(42);
		int result = oneHighParameterHighMethod(high);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeOneHighParameterHighMethod2() {
		int low = SootSecurityLevel.lowId(42);
		int result = oneHighParameterHighMethod(low);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeOneHighParameterHighMethod3() {
		int high = SootSecurityLevel.highId(42);
		int result = oneHighParameterLowMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("low")
	public int twoLowLowParameterLowMethod(int low1, int low2) {
		return low1;
	}

	@Annotations.ReturnSecurity("low")
	public int invokeTwoLowLowParameterLowMethod() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeTwoLowLowParameterLowMethod2() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("low")
	public int twoLowHighParameterLowMethod(int low, int high) {
		return low;
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeTwoLowHighParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoLowHighParameterLowMethod(low, high);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeTwoLowHighParameterLowMethod2() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoLowHighParameterLowMethod(low, high);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int invokeTwoLowHighParameterLowMethod3() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowHighParameterLowMethod(low1, low2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("high")
	public int twoHighLowParameterHighMethod(int high, int low) {
		return high;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeTwoHighLowParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoHighLowParameterHighMethod(high, low);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeTwoHighLowParameterLowMethod2() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoHighLowParameterHighMethod(low1, low2);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeTwoHighLowParameterLowMethod3() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("high")
	public int twoHighHighParameterHighMethod(int high1, int high2) {
		return high1;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeTwoHighHighParameterLowMethod() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		int result = twoHighHighParameterHighMethod(high1, high2);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeTwoHighHighParameterLowMethod2() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoHighHighParameterHighMethod(low1, low2);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int invokeTwoHighHighParameterLowMethod3() {
		int low1 = SootSecurityLevel.lowId(42);
		int low2 = SootSecurityLevel.lowId(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
}
