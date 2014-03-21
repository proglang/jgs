package junitAnalysis;

import security.Definition;
import security.Definition.*;

public class FailIfElse2 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int ifAssign() {
		int thenHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int thenHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
		if (conditionLow) {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		}
		return lowField;
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int ifAssign3() {
		int thenLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int thenHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
		if (conditionHigh) {
			highField = thenHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int ifAssign5() {
		int thenHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int thenHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
		if (conditionLow) {
			highField = thenHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int ifAssign7() {
		int thenHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
		if (conditionLow) {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		}
		return lowField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"high"})
	public int ifAssign8() {
		int thenLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
		if (conditionHigh) {
			highField = thenLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int ifAssign9() {
		int thenLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int thenLow = Definition.lowId(42);
		boolean conditionLow = Definition.lowId(false);
		if (conditionLow) {
			highField = thenLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}
	
	@ReturnSecurity("low")
	public int ifExprAssign() {
		int result = Definition.highId(42);
		int thenHigh = Definition.highId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
		result = conditionHigh ? thenHigh : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign2() {
		int result = Definition.highId(42);
		int thenHigh = Definition.highId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
		result = conditionLow ? thenHigh : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign3() {
		int result = Definition.lowId(42);
		int thenHigh = Definition.highId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
		result = conditionHigh ? thenHigh : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign4() {
		int result = Definition.lowId(42);
		int thenHigh = Definition.highId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
		result = conditionLow ? thenHigh : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign5() {
		int result = Definition.highId(42);
		int thenHigh = Definition.highId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
		result = conditionHigh ? thenHigh : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign6() {
		int result = Definition.highId(42);
		int thenHigh = Definition.highId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionLow = Definition.lowId(false);
		result = conditionLow ? thenHigh : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign7() {
		int result = Definition.lowId(42);
		int thenHigh = Definition.highId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
		result = conditionHigh ? thenHigh : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign8() {
		int result = Definition.lowId(42);
		int thenHigh = Definition.highId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionLow = Definition.lowId(false);
		result = conditionLow ? thenHigh : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign9() {
		int result = Definition.highId(42);
		int thenLow = Definition.lowId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
		result = conditionHigh ? thenLow : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign10() {
		int result = Definition.highId(42);
		int thenLow = Definition.lowId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
		result = conditionLow ? thenLow : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign11() {
		int result = Definition.lowId(42);
		int thenLow = Definition.lowId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
		result = conditionHigh ? thenLow : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign12() {
		int result = Definition.lowId(42);
		int thenLow = Definition.lowId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
		result = conditionLow ? thenLow : elseHigh;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign13() {
		int result = Definition.highId(42);
		int thenLow = Definition.lowId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
		result = conditionHigh ? thenLow : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@ReturnSecurity("low")
	public int ifExprAssign14() {
		int result = Definition.lowId(42);
		int thenLow = Definition.lowId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
		result = conditionHigh ? thenLow : elseLow;
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@WriteEffect({"low"})
	public void ifElseAssignLocalField2() {
		int result = Definition.highId(42);
		int thenHigh = Definition.highId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int result = Definition.highId(42);
		int thenHigh = Definition.highId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
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
		int result = Definition.highId(42);
		int thenHigh = Definition.highId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int result = Definition.highId(42);
		int thenHigh = Definition.highId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionLow = Definition.lowId(false);
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
		int result = Definition.highId(42);
		int thenLow = Definition.lowId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int result = Definition.highId(42);
		int thenLow = Definition.lowId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
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
		int result = Definition.highId(42);
		int thenLow = Definition.lowId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int result = Definition.lowId(42);
		int thenHigh = Definition.highId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int result = Definition.lowId(42);
		int thenHigh = Definition.highId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
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
		int result = Definition.lowId(42);
		int thenHigh = Definition.highId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int result = Definition.lowId(42);
		int thenHigh = Definition.highId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionLow = Definition.lowId(false);
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
		int result = Definition.lowId(42);
		int thenLow = Definition.lowId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int result = Definition.lowId(42);
		int thenLow = Definition.lowId(42);
		int elseHigh = Definition.highId(42);
		boolean conditionLow = Definition.lowId(false);
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
		int result = Definition.lowId(42);
		int thenLow = Definition.lowId(42);
		int elseLow = Definition.lowId(42);
		boolean conditionHigh = Definition.highId(false);
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
		int var3High = Definition.highId(42);
		int var4High = Definition.highId(42);
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
		int var3High = Definition.highId(42);
		int var4Low = Definition.lowId(42);
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
		int var3Low = Definition.lowId(42);
		int var4High = Definition.highId(42);
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
		int var3Low = Definition.lowId(42);
		int var4Low = Definition.lowId(42);
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
		int var3High = Definition.highId(42);
		int var4High = Definition.highId(42);
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
		int var3High = Definition.highId(42);
		int var4Low = Definition.lowId(42);
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
		int var3Low = Definition.lowId(42);
		int var4High = Definition.highId(42);
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
		int var3Low = Definition.lowId(42);
		int var4Low = Definition.lowId(42);
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
		int var3High = Definition.highId(42);
		int var4High = Definition.highId(42);
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
		int var3High = Definition.highId(42);
		int var4Low = Definition.lowId(42);
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
		int var3Low = Definition.lowId(42);
		int var4High = Definition.highId(42);
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
		int var3Low = Definition.lowId(42);
		int var4Low = Definition.lowId(42);
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
		int var3High = Definition.highId(42);
		int var4High = Definition.highId(42);
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
		int var3High = Definition.highId(42);
		int var4Low = Definition.lowId(42);
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
		int var3Low = Definition.lowId(42);
		int var4High = Definition.highId(42);
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
		int var3High = Definition.highId(42);
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
		int var3Low = Definition.lowId(42);
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
		int var3High = Definition.highId(42);
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
		int var3Low = Definition.lowId(42);
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
		int var3High = Definition.highId(42);
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
		int var3Low = Definition.lowId(42);
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
		int var3High = Definition.highId(42);
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
		int var3High = Definition.highId(42);
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
		int var3Low = Definition.lowId(42);
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
		int var3High = Definition.highId(42);
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
		int var3Low = Definition.lowId(42);
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
		int var3High = Definition.highId(42);
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
		int var3Low = Definition.lowId(42);
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
		int var3High = Definition.highId(42);
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
	int lowField = Definition.lowId(42);
	
	@FieldSecurity("high")
	int highField = Definition.highId(42);
	
	@WriteEffect({"low", "high"})
	public FailIfElse2() {
		super();
	}
	
}
