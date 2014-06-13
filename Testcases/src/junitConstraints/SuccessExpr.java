package junitConstraints;

import static security.Definition.*;

public class SuccessExpr {

	public static void main(String[] args) {}
	
	
	@Constraints("high <= @return")
	public int successExpr1() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High - op2High;
	}

	@Constraints("high <= @return")
	public int successExpr2() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High + op2High;
	}

	@Constraints("high <= @return")
	public int successExpr3() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High * op2High;
	}

	@Constraints("high <= @return")
	public int successExpr4() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High / op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr5() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High > op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr6() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High >= op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr7() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High < op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr8() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High <= op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr9() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		return op1High == op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr10() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High && op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr11() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High || op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr12() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High ^ op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr13() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High  & op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr14() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		return op1High | op2High;
	}

	@Constraints("high <= @return")
	public int successExpr15() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High - op2Low;
	}

	@Constraints("high <= @return")
	public int successExpr16() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High + op2Low;
	}

	@Constraints("high <= @return")
	public int successExpr17() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High * op2Low;
	}

	@Constraints("high <= @return")
	public int successExpr18() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High / op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr19() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High > op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr20() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High >= op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr21() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High < op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr22() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High <= op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr23() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		return op1High == op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr24() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High && op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr25() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High || op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr26() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High ^ op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr27() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High  & op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr28() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		return op1High | op2Low;
	}

	@Constraints("high <= @return")
	public int successExpr29() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low - op2High;
	}

	@Constraints("high <= @return")
	public int successExpr30() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low + op2High;
	}

	@Constraints("high <= @return")
	public int successExpr31() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low * op2High;
	}

	@Constraints("high <= @return")
	public int successExpr32() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low / op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr33() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low > op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr34() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low >= op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr35() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low < op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr36() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low <= op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr37() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		return op1Low == op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr38() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low && op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr39() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low || op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr40() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low ^ op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr41() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low  & op2High;
	}

	@Constraints("high <= @return")
	public boolean successExpr42() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		return op1Low | op2High;
	}

	@Constraints("high <= @return")
	public int successExpr43() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low - op2Low;
	}

	@Constraints("high <= @return")
	public int successExpr44() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low + op2Low;
	}

	@Constraints("high <= @return")
	public int successExpr45() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low * op2Low;
	}

	@Constraints("high <= @return")
	public int successExpr46() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low / op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr47() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low > op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr48() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low >= op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr49() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low < op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr50() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low <= op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr51() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low == op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr52() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low && op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr53() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low || op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr54() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low ^ op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr55() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low  & op2Low;
	}

	@Constraints("high <= @return")
	public boolean successExpr56() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low | op2Low;
	}

	@Constraints("low <= @return")
	public int successExpr57() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low - op2Low;
	}

	@Constraints("low <= @return")
	public int successExpr58() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low + op2Low;
	}

	@Constraints("low <= @return")
	public int successExpr59() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low * op2Low;
	}

	@Constraints("low <= @return")
	public int successExpr60() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low / op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr61() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low > op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr62() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low >= op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr63() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low < op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr64() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low <= op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr65() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low == op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr66() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low && op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr67() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low || op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr68() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low ^ op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr69() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low  & op2Low;
	}

	@Constraints("low <= @return")
	public boolean successExpr70() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low | op2Low;
	}
	
	public int successExpr71() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low - op2Low;
	}

	public int successExpr72() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low + op2Low;
	}

	public int successExpr73() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low * op2Low;
	}

public int successExpr74() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low / op2Low;
	}

	public boolean successExpr75() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low > op2Low;
	}

	public boolean successExpr76() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low >= op2Low;
	}

	public boolean successExpr77() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low < op2Low;
	}

	public boolean successExpr78() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low <= op2Low;
	}

	public boolean successExpr79() {
		int op1Low = mkLow(42);
		int op2Low = mkLow(42);
		return op1Low == op2Low;
	}

	public boolean successExpr80() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low && op2Low;
	}

	public boolean successExpr81() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low || op2Low;
	}

	public boolean successExpr82() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low ^ op2Low;
	}

	public boolean successExpr83() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low  & op2Low;
	}

	public boolean successExpr84() {
		boolean op1Low = mkLow(false);
		boolean op2Low = mkLow(false);
		return op1Low | op2Low;
	}
}
