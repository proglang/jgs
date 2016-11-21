package main.testclasses;

import utils.HelperClass.HelperClass;

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
		x = HelperClass.makeHigh(x);
		while (x < 10) {
			y = x++;
		}
		return y;
	}

}
