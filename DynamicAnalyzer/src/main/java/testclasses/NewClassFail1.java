package testclasses;

import testclasses.utils.C;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

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
		b = DynamicLabel.makeHigh(b);
		System.out.println(b);

	}
}
