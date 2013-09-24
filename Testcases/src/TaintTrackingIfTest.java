import security.Annotations;
import security.SootSecurityLevel;

public class TaintTrackingIfTest {
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"high", "low"})
	public void highLow(boolean var1High, boolean var2High) {
		highField = 1;
		lowField = 2;
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void low(boolean var1High, boolean var2High) {
		lowField = 2;
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"high"})
	public void high(boolean var1High, boolean var2High) {
		highField = 2;
	}

	@Annotations.FieldSecurity("low")
	int lowField = SootSecurityLevel.lowId(42);
	
	@Annotations.FieldSecurity("high")
	int highField = SootSecurityLevel.highId(42);
	
}
