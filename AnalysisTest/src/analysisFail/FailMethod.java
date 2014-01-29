package analysisFail;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

public class FailMethod {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@Annotations.ReturnSecurity("low")
	public int failingSimpleLowSecurityMethod() {
		// @security("The returned value has a stronger security level than expected.")
		return SootSecurityLevel.highId(42);
	}
	
	@Annotations.ReturnSecurity("high")
	public int simpleHighSecurityMethod() {
		return SootSecurityLevel.highId(42);
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeSimpleHighSecurityMethod() {
		// @security("The returned value has a stronger security level than expected.")
		return simpleHighSecurityMethod();
	}
	
	@Annotations.ParameterSecurity({"low"})
	public void oneLowParameterVoidMethod(int low) {
		return;
	}
	
	public void failingInvokeOneLowParameterVoidMethod() {
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		oneLowParameterVoidMethod(high);
		return;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("low")
	public int oneLowParameterLowMethod(int low) {
		return low;
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeOneLowParameterLowMethod() {
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		int result = oneLowParameterLowMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int oneLowParameterHighMethod(int low) {
		return SootSecurityLevel.highId(42);
	}

	@Annotations.ReturnSecurity("high")
	public int failingInvokeOneLowParameterHighMethod() {
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		int result = oneLowParameterHighMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("low")
	public int twoLowLowParameterLowMethod(int low1, int low2) {
		return low1;
	}

	@Annotations.ReturnSecurity("low")
	public int failingInvokeTwoLowLowParameterLowMethod() {
		int high = SootSecurityLevel.highId(42);
		int low = SootSecurityLevel.lowId(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		int result = twoLowLowParameterLowMethod(high, low);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeTwoLowLowParameterLowMethod2() {
		int high = SootSecurityLevel.highId(42);
		int low = SootSecurityLevel.lowId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoLowLowParameterLowMethod(low, high);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeTwoLowLowParameterLowMethod3() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		// @security("Security level of arguments 1 is stronger than the expected level of the parameter.")
		// @security("Security level of arguments 2 is stronger than the expected level of the parameter.")
		int result = twoLowLowParameterLowMethod(high1, high2);
		return result;
	}	
	
	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("low")
	public int twoLowHighParameterLowMethod(int low, int high) {
		return low;
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeTwoLowHighParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		int result = twoLowHighParameterLowMethod(high, low);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeTwoLowHighParameterLowMethod2() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		int result = twoLowHighParameterLowMethod(high1, high2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("high")
	public int twoHighLowParameterHighMethod(int high, int low) {
		return high;
	}
	
	@Annotations.ReturnSecurity("high")
	public int failingInvokeTwoHighLowParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(low, high);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public int failingInvokeTwoHighLowParameterLowMethod2() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(high1, high2);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeTwoHighLowParameterLowMethod3() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoHighLowParameterHighMethod(high, low);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeTwoHighLowParameterLowMethod4() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(low, high);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeTwoHighLowParameterLowMethod5() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(high1, high2);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("high")
	public int twoHighHighParameterHighMethod(int high1, int high2) {
		return high1;
	}
	
	@Annotations.ReturnSecurity("low")
	public int failingInvokeTwoHighHighParameterLowMethod() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		int result = twoHighHighParameterHighMethod(high1, high2);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
}
