package exgradual;

import static security.Definition.*;

public class TestWhile {
	
	@FieldSecurity("low")
	public int low = 42;
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	public void test() {
		int v = mkLow(42);
		while (v > 21) {
			low = 23;
			v = mkHigh(42);
		}
	}
	
}
