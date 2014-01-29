package annotationValidity;

import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;

public class Valid04 {
	
	@ParameterSecurity({})
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
	
	@ParameterSecurity({})
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
