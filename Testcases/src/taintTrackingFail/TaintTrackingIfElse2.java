package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.FieldSecurity;

public class TaintTrackingIfElse2 {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
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
	
	@FieldSecurity("low")
	int lowField = SootSecurityLevel.lowId(42);
	
	@FieldSecurity("high")
	int highField = SootSecurityLevel.highId(42);
}
