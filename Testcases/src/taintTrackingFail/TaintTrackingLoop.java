package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingLoop {

	@Annotations.WriteEffect({ "high" })
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("void")
	public void forLoopField2(int arg1High) {
		for (int i = 0; i < arg1High; i++) {
			highField = SootSecurityLevel.lowId(42);
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("void")
	public void forLoopField3(int arg1High) {
		for (int i = 0; i < arg1High; i++) {
			lowField = SootSecurityLevel.highId(42);
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("void")
	public void forLoopField4(int arg1High) {
		for (int i = 0; i < arg1High; i++) {
			lowField = SootSecurityLevel.lowId(42);
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "low" })
	@Annotations.ReturnSecurity("void")
	public void forLoopField7(int arg1Low) {
		for (int i = 0; i < arg1Low; i++) {
			lowField = SootSecurityLevel.highId(42);
		}
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("high")
	public int forLoopLocal2(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		for (int i = 0; i < arg1High; i++) {
			var1High = SootSecurityLevel.lowId(42);
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("high")
	public int forLoopLocal4(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		for (int i = 0; i < arg1High; i++) {
			var1Low = SootSecurityLevel.lowId(42);
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("low")
	public int forLoopLocal9(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		for (int i = 0; i < arg1High; i++) {
			var1High = SootSecurityLevel.highId(42);
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("low")
	public int forLoopLocal10(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		for (int i = 0; i < arg1High; i++) {
			var1High = SootSecurityLevel.lowId(42);
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("low")
	public int forLoopLocal11(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		for (int i = 0; i < arg1High; i++) {
			var1Low = SootSecurityLevel.highId(42);
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("low")
	public int forLoopLocal12(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		for (int i = 0; i < arg1High; i++) {
			var1Low = SootSecurityLevel.lowId(42);
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "low" })
	@Annotations.ReturnSecurity("low")
	public int forLoopLocal13(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		for (int i = 0; i < arg1Low; i++) {
			var1High = SootSecurityLevel.highId(42);
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "low" })
	@Annotations.ReturnSecurity("low")
	public int forLoopLocal14(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		for (int i = 0; i < arg1Low; i++) {
			var1High = SootSecurityLevel.lowId(42);
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "low" })
	@Annotations.ReturnSecurity("low")
	public int forLoopLocal15(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		for (int i = 0; i < arg1Low; i++) {
			var1Low = SootSecurityLevel.highId(42);
		}
		return var1Low;
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("void")
	public void whileLoopField5(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1High) {
			lowField = var1High++;
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("void")
	public void whileLoopField6(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1High) {
			lowField = var1Low++;
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("void")
	public void whileLoopField7(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1High) {
			lowField = var1High++;
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("void")
	public void whileLoopField8(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1High) {
			lowField = var1Low++;
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "low" })
	@Annotations.ReturnSecurity("void")
	public void whileLoopField13(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1Low) {
			lowField = var1High++;
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "low" })
	@Annotations.ReturnSecurity("void")
	public void whileLoopField15(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1Low) {
			lowField = var1High++;
		}
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("low")
	public int whileLoopLocal9(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1High) {
			var1High = var1High++;
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("low")
	public int whileLoopLocal10(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1High) {
			var1High = var1High++;
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("low")
	public int whileLoopLocal11(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1High) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "high" })
	@Annotations.ReturnSecurity("low")
	public int whileLoopLocal12(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1High) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "low" })
	@Annotations.ReturnSecurity("low")
	public int whileLoopLocal13(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1Low) {
			var1High = var1High++;
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({ "low" })
	@Annotations.ReturnSecurity("low")
	public int whileLoopLocal14(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1Low) {
			var1High = var1High++;
		}
		return var1High;
	}

	@Annotations.WriteEffect({ "high" })
	@Annotations.ParameterSecurity({ "high", "high" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField2(int arg1High, boolean arg2High) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2High) {
				highField = SootSecurityLevel.lowId(42);
			}
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high", "high" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField3(int arg1High, boolean arg2High) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2High) {
				lowField = SootSecurityLevel.highId(42);
			}
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high", "high" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField4(int arg1High, boolean arg2High) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2High) {
				lowField = SootSecurityLevel.lowId(42);
			}
		}
	}

	@Annotations.WriteEffect({ "high" })
	@Annotations.ParameterSecurity({ "high", "low" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField6(int arg1High, boolean arg2Low) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2Low) {
				highField = SootSecurityLevel.lowId(42);
			}
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high", "low" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField7(int arg1High, boolean arg2Low) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2Low) {
				lowField = SootSecurityLevel.highId(42);
			}
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "high", "low" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField8(int arg1High, boolean arg2Low) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2Low) {
				lowField = SootSecurityLevel.lowId(42);
			}
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "low", "high" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField11(int arg1Low, boolean arg2High) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2High) {
				lowField = SootSecurityLevel.highId(42);
			}
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "low", "high" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField12(int arg1Low, boolean arg2High) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2High) {
				lowField = SootSecurityLevel.lowId(42);
			}
		}
	}

	@Annotations.WriteEffect({ "high" })
	@Annotations.ParameterSecurity({ "low", "high" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField10(int arg1Low, boolean arg2High) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2High) {
				highField = SootSecurityLevel.lowId(42);
			}
		}
	}

	@Annotations.WriteEffect({ "low" })
	@Annotations.ParameterSecurity({ "low", "low" })
	@Annotations.ReturnSecurity("void")
	public void forLoopIfField15(int arg1Low, boolean arg2Low) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2Low) {
				lowField = SootSecurityLevel.highId(42);
			}
		}
	}

	@Annotations.FieldSecurity("low")
	int lowField = SootSecurityLevel.lowId(42);

	@Annotations.FieldSecurity("high")
	int highField = SootSecurityLevel.highId(42);

	@Annotations.ParameterSecurity({})
	@Annotations.WriteEffect({ "low", "high" })
	public TaintTrackingLoop() {
		super();
	}

}
