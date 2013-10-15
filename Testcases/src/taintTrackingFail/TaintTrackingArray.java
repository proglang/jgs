package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingArray {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int[] arrayAssign() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayLow[23] = varHigh;
		return arrayLow;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int[] arrayAssign2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayHigh[23] = varHigh;
		return arrayHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int[] arrayAssign3() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int[] arrayAssign4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayLow[23] = varHigh;
		return arrayLow;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int[] arrayAssign5() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayHigh[23] = varHigh;
		return arrayHigh;
	}	

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int arrayAccess() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayLow[23] = varHigh;
		return arrayLow[23];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int arrayAccess2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayHigh[23] = varHigh;
		return arrayHigh[23];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int arrayAccess3() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh[23];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int arrayAccess4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayLow[23] = varHigh;
		return arrayLow[23];
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int arrayAccess5() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayHigh[23] = varHigh;
		return arrayHigh[23];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int arrayLength() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayLow[23] = varHigh;
		return arrayLow.length;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int arrayLength2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayHigh[23] = varHigh;
		return arrayHigh.length;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int arrayLength3() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh.length;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int arrayLength4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayLow[23] = varHigh;
		return arrayLow.length;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int arrayLength5() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayHigh[23] = varHigh;
		return arrayHigh.length;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int arrayIndex() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayLow[indexHigh] = varHigh;
		return arrayLow[indexHigh];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int arrayIndex2() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayLow[indexLow] = varHigh;
		return arrayLow[indexLow];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int arrayIndex3() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayHigh[indexHigh] = varHigh;
		return arrayHigh[indexHigh];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int arrayIndex4() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayHigh[indexLow] = varHigh;
		return arrayHigh[indexLow];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int arrayIndex5() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayHigh[indexHigh] = varLow;
		return arrayHigh[indexHigh];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"high"})
	public int arrayIndex6() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayHigh[indexLow] = varLow;
		return arrayHigh[indexLow];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int arrayIndex7() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayLow[indexHigh] = varHigh;
		return arrayLow[indexHigh];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int arrayIndex8() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayLow[indexLow] = varHigh;
		return arrayLow[indexLow];
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int arrayIndex9() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayHigh[indexHigh] = varHigh;
		return arrayHigh[indexHigh];
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int arrayIndex10() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayHigh[indexLow] = varHigh;
		return arrayHigh[indexLow];
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int arrayIndex11() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayLow[indexHigh] = varLow;
		return arrayLow[indexHigh];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int arrayIndex12() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayLow[indexHigh] = varLow;
		return arrayLow[indexHigh];
	}
	
}
