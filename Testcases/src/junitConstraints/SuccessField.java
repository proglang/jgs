package junitConstraints;

import static security.Definition.*;

public class SuccessField {
	
	public static void main(String[] args) {}
	
	@FieldSecurity("low")
	public int lowIField;

	@FieldSecurity("low")
	public static int lowSField;

	@FieldSecurity("high")
	public int highIField;

	@FieldSecurity("high")
	public static int highSField;
	
	@Constraints("@pc <= low")
	public void successField1() {
		lowIField = mkLow(42);
	}
	
	@Constraints("@pc <= high")
	public void successField2() {
		highIField = mkLow(42);
	}
	
	@Constraints("@pc <= high")
	public void successField3() {
		highIField = mkHigh(42);
	}
	
	@Constraints("low <= @return")
	public int successField4() {
		return lowIField;
	}
	
	@Constraints("high <= @return")
	public int successField5() {
		return lowIField;
	}
	
	@Constraints("high <= @return")
	public int successField6() {
		return highIField;
	}
	
	@Constraints("@pc <= low")
	public void successField7() {
		lowSField = mkLow(42);
	}
	
	@Constraints("@pc <= high")
	public void successField8() {
		highSField = mkLow(42);
	}
	
	@Constraints("@pc <= high")
	public void successField9() {
		highSField = mkHigh(42);
	}
	
	@Constraints("low <= @return")
	public int successField10() {
		return lowSField;
	}
	
	@Constraints("high <= @return")
	public int successField11() {
		return lowSField;
	}
	
	@Constraints("high <= @return")
	public int successField12() {
		return highSField;
	}
	
}
