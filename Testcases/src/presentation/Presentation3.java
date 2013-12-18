package presentation;

import security.SootSecurityLevel;
import security.Annotations.*;

@WriteEffect({})
public class Presentation3 {

	@ParameterSecurity({})
	@ReturnSecurity("void")
	@WriteEffect({ "low" })
	public void method1() {
		int high = SootSecurityLevel.highId(42);
		FieldObject fo = new FieldObject();
		fo.low = high;
	}

	@ParameterSecurity({})
	@ReturnSecurity("low")
	@WriteEffect({ "low" })
	public int method2() {
		FieldObject fo = SootSecurityLevel.highId(new FieldObject());
		return fo.low;
	}

	@WriteEffect({})
	public static class FieldObject {

		@FieldSecurity("low")
		public int low = 42;

		@ParameterSecurity({})
		@WriteEffect({ "low" })
		public FieldObject() {
			super();
		}

	}

}
