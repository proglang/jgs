package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class FailIdFunction {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({ "@pc <= low" })
	public FailIdFunction() {}

	@Constraints({ "@pc <= low", "high <= @0" })
	public void failIdFunction1(int i) {
		// @security("Argument has stronger constraints")
		mkLow(i);
	}
	
	@Constraints({ "@pc <= low", "@return <= low" })
	public int failIdFunction2() {
		int high = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return high;
	}

}
