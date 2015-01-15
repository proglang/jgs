package junitLevel;

import static security.Definition.*;

public class SuccessArray {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int[] arrayAssign() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		arrayHigh[23] = varLow;
		return arrayHigh;
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int[] arrayAssign2() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		arrayLow[23] = varLow;
		return arrayLow;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int[] arrayAssign3() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		arrayLow[23] = varLow;
		return arrayLow;
	}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int arrayAccess() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		arrayHigh[23] = varLow;
		return arrayHigh[23];
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int arrayAccess2() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		arrayLow[23] = varLow;
		return arrayLow[23];
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int arrayAccess3() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		arrayLow[23] = varLow;
		return arrayLow[23];
	}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int arrayLength() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		arrayHigh[23] = varLow;
		return arrayHigh.length;
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int arrayLength2() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		arrayLow[23] = varLow;
		return arrayLow.length;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int arrayLength3() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		arrayLow[23] = varLow;
		return arrayLow.length;
	}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int arrayIndex() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		int indexHigh = mkHigh(23);
		arrayHigh[indexHigh] = varLow;
		return arrayHigh[indexHigh];
	}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int arrayIndex2() {
		int[] arrayHigh = mkHigh(new int[42]);
		int varLow = mkLow(42);
		int indexLow = mkLow(23);
		arrayHigh[indexLow] = varLow;
		return arrayHigh[indexLow];
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int arrayIndex3() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		int indexLow = mkLow(23);
		arrayLow[indexLow] = varLow;
		return arrayLow[indexLow];
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int arrayIndex4() {
		int[] arrayLow = mkLow(new int[42]);
		int varLow = mkLow(42);
		int indexLow = mkLow(23);
		arrayLow[indexLow] = varLow;
		return arrayLow[indexLow];
	}
	
}