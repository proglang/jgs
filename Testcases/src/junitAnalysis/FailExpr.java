package junitAnalysis;

import static security.Definition.*;

public class FailExpr {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@ReturnSecurity("low")
	public int expr() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High - op2High;
	}

	@ReturnSecurity("low")
	public int expr2() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High + op2High;
	}

	@ReturnSecurity("low")
	public int expr3() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High * op2High;
	}

	@ReturnSecurity("low")
	public int expr4() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High / op2High;
	}

	@ReturnSecurity("low")
	public boolean expr5() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High > op2High;
	}

	@ReturnSecurity("low")
	public boolean expr6() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High >= op2High;
	}

	@ReturnSecurity("low")
	public boolean expr7() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High < op2High;
	}

	@ReturnSecurity("low")
	public boolean expr8() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High <= op2High;
	}

	@ReturnSecurity("low")
	public boolean expr9() {
		int op1High = mkHigh(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High == op2High;
	}

	@ReturnSecurity("low")
	public boolean expr10() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High && op2High;
	}

	@ReturnSecurity("low")
	public boolean expr11() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High || op2High;
	}

	@ReturnSecurity("low")
	public boolean expr12() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High ^ op2High;
	}

	@ReturnSecurity("low")
	public boolean expr13() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High & op2High;
	}

	@ReturnSecurity("low")
	public boolean expr14() {
		boolean op1High = mkHigh(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High | op2High;
	}

	@ReturnSecurity("low")
	public int expr15() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High - op2Low;
	}

	@ReturnSecurity("low")
	public int expr16() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High + op2Low;
	}

	@ReturnSecurity("low")
	public int expr17() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High * op2Low;
	}

	@ReturnSecurity("low")
	public int expr18() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High / op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr19() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High > op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr20() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High >= op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr21() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High < op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr22() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High <= op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr23() {
		int op1High = mkHigh(42);
		int op2Low = mkLow(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1High == op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr24() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High && op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr25() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High || op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr26() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High ^ op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr27() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High & op2Low;
	}

	@ReturnSecurity("low")
	public boolean expr28() {
		boolean op1High = mkHigh(false);
		boolean op2Low = mkLow(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1High | op2Low;
	}

	@ReturnSecurity("low")
	public int expr29() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low - op2High;
	}

	@ReturnSecurity("low")
	public int expr30() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low + op2High;
	}

	@ReturnSecurity("low")
	public int expr31() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low * op2High;
	}

	@ReturnSecurity("low")
	public int expr32() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low / op2High;
	}

	@ReturnSecurity("low")
	public boolean expr33() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low > op2High;
	}

	@ReturnSecurity("low")
	public boolean expr34() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low >= op2High;
	}

	@ReturnSecurity("low")
	public boolean expr35() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low < op2High;
	}

	@ReturnSecurity("low")
	public boolean expr36() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low <= op2High;
	}

	@ReturnSecurity("low")
	public boolean expr37() {
		int op1Low = mkLow(42);
		int op2High = mkHigh(42);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low == op2High;
	}

	@ReturnSecurity("low")
	public boolean expr38() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low && op2High;
	}

	@ReturnSecurity("low")
	public boolean expr39() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low || op2High;
	}

	@ReturnSecurity("low")
	public boolean expr40() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low ^ op2High;
	}

	@ReturnSecurity("low")
	public boolean expr41() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low  & op2High;
	}

	@ReturnSecurity("low")
	public boolean expr42() {
		boolean op1Low = mkLow(false);
		boolean op2High = mkHigh(false);
		// @security("The returned value has a stronger security level than expected.")
		return op1Low | op2High;
	}
	
}
