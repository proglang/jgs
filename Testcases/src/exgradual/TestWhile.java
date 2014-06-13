package exgradual;

import static security.Definition.*;

public class TestWhile {

	public static void main(String[] args) {}


	// Crash
	@Constraints("high <= @pc")
	public void test1() {
		H.low = 5;
	}
	
	// run {pcs entfernen nach inequality, stack initialisieren mit Sig}
	public void test() {
		H.low = 5;
		if (mkHigh(true)) {
			H.high = 5;
		}
	}

}
