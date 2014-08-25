package junitConstraints;

import static security.Definition.*;
import security.Definition.Constraints;

public class SuccessWhile extends stubs.MinimalFields {
	
	public static void main(String[] args) {}
	
	@Constraints("@pc <= low")
	public void successWhile1() {
		while (lowIField <= 42) {
			lowIField = 42;
		}
	}
	
	public void successWhile2() {
		while (lowIField <= 42) {
			highIField = 42;
		}
	}
	
	public void successWhile3() {
		while (lowIField <= 42) {
			highIField = mkHigh(42);
		}
	}
	
	public void successWhile4() {
		while (highIField <= 42) {
			highIField = 42;
		}
	}
	
	public void successWhile5() {
		while (highIField <= 42) {
			highIField = mkHigh(42);
		}
	}
	
	@Constraints("@pc <= low")
	public void successWhile6() {
		for (int i = 42; i < lowIField; i++) {
			lowIField = i;
		}
	}
	
	public void successWhile7() {
		for (int i = 42; i < lowIField; i++) {
			highIField = i;
		}
	}
	
	public void successWhile8() {
		for (int i = 42; i < lowIField; i++) {
			highIField = mkHigh(i);
		}
	}
	
	public void successWhile9() {
		for (int i = 42; i < highIField; i++) {
			highIField = i;
		}
	}
	
	public void successWhile10() {
		for (int i = 42; i < highIField; i++) {
			highIField = mkHigh(i);
		}
	}
	
	@Constraints("@pc <= low")
	public void successWhile11() {
		while (lowIField <= 42) {
			if (lowIField <= 23) lowIField = 42;
		}
	}
	
	public void successWhile12() {
		while (lowIField <= 42) {
			if (lowIField <= 23) highIField = 42;
		}
	}
	
	public void successWhile13() {
		while (lowIField <= 42) {
			if (highIField <= 23) highIField = 42;
		}
	}
	
	public void successWhile14() {
		while (highIField <= 42) {
			if (lowIField <= 23) highIField = 42;
		}
	}
	
	public void successWhile15() {
		while (highIField <= 42) {
			if (highIField <= 23) highIField = 42;
		}
	}
	
	public void successWhile16() {
		while (lowIField <= 42) {
			if (lowIField <= 23) highIField = mkHigh(42);
		}
	}
	
	public void successWhile17() {
		while (lowIField <= 42) {
			if (highIField <= 23) highIField = mkHigh(42);
		}
	}
	
	public void successWhile18() {
		while (highIField <= 42) {
			if (lowIField <= 23) highIField = mkHigh(42);
		}
	}
	
	public void successWhile19() {
		while (highIField <= 42) {
			if (highIField <= 23) highIField = mkHigh(42);
		}
	}
	
	@Constraints("@pc <= low")
	public void successWhile20() {
		int i = lowIField;
		while (i <= 42) {
			if (i <= -23) i = 7;
			lowIField = i;
		}
	}
	
	public void successWhile21() {
		int i = lowIField;
		while (i <= 42) {
			if (i <= -23) i = 7;
			highIField = i;
		}
	}
	
	public void successWhile22() {
		int i = lowIField;
		while (i <= 42) {
			if (i <= -23) i = mkHigh(7);
			highIField = i;
		}
	}

	public void successWhile23() {
		int i = highIField;
		while (i <= 42) {
			if (i <= -23) i = mkHigh(7);
			highIField = i;
		}
	}
	
	@Constraints("@pc <= low")
	public void successWhile24() {
		int i = highIField;
		while (i <= 42) {
			if (i <= -23) i = 7;
			highIField = i;
		}
	}
	
	@Constraints("@pc <= low")
	public void successWhile25() {
		int i = lowIField;
		while (i <= 42) {
			lowIField = i;
			if (i <= -23) i = 7;
		}
	}
	
	public void successWhile26() {
		int i = lowIField;
		while (i <= 42) {
			highIField = i;
			if (i <= -23) i = 7;
		}
	}
	
	public void successWhile27() {
		int i = lowIField;
		while (i <= 42) {
			highIField = i;
			if (i <= -23) i = mkHigh(7);
		}
	}

	public void successWhile28() {
		int i = highIField;
		while (i <= 42) {
			highIField = i;
			if (i <= -23) i = mkHigh(7);
		}
	}
	
	public void successWhile29() {
		int i = highIField;
		while (i <= 42) {
			highIField = i;
			if (i <= -23) i = 7;
		}
	}
	
	@Constraints("high <= @return")
	public int successWhile30() {
		int result = 42;
		while (highIField <= 23) {
			result = result + 1;
		}
		return result;
	}
	
	@Constraints("@pc <= low")
	public void successWhile31() {
		do {
			lowIField = 23;
		} while (lowIField <= 42);
	}
	
	public void successWhile32() {
		do {
			highIField = 23;
		} while (lowIField <= 42);
	}
	
	public void successWhile33() {
		do {
			highIField = mkHigh(23);
		} while (lowIField <= 42);
	}
	
	public void successWhile34() {
		do {
			highIField = mkHigh(23);
		} while (highIField <= 42);
	}
	
	public void successWhile35() {
		do {
			highIField = 23;
		} while (highIField <= 42);
	}
}
