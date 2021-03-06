package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Obvious implicit information flow.
 * @author Nicolas Müller
 *
 */
public class ImplicitFlow3 {
	public static void main(String[] args) {
		int secret = DynamicLabel.makeHigh(42);
		String message;
		if (secret > 0) {
			message = "Secret number is > 0";
			System.out.println(message);
		} else {
			message = "Secret number is <= 0";
			System.out.println(message);
		}
	}
}