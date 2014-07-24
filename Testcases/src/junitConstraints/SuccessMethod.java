package junitConstraints;

import static security.Definition.*;

public class SuccessMethod {

	public static void main(String[] args) {}

	@Constraints({})
	public void test1() {}

	@Constraints({ "low <= @return" })
	public int test2() {
		return 42;
	}

	@Constraints({ "high <= @return" })
	public int test3() {
		return mkHigh(42);
	}

	@Constraints({ "high <= @return" })
	public int test4() {
		int[] arr = arrayIntHigh(42);
		return arr[23];
		// By removing the constraints which contains locals also the base of ComponentArrayRefs should be considered.
	}

}
