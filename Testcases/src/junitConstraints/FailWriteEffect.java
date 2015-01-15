package junitConstraints;

import static security.Definition.*;

public class FailWriteEffect extends stubs.Fields {
	
	@FieldSecurity("low")
	public stubs.PCI pci;

	public static void main(String[] args) {}

	public void failWriteEffect1() {
		// @security("Missing write effect")
		lowIField = 42;
	}

	@Constraints("@pc <= high")
	public void failWriteEffect2() {
		// @security("Missing write effect")
		lowIField = 42;
	}

	public void failWriteEffect3() {
		// @security("Missing write effect")
		lowLowIField = arrayIntLow(42);
	}

	@Constraints("@pc <= high")
	public void failWriteEffect4() {
		// @security("Missing write effect")
		lowLowIField = arrayIntLow(42);
	}

	public void failWriteEffect5() {
		// @security("Missing write effect")
		lowLowIField[23] = 42;
	}

	@Constraints("@pc <= high")
	public void failWriteEffect6() {
		// @security("Missing write effect")
		lowLowIField[23] = 42;
	}

	public void failWriteEffect7() {
		// @security("Missing write effect")
		lowHighIField = arrayIntHigh(42);
	}

	@Constraints("@pc <= high")
	public void failWriteEffect8() {
		// @security("Missing write effect")
		lowHighIField = arrayIntHigh(42);
	}

	public void failWriteEffect9() {
		int[] arr = arrayIntLow(42);
		// @security("Missing write effect")
		arr[23] = 42;
	}
	
	@Constraints("@pc <= high")
	public void failWriteEffect10() {
		int[] arr = arrayIntLow(42);
		// @security("Missing write effect")
		arr[23] = 42;
	}
 
	public void failWriteEffect11() {
		// @security("Missing write effect")
		pci.lowPC();
	}
	
	@Constraints("@pc <= high")
	public void failWriteEffect12() {
		// @security("Missing write effect")
		pci.lowPC();
	}
	
	public void failWriteEffect13() {
		// @security("Missing write effect")
		stubs.PCSHigh.lowPC();
	}
	
	@Constraints("@pc <= high")
	public void failWriteEffect14() {
		// @security("Missing write effect")
		stubs.PCSHigh.lowPC();
	}
	
	public void failWriteEffect15() {
		// @security("Missing write effect")
		stubs.PCSLow.lowPC();
	}
	
	@Constraints("@pc <= high")
	public void failWriteEffect16() {
		// @security("Missing write effect")
		stubs.PCSLow.lowPC();
	}
	
	public void failWriteEffect17() {
		// @security("Missing write effect")
		stubs.PCSLow.highPC();
	}
	
	@Constraints("@pc <= high")
	public void failWriteEffect18() {
		// @security("Missing write effect")
		stubs.PCSLow.highPC();
	}
	
	@SuppressWarnings("unused")
	public void failWriteEffect19() {
		// @security("Missing write effect")
		int i = stubs.PCSLow.lowSField;
	}
	
	@SuppressWarnings("unused")
	@Constraints("@pc <= high")
	public void failWriteEffect20() {
		// @security("Missing write effect")
		int i = stubs.PCSLow.lowSField;
	}

	@SuppressWarnings("unused")
	public void failWriteEffect21() {
		// @security("Missing write effect")
		int i = stubs.PCSLow.highSField;
	}

	@SuppressWarnings("unused")
	@Constraints("@pc <= high")
	public void failWriteEffect22() {
		// @security("Missing write effect")
		int i = stubs.PCSLow.highSField;
	}

}
