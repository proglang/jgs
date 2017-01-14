package testclasses;

import utils.test.SimpleObject;

/**
 * Not working, because SimpleObject somehow does not get instrumented
 * @author Nicolas Müller
 *
 */
public class StaticMethodsFail {

	public static void main(String[] args) {
		int x = SimpleObject.returnHigh(3);
		System.out.println(x);
	}

}
