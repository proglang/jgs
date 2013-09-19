package taintTrackingSuccess;

import security.Annotations;
import security.SootSecurityLevel;

public class TaintTrackingArray {

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int[] arrayAssign() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayHigh[23] = varHigh;
		return arrayHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int[] arrayAssign2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int[] arrayAssign3() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int[] arrayAssign4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayAccess() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayHigh[23] = varHigh;
		return arrayHigh[23];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayAccess2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh[23];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayAccess3() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow[23];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int arrayAccess4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow[23];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayLength() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		arrayHigh[23] = varHigh;
		return arrayHigh.length;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayLength2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh.length;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayLength3() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow.length;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int arrayLength4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow.length;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayIndex() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayHigh[indexHigh] = varHigh;
		return arrayHigh[indexHigh];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayIndex2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayHigh[indexLow] = varHigh;
		return arrayHigh[indexLow];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayIndex3() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayHigh[indexHigh] = varLow;
		return arrayHigh[indexHigh];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayIndex4() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayHigh[indexLow] = varLow;
		return arrayHigh[indexLow];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayIndex5() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayLow[indexHigh] = varLow;
		return arrayLow[indexHigh];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int arrayIndex6() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayLow[indexLow] = varLow;
		return arrayLow[indexLow];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int arrayIndex7() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayLow[indexHigh] = varLow;
		return arrayLow[indexHigh];
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int arrayIndex8() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayLow[indexLow] = varLow;
		return arrayLow[indexLow];
	}
}
