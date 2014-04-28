package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class FailMethod {
	
	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({ "@pc <= low" })
	public FailMethod() {}
	
	@Constraints({ "@pc <= low", "high <= @0", "@return <= low" })
	public int failMethod1(int i) {
		// @security("Return has stronger constraints than expected")
		return i;
	}
	
//	@Constraints({ "@pc <= low", "@0 <= high", "@return <= low" })
//	public int failMethod2(int i) {
//		// @security("Return has stronger constraints than expected")
//		return i;
//	}

}
