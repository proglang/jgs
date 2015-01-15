package junitConstraints;

import static security.Definition.*;
import security.Definition.Constraints;

public class SuccessArray extends stubs.SpecialArrays {

	public static void main(String[] args) {}
	
	@FieldSecurity("low")
	public int lowIField;
	
	@FieldSecurity("high")
	public int highIField;

	@Constraints("@pc <= low")
	public void successArray1() {
		lowLow = arrayIntLow(23);
	}

	@Constraints("@pc <= low")
	public void successArray2() {
		lowHigh = arrayIntHigh(42);
	}

	public void successArray3() {
		highHigh = arrayIntHigh(42);
	}

	public void successArray4() {
		highHigh = arrayIntHigh(mkHigh(42));
	}

	@Constraints("@pc <= low")
	public void successArray5() {
		lowLow[23] = 42;
	}

	public void successArray6() {
		lowHigh[23] = 42;
	}

	public void successArray7() {
		lowHigh[23] = mkHigh(42);
	}

	public void successArray8() {
		highHigh[23] = mkHigh(42);
	}

	public void successArray9() {
		highHigh[23] = 42;
	}

	@Constraints("low <= @return")
	public int successArray10() {
		return lowLow.length;
	}

	@Constraints("high <= @return")
	public int successArray11() {
		return lowLow.length;
	}

	@Constraints("low <= @return")
	public int successArray12() {
		return lowHigh.length;
	}

	@Constraints("high <= @return")
	public int successArray13() {
		return lowHigh.length;
	}

	@Constraints("high <= @return")
	public int successArray14() {
		return highHigh.length;
	}

	@Constraints({ "low <= @return", "@return[ = low" })
	public int[] successArray15() {
		return lowLow;
	}

	@Constraints({ "high <= @return", "@return[ = low" })
	public int[] successArray16() {
		return lowLow;
	}

	@Constraints({ "low <= @return", "@return[ = high" })
	public int[] successArray17() {
		return lowHigh;
	}

	@Constraints({ "high <= @return", "@return[ = high" })
	public int[] successArray18() {
		return lowHigh;
	}

	@Constraints({ "high <= @return", "@return[ = high" })
	public int[] successArray19() {
		return highHigh;
	}

	@Constraints({ "low <= @return" })
	public int successArray20() {
		return lowLow[23];
	}

	@Constraints({ "high <= @return" })
	public int successArray21() {
		return lowLow[23];
	}

	@Constraints({ "high <= @return" })
	public int successArray22() {
		return lowHigh[23];
	}

	@Constraints({ "high <= @return" })
	public int successArray23() {
		return highHigh[23];
	}
	
	@Constraints({ "@pc <= low" })
	public void successArray24() {
		lowLowLow = arrayIntLowLow(42, 7);
	}
	
	@Constraints({ "@pc <= low" })
	public void successArray25() {
		lowLowHigh = arrayIntLowHigh(42, 7);
	}
	
	@Constraints({ "@pc <= low" })
	public void successArray26() {
		lowHighHigh = arrayIntHighHigh(42, 7);
	}
	
	@Constraints({ "@pc <= low" })
	public void successArray27() {
		lowHighHigh = arrayIntHighHigh(42, mkHigh(7));
	}
	
	public void successArray28() {
		highHighHigh = arrayIntHighHigh(42,7);
	}
	
	public void successArray29() {
		highHighHigh = arrayIntHighHigh(mkHigh(42),7);
	}
	
	public void successArray30() {
		highHighHigh = arrayIntHighHigh(42,mkHigh(7));
	}
	
	public void successArray31() {
		highHighHigh = arrayIntHighHigh(mkHigh(42),mkHigh(7));
	}
	
	@Constraints("@pc <= low")
	public void successArray32() {
		lowLowLow[23] = arrayIntLow(42);
	}

	@Constraints("@pc <= low")
	public void successArray33() {
		lowLowHigh[23] = arrayIntHigh(42);
	}


	public void successArray35() {
		lowHighHigh[23] = arrayIntHigh(42);
	}

	public void successArray36() {
		lowHighHigh[23] = arrayIntHigh(mkHigh(42));
	}
	
	public void successArray37() {
		highHighHigh[23] = arrayIntHigh(42);
	}
	
	public void successArray38() {
		highHighHigh[23] = arrayIntHigh(mkHigh(42));
	}
	
	@Constraints("@pc <= low")
	public void successArray40() {
		lowLowLow[23][7] = 42;
	}
	
	@Constraints("@pc <= low")
	public void successArray41() {
		lowLowHigh[23][7] = 42;
	}
	
	@Constraints("@pc <= low")
	public void successArray42() {
		lowLowHigh[23][7] = mkHigh(42);
	}
	
	public void successArray43() {
		lowHighHigh[23][7] = 42;
	}
	
	public void successArray44() {
		lowHighHigh[23][7] = mkHigh(42);
	}
	
	public void successArray45() {
		highHighHigh[23][7] = 42;
	}
	
	public void successArray46() {
		highHighHigh[23][7] = mkHigh(42);
	}
	
	@Constraints("low <= @return")
	public int successArray47() {
		return lowLowLow.length;
	}
	
	@Constraints("high <= @return")
	public int successArray48() {
		return lowLowLow.length;
	}
	
	@Constraints("low <= @return")
	public int successArray49() {
		return lowLowHigh.length;
	}
	
	@Constraints("high <= @return")
	public int successArray50() {
		return lowLowHigh.length;
	}
	
	@Constraints("low <= @return")
	public int successArray51() {
		return lowHighHigh.length;
	}
	
	@Constraints("high <= @return")
	public int successArray52() {
		return lowHighHigh.length;
	}
	
	@Constraints("high <= @return")
	public int successArray53() {
		return highHighHigh.length;
	}
	
	@Constraints("low <= @return")
	public int successArray54() {
		return lowLowLow[23].length;
	}
	
	@Constraints("high <= @return")
	public int successArray55() {
		return lowLowLow[23].length;
	}
	
	@Constraints("low <= @return")
	public int successArray56() {
		return lowLowHigh[23].length;
	}
	
	@Constraints("high <= @return")
	public int successArray57() {
		return lowLowHigh[23].length;
	}
	
	@Constraints("high <= @return")
	public int successArray58() {
		return lowHighHigh[23].length;
	}
	
	@Constraints("high <= @return")
	public int successArray59() {
		return highHighHigh[23].length;
	}
	
	@Constraints({"low <= @return", "@return[ = low", "@return[[ = low"})
	public int[][] successArray60() {
		return lowLowLow;
	}
	
	@Constraints({"high <= @return", "@return[ = low", "@return[[ = low"})
	public int[][] successArray61() {
		return lowLowLow;
	}
	
	@Constraints({"low <= @return", "@return[ = low", "@return[[ = high"})
	public int[][] successArray62() {
		return lowLowHigh;
	}
	
	@Constraints({"high <= @return", "@return[ = low", "@return[[ = high"})
	public int[][] successArray63() {
		return lowLowHigh;
	}
	
	@Constraints({"low <= @return", "@return[ = high", "@return[[ = high"})
	public int[][] successArray64() {
		return lowHighHigh;
	}
	
	@Constraints({"high <= @return", "@return[ = high", "@return[[ = high"})
	public int[][] successArray65() {
		return lowHighHigh;
	}
	
	@Constraints({"high <= @return", "@return[ = high", "@return[[ = high"})
	public int[][] successArray66() {
		return highHighHigh;
	}

	@Constraints({"low <= @return", "@return[ = low"})
	public int[] successArray67() {
		return lowLowLow[23];
	}
	
	@Constraints({"low <= @return", "@return[ = high"})
	public int[] successArray68() {
		return lowLowHigh[23];
	}
	
	@Constraints({"high <= @return", "@return[ = high"})
	public int[] successArray69() {
		return lowHighHigh[23];
	}
	
	@Constraints({"high <= @return", "@return[ = high"})
	public int[] successArray70() {
		return highHighHigh[23];
	}

	@Constraints("@pc <= low")
	public void successArray71() {
		lowLow[lowIField] = 42;
	}
	
	public void successArray72() {
		lowHigh[lowIField] = 42;
	}
	
	public void successArray73() {
		lowHigh[highIField] = 42;
	}
	
	public void successArray74() {
		highHigh[lowIField] = 42;
	}
	
	public void successArray75() {
		highHigh[highIField] = 42;
	}
	
	@Constraints("@pc <= low")
	public void successArray76() {
		lowLowLow[lowIField][lowIField] = 42;
	}
	
	@Constraints("@pc <= low")
	public void successArray77() {
		lowLowHigh[lowIField][lowIField] = 42;
	}
	
	@Constraints("@pc <= low")
	public void successArray78() {
		lowLowHigh[highIField][lowIField] = 42;
	}
	
	@Constraints("@pc <= low")
	public void successArray79() {
		lowLowHigh[highIField][lowIField] = 42;
	}
	
	@Constraints("@pc <= low")
	public void successArray80() {
		lowLowHigh[highIField][highIField] = 42;
	}
	
	public void successArray81() {
		lowHighHigh[lowIField][lowIField] = 42;
	}
	
	public void successArray82() {
		lowHighHigh[highIField][lowIField] = 42;
	}
	
	public void successArray83() {
		lowHighHigh[lowIField][highIField] = 42;
	}
	
	public void successArray84() {
		lowHighHigh[highIField][highIField] = 42;
	}
	
	public void successArray85() {
		highHighHigh[lowIField][lowIField] = 42;
	}
	
	public void successArray86() {
		highHighHigh[highIField][lowIField] = 42;
	}
	
	public void successArray87() {
		highHighHigh[lowIField][highIField] = 42;
	}
	
	public void successArray88() {
		highHighHigh[highIField][highIField] = 42;
	}
	
	@Constraints("low <= @return")
	public int successArray89() {
		return lowLow[lowIField];
	}
	
	@Constraints("high <= @return")
	public int successArray90() {
		return lowLow[lowIField];
	}
	
	@Constraints("high <= @return")
	public int successArray91() {
		return lowLow[highIField];
	}
	
	@Constraints("high <= @return")
	public int successArray92() {
		return lowHigh[lowIField];
	}
	
	@Constraints("high <= @return")
	public int successArray93() {
		return lowHigh[highIField];
	}
	
	@Constraints("high <= @return")
	public int successArray94() {
		return highHigh[lowIField];
	}
	
	@Constraints("high <= @return")
	public int successArray95() {
		return highHigh[highIField];
	}
	
	public void successArray96() {
		lowHighHigh[highIField][lowIField] = 42;
	}
	
}
