package main.testclasses;

import utils.test.SimpleObject;

/**
 * @author Nicolas Müller
 *
 */
public class StaticMethodsFail {

	public static void main(String[] args) {
		int x = SimpleObject.returnHigh(3);
		System.out.println(x);
	}

}
