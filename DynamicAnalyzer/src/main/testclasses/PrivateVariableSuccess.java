package main.testclasses;

import utils.analyzer.HelperClass;

/**
 * Test that must pass. Information flows from high x to y, but
 * since y cannot be read anywhere, no information leak is present.
 * @author Nicolas Müller
 *
 */
public class PrivateVariableSuccess {
	public static void main(String[] args) {
		int x = HelperClass.makeHigh(3);
		int y;
		if (x > 0) { 
			y = x;
		} else {
			y = 42;
		}
	}
}
