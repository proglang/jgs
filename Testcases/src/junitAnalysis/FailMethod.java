package junitAnalysis;

import security.Definition;
import security.Definition.*;

public class FailMethod {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@ReturnSecurity("low")
	public int failingSimpleLowSecurityMethod() {
		// @security("The returned value has a stronger security level than expected.")
		return Definition.mkHigh(42);
	}
	
	@ReturnSecurity("high")
	public int simpleHighSecurityMethod() {
		return Definition.mkHigh(42);
	}
	
	@ReturnSecurity("low")
	public int failingInvokeSimpleHighSecurityMethod() {
		// @security("The returned value has a stronger security level than expected.")
		return simpleHighSecurityMethod();
	}
	
	@ParameterSecurity({"low"})
	public void oneLowParameterVoidMethod(int low) {
		return;
	}
	
	public void failingInvokeOneLowParameterVoidMethod() {
		int high = Definition.mkHigh(42);
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		oneLowParameterVoidMethod(high);
		return;
	}
	
	@ParameterSecurity({"low"})
	@ReturnSecurity("low")
	public int oneLowParameterLowMethod(int low) {
		return low;
	}
	
	@ReturnSecurity("low")
	public int failingInvokeOneLowParameterLowMethod() {
		int high = Definition.mkHigh(42);
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		int result = oneLowParameterLowMethod(high);
		return result;
	}
	
	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int oneLowParameterHighMethod(int low) {
		return Definition.mkHigh(42);
	}

	@ReturnSecurity("high")
	public int failingInvokeOneLowParameterHighMethod() {
		int high = Definition.mkHigh(42);
		// @security("Security level of argument is stronger than the expected level of the parameter.")
		int result = oneLowParameterHighMethod(high);
		return result;
	}
	
	@ParameterSecurity({"low", "low"})
	@ReturnSecurity("low")
	public int twoLowLowParameterLowMethod(int low1, int low2) {
		return low1;
	}

	@ReturnSecurity("low")
	public int failingInvokeTwoLowLowParameterLowMethod() {
		int high = Definition.mkHigh(42);
		int low = Definition.mkLow(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		int result = twoLowLowParameterLowMethod(high, low);
		return result;
	}
	
	@ReturnSecurity("low")
	public int failingInvokeTwoLowLowParameterLowMethod2() {
		int high = Definition.mkHigh(42);
		int low = Definition.mkLow(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoLowLowParameterLowMethod(low, high);
		return result;
	}
	
	@ReturnSecurity("low")
	public int failingInvokeTwoLowLowParameterLowMethod3() {
		int high1 = Definition.mkHigh(42);
		int high2 = Definition.mkHigh(42);
		// @security("Security level of arguments 1 is stronger than the expected level of the parameter.")
		// @security("Security level of arguments 2 is stronger than the expected level of the parameter.")
		int result = twoLowLowParameterLowMethod(high1, high2);
		return result;
	}	
	
	@ParameterSecurity({"low", "high"})
	@ReturnSecurity("low")
	public int twoLowHighParameterLowMethod(int low, int high) {
		return low;
	}
	
	@ReturnSecurity("low")
	public int failingInvokeTwoLowHighParameterLowMethod() {
		int low = Definition.mkLow(42);
		int high = Definition.mkHigh(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		int result = twoLowHighParameterLowMethod(high, low);
		return result;
	}
	
	@ReturnSecurity("low")
	public int failingInvokeTwoLowHighParameterLowMethod2() {
		int high1 = Definition.mkHigh(42);
		int high2 = Definition.mkHigh(42);
		// @security("Security level of argument 1 is stronger than the expected level of the parameter.")
		int result = twoLowHighParameterLowMethod(high1, high2);
		return result;
	}
	
	@ParameterSecurity({"high", "low"})
	@ReturnSecurity("high")
	public int twoHighLowParameterHighMethod(int high, int low) {
		return high;
	}
	
	@ReturnSecurity("high")
	public int failingInvokeTwoHighLowParameterLowMethod() {
		int low = Definition.mkLow(42);
		int high = Definition.mkHigh(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(low, high);
		return result;
	}
	
	@ReturnSecurity("high")
	public int failingInvokeTwoHighLowParameterLowMethod2() {
		int high1 = Definition.mkHigh(42);
		int high2 = Definition.mkHigh(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(high1, high2);
		return result;
	}
	
	@ReturnSecurity("low")
	public int failingInvokeTwoHighLowParameterLowMethod3() {
		int low = Definition.mkLow(42);
		int high = Definition.mkHigh(42);
		int result = twoHighLowParameterHighMethod(high, low);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@ReturnSecurity("low")
	public int failingInvokeTwoHighLowParameterLowMethod4() {
		int low = Definition.mkLow(42);
		int high = Definition.mkHigh(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(low, high);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@ReturnSecurity("low")
	public int failingInvokeTwoHighLowParameterLowMethod5() {
		int high1 = Definition.mkHigh(42);
		int high2 = Definition.mkHigh(42);
		// @security("Security level of argument 2 is stronger than the expected level of the parameter.")
		int result = twoHighLowParameterHighMethod(high1, high2);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@ParameterSecurity({"high", "high"})
	@ReturnSecurity("high")
	public int twoHighHighParameterHighMethod(int high1, int high2) {
		return high1;
	}
	
	@ReturnSecurity("low")
	public int failingInvokeTwoHighHighParameterLowMethod() {
		int high1 = Definition.mkHigh(42);
		int high2 = Definition.mkHigh(42);
		int result = twoHighHighParameterHighMethod(high1, high2);
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
}
