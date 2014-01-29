package annotationValidity;

import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;
import security.Annotations.WriteEffect;

@WriteEffect({})
public class Valid01 {
	
	@ParameterSecurity({})
	public Valid01() {}
	
	@ParameterSecurity({"high"})
	public Valid01(int arg) {}
	
	@ParameterSecurity({"high", "low"})
	public Valid01(int arg1, int arg2) {}
	
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
