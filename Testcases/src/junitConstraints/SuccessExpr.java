package junitConstraints;

import static security.Definition.*;

public class SuccessExpr {

	public static void main(String[] args) {}
	
	
	@Constraints("high <= @return")
	public int test1() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High - op2High;
	}

	@Constraints("high <= @return")
	public int test2() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High + op2High;
	}

	@Constraints("high <= @return")
	public int test3() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High * op2High;
	}

	@Constraints("high <= @return")
	public int test4() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High / op2High;
	}

	@Constraints("high <= @return")
	public boolean test5() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High > op2High;
	}

	@Constraints("high <= @return")
	public boolean test6() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High >= op2High;
	}

	@Constraints("high <= @return")
	public boolean test7() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High < op2High;
	}

	@Constraints("high <= @return")
	public boolean test8() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High <= op2High;
	}

	@Constraints("high <= @return")
	public boolean test9() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High == op2High;
	}

	@Constraints("high <= @return")
	public boolean test10() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High && op2High;
	}

	@Constraints("high <= @return")
	public boolean test11() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High || op2High;
	}

	@Constraints("high <= @return")
	public boolean test12() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High ^ op2High;
	}

	@Constraints("high <= @return")
	public boolean test13() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High  & op2High;
	}

	@Constraints("high <= @return")
	public boolean test14() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High | op2High;
	}

	@Constraints("high <= @return")
	public int test15() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High - op2Low;
	}

	@Constraints("high <= @return")
	public int test16() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High + op2Low;
	}

	@Constraints("high <= @return")
	public int test17() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High * op2Low;
	}

	@Constraints("high <= @return")
	public int test18() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High / op2Low;
	}

	@Constraints("high <= @return")
	public boolean test19() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High > op2Low;
	}

	@Constraints("high <= @return")
	public boolean test20() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High >= op2Low;
	}

	@Constraints("high <= @return")
	public boolean test21() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High < op2Low;
	}

	@Constraints("high <= @return")
	public boolean test22() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High <= op2Low;
	}

	@Constraints("high <= @return")
	public boolean test23() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High == op2Low;
	}

	@Constraints("high <= @return")
	public boolean test24() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High && op2Low;
	}

	@Constraints("high <= @return")
	public boolean test25() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High || op2Low;
	}

	@Constraints("high <= @return")
	public boolean test26() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High ^ op2Low;
	}

	@Constraints("high <= @return")
	public boolean test27() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High  & op2Low;
	}

	@Constraints("high <= @return")
	public boolean test28() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High | op2Low;
	}

	@Constraints("high <= @return")
	public int test29() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low - op2High;
	}

	@Constraints("high <= @return")
	public int test30() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low + op2High;
	}

	@Constraints("high <= @return")
	public int test31() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low * op2High;
	}

	@Constraints("high <= @return")
	public int test32() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low / op2High;
	}

	@Constraints("high <= @return")
	public boolean test33() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low > op2High;
	}

	@Constraints("high <= @return")
	public boolean test34() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low >= op2High;
	}

	@Constraints("high <= @return")
	public boolean test35() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low < op2High;
	}

	@Constraints("high <= @return")
	public boolean test36() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low <= op2High;
	}

	@Constraints("high <= @return")
	public boolean test37() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low == op2High;
	}

	@Constraints("high <= @return")
	public boolean test38() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low && op2High;
	}

	@Constraints("high <= @return")
	public boolean test39() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low || op2High;
	}

	@Constraints("high <= @return")
	public boolean test40() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low ^ op2High;
	}

	@Constraints("high <= @return")
	public boolean test41() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low  & op2High;
	}

	@Constraints("high <= @return")
	public boolean test42() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low | op2High;
	}

	@Constraints("high <= @return")
	public int test43() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low - op2Low;
	}

	@Constraints("high <= @return")
	public int test44() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low + op2Low;
	}

	@Constraints("high <= @return")
	public int test45() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low * op2Low;
	}

	@Constraints("high <= @return")
	public int test46() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low / op2Low;
	}

	@Constraints("high <= @return")
	public boolean test47() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low > op2Low;
	}

	@Constraints("high <= @return")
	public boolean test48() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low >= op2Low;
	}

	@Constraints("high <= @return")
	public boolean test49() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low < op2Low;
	}

	@Constraints("high <= @return")
	public boolean test50() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low <= op2Low;
	}

	@Constraints("high <= @return")
	public boolean test51() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low == op2Low;
	}

	@Constraints("high <= @return")
	public boolean test52() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low && op2Low;
	}

	@Constraints("high <= @return")
	public boolean test53() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low || op2Low;
	}

	@Constraints("high <= @return")
	public boolean test54() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low ^ op2Low;
	}

	@Constraints("high <= @return")
	public boolean test55() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low  & op2Low;
	}

	@Constraints("high <= @return")
	public boolean test56() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low | op2Low;
	}

	@Constraints("low <= @return")
	public int test57() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low - op2Low;
	}

	@Constraints("low <= @return")
	public int test58() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low + op2Low;
	}

	@Constraints("low <= @return")
	public int test59() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low * op2Low;
	}

	@Constraints("low <= @return")
	public int test60() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low / op2Low;
	}

	@Constraints("low <= @return")
	public boolean test61() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low > op2Low;
	}

	@Constraints("low <= @return")
	public boolean test62() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low >= op2Low;
	}

	@Constraints("low <= @return")
	public boolean test63() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low < op2Low;
	}

	@Constraints("low <= @return")
	public boolean test64() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low <= op2Low;
	}

	@Constraints("low <= @return")
	public boolean test65() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low == op2Low;
	}

	@Constraints("low <= @return")
	public boolean test66() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low && op2Low;
	}

	@Constraints("low <= @return")
	public boolean test67() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low || op2Low;
	}

	@Constraints("low <= @return")
	public boolean test68() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low ^ op2Low;
	}

	@Constraints("low <= @return")
	public boolean test69() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low  & op2Low;
	}

	@Constraints("low <= @return")
	public boolean test70() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low | op2Low;
	}
	
	public int test71() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low - op2Low;
	}

	public int test72() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low + op2Low;
	}

	public int test73() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low * op2Low;
	}

public int test74() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low / op2Low;
	}

	public boolean test75() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low > op2Low;
	}

	public boolean test76() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low >= op2Low;
	}

	public boolean test77() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low < op2Low;
	}

	public boolean test78() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low <= op2Low;
	}

	public boolean test79() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low == op2Low;
	}

	public boolean test80() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low && op2Low;
	}

	public boolean test81() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low || op2Low;
	}

	public boolean test82() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low ^ op2Low;
	}

	public boolean test83() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low  & op2Low;
	}

	public boolean test84() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low | op2Low;
	}
	
}
