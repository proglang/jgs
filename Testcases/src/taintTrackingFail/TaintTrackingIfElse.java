package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingIfElse {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturnExpr() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenHigh : elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturnExpr2() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenHigh : elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturnExpr3() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenHigh : elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturnExpr4() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenHigh : elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturnExpr5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenLow : elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturnExpr6() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenLow : elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturnExpr7() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenLow : elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturnExpr8() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenHigh : elseHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturnExpr9() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenHigh : elseHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturnExpr10() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenHigh : elseLow;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturnExpr11() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenHigh : elseLow;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturnExpr12() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenLow : elseHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturnExpr13() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenLow : elseHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturnExpr14() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenLow : elseLow;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturnExpr15() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenLow : elseLow;
	}	
		
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturn() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenHigh;
		}
		return elseHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturn2() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		}
		return elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturn3() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenHigh;
		}
		return elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturn4() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		}
		return elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturn5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		}
		return elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturn6() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		}
		return elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifReturn7() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		}
		return elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturn8() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenHigh;
		}
		return elseHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturn9() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		}
		return elseHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturn10() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenHigh;
		}
		return elseLow;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturn11() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		}
		return elseLow;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturn12() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		}
		return elseHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturn13() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		}
		return elseHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturn14() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		}
		return elseLow;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifReturn15() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		}
		return elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifElseReturn() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenHigh;
		} else {
			return elseHigh;
		}
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifElseReturn2() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		} else {
			return elseHigh;
		}
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifElseReturn3() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenHigh;
		} else {
			return elseLow;
		}
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifElseReturn4() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		} else {
			return elseLow;
		}
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifElseReturn5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		} else {
			return elseHigh;
		}
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifElseReturn6() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		} else {
			return elseHigh;
		}
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int ifElseReturn7() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		} else {
			return elseLow;
		}
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifElseReturn8() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenHigh;
		} else {
			return elseHigh;
		}
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifElseReturn9() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		} else {
			return elseHigh;
		}
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifElseReturn10() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenHigh;
		} else {
			return elseLow;
		}
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifElseReturn11() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		} else {
			return elseLow;
		}
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifElseReturn12() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		} else {
			return elseHigh;
		}
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifElseReturn13() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		} else {
			return elseHigh;
		}
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifElseReturn14() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		} else {
			return elseLow;
		}
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public int ifElseReturn15() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		} else {
			return elseLow;
		}
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
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
		return result;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenHigh;
		} else {
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField2() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenHigh;
		} else {
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField3() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenHigh;
		} else {
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField4() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenHigh;
		} else {
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenLow;
		} else {
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField6() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenLow;
		} else {
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField7() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenLow;
		} else {
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
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
		return highField;
	}
	
	@Annotations.ParameterSecurity({})
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
		return highField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField10() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenHigh;
		} else {
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField11() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenHigh;
		} else {
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
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
		return highField;
	}
	
	@Annotations.ParameterSecurity({})
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
		return highField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField14() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenHigh;
		} else {
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField15() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenHigh;
		} else {
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
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
		return highField;
	}
	
	@Annotations.ParameterSecurity({})
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
		return highField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField18() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenLow;
		} else {
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField19() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			lowField = thenLow;
		} else {
			lowField = elseHigh;
		}
		return lowField;
	}
	
	@Annotations.ParameterSecurity({})
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
		return highField;
	}
	
	@Annotations.ParameterSecurity({})
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
		return highField;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int ifElseAssignField22() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			lowField = thenLow;
		} else {
			lowField = elseLow;
		}
		return lowField;
	}
	
	@Annotations.FieldSecurity("low")
	public int lowField = SootSecurityLevel.lowId(42);
	
	@Annotations.FieldSecurity("high")
	public int highField = SootSecurityLevel.highId(42);
	
	@Annotations.ParameterSecurity({})
	@Annotations.WriteEffect({"low", "high"})
	public TaintTrackingIfElse() {
		super();
	}
	
}
