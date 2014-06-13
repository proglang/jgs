package junitConstraints;

import static security.Definition.*;
import security.Definition.FieldSecurity;

public class FailField {

	public static void main(String[] args) {}

	@FieldSecurity("low")
	public int lowIField;

	@FieldSecurity("low")
	public static int lowSField;

	@FieldSecurity("high")
	public int highIField;

	@FieldSecurity("high")
	public static int highSField;

	// IMPORTANT "@pc <= high" is always valid
	public void failField1() {
		highIField = mkLow(42);
		// @tautology("Missing write effect to high")
		return;
	}

	public void failField2() {
		lowIField = mkLow(42);
		// @security("Missing write effect to low")
		return;
	}

	@Constraints("@pc <= low")
	public void failField3() {
		// @security("Assignment of high value to low field")
		// @security("Double error")
		lowIField = mkHigh(42); // FIXME
	}

	@Constraints("low <= @return")
	public int failField4() {
		// @security("Assignment of high to low field")
		return highIField;
	}

	@Constraints("low <= @return")
	public int failField5() {
		int i = highIField;
		// @security("Assignment of high to low field")
		return i;
	}

	// IMPORTANT "@pc <= high" is always valid
	public void failField6() {
		highSField = mkLow(42);
		// @tautology("Missing write effect to high")
		return;
	}

	public void failField7() {
		lowSField = mkLow(42);
		// @security("Missing write effect to low")
		return;
	}

	@Constraints("@pc <= low")
	public void failField8() {
		// @security("Assignment of high value to low field")
		// @security("Double error")
		lowSField = mkHigh(42); // FIXME
	}

	@Constraints("low <= @return")
	public int failField9() {
		// @security("Assignment of high to low field")
		return highSField;
	}

	@Constraints("low <= @return")
	public int failField10() {
		int i = highSField;
		// @security("Assignment of high to low field")
		return i;
	}

}
