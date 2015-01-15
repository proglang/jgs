package junitConstraints;

import static security.Definition.*;
import security.Definition.Constraints;

public class SuccessIf extends stubs.MinimalFields {

	public static void main(String[] args) {}

	@Constraints("@pc <= low")
	public void successIf1() {
		if (lowIField == 0) {
			lowIField = 42;
		}
	}

	public void successIf2() {
		if (lowIField == 0) {
			highIField = 42;
		}
	}

	public void successIf3() {
		if (lowIField == 0) {
			highIField = mkHigh(42);
		}
	}

	public void successIf4() {
		if (highIField == 0) {
			highIField = 42;
		}
	}

	public void successIf5() {
		if (highIField == 0) {
			highIField = mkHigh(42);
		}
	}

	@Constraints("@pc <= low")
	public void successIf6() {
		if (lowIField == 0) lowIField = 42;
	}

	public void successIf7() {
		if (lowIField == 0) highIField = 42;
	}

	public void successIf8() {
		if (lowIField == 0) highIField = mkHigh(42);
	}

	public void successIf9() {
		if (highIField == 0) highIField = 42;
	}

	public void successIf10() {
		if (highIField == 0) highIField = mkHigh(42);
	}

	@Constraints("low <= @return")
	public int successIf11() {
		int l = lowIField;
		return l == 0 ? l : l;
	}

	@Constraints("high <= @return")
	public int successIf12() {
		int l = lowIField;
		return l == 0 ? l : l;
	}

	@Constraints("high <= @return")
	public int successIf13() {
		int l = lowIField;
		int h = highIField;
		return h == 0 ? l : l;
	}

	@Constraints("high <= @return")
	public int successIf14() {
		int l = lowIField;
		int h = highIField;
		return l == 0 ? h : l;
	}

	@Constraints("high <= @return")
	public int successIf15() {
		int l = lowIField;
		int h = highIField;
		return h == 0 ? h : l;
	}

	@Constraints("high <= @return")
	public int successIf16() {
		int l = lowIField;
		int h = highIField;
		return l == 0 ? l : h;
	}

	@Constraints("high <= @return")
	public int successIf17() {
		int l = lowIField;
		int h = highIField;
		return h == 0 ? l : h;
	}

	@Constraints("high <= @return")
	public int successIf18() {
		int l = lowIField;
		int h = highIField;
		return l == 0 ? h : h;
	}

	@Constraints("high <= @return")
	public int successIf19() {
		int h = highIField;
		return h == 0 ? h : h;
	}

	@Constraints("high <= @return")
	public int successIf20() {
		int h = highIField;
		int l = lowIField;
		int i = h == 0 ? l : l;
		return i;
	}

	@Constraints("low <= @return")
	public int successIf21() {
		if (lowIField == 0) {
			return lowIField;
		} else {
			return lowIField;
		}
	}

	@Constraints("high <= @return")
	public int successIf22() {
		if (lowIField == 0) {
			return lowIField;
		} else {
			return lowIField;
		}
	}

	@Constraints("high <= @return")
	public int successIf23() {
		if (lowIField == 0) {
			return highIField;
		} else {
			return lowIField;
		}
	}

	@Constraints("high <= @return")
	public int successIf24() {
		if (lowIField == 0) {
			return lowIField;
		} else {
			return highIField;
		}
	}

	@Constraints("high <= @return")
	public int successIf25() {
		if (lowIField == 0) {
			return highIField;
		} else {
			return highIField;
		}
	}

	@Constraints("high <= @return")
	public int successIf26() {
		if (highIField == 0) {
			return lowIField;
		} else {
			return lowIField;
		}
	}

	@Constraints("high <= @return")
	public int successIf27() {
		if (highIField == 0) {
			return highIField;
		} else {
			return lowIField;
		}
	}

	@Constraints("high <= @return")
	public int successIf28() {
		if (highIField == 0) {
			return lowIField;
		} else {
			return highIField;
		}
	}

	@Constraints("high <= @return")
	public int successIf29() {
		if (highIField == 0) {
			return highIField;
		} else {
			return highIField;
		}
	}

	@Constraints("@pc <= low")
	public void successIf30() {
		if (lowIField == 0) {
			lowIField = 42;
		} else {
			lowIField = 42;
		}
	}

	public void successIf31() {
		if (lowIField == 0) {
			highIField = 42;
		} else {
			highIField = 42;
		}
	}

	public void successIf32() {
		if (lowIField == 0) {
			highIField = mkHigh(42);
		} else {
			highIField = mkHigh(42);
		}
	}

	public void successIf33() {
		if (highIField == 0) {
			highIField = 42;
		} else {
			highIField = 42;
		}
	}

	public void successIf34() {
		if (highIField == 0) {
			highIField = mkHigh(42);
		} else {
			highIField = mkHigh(42);
		}
	}

	@Constraints("low <= @return")
	public int successIf35() {
		if (lowIField == 0) {
			return lowIField;
		}
		return lowIField;
	}

	@Constraints("high <= @return")
	public int successIf36() {
		if (highIField == 0) {
			return lowIField;
		}
		return lowIField;
	}

	@Constraints("high <= @return")
	public int successIf37() {
		if (lowIField == 0) {
			return highIField;
		}
		return lowIField;
	}

	@Constraints("high <= @return")
	public int successIf38() {
		if (lowIField == 0) {
			return lowIField;
		}
		return highIField;
	}

	@Constraints("high <= @return")
	public int successIf39() {
		if (lowIField == 0) {
			return highIField;
		}
		return highIField;
	}

	@Constraints("high <= @return")
	public int successIf40() {
		if (highIField == 0) {
			return highIField;
		}
		return highIField;
	}

	@Constraints("high <= @return")
	public int successIf41() {
		if (highIField == 0) {
			return lowIField;
		}
		return highIField;
	}

	@Constraints("high <= @return")
	public int successIf42() {
		if (highIField == 0) {
			return highIField;
		}
		return lowIField;
	}

	@Constraints("low <= @return")
	public int successIf43() {
		int result = 0;
		if (lowIField == 0) {
			result = lowIField;
		}
		return result;
	}

	@Constraints("high <= @return")
	public int successIf44() {
		int result = 0;
		if (lowIField == 0) {
			result = highIField;
		}
		return result;
	}

	@Constraints("high <= @return")
	public int successIf45() {
		int result = 0;
		if (highIField == 0) {
			result = lowIField;
		}
		return result;
	}

	@Constraints("high <= @return")
	public int successIf46() {
		int result = 0;
		if (highIField == 0) {
			result = highIField;
		}
		return result;
	}

	@Constraints("low <= @return")
	public int successIf47() {
		if (lowIField == 0) {
			if (lowIField == 0) {
				return 0;
			} else {
				return 0;
			}
		} else {
			if (lowIField == 0) {
				return 0;
			} else {
				return 0;
			}
		}
	}
	
	@Constraints("high <= @return")
	public int successIf48() {
		if (lowIField == 0) {
			if (highIField == 0) {
				return 0;
			} else {
				return 0;
			}
		} else {
			if (lowIField == 0) {
				return 0;
			} else {
				return 0;
			}
		}
	}
	
	@Constraints("high <= @return")
	public int successIf49() {
		if (lowIField == 0) {
			if (lowIField == 0) {
				return 0;
			} else {
				return 0;
			}
		} else {
			if (highIField == 0) {
				return 0;
			} else {
				return 0;
			}
		}
	}
	
	@Constraints("high <= @return")
	public int successIf50() {
		if (lowIField == 0) {
			if (highIField == 0) {
				return 0;
			} else {
				return 0;
			}
		} else {
			if (highIField == 0) {
				return 0;
			} else {
				return 0;
			}
		}
	}
	
	@Constraints("high <= @return")
	public int successIf51() {
		if (highIField == 0) {
			if (lowIField == 0) {
				return 0;
			} else {
				return 0;
			}
		} else {
			if (lowIField == 0) {
				return 0;
			} else {
				return 0;
			}
		}
	}
	
	@Constraints("high <= @return")
	public int successIf52() {
		if (highIField == 0) {
			if (highIField == 0) {
				return 0;
			} else {
				return 0;
			}
		} else {
			if (lowIField == 0) {
				return 0;
			} else {
				return 0;
			}
		}
	}
	
	@Constraints("high <= @return")
	public int successIf53() {
		if (highIField == 0) {
			if (lowIField == 0) {
				return 0;
			} else {
				return 0;
			}
		} else {
			if (highIField == 0) {
				return 0;
			} else {
				return 0;
			}
		}
	}
	
	@Constraints("high <= @return")
	public int successIf54() {
		if (highIField == 0) {
			if (highIField == 0) {
				return 0;
			} else {
				return 0;
			}
		} else {
			if (highIField == 0) {
				return 0;
			} else {
				return 0;
			}
		}
	}

	@Constraints({ "high <= @return" })
	public int successIf100() {
		int i = mkLow(42);
		if (mkHigh(true)) {
			i = 42;
		}
		return i;
	}

	public void successIf101() {
		boolean x = true;
		if (x) {
			x = mkHigh(false);
			lowIField = 5;
		}
	}

}
