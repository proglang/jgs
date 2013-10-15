package taintTrackingSuccess;

import security.SootSecurityLevel;
import security.Annotations;

@Annotations.WriteEffect({})
public class TaintTrackingLoop {
	
	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField(int arg1High) {
		for (int i = 0; i < arg1High; i++) {
			highField = SootSecurityLevel.highId(42);
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField5(int arg1Low) {
		for (int i = 0; i < arg1Low; i++) {
			highField = SootSecurityLevel.highId(42);
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField6(int arg1Low) {
		for (int i = 0; i < arg1Low; i++) {
			highField = SootSecurityLevel.lowId(42);
		}
	}

	@Annotations.WriteEffect({"low"})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField8(int arg1Low) {
		for (int i = 0; i < arg1Low; i++) {
			lowField = SootSecurityLevel.lowId(42);
		}
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("high")
	public int forLoopLocal(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		for (int i = 0; i < arg1High; i++) {
			var1High = SootSecurityLevel.highId(42);
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("high")
	public int forLoopLocal3(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		for (int i = 0; i < arg1High; i++) {
			var1Low = SootSecurityLevel.highId(42);
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int forLoopLocal5(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		for (int i = 0; i < arg1Low; i++) {
			var1High = SootSecurityLevel.highId(42);
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int forLoopLocal6(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		for (int i = 0; i < arg1Low; i++) {
			var1High = SootSecurityLevel.lowId(42);
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int forLoopLocal7(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		for (int i = 0; i < arg1Low; i++) {
			var1Low = SootSecurityLevel.highId(42);
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int forLoopLocal8(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		for (int i = 0; i < arg1Low; i++) {
			var1Low = SootSecurityLevel.lowId(42);
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("low")
	public int forLoopLocal16(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		for (int i = 0; i < arg1Low; i++) {
			var1Low = SootSecurityLevel.lowId(42);
		}
		return var1Low;
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1High) {
			highField = var1High++;
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField2(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1High) {
			highField = var1Low++;
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField3(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1High) {
			highField = var1High++;
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField4(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1High) {
			highField = var1Low++;
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField9(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1Low) {
			highField = var1High++;
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField10(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1Low) {
			highField = var1Low++;
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField11(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1Low) {
			highField = var1High++;
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField12(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1Low) {
			highField = var1Low++;
		}
	}

	@Annotations.WriteEffect({"low"})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField14(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1Low) {
			lowField = var1Low++;
		}
	}

	@Annotations.WriteEffect({"low"})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("void")
	public void whileLoopField16(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1Low) {
			lowField = var1Low++;
		}
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("high")
	public int whileLoopLocal(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1High) {
			var1High = var1High++;
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("high")
	public int whileLoopLocal2(int arg1High) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1High) {
			var1High = var1High++;
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("high")
	public int whileLoopLocal3(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1High) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"high"})
	@Annotations.ReturnSecurity("high")
	public int whileLoopLocal4(int arg1High) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1High) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int whileLoopLocal5(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1Low) {
			var1High = var1High++;
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int whileLoopLocal6(int arg1Low) {
		int var1High = SootSecurityLevel.highId(42);
		while (var1High < arg1Low) {
			var1High = var1High++;
		}
		return var1High;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int whileLoopLocal7(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1Low) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("high")
	public int whileLoopLocal8(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1Low) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("low")
	public int whileLoopLocal15(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1Low) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@Annotations.WriteEffect({})
	@Annotations.ParameterSecurity({"low"})
	@Annotations.ReturnSecurity("low")
	public int whileLoopLocal16(int arg1Low) {
		int var1Low = SootSecurityLevel.lowId(42);
		while (var1Low < arg1Low) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField(int arg1High, boolean arg2High) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2High) {
				highField = SootSecurityLevel.highId(42);
			}
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField5(int arg1High, boolean arg2Low) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2Low) {
				highField = SootSecurityLevel.highId(42);
			}
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField9(int arg1Low, boolean arg2High) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2High) {
				highField = SootSecurityLevel.highId(42);
			}
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField13(int arg1Low, boolean arg2Low) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2Low) {
				highField = SootSecurityLevel.highId(42);
			}
		}
	}

	@Annotations.WriteEffect({"high"})
	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField14(int arg1Low, boolean arg2Low) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2Low) {
				highField = SootSecurityLevel.lowId(42);
			}
		}
	}

	@Annotations.WriteEffect({"low"})
	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("void")
	public void forLoopField16(int arg1Low, boolean arg2Low) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2Low) {
				lowField = SootSecurityLevel.lowId(42);
			}
		}
	}
	
	@Annotations.FieldSecurity("low")
	int lowField = SootSecurityLevel.lowId(42);
	
	@Annotations.FieldSecurity("high")
	int highField = SootSecurityLevel.highId(42);
	
	@Annotations.ParameterSecurity({})
	@Annotations.WriteEffect({"low", "high"})
	public TaintTrackingLoop() {
		super();
	}

}
