package main.testclasses;

import utils.analyzer.HelperClass;

public class NSUPolicy4 {
	public static void main(String[] args) {

		C b = new C();
		C c = HelperClass.makeHigh(b);
		c.f = true; // should throw an error, since we access access f through
					// high-sec c and PC = LOW

	}
}
