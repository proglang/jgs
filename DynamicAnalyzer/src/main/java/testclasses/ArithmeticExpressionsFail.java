package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class ArithmeticExpressionsFail {
	
	/**
	 * @param args not used.
	 */
	public static void main(String[] args) {
		int res = DynamicLabel.makeHigh(3);
		if (res > 0) {
			res = (res + 1) * 42;
		}
		System.out.println(res);
	}
}
