package junitConstraints;

import static security.Definition.*;

public class FailMethodStatic extends stubs.Methods {

	public static void main(String[] args) {}
	
	// Static methods
	
	@Constraints("low <= @return")
	public static int failMethod1() {
		// @security("Return high value - expected was low")
		return _highS();
	}

	@Constraints({ "low <= @return", "@return[ = low" })
	public static int[] failMethod2() {
		// @security("Return high value - expected was low")
		return _lowHighS();
	}

	@Constraints({ "low <= @return", "@return[ = low" })
	public static int[] failMethod3() {
		// @security("Return high value - expected was low")
		// @security("Flow from high to low")
		return _highHighS();
	}

	@Constraints({ "low <= @return", "@return[ = high" })
	public static int[] failMethod4() {
		// @security("Return high value - expected was low")
		return _highHighS();
	}

	@Constraints({ "high <= @return", "@return[ = low" })
	public static int[] failMethod5() {
		// @security("Return high value - expected was low")
		return _highHighS();
	}

	@Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
	public static int[][] failMethod6() {
		// @security("Return high value - expected was low")
		// @security("Flow from high to low")
		return _lowLowLowS();
	}

	@Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
	public static int[][] failMethod7() {
		// @security("Return high value - expected was low")
		// @security("Flow from high to low")
		return _lowLowLowS();
	}

	@Constraints({ "low <= @return", "@return[ = high", "@return[[ = low" })
	public static int[][] failMethod8() {
		// @security("Return high value - expected was low")
		// @security("Flow from high to low")
		return _lowLowLowS();
	}

	@Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
	public static int[][] failMethod9() {
		// @security("Return high value - expected was low")
		return _lowLowHighS();
	}

	@Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
	public static int[][] failMethod10() {
		// @security("Return high value - expected was low")
		// @security("Flow from high to low")
		return _lowLowHighS();
	}

	@Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
	public static int[][] failMethod11() {
		// @security("Return high value - expected was low")
		return _lowHighHighS();
	}

	@Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
	public static int[][] failMethod12() {
		// @security("Return high value - expected was low")
		return _lowHighHighS();
	}

	@Constraints({ "low <= @return", "@return[ = high", "@return[[ = low" })
	public static int[][] failMethod13() {
		// @security("Return high value - expected was low")
		return _lowHighHighS();
	}

	@Constraints({ "low <= @return", "@return[ = low", "@return[[ = low" })
	public static int[][] failMethod14() {
		// @security("Return high value - expected was low")
		// @security("Flow from high to low")
		return _highHighHighS();
	}

	@Constraints({ "low <= @return", "@return[ = low", "@return[[ = high" })
	public static int[][] failMethod15() {
		// @security("Return high value - expected was low")
		// @security("Flow from high to low")
		return _highHighHighS();
	}

	@Constraints({ "low <= @return", "@return[ = high", "@return[[ = high" })
	public static int[][] failMethod16() {
		// @security("Return high value - expected was low")
		return _highHighHighS();
	}

	@Constraints({ "low <= @return", "@return[ = high", "@return[[ = low" })
	public static int[][] failMethod17() {
		// @security("Return high value - expected was low")
		// @security("Flow from high to low")
		return _highHighHighS();
	}

	@Constraints({ "high <= @return", "@return[ = high", "@return[[ = low" })
	public static int[][] failMethod18() {
		// @security("Return high value - expected was low")
		return _highHighHighS();
	}

	@Constraints({ "high <= @return", "@return[ = low", "@return[[ = high" })
	public static int[][] failMethod19() {
		// @security("Return high value - expected was low")
		return _highHighHighS();
	}

	@Constraints({ "high <= @return", "@return[ = low", "@return[[ = low" })
	public static int[][] failMethod20() {
		// @security("Return high value - expected was low")
		return _highHighHighS();
	}

	public static void failMethod21() {
		// @security("Parameter stronger than allowed")
		low_S(highSField);
	}

	public static void failMethod22() {
		// @security("Parameter stronger than allowed")
		lowLow_S(lowHighSField);
	}

	public static void failMethod23() {
		// @security("Parameter stronger than allowed")
		lowLow_S(highHighSField);
	}

	public static void failMethod24() {
		// @security("Parameter stronger than allowed")
		lowHigh_S(highHighSField);
	}

	public static void failMethod25() {
		// @security("Parameter stronger than allowed")
		lowLowLow_S(lowLowHighSField);
	}

	public static void failMethod26() {
		// @security("Parameter stronger than allowed")
		lowLowLow_S(lowHighHighSField);
	}

	public static void failMethod27() {
		// @security("Parameter stronger than allowed")
		lowLowLow_S(highHighHighSField);
	}

	public static void failMethod28() {
		// @security("Parameter stronger than allowed")
		lowLowHigh_S(lowHighHighSField);
	}

	public static void failMethod29() {
		// @security("Parameter stronger than allowed")
		lowLowHigh_S(highHighHighSField);
	}

	public static void failMethod30() {
		// @security("Parameter stronger than allowed")
		lowHighHigh_S(highHighHighSField);
	}
	
	public static void failMethod31() {
		// @security("Parameter stronger than allowed")
		low$low_S(highSField, highSField);
	}
	
	public static void failMethod32() {
		// @security("Parameter stronger than allowed")
		low$low_S(lowSField, highSField);
	}
	
	public static void failMethod33() {
		// @security("Parameter stronger than allowed")
		low$low_S(highSField, lowSField);
	}
	
	public static void failMethod34() {
		// @security("Parameter stronger than allowed")
		low$lowLow_S(lowSField, lowHighSField);
	}
	
	public static void failMethod35() {
		// @security("Parameter stronger than allowed")
		low$lowLow_S(lowSField, highHighSField);
	}
	
	public static void failMethod36() {
		// @security("Parameter stronger than allowed")
		low$lowLow_S(highSField, lowLowSField);
	}
	
	public static void failMethod37() {
		// @security("Parameter stronger than allowed")
		low$lowLow_S(highSField, lowHighSField);
	}
	
	public static void failMethod38() {
		// @security("Parameter stronger than allowed")
		low$lowLow_S(highSField, highHighSField);
	}
	
	public static void failMethod39() {
		// @security("Parameter stronger than allowed")
		lowLow$lowLow_S(lowLowSField, lowHighSField);
	}
	
	public static void failMethod40() {
		// @security("Parameter stronger than allowed")
		lowLow$lowLow_S(lowLowSField, highHighSField);
	}
	
	public static void failMethod41() {
		// @security("Parameter stronger than allowed")
		lowLow$lowLow_S(lowHighSField, lowLowSField);
	}
	
	public static void failMethod42() {
		// @security("Parameter stronger than allowed")
		lowLow$lowLow_S(lowHighSField, lowHighSField);
	}
	
	public static void failMethod43() {
		// @security("Parameter stronger than allowed")
		lowLow$lowLow_S(lowHighSField, highHighSField);
	}

	public static void failMethod44() {
		// @security("Parameter stronger than allowed")
		lowLow$lowLow_S(highHighSField, lowLowSField);
	}
	
	public static void failMethod45() {
		// @security("Parameter stronger than allowed")
		lowLow$lowLow_S(highHighSField, lowHighSField);
	}
	
	public static void failMethod46() {
		// @security("Parameter stronger than allowed")
		lowLow$lowLow_S(highHighSField, highHighSField);
	}
	
	public static void failMethod47() {
		// @security("Parameter stronger than allowed")
		lowLow$lowHigh_S(lowLowSField, lowLowSField);
	}
	
	public static void failMethod48() {
		// @security("Parameter stronger than allowed")
		lowLow$lowHigh_S(lowLowSField, highHighSField);
	}
	
	public static void failMethod49() {
		// @security("Parameter stronger than allowed")
		lowLow$lowHigh_S(lowHighSField, highHighSField);
	}
	
	public static void failMethod50() {
		// @security("Parameter stronger than allowed")
		lowLow$lowHigh_S(highHighSField, highHighSField);
	}
	
	public static void failMethod51() {
		// @security("Parameter stronger than allowed")
		lowLow$lowHigh_S(lowHighSField, lowLowSField);
	}
	
	public static void failMethod52() {
		// @security("Parameter stronger than allowed")
		lowLow$lowHigh_S(highHighSField, lowLowSField);
	}
	
	public static void failMethod53() {
		// @security("Parameter stronger than allowed")
		lowLow$lowHigh_S(lowHighSField, lowHighSField);
	}
	
	public static void failMethod54() {
		// @security("Parameter stronger than allowed")
		lowLow$lowHigh_S(highHighSField, lowHighSField);
	}	
	
	public static void failMethod55() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowLow_S(lowLowSField, lowLowSField);
	}
	
	public static void failMethod56() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowLow_S(highHighSField, lowLowSField);
	}
	
	public static void failMethod57() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowLow_S(lowLowSField, lowHighSField);
	}
	
	public static void failMethod58() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowLow_S(lowHighSField, lowHighSField);
	}
		
	public static void failMethod59() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowLow_S(highHighSField, lowHighSField);
	}	
	
	public static void failMethod60() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowLow_S(lowLowSField, highHighSField);
	}	
	
	public static void failMethod61() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowLow_S(lowHighSField, highHighSField);
	}	
	
	public static void failMethod62() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowLow_S(highHighSField, highHighSField);
	}	
	
	public static void failMethod63() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowHigh_S(lowLowSField, lowLowSField);
	}	
	
	public static void failMethod64() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowHigh_S(lowLowSField, lowHighSField);
	}	
	
	public static void failMethod65() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowHigh_S(lowLowSField, highHighSField);
	}	
	
	public static void failMethod66() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowHigh_S(lowHighSField, lowLowSField);
	}
	
	public static void failMethod67() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowHigh_S(lowHighSField, highHighSField);
	}
	
	public static void failMethod68() {
		// @security("Parameter stronger than allowed")
		lowHigh$lowHigh_S(highHighSField, highHighSField);
	}
	
	public static void failMethod69() {
		// @security("Parameter stronger than allowed")
		lowHigh$highHigh_S(lowLowSField, lowLowSField);
	}
	
	public static void failMethod70() {
		// @security("Parameter stronger than allowed")
		lowHigh$highHigh_S(lowLowSField, lowHighSField);
	}
	
	public static void failMethod71() {
		// @security("Parameter stronger than allowed")
		lowHigh$highHigh_S(lowLowSField, highHighSField);
	}
	
	public static void failMethod72() {
		// @security("Parameter stronger than allowed")
		lowHigh$highHigh_S(lowHighSField, lowLowSField);
	}
	
	public static void failMethod73() {
		// @security("Parameter stronger than allowed")
		lowHigh$highHigh_S(highHighSField, lowLowSField);
	}
	
	public static void failMethod74() {
		// @security("Parameter stronger than allowed")
		lowHigh$highHigh_S(highHighSField, lowHighSField);
	}
	
	public static void failMethod75() {
		// @security("Parameter stronger than allowed")
		lowHigh$highHigh_S(highHighSField, highHighSField);
	}
		
	public static void failMethod76() {
		// @security("Parameter stronger than allowed")
		highHigh$lowHigh_S(lowLowSField,lowLowSField);
	}
	
	public static void failMethod77() {
		// @security("Parameter stronger than allowed")
		highHigh$lowHigh_S(lowHighSField,lowLowSField);
	}
	
	public static void failMethod78() {
		// @security("Parameter stronger than allowed")
		highHigh$lowHigh_S(highHighSField, lowLowSField);
	}
	
	public static void failMethod79() {
		// @security("Parameter stronger than allowed")
		highHigh$lowHigh_S(lowLowSField,lowHighSField);
	}
	
	public static void failMethod80() {
		// @security("Parameter stronger than allowed")
		highHigh$lowHigh_S(lowLowSField,highHighSField);
	}
	
	public static void failMethod81() {
		// @security("Parameter stronger than allowed")
		highHigh$lowHigh_S(lowHighSField,highHighSField);
	}
	
	public static void failMethod82() {
		// @security("Parameter stronger than allowed")
		highHigh$lowHigh_S( highHighSField, highHighSField);
	}
	
	public static void failMethod83() {
		// @security("Parameter stronger than allowed")
		highHigh$highHigh_S(lowLowSField, lowLowSField);
	}
	
	public static void failMethod84() {
		// @security("Parameter stronger than allowed")
		highHigh$highHigh_S(lowLowSField, lowHighSField);
	}
	
	public static void failMethod85() {
		// @security("Parameter stronger than allowed")
		highHigh$highHigh_S(lowHighSField, lowLowSField);
	}	
	
	@Constraints("low <= @return")
	public static int failMethod86() {
		// @security("Invalid lower bound of return")
		return sl0_sl0S(highSField);
	}

	@Constraints("low <= @return")
	public static int failMethod87() {
		// @security("Invalid lower bound of return")
		return sl0$sl1_sl0Xsl1S(highSField, lowSField);
	}

	@Constraints("low <= @return")
	public static int failMethod88() {
		// @security("Invalid lower bound of return")
		return sl0$sl1_sl0Xsl1S(lowSField, highSField);
	}

	@Constraints("low <= @return")
	public static int failMethod89() {
		// @security("Invalid lower bound of return")
		return sl0$sl1_sl0Xsl1S(highSField, highSField);
	}

	@Constraints({ "@0 <= high", "low <= @return" })
	public static int failMethod90(int a0) {
		// @security("return depends on argument")
		return a0;
	}

	@Constraints({ "@0 <= low", "@0[ = low", "low <= @return", "@return[ = high" })
	public static int[] failMethod91(int[] a0) {
		// @security("Invalid array reference")
		return a0;
	}
	
	@Constraints({ "@0 <= low", "@0[ = high", "low <= @return", "@return[ = low" })
	public static int[] failMethod92(int[] a0) {
		// @security("Invalid array reference")
		return a0;
	}
	
	@Constraints({ "@0 <= high", "@0[ = low", "low <= @return", "@return[ = low" })
	public static int[] failMethod93(int[] a0) {
		// @security("return depends on argument")
		return a0;
	}
	
	@SuppressWarnings("unused")
	@Constraints({ "@0 <= high", "low <= @return" })
	public static int failMethod94(int a0) {
		int i  = failMethod94(a0);
		// @security("return depends on argument")
		return a0;
	}
	
}
