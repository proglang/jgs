package junitAnalysis;

import security.Definition;
import security.Definition.*;

public class SuccessArray {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int[] arrayAssign() {
		int[] arrayHigh = Definition.highId(new int[42]);
		int varLow = Definition.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh;
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int[] arrayAssign2() {
		int[] arrayLow = Definition.lowId(new int[42]);
		int varLow = Definition.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int[] arrayAssign3() {
		int[] arrayLow = Definition.lowId(new int[42]);
		int varLow = Definition.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow;
	}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int arrayAccess() {
		int[] arrayHigh = Definition.highId(new int[42]);
		int varLow = Definition.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh[23];
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int arrayAccess2() {
		int[] arrayLow = Definition.lowId(new int[42]);
		int varLow = Definition.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow[23];
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int arrayAccess3() {
		int[] arrayLow = Definition.lowId(new int[42]);
		int varLow = Definition.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow[23];
	}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int arrayLength() {
		int[] arrayHigh = Definition.highId(new int[42]);
		int varLow = Definition.lowId(42);
		arrayHigh[23] = varLow;
		return arrayHigh.length;
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int arrayLength2() {
		int[] arrayLow = Definition.lowId(new int[42]);
		int varLow = Definition.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow.length;
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int arrayLength3() {
		int[] arrayLow = Definition.lowId(new int[42]);
		int varLow = Definition.lowId(42);
		arrayLow[23] = varLow;
		return arrayLow.length;
	}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int arrayIndex() {
		int[] arrayHigh = Definition.highId(new int[42]);
		int varLow = Definition.lowId(42);
		int indexHigh = Definition.highId(23);
		arrayHigh[indexHigh] = varLow;
		return arrayHigh[indexHigh];
	}

	@ReturnSecurity("high")
	@WriteEffect({"high"})
	public int arrayIndex2() {
		int[] arrayHigh = Definition.highId(new int[42]);
		int varLow = Definition.lowId(42);
		int indexLow = Definition.lowId(23);
		arrayHigh[indexLow] = varLow;
		return arrayHigh[indexLow];
	}

	@ReturnSecurity("high")
	@WriteEffect({"low"})
	public int arrayIndex3() {
		int[] arrayLow = Definition.lowId(new int[42]);
		int varLow = Definition.lowId(42);
		int indexLow = Definition.lowId(23);
		arrayLow[indexLow] = varLow;
		return arrayLow[indexLow];
	}

	@ReturnSecurity("low")
	@WriteEffect({"low"})
	public int arrayIndex4() {
		int[] arrayLow = Definition.lowId(new int[42]);
		int varLow = Definition.lowId(42);
		int indexLow = Definition.lowId(23);
		arrayLow[indexLow] = varLow;
		return arrayLow[indexLow];
	}
	
}