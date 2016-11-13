package main.testclasses;

import utils.analyzer.HelperClass;

/**
 * Testing explicit information leak of a class. Was not implemented until
 * November 2016
 * 
 * @author Nicolas Müller
 *
 */
public class NewClassFail1 {
	public static void main(String[] args) {

		C b = new C();
		C c = HelperClass.makeHigh(b);
		System.out.println(c);

	}
}
