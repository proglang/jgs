package junitAnalysis;

import security.Definition;
import security.Definition.*;

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
	
	@ReturnSecurity("low")
	public int simpleLowSecurityMethod() {
		return Definition.mkLow(42);
	}
	
	@ReturnSecurity("low")
	public int invokeSimpleLowSecurityMethod() {
		return simpleLowSecurityMethod();
	}
	
	@ReturnSecurity("high")
	public int simpleHighSecurityMethod() {
		return Definition.mkHigh(42);
	}
	
	@ReturnSecurity("high")
	public int invokeSimpleHighSecurityMethod() {
		return simpleHighSecurityMethod();
	}
	
	@ReturnSecurity("high")
	public int invokeSimpleHighSecurityMethod2() {
		return simpleLowSecurityMethod();
	}
	
	@ParameterSecurity({"low"})
	public void oneLowParameterVoidMethod(int low) {
		return;
	}
	
	public void invokeOneLowParameterVoidMethod() {
		int low = Definition.mkLow(42);
		oneLowParameterVoidMethod(low);
		return;
	}
	
	@ParameterSecurity({"low"})
	@ReturnSecurity("low")
	public int oneLowParameterLowMethod(int low) {
		return low;
	}
	
	@ReturnSecurity("low")
	public int invokeOneLowParameterLowMethod() {
		int low = Definition.mkLow(42);
		int result = oneLowParameterLowMethod(low);
		return result;
	}
	
	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int oneLowParameterHighMethod(int low) {
		return Definition.mkHigh(42);
	}

	@ReturnSecurity("high")
	public int invokeOneLowParameterHighMethod() {
		int low = Definition.mkLow(42);
		int result = oneLowParameterHighMethod(low);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeOneLowParameterHighMethod2() {
		int low = Definition.mkLow(42);
		int result = oneLowParameterLowMethod(low);
		return result;
	}
	
	@ParameterSecurity({"high"})
	public void oneHighParameterVoidMethod(int high) {
		return;
	}
	
	public void invokeOneHighParameterVoidMethod() {
		int high = Definition.mkHigh(42);
		oneHighParameterVoidMethod(high);
		return;
	}
	
	public void invokeOneHighParameterVoidMethod2() {
		int low = Definition.mkLow(42);
		oneHighParameterVoidMethod(low);
		return;
	}
	
	@ParameterSecurity({"high"})
	@ReturnSecurity("low")
	public int oneHighParameterLowMethod(int high) {
		return Definition.mkLow(42);
	}
	
	@ReturnSecurity("low")
	public int invokeOneHighParameterLowMethod() {
		int high = Definition.mkHigh(42);
		int result = oneHighParameterLowMethod(high);
		return result;
	}
	
	@ReturnSecurity("low")
	public int invokeOneHighParameterLowMethod2() {
		int low = Definition.mkLow(42);
		int result = oneHighParameterLowMethod(low);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeOneHighParameterLowMethod3() {
		int high = Definition.mkHigh(42);
		int result = oneHighParameterLowMethod(high);
		return result;
	}
	
	@ParameterSecurity({"high"})
	@ReturnSecurity("high")
	public int oneHighParameterHighMethod(int high) {
		return high;
	}
	
	@ReturnSecurity("high")
	public int invokeOneHighParameterHighMethod() {
		int high = Definition.mkHigh(42);
		int result = oneHighParameterHighMethod(high);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeOneHighParameterHighMethod2() {
		int low = Definition.mkLow(42);
		int result = oneHighParameterHighMethod(low);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeOneHighParameterHighMethod3() {
		int high = Definition.mkHigh(42);
		int result = oneHighParameterLowMethod(high);
		return result;
	}
	
	@ParameterSecurity({"low", "low"})
	@ReturnSecurity("low")
	public int twoLowLowParameterLowMethod(int low1, int low2) {
		return low1;
	}

	@ReturnSecurity("low")
	public int invokeTwoLowLowParameterLowMethod() {
		int low1 = Definition.mkLow(42);
		int low2 = Definition.mkLow(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeTwoLowLowParameterLowMethod2() {
		int low1 = Definition.mkLow(42);
		int low2 = Definition.mkLow(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
	@ParameterSecurity({"low", "high"})
	@ReturnSecurity("low")
	public int twoLowHighParameterLowMethod(int low, int high) {
		return low;
	}
	
	@ReturnSecurity("low")
	public int invokeTwoLowHighParameterLowMethod() {
		int low = Definition.mkLow(42);
		int high = Definition.mkHigh(42);
		int result = twoLowHighParameterLowMethod(low, high);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeTwoLowHighParameterLowMethod2() {
		int low = Definition.mkLow(42);
		int high = Definition.mkHigh(42);
		int result = twoLowHighParameterLowMethod(low, high);
		return result;
	}
	
	@ReturnSecurity("low")
	public int invokeTwoLowHighParameterLowMethod3() {
		int low1 = Definition.mkLow(42);
		int low2 = Definition.mkLow(42);
		int result = twoLowHighParameterLowMethod(low1, low2);
		return result;
	}
	
	@ParameterSecurity({"high", "low"})
	@ReturnSecurity("high")
	public int twoHighLowParameterHighMethod(int high, int low) {
		return high;
	}
	
	@ReturnSecurity("high")
	public int invokeTwoHighLowParameterLowMethod() {
		int low = Definition.mkLow(42);
		int high = Definition.mkHigh(42);
		int result = twoHighLowParameterHighMethod(high, low);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeTwoHighLowParameterLowMethod2() {
		int low1 = Definition.mkLow(42);
		int low2 = Definition.mkLow(42);
		int result = twoHighLowParameterHighMethod(low1, low2);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeTwoHighLowParameterLowMethod3() {
		int low1 = Definition.mkLow(42);
		int low2 = Definition.mkLow(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
	@ParameterSecurity({"high", "high"})
	@ReturnSecurity("high")
	public int twoHighHighParameterHighMethod(int high1, int high2) {
		return high1;
	}
	
	@ReturnSecurity("high")
	public int invokeTwoHighHighParameterLowMethod() {
		int high1 = Definition.mkHigh(42);
		int high2 = Definition.mkHigh(42);
		int result = twoHighHighParameterHighMethod(high1, high2);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeTwoHighHighParameterLowMethod2() {
		int low1 = Definition.mkLow(42);
		int low2 = Definition.mkLow(42);
		int result = twoHighHighParameterHighMethod(low1, low2);
		return result;
	}
	
	@ReturnSecurity("high")
	public int invokeTwoHighHighParameterLowMethod3() {
		int low1 = Definition.mkLow(42);
		int low2 = Definition.mkLow(42);
		int result = twoLowLowParameterLowMethod(low1, low2);
		return result;
	}
	
}
