package testclasses;

import utils.analyzer.HelperClass;

/**
 * Obvious implicit information flow.
 * @author Nicolas Müller
 *
 */
public class ImplicitFlow2 {
	public static void main(String[] args) {
		int secret = HelperClass.makeHigh(42);
		if (secret > 0) {
			System.out.println(0);
		} else {
			System.out.println(1);
		}
	}
}
