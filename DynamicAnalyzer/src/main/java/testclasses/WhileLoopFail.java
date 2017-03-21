package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class WhileLoopFail {

	public static void main(String[] args) {
		simpleWhile(2);
	}
	
	/**
	 * Simple while-loop.
	 * @param x input
	 * @return output
	 */
	public static int simpleWhile(int x) {
		int y = 0;
		x = DynamicLabel.makeHigh(x);
		while (x < 10) {
			y = x++;
		}
		return y;
	}

}
