package utils.test;

import utils.analyzer.HelperClass;

/**
 * We want end-to-end tests with external classes, so here we manually instrument
 * an class, which will be used by the ExtClasses*.java end-to-end tests.
 * @author NicolasM
 *
 */
public class simpleClassForTests {
	public String provideSecretString() {
		String s = HelperClass.makeHigh("i like donuts");
		return s;
	}
}
