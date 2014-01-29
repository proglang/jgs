package analysisSuccess;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;


public class SuccessIdFunctions {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@Annotations.ReturnSecurity("high")
	public int returnHighSecurity() {
		int high = SootSecurityLevel.highId(42);
		return high;
	}
	
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity() {
		int low = SootSecurityLevel.lowId(42);
		return low;
	}
	
	public void changeSecurityLow2High() {
		int low = SootSecurityLevel.lowId(42);
		SootSecurityLevel.highId(low);
		return;
	}
	
	public void changeSecurityHigh2High() {
		int high = SootSecurityLevel.highId(42);
		SootSecurityLevel.highId(high);
		return;
	}
}
