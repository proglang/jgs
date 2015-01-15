package junitConstraints;

import security.Definition.Constraints;
import security.Definition.FieldSecurity;
import stubs.Obj;

public class FailObject extends stubs.MinimalFields {

	public static void main(String[] args) {}

	@Constraints("@pc <= low")
	public FailObject() {}

	@FieldSecurity("low")
	public Obj lowObj = new Obj();

	@FieldSecurity("high")
	public Obj highObj = new Obj();

	@Constraints("low <= @return")
	public int failObject1() {
		// @security("weaker return level expected")
		return lowObj.highIField;
	}

	@Constraints("low <= @return")
	public int failObject2() {
		// @security("weaker return level expected")
		return highObj.highIField;
	}

	@Constraints("low <= @return")
	public int failObject3() {
		// @security("weaker return level expected")
		return highObj.lowIField;
	}

	@Constraints("low <= @return")
	public int failObject4() {
		// @security("weaker return level expected")
		return lowObj._highI();
	}

	@Constraints("low <= @return")
	public int failObject5() {
		// @security("weaker return level expected")
		return highObj._highI();
	}

	@Constraints("low <= @return")
	public int failObject6() {
		// @security("weaker return level expected")
		return highObj._lowI();
	}

	@Constraints("low <= @return")
	public int failObject7() {
		// @security("invalid parameter")
		// @security("Illegal flow from high to low")
		return lowObj.low_lowI(highIField);
	}

	@Constraints("low <= @return")
	public int failObject8() {
		// @security("weaker return level expected")
		return lowObj.low_highI(lowIField);
	}

	@Constraints("low <= @return")
	public int failObject9() {
		// @security("weaker return level expected")
		return lowObj.high_highI(lowIField);
	}

	@Constraints("low <= @return")
	public int failObject10() {
		// @security("weaker return level expected")
		return lowObj.high_highI(highIField);
	}

	@Constraints("low <= @return")
	public int failObject11() {
		// @security("weaker return level expected")
		return highObj.low_lowI(lowIField);
	}

	@Constraints("low <= @return")
	public int failObject12() {
		// @security("weaker return level expected")
		// @security("invalid parameter")
		return highObj.low_lowI(highIField);
	}

	@Constraints("low <= @return")
	public int failObject13() {
		// @security("weaker return level expected")
		// @security("invalid parameter")
		return highObj.low_highI(highIField);
	}

	@Constraints("low <= @return")
	public int failObject14() {
		// @security("weaker return level expected")
		return highObj.high_lowI(lowIField);
	}

	@Constraints("low <= @return")
	public int failObject15() {
		// @security("weaker return level expected")
		return highObj.high_lowI(highIField);
	}

	@Constraints("low <= @return")
	public int failObject16() {
		// @security("weaker return level expected")
		return highObj.high_highI(lowIField);
	}

	@Constraints("low <= @return")
	public int failObject17() {
		// @security("weaker return level expected")
		return highObj.high_highI(highIField);
	}

	@Constraints({ "low <= @return", "high = @return[" })
	public int[] failObject18() {
		// @security("weaker return level expected")
		// @security("Illegal flow from high to low")
		return lowObj.lowLowIField;
	}

	@Constraints({ "low <= @return", "low = @return[" })
	public int[] failObject19() {
		// @security("weaker return level expected")
		return highObj.lowLowIField;
	}

	@Constraints({ "low <= @return", "high = @return[" })
	public int[] failObject20() {
		// @security("weaker return level expected")
		// @security("Illegal flow from high to low")
		return highObj.lowLowIField;
	}

	@Constraints({ "low <= @return", "low = @return[" })
	public int[] failObject21() {
		// @security("weaker return level expected")
		// @security("Illegal flow from high to low")
		return highObj.lowHighIField;
	}

	@Constraints({ "low <= @return", "high = @return[" })
	public int[] failObject22() {
		// @security("weaker return level expected")
		return highObj.lowHighIField;
	}

	@Constraints({ "low <= @return", "low = @return[" })
	public int[] failObject23() {
		// @security("weaker return level expected")
		// @security("Illegal flow from high to low")
		return highObj.highHighIField;
	}

	@Constraints({ "low <= @return", "high = @return[" })
	public int[] failObject24() {
		// @security("weaker return level expected")
		return highObj.highHighIField;
	}

}
