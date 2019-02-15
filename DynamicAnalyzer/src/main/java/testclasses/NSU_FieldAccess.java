package testclasses;

import testclasses.utils.C;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class NSU_FieldAccess {
	//static int f = 0;
	public static void main(String[] args) {

		C b = new C();
		C c = DynamicLabel.makeHigh(b);
		c.f = true; // should throw an error, since we access f through
					// high-sec c and PC = LOW
	}
}
