package analysisFail;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

public class FailArray {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "low" })
	public int[] arrayAssign() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow;
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int[] arrayAssign2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh;
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int[] arrayAssign3() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh;
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "low" })
	public int[] arrayAssign4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "high" })
	public int[] arrayAssign5() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		return arrayHigh;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "low" })
	public int arrayAccess() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow[23];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int arrayAccess2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[23];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int arrayAccess3() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[23];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "low" })
	public int arrayAccess4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow[23];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "high" })
	public int arrayAccess5() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		return arrayHigh[23];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "low" })
	public int arrayLength() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow.length;
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int arrayLength2() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh.length;
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int arrayLength3() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		arrayHigh[23] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh.length;
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "low" })
	public int arrayLength4() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow.length;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "high" })
	public int arrayLength5() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		return arrayHigh.length;
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "low" })
	public int arrayIndex() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		// @security("Level of the index of an assigned field has to be weaker or equal to the level of the array.")
		arrayLow[indexHigh] = varHigh;
		return arrayLow[indexHigh];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "low" })
	public int arrayIndex2() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[indexLow] = varHigh;
		return arrayLow[indexLow];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int arrayIndex3() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[indexHigh] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[indexHigh];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int arrayIndex4() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[indexLow] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[indexLow];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int arrayIndex5() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		arrayHigh[indexHigh] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[indexHigh];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "high" })
	public int arrayIndex6() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		arrayHigh[indexLow] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[indexLow];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "low" })
	public int arrayIndex7() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		// @security("Level of the index of an assigned field has to be weaker or equal to the level of the array.")
		arrayLow[indexHigh] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayLow[indexHigh];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "low" })
	public int arrayIndex8() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[indexLow] = varHigh;
		return arrayLow[indexLow];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "high" })
	public int arrayIndex9() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[indexHigh] = varHigh;
		return arrayHigh[indexHigh];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "high" })
	public int arrayIndex10() {
		int[] arrayHigh = SootSecurityLevel.highId(new int[42]);
		int varHigh = SootSecurityLevel.highId(42);
		int indexLow = SootSecurityLevel.lowId(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[indexLow] = varHigh;
		return arrayHigh[indexLow];
	}

	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({ "low" })
	public int arrayIndex11() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		// @security("Level of the index of an assigned field has to be weaker or equal to the level of the array.")
		arrayLow[indexHigh] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayLow[indexHigh];
	}

	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({ "low" })
	public int arrayIndex12() {
		int[] arrayLow = SootSecurityLevel.lowId(new int[42]);
		int varLow = SootSecurityLevel.lowId(42);
		int indexHigh = SootSecurityLevel.highId(23);
		// @security("Level of the index of an assigned field has to be weaker or equal to the level of the array.")
		arrayLow[indexHigh] = varLow;
		return arrayLow[indexHigh];
	}

}