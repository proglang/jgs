package junitAnalysis;

import static security.Definition.*;

public class FailArray {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ReturnSecurity("high")
	@WriteEffect({ "low" })
	public int[] arrayAssign() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow;
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int[] arrayAssign2() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh;
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int[] arrayAssign3() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		arrayHigh[23] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh;
	}

	@ReturnSecurity("low")
	@WriteEffect({ "low" })
	public int[] arrayAssign4() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow;
	}

	@ReturnSecurity("high")
	@WriteEffect({ "high" })
	public int[] arrayAssign5() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		return arrayHigh;
	}

	@ReturnSecurity("high")
	@WriteEffect({ "low" })
	public int arrayAccess() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow[23];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int arrayAccess2() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[23];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int arrayAccess3() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		arrayHigh[23] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[23];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "low" })
	public int arrayAccess4() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow[23];
	}

	@ReturnSecurity("high")
	@WriteEffect({ "high" })
	public int arrayAccess5() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		return arrayHigh[23];
	}

	@ReturnSecurity("high")
	@WriteEffect({ "low" })
	public int arrayLength() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow.length;
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int arrayLength2() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh.length;
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int arrayLength3() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		arrayHigh[23] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh.length;
	}

	@ReturnSecurity("low")
	@WriteEffect({ "low" })
	public int arrayLength4() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[23] = varHigh;
		return arrayLow.length;
	}

	@ReturnSecurity("high")
	@WriteEffect({ "high" })
	public int arrayLength5() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[23] = varHigh;
		return arrayHigh.length;
	}

	@ReturnSecurity("high")
	@WriteEffect({ "low" })
	public int arrayIndex() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		int indexHigh = mkHigh(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		// @security("Level of the index of an assigned field has to be weaker or equal to the level of the array.")
		arrayLow[indexHigh] = varHigh;
		return arrayLow[indexHigh];
	}

	@ReturnSecurity("high")
	@WriteEffect({ "low" })
	public int arrayIndex2() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		int indexLow = mkLow(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[indexLow] = varHigh;
		return arrayLow[indexLow];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int arrayIndex3() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		int indexHigh = mkHigh(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[indexHigh] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[indexHigh];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int arrayIndex4() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		int indexLow = mkLow(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[indexLow] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[indexLow];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int arrayIndex5() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		int indexHigh = mkHigh(23);
		arrayHigh[indexHigh] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[indexHigh];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "high" })
	public int arrayIndex6() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		int indexLow = mkLow(23);
		arrayHigh[indexLow] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayHigh[indexLow];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "low" })
	public int arrayIndex7() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		int indexHigh = mkHigh(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		// @security("Level of the index of an assigned field has to be weaker or equal to the level of the array.")
		arrayLow[indexHigh] = varHigh;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayLow[indexHigh];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "low" })
	public int arrayIndex8() {
		int[] arrayLow = mkLow(new int[42]);
		int varHigh = mkHigh(42);
		int indexLow = mkLow(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		// @security("The level of the assigned value has to be weaker or equal to the level of the array.")
		arrayLow[indexLow] = varHigh;
		return arrayLow[indexLow];
	}

	@ReturnSecurity("high")
	@WriteEffect({ "high" })
	public int arrayIndex9() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		int indexHigh = mkHigh(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[indexHigh] = varHigh;
		return arrayHigh[indexHigh];
	}

	@ReturnSecurity("high")
	@WriteEffect({ "high" })
	public int arrayIndex10() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varHigh = mkHigh(42);
		int indexLow = mkLow(23);
		// @security("Only assignment of values with the weakest level are allowed to fields of an array.")
		arrayHigh[indexLow] = varHigh;
		return arrayHigh[indexLow];
	}

	@ReturnSecurity("low")
	@WriteEffect({ "low" })
	public int arrayIndex11() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		int indexHigh = mkHigh(23);
		// @security("Level of the index of an assigned field has to be weaker or equal to the level of the array.")
		arrayLow[indexHigh] = varLow;
		// @security("The level of a returned value has to be weaker or equal to the expected return level.")
		return arrayLow[indexHigh];
	}

	@ReturnSecurity("high")
	@WriteEffect({ "low" })
	public int arrayIndex12() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		int indexHigh = mkHigh(23);
		// @security("Level of the index of an assigned field has to be weaker or equal to the level of the array.")
		arrayLow[indexHigh] = varLow;
		return arrayLow[indexHigh];
	}

}