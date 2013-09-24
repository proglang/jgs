package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingIfElse2 {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifAssign() {
		int thenHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenHigh;
		}
		return lowField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifAssign2() {
		int thenHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenHigh;
		}
		return lowField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifAssign3() {
		int thenLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenLow;
		}
		return lowField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifAssign4() {
		int thenHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			highField = thenHigh;
		}
		return highField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifAssign5() {
		int thenHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenHigh;
		}
		return lowField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifAssign6() {
		int thenHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			highField = thenHigh;
		}
		return highField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifAssign7() {
		int thenHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenHigh;
		}
		return lowField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifAssign8() {
		int thenLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			highField = thenLow;
		}
		return highField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifAssign9() {
		int thenLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenLow;
		}
		return lowField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifAssign10() {
		int thenLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			highField = thenLow;
		}
		return highField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		result = conditionHigh ? thenHigh : elseHigh;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign2() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		result = conditionLow ? thenHigh : elseHigh;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign3() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		result = conditionHigh ? thenHigh : elseHigh;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign4() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		result = conditionLow ? thenHigh : elseHigh;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign5() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		result = conditionHigh ? thenHigh : elseLow;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign6() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		result = conditionLow ? thenHigh : elseLow;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign7() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		result = conditionHigh ? thenHigh : elseLow;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign8() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		result = conditionLow ? thenHigh : elseLow;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign9() {
		int result = SootSecurityLevel.highId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		result = conditionHigh ? thenLow : elseHigh;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign10() {
		int result = SootSecurityLevel.highId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		result = conditionLow ? thenLow : elseHigh;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign11() {
		int result = SootSecurityLevel.lowId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		result = conditionHigh ? thenLow : elseHigh;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign12() {
		int result = SootSecurityLevel.lowId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		result = conditionLow ? thenLow : elseHigh;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign13() {
		int result = SootSecurityLevel.highId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		result = conditionHigh ? thenLow : elseLow;
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifExprAssign14() {
		int result = SootSecurityLevel.lowId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		result = conditionHigh ? thenLow : elseLow;
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField2() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField4() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField6() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField8() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField10() {
		int result = SootSecurityLevel.highId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField12() {
		int result = SootSecurityLevel.highId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField14() {
		int result = SootSecurityLevel.highId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseLow;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField18() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField20() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField22() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField24() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField26() {
		int result = SootSecurityLevel.lowId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField28() {
		int result = SootSecurityLevel.lowId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		lowField = result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void ifElseAssignLocalField30() {
		int result = SootSecurityLevel.lowId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseLow;
		}
		lowField = result;
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf(boolean var1High, boolean var2High) {
		int var3High = SootSecurityLevel.highId(42);
		int var4High = SootSecurityLevel.highId(42);
		if (var1High) {
			highField = var3High;
			lowField = var3High;
		}
		if (var2High) {
			highField = var4High;
			lowField = var4High;
		}
	}

	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf2(boolean var1High, boolean var2High) {
		int var3High = SootSecurityLevel.highId(42);
		int var4Low = SootSecurityLevel.lowId(42);
		if (var1High) {
			highField = var3High;
			lowField = var3High;
		}
		if (var2High) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf3(boolean var1High, boolean var2High) {
		int var3Low = SootSecurityLevel.lowId(42);
		int var4High = SootSecurityLevel.highId(42);
		if (var1High) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2High) {
			highField = var4High;
			lowField = var4High;
		}
	}

	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf4(boolean var1High, boolean var2High) {
		int var3Low = SootSecurityLevel.lowId(42);
		int var4Low = SootSecurityLevel.lowId(42);
		if (var1High) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2High) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf5(boolean var1High, boolean var2Low) {
		int var3High = SootSecurityLevel.highId(42);
		int var4High = SootSecurityLevel.highId(42);
		if (var1High) {
			highField = var3High;
			lowField = var3High;
		}
		if (var2Low) {
			highField = var4High;
			lowField = var4High;
		}
	}

	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf6(boolean var1High, boolean var2Low) {
		int var3High = SootSecurityLevel.highId(42);
		int var4Low = SootSecurityLevel.lowId(42);
		if (var1High) {
			highField = var3High;
			lowField = var3High;
		}
		if (var2Low) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf7(boolean var1High, boolean var2Low) {
		int var3Low = SootSecurityLevel.lowId(42);
		int var4High = SootSecurityLevel.highId(42);
		if (var1High) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2Low) {
			highField = var4High;
			lowField = var4High;
		}
	}

	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf8(boolean var1High, boolean var2Low) {
		int var3Low = SootSecurityLevel.lowId(42);
		int var4Low = SootSecurityLevel.lowId(42);
		if (var1High) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2Low) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf9(boolean var1Low, boolean var2High) {
		int var3High = SootSecurityLevel.highId(42);
		int var4High = SootSecurityLevel.highId(42);
		if (var1Low) {
			highField = var3High;
			lowField = var3High;
		}
		if (var2High) {
			highField = var4High;
			lowField = var4High;
		}
	}

	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf10(boolean var1Low, boolean var2High) {
		int var3High = SootSecurityLevel.highId(42);
		int var4Low = SootSecurityLevel.lowId(42);
		if (var1Low) {
			highField = var3High;
			lowField = var3High;
		}
		if (var2High) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf11(boolean var1Low, boolean var2High) {
		int var3Low = SootSecurityLevel.lowId(42);
		int var4High = SootSecurityLevel.highId(42);
		if (var1Low) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2High) {
			highField = var4High;
			lowField = var4High;
		}
	}

	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf12(boolean var1Low, boolean var2High) {
		int var3Low = SootSecurityLevel.lowId(42);
		int var4Low = SootSecurityLevel.lowId(42);
		if (var1Low) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2High) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf13(boolean var1Low, boolean var2Low) {
		int var3High = SootSecurityLevel.highId(42);
		int var4High = SootSecurityLevel.highId(42);
		if (var1Low) {
			highField = var3High;
			lowField = var3High;
		}
		if (var2Low) {
			highField = var4High;
			lowField = var4High;
		}
	}

	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf14(boolean var1Low, boolean var2Low) {
		int var3High = SootSecurityLevel.highId(42);
		int var4Low = SootSecurityLevel.lowId(42);
		if (var1Low) {
			highField = var3High;
			lowField = var3High;
		}
		if (var2Low) {
			highField = var4Low;
			lowField = var4Low;
		}
	}

	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifIf15(boolean var1Low, boolean var2Low) {
		int var3Low = SootSecurityLevel.lowId(42);
		int var4High = SootSecurityLevel.highId(42);
		if (var1Low) {
			highField = var3Low;
			lowField = var3Low;
		}
		if (var2Low) {
			highField = var4High;
			lowField = var4High;
		}
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifDoubleCond(boolean var1High, boolean var2High) {
		int var3High = SootSecurityLevel.highId(42);
		if (var1High && var2High) {
			highField = var3High;
			lowField = var3High;
		}
		highField = var3High;
		lowField = var3High;
	}

	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifDoubleCond2(boolean var1High, boolean var2High) {
		int var3Low = SootSecurityLevel.lowId(42);
		if (var1High && var2High) {
			highField = var3Low;
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifDoubleCond3(boolean var1High, boolean var2Low) {
		int var3High = SootSecurityLevel.highId(42);
		if (var1High && var2Low) {
			highField = var3High;
			lowField = var3High;
		}
		highField = var3High;
		lowField = var3High;
	}

	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifDoubleCond4(boolean var1High, boolean var2Low) {
		int var3Low = SootSecurityLevel.lowId(42);
		if (var1High && var2Low) {
			highField = var3Low;
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifDoubleCond5(boolean var1Low, boolean var2High) {
		int var3High = SootSecurityLevel.highId(42);
		if (var1Low && var2High) {
			highField = var3High;
			lowField = var3High;
		}
		highField = var3High;
		lowField = var3High;
	}

	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifDoubleCond6(boolean var1Low, boolean var2High) {
		int var3Low = SootSecurityLevel.lowId(42);
		if (var1Low && var2High) {
			highField = var3Low;
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifDoubleCond7(boolean var1Low, boolean var2Low) {
		int var3High = SootSecurityLevel.highId(42);
		if (var1Low && var2Low) {
			highField = var3High;
			lowField = var3High;
		}
		highField = var3High;
		lowField = var3High;
	}
	
	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifElseDoubleCond(boolean var1High, boolean var2High) {
		int var3High = SootSecurityLevel.highId(42);
		if (var1High && var2High) {
			highField = var3High;
			lowField = var3High;
		} else {
			highField = var3High;
			lowField = var3High;
		}
		highField = var3High;
		lowField = var3High;
	}

	@Annotations.ParameterSecurity({"high", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifElseDoubleCond2(boolean var1High, boolean var2High) {
		int var3Low = SootSecurityLevel.lowId(42);
		if (var1High && var2High) {
			highField = var3Low;
			lowField = var3Low;
		} else {
			highField = var3Low;
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifElseDoubleCond3(boolean var1High, boolean var2Low) {
		int var3High = SootSecurityLevel.highId(42);
		if (var1High && var2Low) {
			highField = var3High;
			lowField = var3High;
		} else {
			highField = var3High;
			lowField = var3High;
		}
		highField = var3High;
		lowField = var3High;
	}

	@Annotations.ParameterSecurity({"high", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifElseDoubleCond4(boolean var1High, boolean var2Low) {
		int var3Low = SootSecurityLevel.lowId(42);
		if (var1High && var2Low) {
			highField = var3Low;
			lowField = var3Low;
		} else {
			highField = var3Low;
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifElseDoubleCond5(boolean var1Low, boolean var2High) {
		int var3High = SootSecurityLevel.highId(42);
		if (var1Low && var2High) {
			highField = var3High;
			lowField = var3High;
		} else {
			highField = var3High;
			lowField = var3High;
		}
		highField = var3High;
		lowField = var3High;
	}

	@Annotations.ParameterSecurity({"low", "high"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifElseDoubleCond6(boolean var1Low, boolean var2High) {
		int var3Low = SootSecurityLevel.lowId(42);
		if (var1Low && var2High) {
			highField = var3Low;
			lowField = var3Low;
		} else {
			highField = var3Low;
			lowField = var3Low;
		}
		highField = var3Low;
		lowField = var3Low;
	}

	@Annotations.ParameterSecurity({"low", "low"})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void ifElseDoubleCond7(boolean var1Low, boolean var2Low) {
		int var3High = SootSecurityLevel.highId(42);
		if (var1Low && var2Low) {
			highField = var3High;
			lowField = var3High;
		} else {
			highField = var3High;
			lowField = var3High;
		}
		highField = var3High;
		lowField = var3High;
	}
	
	@Annotations.FieldSecurity("low")
	int lowField = SootSecurityLevel.lowId(42);
	
	@Annotations.FieldSecurity("high")
	int highField = SootSecurityLevel.highId(42);
	
	@Annotations.ParameterSecurity({})
	@Annotations.WriteEffect({"low", "high"})
	public TaintTrackingIfElse2() {
		super();
	}
	
}
