package junitLevel;

import static security.Definition.*;

public class Valid02 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	public Valid02() {
		
	}
	
	@ParameterSecurity({"high"})
	public Valid02(int arg) {
		
	}
	
	@ParameterSecurity({"high", "low"})
	public Valid02(int arg1, int arg2) {
		
	}
	
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
