package junitAnalysis;

import static security.Definition.*;

public class SuccessLoop {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@WriteEffect({"high"})
	@ParameterSecurity({"high"})
	public void forLoopField(int arg1High) {
		for (int i = 0; i < arg1High; i++) {
			highField = mkHigh(42);
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"low"})
	public void forLoopField5(int arg1Low) {
		for (int i = 0; i < arg1Low; i++) {
			highField = mkHigh(42);
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"low"})
	public void forLoopField6(int arg1Low) {
		for (int i = 0; i < arg1Low; i++) {
			highField = mkLow(42);
		}
	}

	@WriteEffect({"low"})
	@ParameterSecurity({"low"})
	public void forLoopField8(int arg1Low) {
		for (int i = 0; i < arg1Low; i++) {
			lowField = mkLow(42);
		}
	}

	@ParameterSecurity({"high"})
	@ReturnSecurity("high")
	public int forLoopLocal(int arg1High) {
		int var1High = mkHigh(42);
		for (int i = 0; i < arg1High; i++) {
			var1High = mkHigh(42);
		}
		return var1High;
	}

	@ParameterSecurity({"high"})
	@ReturnSecurity("high")
	public int forLoopLocal3(int arg1High) {
		int var1Low = mkLow(42);
		for (int i = 0; i < arg1High; i++) {
			var1Low = mkHigh(42);
		}
		return var1Low;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int forLoopLocal5(int arg1Low) {
		int var1High = mkHigh(42);
		for (int i = 0; i < arg1Low; i++) {
			var1High = mkHigh(42);
		}
		return var1High;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int forLoopLocal6(int arg1Low) {
		int var1High = mkHigh(42);
		for (int i = 0; i < arg1Low; i++) {
			var1High = mkLow(42);
		}
		return var1High;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int forLoopLocal7(int arg1Low) {
		int var1Low = mkLow(42);
		for (int i = 0; i < arg1Low; i++) {
			var1Low = mkHigh(42);
		}
		return var1Low;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int forLoopLocal8(int arg1Low) {
		int var1Low = mkLow(42);
		for (int i = 0; i < arg1Low; i++) {
			var1Low = mkLow(42);
		}
		return var1Low;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("low")
	public int forLoopLocal16(int arg1Low) {
		int var1Low = mkLow(42);
		for (int i = 0; i < arg1Low; i++) {
			var1Low = mkLow(42);
		}
		return var1Low;
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"high"})
	public void whileLoopField(int arg1High) {
		int var1High = mkHigh(42);
		while (var1High < arg1High) {
			highField = var1High++;
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"high"})
	public void whileLoopField2(int arg1High) {
		int var1Low = mkLow(42);
		while (var1Low < arg1High) {
			highField = var1Low++;
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"high"})
	public void whileLoopField3(int arg1High) {
		int var1High = mkHigh(42);
		while (var1High < arg1High) {
			highField = var1High++;
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"high"})
	public void whileLoopField4(int arg1High) {
		int var1Low = mkLow(42);
		while (var1Low < arg1High) {
			highField = var1Low++;
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"low"})
	public void whileLoopField9(int arg1Low) {
		int var1High = mkHigh(42);
		while (var1High < arg1Low) {
			highField = var1High++;
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"low"})
	public void whileLoopField10(int arg1Low) {
		int var1Low = mkLow(42);
		while (var1Low < arg1Low) {
			highField = var1Low++;
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"low"})
	public void whileLoopField11(int arg1Low) {
		int var1High = mkHigh(42);
		while (var1High < arg1Low) {
			highField = var1High++;
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"low"})
	public void whileLoopField12(int arg1Low) {
		int var1Low = mkLow(42);
		while (var1Low < arg1Low) {
			highField = var1Low++;
		}
	}

	@WriteEffect({"low"})
	@ParameterSecurity({"low"})
	public void whileLoopField14(int arg1Low) {
		int var1Low = mkLow(42);
		while (var1Low < arg1Low) {
			lowField = var1Low++;
		}
	}

	@WriteEffect({"low"})
	@ParameterSecurity({"low"})
	public void whileLoopField16(int arg1Low) {
		int var1Low = mkLow(42);
		while (var1Low < arg1Low) {
			lowField = var1Low++;
		}
	}

	@ParameterSecurity({"high"})
	@ReturnSecurity("high")
	public int whileLoopLocal(int arg1High) {
		int var1High = mkHigh(42);
		while (var1High < arg1High) {
			var1High = var1High++;
		}
		return var1High;
	}

	@ParameterSecurity({"high"})
	@ReturnSecurity("high")
	public int whileLoopLocal2(int arg1High) {
		int var1High = mkHigh(42);
		while (var1High < arg1High) {
			var1High = var1High++;
		}
		return var1High;
	}

	@ParameterSecurity({"high"})
	@ReturnSecurity("high")
	public int whileLoopLocal3(int arg1High) {
		int var1Low = mkLow(42);
		while (var1Low < arg1High) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@ParameterSecurity({"high"})
	@ReturnSecurity("high")
	public int whileLoopLocal4(int arg1High) {
		int var1Low = mkLow(42);
		while (var1Low < arg1High) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int whileLoopLocal5(int arg1Low) {
		int var1High = mkHigh(42);
		while (var1High < arg1Low) {
			var1High = var1High++;
		}
		return var1High;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int whileLoopLocal6(int arg1Low) {
		int var1High = mkHigh(42);
		while (var1High < arg1Low) {
			var1High = var1High++;
		}
		return var1High;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int whileLoopLocal7(int arg1Low) {
		int var1Low = mkLow(42);
		while (var1Low < arg1Low) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("high")
	public int whileLoopLocal8(int arg1Low) {
		int var1Low = mkLow(42);
		while (var1Low < arg1Low) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("low")
	public int whileLoopLocal15(int arg1Low) {
		int var1Low = mkLow(42);
		while (var1Low < arg1Low) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@ParameterSecurity({"low"})
	@ReturnSecurity("low")
	public int whileLoopLocal16(int arg1Low) {
		int var1Low = mkLow(42);
		while (var1Low < arg1Low) {
			var1Low = var1Low++;
		}
		return var1Low;
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"high", "high"})
	public void forLoopField(int arg1High, boolean arg2High) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2High) {
				highField = mkHigh(42);
			}
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"high", "low"})
	public void forLoopField5(int arg1High, boolean arg2Low) {
		for (int i = 0; i < arg1High; i++) {
			if (arg2Low) {
				highField = mkHigh(42);
			}
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"low", "high"})
	public void forLoopField9(int arg1Low, boolean arg2High) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2High) {
				highField = mkHigh(42);
			}
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"low", "low"})
	public void forLoopField13(int arg1Low, boolean arg2Low) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2Low) {
				highField = mkHigh(42);
			}
		}
	}

	@WriteEffect({"high"})
	@ParameterSecurity({"low", "low"})
	public void forLoopField14(int arg1Low, boolean arg2Low) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2Low) {
				highField = mkLow(42);
			}
		}
	}

	@WriteEffect({"low"})
	@ParameterSecurity({"low", "low"})
	public void forLoopField16(int arg1Low, boolean arg2Low) {
		for (int i = 0; i < arg1Low; i++) {
			if (arg2Low) {
				lowField = mkLow(42);
			}
		}
	}
	
	@FieldSecurity("low")
	int lowField = mkLow(42);
	
	@FieldSecurity("high")
	int highField = mkHigh(42);
	
	@WriteEffect({"low", "high"})
	public SuccessLoop() {
		super();
	}

}
