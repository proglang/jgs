package junitConstraints;

import static security.Definition.*;

@Constraints({})
public class FailMethod {

	@FieldSecurity("low")
	int low;

	@Constraints({ "low <= @pc", "low <= @0" })
	public static void main(String[] args) {}

	// @Constraints({ "low <= @pc" })
	// public FailMethod() {}
	//
	// @Constraints({ "@0 <= @return", "high <= @return" })
	// public int failMethod1(int i) {
	// int j = mkHigh(42);
	// // @security("Return has stronger constraints than expected")
	// return i + j;
	// }
	//
	// @Constraints({ "@0 <= high", "@0 <= @return" })
	// public int failMethod2(int i) {
	// // @security("Return has stronger constraints than expected")
	// return i;
	// }

	@Constraints({})
	public void failMethod3() {
		low = mkHigh(42);
	}

}
