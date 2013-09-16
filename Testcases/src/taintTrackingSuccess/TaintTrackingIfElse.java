package taintTrackingSuccess;

import security.Annotations;
import security.SootSecurityLevel;

public class TaintTrackingIfElse {
	
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int IfExpr() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenLow : elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int IfExpr2() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenLow : elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int IfExpr3() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenHigh : elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int IfExpr4() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenLow : elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int IfExpr5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenLow : elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int ifStmt() {
		int thenLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		}
		return thenLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int ifStmt2() {
		int thenLow = SootSecurityLevel.lowId(42);
		int afterLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		}
		return afterLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int ifStmt3() {
		int thenLow = SootSecurityLevel.lowId(42);
		int afterHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		}
		return afterHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int ifStmt4() {
		int thenLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		}
		return thenLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int ifStmt5() {
		int thenLow = SootSecurityLevel.lowId(42);
		int afterLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		}
		return afterLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int ifStmt6() {
		int thenLow = SootSecurityLevel.lowId(42);
		int afterHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		}
		return afterHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int ifStmt7() {
		int thenLow = SootSecurityLevel.lowId(42);
		int afterHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		}
		return afterHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int ifStmt8() {
		int thenHigh = SootSecurityLevel.highId(42);
		int afterLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenHigh;
		}
		return afterLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int ifStmt9() {
		int thenLow = SootSecurityLevel.lowId(42);
		int afterHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenLow;
		}
		return afterHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int ifStmt10() {
		int thenHigh = SootSecurityLevel.highId(42);
		int afterLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		}
		return afterLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int ifElseStmt() {
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
	@Annotations.ReturnSecurity("high")
	public int ifElseStmt2() {
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
	@Annotations.ReturnSecurity("high")
	public int ifElseStmt3() {
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
	@Annotations.ReturnSecurity("high")
	public int ifElseStmt4() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		} else {
			return elseLow;
		}
	}
	
	// TODO: Assign (Local, Field or Method) to Local or Field
	
	// TODO: IF ELSE
	
	// TODO: Multiple condition values

}
