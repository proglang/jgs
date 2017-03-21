package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Most basic case of implicit information flow from x to y.
 * Must throw IFCError on byte_b1 (compiler will use a byte for int y)
 * @author Nicolas Müller
 *
 */
public class ImplicitFlow1 {
	public static void main(String[] args) {
		int x = DynamicLabel.makeHigh(3);
		int y;
		if (x > 0) {
			y = 42;
		} else {
			y = 41;
		}
		System.out.println(y);
	}
}
