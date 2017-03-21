package testclasses;

import testclasses.utils.C;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class NSU_FieldAccess4 {
	//static int f = 0;
	public static void main(String[] args) {

		C b = new C();
		C c = DynamicLabel.makeHigh(b);
		if (DynamicLabel.makeHigh(42) == 42) {
			update(c); // should throw an error, since globalPC is HIGH and c.f is LOW		
		}
	}
	
	private static void update(C c) {
		c.f = true;
	}
}
