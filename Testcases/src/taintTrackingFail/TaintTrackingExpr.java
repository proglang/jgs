package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingExpr {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr() {
		int op1High = SootSecurityLevel.highId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1High - op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr2() {
		int op1High = SootSecurityLevel.highId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1High + op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr3() {
		int op1High = SootSecurityLevel.highId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1High * op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr4() {
		int op1High = SootSecurityLevel.highId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1High / op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public boolean expr5() {
		int op1High = SootSecurityLevel.highId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1High > op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr6() {
		int op1High = SootSecurityLevel.highId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1High >= op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr7() {
		int op1High = SootSecurityLevel.highId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1High < op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr8() {
		int op1High = SootSecurityLevel.highId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1High <= op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr9() {
		int op1High = SootSecurityLevel.highId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1High == op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr10() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1High && op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr11() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1High || op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr12() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1High ^ op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr13() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1High  & op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr14() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1High | op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr15() {
		int op1High = SootSecurityLevel.highId(42);
		int op2Low = SootSecurityLevel.lowId(42);
		return op1High - op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr16() {
		int op1High = SootSecurityLevel.highId(42);
		int op2Low = SootSecurityLevel.lowId(42);
		return op1High + op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr17() {
		int op1High = SootSecurityLevel.highId(42);
		int op2Low = SootSecurityLevel.lowId(42);
		return op1High * op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr18() {
		int op1High = SootSecurityLevel.highId(42);
		int op2Low = SootSecurityLevel.lowId(42);
		return op1High / op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr19() {
		int op1High = SootSecurityLevel.highId(42);
		int op2Low = SootSecurityLevel.lowId(42);
		return op1High > op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr20() {
		int op1High = SootSecurityLevel.highId(42);
		int op2Low = SootSecurityLevel.lowId(42);
		return op1High >= op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr21() {
		int op1High = SootSecurityLevel.highId(42);
		int op2Low = SootSecurityLevel.lowId(42);
		return op1High < op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr22() {
		int op1High = SootSecurityLevel.highId(42);
		int op2Low = SootSecurityLevel.lowId(42);
		return op1High <= op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr23() {
		int op1High = SootSecurityLevel.highId(42);
		int op2Low = SootSecurityLevel.lowId(42);
		return op1High == op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr24() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2Low = SootSecurityLevel.lowId(false);
		return op1High && op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr25() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2Low = SootSecurityLevel.lowId(false);
		return op1High || op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr26() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2Low = SootSecurityLevel.lowId(false);
		return op1High ^ op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr27() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2Low = SootSecurityLevel.lowId(false);
		return op1High  & op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr28() {
		boolean op1High = SootSecurityLevel.highId(false);
		boolean op2Low = SootSecurityLevel.lowId(false);
		return op1High | op2Low;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr29() {
		int op1Low = SootSecurityLevel.lowId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1Low - op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr30() {
		int op1Low = SootSecurityLevel.lowId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1Low + op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr31() {
		int op1Low = SootSecurityLevel.lowId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1Low * op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int expr32() {
		int op1Low = SootSecurityLevel.lowId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1Low / op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr33() {
		int op1Low = SootSecurityLevel.lowId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1Low > op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr34() {
		int op1Low = SootSecurityLevel.lowId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1Low >= op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr35() {
		int op1Low = SootSecurityLevel.lowId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1Low < op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr36() {
		int op1Low = SootSecurityLevel.lowId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1Low <= op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr37() {
		int op1Low = SootSecurityLevel.lowId(42);
		int op2High = SootSecurityLevel.highId(42);
		return op1Low == op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr38() {
		boolean op1Low = SootSecurityLevel.lowId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1Low && op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr39() {
		boolean op1Low = SootSecurityLevel.lowId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1Low || op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr40() {
		boolean op1Low = SootSecurityLevel.lowId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1Low ^ op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr41() {
		boolean op1Low = SootSecurityLevel.lowId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1Low  & op2High;
	}

	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public boolean expr42() {
		boolean op1Low = SootSecurityLevel.lowId(false);
		boolean op2High = SootSecurityLevel.highId(false);
		return op1Low | op2High;
	}
	
}
