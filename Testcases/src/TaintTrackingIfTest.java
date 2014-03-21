

import security.Definition;
import security.Definition.*;

@WriteEffect({})
public class TaintTrackingIfTest {
	
	@ParameterSecurity({})
	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int forLoop() {
		int var3Low = Definition.lowId(42);
		for (int i = Definition.highId(0); i < 100; i++) {
//			if (i == 50) {
//				var3Low = Definition.highId(42);
//			} else {
				assignLow();
//			}
		}
		return var3Low;
	}
	
//	@ParameterSecurity({})
//	@ReturnSecurity("void")
//	@WriteEffect({"low"})
//	public void invoke() {
//		if (Definition.highId(23) == 5) {
//			assignLow();
//		}
//	}

	
	@ParameterSecurity({})
	@ReturnSecurity("void")
	@WriteEffect({"low"})
	public void assignLow() {
		lowField = 42; 
	}

	@FieldSecurity("low")
	public int lowField = Definition.lowId(42);
	
	@FieldSecurity("high")
	public int highField = Definition.highId(42);
	
	@ParameterSecurity({})
	@WriteEffect({"high", "low"})
	public TaintTrackingIfTest() {
		super();
	}
	
}
