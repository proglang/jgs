package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Obvious implicit information flow.
 * @author Nicolas Müller
 *
 */
public class ImplicitFlow2 {
	public static void main(String[] args) {
		int secret = DynamicLabel.makeHigh(42);
		if (secret > 0) {
			System.out.println(0);
		} else {
			System.out.println(1);
		}
	}
}
