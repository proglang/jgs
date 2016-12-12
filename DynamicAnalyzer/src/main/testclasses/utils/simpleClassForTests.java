package main.testclasses.utils;

import utils.analyzer.HelperClass;


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
		String s = HelperClass.makeHigh("i like donuts");
		return s;
	}
}

