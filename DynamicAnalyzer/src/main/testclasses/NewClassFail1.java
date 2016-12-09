package main.testclasses;

import main.testclasses.utils.C;
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
		b = HelperClass.makeHigh(b);
		System.out.println(b);

	}
}
