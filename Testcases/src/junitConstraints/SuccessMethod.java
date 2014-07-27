package junitConstraints;

import static security.Definition.*;

public class SuccessMethod {

	@FieldSecurity({ "low" })
	private int iFieldL;

	@FieldSecurity({ "high" })
	private int iFieldH;

	@FieldSecurity({ "low" })
	private static int sFieldL;

	@FieldSecurity({ "high" })
	private static int sFieldH;

	public static void main(String[] args) {}

	@Constraints({})
	public void test1() {}

	@Constraints({})
	public static void test2() {}

	public int test3() {
		return 42;
	}

	public static int test4() {
		return 42;
	}

	@Constraints({ "high <= @return" })
	public int test5() {
		return 42;
	}

	@Constraints({ "high <= @return" })
	public static int test6() {
		return 42;
	}

	@Constraints({ "high <= @return" })
	public int test7() {
		return mkHigh(42);
	}

	@Constraints({ "high <= @return" })
	public static int test8() {
		return mkHigh(42);
	}

	public int test9(int a1) {
		return 42;
	}

	public static int test10(int a1) {
		return 42;
	}

	@Constraints({ "@0 <= @return" })
	public int test11(int a1) {
		return a1;
	}

	@Constraints({ "@0 <= @return" })
	public static int test12(int a1) {
		return a1;
	}

	@Constraints({ "@0 <= @return", "high <= @return" })
	public int test13(int a1) {
		return a1 + mkHigh(23);
	}

	@Constraints({ "@0 <= @return", "high <= @return" })
	public static int test14(int a1) {
		return a1 + mkHigh(23);
	}

	public int test15() {
		return iFieldL;
	}

	public static int test16() {
		return sFieldL;
	}

	@Constraints({ "high <= @return" })
	public int test17() {
		return iFieldL;
	}

	@Constraints({ "high <= @return" })
	public static int test18() {
		return sFieldL;
	}

	@Constraints({ "high <= @return" })
	public int test19() {
		return iFieldH;
	}

	@Constraints({ "high <= @return" })
	public static int test20() {
		return sFieldH;
	}

	@Constraints({ "@0 <= low", "low <= @return" })
	public int test21(int a1) {
		return a1;
	}

	@Constraints({ "@0 <= low", "low <= @return" })
	public static int test22(int a1) {
		return a1;
	}
	
	@Constraints({ "@0 <= high", "@0 <= @return" })
	public int test23(int a1) {
		return a1;
	}

	@Constraints({ "@0 <= high", "@0 <= @return" })
	public static int test24(int a1) {
		return a1;
	}

	@Constraints({ "high <= @return" })
	public int test100() {
		int[] arr = arrayIntHigh(42);
		return arr[23];
		// By removing the constraints which contains locals also the base of ComponentArrayRefs should be considered.
	}

}
