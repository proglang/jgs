package analysisSuccess;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

@Annotations.WriteEffect({"low", "high"})
public class SuccessStaticField {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity() {
		return low;
	}
	
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity2() {
		int low2 = low;
		return low2;
	}
	
	@Annotations.ReturnSecurity("high")
	public int returnHighSecurity() {
		return high;
	}
	
	@Annotations.ReturnSecurity("high")
	public int returnHighSecurity2() {
		int high2 = high;
		return high2;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignLowSecurity() {
		int low2 = SootSecurityLevel.lowId(42);
		low = low2;
		return;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignHighSecurity() {
		int low = SootSecurityLevel.lowId(42);
		high = low;
		return;
	}
	
	@Annotations.WriteEffect({"low", "high"})
	public void assignHigh2Security() {
		int high2 = SootSecurityLevel.highId(42);
		high = high2;
		return;
	}
	
	@Annotations.FieldSecurity("low")
	public static int low = 42;
	
	@Annotations.FieldSecurity("high")
	public static int high = 42;
	
}
