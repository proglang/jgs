package analysisFail;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

public class FailIfElse {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturnExpr() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		// @security("The returned value has a stronger security level than expected.")
		return conditionHigh ? thenHigh : elseHigh;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturnExpr2() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		// @security("The returned value has a stronger security level than expected.")
		return conditionLow ? thenHigh : elseHigh;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturnExpr3() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		// @security("The returned value has a stronger security level than expected.")
		return conditionHigh ? thenHigh : elseLow;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturnExpr4() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		// @security("The returned value has a stronger security level than expected.")
		return conditionLow ? thenHigh : elseLow;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturnExpr5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		// @security("The returned value has a stronger security level than expected.")
		return conditionHigh ? thenLow : elseHigh;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturnExpr6() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		// @security("The returned value has a stronger security level than expected.")
		return conditionLow ? thenLow : elseHigh;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturnExpr7() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		// @security("The returned value has a stronger security level than expected.")
		return conditionHigh ? thenLow : elseLow;
	}
		
	@Annotations.ReturnSecurity("low")
	public int ifReturn() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @security("The returned value has a stronger security level than expected.")
			return thenHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return elseHigh;
	}

	@Annotations.ReturnSecurity("low")
	public int ifReturn2() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			// @security("The returned value has a stronger security level than expected.")
			return thenHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return elseHigh;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturn3() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @security("The returned value has a stronger security level than expected.")
			return thenHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return elseLow;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturn4() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			// @security("The returned value has a stronger security level than expected.")
			return thenHigh;
		}
		return elseLow;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturn5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @security("The returned value has a stronger security level than expected.")
			return thenLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return elseHigh;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturn6() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return elseHigh;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifReturn7() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @security("The returned value has a stronger security level than expected.")
			return thenLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return elseLow;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseReturn() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @security("The returned value has a stronger security level than expected.")
			return thenHigh;
		} else {
			// @security("The returned value has a stronger security level than expected.")
			return elseHigh;
		}
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseReturn2() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			// @security("The returned value has a stronger security level than expected.")
			return thenHigh;
		} else {
			// @security("The returned value has a stronger security level than expected.")
			return elseHigh;
		}
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseReturn3() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @security("The returned value has a stronger security level than expected.")
			return thenHigh;
		} else {
			// @security("The returned value has a stronger security level than expected.")
			return elseLow;
		}
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseReturn4() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			// @security("The returned value has a stronger security level than expected.")
			return thenHigh;
		} else {
			return elseLow;
		}
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseReturn5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @security("The returned value has a stronger security level than expected.")
			return thenLow;
		} else {
			// @security("The returned value has a stronger security level than expected.")
			return elseHigh;
		}
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseReturn6() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		} else {
			// @security("The returned value has a stronger security level than expected.")
			return elseHigh;
		}
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseReturn7() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @security("The returned value has a stronger security level than expected.")
			return thenLow;
		} else {
			// @security("The returned value has a stronger security level than expected.")
			return elseLow;
		}
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign2() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign3() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign4() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign5() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign6() {
		int result = SootSecurityLevel.highId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign7() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@Annotations.ReturnSecurity("low")
	public int ifElseAssign8() {
		int result = SootSecurityLevel.lowId(42);
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenHigh;
		} else {
			result = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign9() {
		int result = SootSecurityLevel.highId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign10() {
		int result = SootSecurityLevel.highId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign11() {
		int result = SootSecurityLevel.lowId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign12() {
		int result = SootSecurityLevel.lowId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			result = thenLow;
		} else {
			result = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign13() {
		int result = SootSecurityLevel.highId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}
	
	@Annotations.ReturnSecurity("low")
	public int ifElseAssign14() {
		int result = SootSecurityLevel.lowId(42);
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			result = thenLow;
		} else {
			result = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return result;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		} else {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField2() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		} else {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField3() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		} else {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField4() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		} else {
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenLow;
		} else {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField6() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenLow;
		} else {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField7() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenLow;
		} else {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifElseAssignField8() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			highField = thenHigh;
		} else {
			highField = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifElseAssignField9() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			highField = thenHigh;
		} else {
			highField = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField10() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		} else {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField11() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		} else {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifElseAssignField12() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			highField = thenHigh;
		} else {
			highField = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifElseAssignField13() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			highField = thenHigh;
		} else {
			highField = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField14() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		} else {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField15() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenHigh;
		} else {
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifElseAssignField16() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			highField = thenLow;
		} else {
			highField = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifElseAssignField17() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			highField = thenLow;
		} else {
			highField = elseHigh;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField18() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenLow;
		} else {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField19() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenLow;
		} else {
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifElseAssignField20() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			highField = thenLow;
		} else {
			highField = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}
	
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int ifElseAssignField21() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			highField = thenLow;
		} else {
			highField = elseLow;
		}
		// @security("The returned value has a stronger security level than expected.")
		return highField;
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField22() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = thenLow;
		} else {
			// @sideeffect("Write effect inside of a stronger branch")
			// @security("The security level of the assigned value is stronger than the security level of the field.")
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.FieldSecurity("low")
	public int lowField = SootSecurityLevel.lowId(42);
	
	@Annotations.FieldSecurity("high")
	public int highField = SootSecurityLevel.highId(42);
	
	@Annotations.WriteEffect({"low", "high"})
	public FailIfElse() {
		super();
	}
	
}
