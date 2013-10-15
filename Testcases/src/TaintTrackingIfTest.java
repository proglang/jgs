

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingIfTest {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int forLoop() {
		int var3Low = SootSecurityLevel.lowId(42);
		for (int i = SootSecurityLevel.highId(0); i < 100; i++) {
//			if (i == 50) {
//				var3Low = SootSecurityLevel.highId(42);
//			} else {
				assignLow();
//			}
		}
		return var3Low;
	}
	
//	@Annotations.ParameterSecurity({})
//	@Annotations.ReturnSecurity("void")
//	@Annotations.WriteEffect({"low"})
//	public void invoke() {
//		if (SootSecurityLevel.highId(23) == 5) {
//			assignLow();
//		}
//	}

	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void assignLow() {
		lowField = 42; 
	}

	@Annotations.FieldSecurity("low")
	public int lowField = SootSecurityLevel.lowId(42);
	
	@Annotations.FieldSecurity("high")
	public int highField = SootSecurityLevel.highId(42);
	
	@Annotations.ParameterSecurity({})
	@Annotations.WriteEffect({"high", "low"})
	public TaintTrackingIfTest() {
		super();
	}
	
}
