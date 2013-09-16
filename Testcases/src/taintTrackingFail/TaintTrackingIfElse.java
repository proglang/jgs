package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

public class TaintTrackingIfElse {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int IfExpr() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenLow : elseHigh;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int IfExpr2() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		return conditionLow ? thenHigh : elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int IfExpr3() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenLow : elseLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int IfExpr4() {
		int thenLow = SootSecurityLevel.lowId(42);
		int elseHigh = SootSecurityLevel.highId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		return conditionHigh ? thenLow : elseHigh;
	}
	
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int ifStmt() {
		int thenLow = SootSecurityLevel.lowId(42);
		boolean conditionHigh = SootSecurityLevel.highId(false);
		if (conditionHigh) {
			return thenLow;
		}
		return thenLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int ifStmt2() {
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
	public int ifStmt3() {
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
	public int ifStmt4() {
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
	public int ifStmt5() {
		int thenHigh = SootSecurityLevel.highId(42);
		int elseLow = SootSecurityLevel.lowId(42);
		boolean conditionLow = SootSecurityLevel.lowId(false);
		if (conditionLow) {
			return thenHigh;
		}
		return elseLow;
	}

}
