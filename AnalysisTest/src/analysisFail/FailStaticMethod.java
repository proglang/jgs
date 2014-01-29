package analysisFail;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

@Annotations.WriteEffect({})
public class FailStaticMethod {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@Annotations.ReturnSecurity("low")
	public static int failingSimpleLowSecurityMethod() {
		// @security("The returned value has a stronger security level than expected.")
		return SootSecurityLevel.highId(42);
	}
	
	@Annotations.ReturnSecurity("high")
	public static int simpleHighSecurityMethod() {
		return SootSecurityLevel.highId(42);
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeSimpleHighSecurityMethod() {
		// @security("The returned value has a stronger security level than expected.")
		return simpleHighSecurityMethod();
	}
	
	@Annotations.ParameterSecurity({"low"})
	public static void oneLowParameterVoidMethod(int low) {
		return;
	}
	
	public static void failingInvokeOneLowParameterVoidMethod() {
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		oneLowParameterVoidMethod(high);
		return;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("low")
	public static int oneLowParameterLowMethod(int low) {
		return low;
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeOneLowParameterLowMethod() {
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		int result = oneLowParameterLowMethod(high);
		return result;
	}
	
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public static int oneLowParameterHighMethod(int low) {
		return SootSecurityLevel.highId(42);
	}

	@Annotations.ReturnSecurity("high")
	public static int failingInvokeOneLowParameterHighMethod() {
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		int result = oneLowParameterHighMethod(high);
		return result;
	}
		
	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("low")
	public static int twoLowLowParameterLowMethod(int low1, int low2) {
		return low1;
	}

	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowLowParameterLowMethod() {
		int high = SootSecurityLevel.highId(42);
		int low = SootSecurityLevel.lowId(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		int result = twoLowLowParameterLowMethod(high, low);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowLowParameterLowMethod2() {
		int high = SootSecurityLevel.highId(42);
		int low = SootSecurityLevel.lowId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoLowLowParameterLowMethod(low, high);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowLowParameterLowMethod3() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoLowLowParameterLowMethod(high1, high2);
		return result;
	}	
	
	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("low")
	public static int twoLowHighParameterLowMethod(int low, int high) {
		return low;
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowHighParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		int result = twoLowHighParameterLowMethod(high, low);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoLowHighParameterLowMethod2() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		int result = twoLowHighParameterLowMethod(high1, high2);
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("high")
	public static int twoHighLowParameterHighMethod(int high, int low) {
		return high;
	}
	
	@Annotations.ReturnSecurity("high")
	public static int failingInvokeTwoHighLowParameterLowMethod() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(low, high);
		return result;
	}
	
	@Annotations.ReturnSecurity("high")
	public static int failingInvokeTwoHighLowParameterLowMethod2() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(high1, high2);
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoHighLowParameterLowMethod3() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		int result = twoHighLowParameterHighMethod(high, low);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoHighLowParameterLowMethod4() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(low, high);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoHighLowParameterLowMethod5() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(high1, high2);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("high")
	public static int twoHighHighParameterHighMethod(int high1, int high2) {
		return high1;
	}
	
	@Annotations.ReturnSecurity("low")
	public static int failingInvokeTwoHighHighParameterLowMethod() {
		int high1 = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(42);
		int result = twoHighHighParameterHighMethod(high1, high2);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
}
