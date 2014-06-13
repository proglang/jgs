package junitConstraints;

import static security.Definition.*;

public class FailExpr {

	@Constraints({ })
	public static void main(String[] args) {}
	
	@Constraints("low <= @return")
	public int failExpr1() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High - op2High;
	}

	@Constraints("low <= @return")
	public int failExpr2() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High + op2High;
	}

	@Constraints("low <= @return")
	public int failExpr3() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High * op2High;
	}

	@Constraints("low <= @return")
	public int failExpr4() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High / op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr5() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High > op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr6() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High >= op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr7() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High < op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr8() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High <= op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr9() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High == op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr10() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High && op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr11() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High || op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr12() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High ^ op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr13() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High & op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr14() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High | op2High;
	}

	@Constraints("low <= @return")
	public int failExpr15() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High - op2Low;
	}

	@Constraints("low <= @return")
	public int failExpr16() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High + op2Low;
	}

	@Constraints("low <= @return")
	public int failExpr17() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High * op2Low;
	}

	@Constraints("low <= @return")
	public int failExpr18() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High / op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr19() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High > op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr20() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High >= op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr21() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High < op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr22() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High <= op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr23() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High == op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr24() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High && op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr25() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High || op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr26() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High ^ op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr27() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High & op2Low;
	}

	@Constraints("low <= @return")
	public boolean failExpr28() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High | op2Low;
	}

	@Constraints("low <= @return")
	public int failExpr29() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low - op2High;
	}

	@Constraints("low <= @return")
	public int failExpr30() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low + op2High;
	}

	@Constraints("low <= @return")
	public int failExpr31() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low * op2High;
	}

	@Constraints("low <= @return")
	public int failExpr32() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low / op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr33() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low > op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr34() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low >= op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr35() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low < op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr36() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low <= op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr37() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low == op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr38() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low && op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr39() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low || op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr40() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low ^ op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr41() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low  & op2High;
	}

	@Constraints("low <= @return")
	public boolean failExpr42() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low | op2High;
	}

}
