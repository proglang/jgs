package junitAnalysis;

import security.Definition.*;

public class Valid05 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	public static void voidMethod() {
		return;
	}
	
	@ParameterSecurity({"high"})
	public static void voidMethodInclusivParameter(int arg) {
		return;
	}
	
	@ParameterSecurity({"high", "low"})
	public static void voidMethodInclusivParameter(int arg1, int arg2) {
		return;
	}
	
	@ReturnSecurity("high")
	public static int method() {
		return 42;
	}
	
	@ParameterSecurity({"high"})
	@ReturnSecurity("high")
	public static int methodInclusivParameter(int arg) {
		return arg;
	}
	
	@ParameterSecurity({"high", "low"})
	@ReturnSecurity("high")
	public static int methodInclusivParameter(int arg1, int arg2) {
		return arg1;
	}

}
