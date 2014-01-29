package analysisSuccess;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

public class SuccessArray {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int[] arrayAssign() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int[] arrayAssign2() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow;
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int[] arrayAssign3() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int arrayAccess() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh[23];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int arrayAccess2() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow[23];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int arrayAccess3() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow[23];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int arrayLength() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh.length;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int arrayLength2() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow.length;
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int arrayLength3() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow.length;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int arrayIndex() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayHigh[indexHigh] = varLow;
		return arrayHigh[indexHigh];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"high"})
	public int arrayIndex2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayHigh[indexLow] = varLow;
		return arrayHigh[indexLow];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({"low"})
	public int arrayIndex3() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayLow[indexLow] = varLow;
		return arrayLow[indexLow];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({"low"})
	public int arrayIndex4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayLow[indexLow] = varLow;
		return arrayLow[indexLow];
	}
	
}