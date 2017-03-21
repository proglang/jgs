package testclasses.utils;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;


/**
 * Does not get instrumented!
 * @author NicolasM
 */
public class simpleClassForTests {
	/**
	 * Returns a string with sec-value HIGH
	 * @return string with high-security value
	 */
	public String provideSecretString() {
		String s = DynamicLabel.makeHigh("i like donuts");
		return s;
	}
}

