package junitAnalysis;

import static security.Definition.*;

public class FailIfElse2 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int ifAssign() {
		int thenHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		}
		return lowField;
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int ifAssign2() {
		int thenHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		}
		return lowField;
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int ifAssign3() {
		int thenLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenLow;
		}
		return lowField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"high"})
	public int ifAssign4() {
		int thenHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			highField = thenHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int ifAssign5() {
		int thenHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		}
		return lowField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"high"})
	public int ifAssign6() {
		int thenHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			highField = thenHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int ifAssign7() {
		int thenHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		}
		return lowField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"high"})
	public int ifAssign8() {
		int thenLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			highField = thenLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int ifAssign9() {
		int thenLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenLow;
		}
		return lowField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"high"})
	public int ifAssign10() {
		int thenLow = mkLow(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			highField = thenLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}
	
	@ReturnSecurity("low")
	public int ifExprAssign() {
		int result = mkHigh(42);
		int thenHigh = mkHigh(42);
		int elseHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		result = conditionHigh ? thenHigh : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign2() {
		int result = mkHigh(42);
		int thenHigh = mkHigh(42);
		int elseHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		result = conditionLow ? thenHigh : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign3() {
		int result = mkLow(42);
		int thenHigh = mkHigh(42);
		int elseHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		result = conditionHigh ? thenHigh : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign4() {
		int result = mkLow(42);
		int thenHigh = mkHigh(42);
		int elseHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		result = conditionLow ? thenHigh : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign5() {
		int result = mkHigh(42);
		int thenHigh = mkHigh(42);
		int elseLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		result = conditionHigh ? thenHigh : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign6() {
		int result = mkHigh(42);
		int thenHigh = mkHigh(42);
		int elseLow = mkLow(42);
		boolean conditionLow = mkLow(false);
		result = conditionLow ? thenHigh : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign7() {
		int result = mkLow(42);
		int thenHigh = mkHigh(42);
		int elseLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		result = conditionHigh ? thenHigh : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign8() {
		int result = mkLow(42);
		int thenHigh = mkHigh(42);
		int elseLow = mkLow(42);
		boolean conditionLow = mkLow(false);
		result = conditionLow ? thenHigh : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign9() {
		int result = mkHigh(42);
		int thenLow = mkLow(42);
		int elseHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		result = conditionHigh ? thenLow : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign10() {
		int result = mkHigh(42);
		int thenLow = mkLow(42);
		int elseHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		result = conditionLow ? thenLow : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign11() {
		int result = mkLow(42);
		int thenLow = mkLow(42);
		int elseHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		result = conditionHigh ? thenLow : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign12() {
		int result = mkLow(42);
		int thenLow = mkLow(42);
		int elseHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		result = conditionLow ? thenLow : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign13() {
		int result = mkHigh(42);
		int thenLow = mkLow(42);
		int elseLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		result = conditionHigh ? thenLow : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign14() {
		int result = mkLow(42);
		int thenLow = mkLow(42);
		int elseLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		result = conditionHigh ? thenLow : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@WriteEffect({"low"})
	public void ifElseAssignLocalField2() {
		int result = mkHigh(42);
		int thenHigh = mkHigh(42);
		int elseHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField4() {
		int result = mkHigh(42);
		int thenHigh = mkHigh(42);
		int elseHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField6() {
		int result = mkHigh(42);
		int thenHigh = mkHigh(42);
		int elseLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField8() {
		int result = mkHigh(42);
		int thenHigh = mkHigh(42);
		int elseLow = mkLow(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField10() {
		int result = mkHigh(42);
		int thenLow = mkLow(42);
		int elseHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField12() {
		int result = mkHigh(42);
		int thenLow = mkLow(42);
		int elseHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField14() {
		int result = mkHigh(42);
		int thenLow = mkLow(42);
		int elseLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseLow;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField18() {
		int result = mkLow(42);
		int thenHigh = mkHigh(42);
		int elseHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField20() {
		int result = mkLow(42);
		int thenHigh = mkHigh(42);
		int elseHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField22() {
		int result = mkLow(42);
		int thenHigh = mkHigh(42);
		int elseLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField24() {
		int result = mkLow(42);
		int thenHigh = mkHigh(42);
		int elseLow = mkLow(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField26() {
		int result = mkLow(42);
		int thenLow = mkLow(42);
		int elseHigh = mkHigh(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField28() {
		int result = mkLow(42);
		int thenLow = mkLow(42);
		int elseHigh = mkHigh(42);
		boolean conditionLow = mkLow(false);
		if (conditionLow) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}

	@WriteEffect({"low"})
	public void ifElseAssignLocalField30() {
		int result = mkLow(42);
		int thenLow = mkLow(42);
		int elseLow = mkLow(42);
		boolean conditionHigh = mkHigh(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseLow;
		}
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = result;
	}
	
	@ParameterSecurity({"high", "high"})
	@WriteEffect({"low", "high"})
	public void ifIf(boolean var1High, boolean var2High) {
		int var3High = mkHigh(42);
		int var4High = mkHigh(42);
		if (var1High) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		if (var2High) {
			highField = var4High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4High;
		}
	}

	@ParameterSecurity({"high", "high"})
	@WriteEffect({"low", "high"})
	public void ifIf2(boolean var1High, boolean var2High) {
		int var3High = mkHigh(42);
		int var4Low = mkLow(42);
		if (var1High) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		if (var2High) {
			highField = var4Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4Low;
		}
	}

	@ParameterSecurity({"high", "high"})
	@WriteEffect({"low", "high"})
	public void ifIf3(boolean var1High, boolean var2High) {
		int var3Low = mkLow(42);
		int var4High = mkHigh(42);
		if (var1High) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		if (var2High) {
			highField = var4High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4High;
		}
	}

	@ParameterSecurity({"high", "high"})
	@WriteEffect({"low", "high"})
	public void ifIf4(boolean var1High, boolean var2High) {
		int var3Low = mkLow(42);
		int var4Low = mkLow(42);
		if (var1High) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		if (var2High) {
			highField = var4Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4Low;
		}
	}

	@ParameterSecurity({"high", "low"})
	@WriteEffect({"low", "high"})
	public void ifIf5(boolean var1High, boolean var2Low) {
		int var3High = mkHigh(42);
		int var4High = mkHigh(42);
		if (var1High) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		if (var2Low) {
			highField = var4High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4High;
		}
	}

	@ParameterSecurity({"high", "low"})
	@WriteEffect({"low", "high"})
	public void ifIf6(boolean var1High, boolean var2Low) {
		int var3High = mkHigh(42);
		int var4Low = mkLow(42);
		if (var1High) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		if (var2Low) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@ParameterSecurity({"high", "low"})
	@WriteEffect({"low", "high"})
	public void ifIf7(boolean var1High, boolean var2Low) {
		int var3Low = mkLow(42);
		int var4High = mkHigh(42);
		if (var1High) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		if (var2Low) {
			highField = var4High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4High;
		}
	}

	@ParameterSecurity({"high", "low"})
	@WriteEffect({"low", "high"})
	public void ifIf8(boolean var1High, boolean var2Low) {
		int var3Low = mkLow(42);
		int var4Low = mkLow(42);
		if (var1High) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		if (var2Low) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@ParameterSecurity({"low", "high"})
	@WriteEffect({"low", "high"})
	public void ifIf9(boolean var1Low, boolean var2High) {
		int var3High = mkHigh(42);
		int var4High = mkHigh(42);
		if (var1Low) {
			highField = var3High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		if (var2High) {
			highField = var4High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4High;
		}
	}

	@ParameterSecurity({"low", "high"})
	@WriteEffect({"low", "high"})
	public void ifIf10(boolean var1Low, boolean var2High) {
		int var3High = mkHigh(42);
		int var4Low = mkLow(42);
		if (var1Low) {
			highField = var3High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		if (var2High) {
			highField = var4Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4Low;
		}
	}

	@ParameterSecurity({"low", "high"})
	@WriteEffect({"low", "high"})
	public void ifIf11(boolean var1Low, boolean var2High) {
		int var3Low = mkLow(42);
		int var4High = mkHigh(42);
		if (var1Low) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2High) {
			highField = var4High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4High;
		}
	}

	@ParameterSecurity({"low", "high"})
	@WriteEffect({"low", "high"})
	public void ifIf12(boolean var1Low, boolean var2High) {
		int var3Low = mkLow(42);
		int var4Low = mkLow(42);
		if (var1Low) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2High) {
			highField = var4Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4Low;
		}
	}

	@ParameterSecurity({"low", "low"})
	@WriteEffect({"low", "high"})
	public void ifIf13(boolean var1Low, boolean var2Low) {
		int var3High = mkHigh(42);
		int var4High = mkHigh(42);
		if (var1Low) {
			highField = var3High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		if (var2Low) {
			highField = var4High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4High;
		}
	}

	@ParameterSecurity({"low", "low"})
	@WriteEffect({"low", "high"})
	public void ifIf14(boolean var1Low, boolean var2Low) {
		int var3High = mkHigh(42);
		int var4Low = mkLow(42);
		if (var1Low) {
			highField = var3High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		if (var2Low) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@ParameterSecurity({"low", "low"})
	@WriteEffect({"low", "high"})
	public void ifIf15(boolean var1Low, boolean var2Low) {
		int var3Low = mkLow(42);
		int var4High = mkHigh(42);
		if (var1Low) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2Low) {
			highField = var4High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var4High;
		}
	}
	
	@ParameterSecurity({"high", "high"})
	@WriteEffect({"low", "high"})
	public void ifDoubleCond(boolean var1High, boolean var2High) {
		int var3High = mkHigh(42);
		if (var1High && var2High) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		highField = var3High;
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = var3High;
	}

	@ParameterSecurity({"high", "high"})
	@WriteEffect({"low", "high"})
	public void ifDoubleCond2(boolean var1High, boolean var2High) {
		int var3Low = mkLow(42);
		if (var1High && var2High) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@ParameterSecurity({"high", "low"})
	@WriteEffect({"low", "high"})
	public void ifDoubleCond3(boolean var1High, boolean var2Low) {
		int var3High = mkHigh(42);
		if (var1High && var2Low) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		highField = var3High;
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = var3High;
	}

	@ParameterSecurity({"high", "low"})
	@WriteEffect({"low", "high"})
	public void ifDoubleCond4(boolean var1High, boolean var2Low) {
		int var3Low = mkLow(42);
		if (var1High && var2Low) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@ParameterSecurity({"low", "high"})
	@WriteEffect({"low", "high"})
	public void ifDoubleCond5(boolean var1Low, boolean var2High) {
		int var3High = mkHigh(42);
		if (var1Low && var2High) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		highField = var3High;
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = var3High;
	}

	@ParameterSecurity({"low", "high"})
	@WriteEffect({"low", "high"})
	public void ifDoubleCond6(boolean var1Low, boolean var2High) {
		int var3Low = mkLow(42);
		if (var1Low && var2High) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@ParameterSecurity({"low", "low"})
	@WriteEffect({"low", "high"})
	public void ifDoubleCond7(boolean var1Low, boolean var2Low) {
		int var3High = mkHigh(42);
		if (var1Low && var2Low) {
			highField = var3High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		highField = var3High;
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = var3High;
	}
	
	@ParameterSecurity({"high", "high"})
	@WriteEffect({"low", "high"})
	public void ifElseDoubleCond(boolean var1High, boolean var2High) {
		int var3High = mkHigh(42);
		if (var1High && var2High) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		} else {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		highField = var3High;
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = var3High;
	}

	@ParameterSecurity({"high", "high"})
	@WriteEffect({"low", "high"})
	public void ifElseDoubleCond2(boolean var1High, boolean var2High) {
		int var3Low = mkLow(42);
		if (var1High && var2High) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		} else {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@ParameterSecurity({"high", "low"})
	@WriteEffect({"low", "high"})
	public void ifElseDoubleCond3(boolean var1High, boolean var2Low) {
		int var3High = mkHigh(42);
		if (var1High && var2Low) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		} else {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		highField = var3High;
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = var3High;
	}

	@ParameterSecurity({"high", "low"})
	@WriteEffect({"low", "high"})
	public void ifElseDoubleCond4(boolean var1High, boolean var2Low) {
		int var3Low = mkLow(42);
		if (var1High && var2Low) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		} else {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@ParameterSecurity({"low", "high"})
	@WriteEffect({"low", "high"})
	public void ifElseDoubleCond5(boolean var1Low, boolean var2High) {
		int var3High = mkHigh(42);
		if (var1Low && var2High) {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		} else {
			highField = var3High;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		highField = var3High;
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = var3High;
	}

	@ParameterSecurity({"low", "high"})
	@WriteEffect({"low", "high"})
	public void ifElseDoubleCond6(boolean var1Low, boolean var2High) {
		int var3Low = mkLow(42);
		if (var1Low && var2High) {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		} else {
			highField = var3Low;
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@ParameterSecurity({"low", "low"})
	@WriteEffect({"low", "high"})
	public void ifElseDoubleCond7(boolean var1Low, boolean var2Low) {
		int var3High = mkHigh(42);
		if (var1Low && var2Low) {
			highField = var3High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		} else {
			highField = var3High;
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = var3High;
		}
		highField = var3High;
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		lowField = var3High;
	}
	
	@FieldSecurity("low")
	int lowField = mkLow(42);
	
	@FieldSecurity("high")
	int highField = mkHigh(42);
	
	@WriteEffect({"low", "high"})
	public FailIfElse2() {
		super();
	}
	
}
