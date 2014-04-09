package junitAnalysis;

import static security.Definition.*;

@WriteEffect({"high"})
public class Valid03 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@ParameterSecurity({})
	public Valid03() {
		
	}
	
	@ParameterSecurity({"high"})
	public Valid03(int arg) {
		
	}
	
	@ParameterSecurity({"high", "low"})
	public Valid03(int arg1, int arg2) {
		
	}
	
	@ParameterSecurity({})
	public void voidMethod() {
		return;
	}
	
	@ParameterSecurity({"high"})
	public void voidMethodInclusivParameter(int arg) {
		return;
	}
	
	@ParameterSecurity({"high", "low"})
	public void voidMethodInclusivParameter(int arg1, int arg2) {
		return;
	}
	
	@ParameterSecurity({})
	@ReturnSecurity("high")
	public int method() {
		return 42;
	}
	
	@ParameterSecurity({"high"})
	@ReturnSecurity("high")
	public int methodInclusivParameter(int arg) {
		return arg;
	}
	
	@ParameterSecurity({"high", "low"})
	@ReturnSecurity("high")
	public int methodInclusivParameter(int arg1, int arg2) {
		return arg1;
	}

}
